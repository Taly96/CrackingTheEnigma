package client.view.body.machine;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MachineData {

    private StringProperty abcProperty = null;

    private IntegerProperty rotorsCountProperty = null;

    private IntegerProperty availableRotorsProperty = null;

    private IntegerProperty availableReflectorsProperty = null;


    public MachineData(
            String abc,
            Integer rotorsCount,
            Integer availableRotors,
            Integer availableReflectors
    ){
        this.abcProperty = new SimpleStringProperty(abc);
        this.availableReflectorsProperty = new SimpleIntegerProperty(availableReflectors);
        this.availableRotorsProperty = new SimpleIntegerProperty(availableRotors);
        this.rotorsCountProperty = new SimpleIntegerProperty(rotorsCount);
    }

    public String getAbc() {
        return this.abcProperty.get();
    }

    public StringProperty abcProperty() {
        return this.abcProperty;
    }

    public void setAbcProperty(String abc) {
        this.abcProperty.set(abc);
    }

    public int getRotorsCount() {
        return this.rotorsCountProperty.get();
    }

    public IntegerProperty rotorsCountProperty() {
        return this.rotorsCountProperty;
    }

    public void setRotorsCount(int rotorsCount) {
        this.rotorsCountProperty.set(rotorsCount);
    }

    public int getAvailableRotors() {
        return this.availableRotorsProperty.get();
    }

    public IntegerProperty availableRotorsProperty() {
        return this.availableRotorsProperty;
    }

    public void setAvailableRotorsProperty(int availableRotors) {
        this.availableRotorsProperty.set(availableRotors);
    }

    public int getAvailableReflectors() {
        return this.availableReflectorsProperty.get();
    }

    public IntegerProperty availableReflectorsProperty() {
        return this.availableReflectorsProperty;
    }

    public void setAvailableReflectors(int availableReflectorsProperty) {
        this.availableReflectorsProperty.set(availableReflectorsProperty);
    }
}
