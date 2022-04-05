package com.btlandroid.music.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.activity.PlaySongActivity;
import com.btlandroid.music.adapter.PlaySongAdapter;

public class PlayListSongFragment extends Fragment {

    View view;
    RecyclerView rvPlaySong;
    PlaySongAdapter playSongAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play_list_song, container, false);
        rvPlaySong = view.findViewById(R.id.rvPlaySong);

        if(PlaySongActivity.listSong.size() > 0) {
            playSongAdapter = new PlaySongAdapter(getContext(), PlaySongActivity.listSong);
            rvPlaySong.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            rvPlaySong.setAdapter(playSongAdapter);

        }

        return view;
    }
}
