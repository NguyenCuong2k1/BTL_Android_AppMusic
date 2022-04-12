package com.btlandroid.music.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.BaiHatHotAdapter;
import com.btlandroid.music.model.BaiHat;
import com.btlandroid.music.model.User;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiHatHotFragment extends Fragment {
    private static final String TAG = BaiHatHotFragment.class.getName();
    private static final int TYPE_LOGIN_FACEBOOK = 0;
    private static final int TYPE_LOGIN_GOOGLE = 1;
    View view;
    RecyclerView rvBaiHatHot;
    BaiHatHotAdapter baiHatHotAdapter;
    private ArrayList<BaiHat> listSongLiked;
    private ArrayList<BaiHat> listBaiHat;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_baihathot, container, false);

        rvBaiHatHot = view.findViewById(R.id.rvBaiHatHot);

        getData();
        return view;
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> call = dataService.getListBaiHatHot();
        call.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                listBaiHat = (ArrayList<BaiHat>) response.body();
                Log.d(TAG, listBaiHat.toString());

                baiHatHotAdapter = new BaiHatHotAdapter(getActivity(), listBaiHat);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                rvBaiHatHot.setLayoutManager(linearLayoutManager);
                rvBaiHatHot.setAdapter(baiHatHotAdapter);

            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }
}
