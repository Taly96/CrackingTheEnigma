package engine.managers;


import dto.agents.AgentsInfo;

import java.util.HashMap;
import java.util.Map;

public class AgentsManager {

    private Map<String, AgentsInfo> agents = null;

    public AgentsManager() {
        this.agents = new HashMap<>();
    }
    public void addAgent(AgentsInfo agent) {
        this.agents.put(agent.getName(), agent);
    }
}
