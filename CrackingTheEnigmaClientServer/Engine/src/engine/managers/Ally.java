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

    private Object candidateLock = null;

    private Object allyLock = null;

    private Object agentsLock = null;

    public Ally(String allyName){
        this.candidateLock = new Object();
        this.info = new AlliesInfo(allyName);
        this.agents = new AgentsManager();
        this.candidatesManager = new CandidatesManager();
        this.allyLock = new Object();
        this.agentsLock = new Object();
    }

    public synchronized void updateAlly(String battleName){
        this.info.setBattleName(battleName);
        this.battleName = battleName;
        this.hasJoined = true;
    }

    public synchronized AlliesInfo getInfo() {
        return info;
    }

    public synchronized void addAgent(AgentsInfo agent) {
        synchronized (agentsLock){
            this.info.incrementNumOfAgents();
            this.agents.addAgent(agent);
            agentsLock.notifyAll();
        }
    }

    public synchronized void setReady(String assignmentSize) {
        this.isReady = true;
        this.info.setAssignmentSize(assignmentSize);
        this.assignmentSize = assignmentSize;
    }

    public synchronized String getBattleName() {
        return this.battleName;
    }

    public CandidatesDTO refreshCandidates() {
        CandidatesDTO candidatesDTO;
        synchronized (candidateLock){
            candidatesDTO =
                    this.candidatesManager.getCandidates();
            candidateLock.notifyAll();
        }

        return candidatesDTO;
    }

    public List<AgentsInfo> getAgents() {
        List<AgentsInfo> agents;
        synchronized (agentsLock){
            agents =  this.agents.getAgents();
            agentsLock.notifyAll();
        }

        return agents;
    }

    public synchronized boolean isReady() {
        return isReady;
    }

    public synchronized boolean hasJoined(){
        return this.hasJoined;
    }

    public void updateCandidates(CandidatesDTO candidatesDTO, String allyName) {
        synchronized (candidateLock){
            this.candidatesManager.addCandidates(candidatesDTO, allyName);
            candidateLock.notifyAll();
        }
    }

    public synchronized void removeFromContest() {
        this.assignmentSize ="0";
        this.battleName = "Hasn't joined yet";
        this.isReady = false;
        this.hasJoined = false;
        this.info.removeContestInfo();
        synchronized (agentsLock){
            this.agents.clearContestInfo();
            agentsLock.notifyAll();
        }
        synchronized (candidateLock){
            this.candidatesManager.clearInfo();
            candidateLock.notifyAll();
        }
    }

    public void updateAgent(AgentsInfo agentsInfo) {

        synchronized (agentsLock){
            this.agents.addAgent(agentsInfo);
            agentsLock.notifyAll();
        }
    }
}
