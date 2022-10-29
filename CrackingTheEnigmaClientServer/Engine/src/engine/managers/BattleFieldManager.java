package engine.managers;

import dto.assignment.AssignmentDTOList;
import dto.battlefield.BattleFieldDTO;
import dto.battlefield.BattleFieldInfo;
import dto.codeconfig.CodeConfigInfo;
import dto.loadedmachine.LoadedMachineDTO;
import dto.process.MessageProcessDTO;

import java.util.HashMap;
import java.util.Map;

public class BattleFieldManager {
//Map<BattleName, BattleField>
    private Map<String, BattleField> battleFields = null;

    private Object battleLock = null;

    public BattleFieldManager() {
        this.battleFields = new HashMap<>();
        this.battleLock = new Object();
    }

    public void addBattleField(
            LoadedMachineDTO loadedMachineDTO,
            MachineManager machineManager
    ) {
        synchronized (battleLock) {
            String name = loadedMachineDTO.getBattleFieldInfo().getBattleFieldName();
            this.battleFields.put(
                    name,
                    new BattleField(
                            loadedMachineDTO,
                            machineManager
                    )
            );
            battleLock.notifyAll();
        }
    }

    public boolean isBattleExists(String name) {
        boolean isExists = false;
        synchronized (battleLock){
            isExists = this.battleFields.containsKey(name);
            battleLock.notifyAll();
        }

        return isExists;
    }

    public BattleFieldDTO refreshAllBattleFields() {
        BattleFieldDTO data = new BattleFieldDTO();
        synchronized (battleLock) {

            for (Map.Entry<String, BattleField> set : this.battleFields.entrySet()) {
                data.addInfo(set.getValue().getBattleFieldInfo());
            }

            battleLock.notifyAll();
        }

        return data;
    }

    public BattleFieldInfo getBattleFieldInfo(String battleName) {
        BattleFieldInfo battleFieldInfo;
        synchronized (battleLock){
            battleFieldInfo = this.battleFields.get(battleName).getBattleFieldInfo();
            battleLock.notifyAll();
        }

        return battleFieldInfo;
    }

    public void setAllyReadyForContest(String battleName, String allyName, String assignmentSize) {
        BattleField battleField;
        synchronized (battleLock){
            battleField = this.battleFields.get(battleName);
            battleLock.notifyAll();
        }

        battleField.setAllyReadyForContest(allyName, assignmentSize);
    }

    public void assembleContest(String battleName, String processedMessage) {
        BattleField battleField;
        synchronized (battleLock){
            battleField = this.battleFields.get(battleName);
            battleLock.notifyAll();
        }

        battleField.assembleContest(processedMessage);
    }

    public MessageProcessDTO processMessage(String battleName, String messageToProcess) {
        BattleField battleField;
        synchronized (battleLock){
            battleField = this.battleFields.get(battleName);
            battleLock.notifyAll();
        }
        MessageProcessDTO messageProcessDTO =
                battleField.processMessage(messageToProcess);

        return messageProcessDTO;
    }

    public CodeConfigInfo setCodeConfig(String battleName, CodeConfigInfo inputConfig) {
        BattleField battleField;
        synchronized (battleLock){
            battleField = this.battleFields.get(battleName);
            battleLock.notifyAll();
        }
        CodeConfigInfo configInfo = battleField.setCodeConfig(inputConfig);

        return configInfo;
    }

    public CodeConfigInfo generateCodeConfig(String battleName) {
        BattleField battleField;
        synchronized (battleLock){
            battleField = this.battleFields.get(battleName);
            battleLock.notifyAll();
        }
        CodeConfigInfo configInfo = battleField.generateCodeConfig();

        return configInfo;
    }

    public AssignmentDTOList getAssignments(String battleName, String allyName, Integer numOfAssignmentsPerDraw) {
        BattleField battleField;
        synchronized (battleLock){
            battleField = this.battleFields.get(battleName);
            battleLock.notifyAll();
        }
        AssignmentDTOList assignmentDTOList =
                battleField.getAssignments(allyName, numOfAssignmentsPerDraw);

        return assignmentDTOList;
    }

    public byte[] getSerMachineInventory(String battleFiledName) {
        BattleField battleField;
        synchronized (battleLock){
            battleField = this.battleFields.get(battleFiledName);
            battleLock.notifyAll();
        }
        byte[] serMachine =
                battleField.getSerMachineInventory();

        return serMachine;
    }

    public CodeConfigInfo resetCodeConfig(String battleName) {
        BattleField battleField;
        synchronized (battleLock){
            battleField = this.battleFields.get(battleName);
            battleLock.notifyAll();
        }
        CodeConfigInfo configInfo =
                battleField.resetCode();

        return configInfo;
    }

    public void setWinner(String battleName, String winner) {
        BattleField battleField;
        synchronized (battleLock){
            battleField = this.battleFields.get(battleName);
            battleLock.notifyAll();
        }
        battleField.setWinner(winner);
    }

    public void setContestStatus(String battleName, String status) {
        BattleField battleField;
        synchronized (battleLock){
            battleField = this.battleFields.get(battleName);
            battleLock.notifyAll();
        }
        battleField.setStatus(status);
    }

    public void removeContest(String battleName) {
        synchronized (battleLock){
            battleFields.remove(battleName);
            battleLock.notifyAll();
        }

    }

    public void clearContest(String battleName) {
        BattleField battleField;
        synchronized (battleLock){
            battleField = this.battleFields.get(battleName);
            battleLock.notifyAll();
        }
        battleField.clearContest();
    }

    public void removeAlly(String battleName, String allyName) {
        BattleField battleField;
        synchronized (battleLock){
            battleField = this.battleFields.get(battleName);
            battleLock.notifyAll();
        }

        battleField.removeAlly(allyName);
    }
}

