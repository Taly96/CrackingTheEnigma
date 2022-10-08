package view.login;

import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import okhttp3.*;
import view.main.MainAlliesAppController;

import java.io.IOException;

import static http.Configuration.LOGIN_PAGE;
import static http.Configuration.runAsync;
import static view.constants.Constants.showErrors;

public class LoginController {

    @FXML
    private VBox vBoxLoginComponent;

    @FXML
    private TextField textFieldUserName;

    @FXML
    private TextField testFieldAlliesName;

    @FXML
    private TableView<AlliesData> tableViewAllies;

    @FXML
    private TableColumn<AlliesData, String> tableColumnName;

    @FXML
    private TableColumn<AlliesData, Integer> tableColumnRegistered;

    private MainAlliesAppController mainAlliesAppController = null;

    @FXML
    void onLogin(ActionEvent event) {
        String userName = textFieldUserName.getText();
        if (userName.isEmpty()) {
            showErrors("User name is empty. Please enter a valid user name to proceed.");
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
                        //todo
                    });
                }
            }
        });
    }

    @FXML
    void onQuit(ActionEvent event) {

    }

    public void setMainAlliesAppController(MainAlliesAppController mainAlliesAppController){
        this.mainAlliesAppController= mainAlliesAppController;
    }

}
