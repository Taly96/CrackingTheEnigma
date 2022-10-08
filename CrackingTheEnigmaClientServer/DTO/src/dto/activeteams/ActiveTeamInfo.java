package dto.activeteams;

import java.math.BigDecimal;

public class ActiveTeamInfo {

    private String name = null;

    private Integer numOfAgents = 0;

    private BigDecimal assignmentSize = null;

    public ActiveTeamInfo(
            String name,
            Integer numOfAgents,
            BigDecimal assignmentSize
    ){
        this.name = name;
        this.assignmentSize = assignmentSize;
        this.numOfAgents = numOfAgents;
    }

    public String getName() {
        return this.name;
    }

    public Integer getNumOfAgents() {
        return this.numOfAgents;
    }

    public BigDecimal getAssignmentSize() {
        return this.assignmentSize;
    }
}
