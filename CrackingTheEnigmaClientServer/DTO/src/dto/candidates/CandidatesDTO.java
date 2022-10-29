package dto.candidates;

import java.util.ArrayList;
import java.util.List;

public class CandidatesDTO {

    private List<CandidatesInfo> candidates = null;

    private String allyName = null;

    public CandidatesDTO(){
        this.candidates = new ArrayList<>();
    }
    public List<CandidatesInfo> getCandidates() {
        return this.candidates;
    }

    public void addInfo(CandidatesInfo candidatesInfo){
        this.candidates.add(candidatesInfo);
    }

    public void changeVersion(int version) {
        this.candidates = this.candidates.subList((this.candidates.size() - version) + 1, this.candidates.size());
    }

    public String getAllyName() {
        return allyName;
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

    public void clear() {
        this.candidates.clear();
        this.allyName = null;
    }
}
