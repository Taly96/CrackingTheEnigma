package dto.assignment;

public class AssignmentDTO {

    private String startingPoint = null;

    private String finishPoint = null;

    private byte[] knownComponents = null;

    public AssignmentDTO(
            String startingPoint,
            String finishPoint,
            byte[] knownComponents
    ){
        this.startingPoint = startingPoint;
        this.finishPoint = finishPoint;
        this.knownComponents = knownComponents;
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
}
