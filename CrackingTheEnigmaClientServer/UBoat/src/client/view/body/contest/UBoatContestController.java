package client.view.body.contest;

import dto.staticinfo.StaticMachineDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import client.view.body.UBoatCenterController;

public class UBoatContestController {

    @FXML
    private TableView<?> tableViewActiveTeams;

    @FXML
    private TableColumn<?, ?> tableColumnAllies;

    @FXML
    private TableColumn<?, ?> tableColumnAgents;

    @FXML
    private TableColumn<?, ?> tableColumnAssignmentSize;

    @FXML
    private TableView<?> tableViewCandidates;

    @FXML
    private TableColumn<?, ?> tableColumnCandidates;

    @FXML
    private TableColumn<?, ?> tableColumnFoundBy;

    @FXML
    private TableColumn<?, ?> tableColumnCodeConfig;

    @FXML
    private TextField textFieldMessageToProcess;

    @FXML
    private ListView<String> listViewDictionary;

    @FXML
    private TextField textFieldProcessedMessage;

    @FXML
    private Button buttonProcessMessage;

    @FXML
    private Button buttonClearAll;

    @FXML
    private Button buttonReady;

    private UBoatCenterController uBoatCenterController = null;

    private StringProperty messageToProcessProperty = null;

    @FXML
    public void initialize(){
        this.messageToProcessProperty = new SimpleStringProperty("");
        this.textFieldMessageToProcess.textProperty().bind(this.messageToProcessProperty);

    }

    @FXML
    void onClearAll(ActionEvent event) {
        this.messageToProcessProperty.set("");
        this.textFieldProcessedMessage.setText("");
        this.buttonProcessMessage.setDisable(true);
        this.buttonReady.setDisable(true);
    }

    @FXML
    void onProcessMessage(ActionEvent event) {
        this.uBoatCenterController.processMessage(this.messageToProcessProperty.get().trim());
    }

    @FXML
    void onStartContest(ActionEvent event) {

    }

    public void setCenterController(UBoatCenterController uBoatCenterController) {
        this.uBoatCenterController = uBoatCenterController;
    }

    public void fileLoaded(StaticMachineDTO staticMachineDTO){
        ObservableList<String> dictionary = FXCollections.observableArrayList();
        dictionary.setAll(staticMachineDTO.getWords());
        this.listViewDictionary.getItems().clear();
        this.listViewDictionary.setItems(dictionary);
        this.listViewDictionary.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.buttonProcessMessage.setDisable(false);
            this.messageToProcessProperty.set(this.messageToProcessProperty.get() + newValue + " ");
        });
    }

    public void messageProcessed(String processedMessage) {
        this.textFieldProcessedMessage.setText(this.textFieldProcessedMessage.getText() + processedMessage);
    }
}
