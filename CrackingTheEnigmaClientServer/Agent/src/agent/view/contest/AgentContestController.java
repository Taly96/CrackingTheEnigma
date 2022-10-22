package agent.view.contest;

import agent.view.contest.refreshers.ContestRefresher;
import agent.view.main.MainAgentAppController;
import dto.agents.AgentsInfo;
import dto.battlefield.BattleFieldInfo;
import dto.candidates.CandidatesInfo;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Timer;

import static httpcommon.constants.Constants.REFRESH_RATE;

public class AgentContestController {

    @FXML
    private Label labelAllyTeam;
    @FXML
    private TableView<BattleFieldInfo> tableViewContest;

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
    private Label labelTotalAssignments;

    @FXML
    private Label labelTotalCompletedAssignments;

    @FXML
    private Label labelTotalCandidates;

    @FXML
    private Label labelAwaitingAssignments;

    @FXML
    private TableView<CandidatesInfo> tableViewCandidates;

    @FXML
    private TableColumn<CandidatesInfo, String> tableColumnCandidatesString;

    @FXML
    private TableColumn<CandidatesInfo, String> tableColumnCandidateConfig;

    private MainAgentAppController mainAgentAppController = null;

    private ContestRefresher contestRefresher = null;

    private Timer timer = null;

    private IntegerProperty totalAssignmentsDrawnProperty = null;

    private IntegerProperty totalCompletedAssignmentsProperty = null;

    private LongProperty totalCandidatesProperty = null;

    private IntegerProperty awaitingAssignmentsProperty = null;

    private StringProperty allyNameProperty = null;

    private boolean isDeciphering = false;

    @FXML
    public void initialize(){
        this.allyNameProperty = new SimpleStringProperty("Hasn't Joined Yet.");
        this.awaitingAssignmentsProperty = new SimpleIntegerProperty(0);
        this.totalCandidatesProperty = new SimpleLongProperty(0);
        this.totalAssignmentsDrawnProperty = new SimpleIntegerProperty(0);
        this.totalCompletedAssignmentsProperty = new SimpleIntegerProperty(0);
        this.labelAwaitingAssignments.textProperty().bind(
                Bindings.concat(
                        "Currently Awaiting Assignments: ",
                        this.awaitingAssignmentsProperty
                )
        );
        this.labelTotalAssignments.textProperty().bind(
                Bindings.concat(
                        "Total Assignments: ",
                        this.totalAssignmentsDrawnProperty
                )
        );
        this.labelTotalCandidates.textProperty().bind(
                Bindings.concat(
                        "Total Candidates Found: ",
                        this.totalCandidatesProperty
                )
        );
        this.labelTotalCompletedAssignments.textProperty().bind(
                Bindings.concat(
                        "Total Completed Assignments: ",
                        this.totalCompletedAssignmentsProperty
                )
        );
        this.labelAllyTeam.textProperty().bind(
                Bindings.concat(
                        "Ally Team: ",
                        this.allyNameProperty
                )
        );
        this.initializeCandidatesTableView();
        this.initializeContestTableView();
    }

    private void initializeContestTableView() {
        this.tableColumnBattleField.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBattleFieldName()));
        this.tableColumnUBoat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUBoat()));
        this.tableColumnNeeded.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNeededNumOfAllies()).asObject());
        this.tableColumnRegistered.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRegisteredAllies()).asObject());
        this.tableColumnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        this.tableColumnLevel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLevel()));
    }
    private void initializeCandidatesTableView() {
        this.tableColumnCandidatesString.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getCandidate())
        );
        this.tableColumnCandidateConfig.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getCodeConfig())
        );
    }

    public void setMainAppController(MainAgentAppController mainAgentAppController) {
        this.mainAgentAppController = mainAgentAppController;
    }

    public void loggedIn(AgentsInfo newAgent) {
        this.allyNameProperty.set(newAgent.getAlliesTeam());
        this.startBattleRefresher();
    }

    private void startBattleRefresher(){
        this.contestRefresher = new ContestRefresher(
                this::updateContest
        );
        this.timer = new Timer();
        this.timer.schedule(this.contestRefresher, REFRESH_RATE, REFRESH_RATE);

    }

    private void updateContest(BattleFieldInfo battleFieldInfo) {
        if(battleFieldInfo.getBattleFieldName() != null){
            if(battleFieldInfo.getStatus().equals("Active")){
                if(!this.isDeciphering){
                    this.isDeciphering = true;
                    this.decipher();
                }
            } else if (battleFieldInfo.getStatus().equals("Ended")) {
                this.isDeciphering = false;
                this.stopRefreshers();
            }
            this.tableViewContest.getItems().clear();
            this.tableViewContest.getItems().add(battleFieldInfo);
        }
    }

    private void stopRefreshers() {
        this.contestRefresher.cancel();
        this.timer.cancel();
    }

    private void decipher(){
        System.out.println("String Deciphering");
        //todo - start deciphering task
    }
}
