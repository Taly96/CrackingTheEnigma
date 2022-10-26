package engine.managers;

import dm.decipher.DecipherManager;
import dto.assignment.AssignmentDTOList;
import dto.battlefield.BattleFieldInfo;
import dto.codeconfig.CodeConfigInfo;
import dto.loadedmachine.LoadedMachineDTO;
import dto.process.MessageProcessDTO;

import java.util.HashMap;
import java.util.Map;

public class BattleField {

    //Map<AllyName, DM>
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

    public synchronized BattleFieldInfo getBattleFieldInfo() {

        return this.battleFieldInfo;
    }
    public synchronized void assembleContest(String processedMessage) {
        this.battleFieldInfo.setStatus("Waiting");
        this.battleFieldInfo.setMessageToDecipher(processedMessage);
    }
    public synchronized void setAllyReadyForContest(String allyName, String assignmentSize) {

        this.contest.put(allyName, new DecipherManager(
                this.battleFieldInfo.getLevel(),
                assignmentSize,
                this.getBattleFieldInfo().getTotalNumberOfAssignment(),
                this.machineManager.getDecipherDTO()
        ));
        if (contest.size() == this.battleFieldInfo.getNeededNumOfAllies()) {
            this.startContest();
        }
    }

    public synchronized MessageProcessDTO processMessage(String messageToProcess) {

        return this.machineManager.processMessage(messageToProcess);
    }

    public synchronized CodeConfigInfo setCodeConfig(CodeConfigInfo inputConfig) {

        return this.machineManager.setCodeConfig(inputConfig, false);
    }

    public synchronized CodeConfigInfo generateCodeConfig() {

        return this.machineManager.generateCodeConfig();
    }

    private synchronized void startContest(){

        for(DecipherManager dm : this.contest.values()){
            dm.startProducingAssignments();
            System.out.println("DM started producing");
        }
        this.battleFieldInfo.setStatus("Active");
    }

    public synchronized AssignmentDTOList getAssignments(String allyName, Integer numOfAssignmentsPerDraw) {

        AssignmentDTOList assignmentDTOList =
                this.contest.get(allyName).getAssignments(numOfAssignmentsPerDraw);

        return assignmentDTOList;
    }

    public synchronized byte[] getSerMachineInventory() {

        return this.machineManager.getSerMachineInventory();
    }

    public synchronized CodeConfigInfo resetCode() {

        return this.machineManager.resetMachine();
    }
}
