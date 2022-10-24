package allies.view.contest;

import allies.view.contest.refreshers.CandidatesRefresher;
import allies.view.contest.refreshers.ContestantsRefresher;
import allies.view.contest.refreshers.CurrentBattleRefresher;
import allies.view.contest.refreshers.TeamProgressRefresher;
import allies.view.main.MainAlliesAppController;
import dto.activeteams.AlliesDTO;
import dto.activeteams.AlliesInfo;
import dto.agents.AgentsDTO;
import dto.agents.AgentsInfo;
import dto.battlefield.BattleFieldInfo;
import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesInfo;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Timer;

import static httpcommon.constants.Constants.REFRESH_RATE;
import static httpcommon.utils.Utils.showErrors;

public class AlliesContestController {

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
    private TextField textFieldAssignmentSize;

    @FXML
    private Button buttonReady;

    @FXML
    private TableView<AlliesInfo> tableViewContestants;

    @FXML
    private TableColumn<AlliesInfo, String> tableColumnName;

    @FXML
    private TableColumn<AlliesInfo, Integer> tableViewNumberOfAgents;

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
    private TableView<AgentsInfo> tableViewAgentsProgress;

    @FXML
    private TableColumn<AgentsInfo, String> tableColumnAgentName;

    @FXML
    private TableColumn<AgentsInfo, String> tableColumnTotalAssignments;

    @FXML
    private TableColumn<AgentsInfo, String> tableColumnAssignmentsLeft;

    @FXML
    private TableColumn<AgentsInfo, Integer> tableColumnCandidatesFound;

    @FXML
    private Label labelTotalAssignments;

    @FXML
    private Label labelCompletedAssignments;

    @FXML
    private Label labelCreatedAssignments;

    @FXML
    private Label labelCurrentlyWorkingOn;

    private StringProperty currentlyDecipheringProperty = null;

    private BigDecimal maxAssignmentSizeValue = null;

    private BigDecimal completedAssignments = null;

    private BigDecimal createdAssignments = null;

    private MainAlliesAppController alliesAppController = null;

    private String userName = null;

    private int numberOfAgents = 0;

    private CandidatesRefresher candidatesRefresher = null;

    private ContestantsRefresher contestantsRefresher = null;

    private CurrentBattleRefresher currentBattleRefresher = null;

    private TeamProgressRefresher teamProgressRefresher = null;

    private BooleanProperty isContestOngoing = null;

    private StringProperty totalAssignmentsProperty = null;

    private StringProperty createdAssignmentsProperty = null;

    private StringProperty completedAssignmentsProperty = null;

    private Timer timer = null;

    @FXML
    public void initialize(){
        this.completedAssignmentsProperty = new SimpleStringProperty("0");
        this.totalAssignmentsProperty = new SimpleStringProperty("0");
        this.createdAssignmentsProperty = new SimpleStringProperty("0");
        this.isContestOngoing = new SimpleBooleanProperty(false);
        this.currentlyDecipheringProperty = new SimpleStringProperty("None");
        this.maxAssignmentSizeValue = new BigDecimal(0);
        this.createdAssignments = new BigDecimal(0);
        this.completedAssignments = new BigDecimal(0);
        this.labelTotalAssignments.textProperty().bind(
                Bindings.concat(
                        "Total Assignments: ",
                        this.totalAssignmentsProperty
                )
        );
        this.labelCompletedAssignments.textProperty().bind(
                Bindings.concat("Completed Assignments: ",
                        this.completedAssignmentsProperty
                )
        );
        this.labelCreatedAssignments.textProperty().bind(
                Bindings.concat(
                        "Created Assignments: ",
                        this.createdAssignmentsProperty
                )
        );
        this.labelCurrentlyWorkingOn.textProperty().bind(
                Bindings.concat(
                        "Message To Decipher: ",
                        this.currentlyDecipheringProperty
                )
        );
        this.initializeContestTableView();
        this.initializeContestantsTableView();
        this.initializeCandidatesTableView();
        this.initializeTeamsProgressView();
    }

