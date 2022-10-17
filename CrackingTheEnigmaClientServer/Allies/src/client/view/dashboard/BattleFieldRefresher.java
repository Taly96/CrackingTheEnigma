package client.view.dashboard;

import dto.battlefield.BattleFieldDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.resources.Constants.showErrors;
import static constants.Constants.SC_OK;
import static utils.HttpClientUtil.GSON_INSTANCE;
import static utils.HttpClientUtil.runAsync;

public class BattleFieldRefresher extends TimerTask {

    private final Consumer<BattleFieldDTO> battleFieldsDataConsumer;

    private String alliesUserName = null;

    public BattleFieldRefresher(
            Consumer<BattleFieldDTO> battleFieldsDataConsumer,
            String alliesUserName
    ){
        this.alliesUserName = alliesUserName;
        this.battleFieldsDataConsumer = battleFieldsDataConsumer;
    }
    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse("/refresh")
                .newBuilder()
                .addQueryParameter("username", this.alliesUserName)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> showErrors(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == SC_OK){
                    BattleFieldDTO data =
                            GSON_INSTANCE.fromJson(
                                    response.body().string(),
                                    BattleFieldDTO.class
                            );
                    battleFieldsDataConsumer.accept(data);
                }
                else {
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
}
