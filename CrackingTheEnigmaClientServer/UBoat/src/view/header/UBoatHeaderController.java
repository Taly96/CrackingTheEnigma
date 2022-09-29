package view.header;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import view.main.AppMainController;

public class UBoatHeaderController {

    @FXML
    private Button buttonLoad;

    @FXML
    private TextField textFieldXMLFile;


    private final AppMainController uBoatController = null;

    @FXML
    void onLoadXMLFile(ActionEvent event) {
        this.uBoatController.loadXMLFile();
    }

}
