package engine.managers;

import dto.battlefield.BattleFieldDTO;
import dto.battlefield.BattleFieldInfo;
import dto.codeconfig.CodeConfigInfo;
import dto.loadedmachine.LoadedMachineDTO;

import java.util.HashMap;
import java.util.Map;

public class BattleFieldManager {

    private Map<String, BattleField> battleFields = null;

    public BattleFieldManager() {
        this.battleFields = new HashMap<>();
    }

    public void addBattleField(
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

    public boolean isBattleExists(String name) {
        return this.battleFields.containsKey(name);
    }

    public synchronized BattleFieldDTO refreshBattleFields() {
        BattleFieldDTO data = new BattleFieldDTO();

        for(Map.Entry<String, BattleField> set : this.battleFields.entrySet()){
            data.addInfo(set.getValue().getBattleFieldInfo());
        }

        return data;
    }

    public synchronized BattleFieldInfo getBattleFieldInfo(String battleName) {

        return this.battleFields.get(battleName).getBattleFieldInfo();
    }

    public void setAllyReadyForContest(String battleName, String allyName, String assignmentSize) {
        this.battleFields.get(battleName).setAllyReadyForContest(allyName, assignmentSize);
    }

    public void assembleContest(String battleName, String toEncrypt) {
        this.battleFields.get(battleName).assembleContest(toEncrypt);
    }

    public String processMessage(String battleName, String messageToProcess) {

        return this.battleFields.get(battleName).processMessage(messageToProcess);
    }

    public CodeConfigInfo setCodeConfig(String battleName, CodeConfigInfo inputConfig) {

        return this.battleFields.get(battleName).setCodeConfig(inputConfig);
    }

    public CodeConfigInfo generateCodeConfig(String battleName) {

        return this.battleFields.get(battleName).generateCodeConfig();
    }
}

