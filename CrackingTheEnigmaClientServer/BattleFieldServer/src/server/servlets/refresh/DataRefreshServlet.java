package server.servlets.refresh;

import com.google.gson.Gson;
import dto.activeteams.AlliesDTO;
import dto.battlefield.BattleFieldDTO;
import dto.candidates.CandidatesDTO;
import engine.managers.BattleField;
import engine.managers.BattleFieldManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.enums.Enums;
import server.utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static server.utils.Constants.DATA;
import static server.utils.ServletUtils.*;

@WebServlet("/refresh")
public class DataRefreshServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String battleFieldName = SessionUtils.getBattleFieldName(req);
        if(battleFieldName == null){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getOutputStream().print("Request to refresh data made by unauthorized user.");
            resp.flushBuffer();
        }
        else{
            String dataType = req.getParameter(DATA);
            String json = this.processRequest(battleFieldName, dataType);
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            out.println(json);
            out.flush();
        }
    }

    private String processRequest(String battleFieldName, String dataType) {
        String res = null;

        switch (Enums.DataTypes.valueOf(dataType)){
            case ALLIES:{
                //res = this.refreshAllies(battleFieldName);
                break;
            }
            case CANDIDATES:{
                //res = this.refreshCandidates(battleFieldName);
                break;
            }
            case BATTLE_FIELDS:{
                res = this.refreshBattleFields();
                break;
            }
            default:{
                //todo write error response
                break;
            }
        }

        return res;
    }

    private String refreshBattleFields() {

        String json = null;
        synchronized (this){
            BattleFieldManager battleField = getBattleFieldManager(getServletContext());
            BattleFieldDTO refreshedData = battleField.refreshBattleFields();
            Gson gson = new Gson();
            json = gson.toJson(refreshedData);
        }

        return json;
    }

//    private String refreshCandidates(String battleFieldName) { //todo
//        String json = null;
//        synchronized (this){
//            BattleField  battleField = getBattleFieldManager(getServletContext()).getBattleField(battleFieldName);
//            CandidatesDTO refreshedData = battleField.refreshCandidates();
//            Gson gson = new Gson();
//            json = gson.toJson(refreshedData);
//        }
//
//        return json;
//    }
//
//    private String refreshAllies(String battleFieldName) {//todo
//        String json = null;
//        synchronized (this) {
//            BattleField battleField = getBattleFieldManager(getServletContext()).getBattleField(battleFieldName);
//            AlliesDTO refreshedData = battleField.refreshAllies();
//            Gson gson = new Gson();
//            json = gson.toJson(refreshedData);
//        }
//
//        return json;
//    }
}
