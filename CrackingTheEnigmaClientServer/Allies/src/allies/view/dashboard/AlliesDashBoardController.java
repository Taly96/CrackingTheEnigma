package allies.view.dashboard;

import allies.view.main.MainAlliesAppController;
import dto.agents.AgentsDTO;
import dto.agents.AgentsInfo;
import dto.battlefield.BattleFieldDTO;
import dto.battlefield.BattleFieldInfo;
import javafx.application.Platform;
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

    private MainAlliesAppController alliesAppController = null;

    private Timer timer = null;

    private BattleFieldsRefresher battleFieldsRefresher = null;

    private AgentsRefresher agentsRefresher = null;

    private StringProperty chosenBattleField = null;

    @FXML
    public void initialize(){
        this.chosenBattleField = new SimpleStringProperty();
        this.tableViewContests.selectionModelProperty()
                .addListener((observable, oldValue, newValue) ->
                        this.chosenBattleField
                                .set(newValue.getSelectedItem()
                                        .getBattleFieldName()
                                )
                );
        this.startRefreshers();
    }

    @FXML
    void onSignUpContest(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm BattleField");
        alert.setContentText(
                "You are about to register to the "
                        + this.chosenBattleField.get()
                        + " contest."
                        + System.lineSeparator()
                        + "Correct?"
        );
        Optional<ButtonType> res = alert.showAndWait();
        if(res.isPresent() && res.equals(ButtonType.OK)) {
            this.alliesAppController.signUpContest(
                    this.chosenBattleField.get(),
                    this.tableViewAgents.getItems().size());
        }
    }

    public void setMainAppController(MainAlliesAppController mainAlliesAppController) {
        this.alliesAppController = mainAlliesAppController;
    }

    public void startRefreshers(){
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
