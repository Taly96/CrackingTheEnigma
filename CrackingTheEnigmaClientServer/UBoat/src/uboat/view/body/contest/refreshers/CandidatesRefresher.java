package uboat.view.body.contest.refreshers;

import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesDTOList;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.function.Consumer;

import static httpcommon.constants.Constants.*;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;

public class CandidatesRefresher extends TimerTask {

    private final Consumer<CandidatesDTO> candidatesConsumer;

    private Map<String, Integer> alliesVersions = null;

    public CandidatesRefresher(Consumer<CandidatesDTO> activeTeamsDataConsumer){
        this.candidatesConsumer = activeTeamsDataConsumer;
        this.alliesVersions = new HashMap<>();
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
                    if(data != null) {
                        for (CandidatesDTO candidatesDTO : data.getCandidatesDTOList()) {
                            String alyName = candidatesDTO.getAllyName();
                            if(alyName != null) {
                                Integer allyVersion = alliesVersions.get(alyName);
                                if (allyVersion != null) {
                                    if (allyVersion == 0) {
                                        alliesVersions.put(alyName, candidatesDTO.getCandidates().size());
                                    } else {
                                        candidatesDTO.changeVersion(allyVersion);
                                        alliesVersions.put(alyName, candidatesDTO.getCandidates().size());
                                        candidatesConsumer.accept(candidatesDTO);
                                    }
                                } else {
                                    alliesVersions.put(alyName, candidatesDTO.getCandidates().size());
                                }
                            }
                        }
                    }
                }
                else {
                    Platform.runLater(() -> showErrors(body));
                }
                response.close();
            }
        });
    }
}
