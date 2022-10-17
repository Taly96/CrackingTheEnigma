package view.contest;

import dto.agents.AgentsInfo;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.VBox;
import view.login.LoginController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import view.main.MainAgentAppController;

public class AgentContestController {

    @FXML
    private TableView<?> tableViewAlly;

    @FXML
    private TableColumn<?, ?> tableColumnAllyName;

    @FXML
    private TableView<?> tableViewContest;

    @FXML
    private TableColumn<?, ?> tableColumnBattleField;

    @FXML
    private TableColumn<?, ?> tableColumnUBoat;

    @FXML
    private TableColumn<?, ?> tableColumnNeeded;

    @FXML
    private TableColumn<?, ?> tableColumnRegistered;

    @FXML
    private TableColumn<?, ?> tableColumnStatus;

    @FXML
    private TableColumn<?, ?> tableColumnLevel;

    @FXML
    private Label labelTotalAssignments;

    @FXML
    private Label labelTotalCompletedAssignments;

    @FXML
    private Label labelTotalCandidates;

    @FXML
    private Label labelAwaitingAssignments;

    @FXML
    private TableView<?> tableViewCandidates;

    @FXML
    private TableColumn<?, ?> tableColumnCandidatesString;

    @FXML
    private TableColumn<?, ?> tableColumnCandidateConfig;

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
}
