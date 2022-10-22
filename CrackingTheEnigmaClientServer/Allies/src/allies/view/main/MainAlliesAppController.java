package allies.view.main;

import allies.view.contest.AlliesContestController;
import allies.view.dashboard.AlliesDashBoardController;
import allies.view.login.AlliesLoginController;
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
import java.math.BigInteger;
import java.net.URL;

import static allies.resources.Constants.*;
import static httpcommon.constants.Constants.READY;
import static httpcommon.constants.Constants.SC_OK;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;

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

    public void signUpContest(String contestName) {
        String finalURL = HttpUrl
                .parse(READY)
                .newBuilder()
                .build()
                .toString();
        String json = "battle=" + GSON_INSTANCE.toJson(contestName);
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
                String body = response.body().string();
                if(response.code() == SC_OK) {
                    BattleFieldInfo signedUpFor = GSON_INSTANCE.fromJson(
                            body,
                            BattleFieldInfo.class
                    );
                    Platform.runLater(() -> {
                        contestController.updateSignedUpFor(signedUpFor);
                        switchToContestView();
                        dashBoardController.stopRefreshers();
                    });
                }
                else{
                    Platform.runLater(() -> showErrors(body));
                }
                response.close();
            }
        });
    }

    private void switchToContestView() {
        setMainPanelTo(contestComponent);
    }

    public String getUserName() {
        return this.currentUserProperty.get();
    }

    public void loggedIn(String userName) {
        this.updateUserName(userName);
        this.dashBoardController.startBattleRefresher();
        this.contestController.updateUserName(userName);
        this.switchToDashBoardView();
    }

    public void setReadyForContest(BigInteger assignmentSize) {
        String finalURL = HttpUrl
                .parse(READY)
                .newBuilder()
                .build()
                .toString();
        String json = "assignment=" + GSON_INSTANCE.toJson(assignmentSize.toString());
        Request request = new Request.Builder()
                .url(finalURL)
                .addHeader("Content-type", "application/json")
                .put(RequestBody.create(json.getBytes()))
                .build();
        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> showErrors(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                if(response.code() != SC_OK){
                    Platform.runLater(() -> showErrors(body));
                }
                else{
                    Platform.runLater(() ->{
                        switchToContestView();
                    });
                }
                response.close();
            }
        });

    }
}
