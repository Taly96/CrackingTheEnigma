package agent.view.contest;

import agent.decipher.DecipherTask;
import agent.view.contest.refreshers.AgentsInfoUpdater;
import agent.view.contest.refreshers.ContestRefresher;
import agent.view.main.MainAgentAppController;
import dto.agents.AgentsInfo;
import dto.battlefield.BattleFieldInfo;
import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesInfo;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import okhttp3.*;
import org.controlsfx.control.NotificationPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Timer;

import static agent.http.Configuration.MACHINE_INVENTORY;
import static httpcommon.constants.Constants.*;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;

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

    private DecipherTask decipherTask;

    private Thread decipherThread = null;

    private AgentsInfo me = null;

    private String messageToProcess = null;

    private AgentsInfoUpdater agentsInfoUpdater = null;

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
        this.me = newAgent;
        this.startBattleRefresher();
    }

    private void startBattleRefresher(){
        this.tableViewContest.getItems().clear();
        this.contestRefresher = new ContestRefresher(
                this::updateContest
        );
        this.timer = new Timer();
        this.timer.schedule(this.contestRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateContest(BattleFieldInfo battleFieldInfo) {
        if(battleFieldInfo.getBattleFieldName() != null){
            Platform.runLater(() -> {
                if(battleFieldInfo.getStatus().equals("Active")) {
                    if(!this.isDeciphering){
                        this.isDeciphering = true;
                        if(this.messageToProcess == null){
                            this.messageToProcess = battleFieldInfo.getMessageToDecipher();
                        }
                        Alert alert = new Alert(
                                Alert.AlertType.INFORMATION,
                                "The contest has started."
                        );
                        alert.setTitle("The contest has started.");
                        this.requestInventory();
                        alert.show();
                    }
                    this.tableViewContest.getItems().clear();
                    this.tableViewContest.getItems().add(battleFieldInfo);
                } else if (battleFieldInfo.getStatus().equals("Ended")) {
                    if(isDeciphering){
                        this.decipherTask.cancel();
                        this.decipherThread.interrupt();
                        this.isDeciphering = false;
                        Alert alert = new Alert(
                                Alert.AlertType.INFORMATION,
                                "The contest has ended." +
                                "The winner is " + battleFieldInfo.getWinner()
                        );
                        alert.setTitle("The contest has ended.");
                        alert.showAndWait();
                        this.tableViewContest.getItems().clear();
                        this.clearView();
                    }
                }
                else{
                    this.tableViewContest.getItems().clear();
                }
            });
        }
    }

    private void clearView() {
        this.awaitingAssignmentsProperty.unbind();
        this.totalAssignmentsDrawnProperty.unbind();
        this.totalCandidatesProperty.unbind();
        this.totalCompletedAssignmentsProperty.unbind();
        this.totalCandidatesProperty.set(0);
        this.totalCompletedAssignmentsProperty.set(0);
        this.awaitingAssignmentsProperty.set(0);
        this.totalAssignmentsDrawnProperty.set(0);
        this.tableViewCandidates.getItems().clear();
        this.messageToProcess = null;
    }

    private void requestInventory() {
        String finalUrl = HttpUrl
                .parse(REFRESH_DATA)
                .newBuilder()
                .addQueryParameter(DATA, MACHINE_INVENTORY)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> showErrors(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                if (response.code() != SC_OK) {
                    Platform.runLater(() -> showErrors(body));
                    System.out.println(body);
                }
                else{
                    byte[] staticMachineInventory =
                            GSON_INSTANCE.fromJson(
                                    body,
                                    byte[].class
                            );
                    decipher(staticMachineInventory);
                }
                response.close();
            }
        });
    }

    private void decipher(byte[] staticMachineInventory){
        this.agentsInfoUpdater = new AgentsInfoUpdater(this.me);
        this.timer.schedule(agentsInfoUpdater,REFRESH_RATE,REFRESH_RATE);
        this.decipherTask = new DecipherTask(
                this.me,
                this.messageToProcess,
                staticMachineInventory,
                this::updateCandidates
        );
        this.totalCompletedAssignmentsProperty.bind(decipherTask.totalCompletedAssignmentsProperty());
        this.totalCandidatesProperty.bind(decipherTask.totalCandidatesFoundProperty());
        this.totalAssignmentsDrawnProperty.bind(decipherTask.totalDrawnAssignmentsProperty());
        this.awaitingAssignmentsProperty.bind(decipherTask.awaitingAssignmentsProperty());
        this.decipherTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                    decipherTask.cancel();
                    try {
                        decipherThread.join();
                    } catch (InterruptedException ignored) {}
                }
        );
        this.decipherThread = new Thread(this.decipherTask, "Decipher Task");
        this.decipherThread.start();
    }

    private void updateCandidates(CandidatesDTO candidatesDTO) {
        Platform.runLater(() -> {
            if (candidatesDTO != null) {

                for (CandidatesInfo info : candidatesDTO.getCandidates()) {
                    this.tableViewCandidates.getItems().add(info);
                }

            }
        });
    }
}
