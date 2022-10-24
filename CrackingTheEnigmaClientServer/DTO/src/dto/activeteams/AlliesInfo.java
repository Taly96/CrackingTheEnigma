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

    public synchronized String getName() {
        return name;
    }

    public synchronized Integer getNumOfAgents() {
        return numOfAgents;
    }

    public synchronized String getAssignmentSize() {
        return assignmentSize;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public synchronized void incrementNumOfAgents() {
        this.numOfAgents++;
    }

    public synchronized void setAssignmentSize(String assignmentSize) {
        this.assignmentSize = assignmentSize;
    }


    public synchronized String getCreatedAssignments() {
        return createdAssignments;
    }

    public synchronized void setCreatedAssignments(String createdAssignments) {
        this.createdAssignments = createdAssignments;
    }

    public synchronized String getBattleName() {
        return battleName;
    }

    public synchronized void setBattleName(String battleName) {
        this.battleName = battleName;
    }
}
