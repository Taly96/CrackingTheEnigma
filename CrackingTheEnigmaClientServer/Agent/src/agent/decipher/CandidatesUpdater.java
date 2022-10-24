package agent.decipher;

import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesInfo;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static agent.http.Configuration.UPDATE_CANDIDATES;
import static httpcommon.constants.Constants.SC_OK;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;

public class CandidatesUpdater implements Runnable {

    private Consumer<CandidatesDTO> candidatesDTOConsumer = null;

    private BlockingQueue<CandidatesInfo> candidates = null;

    private AtomicBoolean hasMoreAssignments = null;

    public CandidatesUpdater(
            Consumer<CandidatesDTO> candidatesDTOConsumer,
            BlockingQueue<CandidatesInfo> candidates,
            AtomicBoolean hasMoreAssignments
    ) {
        this.candidates = candidates;
        this.hasMoreAssignments = hasMoreAssignments;
        this.candidatesDTOConsumer = candidatesDTOConsumer;
    }

    @Override
    public void run() {
        System.out.println("Candidate updater working");
        while(this.hasMoreAssignments.get()){
            CandidatesDTO candidates = new CandidatesDTO();
            CandidatesInfo candidatesInfo =
                    this.candidates.poll();
            while (candidatesInfo != null){
                System.out.println("got agents candidates");
                candidates.addInfo(candidatesInfo);
                candidatesInfo =
                        this.candidates.poll();
            }
            if(candidates.getCandidates().size() !=0 ){
                this.candidatesDTOConsumer.accept(candidates);
                this.updateCandidates(candidates);
            }
        }
        try {
            System.out.println("Candidate updater done");
            Thread.currentThread().join();
        } catch (InterruptedException ignored) {}
    }

    private void updateCandidates(CandidatesDTO candidates) {
        System.out.println("Updating candidates");
        String json =
                "candidates=" +
                        GSON_INSTANCE.toJson(candidates);
        String finalUrl = HttpUrl
                .parse(UPDATE_CANDIDATES)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create(json.getBytes()))
                .build();

        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> showErrors(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                if (response.code() != SC_OK) {
                    Platform.runLater(() -> showErrors(body));
                }
                else{
                    System.out.println("successfully updated candidates");
                    String gameStatus =
                            GSON_INSTANCE.toJson(body, String.class);
//                    if(gameStatus.equals("Ended")){
//                        hasMoreAssignments = false;
//                    }
                }
                response.close();
            }
        });
    }
}
