package servlets.ready;

import com.google.gson.Gson;
import dto.activeteams.AlliesInfo;
import dto.battlefield.BattleFieldInfo;
import enigma.managers.BattleField;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

@WebServlet("/ally-ready")
public class AlliesReadyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userNameFromParameter = req.getParameter("username");
        if(userNameFromParameter.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getOutputStream().print("Empty user name entered");
        }
        else{
            String battleName = req.getParameter("battle");
            if(battleName.isEmpty()){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getOutputStream().print("Empty battle name used");
            }
            else{
                BattleField battleField = ServletUtils.getBattleFieldManager(getServletContext()).getBattleField(battleName);
                if(battleField == null){
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getOutputStream().print("Couldn't recognize battle name.");
                }
                else {
                    Properties prop = new Properties();
                    prop.load(req.getInputStream());
                    AlliesInfo newAlly = new Gson().fromJson(prop.getProperty("ally"), AlliesInfo.class);
                    if(newAlly != null) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.setContentType("application/json");
                        synchronized (this) {
                            req.getSession(true).setAttribute(userNameFromParameter, battleName);
                            battleField.addAlly(newAlly);
                            BattleFieldInfo info = battleField.getBattleFieldInfo();
                            String json = new Gson().toJson(info);
                            resp.getOutputStream().print(json);
                        }
                    }
                    else{
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getOutputStream().print("Couldn't load ally from request body.");
                    }
                }
            }
        }
        resp.flushBuffer();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String battleName = SessionUtils.getBattleFieldName(req);
        BattleField battleField = ServletUtils.getBattleFieldManager(getServletContext()).getBattleField(battleName);
        battleField.getDecipherManager().startProducingAssignments(
                new BigDecimal(27000),
                new BigDecimal(70),
                battleField.getMachineManager().getDecipherDTO()
        );
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
