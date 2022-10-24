package engine.managers;

import dm.decipher.DecipherManager;
import dto.assignment.AssignmentDTOList;
import dto.battlefield.BattleFieldInfo;
import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesInfo;
import dto.codeconfig.CodeConfigInfo;
import dto.decipher.OriginalInformation;
import dto.loadedmachine.LoadedMachineDTO;
import dto.process.MessageProcessDTO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class BattleField {

    //Map<AllyName, DM>
    private BattleFieldInfo battleFieldInfo = null;

    private MachineManager machineManager= null;

    private Map<String, DecipherManager> contest = null;

    private String originalMessage = null;

    private String originalCodeConfig = null;

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
    public synchronized void assembleContest(OriginalInformation originalInformation) {
        this.battleFieldInfo.setStatus("Waiting");
        this.battleFieldInfo.setMessageToDecipher(originalInformation.getMessageToDecipher());
        this.originalMessage =originalInformation.getOriginalMessage();
        this.originalCodeConfig = this.machineManager.getCurrentCodeConfig();
    }
    public synchronized boolean setAllyReadyForContest(String allyName, String assignmentSize) {
        if(!this.battleFieldInfo.getStatus().equals("full")) {
            this.contest.put(allyName, new DecipherManager(
                    this.battleFieldInfo.getLevel(),
                    assignmentSize,
                    this.getBattleFieldInfo().getTotalNumberOfAssignment(),
                    this.machineManager.getDecipherDTO()
            ));
            if (contest.size() == this.battleFieldInfo.getNeededNumOfAllies()) {
                this.battleFieldInfo.setStatus("full");
                this.startContest();
            }

            return true;
        }

        return false;
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
        this.originalCodeConfig =
                this.machineManager.getCurrentCodeConfig();
        for(DecipherManager dm : this.contest.values()){
            dm.startProducingAssignments();
        }

        this.battleFieldInfo.setStatus("Active");
    }

    public synchronized void checkForWinner(CandidatesDTO candidatesDTO) {
        for(CandidatesInfo info : candidatesDTO.getCandidates()){
            if(info.getCandidate().equals(this.originalMessage) && info.getCodeConfig().equals(this.originalCodeConfig)){
                this.battleFieldInfo.setWinner(info.getFoundBy());
                System.out.println("Server found winner");
                this.battleFieldInfo.setStatus("Ended");
                for(DecipherManager dm : this.contest.values()){
                    dm.stopProducing();
                }
            }
        }
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
