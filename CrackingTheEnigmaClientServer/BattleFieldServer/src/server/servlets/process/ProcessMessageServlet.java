package server.servlets.process;

import com.google.gson.Gson;
import engine.managers.BattleFieldManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import static server.utils.ServletUtils.GSON_INSTANCE;

@WebServlet("/process")
public class ProcessMessageServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String battleName = SessionUtils.getBattleFieldName(req);
        if(battleName == null || battleName.isEmpty()){
            resp.setContentType("text/plain");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().print("Can't find battle name.");
        }
        else{
            Properties prop = new Properties();
            prop.load(req.getInputStream());
            String messageToProcess =
                    GSON_INSTANCE.fromJson(
                            prop.getProperty("message"),
                            String.class
                    );
            if(messageToProcess == null || messageToProcess.isEmpty()){
                resp.setContentType("text/plain");
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getOutputStream().print("Empty message sent.");
            }
            else{
                BattleFieldManager battleFieldManager = ServletUtils.getBattleFieldManager(getServletContext());
                synchronized (this){
                    String processedMessage =
                            battleFieldManager.processMessage(battleName, messageToProcess);
                    resp.getOutputStream().print(
                            GSON_INSTANCE.toJson(processedMessage)
                    );
                }
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
            }
        }
        resp.flushBuffer();
    }
}
