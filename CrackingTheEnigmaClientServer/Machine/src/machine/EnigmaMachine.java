package machine;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dto.codeconfig.CodeConfigInfo;
import machine.codeconfiguration.CodeConfiguration;
import machine.history.MachineHistory;
import machine.rotor.Rotor;
import machine.reflector.Reflector;


public class EnigmaMachine implements Serializable {
    private String alphabet = null;

    private List<Rotor> rotors = null;

    private Reflector reflector = null;

    private MachineHistory currentMachineHistory = null;

    private boolean isPlugBoardUsed = false;

    private boolean isConfigured = false;

    private int numOfProcessedMessages = 0;

    private int rotorsCount = 0;

    public EnigmaMachine(){this.currentMachineHistory = new MachineHistory();}

    public void setRotorsCount(int rotorsCount) {
        this.rotorsCount = rotorsCount;
        this.rotors = new ArrayList<>(this.rotorsCount);
    }

    public void setABC(String abc) {
        this.alphabet = abc;
    }

    public void addRotor(Rotor rotorToAdd) {
        this.rotors.add(rotorToAdd);
    }

    public String process(String messageToEncrypt) {
        String res = "";

        for (Character ch : messageToEncrypt.toCharArray()) {
            res += this.encrypt(ch);
        }

        return res;
    }

    public char encrypt(char charToEncrypt) {
        int charIndex = -1;
        this.advanceRotors();
        charIndex = this.alphabet.lastIndexOf(charToEncrypt);

        for (int i = 0; i < this.rotors.size(); i++) {
            charIndex = this.rotors.get(i).encrypt(charIndex, true);
        }

        charIndex = this.reflector.reflect(charIndex);

        for (int i = this.rotors.size() - 1; i >= 0; i--) {
            charIndex = this.rotors.get(i).encrypt(charIndex, false);
        }

        return this.alphabet.charAt(charIndex);
    }

    private void advanceRotors() {
        this.rotors.get(0).advance();
        boolean isCheckWindowPos = true;

        for (int i = 0; i < this.rotors.size() - 1; i++) {
            if (isCheckWindowPos && this.rotors.get(i).isNotchAtWindowPosition()) {
                this.rotors.get(i + 1).advance();
                isCheckWindowPos = true;
            } else {
                isCheckWindowPos = false;
            }
        }
    }

    public void addReflector(Reflector inputReflector) {
        this.reflector = inputReflector;
    }

    public boolean setRotorsStartingPosition(int rotorIndex, String startingPos) {
        return this.rotors.get(rotorIndex).setStartingPosition(startingPos);
    }

    public void setIsConfigured(boolean isConfigured) {
        this.isConfigured = isConfigured;
    }

    public void clearCodeConfig() {
        this.rotors.clear();
        this.reflector = null;
        this.isPlugBoardUsed = false;
    }

    public void resetMachine() {
        this.alphabet = "";
        this.isPlugBoardUsed = false;
        this.isConfigured = false;
        this.numOfProcessedMessages = 0;
        this.clearCodeConfig();
    }

    public List<Rotor> getMachineRotors() {
        return this.rotors;
    }

    public CodeConfiguration getCurrentComponents() {
        CodeConfiguration currentConfig = new CodeConfiguration();
        currentConfig.setRotorsOrder(this.rotors);
        currentConfig.setReflector(this.reflector);

        return currentConfig;
    }

    public CodeConfigInfo setCodeConfig(CodeConfiguration code){
        List<Integer> rotorsOrder = new ArrayList<>();
        List<String> rotorsStartingPos = new ArrayList<>();
        List<Integer> rotorsNotchPosFromWindow = new ArrayList<>();
        String reflectorID = "";

        this.rotors = code.getRotorsOrder();
        int rotorIndex = 0;

        for(Rotor rotor : rotors){
            rotorsOrder.add(rotor.getID());
            rotor.setStartingPosition(String.format("%s", code.getRotorsPositions().charAt(rotorIndex)));
            rotorsStartingPos.add(rotor.getWindowPos());
            rotorsNotchPosFromWindow.add(rotor.getNotchPosFromWindow());
            rotorIndex++;
        }

        this.reflector = code.getReflector();
        reflectorID = this.reflector.getID();

        return new CodeConfigInfo(
                rotorsOrder,
                code.getRotorsPositions(),
                reflectorID,
                rotorsNotchPosFromWindow
        );
    }

    public void addCodeConfigToCurrentHistory(CodeConfigInfo setCodeConfig) {
        this.currentMachineHistory.addNewCodeConfig(setCodeConfig);
    }

    public List<CodeConfigInfo> getCurrentMachineHistory() {

        return this.currentMachineHistory.getCodeConfigStats();
    }

    public CodeConfigInfo getCurrentCodeConfig() {
        List<Integer> rotorsOrder = new ArrayList<>();
        List<Integer> rotorsNotchPos = new ArrayList<>();
        String rotorsPos = "";
        String reflector = this.reflector.getID();

        for(Rotor rotor : this.rotors){
            rotorsOrder.add(rotor.getID());
            rotorsNotchPos.add(rotor.getNotchPosFromWindow());
            rotorsPos += rotor.getWindowPos();
        }

        return
                new CodeConfigInfo(
                        rotorsOrder,
                        rotorsPos,
                        reflector,
                        rotorsNotchPos
                );
    }
}