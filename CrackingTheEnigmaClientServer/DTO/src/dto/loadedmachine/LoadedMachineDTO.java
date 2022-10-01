package dto.loadedmachine;

public class LoadedMachineDTO {

    private String abc = null;

    private int numOfNeededRotors = 0;

    private int numOfAvailableRotors = 0;

    private int numOfAvailableReflectors = 0;

    public LoadedMachineDTO(
            String abc,
            int numOfNeededRotors,
            int numOfAvailableRotors,
            int numOfAvailableReflectors
    ){
        this.abc = abc;
        this.numOfAvailableReflectors =  numOfAvailableReflectors;
        this.numOfAvailableRotors = numOfAvailableRotors;
        this.numOfNeededRotors = numOfNeededRotors;
    }

    public String getAbc() {
        return this.abc;
    }

    public int getNumOfNeededRotors() {
        return this.numOfNeededRotors;
    }

    public int getNumOfAvailableRotors() {
        return this.numOfAvailableRotors;
    }

    public int getNumOfAvailableReflectors() {
        return this.numOfAvailableReflectors;
    }
}
