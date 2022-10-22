package dto.activeteams;

public class AlliesInfo {

    private String name = null;

    private String battleName = "Hasn't Joined Yet.";

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

    public synchronized String getBattleName() {
        return battleName;
    }

    public synchronized void setBattleName(String battleName) {
        this.battleName = battleName;
    }
}
