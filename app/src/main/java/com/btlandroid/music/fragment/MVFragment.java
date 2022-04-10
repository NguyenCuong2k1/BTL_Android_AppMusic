package com.btlandroid.music.fragment;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.activity.MVActivity;
import com.btlandroid.music.adapter.MVAdapter;
import com.btlandroid.music.model.Mv;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MVFragment extends Fragment {
    View view;
    RecyclerView recyclerViewMV;
    TextView txtXemThemMV;
    MVAdapter mvAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mv, container, false);

        recyclerViewMV = view.findViewById(R.id.relativeMV);
        txtXemThemMV = view.findViewById(R.id.tvSeeMoreMV);

        GetData();
        txtXemThemMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MVActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<Mv>> callback = dataService.getMVHot();
        callback.enqueue(new Callback<List<Mv>>() {
            @Override
            public void onResponse(Call<List<Mv>> call, Response<List<Mv>> response) {
                ArrayList<Mv> mvArrayList = (ArrayList<Mv>) response.body();
                mvAdapter = new MVAdapter(getActivity(), mvArrayList);
                LinearLayoutManager linearLayout = new LinearLayoutManager (getActivity());
                linearLayout.setOrientation(linearLayout.HORIZONTAL);
                recyclerViewMV.setLayoutManager(linearLayout);
                recyclerViewMV.setAdapter(mvAdapter);
            }

            @Override
            public void onFailure(Call<List<Mv>> call, Throwable t) {

            }
        });
    }
}
