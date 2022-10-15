package dto.codeconfig;

import java.util.List;

public class CodeConfigInfo {

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

    public void setRotorsID(List<Integer> rotorsID) {
        this.rotorsID = rotorsID;
    }

    public String getRotorsPos() {
        return this.rotorsPos;
    }

    public void setRotorsPos(String rotorsPos) {
        this.rotorsPos = rotorsPos;
    }

    public List<Integer> getRotorsNotchPos() {
        return this.rotorsNotchPos;
    }

    public void setRotorsNotchPos(List<Integer> rotorsNotchPos) {
        this.rotorsNotchPos = rotorsNotchPos;
    }

    public String getReflectorID() {
        return this.reflectorID;
    }

    public void setReflectorID(String reflectorID) {
        this.reflectorID = reflectorID;
    }

    public String getRotorsStartingPos() {

        return this.rotorsPos;
    }
}
