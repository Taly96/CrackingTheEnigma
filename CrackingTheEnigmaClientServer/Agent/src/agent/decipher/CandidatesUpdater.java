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

import static httpcommon.constants.Constants.*;
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
        System.out.println("candidates updater in agent running");
        while(this.hasMoreAssignments.get()){
            CandidatesDTO candidates = new CandidatesDTO();
            CandidatesInfo candidatesInfo =
                    this.candidates.poll();
            while (candidatesInfo != null){
                System.out.println("candidates updater got candidates");
                candidates.addInfo(candidatesInfo);
                candidatesInfo =
                        this.candidates.poll();
            }
            if(candidates.getCandidates().size() !=0 ){
                this.candidatesDTOConsumer.accept(candidates);
                System.out.println("accepted candidates updater candidates");
                this.updateCandidates(candidates);
            }
        }
        try {
            System.out.println("candidates updater is going to sleep");
            Thread.currentThread().join();
        } catch (InterruptedException ignored) {}
    }

    private void updateCandidates(CandidatesDTO candidates) {
        System.out.println("about to send update candidates req to server");
        String json =
                "candidates=" +
                        GSON_INSTANCE.toJson(candidates);
        String finalUrl = HttpUrl
                .parse(UPDATE)
                .newBuilder()
                .addQueryParameter(DATA, CANDIDATES)
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
                    System.out.println(body);
                }
                response.close();
            }
        });
    }
}
