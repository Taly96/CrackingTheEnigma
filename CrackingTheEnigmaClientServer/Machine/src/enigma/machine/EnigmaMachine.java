package enigma.machine;

import enigma.machine.codeconfiguration.CodeConfiguration;
import enigma.machine.plugboard.PlugBoard;
import enigma.machine.reflector.Reflector;
import enigma.machine.rotor.Rotor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnigmaMachine implements Serializable {
    private String alphabet = null;

    private List<Rotor> rotors = null;

    private Reflector reflector = null;

    //private PlugBoard plugBoard = null;


    //private MachineHistory currentMachineHistory = null;

    private boolean isPlugBoardUsed = false;

    private boolean isConfigured = false;

    private int numOfProcessedMessages = 0;

    private int rotorsCount = 0;

    public EnigmaMachine() {
    }

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

    public int getNumOfProcessedMessages() {
        return this.numOfProcessedMessages;
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
}