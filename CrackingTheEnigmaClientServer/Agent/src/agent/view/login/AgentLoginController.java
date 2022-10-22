package agent.view.login;

import agent.view.login.refreshers.AlliesDataRefresher;
import agent.view.main.MainAgentAppController;
import com.sun.istack.internal.NotNull;
import dto.activeteams.AlliesDTO;
import dto.activeteams.AlliesInfo;
import dto.agents.AgentsInfo;
import dto.battlefield.BattleFieldInfo;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import okhttp3.*;

import java.io.IOException;
import java.util.Optional;
import java.util.Timer;

import static agent.http.Configuration.AGENT;
import static httpcommon.constants.Constants.*;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;

public class AgentLoginController {

    @FXML
    private TextField textFieldUserName;

    @FXML
    private TextField textFieldAlliesName;

    @FXML
    private TableView<AlliesInfo> tableViewAllies;

    @FXML
    private TableColumn<AlliesInfo, String> tableColumnName;

    @FXML
    private TableColumn<AlliesInfo, Integer> tableColumnRegistered;

    @FXML
    private Slider sliderThreads;

    @FXML
    private TextField textFieldAssignments;

    @FXML
    private Button buttonSet;

    @FXML
    private Button buttonLogin;

    private MainAgentAppController mainAgentAppController = null;

    private StringProperty alliesChosenProperty;

    private IntegerProperty numOfThreadsProperty;

    private IntegerProperty numOfAssignmentsProperty = null;

    private StringProperty userNameProperty = null;

    private AlliesDataRefresher alliesDataRefresher = null;

    private Timer timer = null;

    @FXML
    public void initialize(){
        this.numOfThreadsProperty = new SimpleIntegerProperty();
        this.numOfAssignmentsProperty = new SimpleIntegerProperty();
        this.alliesChosenProperty = new SimpleStringProperty("Click on an ally to join its' team.");
        this.userNameProperty = new SimpleStringProperty();
        this.numOfThreadsProperty.bind(this.sliderThreads.valueProperty());
        this.sliderThreads.setValue(1);
        this.textFieldAlliesName.textProperty().bind(this.alliesChosenProperty);
        this.tableViewAllies.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.tableColumnName.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getName())
        );
        this.tableColumnRegistered.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getNumOfAgents()).asObject()
        );
    }

    @FXML
    void onSet(ActionEvent event) {
        try{
            int parsedAssignments = Integer.parseInt(this.textFieldAssignments.getText());
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
        else if(this.alliesChosenProperty.get().equals("Click on an ally to join its' team.")){
            showErrors("Please choose an ally to join.");
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
            if(buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)){
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
                        .addQueryParameter(USERNAME, userName)
                        .addQueryParameter(TYPE, AGENT)
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
                        String responseBody = response.body().string();
                        if (response.code() != 200) {
                            Platform.runLater(() ->
                                    showErrors(responseBody)
                            );
                        } else {
                            Platform.runLater(() -> {
                                mainAgentAppController.loggedIn(newAgent);
                                stopRefresher();
                            });
                        }
                        response.close();
                    }
                });
            }
        }
    }

    @FXML
    void onQuit(ActionEvent event) {
        Platform.exit();
    }

    public void setMainAppController(MainAgentAppController mainAgentAppController) {
        this.mainAgentAppController = mainAgentAppController;
    }

    public void startRefresher() {
        this.alliesDataRefresher = new AlliesDataRefresher(
                this::updateAllies
        );
        this.timer = new Timer();
        this.timer.schedule(this.alliesDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateAllies(AlliesDTO alliesDTO) {
        Platform.runLater(() -> {
            AlliesInfo selectedCell =
                    this.tableViewAllies.getSelectionModel().getSelectedItem();
            if(selectedCell != null){
                this.alliesChosenProperty.set(selectedCell.getName());
            }
            this.tableViewAllies.getItems().clear();

            for(AlliesInfo info : alliesDTO.getAllies()){
                this.tableViewAllies.getItems().add(info);
            }
        });
    }

    private void stopRefresher(){
        this.alliesDataRefresher.cancel();
        this.timer.cancel();
    }
}
