package view.login;

import com.sun.istack.internal.NotNull;
import dto.activeteams.AlliesInfo;
import dto.agents.AgentsInfo;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import okhttp3.*;
import view.main.MainAgentAppController;

import java.io.IOException;
import java.util.Optional;

import static constants.Constants.LOGIN_PAGE;
import static utils.HttpClientUtil.GSON_INSTANCE;
import static utils.HttpClientUtil.runAsync;
import static utils.Utils.showErrors;

public class LoginController {

    @FXML
    private TextField textFieldUserName;

    @FXML
    private TextField testFieldAlliesName;

    @FXML
    private TableView<AlliesInfo> tableViewAllies;

    @FXML
    private TableColumn<?, ?> tableColumnName;

    @FXML
    private TableColumn<?, ?> tableColumnRegistered;

    @FXML
    private Slider sliderThreads;

    @FXML
    private TextField textFieldAssignments;

    @FXML
    private Button buttonSet;

    private MainAgentAppController mainAgentAppController = null;

    private StringProperty alliesChosenProperty;

    private IntegerProperty numOfThreadsProperty;

    private IntegerProperty numOfAssignmentsProperty = null;

    private StringProperty userNameProperty = null;

    @FXML
    public void initialize(){
        this.numOfThreadsProperty = new SimpleIntegerProperty();
        this.numOfAssignmentsProperty = new SimpleIntegerProperty();
        this.alliesChosenProperty = new SimpleStringProperty();
        this.userNameProperty = new SimpleStringProperty();
        this.tableViewAllies
                .selectionModelProperty()
                .addListener((observable, oldValue, newValue) -> this.alliesChosenProperty.set(newValue.getSelectedItem().getName()));
        this.numOfThreadsProperty.bind(this.sliderThreads.valueProperty());
        this.sliderThreads.setValue(1);
    }

    @FXML
    void onSet(ActionEvent event) {
        try{
            Integer parsedAssignments = Integer.parseInt(this.textFieldAssignments.getText());
            if(parsedAssignments == 0){
                throw new NumberFormatException();
            }
            else{
                this.numOfAssignmentsProperty.set(parsedAssignments);
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong input");
            alert.setContentText("Please enter a valid natural number.");
            alert.showAndWait();
        }
    }

    @FXML
    void onLogin(ActionEvent event) {
        String userName = textFieldUserName.getText();
        if (userName.isEmpty()) {
            showErrors("User name is empty. Please enter a valid user name to proceed.");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Login information");
            alert.setContentText(
                    "You are about to login with this information:" +
                            System.lineSeparator() +
                            "User name : " + userName + System.lineSeparator() +
                            "Allies team: " + this.alliesChosenProperty.get() + System.lineSeparator() +
                            "Number of threads: " + this.numOfThreadsProperty.get() +System.lineSeparator() +
                            "Assignments per draw: " + this.numOfAssignmentsProperty.get() + System.lineSeparator() +
                            "Correct?"
                    );
            Optional<ButtonType> buttonType = alert.showAndWait();
            if(buttonType .equals(ButtonType.OK)){
                AgentsInfo newAgent = new AgentsInfo(
                        userName,
                        this.numOfThreadsProperty.get(),
                        this.numOfAssignmentsProperty.get(),
                        this.alliesChosenProperty.get()
                );
                String json = "agent="+GSON_INSTANCE.toJson(newAgent);


                String finalUrl = HttpUrl
                        .parse(LOGIN_PAGE)
                        .newBuilder()
                        .addQueryParameter("username", userName)
                        .addQueryParameter("type", "agent")
                        .build()
                        .toString();

                Request request = new Request.Builder()
                        .url(finalUrl)
                        .post(RequestBody.create(json.getBytes()))
                        .build();
                runAsync(request, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Platform.runLater(() ->
                                showErrors(e.getMessage())
                        );
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.code() != 200) {
                            String responseBody = response.body().string();
                            Platform.runLater(() ->
                                    showErrors(responseBody)
                            );
                        } else {
                            Platform.runLater(() -> {
                                mainAgentAppController.loggedIn(newAgent);
                            });
                        }
                    }
                });
            }
        }
    }

    @FXML
    void onQuit(ActionEvent event) {

    }

    public void setMainAppController(MainAgentAppController mainAgentAppController) {
        this.mainAgentAppController = mainAgentAppController;
    }
}
