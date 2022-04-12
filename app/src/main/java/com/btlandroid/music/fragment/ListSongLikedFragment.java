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
import com.btlandroid.music.adapter.ListSongLikedAdapter;
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

public class ListSongLikedFragment extends Fragment {
    private static final int TYPE_LOGIN_FACEBOOK = 0;
    private static final int TYPE_LOGIN_GOOGLE = 1;
    private static final String TAG = ListSongLikedFragment.class.getName();

    View view;
    RecyclerView rvListSong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_song_liked_fragment, container, false);
        rvListSong = view.findViewById(R.id.rvListSong);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("DataUser", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("User", "");
        User user = gson.fromJson(json, User.class);

        if (user != null) {
            DataService dataService = APIService.getService();
            Call<Integer> call;
            if (user.getUser_IdFacebook() != null && !user.getUser_IdFacebook().equals("")) {
                call = dataService.getUserId(user.getUser_IdFacebook(), TYPE_LOGIN_FACEBOOK);
            } else {
                call = dataService.getUserId(user.getUser_IdGoogle(), TYPE_LOGIN_GOOGLE);
            }
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    Integer integer = response.body();
                    Log.d(TAG, integer.toString());

                    getListSongLiked(integer);
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });

        }


    }

    private void getListSongLiked(Integer user_id) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> call = dataService.getListSongLiked(user_id);
        call.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                ArrayList<BaiHat> listSongLiked = (ArrayList<BaiHat>) response.body();
                Log.d(TAG, listSongLiked.toString());

                ListSongLikedAdapter listSongLikedAdapter = new ListSongLikedAdapter(getContext(), listSongLiked);
                rvListSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                rvListSong.setAdapter(listSongLikedAdapter);

            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

}
