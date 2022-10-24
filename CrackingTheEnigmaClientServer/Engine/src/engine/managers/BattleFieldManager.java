package engine.managers;

import dto.assignment.AssignmentDTOList;
import dto.battlefield.BattleFieldDTO;
import dto.battlefield.BattleFieldInfo;
import dto.candidates.CandidatesDTO;
import dto.codeconfig.CodeConfigInfo;
import dto.decipher.OriginalInformation;
import dto.loadedmachine.LoadedMachineDTO;
import dto.process.MessageProcessDTO;

import java.util.HashMap;
import java.util.Map;

public class BattleFieldManager {
//Map<BattleName, BattleField>
    private Map<String, BattleField> battleFields = null;

    public BattleFieldManager() {
        this.battleFields = new HashMap<>();
    }

    public synchronized void addBattleField(
            LoadedMachineDTO loadedMachineDTO,
            MachineManager machineManager
    ) {
        String name = loadedMachineDTO.getBattleFieldInfo().getBattleFieldName();
        this.battleFields.put(
                name,
                new BattleField(
                        loadedMachineDTO,
                        machineManager
                )
        );
    }

    public synchronized BattleField getBattleField(String name) {
        return this.battleFields.get(name);
    }

    public synchronized boolean isBattleExists(String name) {
        return this.battleFields.containsKey(name);
    }

    public synchronized BattleFieldDTO refreshAllBattleFields() {
        BattleFieldDTO data = new BattleFieldDTO();

        for(Map.Entry<String, BattleField> set : this.battleFields.entrySet()){
            data.addInfo(set.getValue().getBattleFieldInfo());
        }

        return data;
    }

    public synchronized BattleFieldInfo getBattleFieldInfo(String battleName) {

        return this.battleFields.get(battleName).getBattleFieldInfo();
    }

    public synchronized boolean setAllyReadyForContest(String battleName, String allyName, String assignmentSize) {

        return
                this.battleFields.get(battleName)
                        .setAllyReadyForContest(allyName, assignmentSize);
    }

    public synchronized void assembleContest(String battleName, OriginalInformation originalInformation) {
        this.battleFields.get(battleName).assembleContest(originalInformation);
    }

    public synchronized MessageProcessDTO processMessage(String battleName, String messageToProcess) {

        return this.battleFields.get(battleName).processMessage(messageToProcess);
    }

    public synchronized CodeConfigInfo setCodeConfig(String battleName, CodeConfigInfo inputConfig) {

        return this.battleFields.get(battleName).setCodeConfig(inputConfig);
    }

    public synchronized CodeConfigInfo generateCodeConfig(String battleName) {

        return this.battleFields.get(battleName).generateCodeConfig();
    }

    public synchronized void checkForWinner(String battleName, CandidatesDTO candidatesDTO) {
        this.battleFields.get(battleName).checkForWinner(candidatesDTO);
    }

    public synchronized String getBattleStatus(String battleName) {

        return this.battleFields.get(battleName).getBattleFieldInfo().getStatus();
    }

    public synchronized AssignmentDTOList getAssignments(String battleName, String allyName, Integer numOfAssignmentsPerDraw) {

        return this.battleFields.get(battleName).getAssignments(allyName, numOfAssignmentsPerDraw);
    }

    public synchronized byte[] getSerMachineInventory(String battleFiledName) {

        return this.battleFields.get(battleFiledName).getSerMachineInventory();
    }

    public synchronized CodeConfigInfo resetCodeConfig(String battleName) {

        return this.battleFields.get(battleName).resetCode();
    }
}

