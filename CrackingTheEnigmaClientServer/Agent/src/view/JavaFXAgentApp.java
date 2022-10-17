package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.main.MainAgentAppController;

import java.net.URL;

public class JavaFXAgentApp extends Application {
    private static final String MAIN_AGENT_VIEW = "/view/main/AgentMainView.fxml";

    private MainAgentAppController agentAppControllerAppController = null;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource(MAIN_AGENT_VIEW);
        loader.setLocation(mainFXML);
        Parent root = loader.load();
        this.agentAppControllerAppController = loader.getController();
        primaryStage.setTitle("Agent");

        Scene scene = new Scene(root, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
