package client.view.resources;

import javafx.scene.control.Alert;

public class Constants {

    final public static String ROTOR_VIEW = "/client/view/body/machine/RotorView.fxml";

    final public static String ROTOR_STYLE = "/client/view/body/machine/rotors/RotorStyle.css";

    final public static String NO_NAME = "A girl has no name";

    final public static String LOGIN_VIEW = "/client/view/login/LoginView.fxml";

    final public static String HEADER_VIEW = "/client/view/header/UBoatHeaderView.fxml";

    final public static String BOTTOM_VIEW = "/client/view/bottom/UBoatBottomView.fxml";

    final public static String CENTER_VIEW = "/client/view/body/UBoatCenterView.fxml";

    final public static void  showErrors(String errors){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(
                "Something went wrong" +
                System.lineSeparator() +
                errors
        );
        alert.showAndWait();
    }
}
