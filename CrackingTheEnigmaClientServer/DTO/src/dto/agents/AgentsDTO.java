package dto.agents;

import java.util.ArrayList;
import java.util.List;

public class AgentsDTO {

    private List<AgentsInfo> agents = null;

    public AgentsDTO(){
        this.agents = new ArrayList<>();
    }

    public List<AgentsInfo> getAgents() {
        return this.agents;
    }

    public void addInfo(AgentsInfo agentsInfo){
        this.agents.add(agentsInfo);
    }
}
