package dto.decipher;

public class DecipherDTO {

    private byte[] knownComponents = null;

    private byte[] machineInventory = null;

    public DecipherDTO(
            byte[] knownComponents,
            byte[] machineInventory
    ) {
        this.knownComponents = knownComponents;
        this.machineInventory = machineInventory;
    }

    public byte[] getKnownComponents() {
        return knownComponents;
    }

    public byte[] getMachineInventory() {
        return machineInventory;
    }

    public void setKnownComponents(byte[] knownComponents) {
        this.knownComponents = knownComponents;
    }
}
