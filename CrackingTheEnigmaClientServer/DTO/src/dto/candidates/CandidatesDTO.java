package dto.candidates;

import java.util.ArrayList;
import java.util.List;

public class CandidatesDTO {

    private List<CandidatesInfo> candidates = null;

    public CandidatesDTO(){
        this.candidates = new ArrayList<>();
    }
    public synchronized List<CandidatesInfo> getCandidates() {
        return this.candidates;
    }

    public synchronized void addInfo(CandidatesInfo candidatesInfo){
        this.candidates.add(candidatesInfo);
    }
}
