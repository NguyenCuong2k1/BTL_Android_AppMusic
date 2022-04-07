package com.btlandroid.music.retrofit;

import com.btlandroid.music.config.Config;

public class APIService {
    private static String baseUrl = (Config.domain);
    public static DataService getService() {
        return APIRetrofitClient.getClient(baseUrl).create(DataService.class);
    }
}
