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

    public void addAlly(String allyName){
        this.allies.put(allyName, new Ally());
    }

    public void updateAllyInfo(AlliesInfo info){
        this.allies.get(info.getName()).updateAlly(info);
    }

    public void clearInfo(){
        this.allies.clear();
    }

    public AlliesDTO refreshAllAllies(String battleName) {
        AlliesDTO alliesDTO = new AlliesDTO();

        for(Ally ally : this.allies.values()){
            if(ally.getBattleName().equals(battleName)){
                alliesDTO.addInfo(ally.getInfo());
            }
        }

        return alliesDTO;
    }

    public  void addAgent (AgentsInfo agent) {
        this.allies.get(agent.getAlliesTeam()).addAgent(agent);
    }

    public void setAllyReadyForContest(String allyName, String assignmentSize) {
        this.allies.get(allyName).setReady(assignmentSize);
    }

    public List<CandidatesDTO> refreshAllCandidates(String battleFieldName) {
        List<CandidatesDTO> candidatesDTOs = new ArrayList<>();
        for(Ally ally :  this.allies.values()){
            if(ally.getBattleName().equals(battleFieldName)){
                candidatesDTOs.add(ally.refreshCandidates());
            }
        }

        return candidatesDTOs;
    }

    public CandidatesDTO refreshCandidates(String allyName) {
        Ally ally = this.allies.get(allyName);
        CandidatesDTO candidatesDTOs = ally.refreshCandidates();

        return candidatesDTOs;
    }

    public AlliesInfo refreshAlly(String allyName) {
        Ally ally = this.allies.get(allyName);
        AlliesInfo alliesInfo = ally.getInfo();

        return alliesInfo;
    }

    public AgentsDTO refreshAgents(String allyName) {
        AgentsDTO agentsDTO = new AgentsDTO();

        for(AgentsInfo agent : this.allies.get(allyName).getAgents()){
            agentsDTO.addInfo(agent);
        }

        return agentsDTO;
    }
}
