package engine.managers;

import dto.activeteams.AlliesInfo;
import dto.agents.AgentsInfo;

public class Ally {

    private Integer assignmentSize = 0;

    private AlliesInfo info = null;

    private AgentsManager agents = null;

    private CandidatesManager candidatesManager = null;

    public Ally(){
        this.agents = new AgentsManager();
        this.candidatesManager = new CandidatesManager();
    }

    public  AlliesInfo getInfo() {
        return info;
    }

    public  void updateAlly(AlliesInfo info){
        this.info = info;
    }

    public void addAgent(AgentsInfo agent) {
        this.agents.addAgent(agent);
    }
}
