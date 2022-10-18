package agent.view.contest;

import agent.view.main.MainAgentAppController;
import dto.agents.AgentsInfo;
import dto.battlefield.BattleFieldInfo;
import dto.candidates.CandidatesInfo;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AgentContestController {

    @FXML
    private TableView<String> tableViewAlly;

    @FXML
    private TableColumn<String, String> tableColumnAllyName;

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

    private StringProperty totalAssignmentsDrawnProperty = null;

    private StringProperty totalCompletedAssignmentsProperty = null;

    private StringProperty totalCandidatesFoundProperty = null;

    private StringProperty awaitingAssignmentProperty = null;

    private  int totalAssignmentsDrawn = 0;

    private int totalCompletedAssignments = 0;

    private long totalCandidates = 0;

    private int awaitingAssignments = 0;

    @FXML
    public void initialize(){
        this.awaitingAssignmentProperty = new SimpleStringProperty();
        this.totalAssignmentsDrawnProperty = new SimpleStringProperty();
        this.totalCandidatesFoundProperty = new SimpleStringProperty();
        this.totalCompletedAssignmentsProperty = new SimpleStringProperty();
        this.labelAwaitingAssignments.textProperty().bind(
                Bindings.concat(
                        "Currently awaiting assignments: ",
                        this.awaitingAssignments
                )
        );
        this.labelTotalAssignments.textProperty().bind(
                Bindings.concat(
                        "Total drawn assignments: ",
                        this.totalAssignmentsDrawn
                )
        );
        this.labelTotalCandidates.textProperty().bind(
                Bindings.concat(
                        "Total candidates found: ",
                        this.totalCandidates
                )
        );
        this.labelTotalCompletedAssignments.textProperty().bind(
                Bindings.concat(
                        "Total completed assignments: ",
                        this.totalCompletedAssignments
                )
        );
    }

    public void setMainAppController(MainAgentAppController mainAgentAppController) {
        this.mainAgentAppController = mainAgentAppController;
    }

    public void loggedIn(AgentsInfo newAgent, BattleFieldInfo registeredTo) {
        this.tableViewAlly.getItems().add(newAgent.getName());
        this.tableViewContest.getItems().add(registeredTo);

    }

    private void decipher(){

    }

    //todo-refreshBattleStatus
}
