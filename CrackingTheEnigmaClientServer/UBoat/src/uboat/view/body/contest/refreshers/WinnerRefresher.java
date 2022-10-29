package uboat.view.body.contest.refreshers;

import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesInfo;
import dto.decipher.OriginalInformation;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static httpcommon.constants.Constants.*;
import static httpcommon.constants.Constants.SC_OK;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;
import static uboat.http.Configuration.WINNER;

public class WinnerRefresher implements Runnable{

    private AtomicBoolean isOnGoingContest = null;

    private BlockingQueue<CandidatesDTO> candidatesToCheck = null;

    private OriginalInformation originalInformation = null;

    public WinnerRefresher(
            BlockingQueue<CandidatesDTO> candidatesToCheck,
            AtomicBoolean isOnGoingContest,
            OriginalInformation originalInformation
    ) {
        this.candidatesToCheck = candidatesToCheck;
        this.isOnGoingContest = isOnGoingContest;
        this.originalInformation = originalInformation;
        System.out.println("Winner Refresher created");
    }

    @Override
    public void run() {
        System.out.println("Refreshing winner started");
        while(isOnGoingContest.get()){
            try {
                CandidatesDTO candidatesDTO =
                        this.candidatesToCheck.take();
                for(CandidatesInfo info : candidatesDTO.getCandidates()){
                    if(info.getCandidate().equals(originalInformation.getOriginalMessage())) {
                        String winner = candidatesDTO.getAllyName();
                        System.out.println("Updating Winner " + winner);
                        this.updateWinner(winner);
                        System.out.println("Updated Winner " + winner + ", going to sleep");
                        Thread.currentThread().join();
                    }
                }
            } catch (InterruptedException interrupted) {
                try {
                    System.out.println("Winner refresher going to sleep");
                    Thread.currentThread().join();
                } catch (InterruptedException ignored) {}
            }

        }
        System.out.println("Winner refreshser done");
    }

    private void updateWinner(String winner) {
        System.out.println("About to update winner in server");
        String finalUrl = HttpUrl
                .parse(UPDATE)
                .newBuilder()
                .addQueryParameter(DATA, WINNER)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create((WINNER + "=" + winner).getBytes()))
                .build();

        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> showErrors(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                if(response.code() != SC_OK){
                    Platform.runLater(() -> showErrors(body));
                    System.out.println(body);
                }

                response.close();
            }
        });
    }
}
