package client.view.body.contest;

import dto.activeteams.AlliesInfo;
import dto.activeteams.AlliesDTO;
import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesInfo;
import dto.staticinfo.StaticMachineDTO;
import javafx.application.Platform;
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

import java.math.BigDecimal;
import java.util.Timer;

import static constants.Constants.REFRESH_RATE;


public class UBoatContestController {

    @FXML
    private TableView<AlliesInfo> tableViewActiveTeams;

    @FXML
    private TableColumn<AlliesInfo, String> tableColumnAllies;

    @FXML
    private TableColumn<AlliesInfo, Integer> tableColumnAgents;

    @FXML
    private TableColumn<AlliesInfo, BigDecimal> tableColumnAssignmentSize;

    @FXML
    private TableView<CandidatesInfo> tableViewCandidates;

    @FXML
    private TableColumn<CandidatesInfo, String> tableColumnCandidates;

    @FXML
    private TableColumn<CandidatesInfo, String> tableColumnFoundBy;

    @FXML
    private TableColumn<CandidatesInfo, String> tableColumnCodeConfig;

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

    private ActiveTeamsRefresher activeTeamsRefresher = null;

    private CandidatesRefresher candidatesRefresher = null;

    private Timer timer = null;

    @FXML
    public void initialize() {
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
//        this.activeTeamsRefresher = new ActiveTeamsRefresher(
//                this::updateActiveTeams,
//                this.uBoatCenterController.getBattleFieldName()
//        );
//        this.timer = new Timer();
//        this.timer.schedule(this.activeTeamsRefresher, REFRESH_RATE, REFRESH_RATE);

        this.uBoatCenterController.startContest();
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
        this.buttonReady.setDisable(false);
        this.textFieldProcessedMessage.setText(this.textFieldProcessedMessage.getText() + processedMessage);
    }

    private void startCandidatesRefresher(String battleFieldName) {
        this.candidatesRefresher = new CandidatesRefresher(
                this::updateCandidates,
                this.uBoatCenterController.getBattleFieldName()
        );
        this.timer.schedule(this.candidatesRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateCandidates(CandidatesDTO candidatesDTO) {
        Platform.runLater(() -> {
            this.tableViewCandidates.getItems().clear();
            for (CandidatesInfo info : candidatesDTO.getCandidates()){
                this.tableViewCandidates.getItems().add(info);
            }
        });
    }

    private void updateActiveTeams(AlliesDTO activeTeamsDTO) {
        Platform.runLater(() -> {
            this.tableViewActiveTeams.getItems().clear();
            for (AlliesInfo info : activeTeamsDTO.getAllies()){
                this.tableViewActiveTeams.getItems().add(info);
            }
        });
    }
}
