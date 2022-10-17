package client.view.body.contest;

import dto.candidates.CandidatesDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.view.resources.Constants.*;
import static constants.Constants.REFRESH_DATA;
import static constants.Constants.SC_OK;
import static utils.HttpClientUtil.GSON_INSTANCE;
import static utils.HttpClientUtil.runAsync;
import static utils.Utils.showErrors;

public class CandidatesRefresher extends TimerTask {

    private final Consumer<CandidatesDTO> candidatesConsumer;

    private String battleFieldName = null;

    public CandidatesRefresher(
            Consumer<CandidatesDTO> activeTeamsDataConsumer,
            String battleFieldName
    ){
        this.candidatesConsumer = activeTeamsDataConsumer;
        this.battleFieldName = battleFieldName;
    }
    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(REFRESH_DATA)
                .newBuilder()
                .addQueryParameter(BATTLE, this.battleFieldName)
                .addQueryParameter(DATA, "CANDIDATES")
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
                    CandidatesDTO data =
                            GSON_INSTANCE.fromJson(
                                    response.body().string(),
                                    CandidatesDTO.class
                            );
                    candidatesConsumer.accept(data);
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
