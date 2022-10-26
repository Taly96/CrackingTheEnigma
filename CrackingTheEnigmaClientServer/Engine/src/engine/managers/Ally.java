package engine.managers;

import dto.activeteams.AlliesInfo;
import dto.agents.AgentsInfo;
import dto.candidates.CandidatesDTO;

import java.util.List;

public class Ally {

    private String assignmentSize ="0";

    private String battleName = "Hasn't joined yet";

    private boolean isReady = false;

    private boolean hasJoined = false;

    private AlliesInfo info = null;

    private AgentsManager agents = null;

    private CandidatesManager candidatesManager = null;

    public Ally(String allyName){
        this.info = new AlliesInfo(allyName);
        this.agents = new AgentsManager();
        this.candidatesManager = new CandidatesManager();
    }

    public synchronized void updateAlly(String battleName){
        this.info.setBattleName(battleName);
        this.battleName = battleName;
        this.hasJoined = true;
    }

    public synchronized  AlliesInfo getInfo() {
        return info;
    }

    public synchronized void addAgent(AgentsInfo agent) {
        this.info.incrementNumOfAgents();
        this.agents.addAgent(agent);
    }

    public synchronized void setReady(String assignmentSize) {
        this.isReady = true;
        this.info.setAssignmentSize(assignmentSize);
        this.assignmentSize = assignmentSize;
    }

    public synchronized String getBattleName() {
        return this.battleName;
    }

    public synchronized CandidatesDTO refreshCandidates() {

        return this.candidatesManager.getCandidates();
    }

    public synchronized List<AgentsInfo> getAgents() {

        return this.agents.getAgents();
    }

    public synchronized boolean isReady() {
        return isReady;
    }

    public synchronized boolean hasJoined(){
        return this.hasJoined;
    }

    public synchronized void setReady(boolean ready) {
        isReady = ready;
    }

    public synchronized void updateCandidates(CandidatesDTO candidatesDTO, String allyName) {
        this.candidatesManager.addCandidates(candidatesDTO, allyName);
    }
}
