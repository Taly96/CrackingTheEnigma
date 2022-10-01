package enigma.machine;

import enigma.inventory.MachineInventory;

public class MachineManager {

    private EnigmaMachine theEnigma = null;

    public MachineManager(){
        this.theEnigma = new EnigmaMachine();
    }

    public void configureMachine(MachineInventory theEnigmaInventory) {
        // TODO: create a new machine with the relevant data in inventory
    }
}
