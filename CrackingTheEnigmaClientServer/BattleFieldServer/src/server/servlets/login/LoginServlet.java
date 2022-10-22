package server.servlets.login;

import dto.activeteams.AlliesInfo;
import dto.agents.AgentsInfo;
import engine.managers.AlliesManager;
import engine.managers.UsersManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import java.io.IOException;
import java.util.Properties;

import static server.utils.Constants.*;
import static server.utils.ServletUtils.GSON_INSTANCE;
import static server.utils.SessionUtils.ALLY_NAME;
import static server.utils.SessionUtils.USERNAME;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        if (usernameFromSession == null) {
            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getOutputStream().print("Please enter a valid user name.");
            } else {
                String typeFromParameter = request.getParameter("type");
                if(typeFromParameter == null || typeFromParameter.isEmpty()){
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getOutputStream().print("Couldn't recognize type of user.");
                }
                else {
                    switch(typeFromParameter){
                        case UBOAT:
                            this.loginUBoat(request, response, usernameFromParameter, typeFromParameter);
                            break;
                        case ALLY:{
                            this.loginAlly(request, response, usernameFromParameter, typeFromParameter);
                            break;
                        }
                        case AGENT:{
                            this.loginAgent(request, response, usernameFromParameter, typeFromParameter);
                            break;
                        }
                        default:
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                            response.getOutputStream().print("Couldn't recognize type of user.");
                            break;
                    }

                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        response.flushBuffer();
    }

    private void loginAgent(HttpServletRequest request, HttpServletResponse response, String usernameFromParameter, String typeFromParameter) throws IOException {
        UsersManager userManager = ServletUtils.getUsersManager(getServletContext());
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        usernameFromParameter = usernameFromParameter.trim();
        synchronized (this) {
            if (userManager.isUserExists(usernameFromParameter)) {
                String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print(errorMessage);
            } else {
                userManager.addUser(usernameFromParameter);
                Properties prop = new Properties();
                prop.load(request.getInputStream());
                AgentsInfo agent = GSON_INSTANCE.fromJson(prop.getProperty("agent"), AgentsInfo.class);
                if(agent != null){
                    alliesManager.addAgent(agent);
                    request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
                    request.getSession(false).setAttribute(ALLY_NAME, agent.getAlliesTeam());
                    request.getSession(false).setAttribute(TYPE, typeFromParameter);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else{
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getOutputStream().print("Agent is null.");
                }
            }
        }
    }

    private void loginUBoat(HttpServletRequest request, HttpServletResponse response, String usernameFromParameter, String typeFromParameter) throws IOException {
        UsersManager userManager = ServletUtils.getUsersManager(getServletContext());
        usernameFromParameter = usernameFromParameter.trim();
        synchronized (this) {
            if (userManager.isUserExists(usernameFromParameter)) {
                String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print(errorMessage);
            } else {
                userManager.addUser(usernameFromParameter);
                request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
                request.getSession(false).setAttribute(TYPE, typeFromParameter);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    private void loginAlly(HttpServletRequest request, HttpServletResponse response, String usernameFromParameter, String typeFromParameter) throws IOException {
        UsersManager userManager = ServletUtils.getUsersManager(getServletContext());
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        usernameFromParameter = usernameFromParameter.trim();
        synchronized (this) {
            if (userManager.isUserExists(usernameFromParameter)) {
                String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print(errorMessage);
            } else {
                userManager.addUser(usernameFromParameter);
                alliesManager.addAlly(usernameFromParameter);
                request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
                request.getSession(false).setAttribute(TYPE, typeFromParameter);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
}
