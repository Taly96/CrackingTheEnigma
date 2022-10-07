package client.view.body;

import client.view.body.contest.UBoatContestController;
import client.view.body.machine.UBoatMachineController;
import dto.codeconfig.CodeConfigDTO;
import dto.loadedmachine.LoadedMachineDTO;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import client.view.main.AppMainController;

import java.io.IOException;

public class UBoatCenterController {

    @FXML
    private GridPane gridPaneMachineTabComponent;

    @FXML
    private UBoatMachineController gridPaneMachineTabComponentController;

    @FXML
    private GridPane gridPaneContestTabComponent;

    @FXML
    private UBoatContestController gridPaneContestTabComponentController;

    private AppMainController uBoatMainController = null;

    @FXML
    public void initialize(){
        this.gridPaneMachineTabComponentController.setCenterController(this);
        this.gridPaneContestTabComponentController.setCenterController(this);
    }

    public void fileLoaded(LoadedMachineDTO loadedMachineDTO) {
        this.gridPaneMachineTabComponentController.fileLoaded(loadedMachineDTO);
        this.gridPaneContestTabComponentController.fileLoaded(loadedMachineDTO.getStaticMachineDTO());
    }

    public void setCode(CodeConfigDTO codeConfigDTO) throws IOException {
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

    public void setMainAppController(AppMainController appMainController) {
        this.uBoatMainController = appMainController;
    }
}
