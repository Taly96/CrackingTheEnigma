package client.view.bottom;

import dto.codeconfig.CodeConfigDTO;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import client.view.main.AppMainController;

import java.util.Collections;

public class UBoatBottomController {

    @FXML
    private Text textCodeConfig;

    private AppMainController uBoatMainController = null;

    public void codeSet(CodeConfigDTO currentCodeConfig) {
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

    public void setMainAppController(AppMainController appMainController) {
        this.uBoatMainController = appMainController;
    }
}
