package uboat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uboat.view.main.MainUBoatAppController;

import java.net.URL;

public class JavaFXUBoatClient extends Application {
    private static final String MAIN_UBOAT_SCENE_PATH = "/uboat/view/main/UBoatMainView.fxml";

    private MainUBoatAppController uBoatMainController = null;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource(MAIN_UBOAT_SCENE_PATH);
        loader.setLocation(mainFXML);
        Parent root = loader.load();
        this.uBoatMainController = loader.getController();
        this.uBoatMainController.setPrimaryStage(primaryStage);
        primaryStage.setTitle("UBoat");

        Scene scene = new Scene(root, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
