package dto.codeconfig;

import java.util.ArrayList;
import java.util.List;

public class CodeConfigDTO {

    private List<CodeConfigInfo> codeConfigurations = null;

    public CodeConfigDTO(){
        this.codeConfigurations = new ArrayList<>();
    }

    public List<CodeConfigInfo> getCodeConfigurations() {
        return this.codeConfigurations;
    }

    public void addInfo(CodeConfigInfo codeConfigInfo){
        this.codeConfigurations.add(codeConfigInfo);
    }
}
