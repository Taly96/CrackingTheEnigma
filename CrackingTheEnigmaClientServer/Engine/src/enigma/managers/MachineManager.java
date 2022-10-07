package enigma.managers;

import dto.codeconfig.CodeConfigDTO;
import dto.loadedmachine.LoadedMachineDTO;
import enigma.machine.EnigmaMachine;
import enigma.machine.rotor.Rotor;
import enigma.xml.generated.CTEEnigma;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public CodeConfigDTO setCodeConfig(CodeConfigDTO inputConfig) {
        this.theEnigma.clearCodeConfig();
        this.setReflectorConfig(inputConfig.getReflectorID());
        List<Integer> rotorsPosFromNotch = this.setRotors(
                inputConfig.getRotorsID(),
                inputConfig.getRotorsPos());

        CodeConfigDTO setCodeConfig = new CodeConfigDTO(
                inputConfig.getRotorsID(),
                inputConfig.getRotorsStartingPos(),
                inputConfig.getReflectorID(),
                rotorsPosFromNotch
        );

        this.theEnigma.setIsConfigured(true);
//        if(!isReset){
//            this.machine.addCodeConfigToCurrentHistory(setCodeConfig);
//        }

        return setCodeConfig;
    }

    private List<Integer> setRotors(List<Integer> rotorsOrder, String rotorsStartingPos) {
        List<Integer> rotorsNotchPosFromWindow = new ArrayList<>();
        int rotorIndex = 0;

        for (Integer rotorID : rotorsOrder) {
            this.setRotorIDConfig(rotorID);
        }

        for (Character startingPos : rotorsStartingPos.toCharArray()) {
            this.setRotorStartingPositionsConfig(rotorIndex, startingPos.toString());
            rotorIndex++;
        }

        for(Rotor rotors : this.theEnigma.getMachineRotors()){
            rotorsNotchPosFromWindow.add(rotors.getNotchPosFromWindow());
        }

        return rotorsNotchPosFromWindow;
    }

    private void setRotorStartingPositionsConfig(int rotorIndex, String pos) {
        this.theEnigma.setRotorsStartingPosition(rotorIndex, pos);
    }

    private void setRotorIDConfig(Integer rotorID) {
        this.theEnigma.addRotor(this.machineInventory.getRotor(rotorID));
    }

    private void setReflectorConfig(String reflectorID) {
        this.theEnigma.addReflector(this.machineInventory.getReflector(reflectorID));
    }

    public CodeConfigDTO generateCodeConfig() {
        List<Integer> rotorsOrder = this.generateRotorsOrder();
        String rotorsStartingPoints = this.generateRotorsStartingPoints();
        String reflectorID = this.generateReflector();
        CodeConfigDTO setCodeConfig = this.setCodeConfig(
                new CodeConfigDTO(
                        rotorsOrder,
                        rotorsStartingPoints,
                        reflectorID
                )
        );

        return this.setCodeConfig(setCodeConfig);
    }

    private String generateReflector() {
        Random rand = new Random();
        int reflectorIndex = rand.nextInt(
                this.machineInventory.getAvailableReflectorIDs().size());

        return this.machineInventory.getAvailableReflectorIDs().get(reflectorIndex).getID();
    }

    private String generateRotorsStartingPoints() {
        int numOfPos = 0;
        StringBuilder rotorsStartingPoint = new StringBuilder();
        Random rand = new Random();
        String abc = this.machineInventory.getABC();

        while (numOfPos != this.machineInventory.getRotorsCount()) {
            int posIndex = rand.nextInt(abc.length());
            rotorsStartingPoint.append(String.format("%c", abc.charAt(posIndex)));
            numOfPos++;
        }

        return rotorsStartingPoint.toString();
    }

    private List<Integer> generateRotorsOrder() {
        List<Integer> rotorsIDOrder = new ArrayList<>();
        Random rand = new Random();

        while (rotorsIDOrder.size() != this.machineInventory.getRotorsCount()) {
            int rotorIndex = rand.nextInt(this.machineInventory.getAvailableRotorIDs().size());
            if (!rotorsIDOrder.contains(this.machineInventory.getAvailableRotorIDs().get(rotorIndex).getID())) {
                rotorsIDOrder.add(this.machineInventory.getAvailableRotorIDs().get(rotorIndex).getID());
            }
        }

        return rotorsIDOrder;
    }

    public String processMessage(String messageToProcess) {

        return this.theEnigma.process(messageToProcess);
    }
}
