package enigma.machine.history;

//import dto.code.config.CodeConfigDTO;
//import dto.input.message.InputMessageDTO;
//import dto.stats.CodeConfigStatsDTO;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MachineHistory implements Serializable {
//    private List<CodeConfigStatsDTO> machineHistory = null;
//
//    public MachineHistory(){
//        this.machineHistory  = new ArrayList<>();
//    }
//
//    public List<CodeConfigStatsDTO> getCodeConfigStats(){
//        return this.machineHistory;
//    }
//
//    public void addNewCodeConfig(CodeConfigDTO newCodeConfig){
//        this.machineHistory.add(0, new CodeConfigStatsDTO(newCodeConfig));
//    }
//
//    public void addStatsToCurrentCodeConfig(InputMessageDTO newProcessStats){
//        this.machineHistory.get(0).addStats(newProcessStats);
//    }
//
//    public void clear() {
//        this.machineHistory.clear();
//    }
//}
