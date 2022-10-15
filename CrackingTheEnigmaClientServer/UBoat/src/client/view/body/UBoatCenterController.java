package client.view.body;

import client.view.body.contest.UBoatContestController;
import client.view.body.machine.UBoatMachineController;
import dto.codeconfig.CodeConfigInfo;
import dto.loadedmachine.LoadedMachineDTO;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import client.view.main.MainUBoatAppController;

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

    public void bind(BooleanProperty isMachineConfiguredProperty){
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

    public void startContest() {
        this.uBoatMainController.startContest();
    }
}
