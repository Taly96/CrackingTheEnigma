package dto.candidates;

import java.util.ArrayList;
import java.util.List;

public class CandidatesDTO {

    private List<CandidatesInfo> candidates = null;

    public CandidatesDTO(){
        this.candidates = new ArrayList<>();
    }
    public List<CandidatesInfo> getCandidates() {
        return this.candidates;
    }

    public void addInfo(CandidatesInfo candidatesInfo){
        this.candidates.add(candidatesInfo);
    }
}
