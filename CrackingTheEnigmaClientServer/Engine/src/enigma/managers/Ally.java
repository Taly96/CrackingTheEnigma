package enigma.managers;

import dto.activeteams.AlliesInfo;
import dto.candidates.CandidatesDTO;

public class Ally {

    private Integer assignmentSize = 0;

    private AlliesInfo info = null;

    private AgentsManager agents = null;

    public Ally(){
        this.agents = new AgentsManager();
    }

    public synchronized AlliesInfo getInfo() {

        return info;
    }
}
