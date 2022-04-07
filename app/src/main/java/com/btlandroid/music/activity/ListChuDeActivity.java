package com.btlandroid.music.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.ListChuDeAdapter;
import com.btlandroid.music.model.ChuDe;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListChuDeActivity extends AppCompatActivity {
    private static final String TAG = ListChuDeActivity.class.getName();
    RecyclerView rvChuDe;
    Toolbar toolbarChuDe;
    ListChuDeAdapter listChuDeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chu_de);

        mapping();
        getData();
        initView();
        addEvent();
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<ChuDe>> callback = dataService.getListChuDe();
        callback.enqueue(new Callback<List<ChuDe>>() {
            @Override
            public void onResponse(Call<List<ChuDe>> call, Response<List<ChuDe>> response) {
                ArrayList<ChuDe> listChuDe = (ArrayList<ChuDe>) response.body();
                Log.d(TAG, listChuDe.toString());

                listChuDeAdapter = new ListChuDeAdapter(ListChuDeActivity.this, listChuDe);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListChuDeActivity.this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

                rvChuDe.setLayoutManager(linearLayoutManager);
                rvChuDe.setAdapter(listChuDeAdapter);

            }

            @Override
            public void onFailure(Call<List<ChuDe>> call, Throwable t) {

            }
        });
    }

    private void addEvent() {

    }

    private void initView() {
        setSupportActionBar(toolbarChuDe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tất cả chủ đề");
        toolbarChuDe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mapping() {
        rvChuDe = findViewById(R.id.rvChuDe);
        toolbarChuDe = findViewById(R.id.toolBarChuDe);
    }
}