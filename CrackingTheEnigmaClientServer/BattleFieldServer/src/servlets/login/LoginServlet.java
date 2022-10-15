package servlets.login;

import enigma.managers.UsersManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static utils.SessionUtils.USERNAME;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        UsersManager userManager = ServletUtils.getUsersManager(getServletContext());
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
                    usernameFromParameter = usernameFromParameter.trim();
                    synchronized (this) {
                        if (userManager.isUserExists(usernameFromParameter)) {
                            String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getOutputStream().print(errorMessage);
                        } else {
                            userManager.addUser(usernameFromParameter);
                            request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
                            request.getSession(false).setAttribute("type", typeFromParameter);
                            response.setStatus(HttpServletResponse.SC_OK);
                        }
                    }
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
