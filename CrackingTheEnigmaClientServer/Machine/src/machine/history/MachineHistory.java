package machine.history;


import dto.codeconfig.CodeConfigInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MachineHistory implements Serializable {
    private List<CodeConfigInfo> machineHistory = null;

    public MachineHistory(){
        this.machineHistory  = new ArrayList<>();
    }

    public synchronized List<CodeConfigInfo> getCodeConfigStats(){
        return this.machineHistory;
    }

    public synchronized void addNewCodeConfig(CodeConfigInfo newCodeConfig){
        this.machineHistory.add(0, newCodeConfig);
    }

    public synchronized void clear() {
        this.machineHistory.clear();
    }
}
