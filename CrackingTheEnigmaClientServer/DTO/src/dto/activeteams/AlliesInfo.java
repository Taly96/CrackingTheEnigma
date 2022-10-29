package dto.activeteams;

public class AlliesInfo {

    private String name = null;

    private String battleName = "Hasn't Joined Yet.";

    private Integer numOfAgents = 0;

    private String assignmentSize = "0";

    private String createdAssignments = "0";

    public AlliesInfo(String userName) {
        this.name = userName;
    }

    public  String getName() {
        return name;
    }

    public  Integer getNumOfAgents() {
        return numOfAgents;
    }

    public  String getAssignmentSize() {
        return assignmentSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  void incrementNumOfAgents() {
        this.numOfAgents++;
    }

    public void setAssignmentSize(String assignmentSize) {
        this.assignmentSize = assignmentSize;
    }

    public  String getCreatedAssignments() {
        return createdAssignments;
    }

    public void setBattleName(String battleName) {
        this.battleName = battleName;
    }

    public void removeContestInfo() {
        this.battleName = "Hasn't Joined Yet.";
        this.assignmentSize = "0";
        this.createdAssignments = "0";
    }
}
