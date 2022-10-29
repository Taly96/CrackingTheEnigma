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

    private Object updateLock = null;

    public CandidatesRefresher(Consumer<CandidatesDTO> activeTeamsDataConsumer){
        this.candidatesConsumer = activeTeamsDataConsumer;
        this.alliesVersions = new HashMap<>();
        this.updateLock = new Object();
    }
    @Override
    public void run() {
        System.out.println("about to update candidates from server");
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
                    System.out.println("got candidates json");
                    if (data != null) {
                        synchronized (updateLock) {
                            for (CandidatesDTO candidatesDTO : data.getCandidatesDTOList()) {
                                String alyName = candidatesDTO.getAllyName();
                                if (alyName != null) {
                                    Integer allyVersion = alliesVersions.get(alyName);
                                    if (allyVersion != null) {
                                        if(candidatesDTO.getCandidates().size() > allyVersion){
                                            if(allyVersion != 0){
                                                candidatesDTO.changeVersion(allyVersion);
                                            }
                                            alliesVersions.put(alyName, allyVersion + candidatesDTO.getCandidates().size());
                                            candidatesConsumer.accept(candidatesDTO);
                                            System.out.println("candidates accepted");
                                        }
                                    }
                                    else {
                                        alliesVersions.put(alyName, candidatesDTO.getCandidates().size());
                                        candidatesConsumer.accept(candidatesDTO);
                                        System.out.println("candidates accepted");
                                    }
                                }
                            }
                            updateLock.notifyAll();
                        }
                    }
                }
                else {
                    System.out.println(body);
                    Platform.runLater(() -> showErrors(body));
                }
                response.close();
            }
        });
    }
}
