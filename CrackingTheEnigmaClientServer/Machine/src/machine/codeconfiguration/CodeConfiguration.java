package machine.codeconfiguration;


import machine.reflector.Reflector;
import machine.rotor.Rotor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CodeConfiguration implements Serializable {

    private List<Rotor> rotorsOrder = null;

    private String rotorsPositions = null;

    private Reflector reflector = null;

    public CodeConfiguration () { this.rotorsOrder = new ArrayList<>(); }

    public List<Rotor> getRotorsOrder() {
        return this.rotorsOrder;
    }


    public String getRotorsPositions() {
        return this.rotorsPositions;
    }


    public Reflector getReflector() {
        return this.reflector;
    }


    public void setRotorsPositions(String positions) { this.rotorsPositions = positions; }


    public void setReflector(Reflector reflector) { this.reflector = reflector; }


    public void setRotorsOrder(List<Rotor> rotors){ this.rotorsOrder = rotors; }

}
