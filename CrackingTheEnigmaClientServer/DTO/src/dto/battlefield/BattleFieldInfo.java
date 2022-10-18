package dto.battlefield;

import java.io.Serializable;

public class BattleFieldInfo implements Serializable {

    private String battleFieldName = null;

    private String uBoat = null;

    private String level = null;

    private Integer neededNumOfAllies = null;

    private Integer registeredAllies = null;

    private String status = null;

    public BattleFieldInfo(
            String battleFieldName,
            String level,
            int neededNumOfAllies
    ) {
        this.battleFieldName = battleFieldName;
        this.level = level;
        this.neededNumOfAllies = neededNumOfAllies;
        this.status = "Inactive";
        this.registeredAllies = 0;
    }

    public String getBattleFieldName() {
        return battleFieldName;
    }

    public String getLevel() {
        return level;
    }

    public Integer getNeededNumOfAllies() {
        return neededNumOfAllies;
    }

    public Integer getRegisteredAllies() {
        return registeredAllies;
    }

    public String getStatus() {
        return status;
    }

    public String getUBoat() {
        return uBoat;
    }

    public void setRegisteredAllies(Integer registeredAllies) {
        this.registeredAllies = registeredAllies;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUBoat(String uBoat){
        this.uBoat = uBoat;
    }

    public void incrementAllies() {this.registeredAllies++;}
}
