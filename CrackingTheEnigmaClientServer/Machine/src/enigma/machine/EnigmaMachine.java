package enigma.machine;

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

    private PlugBoard plugBoard = null;


    //private MachineHistory currentMachineHistory = null;

    private boolean isPlugBoardUsed = false;

    private boolean isConfigured = false;

    private int numOfProcessedMessages = 0;

    private int rotorsCount = 0;

    public EnigmaMachine(){
        //this.rotors = new ArrayList<>();
        this.plugBoard = new PlugBoard();
        //this.currentMachineHistory = new MachineHistory();
    }

//    public List<CodeConfigStatsDTO> getCurrentMachineHistory(){
//        return this.currentMachineHistory.getCodeConfigStats();
//    }

    public void setRotorsCount(int rotorsCount){
        this.rotorsCount = rotorsCount;
        this.rotors = new ArrayList<>(this.rotorsCount);
    }

    public void setABC(String abc){
        this.alphabet = abc;
    }

    public void addRotor(Rotor rotorToAdd) {
        this.rotors.add(rotorToAdd);
    }

    public String process(String messageToEncrypt){
        String res = "";

        for(Character ch : messageToEncrypt.toCharArray()){
            res += this.encrypt(ch);
        }

        return res;
    }

    public char encrypt (char charToEncrypt){
        int charIndex = -1;
        this.advanceRotors();
        if (this.isPlugBoardUsed && this.plugBoard.isExistingPlug(charToEncrypt)){
            charIndex = this.alphabet.lastIndexOf(this.plugBoard.encrypt(charToEncrypt));
        }
        else{
            charIndex = this.alphabet.lastIndexOf(charToEncrypt);
        }

        for (int i = 0; i < this.rotors.size(); i++){
            charIndex = this.rotors.get(i).encrypt(charIndex, true);
        }

        charIndex = this.reflector.reflect(charIndex);

        for (int i = this.rotors.size() - 1; i >= 0; i--){
            charIndex = this.rotors.get(i).encrypt(charIndex, false);
        }

        if (this.isPlugBoardUsed && this.plugBoard.isExistingPlug(this.alphabet.charAt(charIndex))){
            charIndex = this.alphabet.lastIndexOf(this.plugBoard.encrypt(this.alphabet.charAt(charIndex)));
        }

        return this.alphabet.charAt(charIndex);
    }

    private void advanceRotors() {
        this.rotors.get(0).advance();
        boolean isCheckWindowPos = true;

        for (int i = 0; i < this.rotors.size() - 1; i++){
            if (isCheckWindowPos && this.rotors.get(i).isNotchAtWindowPosition()){
                this.rotors.get(i+1).advance();
                isCheckWindowPos = true;
            }
            else{
                isCheckWindowPos = false;
            }
        }
    }

    public void addReflector(Reflector inputReflector){
        this.reflector = inputReflector;
    }

    public int getNumOfProcessedMessages() {
        return this.numOfProcessedMessages;
    }

    public boolean setRotorsStartingPosition(int rotorIndex, String startingPos) {
        return this.rotors.get(rotorIndex).setStartingPosition(startingPos);
    }

    public boolean addPlugToPlugBoard(String from, String to) {
        if(from == null && to == null){
            this.isPlugBoardUsed = false;
            return true;
        }
        else{
            this.isPlugBoardUsed = true;

            return this.plugBoard.addPlug(from.charAt(0), to.charAt(0));
        }
    }

    public void setIsConfigured(boolean isConfigured){
        this.isConfigured = isConfigured;
    }

    public void clearCodeConfig() {
        this.rotors.clear();
        this.plugBoard.clear();
        this.reflector = null;
        this.isPlugBoardUsed = false;
    }

    public void resetMachine() {
        this.alphabet = "";
        //this.currentMachineHistory.clear();
        this.isPlugBoardUsed = false;
        this.isConfigured = false;
        this.numOfProcessedMessages = 0;
        this.clearCodeConfig();
    }

//    public void addCodeConfigToCurrentHistory(CodeConfigDTO codeConfig) {
//        this.currentMachineHistory.addNewCodeConfig(codeConfig);
//    }

//    public void addMessageStatsToCurrentCodeConfig(InputMessageDTO newProcessRes){
//        this.currentMachineHistory.addStatsToCurrentCodeConfig(newProcessRes);
//        this.numOfProcessedMessages++;
//    }

    public List<Rotor> getMachineRotors() {
        return this.rotors;
    }

//    public CodeConfigDTO setCodeConfig(CodeConfiguration code){
//        List<Integer> rotorsOrder = new ArrayList<>();
//        List<String> rotorsStartingPos = new ArrayList<>();
//        List<Integer> rotorsNotchPosFromWindow = new ArrayList<>();
//        String reflectorID = "";
//
//        this.rotors = code.getRotorsOrder();
//        int rotorIndex = 0;
//
//        for(Rotor rotor : rotors){
//            rotorsOrder.add(rotor.getID());
//            rotor.setStartingPosition(String.format("%s", code.getRotorsPositions().charAt(rotorIndex)));
//            rotorsStartingPos.add(rotor.getWindowPos());
//            rotorsNotchPosFromWindow.add(rotor.getNotchPosFromWindow());
//            rotorIndex++;
//        }
//
//        this.reflector = code.getReflector();
//        reflectorID = this.reflector.getID();
//
//        return new CodeConfigDTO(
//                rotorsOrder,
//                rotorsStartingPos,
//                rotorsNotchPosFromWindow,
//                reflectorID,
//                null
//        );
//    }
//
//    public Reflector getMachineReflector() { return  this.reflector;}
//
    }