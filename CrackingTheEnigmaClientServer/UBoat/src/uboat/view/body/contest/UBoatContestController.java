package uboat.view.body.contest;

import dto.activeteams.AlliesInfo;
import dto.activeteams.AlliesDTO;
import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesDTOList;
import dto.candidates.CandidatesInfo;
import dto.staticinfo.StaticMachineDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uboat.view.body.UBoatCenterController;
import uboat.view.body.contest.refreshers.ActiveTeamsRefresher;
import uboat.view.body.contest.refreshers.CandidatesRefresher;

import java.util.Optional;
import java.util.Timer;

import static httpcommon.constants.Constants.REFRESH_RATE;


public class UBoatContestController {

    @FXML
    private TableView<AlliesInfo> tableViewActiveTeams;

    @FXML
    private TableColumn<AlliesInfo, String> tableColumnAllies;

    @FXML
    private TableColumn<AlliesInfo, Integer> tableColumnAgents;

    @FXML
    private TableColumn<AlliesInfo, String> tableColumnAssignmentSize;

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
    private VBox vBoxProcess;

    @FXML
    private HBox hBoxButtons;

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
        this.buttonReady.setDisable(true);
        this.listViewDictionary.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.initializeActiveTeamsTableView();
        this.initializeCandidatesTableView();
    }

    private void initializeCandidatesTableView() {
        this.tableColumnCandidates.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getCandidate())
        );
        this.tableColumnFoundBy.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getFoundBy())
        );
        this.tableColumnCodeConfig.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getCodeConfig())
        );
    }

    private void initializeActiveTeamsTableView() {
        this.tableColumnAllies.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        this.tableColumnAgents.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getNumOfAgents()).asObject()
        );
        this.tableColumnAssignmentSize.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getAssignmentSize())
        );
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
        this.buttonReady.setDisable(false);
    }

    @FXML
    void onStartContest(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("About to assemble a contest");
        alert.setContentText("You are about to assemble a contest, using " +
                this.textFieldProcessedMessage.getText() + " for deciphering." + System.lineSeparator() +
                "Correct?");
        Optional<ButtonType> res = alert.showAndWait();
        if(res.isPresent() && res.get().equals(ButtonType.OK)) {
            this.uBoatCenterController.startContest(this.textFieldProcessedMessage.getText());
            this.starRefreshers();
        }
    }

    public void contestStarted(){
        this.vBoxProcess.setDisable(true);
        this.hBoxButtons.setDisable(true);
    }

    private void starRefreshers() {
        this.candidatesRefresher = new CandidatesRefresher(
                this::updateCandidates,
                this.uBoatCenterController.getBattleFieldName()
        );
        this.activeTeamsRefresher = new ActiveTeamsRefresher(
                this::updateActiveTeams,
                this.uBoatCenterController.getBattleFieldName()
        );
        this.timer = new Timer();
        this.timer.schedule(this.activeTeamsRefresher, REFRESH_RATE, REFRESH_RATE);
        this.timer.schedule(this.candidatesRefresher, REFRESH_RATE,REFRESH_RATE);
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

    private void updateCandidates(CandidatesDTOList candidatesDTOList) {
        Platform.runLater(() -> {
            this.tableViewCandidates.getItems().clear();
            for (CandidatesDTO DTO : candidatesDTOList.getCandidatesDTOList() ){
                for(CandidatesInfo info : DTO.getCandidates()){
                    this.tableViewCandidates.getItems().add(info);
                }
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
