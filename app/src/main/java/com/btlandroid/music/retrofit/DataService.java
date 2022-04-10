package com.btlandroid.music.retrofit;

import com.btlandroid.music.model.Album;
import com.btlandroid.music.model.BaiHat;
import com.btlandroid.music.model.ChuDe;
import com.btlandroid.music.model.Playlist;
import com.btlandroid.music.model.QuangCao;
import com.btlandroid.music.model.TheLoai;
import com.btlandroid.music.model.TheLoaiTrongNgay;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService {
    @GET("/APIAppMusic/server/quangcao.php")
    Call<List<QuangCao>> getDataBanner();

    @GET("/APIAppMusic/server/playlistforday.php")
    Call<List<Playlist>> getPlaylistCurrentDay();

    @GET("/APIAppMusic/server/theloaichudeforday.php")
    Call<TheLoaiTrongNgay> getCategoryMusic();

    @GET("/APIAppMusic/server/albumforday.php")
    Call<List<Album>> getListAlbumHot();

    @GET("/APIAppMusic/server/baihatyeuthich.php")
    Call<List<BaiHat>> getListBaiHatHot();

    @FormUrlEncoded
    @POST("/APIAppMusic/server/danhsachbaihat.php")
    Call<List<BaiHat>> getListSongByBanner(@Field("idQuangCao") String idBanner);

    @FormUrlEncoded
    @POST("/APIAppMusic/server/danhsachbaihat.php")
    Call<List<BaiHat>> getListSongByPlaylist(@Field("idPlaylist") String idPlaylist);

    @FormUrlEncoded
    @POST("/APIAppMusic/server/danhsachbaihat.php")
    Call<List<BaiHat>> getListSongByTheLoai(@Field("idTheLoai") String idTheLoai);

    @FormUrlEncoded
    @POST("/APIAppMusic/server/danhsachbaihat.php")
    Call<List<BaiHat>> getListSongByAlbum(@Field("idAlbum") String idAlbum);

    @GET("/APIAppMusic/server/playlist.php")
    Call<List<Playlist>> getListPlaylist();


    @GET("/APIAppMusic/server/chude.php")
    Call<List<ChuDe>> getListChuDe();

    @FormUrlEncoded
    @POST("/APIAppMusic/server/theloaitheochude.php")
    Call<List<TheLoai>> getListTheLoaiByChuDe(@Field("idChuDe") String idChuDe);

    @GET("/APIAppMusic/server/album.php")
    Call<List<Album>> getListAlbum();

    @FormUrlEncoded
    @POST("/APIAppMusic/server/updateluotthich.php")
    Call<String> updateCountLike(@Field("luotThich") String like, @Field("idBaiHat") String song);

    @FormUrlEncoded
    @POST("/APIAppMusic/server/timkiembaihat.php")
    Call<List<BaiHat>> getListSongBySearch(@Field("tuKhoa") String keyWord);

    @FormUrlEncoded
    @POST("/APIAppMusic/server/process_login_with_facebook.php")
    Call<String> loginByFacebook(@Field("user_IdFacebook") String user_IdFacebook
                                , @Field("user_name") String user_name
                                , @Field("user_url_image") String user_url_image);

    @FormUrlEncoded
    @POST("/APIAppMusic/server/process_login_with_google.php")
    Call<String> loginByGoogle(@Field("user_name") String user_name
            , @Field("user_email") String user_email
            , @Field("user_IdGoogle") String user_IdGoogle
            , @Field("user_url_image") String user_url_image);
}
