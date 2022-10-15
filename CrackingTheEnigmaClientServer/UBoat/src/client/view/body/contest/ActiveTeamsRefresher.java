package client.view.body.contest;

import dto.activeteams.AlliesDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.http.Configuration.*;
import static client.http.HttpClientUtil.runAsync;
import static client.view.resources.Constants.*;

public class ActiveTeamsRefresher extends TimerTask {

    private final Consumer<AlliesDTO> activeTeamsDataConsumer;

    private String uBoatUserName = null;

    public ActiveTeamsRefresher(
            Consumer<AlliesDTO> activeTeamsDataConsumer,
            String uBoatUserName
    ){
        this.activeTeamsDataConsumer = activeTeamsDataConsumer;
        this.uBoatUserName = uBoatUserName;
    }
    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(REFRESH_DATA)
                .newBuilder()
                .addQueryParameter(DATA, "ALLIES")
                .addQueryParameter("username", this.uBoatUserName)
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
                    AlliesDTO data =
                            GSON_INSTANCE.fromJson(
                                    response.body().string(),
                                    AlliesDTO.class
                            );
                    activeTeamsDataConsumer.accept(data);
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