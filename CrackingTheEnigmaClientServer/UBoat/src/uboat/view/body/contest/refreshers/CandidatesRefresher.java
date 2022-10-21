package uboat.view.body.contest.refreshers;

import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesDTOList;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static httpcommon.constants.Constants.*;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;

public class CandidatesRefresher extends TimerTask {

    private final Consumer<CandidatesDTOList> candidatesConsumer;

    private String battleFieldName = null;

    public CandidatesRefresher(
            Consumer<CandidatesDTOList> activeTeamsDataConsumer,
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
                .addQueryParameter(DATA, CANDIDATES)
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
                String body = response.body().string();
                if(response.code() == SC_OK){
                    CandidatesDTOList data =
                            GSON_INSTANCE.fromJson(
                                    body,
                                    CandidatesDTOList.class
                            );
                    candidatesConsumer.accept(data);
                }
                else {
                    Platform.runLater(() -> showErrors(body));
                }
                response.close();
            }
        });
    }
}
