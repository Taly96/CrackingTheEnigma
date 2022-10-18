package server.servlets.codeconfig;

import com.google.gson.Gson;
import dto.codeconfig.CodeConfigInfo;
import engine.managers.BattleFieldManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import static server.utils.ServletUtils.BATTLE_FIELD;

@WebServlet("/code")
public class CodeConfigServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String battleField = req.getParameter(BATTLE_FIELD);
        PrintWriter out = resp.getWriter();
        resp.setStatus(HttpServletResponse.SC_OK);
        Properties prop = new Properties();
        prop.load(req.getInputStream());
        String json = prop.getProperty("code");
        Gson gson = new Gson();
        CodeConfigInfo inputConfig = gson.fromJson(json, CodeConfigInfo.class);
        BattleFieldManager battleFieldManager = ServletUtils.getBattleFieldManager(getServletContext());
        CodeConfigInfo currentCodeConfig =
                battleFieldManager
                        .getBattleField(battleField)
                        .getMachineManager()
                        .setCodeConfig(inputConfig);
        json = gson.toJson(currentCodeConfig);
        out.println(json);
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String battleField = req.getParameter(BATTLE_FIELD);
        PrintWriter out = resp.getWriter();
        resp.setStatus(HttpServletResponse.SC_OK);
        BattleFieldManager battleFieldManager = ServletUtils.getBattleFieldManager(getServletContext());
        Gson gson = new Gson();
        CodeConfigInfo currentCodeConfig =
                battleFieldManager
                        .getBattleField(battleField)
                        .getMachineManager()
                        .generateCodeConfig();
        String json = gson.toJson(currentCodeConfig);
        out.println(json);
        out.flush();
    }
}
