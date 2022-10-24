package dto.decipher;

public class OriginalInformation {

    private String originalMessage = null;

    private String messageToDecipher = null;

    public OriginalInformation(String originalMessage, String originalConfig){
        this.messageToDecipher = originalConfig;
        this.originalMessage = originalMessage;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public String getMessageToDecipher() {
        return messageToDecipher;
    }
}
