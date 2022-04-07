package com.btlandroid.music.fragment;

import android.content.Intent;
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
import com.btlandroid.music.activity.ListAlbumActivity;
import com.btlandroid.music.adapter.AlbumAdapter;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.Album;
import com.btlandroid.music.service.APIService;
import com.btlandroid.music.service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumHotFragment extends Fragment {
    private static final String TAG = AlbumHotFragment.class.getName();
    View view;
    RecyclerView rvAlbum;
    TextView tvSeeMore;
    AlbumAdapter albumAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album_hot, container, false);

        rvAlbum = view.findViewById(R.id.rvAlbum);
        tvSeeMore = view.findViewById(R.id.tvSeeMore);

        tvSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListAlbumActivity.class);
                startActivity(intent);
            }
        });

        getData();
        return view;
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Album>> call = dataService.getListAlbumHot();
        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                ArrayList<Album> listAlbum = (ArrayList<Album>) response.body();
                Log.d(TAG, Config.domain + listAlbum.get(0).getImageAlbum());
                albumAdapter = new AlbumAdapter(getContext(), listAlbum);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rvAlbum.setLayoutManager(linearLayoutManager);
                rvAlbum.setAdapter(albumAdapter);

            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }
}
