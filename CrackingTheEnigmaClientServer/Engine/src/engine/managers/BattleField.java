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

    public MessageProcessDTO processMessage(String messageToProcess) {

        return this.machineManager.processMessage(messageToProcess);
    }

    public CodeConfigInfo setCodeConfig(CodeConfigInfo inputConfig) {

        return this.machineManager.setCodeConfig(inputConfig, false);
    }

    public CodeConfigInfo generateCodeConfig() {

        return this.machineManager.generateCodeConfig();
    }

    private synchronized void startContest(){

        for(DecipherManager dm : this.contest.values()){
            dm.startProducingAssignments();
        }

        this.battleFieldInfo.setStatus("Active");

    }

    public synchronized AssignmentDTOList getAssignments(String allyName, Integer numOfAssignmentsPerDraw) {
        if(this.battleFieldInfo.getStatus().equals("Active")){
            AssignmentDTOList assignmentDTOList =
                    this.contest.get(allyName).getAssignments(numOfAssignmentsPerDraw);
            return assignmentDTOList;
        }


        return
                new AssignmentDTOList();
    }

    public byte[] getSerMachineInventory() {

        return this.machineManager.getSerMachineInventory();
    }

    public CodeConfigInfo resetCode() {

        return this.machineManager.resetMachine();
    }

    public synchronized void setWinner(String winner) {
        this.battleFieldInfo.setWinner(winner);
        this.battleFieldInfo.setStatus("Ended");

        for(DecipherManager dm : this.contest.values()){
            dm.stopProducing();
        }
    }

    public synchronized void setStatus(String status) {
        this.battleFieldInfo.setStatus(status);
    }

    public synchronized void clearContest() {
        this.battleFieldInfo.setWinner("No one");
    }

    public synchronized void removeAlly(String allyName) {
        this.contest.remove(allyName);
        this.battleFieldInfo.decrementAllies();
    }
}
