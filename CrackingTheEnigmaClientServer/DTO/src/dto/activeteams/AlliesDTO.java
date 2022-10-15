package dto.activeteams;

import java.util.ArrayList;
import java.util.List;

public class AlliesDTO {
    private List<AlliesInfo> allies = null;

    public AlliesDTO(){
        this.allies = new ArrayList<>();
    }

    public void addInfo(AlliesInfo newInfo){
        this.allies.add(newInfo);
    }

    public List<AlliesInfo> getAllies() {
        return this.allies;
    }
}
