package client.view.header;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import client.view.main.AppMainController;

import java.io.IOException;

public class UBoatHeaderController {

    @FXML
    private Button buttonLoad;

    @FXML
    private TextField textFieldXMLFile;


    private AppMainController uBoatMainController = null;


    @FXML
    void onLoadXMLFile(ActionEvent event) throws IOException {
        this.uBoatMainController.loadXMLFile();
    }

    public void bind(
            StringProperty filePathProperty,
            BooleanProperty isFileLoadedProperty
    ){
        this.textFieldXMLFile.textProperty().bind(filePathProperty);
        this.buttonLoad.disableProperty().bind(isFileLoadedProperty);
    }

    public void setMainAppController(AppMainController appMainController) {
        this.uBoatMainController = appMainController;
    }
}
