package dto.battlefield;

public class BattleFieldDTO {

    private String battleFieldName = null;

    private String level = null;

    private int maxNumOfAllies = 0;

    public BattleFieldDTO(
            String battleFieldName,
            String level,
            int maxNumOfAllies
    ){
        this.battleFieldName = battleFieldName;
        this.level = level;
        this.maxNumOfAllies = maxNumOfAllies;
    }

    public String getBattleFieldName() {
        return this.battleFieldName;
    }

    public String getLevel() {
        return this.level;
    }

    public int getMaxNumOfAllies() {
        return this.maxNumOfAllies;
    }
}
