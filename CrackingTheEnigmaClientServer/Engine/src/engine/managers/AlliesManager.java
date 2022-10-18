package engine.managers;

import dm.decipher.DecipherManager;
import dto.activeteams.AlliesDTO;
import dto.activeteams.AlliesInfo;
import dto.agents.AgentsInfo;
import dto.candidates.CandidatesDTO;

import java.util.HashMap;
import java.util.Map;

public class AlliesManager {

    private Map<String, Ally> allies = null;

    public AlliesManager(){
        this.allies = new HashMap<>();
    }

    public synchronized void addAlly(String name){
        this.allies.put(name, new Ally());
    }

    public synchronized void updateAllInfo(AlliesInfo info){
        this.allies.get(info.getName()).updateAlly(info);
    }

    public synchronized void clearInfo(){
        this.allies.clear();
    }

    public synchronized AlliesDTO refreshAllies() {
        AlliesDTO alliesDTO = new AlliesDTO();

        for(Map.Entry<String, Ally> ally : this.allies.entrySet()){
            alliesDTO.addInfo(ally.getValue().getInfo());
        }

        return alliesDTO;
    }

    public synchronized void addAgent(AgentsInfo agent) {
        this.allies.get(agent.getName()).addAgent(agent);
    }
}
