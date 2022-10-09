package client.view.contest;

import client.view.main.MainAlliesAppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AlliesContestController {

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
    private TableColumn<?, ?> tableColumnAssignmentSize;

    @FXML
    private Button buttonReady;

    @FXML
    private TableView<?> tableViewContestants;

    @FXML
    private TableColumn<?, ?> tableColumnName;

    @FXML
    private TableColumn<?, ?> tableViewNumberOfAgents;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label labelProgress;

    @FXML
    private Label labelTotalAssignments;

    @FXML
    private Label labelCompletedAssignments;

    @FXML
    private TableView<?> tableViewCandidates;

    @FXML
    private TableColumn<?, ?> tableColumnCandidates;

    @FXML
    private TableColumn<?, ?> tableColumnFoundBy;

    @FXML
    private TableColumn<?, ?> tableColumnCodeConfig;

    @FXML
    private TableView<?> tableViewAgentsProgress;

    @FXML
    private TableColumn<?, ?> tableColumnTotalAssignments;

    @FXML
    private TableColumn<?, ?> tableColumnAssignmentsLeft;

    private MainAlliesAppController alliesAppController = null;

    @FXML
    void onReady(ActionEvent event) {

    }

    public void setMainAppController(MainAlliesAppController mainAlliesAppController) {
        this.alliesAppController = mainAlliesAppController;
    }
}
