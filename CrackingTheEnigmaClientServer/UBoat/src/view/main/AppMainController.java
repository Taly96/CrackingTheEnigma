package view.main;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AppMainController {

    private Stage primaryStage = null;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void loadXMLFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if(selectedFile == null){
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
    }
}
