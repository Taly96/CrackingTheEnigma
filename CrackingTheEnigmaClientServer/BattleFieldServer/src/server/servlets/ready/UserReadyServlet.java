package server.servlets.ready;

import dto.battlefield.BattleFieldInfo;
import dto.decipher.OriginalInformation;
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
        String battleName = GSON_INSTANCE.fromJson(prop.getProperty(BATTLE), String.class);
        if (battleName != null) {
            String allyName = SessionUtils.getUsername(req);
            AlliesManager alliesManager = getAlliesManager(getServletContext());
            BattleFieldManager battleFieldManager = getBattleFieldManager(getServletContext());
            synchronized (this) {
                alliesManager.updateAllyInfo(allyName, battleName);
                BattleFieldInfo battleFieldInfo = battleFieldManager.getBattleFieldInfo(battleName);
                if(battleFieldInfo.incrementAllies()){
                    req.getSession(false).setAttribute(BATTLE, battleName);
                    String json = GSON_INSTANCE.toJson(battleFieldInfo);
                    resp.getOutputStream().print(json);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.setContentType("json/application");
                }
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
            String processedMessage
                    = GSON_INSTANCE.fromJson(
                            prop.getProperty("processed"),
                            String.class
            );
            if (processedMessage == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("text/plain;charset=UTF-8");
                resp.getOutputStream().print("Can't start a contest without processed message.");
            } else {
                BattleFieldManager battleFieldManager = getBattleFieldManager(getServletContext());
                resp.setStatus(HttpServletResponse.SC_OK);
                synchronized (this) {
                    battleFieldManager.assembleContest(battleName, processedMessage);
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
                        battleFieldManager.setAllyReadyForContest(battleName, allyName, assignmentSize);
                        isValidReq = true;
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
