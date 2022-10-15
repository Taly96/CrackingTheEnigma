package dto.battlefield;

import java.util.ArrayList;
import java.util.List;

public class BattleFieldDTO {
    private List<BattleFieldInfo> battleFields = null;

    public BattleFieldDTO() {
        this.battleFields = new ArrayList<>();
    }

    public List<BattleFieldInfo> getBattleFields() {
        return battleFields;
    }

    public void addInfo(BattleFieldInfo battleFieldInfo){
        this.battleFields.add(battleFieldInfo);
    }
}
