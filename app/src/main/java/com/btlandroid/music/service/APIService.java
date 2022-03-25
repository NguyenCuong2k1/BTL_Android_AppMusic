package com.btlandroid.music.service;

import com.btlandroid.music.config.Config;

public class APIService {
    private static String baseUrl = (Config.domain);
    public static DataService getService() {
        return APIRetrofitClient.getClient(baseUrl).create(DataService.class);
    }
}