    private void initializeTeamsProgressView() {
        this.tableColumnAgentName.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        this.tableColumnTotalAssignments.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getAssignmentsCompleted()));
        this.tableColumnAssignmentsLeft.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getAssignmentsLeft()));
        this.tableColumnCandidatesFound.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getCandidatesFound()).asObject());
    }

    private void initializeCandidatesTableView() {
        this.tableColumnCandidates.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getCandidate()));
        this.tableColumnFoundBy.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getFoundBy()));
        this.tableColumnCodeConfig.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getCodeConfig()));

    }

    private void initializeContestantsTableView() {
        this.tableColumnName.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        this.tableColumnAssignmentSize.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getAssignmentSize()));
        this.tableViewNumberOfAgents.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getNumOfAgents()).asObject());
    }

    private void initializeContestTableView() {
        this.tableColumnLevel.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getLevel()));
        this.tableColumnBattleField.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getBattleFieldName()));
        this.tableColumnNeeded.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getNeededNumOfAllies()).asObject());
        this.tableColumnUBoat.setCellValueFactory
                (cellData -> new SimpleStringProperty(
                        cellData.getValue().getUBoat()));
        this.tableColumnRegistered.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getRegisteredAllies()).asObject());
        this.tableColumnStatus.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
    }

    @FXML
    void onReady(ActionEvent event) {
        try {
            if(this.textFieldAssignmentSize.getText().isEmpty()){
                throw new NumberFormatException();
            }
            else {
                BigInteger assignmentSize = new BigInteger(this.textFieldAssignmentSize.getText());
                if (assignmentSize.equals(BigInteger.ZERO)) {
                    throw new NumberFormatException();
                } else if (this.maxAssignmentSizeValue.compareTo(new BigDecimal(assignmentSize.toString())) > 0) {
                    throw new NumberFormatException();
                } else {
                    if(this.numberOfAgents == 0){
                        showErrors("Wait for agents to join your team.");
                    }
                    else {
                        String competition = this.tableColumnBattleField.getCellData(0);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("About to start competing");
                        alert.setContentText("You are about to enter the " +
                                competition + " contest" + System.lineSeparator() +
                                ", with " + this.numberOfAgents + " agents " +
                                "and " + assignmentSize + " assignment size per agent." +
                                System.lineSeparator() + " Correct?");
                        Optional<ButtonType> res = alert.showAndWait();
                        if (res.isPresent() && res.get().equals(ButtonType.OK)) {
                            this.alliesAppController.setReadyForContest(assignmentSize);
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong input");
            alert.setContentText(
                    "Please enter a valid natural number no higher than "
                            + this.maxAssignmentSizeValue
                            + System.lineSeparator() +
                            "for the size of the assignment."
            );
            alert.showAndWait();
        }
    }

    public void setMainAppController(MainAlliesAppController mainAlliesAppController) {
        this.alliesAppController = mainAlliesAppController;
    }

    public void updateSignedUpFor(BattleFieldInfo signedUpFor) {
        this.tableViewContest.getItems().clear();
        this.tableViewContest.getItems().add(signedUpFor);
        this.totalAssignmentsProperty.set(signedUpFor.getTotalNumberOfAssignment());
        this.currentlyDecipheringProperty.set(signedUpFor.getMessageToDecipher());
        this.startRefreshers();
    }

    private void startRefreshers() {
        this.candidatesRefresher = new CandidatesRefresher(
                this.userName,
                this::updateCandidates
        );
        this.contestantsRefresher = new ContestantsRefresher(
                this.userName,
                this::updateContestants
        );
        this.currentBattleRefresher = new CurrentBattleRefresher(
                this.userName,
                this::updateBattle
        );
        this.teamProgressRefresher = new TeamProgressRefresher(
                this.userName,
                this::updateTeamsProgress
        );

        this.timer = new Timer();
        this.timer.schedule(this.contestantsRefresher, REFRESH_RATE, REFRESH_RATE);
        this.timer.schedule(this.candidatesRefresher, REFRESH_RATE, REFRESH_RATE);
        this.timer.schedule(this.currentBattleRefresher, REFRESH_RATE, REFRESH_RATE);
        this.timer.schedule(this.teamProgressRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateTeamsProgress(AgentsDTO agentsDTO) {
        Platform.runLater(() -> {
            if(this.numberOfAgents != agentsDTO.getAgents().size()){
                this.numberOfAgents = agentsDTO.getAgents().size();
            }
            this.tableViewAgentsProgress.getItems().clear();

            for(AgentsInfo agentsInfo : agentsDTO.getAgents()){
                this.tableViewAgentsProgress.getItems().add(agentsInfo);
                this.completedAssignments = this.completedAssignments.add(new BigDecimal(agentsInfo.getAssignmentsCompleted()));
            }

            this.completedAssignmentsProperty.set(this.completedAssignments.toString());
        });
    }

    private void updateBattle(BattleFieldInfo battleFieldInfo) {
        Platform.runLater(() -> {
            if(battleFieldInfo.getStatus().equals("Active")){
                if(!this.isContestOngoing.get()){
                    this.isContestOngoing.set(true);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Contest has started");
                    alert.setContentText("All teams are ready, the contest has started.");
                    alert.showAndWait();
                }
            } else if (battleFieldInfo.getStatus().equals("Ended")) {
                this.stopRefreshers();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Contest has ended");
                alert.setContentText("The contest has ended. The winner is " + battleFieldInfo.getWinner());
                alert.showAndWait();
                //todo-back to log in?
            }
            this.tableViewContest.getItems().clear();
            this.tableViewContest.getItems().add(battleFieldInfo);
        });
    }

    private void updateContestants(AlliesDTO alliesDTO) {
        Platform.runLater(() -> {
            this.tableViewContestants.getItems().clear();

            for(AlliesInfo alliesInfo : alliesDTO.getAllies()){
                if(alliesInfo.getName().equals(this.userName)){
                    this.createdAssignmentsProperty.set(alliesInfo.getCreatedAssignments());
                }
                this.tableViewContestants.getItems().add(alliesInfo);
            }
        });
    }

    private void updateCandidates(CandidatesDTO candidatesDTO) {
        Platform.runLater(() -> {
            this.tableViewCandidates.getItems().clear();
            for(CandidatesInfo candidatesInfo : candidatesDTO.getCandidates()){
                this.tableViewCandidates.getItems().add(candidatesInfo);
            }
        });
    }

    public void updateUserName(String userName) {
        this.userName = userName;
    }

    public void stopRefreshers(){
        this.contestantsRefresher.cancel();
        this.teamProgressRefresher.cancel();
        this.currentBattleRefresher.cancel();
        this.candidatesRefresher.cancel();
        this.timer.cancel();
    }

    public void setReady() {
        this.buttonReady.setDisable(true);
    }
}
