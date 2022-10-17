package client.view.login;

import client.view.main.MainAlliesAppController;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

import static client.resources.Constants.showErrors;
import static constants.Constants.LOGIN_PAGE;
import static constants.Constants.SC_OK;
import static utils.HttpClientUtil.runAsync;

public class AlliesLoginController {

    @FXML
    private TextField textFieldUserName;

    @FXML
    private Button buttonLogin;

    @FXML
    private Button buttonQuit;

    @FXML
    private Text textInstructions;

    private MainAlliesAppController alliesAppController = null;

    @FXML
    void onLogin(ActionEvent event) {
        String userName = textFieldUserName.getText();
        if (userName.isEmpty()) {
            showErrors("User name is empty. Please enter a valid user name to proceed.");
            this.alliesAppController.clearUserName();
        }
        else {
            String finalUrl = HttpUrl
                    .parse(LOGIN_PAGE)
                    .newBuilder()
                    .addQueryParameter("username", userName)
                    .addQueryParameter("type", "ally")
                    .build()
                    .toString();
            Request request = new Request.Builder()
                    .url(finalUrl)
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
                        Platform.runLater(() -> alliesAppController.loggedIn(userName));
                    }
                }
            });
        }
    }

    @FXML
    void onQuit(ActionEvent event) {
        Platform.exit();
    }

    public void setMainAppController(MainAlliesAppController mainAlliesAppController) {
        this.alliesAppController = mainAlliesAppController;
    }

    public void bind(StringProperty messageToUserProperty) {
        this.textInstructions.textProperty().bind(messageToUserProperty);

    }
}
