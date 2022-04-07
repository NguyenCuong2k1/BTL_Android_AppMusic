package com.btlandroid.music.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.ListPlaylistAdapter;
import com.btlandroid.music.model.Playlist;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvPlaylist;
    private String TAG = PlaylistActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        mapping();

        initView();
        getData();
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.getListPlaylist();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                ArrayList<Playlist> listPlaylist = (ArrayList<Playlist>) response.body();
                Log.d(TAG, listPlaylist.toString());
                ListPlaylistAdapter playlistAdapter = new ListPlaylistAdapter(PlaylistActivity.this, listPlaylist);
                rvPlaylist.setLayoutManager(new GridLayoutManager(PlaylistActivity.this, 2));
                rvPlaylist.setAdapter(playlistAdapter);
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Play list");
        toolbar.setTitleTextColor(getResources().getColor(R.color.purple_500));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarPlaylist);
        rvPlaylist = findViewById(R.id.rvListPlayList);

    }
}