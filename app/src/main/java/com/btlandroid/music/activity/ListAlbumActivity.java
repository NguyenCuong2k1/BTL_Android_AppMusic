package com.btlandroid.music.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.AllAlbumAdapter;
import com.btlandroid.music.model.Album;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListAlbumActivity extends AppCompatActivity {
    private static final String TAG = ListAlbumActivity.class.getName();
    Toolbar tbarAlbum;
    RecyclerView rvAlbum;

    AllAlbumAdapter allAlbumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_album);

        mapping();
        getData();
        initView();
        addEvent();
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Album>> callback = dataService.getListAlbum();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                ArrayList<Album> listAlbum = (ArrayList<Album>) response.body();
                Log.d(TAG, listAlbum.toString());
                allAlbumAdapter = new AllAlbumAdapter(ListAlbumActivity.this, listAlbum);
                rvAlbum.setLayoutManager(new GridLayoutManager(ListAlbumActivity.this, 2));
                rvAlbum.setAdapter(allAlbumAdapter);
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }

    private void initView() {
        setSupportActionBar(tbarAlbum);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Album");

        tbarAlbum.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addEvent() {

    }

    private void mapping() {
        tbarAlbum = findViewById(R.id.tbarAlbum);
        rvAlbum = findViewById(R.id.rvAlbum);
    }
}