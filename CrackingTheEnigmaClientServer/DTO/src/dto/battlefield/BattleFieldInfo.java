package dto.battlefield;

import java.io.Serializable;

public class BattleFieldInfo implements Serializable {

    private String battleFieldName = null;

    private String messageToDecipher = null;

    private String uBoat = null;

    private String level = null;

    private String winner = null;

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

    public BattleFieldInfo() {}

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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUBoat(String uBoat){
        this.uBoat = uBoat;
    }

    public boolean incrementAllies() {
        if (this.status.equals("Full")) {
            return false;
        } else {
            this.registeredAllies++;
            if (this.registeredAllies == this.neededNumOfAllies) {
                this.setStatus("Full");
            }

            return true;
        }
    }

    public void setTotalNumberOfAssignment(String totalNumberOfAssignment) {
        this.totalNumberOfAssignment = totalNumberOfAssignment;
    }

    public String getTotalNumberOfAssignment() {
        return totalNumberOfAssignment;
    }

    public void setMessageToDecipher(String messageToDecipher) {
        this.messageToDecipher = messageToDecipher;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getMessageToDecipher() {
        return messageToDecipher;
    }

    public void decrementAllies() {
        this.registeredAllies--;
    }
}
