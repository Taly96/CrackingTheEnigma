package dto.activeteams;

import java.util.ArrayList;
import java.util.List;

public class ActiveTeamsDTO {
    private List<ActiveTeamInfo> activeTeams = null;

    public ActiveTeamsDTO(){
        this.activeTeams = new ArrayList<>();
    }

    public void addInfo(ActiveTeamInfo newInfo){
        this.activeTeams.add(newInfo);
    }

    public List<ActiveTeamInfo> getActiveTeams() {
        return this.activeTeams;
    }
}
