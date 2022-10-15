package client.http;

import com.google.gson.Gson;
import okhttp3.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuration {
    public final static String BASE_URL = "http://localhost:8080/BattleFieldServer_Web_exploded";

    public final static String FILE_UPLOAD = BASE_URL + "/upload-file";

    public final static String CODE = BASE_URL + "/code";

    public final static String PROCESS_MESSAGE = BASE_URL + "/process";

    public final static String LOGIN_PAGE = BASE_URL + "/login";

    public final static int SC_OK = 200;

    public final static String REFRESH_DATA = "/refresh";

    public final static int REFRESH_RATE = 2000;



    public final static Gson GSON_INSTANCE = new Gson();


}