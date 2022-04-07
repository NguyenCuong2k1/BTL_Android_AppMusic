package com.btlandroid.music.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.TheLoaiTheoChuDeAdapter;
import com.btlandroid.music.model.ChuDe;
import com.btlandroid.music.model.TheLoai;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TheLoaiTheoChuDeActivity extends AppCompatActivity {

    private static final String TAG = TheLoaiTheoChuDeActivity.class.getName();
    ChuDe chuDe;
    RecyclerView rvTheLoai;
    Toolbar tbarTheLoai;

    TheLoaiTheoChuDeAdapter theLoaiTheoChuDeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_loai_theo_chu_de);

        mapping();
        getData();
        initView();
    }

    private void initView() {

        setSupportActionBar(tbarTheLoai);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(chuDe.getTenChuDe());
        tbarTheLoai.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getData() {
        Intent intent = getIntent();
        if(intent.hasExtra("ChuDe")) {
            chuDe = (ChuDe) intent.getSerializableExtra("ChuDe");
            Log.d(TAG, chuDe.toString());
        }

        DataService dataService = APIService.getService();
        Call<List<TheLoai>> callback = dataService.getListTheLoaiByChuDe(chuDe.getIdChuDe());
        callback.enqueue(new Callback<List<TheLoai>>() {
            @Override
            public void onResponse(Call<List<TheLoai>> call, Response<List<TheLoai>> response) {
                ArrayList<TheLoai> listTheLoai = (ArrayList<TheLoai>) response.body();
                Log.d(TAG, listTheLoai.toString());

                theLoaiTheoChuDeAdapter = new TheLoaiTheoChuDeAdapter(TheLoaiTheoChuDeActivity.this, listTheLoai);
                rvTheLoai.setLayoutManager(new GridLayoutManager(TheLoaiTheoChuDeActivity.this, 2));
                rvTheLoai.setAdapter(theLoaiTheoChuDeAdapter);
            }

            @Override
            public void onFailure(Call<List<TheLoai>> call, Throwable t) {

            }
        });
    }

    private void mapping() {
        rvTheLoai = findViewById(R.id.rvTheLoai);
        tbarTheLoai = findViewById(R.id.tbarTheLoaiTheoChuDe);
    }
}