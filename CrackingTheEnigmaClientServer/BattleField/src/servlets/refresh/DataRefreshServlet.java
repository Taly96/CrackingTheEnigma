package servlets.refresh;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.enums.Enums;

import java.io.IOException;
import java.io.PrintWriter;

import static utils.ServletUtils.BATTLE_FIELD;
import static utils.ServletUtils.DATA;

@WebServlet("/refresh")
public class DataRefreshServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String battleFieldName =req.getParameter(BATTLE_FIELD);
        String dataType = req.getParameter(DATA);
        String json = this.processRequest(battleFieldName, dataType);
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        out.println(json);
        out.flush();
    }

    private String processRequest(String battleFieldName, String dataType) {
        String res = null;

        switch (Enums.DataTypes.valueOf(dataType)){
            case ACTIVE_TEAMS:{
                res = this.refreshActiveTeams(battleFieldName);
                break;
            }
            case CANDIDATES:{
                res = this.refreshCandidates(battleFieldName);
            }
            default:{
                break;
            }
        }

        return res;
    }

    private String refreshCandidates(String battleFieldName) {//todo
        return null;
    }

    private String refreshActiveTeams(String battleFieldName) {//todo
        return null;
        
    }
}
