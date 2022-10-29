package server.servlets.candidates;

import dto.agents.AgentsInfo;
import dto.candidates.CandidatesDTO;
import engine.managers.AlliesManager;
import engine.managers.BattleFieldManager;
import engine.managers.UsersManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.SessionUtils;

import java.io.IOException;
import java.util.Properties;

import static server.utils.Constants.*;
import static server.utils.ServletUtils.*;
import static server.utils.SessionUtils.TYPE;
import static server.utils.SessionUtils.USERNAME;

@WebServlet("/update")
public class ServerUpdateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = SessionUtils.getType(req);
        if(userType != null && !userType.isEmpty()){
            switch (userType){
                case UBOAT:{
                    this.processUBoatRequest(req, resp);
                    break;
                }
                case AGENT: {
                    this.processAgentRequest(req, resp);
                    break;
                }
                default:
                    resp.setContentType("text/plain");
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                    resp.getOutputStream().print("Cant' update server with data. Can't recognize user type.");
            }
        }else{
            resp.setContentType("text/plain");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().print("Cant' update server with data. Can't recognize user type.");
        }

        resp.flushBuffer();
    }

    private void processAgentRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String dataType =
                req.getParameter(DATA);
        switch (dataType){
            case CANDIDATES: {
                this.updateCandidates(req, resp);
                break;
            }
            case AGENT: {
                this.updateAgent(req, resp);
                break;
            }
            default: {
                resp.setContentType("text/plain");
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getOutputStream().print("Cant' update server with data. Can't recognize data type.");
                break;
            }
        }

    }

    private void updateAgent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String allyName =
                SessionUtils.getAllyName(req);
        if(allyName!= null && !allyName.isEmpty()){
            AlliesManager alliesManager =
                    getAlliesManager(getServletContext());
            Properties properties = new Properties();
            properties.load(req.getInputStream());
            AgentsInfo agentsInfo =
                    GSON_INSTANCE.fromJson(
                            properties.getProperty(AGENT),
                            AgentsInfo.class
                    );
            synchronized (this){
                alliesManager.updateAgentInfo(allyName, agentsInfo);
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    private void processUBoatRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String dataType =
                req.getParameter(DATA);
        switch (dataType){
            case WINNER: {
                this.updateWinner(req, resp);
                break;
            }
            case STATUS: {
                this.updateStatus(req, resp);
                break;
            }
            default: {
                resp.setContentType("text/plain");
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getOutputStream().print("Cant' update server with data. Can't recognize data type.");
                break;
            }
        }
    }

    private void updateStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Properties properties = new Properties();
        properties.load(req.getInputStream());
        String status =
                GSON_INSTANCE.fromJson(
                        properties.getProperty(STATUS),
                        String.class
                );
        if(status != null && !status.isEmpty()){
            String battleName =
                    SessionUtils.getBattleFieldName(req);
            if(battleName != null && !battleName.isEmpty()){
                BattleFieldManager battleFieldManager =
                        getBattleFieldManager(getServletContext());
                switch (status){
                    case "Inactive": {
                        String userName =
                                SessionUtils.getUsername(req);
                        UsersManager usersManager =
                                getUsersManager(getServletContext());
                        synchronized (this){
                            battleFieldManager.setContestStatus(battleName, status);
                            usersManager.removeUser(userName);
                            battleFieldManager.removeContest(battleName);
                            req.getSession(false).removeAttribute(USERNAME);
                            req.getSession(false).removeAttribute(BATTLE_FIELD);
                            req.getSession(false).removeAttribute(TYPE);
                        }
                        resp.setStatus(HttpServletResponse.SC_OK);
                        break;
                    }
                    case "Waiting": {
                        synchronized (this){
                            battleFieldManager.setContestStatus(battleName, status);
                            battleFieldManager.clearContest(battleName);
                        }
                        resp.setStatus(HttpServletResponse.SC_OK);
                        break;
                    }
                    default:
                        resp.setStatus(HttpServletResponse.SC_CONFLICT);
                        resp.getOutputStream().print("Can't update contest status. Can't recognize status requested.");
                        resp.setContentType("text/plain");
                        break;
                }

            }
        }
    }

    private void updateCandidates(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String allyName = SessionUtils.getAllyName(req);
        if(allyName != null && !allyName.isEmpty()){
            String agentName = SessionUtils.getUsername(req);
            if(agentName != null && !agentName.isEmpty()){
                String battleName =
                        SessionUtils.getBattleFieldName(req);
                if(battleName != null && !battleName.isEmpty()) {
                    Properties properties = new Properties();
                    properties.load(req.getInputStream());
                    AlliesManager alliesManager =
                            getAlliesManager(getServletContext());
                    CandidatesDTO candidatesDTO =
                            GSON_INSTANCE.fromJson(
                                    properties.getProperty(CANDIDATES),
                                    CandidatesDTO.class
                            );
                    if (candidatesDTO != null) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        synchronized (this) {
                            alliesManager.updateCandidates(allyName, candidatesDTO);
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
    }

    private void updateWinner(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Properties properties = new Properties();
        properties.load(req.getInputStream());
        String winner =
                GSON_INSTANCE.fromJson(
                        properties.getProperty(WINNER),
                        String.class
                );
        if (winner != null && !winner.isEmpty()) {
            String battleName =
                    SessionUtils.getBattleFieldName(req);
            if (battleName != null && !battleName.isEmpty()) {
                BattleFieldManager battleFieldManager =
                        getBattleFieldManager(getServletContext());
                synchronized (this) {
                    battleFieldManager.setWinner(battleName, winner);
                }
            } else {
                resp.setContentType("text/plain");
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getOutputStream().print("Cant' update winner, no battle name.");
            }
        } else {
            resp.setContentType("text/plain");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().print("Cant' update winner, no winner name.");
        }
    }
}
