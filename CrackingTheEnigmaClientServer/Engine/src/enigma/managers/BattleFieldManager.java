package enigma.managers;

import java.util.HashMap;
import java.util.Map;

public class BattleFieldManager {

    private Map<String, MachineManager> battleFields = null;

    public BattleFieldManager(){
        this.battleFields = new HashMap<>();
    }

    public synchronized void addBattleField(String name, MachineManager machineManager){
        this.battleFields.put(name, machineManager);
    }

    public synchronized MachineManager getBattleField(String name){
        return this.battleFields.get(name);
    }

    public boolean isBattleExists(String name){
        return this.battleFields.containsKey(name);
    }
}
