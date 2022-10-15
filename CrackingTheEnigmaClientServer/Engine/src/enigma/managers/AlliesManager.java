package enigma.managers;

import dto.activeteams.AlliesDTO;
import dto.activeteams.AlliesInfo;
import dto.candidates.CandidatesDTO;

import java.util.HashMap;
import java.util.Map;

public class AlliesManager {

    private Map<String, Ally> allies = null;

    private CandidatesManager candidatesManager;
    public AlliesManager(){
        this.allies = new HashMap<>();
        this.candidatesManager = new CandidatesManager();
    }

    public synchronized void addAlly(AlliesInfo newAlly){
        this.allies.put(newAlly.getName(), new Ally() );
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

    public synchronized CandidatesDTO refreshCandidates() {

        return this.candidatesManager.getCandidates();
    }
}
