package enigma.machine.inventory;

import dto.battlefield.BattleFieldDTO;
import dto.battlefield.BattleFieldInfo;
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

    private BattleFieldInfo battleFieldInfo = null;

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

    public BattleFieldInfo getBattleFieldInfo() {
        return this.battleFieldInfo;
    }

    public void setBattleFieldInfo(BattleFieldInfo battleFieldInfo) {
        this.battleFieldInfo = battleFieldInfo;
    }

    public Reflector getReflector(String reflectorID) {

        for(Reflector reflector : this.availableReflectors){
            if(reflector.getID().equals(reflectorID)){
                return reflector;
            }
        }

        return null;
    }

    public Rotor getRotor(Integer rotorID) {

        for(Rotor rotor : this.availableRotors){
            if(rotor.getID() == rotorID){
                return  rotor;
            }
        }

        return null;
    }
}
