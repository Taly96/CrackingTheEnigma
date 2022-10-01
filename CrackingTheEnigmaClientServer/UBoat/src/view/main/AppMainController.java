package view.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import okhttp3.*;
import view.header.UBoatHeaderController;

import static http.Configuration.BASE_URL;
import static http.Configuration.HTTP_CLIENT;


public class AppMainController {

    @FXML
    private HBox hBoxHeaderComponent;

    @FXML
    private UBoatHeaderController hBoxHeaderComponentController;

    private Stage primaryStage = null;

    private StringProperty filePathProperty = null;

    private final int SC_OK = 200;

    @FXML
    public void initialize(){
        this.hBoxHeaderComponentController.setMainController(this);
        this.hBoxHeaderComponentController.bind(this.filePathProperty);
    }

    public AppMainController(){
        this.filePathProperty = new SimpleStringProperty();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void loadXMLFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File f = fileChooser.showOpenDialog(primaryStage);
        String RESOURCE = "/BattleField_Web_exploded/upload-file";

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file1", f.getName(), RequestBody.create(f, MediaType.parse("text/plain")))
                        .build();

        Request request = new Request.Builder()
                .url(BASE_URL + RESOURCE)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = call.execute();

        if(response.code() == SC_OK){
            this.filePathProperty.set(f.getAbsolutePath());
        }
        else{
            this.filePathProperty.set("An invalid file was chosen.");
        }

    }
}
