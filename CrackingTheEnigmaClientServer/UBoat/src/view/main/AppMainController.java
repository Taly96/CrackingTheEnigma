package view.main;

import com.google.gson.Gson;
import dto.loadedmachine.LoadedMachineDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import okhttp3.*;
import view.body.UBoatCenterController;
import view.header.UBoatHeaderController;

import static http.Configuration.*;


public class AppMainController {

    @FXML
    private HBox hBoxHeaderComponent;

    @FXML
    private UBoatHeaderController hBoxHeaderComponentController;

    @FXML
    private TabPane tabPaneCenterComponent;

    @FXML
    private UBoatCenterController tabPaneCenterComponentController;

    private Stage primaryStage = null;

    private StringProperty filePathProperty = null;


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

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file1", f.getName(), RequestBody.create(f, MediaType.parse("text/plain")))
                        .build();

        Request request = new Request.Builder()
                .url(BASE_URL + FILE_UPLOAD)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = call.execute();

        if(response.code() == SC_OK){
            this.filePathProperty.set(f.getAbsolutePath());
            Gson gson = new Gson();
            LoadedMachineDTO loadedMachineDTO =
                    gson.fromJson(
                            response.body().string(),
                            LoadedMachineDTO.class
                    );
            this.tabPaneCenterComponentController.fileLoaded(loadedMachineDTO);
        }
        else{
            this.filePathProperty.set("An invalid file was chosen.");
        }
    }
}
