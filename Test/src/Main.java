import dto.agents.AgentsDTO;
import dto.agents.AgentsInfo;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static httpcommon.constants.Constants.SC_OK;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;

public class Main {

    public final static String BASE_URL = "http://localhost:8080/BattleFieldServer_Web_exploded";

    public static void main(String[] args) throws InterruptedException {

        doPost();
        Thread.sleep(2000);
        doGet();

    }

    private static void doGet() {
        String finalURL = HttpUrl
                .parse(BASE_URL + "/test")
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalURL)
                .get()
                .build();
        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //Platform.runLater(() -> showErrors(e.getMessage()));
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                if (response.code() != SC_OK) {
                    //Platform.runLater(() -> showErrors(body));
                    System.out.println(body);
                }
                else{
                    AgentsDTO agentsDTO = GSON_INSTANCE.fromJson(body, AgentsDTO.class);
                    for(AgentsInfo agent : agentsDTO.getAgents()){
                        System.out.println(agent.getName() + " " +
                                agent.getAlliesTeam() + " " +
                                agent.getAssignmentsPerDraw() + " " +
                                agent.getNumberOfThreads());
                    }
                }
                response.code();
            }
        });
    }

    private static void doPost() {
        AgentsDTO agentsDTO = new AgentsDTO();
        for(int i = 0; i < 2; i++){
            agentsDTO.addInfo(new AgentsInfo("avrum- " + i, 3, 3, "mike"));
        }
        String json = "agents=" + GSON_INSTANCE.toJson(agentsDTO);

        String finalURL = HttpUrl
                .parse(BASE_URL + "/test")
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalURL)
                .addHeader("Content-type", "application/json")
                .post(RequestBody.create(json.getBytes()))
                .build();
        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //Platform.runLater(() -> showErrors(e.getMessage()));
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != SC_OK) {
                    String body = response.body().string();
                    System.out.println(body);
                    //Platform.runLater(() -> showErrors(body));
                }
                response.code();
            }
        });

    }

}
