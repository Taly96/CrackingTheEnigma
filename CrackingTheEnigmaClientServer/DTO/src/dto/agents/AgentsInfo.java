package dto.agents;


public class AgentsInfo {

    private String name = null;

    private Integer numberOfThreads = null;

    private String assignmentSize = null;

    private String alliesTeam = null;

    private Integer assignmentsPerDraw = null;

    private Integer candidatesFound = 0;

    private String assignmentsCompleted = "0";

    private String assignmentsLeft = "0";

    public AgentsInfo(
            String userName,
            int threads,
            int assignmentsPerDraw,
            String alliesTeam
    ) {
        this.alliesTeam = alliesTeam;
        this.assignmentsPerDraw = assignmentsPerDraw;
        this.numberOfThreads = threads;
        this.name = userName;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized Integer getNumberOfThreads() {
        return numberOfThreads;
    }

    public synchronized String getAssignmentSize() {
        return assignmentSize;
    }

    public synchronized Integer getCandidatesFound() {
        return candidatesFound;
    }

    public synchronized String getAssignmentsCompleted() {
        return assignmentsCompleted;
    }

    public synchronized String getAssignmentsLeft() {
        return assignmentsLeft;
    }

    public synchronized String getAlliesTeam() {
        return alliesTeam;
    }

    public synchronized Integer getAssignmentsPerDraw() {
        return assignmentsPerDraw;
    }
}
