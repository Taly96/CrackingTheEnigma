package dto.candidates;

import java.util.ArrayList;
import java.util.List;

public class CandidatesDTOList {
    private List<CandidatesDTO> candidatesDTOList = null;

    public CandidatesDTOList(){
        this.candidatesDTOList = new ArrayList<>();
    }

    public List<CandidatesDTO> getCandidatesDTOList() {
        return candidatesDTOList;
    }

    public void setCandidatesDTOList(List<CandidatesDTO> candidatesDTOList) { this.candidatesDTOList = candidatesDTOList;}


}
