package uboat.view.body;

import dto.decipher.OriginalInformation;
import uboat.view.body.contest.UBoatContestController;
import uboat.view.body.config.UBoatMachineController;
import dto.codeconfig.CodeConfigInfo;
import dto.loadedmachine.LoadedMachineDTO;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import uboat.view.main.MainUBoatAppController;

import java.io.IOException;

public class UBoatCenterController {

    @FXML
    private GridPane gridPaneMachineTabComponent;

    @FXML
    private UBoatMachineController gridPaneMachineTabComponentController;

    @FXML
    private GridPane gridPaneContestTabComponent;

    @FXML
    private Tab tabContest;

    @FXML
    private Tab tabMachine;

    @FXML
    private UBoatContestController gridPaneContestTabComponentController;

    private MainUBoatAppController uBoatMainController = null;

    @FXML
    public void initialize(){
        this.gridPaneMachineTabComponentController.setCenterController(this);
        this.gridPaneContestTabComponentController.setCenterController(this);
        this.tabContest.setDisable(true);
    }

    public void fileLoaded(LoadedMachineDTO loadedMachineDTO) {
        this.gridPaneMachineTabComponentController.fileLoaded(loadedMachineDTO);
        this.gridPaneContestTabComponentController.fileLoaded(loadedMachineDTO.getStaticMachineDTO());
    }

    public void bind(BooleanProperty isMachineConfiguredProperty, BooleanProperty isContestStartedProperty){
        this.tabMachine.disableProperty().bind(isContestStartedProperty);
        this.gridPaneContestTabComponentController.bind(isContestStartedProperty);
        this.tabContest.disableProperty().bind(isMachineConfiguredProperty.not());
    }

    public void setCode(CodeConfigInfo codeConfigDTO) throws IOException {
        this.uBoatMainController.setCodeConfig(codeConfigDTO);
    }

    public void generateCode() {
        this.uBoatMainController.generateCode();
    }

    public void processMessage(String messageToProcess) {
        this.uBoatMainController.processMessage(messageToProcess);
    }

    public void messageProcessed(String processedMessage) {
        this.gridPaneContestTabComponentController.messageProcessed(processedMessage);
    }

    public void setMainAppController(MainUBoatAppController mainUBoatAppController) {
        this.uBoatMainController = mainUBoatAppController;
    }

    public String getBattleFieldName() {
        return this.uBoatMainController.getBattleFieldName();
    }

    public void startContest(String processedMessage) {
        this.uBoatMainController.startContest(processedMessage);
    }

    public void contestEnded(){
        this.gridPaneMachineTabComponentController.contestEnded();
        this.uBoatMainController.contestEnded();
    }

    public void contestStarted() {
        this.gridPaneContestTabComponentController.startRefreshers();
    }
}
