package servlets.process;

import com.google.gson.Gson;
import enigma.managers.BattleFieldManager;
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

@WebServlet("/process")
public class ProcessMessageServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setStatus(HttpServletResponse.SC_OK);
        Properties prop = new Properties();
        prop.load(req.getInputStream());
        String json = prop.getProperty("message");
        Gson gson = new Gson();
        String battleFieldName = req.getParameter(BATTLE_FIELD);
        String messageToProcess = gson.fromJson(json, String.class);
        BattleFieldManager battleFieldManager = ServletUtils.getBattleFieldManager(getServletContext());
        String processedMessage =
                battleFieldManager
                        .getBattleField(battleFieldName)
                        .processMessage(messageToProcess);
        json = gson.toJson(processedMessage);
        resp.setContentType("application/json");
        out.println(json);
        out.flush();
    }
}
