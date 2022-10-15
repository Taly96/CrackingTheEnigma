package dto.activeteams;

public class AlliesInfo {

    private String name = null;

    private Integer numOfAgents = null;

    private String assignmentSize = null;

    public AlliesInfo(
            String name,
            Integer numOfAgents,
            String assignmentSize
    ){
        this.name = name;
        this.assignmentSize= assignmentSize;
        this.numOfAgents = numOfAgents;
    }

    public String getName() {
        return name;
    }

    public Integer getNumOfAgents() {
        return numOfAgents;
    }

    public String getAssignmentSize() {
        return assignmentSize;
    }
}
