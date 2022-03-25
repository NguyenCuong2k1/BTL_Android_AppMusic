package com.btlandroid.music.service;

import com.btlandroid.music.model.QuangCao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataService {
    @GET("/APIAppMusic/server/Quangcao/data.php")
    Call<List<QuangCao>> getDataBanner();
}
