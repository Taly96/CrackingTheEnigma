package httpcommon.utils;

import javafx.scene.control.Alert;

public class Utils {

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
