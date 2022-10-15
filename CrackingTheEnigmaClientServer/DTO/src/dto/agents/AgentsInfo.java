package dto.agents;


public class AgentsInfo {

    private String name = null;

    private Integer numberOfThreads = null;

    private String assignmentSize = null;

    private Integer candidatesFound = null;

    private Integer assignmentsCompleted = null;

    private Integer assignmentsLeft = null;

    public AgentsInfo(
            String name,
            Integer threads,
            String assignmentSize,
            Integer candidatesFound,
            Integer assignmentsCompleted,
            Integer assignmentsLeft
    ){
        this.name = name;
        this.numberOfThreads = threads;
        this.assignmentSize = assignmentSize;
        this.candidatesFound = candidatesFound;
        this.assignmentsCompleted = assignmentsCompleted;
        this.assignmentsLeft = assignmentsLeft;
    }

    public String getName() {
        return name;
    }

    public Integer getNumberOfThreads() {
        return numberOfThreads;
    }

    public String getAssignmentSize() {
        return assignmentSize;
    }

    public Integer getCandidatesFound() {
        return candidatesFound;
    }

    public Integer getAssignmentsCompleted() {
        return assignmentsCompleted;
    }

    public Integer getAssignmentsLeft() {
        return assignmentsLeft;
    }
}