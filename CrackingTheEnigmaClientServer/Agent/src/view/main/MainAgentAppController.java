package view.main;

import dto.agents.AgentsInfo;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import view.constants.Constants;
import view.contest.AgentContestController;
import view.login.LoginController;

import java.net.URL;

public class MainAgentAppController {
    @FXML
    private BorderPane borderPainMainComponent;

    private LoginController loginController = null;

    private AgentContestController contestController = null;

    private VBox loginComponent = null;

    private VBox contestComponent = null;


    @FXML
    public void initialize(){
        this.loadContest();
        this.loadLogin();
    }

    private void loadLogin() {
        URL loginURL = getClass().getResource(Constants.LOGIN_VIEW);

        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginURL);
            this.loginComponent = fxmlLoader.load();
            this.loginController = fxmlLoader.getController();
            this.loginController.setMainAppController(this);
            this.setMainPanelTo(this.loginComponent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadContest() {
        URL contestURL = getClass().getResource(Constants.CONTEST_VIEW);

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

    private StringProperty userNameProperty = null;
    public void loggedIn(AgentsInfo newAgent) {
        this.userNameProperty.set(newAgent.getName());
        this.setMainPanelTo(this.contestComponent);
    }

    private void setMainPanelTo(Parent pane) {
        this.borderPainMainComponent.setCenter(pane);
    }
}
