package agent.view.contest.refreshers;

import dto.agents.AgentsInfo;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;

import static agent.http.Configuration.AGENT;
import static httpcommon.constants.Constants.*;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;

public class AgentsInfoUpdater extends TimerTask {

    private AgentsInfo infoToUpdate = null;

    public AgentsInfoUpdater(AgentsInfo agent){
        this.infoToUpdate = agent;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(UPDATE)
                .newBuilder()
                .addQueryParameter(DATA, AGENT)
                .build()
                .toString();
        String json = "agent=" + GSON_INSTANCE.toJson(this.infoToUpdate);
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
                if(response.code() != SC_OK){
                    Platform.runLater(() -> showErrors(body));
                    System.out.println(body);
                }
                response.close();
            }
        });
    }
}
