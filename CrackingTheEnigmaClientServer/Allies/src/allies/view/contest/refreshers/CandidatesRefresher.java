package allies.view.contest.refreshers;

import dto.battlefield.BattleFieldDTO;
import dto.candidates.CandidatesDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static httpcommon.constants.Constants.*;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;

public class CandidatesRefresher extends TimerTask {

    private Consumer<CandidatesDTO> candidatesConsumer = null;

    private int version = 0;

    private Object updateLock = null;

    public CandidatesRefresher(Consumer<CandidatesDTO> candidatesConsumer) {
        this.candidatesConsumer = candidatesConsumer;
        this.updateLock = new Object();
    }

    @Override
    public void run() {
        System.out.println("Ally updating candidates from server, about to send request");
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
                    synchronized (updateLock) {
                        CandidatesDTO data =
                                GSON_INSTANCE.fromJson(
                                        body,
                                        CandidatesDTO.class
                                );
                        System.out.println("ally got candidates json");
                        if (data.getCandidates().size() > version) {
                            if (version != 0) {
                                data.changeVersion(version);
                            }
                            version += data.getCandidates().size();
                            candidatesConsumer.accept(data);
                            System.out.println("allies candidates accepted");
                        }
                        updateLock.notifyAll();
                    }
                }
                else {
                    Platform.runLater(() -> showErrors(body));
                    System.out.println(body);
                }
                response.close();
            }
        });
    }
}
