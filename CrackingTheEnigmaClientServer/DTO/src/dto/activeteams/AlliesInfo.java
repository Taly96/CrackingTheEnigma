package dto.activeteams;

public class AlliesInfo {

    private String name = null;

    private String battleName = null;

    private Integer numOfAgents = 0;

    private String assignmentSize = "0";

    public AlliesInfo(
            String name,
            Integer numOfAgents,
            String assignmentSize,
            String battleName
    ){
        this.name = name;
        this.assignmentSize= assignmentSize;
        this.numOfAgents = numOfAgents;
        this.battleName = battleName;
    }

    public AlliesInfo(String userName) {
        this.name = userName;
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

    public void setName(String name) {
        this.name = name;
    }

    public void incrementNumOfAgents() {
        this.numOfAgents++;
    }

    public void setAssignmentSize(String assignmentSize) {
        this.assignmentSize = assignmentSize;
    }

    public String getBattleName() {
        return battleName;
    }

    public void setBattleName(String battleName) {
        this.battleName = battleName;
    }
}
