package server.servlets.candidates;

import dto.candidates.CandidatesDTO;
import engine.managers.AlliesManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.SessionUtils;

import java.io.IOException;
import java.util.Properties;

import static server.utils.Constants.CANDIDATES;
import static server.utils.ServletUtils.*;

@WebServlet("/update-candidates")
public class CandidatesUpdateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String allyName = SessionUtils.getAllyName(req);
        if(allyName != null && !allyName.isEmpty()){
            String agentName = SessionUtils.getUsername(req);
            if(agentName != null && !agentName.isEmpty()){
                String battleName =
                        SessionUtils.getBattleFieldName(req);
                if(battleName != null && !battleName.isEmpty()) {
                    Properties properties = new Properties();
                    properties.load(req.getInputStream());
                    CandidatesDTO candidatesDTO =
                            GSON_INSTANCE.fromJson(
                                    properties.getProperty(CANDIDATES),
                                    CandidatesDTO.class
                            );
                    AlliesManager alliesManager =
                            getAlliesManager(getServletContext());
                    if (candidatesDTO != null) {
                        synchronized (this) {
                            resp.setStatus(HttpServletResponse.SC_OK);
                            alliesManager.updateCandidates(allyName, agentName, candidatesDTO);
                        }
                    }
                    else{
                        resp.setContentType("text/plain");
                        resp.setStatus(HttpServletResponse.SC_CONFLICT);
                        resp.getOutputStream().print("Cant' update candidates, no candidates.");
                    }
                }
                else{
                    resp.setContentType("text/plain");
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                    resp.getOutputStream().print("Cant' update candidates, battle name.");
                }
            }
            else{
                resp.setContentType("text/plain");
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getOutputStream().print("Cant' update candidates, no agent name.");
            }
        }
        else{
            resp.setContentType("text/plain");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().print("Cant' update candidates, no ally name.");
        }
        resp.flushBuffer();
    }
}
