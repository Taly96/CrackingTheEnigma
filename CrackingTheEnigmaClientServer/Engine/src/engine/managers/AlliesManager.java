package engine.managers;

import dto.activeteams.AlliesDTO;
import dto.activeteams.AlliesInfo;
import dto.agents.AgentsDTO;
import dto.agents.AgentsInfo;
import dto.candidates.CandidatesDTO;

import java.util.*;

public class AlliesManager {

    //Map<allyName,Ally>
    private Map<String, Ally> allies = null;

    public AlliesManager(){
        this.allies = new HashMap<>();
    }

    public synchronized void addAlly(String allyName){
        this.allies.put(allyName, new Ally(allyName));
    }

    public synchronized void updateAllyInfo(String allyName, String battleName){
        this.allies.get(allyName).updateAlly(battleName);
    }

    public synchronized AlliesDTO refreshBattleAllies(String battleName) {
        AlliesDTO alliesDTO = new AlliesDTO();

        for(Ally ally : this.allies.values()){
            if(ally.getBattleName().equals(battleName)){
                alliesDTO.addInfo(ally.getInfo());
            }
         }

        return alliesDTO;
    }

    public synchronized void addAgent (AgentsInfo agent) {
        this.allies.get(agent.getAlliesTeam()).addAgent(agent);
    }

    public synchronized void setAllyReadyForContest(String allyName, String assignmentSize) {
        this.allies.get(allyName).setReady(assignmentSize);
    }

    public synchronized List<CandidatesDTO> refreshBattleCandidates(String battleFieldName) {
        List<CandidatesDTO> candidatesDTOs = new ArrayList<>();

        for (Ally ally : this.allies.values()) {
            if (ally.getBattleName().equals(battleFieldName)) {
                candidatesDTOs.add(ally.refreshCandidates());
            }
        }

        return candidatesDTOs;
    }

    public synchronized CandidatesDTO refreshAllyCandidates(String allyName) {

        return this.allies.get(allyName).refreshCandidates();
    }

    public synchronized AlliesInfo refreshAlly(String allyName) {

        return this.allies.get(allyName).getInfo();
    }

    public synchronized AgentsDTO refreshAgents(String allyName) {
        AgentsDTO agentsDTO = new AgentsDTO();

        for (AgentsInfo agent : this.allies.get(allyName).getAgents()) {
            agentsDTO.addInfo(agent);
        }

        return agentsDTO;
    }

    public synchronized AlliesDTO refreshAllAllies() {
        AlliesDTO allAllies = new AlliesDTO();

        for (Ally ally : this.allies.values()) {
            if (!ally.isReady()) {
                allAllies.addInfo(ally.getInfo());

            }
        }

        return allAllies;
    }

    public synchronized Ally getAlly(String allyName) {

        return this.allies.get(allyName);
    }


    public synchronized void updateCandidates(String allyName, CandidatesDTO candidatesDTO) {
       this.allies.get(allyName).updateCandidates(candidatesDTO, allyName);
    }

    public synchronized void removeAllyFromContest(String allyName) {
        this.allies.get(allyName).removeFromContest();
    }
    public synchronized void updateAgentInfo(String allyName, AgentsInfo agentsInfo) {
        this.allies.get(allyName).updateAgent(agentsInfo);
    }
}
