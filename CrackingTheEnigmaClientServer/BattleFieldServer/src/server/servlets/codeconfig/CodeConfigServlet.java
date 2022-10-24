package server.servlets.codeconfig;

import dto.codeconfig.CodeConfigInfo;
import engine.managers.BattleFieldManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import java.io.IOException;
import java.util.Properties;

import static server.utils.ServletUtils.GSON_INSTANCE;
import static server.utils.ServletUtils.getBattleFieldManager;

@WebServlet("/code")
public class CodeConfigServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String battleName = SessionUtils.getBattleFieldName(req);
        if(battleName == null || battleName.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().print("Can't find battle name.");
        }
        else{
            Properties prop = new Properties();
            prop.load(req.getInputStream());
            CodeConfigInfo inputConfig =
                    GSON_INSTANCE.fromJson(
                            prop.getProperty("code")
                            , CodeConfigInfo.class
                    );
            BattleFieldManager battleFieldManager = ServletUtils.getBattleFieldManager(getServletContext());
            synchronized (this){
                CodeConfigInfo currentCodeConfig =
                        battleFieldManager.setCodeConfig(battleName, inputConfig);
                resp.getOutputStream().print(
                        GSON_INSTANCE.toJson(currentCodeConfig)
                );
            }
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        resp.flushBuffer();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String battleName = SessionUtils.getBattleFieldName(req);
        if(battleName == null || battleName.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().print("Can't find battle name.");
        }
        else{
            BattleFieldManager battleFieldManager = ServletUtils.getBattleFieldManager(getServletContext());
            synchronized (this){
                CodeConfigInfo currentCodeConfig =
                        battleFieldManager.generateCodeConfig(battleName);
                resp.getOutputStream().print(
                        GSON_INSTANCE.toJson(currentCodeConfig)
                );
            }
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        resp.flushBuffer();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String battleName =
                SessionUtils.getBattleFieldName(req);
        if(battleName != null && !battleName.isEmpty()){
            BattleFieldManager battleFieldManager =
                    getBattleFieldManager(getServletContext());
            synchronized (this) {
                CodeConfigInfo currenCode =
                        battleFieldManager.resetCodeConfig(battleName);
                if(currenCode != null){
                    String json =
                            GSON_INSTANCE.toJson(currenCode);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.setContentType("json/application");
                    resp.getOutputStream().print(json);
                }
                else{
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                    resp.setContentType("text/plain");
                    resp.getOutputStream().print("Couldn't reset code. Code configuration is null.");
                }
            }
        }
        else {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.setContentType("text/plain");
            resp.getOutputStream().print("Couldn't reset code. BattleField name is null");
        }
        resp.flushBuffer();
    }
}
