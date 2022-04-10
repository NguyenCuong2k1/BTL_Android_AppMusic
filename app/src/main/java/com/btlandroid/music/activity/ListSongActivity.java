package com.btlandroid.music.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.ListSongAdapter;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.Album;
import com.btlandroid.music.model.BaiHat;
import com.btlandroid.music.model.Playlist;
import com.btlandroid.music.model.QuangCao;
import com.btlandroid.music.model.TheLoai;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;
import com.btlandroid.music.task.LoadImageFromInternet;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSongActivity extends AppCompatActivity implements ListSongAdapter.IDownload{
    QuangCao quangCao;
    ImageView imvListSong;
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView rvListSong;
    Button btnPlaySongShuffle;

    ArrayList<BaiHat> listSong;
    private String TAG = ListSongActivity.class.getName();
    ListSongAdapter listSongAdapter;

    Playlist playlist;
    TheLoai theLoai;
    Album album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);

        mapping();
        getData();

        addEvent();

        if(quangCao != null && !quangCao.getImageBaiHat().equals("")) {
            setValueInView(quangCao.getNameBaiHat(), quangCao.getImageBaiHat());
            getDataBanner(quangCao.getIdQuangCao());
        }

        if(playlist != null && !playlist.getTen().equals("")) {
            setValueInView(playlist.getTen(), playlist.getHinh());
            getDataPlaylist(playlist.getIdPlaylist());
        }

        if(theLoai != null && !theLoai.getTenTheLoai().equals("")) {
            setValueInView(theLoai.getTenTheLoai(), theLoai.getHinhTheLoai());
            getDataTheLoai(theLoai.getIdTheLoai());
        }
        if(album != null && !album.getNameAlbum().equals("")) {
            setValueInView(album.getNameAlbum(), album.getImageAlbum());
            getDataAlbum(album.getId());
        }

        eventClickFloatButton();

    }

    private void getDataAlbum(String idAlbum) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.getListSongByAlbum(idAlbum);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                listSong = (ArrayList<BaiHat>) response.body();
                Log.d(TAG, listSong.toString());
                listSongAdapter = new ListSongAdapter(ListSongActivity.this, listSong);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListSongActivity.this);
                rvListSong.setLayoutManager(linearLayoutManager);
                rvListSong.setAdapter(listSongAdapter);
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void getDataTheLoai(String idTheLoai) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.getListSongByTheLoai(idTheLoai);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                listSong = (ArrayList<BaiHat>) response.body();
                Log.d(TAG, listSong.toString());
                listSongAdapter = new ListSongAdapter(ListSongActivity.this, listSong);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListSongActivity.this);
                rvListSong.setLayoutManager(linearLayoutManager);
                rvListSong.setAdapter(listSongAdapter);
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void getDataPlaylist(String idPlaylist) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.getListSongByPlaylist(idPlaylist);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                listSong = (ArrayList<BaiHat>) response.body();
                Log.d(TAG, listSong.toString());
                listSongAdapter = new ListSongAdapter(ListSongActivity.this, listSong);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListSongActivity.this);
                rvListSong.setLayoutManager(linearLayoutManager);
                rvListSong.setAdapter(listSongAdapter);
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void getDataBanner(String idBanner) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.getListSongByBanner(idBanner);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                listSong = (ArrayList<BaiHat>) response.body();
                Log.d(TAG, listSong.toString());
                listSongAdapter = new ListSongAdapter(ListSongActivity.this, listSong);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListSongActivity.this);
                rvListSong.setLayoutManager(linearLayoutManager);
                rvListSong.setAdapter(listSongAdapter);
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void setValueInView(String name, String urlImage) {
        collapsingToolbarLayout.setTitle(name);
//        try {
//            URL url = new URL(Config.domain + urlImage);
//            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
//            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
//                collapsingToolbarLayout.setBackground(bitmapDrawable);
//            }
            LoadImageFromInternet loadImageFromInternet = new LoadImageFromInternet(collapsingToolbarLayout, this);
            loadImageFromInternet.execute(Config.domain + urlImage);

//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Picasso.get().load(Config.domain + urlImage).into(imvListSong);


    }

    private void addEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void getData() {
        Intent intent = getIntent();
        if(intent != null) {
            if(intent.hasExtra("banner")) {
                quangCao = (QuangCao) intent.getSerializableExtra("banner");

            }
            if(intent.hasExtra("ItemPlaylist")) {
                playlist = (Playlist) intent.getSerializableExtra("ItemPlaylist");
            }
            if(intent.hasExtra("IdTheLoai")) {
                theLoai = (TheLoai) intent.getSerializableExtra("IdTheLoai");
            }
            if(intent.hasExtra("Album")) {
                album = (Album) intent.getSerializableExtra("Album");
            }
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        btnPlaySongShuffle.setEnabled(false);


    }

    private void mapping() {
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        rvListSong = findViewById(R.id.rvListSong);
        toolbar = findViewById(R.id.toolbarDanhSach);
        btnPlaySongShuffle = findViewById(R.id.btnPlaySongShuffle);
        imvListSong = findViewById(R.id.imvListSong);
    }

    private void eventClickFloatButton() {
        btnPlaySongShuffle.setEnabled(true);
        btnPlaySongShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListSongActivity.this, PlaySongActivity.class);
                intent.putExtra("ListSong", listSong);
                intent.putExtra("isShuffle", true);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onDownload(String url) {
        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(uri.getLastPathSegment());
        request.setDescription("Downloading...");
        request.setMimeType("audio/MP3)");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED | DownloadManager.Request.VISIBILITY_VISIBLE);
        if(isStoragePermissionGranted()) {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, uri.getLastPathSegment());
            Log.d(TAG, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_MUSIC + "/" + uri.getLastPathSegment());
//                request.setDestinationUri(Uri.parse("file://" + "music" + uri.getLastPathSegment()));
            downloadmanager.enqueue(request);
            Toast.makeText(this, "Downloading", Toast.LENGTH_LONG).show();
        }
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
//            onDownload();
        }
    }
}