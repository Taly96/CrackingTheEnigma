package servlets.codeconfig;

import com.google.gson.Gson;
import dto.codeconfig.CodeConfigDTO;
import enigma.managers.BattleFieldManager;
import enigma.managers.MachineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import static utils.ServletUtils.BATTLE_FIELD;

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
        CodeConfigDTO inputConfig = gson.fromJson(json, CodeConfigDTO.class);
        BattleFieldManager battleFieldManager = ServletUtils.getBattleFieldManager(getServletContext());
        CodeConfigDTO currentCodeConfig = battleFieldManager.getBattleField(battleField).setCodeConfig(inputConfig);
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
        CodeConfigDTO currentCodeConfig = battleFieldManager.getBattleField(battleField).generateCodeConfig();
        String json = gson.toJson(currentCodeConfig);
        out.println(json);
        out.flush();
    }
}
