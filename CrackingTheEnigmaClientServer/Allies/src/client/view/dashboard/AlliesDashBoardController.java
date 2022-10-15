package client.view.dashboard;

import client.view.main.MainAlliesAppController;
import dto.activeteams.AlliesInfo;
import dto.battlefield.BattleFieldDTO;
import dto.battlefield.BattleFieldInfo;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;
import java.util.Timer;

import static client.http.Configuration.*;
import static client.resources.Constants.showErrors;

public class AlliesDashBoardController {

    @FXML
    private TableView<?> tableViewAgents;

    @FXML
    private TableColumn<?, ?> tableColumnName;

    @FXML
    private TableColumn<?, ?> tableColumnNumberOfThreads;

    @FXML
    private TableColumn<?, ?> tableColumnAssignmentSize;

    @FXML
    private TableView<BattleFieldInfo> tableViewContests;

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
    private Button buttonReady;

    private MainAlliesAppController alliesAppController = null;

    private Timer timer = null;

    private BattleFieldRefresher battleFieldRefresher = null;

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
        this.battleFieldRefresher = new BattleFieldRefresher(
                this::updateContests,
                this.alliesAppController.getUserName()
        );
        this.timer = new Timer();
        this.timer.schedule(this.battleFieldRefresher, 2000,2000);
    }

    private void updateContests(BattleFieldDTO battleFieldDTO) {
        Platform.runLater(() -> {
            this.tableViewContests.getItems().clear();
            for(BattleFieldInfo info : battleFieldDTO.getBattleFields()){
                this.tableViewContests.getItems().add(info);
            }
        });
    }
}
