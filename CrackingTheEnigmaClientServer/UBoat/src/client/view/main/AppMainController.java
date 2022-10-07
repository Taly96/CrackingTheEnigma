package client.view.main;

import client.view.body.UBoatCenterController;
import client.view.bottom.UBoatBottomController;
import client.view.header.UBoatHeaderController;
import client.view.login.LoginController;
import client.view.resources.Constants;
import com.google.gson.Gson;
import dto.codeconfig.CodeConfigDTO;
import dto.loadedmachine.LoadedMachineDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import static client.http.Configuration.*;


public class AppMainController {

    @FXML
    private BorderPane borderPaneApp;

    private HBox hBoxHeaderComponent;

    private UBoatHeaderController headerController;


    private TabPane tabPaneCenterComponent;


    private UBoatCenterController centerController;


    private VBox vBoxBottomComponent;


    private UBoatBottomController bottomController;

    private GridPane loginComponent = null;

    private LoginController loginController = null;

    private Stage primaryStage = null;

    private StringProperty filePathProperty = null;

    private BooleanProperty isLoggedInProperty = null;

    private StringProperty currentUserProperty = null;

    @FXML
    public void initialize(){
        this.currentUserProperty = new SimpleStringProperty(Constants.NO_NAME);
        this.isLoggedInProperty = new SimpleBooleanProperty(false);
        this.loadHeader();
        this.loadCenter();
        this.loadBottom();
        this.loadLoginPage();
    }

    private void loadBottom() {
        URL bottomURL = getClass().getResource(Constants.BOTTOM_VIEW);

        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(bottomURL);
            this.vBoxBottomComponent = fxmlLoader.load();
            this.bottomController = fxmlLoader.getController();
            this.bottomController.setMainAppController(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCenter() {
        URL centerURL = getClass().getResource(Constants.CENTER_VIEW);

        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(centerURL);
            this.tabPaneCenterComponent = fxmlLoader.load();
            this.centerController = fxmlLoader.getController();
            this.centerController.setMainAppController(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadHeader() {
        URL headerURL = getClass().getResource(Constants.HEADER_VIEW);

        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(headerURL);
            this.hBoxHeaderComponent = fxmlLoader.load();
            this.headerController = fxmlLoader.getController();
            this.headerController.setMainAppController(this);
            this.headerController.bind(
                    this.filePathProperty,
                    this.isLoggedInProperty
            );
            this.borderPaneApp.setTop(this.hBoxHeaderComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadLoginPage() {
        URL loginPageURL = getClass().getResource(Constants.LOGIN_VIEW);
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageURL);
            this.loginComponent = fxmlLoader.load();
            this.loginController = fxmlLoader.getController();
            this.loginController.setMainAppController(this);
            this.setMainPanelTo(this.loginComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AppMainController(){
        this.filePathProperty = new SimpleStringProperty();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void loadXMLFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File f = fileChooser.showOpenDialog(primaryStage);

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file1", f.getName(), RequestBody.create(f, MediaType.parse("text/plain")))
                        .build();

        Request request = new Request.Builder()
                .url(FILE_UPLOAD)
                .post(body)
                .build();

        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Constants.showErrors(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() ->{
                    if(response.code() == SC_OK) {
                        try {
                            filePathProperty.set(f.getAbsolutePath());
                            Gson gson = GSON_INSTANCE;
                            LoadedMachineDTO loadedMachineDTO =
                                    gson.fromJson(
                                            response.body().string(),
                                            LoadedMachineDTO.class
                                    );
                            centerController.fileLoaded(loadedMachineDTO);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        try {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("File Errors");
                            alert.setContentText(response.body().string());
                            filePathProperty.set("An invalid file was chosen.");
                            alert.showAndWait();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }

    public void setCodeConfig(CodeConfigDTO userInput) {
        String json = "code=" + GSON_INSTANCE.toJson(userInput);

        Request request = new Request.Builder()
                .url(CODE)
                .addHeader("Content-type", "application/json")
                .post(RequestBody.create(json.getBytes()))
                .build();

        this.sendCodeConfigRequest(request);
    }

    public void generateCode() {
        Request request = new Request.Builder()
                .url(CODE)
                .get()
                .build();

        this.sendCodeConfigRequest(request);
    }

    private void sendCodeConfigRequest(Request request) {
        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Constants.showErrors(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() ->{
                    try {
                        Gson gson = GSON_INSTANCE;
                        CodeConfigDTO currentCode =
                                gson.fromJson(
                                        response.body().string(),
                                        CodeConfigDTO.class
                                );
                        bottomController.codeSet(currentCode);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

    public void processMessage(String messageToProcess) {
        String json = "message=" + GSON_INSTANCE.toJson(messageToProcess);
        Request request = new Request.Builder()
                .url(PROCESS_MESSAGE)
                .addHeader("Content-type","application/json")
                .post(RequestBody.create(json.getBytes()))
                .build();
        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("something went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        String processedMessage =
                                GSON_INSTANCE.fromJson(
                                        response.body().string(),
                                        String.class
                                );
                        centerController.messageProcessed(processedMessage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

    public void updateUserName(String userName) {
        this.currentUserProperty.set(userName);
    }

    public void switchToLogin() {
        Platform.runLater(() -> {
            this.currentUserProperty.set(Constants.NO_NAME);
            this.setMainPanelTo(loginComponent);
        });
    }

    private void setMainPanelTo(Parent pane) {
        this.borderPaneApp.setCenter(pane);
    }

    public void switchToAppView() {
        this.setMainPanelTo(this.tabPaneCenterComponent);
        this.isLoggedInProperty.set(true);
        this.borderPaneApp.setBottom(this.vBoxBottomComponent);
        this.borderPaneApp.setTop(this.hBoxHeaderComponent);
    }
}
