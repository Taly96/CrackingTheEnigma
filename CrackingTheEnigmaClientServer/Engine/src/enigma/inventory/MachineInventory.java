package enigma.inventory;

import dto.battlefield.BattleFieldDTO;
import dto.staticinfo.StaticMachineDTO;
import enigma.machine.reflector.Reflector;
import enigma.machine.rotor.Rotor;

import java.io.Serializable;
import java.util.List;

public class MachineInventory implements Serializable {

    private int rotorsCount = 0;

    private List<Rotor> availableRotors = null;

    private List<Reflector> availableReflectors = null;

    private StaticMachineDTO staticMachineInfo = null;

    private BattleFieldDTO battleFieldInfo = null;

    public MachineInventory(int rotorsCount){
        this.rotorsCount = rotorsCount;
    }

    public int getRotorsCount() {
        return this.rotorsCount;
    }

    public void setRotorsCount(int rotorsCount) {
        this.rotorsCount = rotorsCount;
    }

    public List<Rotor> getAvailableRotors() {
        return this.availableRotors;
    }

    public void setAvailableRotors(List<Rotor> availableRotors) {
        this.availableRotors = availableRotors;
    }

    public List<Reflector> getAvailableReflectors() {
        return this.availableReflectors;
    }

    public void setAvailableReflectors(List<Reflector> availableReflectors) {
        this.availableReflectors = availableReflectors;
    }

    public StaticMachineDTO getStaticMachineInfo() {
        return this.staticMachineInfo;
    }

    public void setStaticMachineInfo(StaticMachineDTO staticMachineInfo) {
        this.staticMachineInfo = staticMachineInfo;
    }

    public BattleFieldDTO getBattleFieldInfo() {
        return this.battleFieldInfo;
    }

    public void setBattleFieldInfo(BattleFieldDTO battleFieldInfo) {
        this.battleFieldInfo = battleFieldInfo;
    }
}
