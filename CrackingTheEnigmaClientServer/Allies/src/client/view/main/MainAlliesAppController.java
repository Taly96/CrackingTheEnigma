package client.view.main;

import client.view.contest.AlliesContestController;
import client.view.dashboard.AlliesDashBoardController;
import client.view.login.AlliesLoginController;
import com.sun.deploy.util.BlackList;
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

import java.net.URL;

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

    public void updateUserName(String userName) {
        this.currentUserProperty.set(userName);
        this.messageToUserProperty.set("Hello " + this.currentUserProperty.get() + "!");
    }

    public void switchToDashBoardView() {
        Platform.runLater(() -> setMainPanelTo(dashBoardComponent));
    }
}
