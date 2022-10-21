package server.servlets.refresh;

import dto.activeteams.AlliesDTO;
import dto.activeteams.AlliesInfo;
import dto.agents.AgentsDTO;
import dto.battlefield.BattleFieldDTO;
import dto.battlefield.BattleFieldInfo;
import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesDTOList;
import engine.managers.AlliesManager;
import engine.managers.BattleFieldManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.enums.Enums;
import server.utils.SessionUtils;

import java.io.IOException;
import java.util.List;

import static server.utils.Constants.*;
import static server.utils.Constants.ALLY;
import static server.utils.ServletUtils.*;

@WebServlet("/refresh")
public class DataRefreshServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dataType = req.getParameter(DATA);
        if(dataType != null && !dataType.isEmpty()){
            String json = this.processRequest(req, dataType);
            if(json != null){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getOutputStream().print(json);
                resp.setContentType("application/json");

            }else{
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getOutputStream().print("Couldn't refresh data.");
                resp.setContentType("text/plain");
            }
        }
        else{
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().print("Couldn't refresh data.");
            resp.setContentType("text/plain");
        }
        resp.flushBuffer();
    }

    private String processRequest(HttpServletRequest req, String dataType) {
        String res = null;

        switch (dataType){
            case ALLIES:{
                res = this.refreshAllies(req);
                break;
            }
            case ALLY:{
                res = this.refreshAlly(req);
                break;
            }
            case CANDIDATES:{
                res = this.refreshCandidates(req);
                break;
            }
            case BATTLES:{
                res = this.refreshBattleFields();
                break;
            }
            case BATTLE:{
                res = this.refreshBattle(req);
                break;
            }
            case AGENTS: {
                res = this.refreshAgents(req);
                break;
            }
            default:{
                break;
            }
        }

        return res;
    }

    private String refreshAgents(HttpServletRequest req) {
        String allyName = SessionUtils.getUsername(req);
        String json = null;
        AlliesManager alliesManager =
                getAlliesManager(getServletContext());
        if(allyName != null && !allyName.isEmpty()){
            AgentsDTO agentsDTO =
                    alliesManager.refreshAgents(allyName);
            json = GSON_INSTANCE.toJson(agentsDTO);
        }

        return json;
    }

    private String refreshAlly(HttpServletRequest req) {
        String json = null;
        String allyName = SessionUtils.getUsername(req);
        if(allyName != null && !allyName.isEmpty()){
            AlliesManager alliesManager =
                    getAlliesManager(getServletContext());
            synchronized (this){
                AlliesInfo alliesInfo  =
                        alliesManager.refreshAlly(allyName);
                json = GSON_INSTANCE.toJson(alliesInfo);
            }
        }

        return json;
    }

    private String refreshBattle(HttpServletRequest req) {
        String json = null;
        String battleName =
                SessionUtils.getBattleFieldName(req);
        if(battleName != null && !battleName.isEmpty()){
            BattleFieldManager battleFieldManager =
                    getBattleFieldManager(getServletContext());
            synchronized (this){
                BattleFieldInfo battleFieldInfo =
                        battleFieldManager.getBattleFieldInfo(battleName);
                json = GSON_INSTANCE.toJson(battleFieldInfo);
            }
        }

        return json;
    }

    private String refreshAllies(HttpServletRequest req) {
        String json = null;
        String battleFieldName = SessionUtils.getBattleFieldName(req);
        if(battleFieldName != null && !battleFieldName.isEmpty()){
            AlliesManager alliesManager = getAlliesManager(getServletContext());
            synchronized (this){
                AlliesDTO alliesDTO =
                        alliesManager.refreshAllAllies(battleFieldName);
                json = GSON_INSTANCE.toJson(alliesDTO);
            }
        }

        return json;
    }

    private String refreshBattleFields() {
        BattleFieldManager battleFieldManager =
                getBattleFieldManager(getServletContext());
        String json = null;
        synchronized (this){
            BattleFieldDTO battleFieldDTO =
                    battleFieldManager.refreshBattleFields();
            json = GSON_INSTANCE.toJson(battleFieldDTO);
        }

        return json;
    }

    private String refreshCandidates(HttpServletRequest req) {
        String json = null;
        String userType =
                SessionUtils.getType(req);
        if(userType != null && !userType.isEmpty()){
            String battleFieldName =
                    SessionUtils.getBattleFieldName(req);
            if(battleFieldName != null && !battleFieldName.isEmpty()){
                AlliesManager alliesManager =
                        getAlliesManager(getServletContext());
                if(userType.equals(UBOAT)){
                    synchronized (this){
                        CandidatesDTOList info = new CandidatesDTOList(
                                alliesManager.refreshAllCandidates(battleFieldName));
                        json = GSON_INSTANCE.toJson(info);
                    }
                }
                else if (userType.equals(ALLY)){
                    String allyName = SessionUtils.getUsername(req);
                    if(allyName != null && !allyName.isEmpty()){
                        synchronized (this){
                            CandidatesDTO candidatesDTO =
                                    alliesManager.refreshCandidates(allyName);
                            json = GSON_INSTANCE.toJson(candidatesDTO);
                        }
                    }
                }
            }
        }

        return json;
    }
}
