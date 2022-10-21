package server.servlets;

import dto.activeteams.AlliesDTO;
import dto.agents.AgentsDTO;
import engine.managers.AlliesManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.SessionUtils;

import java.io.IOException;
import java.util.Properties;

import static server.utils.ServletUtils.GSON_INSTANCE;

@WebServlet("/test")
public class test extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties properties = new Properties();
        properties.load(req.getInputStream());
        AgentsDTO agentsDTO = GSON_INSTANCE.fromJson(properties.getProperty("agents"), AgentsDTO.class);
        getServletContext().setAttribute("agents", agentsDTO);
        if(agentsDTO != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        else{
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        resp.flushBuffer();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AgentsDTO agentsDTO = (AgentsDTO) getServletContext().getAttribute("agents");
        String json = GSON_INSTANCE.toJson(agentsDTO);
        resp.getOutputStream().print(json);
        resp.flushBuffer();
    }
}
