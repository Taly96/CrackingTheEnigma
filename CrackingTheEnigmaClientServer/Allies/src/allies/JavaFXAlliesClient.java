package allies;

import allies.view.main.MainAlliesAppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class JavaFXAlliesClient extends Application {

    private static final String MAIN_ALLIES_VIEW = "/allies/view/main/AlliesMainView.fxml";

    private MainAlliesAppController alliesAppController = null;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource(MAIN_ALLIES_VIEW);
        loader.setLocation(mainFXML);
        Parent root = loader.load();
        this.alliesAppController = loader.getController();
        primaryStage.setTitle("Allies");

        Scene scene = new Scene(root, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
