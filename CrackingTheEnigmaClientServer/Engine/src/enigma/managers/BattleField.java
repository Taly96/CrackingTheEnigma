package enigma.managers;

import dto.activeteams.AlliesDTO;
import dto.activeteams.AlliesInfo;
import dto.battlefield.BattleFieldInfo;
import dto.candidates.CandidatesDTO;
import dto.loadedmachine.LoadedMachineDTO;

public class BattleField {

    private BattleFieldInfo battleFieldInfo = null;

    private MachineManager machineManager= null;

    private DecipherManager decipherManager = null;

    private AlliesManager alliesManager = null;

    public BattleField(LoadedMachineDTO machineDTO, MachineManager machineManager){
        this.battleFieldInfo = machineDTO.getBattleFieldInfo();
        this.machineManager = machineManager;
        this.decipherManager = new DecipherManager(this.battleFieldInfo.getLevel());
        this.alliesManager = new AlliesManager();
    }

    public synchronized MachineManager getMachineManager() {
        return this.machineManager;
    }


    public synchronized DecipherManager getDecipherManager() {
        return this.decipherManager;
    }

    public synchronized AlliesDTO refreshAllies() {
        return this.alliesManager.refreshAllies();
    }

    public synchronized BattleFieldInfo getBattleFieldInfo(){ return this.battleFieldInfo; }

    public synchronized void addAlly(AlliesInfo newAlly) {
        this.alliesManager.addAlly(newAlly);
    }

    public synchronized CandidatesDTO refreshCandidates() {
        return this.alliesManager.refreshCandidates();
    }
}
