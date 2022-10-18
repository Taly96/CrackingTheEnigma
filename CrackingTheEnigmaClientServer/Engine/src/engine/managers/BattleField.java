package engine.managers;

import dm.decipher.DecipherManager;
import dto.battlefield.BattleFieldInfo;
import dto.loadedmachine.LoadedMachineDTO;

import java.util.HashMap;
import java.util.Map;

public class BattleField {

    private String messageToEncrypt = null;
    private BattleFieldInfo battleFieldInfo = null;

    private MachineManager machineManager= null;

    private Map<String, DecipherManager> contest = null;

    public BattleField(LoadedMachineDTO machineDTO, MachineManager machineManager){
        this.battleFieldInfo = machineDTO.getBattleFieldInfo();
        this.machineManager = machineManager;
        this.contest = new HashMap<>();
    }

    public synchronized MachineManager getMachineManager() {
        return this.machineManager;
    }

    public synchronized BattleFieldInfo getBattleFieldInfo(){ return this.battleFieldInfo; }

    public void startContest(String messageToEncrypt) {
        this.battleFieldInfo.setStatus("Waiting");
        this.messageToEncrypt = messageToEncrypt;
    }
}
