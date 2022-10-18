package uboat.view.bottom;

import dto.codeconfig.CodeConfigInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import uboat.view.main.MainUBoatAppController;

import java.util.Collections;

public class UBoatBottomController {

    @FXML
    private Text textCodeConfig;

    @FXML
    private Button buttonResetCode;

    private MainUBoatAppController uBoatMainController = null;

    @FXML
    void onResetCode(ActionEvent event) {
        this.uBoatMainController.resetCode();
    }
    public void codeSet(CodeConfigInfo currentCodeConfig) {
        StringBuilder codeConfigToShow = new StringBuilder("<");
        int index = 0;
        int notchPos = 0;

        Collections.reverse(currentCodeConfig.getRotorsID());

        for (Integer rotorID : currentCodeConfig.getRotorsID()) {
            codeConfigToShow.append(String.format("%s", rotorID + ","));
        }

        Collections.reverse(currentCodeConfig.getRotorsID());
        codeConfigToShow.replace(codeConfigToShow.length() - 1, codeConfigToShow.length(), ">");
        codeConfigToShow.append("<");
        Collections.reverse(currentCodeConfig.getRotorsNotchPos());

        for(int i = currentCodeConfig.getRotorsStartingPos().length() - 1; i >=0; i--){
            notchPos = currentCodeConfig.getRotorsNotchPos().get(index);
            codeConfigToShow.append(String.format("%s", currentCodeConfig.getRotorsStartingPos().charAt(i) + "(" + notchPos + ")" + ","));
            index++;
        }

        codeConfigToShow.replace(codeConfigToShow.length() - 1, codeConfigToShow.length(), ">");
        Collections.reverse(currentCodeConfig.getRotorsNotchPos());
        codeConfigToShow.append(String.format("%s", "<" + currentCodeConfig.getReflectorID() + ">"));
        this.textCodeConfig.setText(codeConfigToShow.toString());
    }

    public void setMainAppController(MainUBoatAppController mainUBoatAppController) {
        this.uBoatMainController = mainUBoatAppController;
    }
}
