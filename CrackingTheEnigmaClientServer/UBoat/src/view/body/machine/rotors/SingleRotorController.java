package view.body.machine.rotors;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import view.body.machine.UBoatMachineController;

import static view.resources.Resources.ROTOR_STYLE;

public class SingleRotorController {

    @FXML
    private Label labelRotorID;

    @FXML
    private VBox vBoxRotor;

    private IntegerProperty rotorIDProperty = null;

    private UBoatMachineController machineController = null;

    @FXML
    void onChosenRotor(MouseEvent event) {
        this.vBoxRotor.setDisable(true);
        this.machineController.rotorChosen(this.rotorIDProperty.get());
    }

    public void initialize(int id){
        this.rotorIDProperty = new SimpleIntegerProperty(id);
        this.labelRotorID.textProperty().bind(Bindings.concat("ID: ", this.rotorIDProperty));
        this.vBoxRotor.getStylesheets().add(ROTOR_STYLE);
    }



    public void setMachineController(UBoatMachineController machineController) {
        this.machineController = machineController;
    }


    public void setDisable(boolean isDisabled) {
        this.vBoxRotor.setDisable(isDisabled);
    }
}
