package com.github.andrei1993ak.finances.api;

import com.example.andre.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

public class ApiManager {

    private static final String APP_ENGINE_BASE_URL = "https://financepmver3.appspot.com/_ah/api";
    private MyApi myApi;

    private static ApiManager instance;

    public static ApiManager get() {
        if (instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }

    private ApiManager() {
    }

    public MyApi myApi() {
        if (myApi == null) {
            final MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(), null)
                    .setRootUrl(APP_ENGINE_BASE_URL);
            myApi = builder.build();
        }
        return myApi;
    }

}
