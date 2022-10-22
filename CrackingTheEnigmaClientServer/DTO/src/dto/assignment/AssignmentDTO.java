package dto.assignment;

public class AssignmentDTO {

    private String startingPoint = null;

    private String finishPoint = null;

    private byte[] knownComponents = null;

    private Integer numOfRotors = 0;

    public AssignmentDTO(
            String startingPoint,
            String finishPoint,
            byte[] knownComponents,
            Integer numOfRotors
    ){
        this.startingPoint = startingPoint;
        this.finishPoint = finishPoint;
        this.knownComponents = knownComponents;
        this.numOfRotors = numOfRotors;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public String getFinishPoint() {
        return finishPoint;
    }

    public byte[] getKnownComponents() {
        return knownComponents;
    }

    public Integer getNumOfRotors() {
        return numOfRotors;
    }
}
