package enigma.machine.inventory;

import enigma.machine.reflector.Reflector;
import enigma.machine.rotor.Rotor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CTEInventory implements Serializable {
    private String machineABC = null;

    private int rotorsCount = 0;

    private List<Rotor> availableRotors = null;

    private List<Reflector> availableReflectors = null;

    private int numberOfMaxAgents = 0;

    private Set<String> words = null;

    private String excludeWords = "";

    public CTEInventory (String abc, int rotorsCount){
        this.machineABC = abc;
        this.rotorsCount = rotorsCount;
        this.availableReflectors = new ArrayList<>();
        this.availableRotors = new ArrayList<>();
        this.words = new HashSet<>();
    }

    public void addRotor(Rotor newRotor){
        this.availableRotors.add(newRotor);
    }

    public void addReflector(Reflector newReflector) {
        this.availableReflectors.add(newReflector);
    }

    public Rotor getRotor(int rotorID){

        for(Rotor rotor : this.availableRotors){
            if(rotor.getID() == rotorID){
                return rotor;
            }
        }

        return null;
    }

    public Reflector getReflector(String reflectorID){

        for(Reflector reflector : this.availableReflectors){
            if(reflector.getID().equals(reflectorID)){
                return reflector;
            }
        }

        return null;
    }

    public String getABC(){
        return this.machineABC;
    }

    public int getRotorsCount() {
        return this.rotorsCount;
    }

    public int getNumberOfAvailableRotors(){
        return this.availableRotors.size();
    }

    public int getNumberOfAvailableReflectors() {
        return this.availableReflectors.size();
    }

    public List<Integer> getAvailableRotorIDs() {
        List<Integer> IDs = new ArrayList<>();

        for(Rotor rotor : this.availableRotors){
            IDs.add(rotor.getID());
        }

        return IDs;
    }

    public List<String> getAvailableReflectorIDs() {
        List<String> IDs = new ArrayList<>();

        for(Reflector reflector : this.availableReflectors){
            IDs.add(reflector.getID());
        }

        return IDs;
    }

    public void setExcludeWords(String excludeChars) {
        this.excludeWords = excludeChars.toUpperCase();
    }

    public void setWords(Set<String> words) {
        this.words = words;
    }

    public void setNumOfAgents(int agents) {
        this.numberOfMaxAgents = agents;
    }

    public Set<String> getWords() {

        return this.words;
    }

    public int getNumberOfMaxAgents() {
        return this.numberOfMaxAgents;
    }

    public String getExcludeWords() {
        return this.excludeWords;
    }

    public List<Reflector> getAvailableReflectors() {

        return this.availableReflectors;
    }

    public void setABC(String s) {
        this.machineABC = s;
    }
}
