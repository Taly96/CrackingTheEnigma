package agent.view.contest.refreshers;

import dto.activeteams.AlliesDTO;
import dto.battlefield.BattleFieldInfo;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static agent.http.Configuration.AGENT;
import static agent.http.Configuration.BATTLE;
import static httpcommon.constants.Constants.*;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;

public class ContestRefresher extends TimerTask {

    private Consumer<BattleFieldInfo> battleFieldInfoConsumer = null;

    public ContestRefresher(Consumer<BattleFieldInfo> battleFieldInfoConsumer){
        this.battleFieldInfoConsumer = battleFieldInfoConsumer;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(REFRESH_DATA)
                .newBuilder()
                .addQueryParameter(DATA, BATTLE)
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
                    BattleFieldInfo data =
                            GSON_INSTANCE.fromJson(
                                    body,
                                    BattleFieldInfo.class
                            );
                    battleFieldInfoConsumer.accept(data);
                }
                else {
                    Platform.runLater(() -> showErrors(body));
                }
                response.close();
            }
        });
    }
}
