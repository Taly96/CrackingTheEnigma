package allies.view.contest.refreshers;

import dto.agents.AgentsDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static httpcommon.constants.Constants.*;
import static httpcommon.constants.Constants.SC_OK;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;

public class TeamProgressRefresher extends TimerTask {

    private String allyUserName = null;

    private Consumer<AgentsDTO> agentsConsumer = null;

    public TeamProgressRefresher(String allyUserName, Consumer<AgentsDTO> agentsConsumer){
        this.agentsConsumer = agentsConsumer;
        this.allyUserName = allyUserName;
    }
    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(REFRESH_DATA)
                .newBuilder()
                .addQueryParameter(DATA, AGENTS)
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
                    AgentsDTO data =
                            GSON_INSTANCE.fromJson(
                                    body,
                                    AgentsDTO.class
                            );
                    agentsConsumer.accept(data);
                }
                else {
                    Platform.runLater(() -> showErrors(body));
                }
                response.close();
            }
        });
    }
}
