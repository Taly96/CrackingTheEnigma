package engine.xml.validator;

import engine.xml.generated.*;
import java.util.*;

public class CTEValidator {

    private String abcToValidate = null;

    private List<CTERotor> rotorsToValidate = null;

    private List<CTEReflector> reflectorsToValidate = null;

    private CTEMachine machineToValidate = null;

    private final Map<Integer,String> reflectorsValidIDs = new HashMap<>();

    private List<String> exceptionsToShowUser = null;

    private final int MAX_ROTORS = 99;

    private final int MIN_ROTORS = 2;

    private final int MAX_REFLECTORS = 5;

    private final int MIN_REFLECTORS = 1;


    public CTEValidator(){
        this.reflectorsValidIDs.put(1,"I");
        this.reflectorsValidIDs.put(2,"II");
        this.reflectorsValidIDs.put(3,"III");
        this.reflectorsValidIDs.put(4,"IV");
        this.reflectorsValidIDs.put(5,"V");
        this.exceptionsToShowUser = new ArrayList<>();
    }

    public List<String> isValid (CTEEnigma toValidate) {
        this.abcToValidate = toValidate.getCTEMachine().getABC().trim();
        this.rotorsToValidate = toValidate.getCTEMachine().getCTERotors().getCTERotor();
        this.reflectorsToValidate = toValidate.getCTEMachine().getCTEReflectors().getCTEReflector();
        this.machineToValidate = toValidate.getCTEMachine();
        this.isEvenABC();
        this.isValidRotorsConfig();
        this.isValidReflectorsConfig();

        return this.exceptionsToShowUser;
    }

    private void isValidRotorsConfig() {
        this.isValidNumOfRotors();
        this.isValidRotorsIdentification();
        this.isValidRotorsMapping();
        this.isValidRotorsNotchSet();
    }

    private void isValidReflectorsConfig() {
        this.isValidReflectorsIdentification();
        this.isValidReflectorsMapping();
    }
    private void isEvenABC() {
        if((this.abcToValidate.length() % 2) != 0 || this.abcToValidate.isEmpty()){
            this.exceptionsToShowUser.add("num of characters is invalid.");
        }
    }

    private void isValidNumOfRotors() {
        if(this.machineToValidate.getRotorsCount() < MIN_ROTORS){
            this.exceptionsToShowUser.add("rotors count <= 1");
        }
        if(this.machineToValidate.getRotorsCount() > this.rotorsToValidate.size()){
            this.exceptionsToShowUser.add("rotors count is higher than actual number of rotors provided.");
        }

        if(this.machineToValidate.getRotorsCount() > MAX_ROTORS){
            this.exceptionsToShowUser.add("number of rotors needed is out of range.");
        }
    }

    private void isValidRotorsIdentification() {
        Set<Integer> rotorsIDs = new HashSet<>();

        for (CTERotor rotor : this.rotorsToValidate) {
            if(rotor.getId() <= 0){
                this.exceptionsToShowUser.add("invalid rotor ID for rotor " + rotor.getId() + ".");
            }
            if(!rotorsIDs.add(rotor.getId())){
                this.exceptionsToShowUser.add("rotor " + rotor.getId() + " shares ID# with another rotor.");
            }
        }

        for(int i = 1; i <= rotorsIDs.size(); i++){
            if(!rotorsIDs.contains(i)){
                this.exceptionsToShowUser.add("invalid rotors identification, ID# " + i + " is missing.");
            }
        }
    }

    private void isValidRotorsMapping() {
        Set<String > rights = new HashSet<>();
        Set<String > lefts = new HashSet<>();

        for (CTERotor rotor : this.rotorsToValidate){
            if(rotor.getCTEPositioning().size() != this.abcToValidate.length()){
                this.exceptionsToShowUser.add("invalid number of mappings in rotor " + rotor.getId() + ".");
            }
            for(CTEPositioning positioning : rotor.getCTEPositioning()) {
                if(!rights.add(positioning.getRight().toUpperCase())){
                    this.exceptionsToShowUser.add((
                            positioning.getRight().toUpperCase() + " is mapped twice in rotor " + rotor.getId() + "."));
                }
                if(!lefts.add(positioning.getLeft().toUpperCase())){
                    this.exceptionsToShowUser.add(
                            positioning.getLeft().toUpperCase() + " is mapped twice in rotor " + rotor.getId() + ".");
                }
            }

            rights.clear();
            lefts.clear();
        }
    }

    private void isValidRotorsNotchSet() {

        for (CTERotor rotor : this.rotorsToValidate) {
            if(rotor.getNotch() > this.abcToValidate.length() ||
                    rotor.getNotch() <= 0){
                this.exceptionsToShowUser.add("the notch in rotor " + rotor.getId() + " is out of range.");
            }
        }
    }

    private  void isValidReflectorsIdentification() {
        Set<String> reflectorsIDs = new HashSet<>();

        if(reflectorsToValidate.size() > MAX_REFLECTORS || reflectorsToValidate.size() < MIN_REFLECTORS ){
            this.exceptionsToShowUser.add(
                    "Number of reflectors is out of range (should be up to 5 and at least 1).");
        }

        for(CTEReflector reflector : this.reflectorsToValidate){
            if(!this.reflectorsValidIDs.containsValue(reflector.getId())){
                this.exceptionsToShowUser.add(
                        "invalid identification for reflector " + reflector.getId() + ".");
            }
            if(!reflectorsIDs.add(reflector.getId())){
                this.exceptionsToShowUser.add(
                        "reflector " + reflector.getId() + " shares identification with another reflector.");
            }
        }

        for(int i = 0; i < reflectorsIDs.size(); i++){
            if(!reflectorsIDs.contains(this.reflectorsValidIDs.get(i + 1))){
                this.exceptionsToShowUser.add("Invalid reflectors identification, " +
                        "reflector "
                        + this.reflectorsValidIDs.get(i + 1) +
                        " is missing.");
            }
        }
    }

    private void isValidReflectorsMapping() {
        Set<Integer> input = new HashSet<>();
        Set<Integer> output = new HashSet<>();

        for(CTEReflector reflector : this.reflectorsToValidate){
            if(reflector.getCTEReflect().size() != (this.abcToValidate.length()) / 2){
                this.exceptionsToShowUser.add("invalid number of reflections in reflector " + reflector.getId() + ".");
            }

            for(CTEReflect reflect : reflector.getCTEReflect()){
                if(!input.add(reflect.getInput())){
                    this.exceptionsToShowUser.add("reflector " + reflector.getId() +
                            " reflects " + reflect.getInput() + " more than once.");
                }
                if(!output.add(reflect.getOutput())){
                    this.exceptionsToShowUser.add("reflector " + reflector.getId() +
                            " uses " + reflect.getOutput() + " as a reflection more than once.");
                }
                if(reflect.getInput() == reflect.getOutput()){
                    this.exceptionsToShowUser.add("self reflection in reflector " + reflector.getId() + ".");
                }
                if(reflect.getInput() < 1 ||
                        reflect.getOutput() < 1 ||
                        reflect.getInput() > this.abcToValidate.length() ||
                        reflect.getOutput() > this.abcToValidate.length()){
                    this.exceptionsToShowUser.add("reflection out of range in " + reflector.getId() + ".");
                }
            }

            for(int i = 1; i <= this.abcToValidate.length(); i++){
                if(!output.contains(i) && !input.contains(i)){
                    this.exceptionsToShowUser.add("reflector " + reflector.getId() + " is missing the mapping for " +
                            this.abcToValidate.charAt(i - 1) + ".");
                }
            }

            output.clear();
            input.clear();
        }
    }
}
