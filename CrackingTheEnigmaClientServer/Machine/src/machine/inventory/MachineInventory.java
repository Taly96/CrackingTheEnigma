package machine.inventory;

import dto.battlefield.BattleFieldInfo;
import dto.staticinfo.StaticMachineDTO;
import machine.reflector.Reflector;
import machine.rotor.Rotor;

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

    public synchronized int getRotorsCount() {
        return this.rotorsCount;
    }

    public synchronized void setRotorsCount(int rotorsCount) {
        this.rotorsCount = rotorsCount;
    }

    public synchronized List<Rotor> getAvailableRotors() {
        return this.availableRotors;
    }

    public synchronized void setAvailableRotors(List<Rotor> availableRotors) {
        this.availableRotors = availableRotors;
    }

    public synchronized List<Reflector> getAvailableReflectors() {
        return this.availableReflectors;
    }

    public synchronized void setAvailableReflectors(List<Reflector> availableReflectors) {
        this.availableReflectors = availableReflectors;
    }

    public synchronized StaticMachineDTO getStaticMachineInfo() {
        return this.staticMachineInfo;
    }

    public synchronized void setStaticMachineInfo(StaticMachineDTO staticMachineInfo) {
        this.staticMachineInfo = staticMachineInfo;
    }

    public synchronized BattleFieldInfo getBattleFieldInfo() {
        return this.battleFieldInfo;
    }

    public synchronized void setBattleFieldInfo(BattleFieldInfo battleFieldInfo) {
        this.battleFieldInfo = battleFieldInfo;
    }

    public synchronized Reflector getReflector(String reflectorID) {

        for(Reflector reflector : this.availableReflectors){
            if(reflector.getID().equals(reflectorID)){
                return reflector;
            }
        }

        return null;
    }

    public synchronized Rotor getRotor(Integer rotorID) {

        for(Rotor rotor : this.availableRotors){
            if(rotor.getID() == rotorID){
                return  rotor;
            }
        }

        return null;
    }

    public synchronized void clear() {
        this.rotorsCount = 0;
        this.availableRotors.clear();
        this.availableReflectors.clear();
        this.staticMachineInfo.clear();
    }
}
