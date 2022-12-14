package uboat.view.login;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import uboat.view.main.MainUBoatAppController;

import java.io.IOException;

import static httpcommon.constants.Constants.*;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;
import static uboat.http.Configuration.UBOAT;


public class UBoatLoginController {

    @FXML
    private TextField userNameTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Text textInstructions;

    private MainUBoatAppController uBoatMainController = null;

    @FXML
    public void initialize(){
    }
    @FXML
    void loginButtonClicked(ActionEvent event) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            showErrors("User name is empty. Please enter a valid user name to proceed.");
            this.uBoatMainController.clearUserName();
        }
        else {
            String finalUrl = HttpUrl
                    .parse(LOGIN_PAGE)
                    .newBuilder()
                    .addQueryParameter(USERNAME, userName)
                    .addQueryParameter(TYPE, UBOAT)
                    .build()
                    .toString();
            Request request = new Request.Builder()
                    .url(finalUrl)
                    .get()
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
                    if (response.code() != SC_OK) {
                        String responseBody = response.body().string();
                        Platform.runLater(() ->
                                showErrors(responseBody)
                        );
                    } else {
                        Platform.runLater(() ->{
                            uBoatMainController.updateUserName(userName);
                        });
                    }
                    response.close();
                }
            });
        }
    }

    @FXML
    void quitButtonClicked(ActionEvent event) {
        Platform.exit();
    }

    public void setMainAppController(MainUBoatAppController mainUBoatAppController) {
        this.uBoatMainController = mainUBoatAppController;
    }

    public void bind(StringProperty currentUserProperty) {
        this.textInstructions.textProperty().bind(currentUserProperty);
    }
}
