package server.servlets.refresh;

import dto.activeteams.AlliesDTO;
import dto.activeteams.AlliesInfo;
import dto.agents.AgentsDTO;
import dto.assignment.AssignmentDTOList;
import dto.battlefield.BattleFieldDTO;
import dto.battlefield.BattleFieldInfo;
import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesDTOList;
import dto.versions.AlliesVersions;
import engine.managers.AlliesManager;
import engine.managers.Ally;
import engine.managers.BattleFieldManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.SessionUtils;

import java.io.IOException;
import java.util.Properties;

import static server.utils.Constants.*;
import static server.utils.Constants.ALLY;
import static server.utils.ServletUtils.*;
import static server.utils.SessionUtils.TYPE;

@WebServlet("/refresh")
public class DataRefreshServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String dataType = req.getParameter(DATA);
        if(dataType != null && !dataType.isEmpty()){
            String json = this.processRequest(req, dataType);
            if(json != null){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getOutputStream().print(json);
                resp.setContentType("application/json");

            }else{
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getOutputStream().print("Couldn't refresh " + dataType + ".");
                resp.setContentType("text/plain");
            }
        }
        else{
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().print("Couldn't refresh data, no data type found.");
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
                res = this.refreshAllBattleFields();
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
            case ASSIGNMENTS: {
                res = this.refreshAssignments(req);
                break;
            }
            case MACHINE_INVENTORY:{
                res = refreshInventory(req);
                break;
            }
            default:{
                break;
            }
        }

        return res;
    }

    private String refreshInventory(HttpServletRequest req) {
        String battleFiledName =
                SessionUtils.getBattleFieldName(req);
        String json = null;
        if(battleFiledName != null && !battleFiledName.isEmpty()){
            BattleFieldManager battleFieldManager =
                    getBattleFieldManager(getServletContext());
            synchronized (this){
                byte[] inventory =
                        battleFieldManager.getSerMachineInventory(battleFiledName);

                if(inventory != null){
                    json = GSON_INSTANCE.toJson(inventory);
                }
            }

        }

        return json;
    }

    private String refreshAssignments(HttpServletRequest req) {
        System.out.println("Refreshing assignments");
        String allyName =
                SessionUtils.getAllyName(req);
        String json = null;
        if(allyName != null && !allyName.isEmpty()){
            String battleName =
                    SessionUtils.getBattleFieldName(req);
            if(battleName != null && !battleName.isEmpty()) {
                synchronized (this) {
                    Integer numOfAssignmentsPerDraw =
                            SessionUtils.getNumOfAssignmentsPerDraw(req);
                    if (numOfAssignmentsPerDraw != null && numOfAssignmentsPerDraw != 0) {
                        BattleFieldManager battleFieldManager =
                                getBattleFieldManager(getServletContext());
                        AssignmentDTOList assignmentDTOList =
                                battleFieldManager.getAssignments(battleName, allyName, numOfAssignmentsPerDraw);
                        json = GSON_INSTANCE.toJson(assignmentDTOList);
                        System.out.println("Refreshed assignments");
                    }
                }
            }
        }

        return json;
    }

    private String refreshAgents(HttpServletRequest req) {
        String allyName = SessionUtils.getUsername(req);
        String json = null;
        AlliesManager alliesManager =
                getAlliesManager(getServletContext());
        if(allyName != null && !allyName.isEmpty()){
            synchronized (this){
                AgentsDTO agentsDTO =
                        alliesManager.refreshAgents(allyName);
                json = GSON_INSTANCE.toJson(agentsDTO);
            }
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
        String userType =
                SessionUtils.getType(req);
        String battleName =
                SessionUtils.getBattleFieldName(req);
        BattleFieldManager battleFieldManager =
                getBattleFieldManager(getServletContext());
        AlliesManager alliesManager =
                getAlliesManager(getServletContext());
        if(battleName != null && !battleName.isEmpty()){
            synchronized (this){
                BattleFieldInfo battleFieldInfo =
                        battleFieldManager.getBattleFieldInfo(battleName);
                json = GSON_INSTANCE.toJson(battleFieldInfo);
            }
        }
        else{
            if(userType != null && ! userType.isEmpty()){
                if(userType.equals(AGENT)){
                    String allyName =
                            SessionUtils.getAllyName(req);
                    System.out.println(allyName);
                    synchronized (this) {
                        Ally ally =
                                alliesManager.getAlly(allyName);
                        if (ally != null) {
                            if (ally.hasJoined()) {
                                String battleFieldName =
                                        ally.getBattleName();
                                req.getSession(false).setAttribute(BATTLE, battleFieldName);
                                BattleFieldInfo battleFieldInfo =
                                        battleFieldManager.getBattleFieldInfo(battleFieldName);
                                json = GSON_INSTANCE.toJson(battleFieldInfo);
                            } else {
                                json = GSON_INSTANCE.toJson(new BattleFieldInfo());
                            }
                        }
                    }
                }
            }
        }

        return json;
    }

    private String refreshAllies(HttpServletRequest req) {
        String json = null;
        String userType = SessionUtils.getType(req);
        AlliesManager alliesManager = getAlliesManager(getServletContext());
        if(userType != null && !userType.isEmpty()){
            if(userType.equals(ALLY) || userType.equals(UBOAT)){
                String battleFieldName = SessionUtils.getBattleFieldName(req);
                if(battleFieldName != null && !battleFieldName.isEmpty()){
                    synchronized (this){
                        AlliesDTO alliesDTO =
                                alliesManager.refreshBattleAllies(battleFieldName);
                        json = GSON_INSTANCE.toJson(alliesDTO);
                    }
                }
            }
        }
        else{
            String userTypeFromParameter =
                    req.getParameter(TYPE);
            if(userTypeFromParameter != null && userTypeFromParameter.equals(AGENT)){
                synchronized (this){
                    AlliesDTO alliesDTO =
                            alliesManager.refreshAllAllies();
                    json = GSON_INSTANCE.toJson(alliesDTO);
                }
            }
        }

        return json;
    }

    private String refreshAllBattleFields() {
        BattleFieldManager battleFieldManager =
                getBattleFieldManager(getServletContext());
        String json = null;
        synchronized (this){
            BattleFieldDTO battleFieldDTO =
                    battleFieldManager.refreshAllBattleFields();
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
                    synchronized (this) {
                        CandidatesDTOList info = new CandidatesDTOList(
                                alliesManager.refreshBattleCandidates(battleFieldName));
                        json = GSON_INSTANCE.toJson(info);
                    }
                }
                else if (userType.equals(ALLY)){
                    String allyName = SessionUtils.getUsername(req);
                    if(allyName != null && !allyName.isEmpty()){
                        synchronized (this){
                            CandidatesDTO candidatesDTO =
                                    alliesManager.refreshAllyCandidates(allyName);
                            json = GSON_INSTANCE.toJson(candidatesDTO);
                        }
                    }
                }
            }
        }

        return json;
    }
}
