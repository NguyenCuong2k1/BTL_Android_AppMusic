package com.btlandroid.music.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.MVAllAdapter;
import com.btlandroid.music.model.Mv;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MVFragment_all extends Fragment {
    View view;
    RecyclerView recyclerViewMV;
    MVAllAdapter mvAllAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mv_all, container, false);

        recyclerViewMV = view.findViewById(R.id.rvAllMV);
        GetData();

        return view;
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<Mv>> callback = dataService.getAllMVHot();
        callback.enqueue(new Callback<List<Mv>>() {
            @Override
            public void onResponse(Call<List<Mv>> call, Response<List<Mv>> response) {
                ArrayList<Mv> mvArrayList = (ArrayList<Mv>) response.body();
                mvAllAdapter = new MVAllAdapter(getActivity(), mvArrayList);
                LinearLayoutManager linearLayout = new LinearLayoutManager (getActivity());
                linearLayout.setOrientation(linearLayout.VERTICAL);
                recyclerViewMV.setLayoutManager(linearLayout);
                recyclerViewMV.setAdapter(mvAllAdapter);
            }

            @Override
            public void onFailure(Call<List<Mv>> call, Throwable t) {

            }
        });
    }
}
