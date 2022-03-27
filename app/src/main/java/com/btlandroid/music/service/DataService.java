package com.btlandroid.music.service;

import com.btlandroid.music.model.Playlist;
import com.btlandroid.music.model.QuangCao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataService {
    @GET("/APIAppMusic/server/quangcao.php")
    Call<List<QuangCao>> getDataBanner();

    @GET("/APIAppMusic/server/playlist.php")
    Call<List<Playlist>> getPlaylistCurrentDay();
}
