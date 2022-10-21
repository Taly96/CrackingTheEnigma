package engine.managers;

import dto.activeteams.AlliesInfo;
import dto.agents.AgentsInfo;
import dto.candidates.CandidatesDTO;

import java.util.List;

public class Ally {

    private String assignmentSize ="0";

    private String battleName = "Hasn't joined yet";

    private boolean isReady = false;

    private AlliesInfo info = null;

    private AgentsManager agents = null;

    private CandidatesManager candidatesManager = null;

    public Ally(){
        this.agents = new AgentsManager();
        this.candidatesManager = new CandidatesManager();
    }

    public void updateAlly(AlliesInfo info){
        this.battleName = info.getBattleName();
        this.info = info;
    }

    public  AlliesInfo getInfo() {
        return info;
    }

    public void addAgent(AgentsInfo agent) {
        this.agents.addAgent(agent);
    }

    public void setReady(String assignmentSize) {
        this.isReady = true;
        this.assignmentSize = assignmentSize;
    }

    public String getBattleName() {
        return this.battleName;
    }

    public CandidatesDTO refreshCandidates() {

        return this.candidatesManager.getCandidates();
    }

    public List<AgentsInfo> getAgents() {

        return this.agents.getAgents();
    }
}
