package engine.managers;

import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesInfo;

public class CandidatesManager {

    private CandidatesDTO candidates = null;

    public CandidatesManager(){
        this.candidates = new CandidatesDTO();
    }
    public CandidatesDTO getCandidates() {
        return this.candidates;
    }

    public void clearInfo(){
        this.candidates.clear();
    }

    public void addCandidates(CandidatesDTO candidatesDTO, String allyName) {
        if(this.candidates.getAllyName() == null){
            this.candidates.setAllyName(allyName);
        }

        for(CandidatesInfo candidatesInfo : candidatesDTO.getCandidates()){
            this.candidates.addInfo(candidatesInfo);
        }
    }
}
