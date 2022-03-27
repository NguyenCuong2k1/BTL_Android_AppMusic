package com.btlandroid.music.service;

import com.btlandroid.music.model.Playlist;
import com.btlandroid.music.model.QuangCao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService {
    @GET("/APIAppMusic/server/quangcao.php")
    Call<List<QuangCao>> getDataBanner();

    @GET("/APIAppMusic/server/playlist.php")
    Call<List<Playlist>> getPlaylistCurrentDay();

    @FormUrlEncoded
    @POST("danhsachbaihat.php")
    Call<List<BaiHat>> GetDanhsachbaihattheoquangcao(@Field("idquangcao") String idquangcao);
}

    @FormUrlEncoded
    @POST("danhsachbaihat.php")
    Call<List<BaiHat>> GetDanhsachbaihattheoplaylist(@Field("idplaylist") String idplaylist);
}
