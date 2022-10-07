package client.view.login;

import client.view.resources.Constants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import client.view.main.AppMainController;

import java.io.IOException;

import static client.http.Configuration.*;

public class LoginController {

    @FXML
    private TextField userNameTextField;

    @FXML
    private Button loginButton;

    private AppMainController uBoatMainController = null;

    @FXML
    void loginButtonClicked(ActionEvent event) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            Constants.showErrors("User name is empty. Please enter a valid user name to proceed.");
        }
        String finalUrl = HttpUrl
                .parse(LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                    Constants.showErrors(e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            Constants.showErrors(responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        uBoatMainController.updateUserName(userName);
                        uBoatMainController.switchToAppView();
                    });
                }
            }
        });
    }

    @FXML
    void quitButtonClicked(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void userNameKeyTyped(KeyEvent event) {

    }

    public void setMainAppController(AppMainController appMainController) {
        this.uBoatMainController = appMainController;
    }
}
