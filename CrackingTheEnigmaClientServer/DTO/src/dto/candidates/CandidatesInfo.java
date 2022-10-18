package dto.candidates;

public class CandidatesInfo {

    private String candidate = null;

    private String foundBy = null;

    private byte[] codeConfig = null;

    public CandidatesInfo(
            String candidate,
            String foundBy,
            byte[] codeConfig
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

    public byte[] getCodeConfig() {
        return this.codeConfig;
    }
}
