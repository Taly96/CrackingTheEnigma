package dto.decipher;

public class OriginalInformation {

    private String originalMessage = null;

    private String originalConfig = null;

    public OriginalInformation(String originalMessage, String originalConfig){
        this.originalConfig = originalConfig;
        this.originalMessage = originalMessage;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public String getOriginalConfig() {
        return originalConfig;
    }
}
