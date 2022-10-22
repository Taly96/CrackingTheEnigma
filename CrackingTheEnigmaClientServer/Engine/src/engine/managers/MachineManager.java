package engine.managers;

import dto.codeconfig.CodeConfigInfo;
import dto.decipher.DecipherDTO;
import dto.loadedmachine.LoadedMachineDTO;
import machine.EnigmaMachine;
import machine.codeconfiguration.CodeConfiguration;
import machine.rotor.Rotor;
import engine.xml.generated.CTEEnigma;
import dm.utils.Utils;

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

    public synchronized LoadedMachineDTO configureMachine(CTEEnigma currentLoadedMachine) {
        LoadedMachineDTO currentLoadedMachineDTO =
                this.machineInventory.configureManager(currentLoadedMachine);
        this.theEnigma.setABC(this.machineInventory.getABC());
        this.theEnigma.setRotorsCount(this.machineInventory.getRotorsCount());

        return currentLoadedMachineDTO;
    }

    public synchronized CodeConfigInfo setCodeConfig(CodeConfigInfo inputConfig) {
        this.theEnigma.clearCodeConfig();
        this.setReflectorConfig(inputConfig.getReflectorID());
        List<Integer> rotorsPosFromNotch = this.setRotors(
                inputConfig.getRotorsID(),
                inputConfig.getRotorsPos());

        CodeConfigInfo setCodeConfig = new CodeConfigInfo(
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

    private synchronized List<Integer> setRotors(List<Integer> rotorsOrder, String rotorsStartingPos) {
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

    private synchronized void setRotorStartingPositionsConfig(int rotorIndex, String pos) {
        this.theEnigma.setRotorsStartingPosition(rotorIndex, pos);
    }

    private synchronized void setRotorIDConfig(Integer rotorID) {
        this.theEnigma.addRotor(this.machineInventory.getRotor(rotorID));
    }

    private synchronized void setReflectorConfig(String reflectorID) {
        this.theEnigma.addReflector(this.machineInventory.getReflector(reflectorID));
    }

    public synchronized CodeConfigInfo generateCodeConfig() {
        List<Integer> rotorsOrder = this.generateRotorsOrder();
        String rotorsStartingPoints = this.generateRotorsStartingPoints();
        String reflectorID = this.generateReflector();
        CodeConfigInfo setCodeConfig = this.setCodeConfig(
                new CodeConfigInfo(
                        rotorsOrder,
                        rotorsStartingPoints,
                        reflectorID
                )
        );

        return this.setCodeConfig(setCodeConfig);
    }

    private synchronized String generateReflector() {
        Random rand = new Random();
        int reflectorIndex = rand.nextInt(
                this.machineInventory.getAvailableReflectorIDs().size());

        return this.machineInventory.getAvailableReflectorIDs().get(reflectorIndex).getID();
    }

    private synchronized String generateRotorsStartingPoints() {
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

    private synchronized List<Integer> generateRotorsOrder() {
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

    public synchronized String processMessage(String messageToProcess) {

        return this.theEnigma.process(messageToProcess);
    }

    public synchronized InventoryManager getMachineInventoryManager() {
        return machineInventory;
    }

    public synchronized DecipherDTO getDecipherDTO() {
        boolean isValid = true;
        byte[] serInventory =
                Utils.fromObjectToByteArray(
                        this.machineInventory.getTheEnigmaInventory()
                );
        CodeConfiguration knownComponents =
                this.theEnigma.getCurrentComponents();

        switch (this.machineInventory.getTheEnigmaInventory().getBattleFieldInfo().getLevel()) {
            case "Easy":{
                knownComponents.setRotorsPositions("");
                break;
            }
            case "Medium":
            case "Hard": {
                knownComponents.setRotorsPositions("");
                knownComponents.setReflector(null);
                break;
            }
            case "Advanced":{
                knownComponents.setRotorsPositions("");
                knownComponents.setReflector(null);
                knownComponents.setRotorsOrder(null);
                break;
            }
            default:{
                isValid = false;
                break;
            }
        }

        if(isValid){
            byte[] serKnownComponents =
                    Utils.fromObjectToByteArray(
                            knownComponents
                    );
            return new DecipherDTO(
                    serKnownComponents,
                    serInventory
            );
        }

        return null;
    }
}
