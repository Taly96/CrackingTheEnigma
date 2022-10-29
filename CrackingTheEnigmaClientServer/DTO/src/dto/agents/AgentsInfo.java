package dto.agents;


public class AgentsInfo {

    private String name = null;

    private Integer numberOfThreads = null;

    private Integer assignmentSize = null;

    private String alliesTeam = null;

    private Integer assignmentsPerDraw = null;

    private long candidatesFound = 0;

    private Integer assignmentsCompleted = 0;

    private Integer assignmentsLeft = 0;

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

    public  String getName() {
        return name;
    }

    public  Integer getNumberOfThreads() {
        return numberOfThreads;
    }

    public  Integer getAssignmentSize() {
        return assignmentSize;
    }

    public  Long getCandidatesFound() {
        return candidatesFound;
    }

    public  Integer getAssignmentsCompleted() {
        return assignmentsCompleted;
    }

    public  Integer getAssignmentsLeft() {
        return assignmentsLeft;
    }

    public  String getAlliesTeam() {
        return alliesTeam;
    }

    public  Integer getAssignmentsPerDraw() {
        return assignmentsPerDraw;
    }

    public void clearContestInfo() {
        this.candidatesFound = 0;
        this.assignmentsCompleted = 0;
        this.assignmentsLeft = 0;
        this.assignmentSize = 0;
    }

    public void setDrawn(int drawnAssignments) {

        this.assignmentSize = drawnAssignments;
    }

    public void setLeft(int left) {
        this.assignmentsLeft = left;
    }

    public void setCompleted(int completed) {
        this.assignmentsCompleted = completed;
    }

    public void setTotalFound(long totalFound) {
        this.candidatesFound = totalFound;
    }
}
