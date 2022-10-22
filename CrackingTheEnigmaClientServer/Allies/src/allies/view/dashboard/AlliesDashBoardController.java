package allies.view.dashboard;

import allies.view.dashboard.refreshers.AgentsRefresher;
import allies.view.dashboard.refreshers.BattleFieldsRefresher;
import allies.view.main.MainAlliesAppController;
import dto.agents.AgentsDTO;
import dto.agents.AgentsInfo;
import dto.battlefield.BattleFieldDTO;
import dto.battlefield.BattleFieldInfo;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;
import java.util.Timer;

import static httpcommon.constants.Constants.REFRESH_RATE;

public class AlliesDashBoardController {

    @FXML
    private TableView<AgentsInfo> tableViewAgents;

    @FXML
    private TableColumn<AgentsInfo, String> tableColumnName;

    @FXML
    private TableColumn<AgentsInfo, Integer> tableColumnNumberOfThreads;

    @FXML
    private TableColumn<AgentsInfo, Integer> tableColumnAssignment;

    @FXML
    private TableView<BattleFieldInfo> tableViewContests;

    @FXML
    private TableColumn<BattleFieldInfo, String> tableColumnBattleField;

    @FXML
    private TableColumn<BattleFieldInfo, String> tableColumnUBoat;

    @FXML
    private TableColumn<BattleFieldInfo, Integer> tableColumnNeeded;

    @FXML
    private TableColumn<BattleFieldInfo, Integer> tableColumnRegistered;

    @FXML
    private TableColumn<BattleFieldInfo, String> tableColumnStatus;

    @FXML
    private TableColumn<BattleFieldInfo, String> tableColumnLevel;

    @FXML
    private Button buttonReady;

    @FXML
    private TextField textFieldChosenBattleField;

    private MainAlliesAppController alliesAppController = null;

    private Timer timer = null;

    private BattleFieldsRefresher battleFieldsRefresher = null;

    private AgentsRefresher agentsRefresher = null;

    private StringProperty chosenBattleFieldProperty = null;

    @FXML
    public void initialize(){
        this.chosenBattleFieldProperty = new SimpleStringProperty("Click on a battle to join.");
        this.textFieldChosenBattleField.textProperty().bind(this.chosenBattleFieldProperty);
        this.initializeActiveTeamsTableView();
        this.initializeContestTableView();
    }

    private void initializeContestTableView() {
        this.tableViewContests.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.tableColumnBattleField.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBattleFieldName()));
        this.tableColumnUBoat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUBoat()));
        this.tableColumnNeeded.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNeededNumOfAllies()).asObject());
        this.tableColumnRegistered.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRegisteredAllies()).asObject());
        this.tableColumnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        this.tableColumnLevel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLevel()));
    }

    private void initializeActiveTeamsTableView() {
        this.tableColumnName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        this.tableColumnNumberOfThreads.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumberOfThreads()).asObject());
        this.tableColumnAssignment.setCellValueFactory(cellData-> new SimpleIntegerProperty(cellData.getValue().getAssignmentsPerDraw()).asObject());
    }

    @FXML
    void onSignUpContest(ActionEvent event) {
        if(this.chosenBattleFieldProperty.get().equals("Click on a battle to join.")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setContentText("Chose a battle before pressing 'Join'.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm BattleField");
            alert.setContentText(
                    "You are about to register to the "
                            + this.chosenBattleFieldProperty.get()
                            + " contest."
                            + System.lineSeparator()
                            + "Correct?"
            );
            Optional<ButtonType> res = alert.showAndWait();
            if (res.isPresent() && res.get().equals(ButtonType.OK)) {
                this.alliesAppController.signUpContest(this.chosenBattleFieldProperty.get());
            }
        }
    }

    public void setMainAppController(MainAlliesAppController mainAlliesAppController) {
        this.alliesAppController = mainAlliesAppController;
    }

    public void startBattleRefresher(){
        this.battleFieldsRefresher = new BattleFieldsRefresher(
                this::updateContests,
                this.alliesAppController.getUserName()
        );
        this.agentsRefresher = new AgentsRefresher(
                this::updateAgents,
                this.alliesAppController.getUserName()
        );
        this.timer = new Timer();
        this.timer.schedule(this.agentsRefresher, REFRESH_RATE, REFRESH_RATE);
        this.timer.schedule(this.battleFieldsRefresher, REFRESH_RATE,REFRESH_RATE);
    }

    private void updateAgents(AgentsDTO agentsDTO) {
        Platform.runLater(() -> {
            this.tableViewAgents.getItems().clear();
            for(AgentsInfo info : agentsDTO.getAgents()){
                this.tableViewAgents.getItems().add(info);
            }
        });
    }

    private void updateContests(BattleFieldDTO battleFieldDTO) {
        Platform.runLater(() -> {
            BattleFieldInfo selectedCell =
                    this.tableViewContests.getSelectionModel().getSelectedItem();
            if(selectedCell != null){
                if(selectedCell.getStatus().equals("Waiting")) {
                    this.chosenBattleFieldProperty.set(selectedCell.getBattleFieldName());
                }
                else {
                    this.tableViewContests.getSelectionModel().clearSelection();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Warning");
                    alert.setContentText("Can't enter Full/Inactive/Ended contest");
                    alert.showAndWait();
                }
            }
            this.tableViewContests.getItems().clear();
            for(BattleFieldInfo info : battleFieldDTO.getBattleFields()){
                this.tableViewContests.getItems().add(info);
            }
        });
    }

    public void stopRefreshers() {
        this.agentsRefresher.cancel();
        this.battleFieldsRefresher.cancel();
        this.timer.cancel();
    }
}
