package view.login;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AlliesData {
    private StringProperty nameProperty = null;

    private IntegerProperty registeredProperty = null;

    public AlliesData(String name, Integer registered){
        this.nameProperty = new SimpleStringProperty(name);
        this.registeredProperty = new SimpleIntegerProperty(registered);
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public IntegerProperty registeredProperty() {
        return registeredProperty;
    }
}
