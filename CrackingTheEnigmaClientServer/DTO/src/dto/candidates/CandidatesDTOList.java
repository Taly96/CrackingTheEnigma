package dto.candidates;

import java.util.List;

public class CandidatesDTOList {
    private List<CandidatesDTO> candidatesDTOList = null;

    public CandidatesDTOList(List<CandidatesDTO> candidatesDTOList){
        this.candidatesDTOList = candidatesDTOList;
    }

    public List<CandidatesDTO> getCandidatesDTOList() {
        return candidatesDTOList;
    }
}
