package dto.candidates;

public class CandidatesInfo {

    private String candidate = null;

    private String foundBy = null;

    private String codeConfig = null;

    public CandidatesInfo(
            String candidate,
            String foundBy,
            String codeConfig
    ){
        this.candidate = candidate;
        this.codeConfig = codeConfig;
        this.foundBy = foundBy;
    }

    public String getCandidate() {
        return this.candidate;
    }

    public String getFoundBy() {
        return this.foundBy;
    }

    public String getCodeConfig() {
        return this.codeConfig;
    }
}
