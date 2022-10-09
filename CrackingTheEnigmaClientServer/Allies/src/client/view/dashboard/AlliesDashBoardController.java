package client.view.dashboard;

import client.view.main.MainAlliesAppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
    private TableView<?> tableViewContests;

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

    @FXML
    void onReady(ActionEvent event) {

    }

    public void setMainAppController(MainAlliesAppController mainAlliesAppController) {
        this.alliesAppController = mainAlliesAppController;
    }
}
