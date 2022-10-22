package engine.managers;

import dto.battlefield.BattleFieldInfo;
import dto.loadedmachine.LoadedMachineDTO;
import dto.staticinfo.StaticMachineDTO;
import machine.inventory.MachineInventory;
import machine.reflector.Reflector;
import machine.rotor.Rotor;
import engine.xml.generated.*;

import java.util.*;

public class InventoryManager {
    private MachineInventory theEnigmaInventory = null;

    public synchronized LoadedMachineDTO configureManager(CTEEnigma enigma){
        this.theEnigmaInventory =
                new MachineInventory(
                        enigma.getCTEMachine().getRotorsCount()
                );
        this.theEnigmaInventory.setAvailableReflectors(
                this.getAvailableReflectors(
                        enigma.getCTEMachine().getCTEReflectors()
                )
        );
        this.theEnigmaInventory.setAvailableRotors(
                this.getAvailableRotors(
                        enigma.getCTEMachine().getCTERotors()
                )
        );
        this.theEnigmaInventory.setBattleFieldInfo(
                this.getBattleFieldInfo(
                        enigma.getCTEBattlefield()
                )
        );
        this.theEnigmaInventory.setStaticMachineInfo(
                this.getStaticMachineInfo(
                        enigma.getCTEDecipher(),
                        enigma.getCTEMachine().getABC().toUpperCase().trim()
                )
        );

        List<Integer> availableRotors = this.getAvailableRotorsIDList();
        List<String> availableReflectors = this.getAvailableReflectorsIDList();

        return new LoadedMachineDTO(
                this.theEnigmaInventory.getStaticMachineInfo().getAbc(),
                this.theEnigmaInventory.getRotorsCount(),
                availableRotors,
                availableReflectors,
                this.theEnigmaInventory.getBattleFieldInfo(),
                this.theEnigmaInventory.getStaticMachineInfo()
        );
    }

    private synchronized List<String> getAvailableReflectorsIDList() {
        List<String> res = new ArrayList<>();

        for(Reflector reflectors : this.theEnigmaInventory.getAvailableReflectors()){
            res.add(reflectors.getID());
        }

        return res;
    }

    private synchronized List<Integer> getAvailableRotorsIDList() {
        List<Integer> res = new ArrayList<>();

        for(Rotor rotor : this.theEnigmaInventory.getAvailableRotors()){
            res.add(rotor.getID());
        }

        return res;
    }

    private synchronized StaticMachineDTO getStaticMachineInfo(CTEDecipher cteDecipher, String abc) {

        Set<String> filteredWords =
                this.filterWords(
                        abc,
                        cteDecipher.getCTEDictionary().getWords().toUpperCase().trim(),
                        cteDecipher.getCTEDictionary().getExcludeChars().toUpperCase().trim()
                );

        return new StaticMachineDTO(
                abc,
                filteredWords,
                cteDecipher.getCTEDictionary().getExcludeChars().toUpperCase().trim()
        );
    }

    private synchronized Set<String> filterWords(String abc, String words, String excludeWords) {
        Set<String> filteredWords = new HashSet<>();
        StringTokenizer st = new StringTokenizer(words, " ");
        boolean isValidWord = true;

        while(st.hasMoreTokens()){
            String token =  st.nextToken();

            for(Character ch :token.toUpperCase().toCharArray()){
                if(!abc.contains(ch.toString())){
                    isValidWord = false;
                    break;
                }
                if(excludeWords.contains(ch.toString())){
                    token = token.replace(ch.toString(), "");
                }
            }

            if(isValidWord){
                filteredWords.add(token);
            }

            isValidWord = true;
        }

        return filteredWords;
    }

    private synchronized BattleFieldInfo getBattleFieldInfo(CTEBattlefield cteBattlefield) {

        return new BattleFieldInfo(
                cteBattlefield.getBattleName().trim(),
                cteBattlefield.getLevel().trim(),
                cteBattlefield.getAllies()
        );
    }

    private synchronized List<Rotor> getAvailableRotors(CTERotors cteRotors) {
        List<Rotor> availableRotors = new ArrayList<>();
        int notchIndex = 1;
        boolean isNotch = false;

        for(CTERotor rotor : cteRotors.getCTERotor()){
            Rotor newRotor = new Rotor(rotor.getId());

            for (CTEPositioning positioning : rotor.getCTEPositioning()) {
                if (notchIndex == rotor.getNotch()) {
                    isNotch = true;
                }
                newRotor.addRoute(
                        positioning.getLeft().toUpperCase().charAt(0),
                        positioning.getRight().toUpperCase().charAt(0),
                        isNotch);
                notchIndex++;
                isNotch = false;
            }

            notchIndex = 1;
            availableRotors.add(newRotor);
        }

        return availableRotors;
    }

    private synchronized List<Reflector> getAvailableReflectors(CTEReflectors cteReflectors) {
        List<Reflector> availableReflectors = new ArrayList<>();

        for (CTEReflector reflector : cteReflectors.getCTEReflector()) {
            Reflector newReflector =
                    new Reflector(
                            reflector.getId(),
                            reflector.getCTEReflect().size()
                    );

            for (CTEReflect reflection : reflector.getCTEReflect()) {
                newReflector.addReflection(reflection.getInput(), reflection.getOutput());
            }

            availableReflectors.add(newReflector);
        }

        return availableReflectors;
    }

    public synchronized String getABC() {

        return this.theEnigmaInventory.getStaticMachineInfo().getAbc();
    }

    public synchronized int getRotorsCount() {

        return this.theEnigmaInventory.getRotorsCount();
    }

    public synchronized Reflector getReflector(String reflectorID) {

        return this.theEnigmaInventory.getReflector(reflectorID);
    }

    public synchronized Rotor getRotor(Integer rotorID) {
        return this.theEnigmaInventory.getRotor(rotorID);
    }

    public synchronized List<Rotor> getAvailableRotorIDs() {

        return this.theEnigmaInventory.getAvailableRotors();
    }

    public synchronized List<Reflector> getAvailableReflectorIDs(){

        return this.theEnigmaInventory.getAvailableReflectors();
    }

    public synchronized MachineInventory getTheEnigmaInventory() {
        return theEnigmaInventory;
    }
}
