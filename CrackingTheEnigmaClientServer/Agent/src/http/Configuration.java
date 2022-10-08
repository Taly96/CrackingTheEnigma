package http;

import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Configuration {
    public final static String BASE_URL = "http://localhost:8080/BattleField_Web_exploded";

    public final static String FILE_UPLOAD = BASE_URL + "/upload-file";

    public final static String CODE = BASE_URL + "/code";

    public final static String PROCESS_MESSAGE = BASE_URL + "/process";

    public final static String LOGIN_PAGE = BASE_URL + "/login";

    public final static int SC_OK = 200;

    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();


    public final static Gson GSON_INSTANCE = new Gson();

    public static void runAsync(Request req, Callback callback) {
        Call call = HTTP_CLIENT.newCall(req);
        call.enqueue(callback);
    }
}
