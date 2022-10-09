package client.resources;

import javafx.scene.control.Alert;

public class Constants {

    public static final String LOGIN_VIEW = "/client/view/login/AlliesLoginView.fxml";

    public static final String DASHBOARD_VIEW = "/client/view/dashboard/AlliesDashBoardView.fxml";

    public static final String CONTEST_VIEW = "/client/view/contest/AlliesContestView.fxml";

    final public static String NO_NAME = "A girl has no name?";

    public static void  showErrors(String errors){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(
                "Something went wrong" +
                        System.lineSeparator() +
                        errors
        );
        alert.showAndWait();
    }
}
