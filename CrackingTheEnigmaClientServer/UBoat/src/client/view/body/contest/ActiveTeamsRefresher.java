package client.view.body.contest;

import dto.activeteams.ActiveTeamsDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.http.Configuration.*;
import static client.view.resources.Constants.*;

public class ActiveTeamsRefresher extends TimerTask {

    private final Consumer<ActiveTeamsDTO> activeTeamsDataConsumer;

    private String battleFieldName = null;

    public ActiveTeamsRefresher(
            Consumer<ActiveTeamsDTO> activeTeamsDataConsumer,
            String battleFieldName
    ){
        this.activeTeamsDataConsumer = activeTeamsDataConsumer;
        this.battleFieldName = battleFieldName;
    }
    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(REFRESH_DATA)
                .newBuilder()
                .addQueryParameter(BATTLE, this.battleFieldName)
                .addQueryParameter(DATA, "ACTIVE_TEAMS")
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
                    ActiveTeamsDTO data =
                            GSON_INSTANCE.fromJson(
                                    response.body().string(),
                                    ActiveTeamsDTO.class
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
