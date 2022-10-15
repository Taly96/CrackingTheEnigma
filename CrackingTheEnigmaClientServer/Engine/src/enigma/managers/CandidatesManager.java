package enigma.managers;

import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesInfo;

public class CandidatesManager {

    private CandidatesDTO candidates = null;

    public CandidatesManager(){
        this.candidates = new CandidatesDTO();
    }
    public synchronized CandidatesDTO getCandidates() {
        return this.candidates;
    }

    public synchronized void addCandidate(CandidatesInfo newInfo){
        this.candidates.addInfo(newInfo);
    }

    public synchronized void clearInfo(){
        this.candidates.getCandidates().clear();
    }
}
