package view.body;

import dto.loadedmachine.LoadedMachineDTO;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import view.body.machine.UBoatMachineController;

public class UBoatCenterController {

    @FXML
    private GridPane gridPaneMachineTabComponent;

    @FXML
    private UBoatMachineController gridPaneMachineTabComponentController;

    public void fileLoaded(LoadedMachineDTO loadedMachineDTO) {
        this.gridPaneMachineTabComponentController.fileLoaded(loadedMachineDTO);
    }
}
