package client.view.main;

import client.view.contest.AlliesContestController;
import client.view.dashboard.AlliesDashBoardController;
import client.view.login.AlliesLoginController;
import dto.activeteams.AlliesInfo;
import dto.battlefield.BattleFieldInfo;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

import static client.http.Configuration.*;
import static client.resources.Constants.*;

public class MainAlliesAppController {

    @FXML
    private BorderPane borderPaneMainViewComponent;

    private VBox dashBoardComponent = null;

    private AlliesDashBoardController dashBoardController = null;

    private Accordion contestComponent = null;

    private AlliesContestController contestController = null;

    private GridPane loginComponent = null;

    private AlliesLoginController loginController = null;

    private StringProperty currentUserProperty;

    private StringProperty messageToUserProperty;


    @FXML
    public void initialize(){
        this.currentUserProperty = new SimpleStringProperty(NO_NAME);
        this.messageToUserProperty = new SimpleStringProperty(this.currentUserProperty.get());
        this.loadDashBoardView();
        this.loadContestView();
        this.loadLoginView();
    }

    private void loadLoginView() {
        URL loginURL = getClass().getResource(LOGIN_VIEW);

        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginURL);
            this.loginComponent = fxmlLoader.load();
            this.loginController = fxmlLoader.getController();
            this.loginController.setMainAppController(this);
            this.loginController.bind(this.messageToUserProperty);
            this.setMainPanelTo(this.loginComponent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setMainPanelTo(Node mainPanel) {
        this.borderPaneMainViewComponent.setCenter(mainPanel);
    }

    private void loadContestView() {
        URL contestURL = getClass().getResource(CONTEST_VIEW);

        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(contestURL);
            this.contestComponent = fxmlLoader.load();
            this.contestController = fxmlLoader.getController();
            this.contestController.setMainAppController(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadDashBoardView() {
        URL dashBoardURL = getClass().getResource(DASHBOARD_VIEW);

        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(dashBoardURL);
            this.dashBoardComponent = fxmlLoader.load();
            this.dashBoardController = fxmlLoader.getController();
            this.dashBoardController.setMainAppController(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clearUserName() {
        this.currentUserProperty.set("");
        this.messageToUserProperty.set(NO_NAME);
    }

    private void updateUserName(String userName) {
        this.currentUserProperty.set(userName);
        this.messageToUserProperty.set("Hello " + this.currentUserProperty.get() + "!");
    }

    private void switchToDashBoardView() {
         setMainPanelTo(dashBoardComponent);
    }

    public void signUpContest(String contestName, Integer numOfAgents) {
        AlliesInfo me = new AlliesInfo(
                this.currentUserProperty.get(),
                numOfAgents,
                "0"
        );

        String finalURL = HttpUrl
                .parse("ally-ready")
                .newBuilder()
                .addQueryParameter("username", this.currentUserProperty.get())
                .addQueryParameter("battle", contestName)
                .build()
                .toString();
        String json = "ally=" + GSON_INSTANCE.toJson(me);

        Request request = new Request.Builder()
                .url(finalURL)
                .addHeader("Content-type", "application/json")
                .post(RequestBody.create(json.getBytes()))
                .build();

        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> showErrors(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == SC_OK) {
                    BattleFieldInfo signedUpFor = GSON_INSTANCE.fromJson(
                            response.body().string(),
                            BattleFieldInfo.class
                    );
                    Platform.runLater(() -> contestController.updateSignedUpFor(signedUpFor));
                }
                else{
                    Platform.runLater(() -> {
                        try {
                            showErrors(response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        });
    }

    public String getUserName() {
        return this.currentUserProperty.get();
    }

    public void loggedIn(String userName) {
        this.updateUserName(userName);
        this.dashBoardController.startRefreshers();
        this.switchToDashBoardView();
    }
}
