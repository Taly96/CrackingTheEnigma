package dto.process;

import dto.codeconfig.CodeConfigInfo;

public class MessageProcessDTO {

    private CodeConfigInfo currentCode = null;

    private String processedMessage = null;

    public MessageProcessDTO(CodeConfigInfo currentCode, String processedMessage) {
        this.currentCode = currentCode;
        this.processedMessage = processedMessage;
    }

    public CodeConfigInfo getCurrentCode() {
        return currentCode;
    }

    public String getProcessedMessage() {
        return processedMessage;
    }
}
