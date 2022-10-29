package dto.codeconfig;

import java.io.Serializable;
import java.util.List;

public class CodeConfigInfo implements Serializable {

    private List<Integer> rotorsID = null;

    private String rotorsPos = null;

    private List<Integer> rotorsNotchPos = null;

    private String reflectorID = null;

    public CodeConfigInfo(
            List<Integer> rotorsIDs,
            String rotorsStartingPos,
            String reflectorID
    ){
        this.reflectorID = reflectorID;
        this.rotorsID = rotorsIDs;
        this.rotorsPos = rotorsStartingPos;
    }

    public CodeConfigInfo(
            List<Integer> rotorsIDs,
            String rotorsStartingPos,
            String reflectorID,
            List<Integer> rotorsNotchPos
    ){
        this.reflectorID = reflectorID;
        this.rotorsID = rotorsIDs;
        this.rotorsPos = rotorsStartingPos;
        this.rotorsNotchPos = rotorsNotchPos;
    }

    public List<Integer> getRotorsID() {
        return this.rotorsID;
    }

    public String getRotorsPos() {
        return this.rotorsPos;
    }

    public List<Integer> getRotorsNotchPos() {
        return this.rotorsNotchPos;
    }

    public String getReflectorID() {
        return this.reflectorID;
    }

    public String getRotorsStartingPos() {

        return this.rotorsPos;
    }
}
