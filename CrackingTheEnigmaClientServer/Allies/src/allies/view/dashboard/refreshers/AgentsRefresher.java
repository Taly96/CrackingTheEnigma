package allies.view.dashboard.refreshers;

import dto.agents.AgentsDTO;
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

public class AgentsRefresher extends TimerTask {

    private Consumer<AgentsDTO> agentsConsumer = null;

    private String userName = null;

    public AgentsRefresher(Consumer<AgentsDTO> agentsConsumer, String userName){
        this.agentsConsumer = agentsConsumer;
        this.userName = userName;
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
                    AgentsDTO agents = GSON_INSTANCE.fromJson(body, AgentsDTO.class);
                    agentsConsumer.accept(agents);
                }
                else{
                    Platform.runLater(() -> showErrors(body));
                }
                response.close();
            }
        });
    }
}
