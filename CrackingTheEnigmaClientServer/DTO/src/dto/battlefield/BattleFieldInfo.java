package dto.battlefield;

import java.io.Serializable;

public class BattleFieldInfo implements Serializable {

    private String battleFieldName = null;

    private String messageToDecipher = null;

    private String uBoat = null;

    private String level = null;

    private String totalNumberOfAssignment = null;

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

    public BattleFieldInfo() {
    }

    public synchronized String getBattleFieldName() {
        return battleFieldName;
    }

    public synchronized String getLevel() {
        return level;
    }

    public synchronized Integer getNeededNumOfAllies() {
        return neededNumOfAllies;
    }

    public synchronized Integer getRegisteredAllies() {
        return registeredAllies;
    }

    public synchronized String getStatus() {
        return status;
    }

    public synchronized String getUBoat() {
        return uBoat;
    }

    public synchronized void setRegisteredAllies(Integer registeredAllies) {
        this.registeredAllies = registeredAllies;
    }

    public synchronized void setStatus(String status) {
        this.status = status;
    }

    public synchronized void setUBoat(String uBoat){
        this.uBoat = uBoat;
    }

    public synchronized void incrementAllies() {this.registeredAllies++;}

    public synchronized void setTotalNumberOfAssignment(String totalNumberOfAssignment) {
        this.totalNumberOfAssignment = totalNumberOfAssignment;
    }

    public synchronized String getTotalNumberOfAssignment() {
        return totalNumberOfAssignment;
    }

    public synchronized void setMessageToDecipher(String messageToDecipher) {
        this.messageToDecipher = messageToDecipher;
    }

    public synchronized String getMessageToDecipher() {
        return messageToDecipher;
    }
}
