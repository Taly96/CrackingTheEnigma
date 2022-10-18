package server.servlets.ready;

import com.google.gson.Gson;
import dto.activeteams.AlliesInfo;
import dto.battlefield.BattleFieldInfo;
import engine.managers.BattleField;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import java.io.IOException;
import java.util.Properties;

import static server.utils.Constants.ALLY;
import static server.utils.Constants.UBOAT;
import static server.utils.ServletUtils.GSON_INSTANCE;

@WebServlet("/ready")
public class UserReadyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userNameFromSession = SessionUtils.getUsername(req);
        if(userNameFromSession == null || userNameFromSession.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().print("No valid user name.");
        }
        else{
            String battleName = SessionUtils.getBattleFieldName(req);
            if(battleName == null || battleName.isEmpty()){
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getOutputStream().print("No battlefield name found.");
            }
            else{
                String userType = SessionUtils.getType(req);
                if(userType == null || userType.isEmpty()){
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                    resp.getOutputStream().print("No user type found.");
                }
                else {
                    Properties prop = new Properties();
                    prop.load(req.getInputStream());
                    switch (userType){
                        case UBOAT:
                            this.setUBoatReady(prop,battleName, resp);
                            break;
                        case ALLY:
                            this.setAllyReady(prop, battleName, resp);
                            break;
                        default:
                            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            resp.getOutputStream().print("Cant recognize user type.");
                            break;
                    }
                    resp.flushBuffer();
                }
            }
        }
        resp.flushBuffer();
    }

    private void setAllyReady(Properties prop, String battleName, HttpServletResponse resp) throws IOException {

    }

    private void setUBoatReady(Properties prop, String battleName, HttpServletResponse resp) throws IOException {
        BattleField battleField = ServletUtils.getBattleFieldManager(getServletContext()).getBattleField(battleName);
        String messageToEncrypt = GSON_INSTANCE.fromJson(prop.getProperty("encrypt"), String.class);
        if(messageToEncrypt == null || messageToEncrypt.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/plain;charset=UTF-8");
            resp.getOutputStream().print("Can't start a contest with an empty message to encrypt.");
        }
        else{
            resp.setStatus(HttpServletResponse.SC_OK);
            synchronized (this){
                battleField.startContest(messageToEncrypt);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String battleName = SessionUtils.getBattleFieldName(req);
        BattleField battleField = ServletUtils.getBattleFieldManager(getServletContext()).getBattleField(battleName);
//        battleField.getDecipherManager().startProducingAssignments(
//                new BigDecimal(27000),
//                new BigDecimal(70),
//                battleField.getMachineManager().getDecipherDTO()
//        );
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
