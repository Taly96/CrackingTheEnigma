package view.header;

import dto.loadedmachine.LoadedMachineDTO;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import view.main.AppMainController;

import java.io.IOException;

public class UBoatHeaderController {

    @FXML
    private Button buttonLoad;

    @FXML
    private TextField textFieldXMLFile;


    private AppMainController uBoatController = null;


    @FXML
    void onLoadXMLFile(ActionEvent event) throws IOException {
        this.uBoatController.loadXMLFile();
    }

    public void setMainController(AppMainController mainController){
        this.uBoatController = mainController;
    }

    public void bind(StringProperty filePathProperty){
        this.textFieldXMLFile.textProperty().bind(filePathProperty);
    }

}
