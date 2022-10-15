package enigma.managers;

import dto.battlefield.BattleFieldDTO;
import dto.battlefield.BattleFieldInfo;
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

    public synchronized boolean isBattleExists(String name) {
        return this.battleFields.containsKey(name);
    }

    public synchronized BattleFieldDTO refreshBattleFields() {
        BattleFieldDTO data = new BattleFieldDTO();

        for(Map.Entry<String, BattleField> set : this.battleFields.entrySet()){
            data.addInfo(set.getValue().getBattleFieldInfo());
        }

        return data;
    }
}

