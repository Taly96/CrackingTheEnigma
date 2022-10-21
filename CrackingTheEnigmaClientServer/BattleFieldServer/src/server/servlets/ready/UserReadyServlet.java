package server.servlets.ready;

import dto.activeteams.AlliesInfo;
import dto.battlefield.BattleFieldInfo;
import engine.managers.AlliesManager;
import engine.managers.BattleFieldManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.SessionUtils;

import java.io.IOException;
import java.util.Properties;

import static server.utils.Constants.*;
import static server.utils.ServletUtils.*;

@WebServlet("/ready")
public class UserReadyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userNameFromSession = SessionUtils.getUsername(req);
        if (userNameFromSession == null || userNameFromSession.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().print("No valid user name.");
        } else {
            String userType = SessionUtils.getType(req);
            if (userType == null || userType.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getOutputStream().print("No user type found.");
            } else {
                Properties prop = new Properties();
                prop.load(req.getInputStream());
                switch (userType) {
                    case UBOAT:
                        this.setUBoatReady(prop, req, resp);
                        break;
                    case ALLY:
                        this.setAllyReady(prop, req, resp);
                        break;
                    default:
                        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        resp.getOutputStream().print("Cant recognize user type.");
                        break;
                }
            }
        }
        resp.flushBuffer();
    }

    private void setAllyReady(Properties prop, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AlliesInfo newAlly = GSON_INSTANCE.fromJson(prop.getProperty(ALLY), AlliesInfo.class);
        if (newAlly != null) {
            AlliesManager alliesManager = getAlliesManager(getServletContext());
            BattleFieldManager battleFieldManager = getBattleFieldManager(getServletContext());
            synchronized (this) {
                alliesManager.updateAllyInfo(newAlly);
                BattleFieldInfo battleFieldInfo = battleFieldManager.getBattleFieldInfo(newAlly.getBattleName());
                battleFieldInfo.incrementAllies();
                req.getSession(false).setAttribute(BATTLE, newAlly.getBattleName());
                String json = GSON_INSTANCE.toJson(battleFieldInfo);
                resp.getOutputStream().print(json);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("json/application");
            }
        } else {
            resp.getOutputStream().print("No ally added.");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.setContentType("text/plain");
        }
    }

    private void setUBoatReady(Properties prop, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String battleName = SessionUtils.getBattleFieldName(req);
        if (battleName == null || battleName.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().print("No battlefield name found.");
        } else {
            String messageToEncrypt = GSON_INSTANCE.fromJson(prop.getProperty("encrypt"), String.class);
            if (messageToEncrypt == null || messageToEncrypt.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("text/plain;charset=UTF-8");
                resp.getOutputStream().print("Can't start a contest with an empty message to encrypt.");
            } else {
                BattleFieldManager battleFieldManager = getBattleFieldManager(getServletContext());
                resp.setStatus(HttpServletResponse.SC_OK);
                synchronized (this) {
                    battleFieldManager.assembleContest(battleName, messageToEncrypt);
                }
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String battleName = SessionUtils.getBattleFieldName(req);
        boolean isValidReq = false;
        if (battleName != null && !battleName.isEmpty()) {
            Properties properties = new Properties();
            properties.load(req.getInputStream());
            String assignmentSize = GSON_INSTANCE.fromJson(properties.getProperty("assignment"), String.class);
            if (assignmentSize != null && !assignmentSize.isEmpty()) {
                String allyName = SessionUtils.getUsername(req);
                if (allyName != null && !allyName.isEmpty()) {
                    BattleFieldManager battleFieldManager = getBattleFieldManager(getServletContext());
                    AlliesManager alliesManager = getAlliesManager(getServletContext());
                    synchronized (this) {
                        isValidReq = true;
                        battleFieldManager.setAllyReadyForContest(battleName, allyName, assignmentSize);
                        alliesManager.setAllyReadyForContest(allyName, assignmentSize);
                        resp.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
            if (!isValidReq) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getOutputStream().print("Couldn't set ally ready for contest.");
                resp.setContentType("text/plain");
            }
        }
    }
}
