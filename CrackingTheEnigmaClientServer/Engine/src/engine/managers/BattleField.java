package engine.managers;

import dm.decipher.DecipherManager;
import dto.battlefield.BattleFieldInfo;
import dto.codeconfig.CodeConfigInfo;
import dto.loadedmachine.LoadedMachineDTO;

import java.util.HashMap;
import java.util.Map;

public class BattleField {
    private String messageToEncrypt = "Contest is inactive";

    private BattleFieldInfo battleFieldInfo = null;

    private MachineManager machineManager= null;

    private Map<String, DecipherManager> contest = null;

    public BattleField(LoadedMachineDTO machineDTO, MachineManager machineManager){
        this.battleFieldInfo = machineDTO.getBattleFieldInfo();
        this.machineManager = machineManager;
        this.contest = new HashMap<>();
        new Thread(new TotalAssignmentsCalculator(
                this.battleFieldInfo.getLevel(),
                machineDTO,
                bigDecimal -> battleFieldInfo.setTotalNumberOfAssignment(bigDecimal.toString())
        )).start();
    }

    public synchronized MachineManager getMachineManager() {
        return this.machineManager;
    }

    public synchronized BattleFieldInfo getBattleFieldInfo(){ return this.battleFieldInfo; }

    public void assembleContest(String messageToEncrypt) {
        this.battleFieldInfo.setStatus("Waiting");
        this.messageToEncrypt = messageToEncrypt;
    }
    public void setAllyReadyForContest(String allyName, String assignmentSize) {
        this.contest.put(allyName, new DecipherManager(
                this.battleFieldInfo.getLevel(),
                assignmentSize,
                this.getBattleFieldInfo().getTotalNumberOfAssignment()
        ));
    }

    public String processMessage(String messageToProcess) {

        return this.machineManager.processMessage(messageToProcess);
    }

    public CodeConfigInfo setCodeConfig(CodeConfigInfo inputConfig) {

        return this.machineManager.setCodeConfig(inputConfig);
    }

    public CodeConfigInfo generateCodeConfig() {

        return this.machineManager.generateCodeConfig();
    }
}
