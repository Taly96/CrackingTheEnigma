package enigma.engine.managers;

import dto.loadedmachine.LoadedMachineDTO;
import enigma.machine.EnigmaMachine;
import enigma.xml.generated.CTEEnigma;

public class MachineManager {

    private InventoryManager machineInventory = null;

    private EnigmaMachine theEnigma = null;

    public MachineManager(){
        this.machineInventory = new InventoryManager();
        this.theEnigma = new EnigmaMachine();
    }

    public LoadedMachineDTO configureMachine(CTEEnigma currentLoadedMachine) {
        LoadedMachineDTO currentLoadedMachineDTO =
                this.machineInventory.configureManager(currentLoadedMachine);
        this.theEnigma.setABC(this.machineInventory.getABC());
        this.theEnigma.setRotorsCount(this.machineInventory.getRotorsCount());

        return currentLoadedMachineDTO;

    }
}
