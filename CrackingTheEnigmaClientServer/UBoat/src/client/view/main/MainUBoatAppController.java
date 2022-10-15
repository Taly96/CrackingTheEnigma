package client.view.main;

import client.view.body.UBoatCenterController;
import client.view.bottom.UBoatBottomController;
import client.view.header.UBoatHeaderController;
import client.view.login.UBoatLoginController;
import client.view.resources.Constants;
import dto.codeconfig.CodeConfigInfo;
import dto.loadedmachine.LoadedMachineDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import static client.http.Configuration.*;
import static client.http.HttpClientUtil.runAsync;
import static client.view.resources.Constants.NO_NAME;
import static client.view.resources.Constants.showErrors;


public class MainUBoatAppController {

    @FXML
    private BorderPane borderPaneApp;

    private HBox hBoxHeaderComponent;

    private UBoatHeaderController headerController;


    private TabPane tabPaneCenterComponent;


    private UBoatCenterController centerController;


    private VBox vBoxBottomComponent;


    private UBoatBottomController bottomController;

    private GridPane loginComponent = null;

    private UBoatLoginController uBoatLoginController = null;

    private Stage primaryStage = null;

    private StringProperty filePathProperty = null;

    private BooleanProperty isLoggedInProperty = null;

    private BooleanProperty isFileLoadedProperty = null;

    private StringProperty currentUserProperty = null;

    private StringProperty messageToUserProperty = null;

    private StringProperty currentBattleFieldNameProperty = null;

    private BooleanProperty isMachineConfiguredProperty = null;

    @FXML
    public void initialize(){
        this.isFileLoadedProperty = new SimpleBooleanProperty(false);
        this.currentUserProperty = new SimpleStringProperty(Constants.NO_NAME);
        this.messageToUserProperty = new SimpleStringProperty(this.currentUserProperty.get());
        this.currentBattleFieldNameProperty = new SimpleStringProperty("life");
        this.isLoggedInProperty = new SimpleBooleanProperty(false);
        this.isMachineConfiguredProperty = new SimpleBooleanProperty(false);
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
            this.centerController.bind(this.isMachineConfiguredProperty);
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
            this.uBoatLoginController = fxmlLoader.getController();
            this.uBoatLoginController.setMainAppController(this);
            this.uBoatLoginController.bind(this.messageToUserProperty);
            this.setMainPanelTo(this.loginComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MainUBoatAppController(){
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
        String finalUrl = HttpUrl
                .parse(FILE_UPLOAD)
                .newBuilder()
                .build()
                .toString();

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file1", f.getName(), RequestBody.create(f, MediaType.parse("text/plain")))
                        .build();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();
        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> showErrors(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == SC_OK) {
                    LoadedMachineDTO loadedMachineDTO =
                            GSON_INSTANCE.fromJson(
                                    response.body().string(),
                                    LoadedMachineDTO.class
                            );
                    response.close();
                    Platform.runLater(() -> {
                        filePathProperty.set(f.getAbsolutePath());
                        currentBattleFieldNameProperty.set(loadedMachineDTO.getBattleFieldInfo().getBattleFieldName());
                        centerController.fileLoaded(loadedMachineDTO);
                        switchToAppView();
                    });
                }else {
                    String error = response.body().string();
                    response.close();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("File Errors");
                        alert.setContentText(error);
                        filePathProperty.set("An invalid file was chosen.");
                        alert.showAndWait();
                    });
                }
            }
        });
    }

    public void setCodeConfig(CodeConfigInfo userInput) {
        String finalUrl = HttpUrl
                .parse(CODE)
                .newBuilder()
                .addQueryParameter("battle", this.currentBattleFieldNameProperty.get())
                .build()
                .toString();
        String json = "code=" + GSON_INSTANCE.toJson(userInput);

        Request request = new Request.Builder()
                .url(finalUrl)
                .addHeader("Content-type", "application/json")
                .post(RequestBody.create(json.getBytes()))
                .build();

        this.sendCodeConfigRequest(request);
    }

    public void generateCode() {
        String finalUrl = HttpUrl
                .parse(CODE)
                .newBuilder()
                .addQueryParameter("battle", this.currentBattleFieldNameProperty.get())
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .build();

        this.sendCodeConfigRequest(request);
    }

    private void sendCodeConfigRequest(Request request) {
        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    isMachineConfiguredProperty.set(false);
                    showErrors(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                CodeConfigInfo currentCode =
                        GSON_INSTANCE.fromJson(
                                response.body().string(),
                                CodeConfigInfo.class
                        );
                response.close();
                Platform.runLater(() -> {
                    bottomController.codeSet(currentCode);
                    isMachineConfiguredProperty.set(true);
                });
            }
        });
    }

    public void processMessage(String messageToProcess) {
        String finalUrl = HttpUrl
                .parse(PROCESS_MESSAGE)
                .newBuilder()
                .addQueryParameter("battle", this.currentBattleFieldNameProperty.get())
                .build()
                .toString();
        String json = "message=" + GSON_INSTANCE.toJson(messageToProcess);
        Request request = new Request.Builder()
                .url(finalUrl)
                .addHeader("Content-type","application/json")
                .post(RequestBody.create(json.getBytes()))
                .build();
        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> showErrors(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String processedMessage =
                        GSON_INSTANCE.fromJson(
                                response.body().string(),
                                String.class
                        );
                response.close();
                Platform.runLater(() -> centerController.messageProcessed(processedMessage));
            }
        });
    }

    public void updateUserName(String userName) {
        this.isLoggedInProperty.set(true);
        this.currentUserProperty.set(userName);
        this.messageToUserProperty.set("Hello " + this.currentUserProperty.get() + "!");
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
        this.borderPaneApp.setBottom(this.vBoxBottomComponent);
        this.borderPaneApp.setTop(this.hBoxHeaderComponent);
    }

    public void clearUserName() {
        this.currentUserProperty.set("");
        this.messageToUserProperty.set(NO_NAME);
    }

    public String getBattleFieldName() {
        return this.currentBattleFieldNameProperty.get();
    }

    public void startContest() {
        String finalUrl = HttpUrl
                .parse(BASE_URL + "/ally-ready")
                .newBuilder()
                .addQueryParameter("username", this.currentUserProperty.get())
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .put(RequestBody.create("start".getBytes()))
                .build();

        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> showErrors(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String errors = response.body().string();
                response.close();
                Platform.runLater(() -> showErrors(errors));
            }
        });
    }

    public void resetCode() {//todo
    }
}
