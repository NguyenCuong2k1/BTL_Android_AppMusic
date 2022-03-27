package com.btlandroid.music.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.PlaylistAdapter;
import com.btlandroid.music.model.Playlist;
import com.btlandroid.music.service.APIRetrofitClient;
import com.btlandroid.music.service.APIService;
import com.btlandroid.music.service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistFragment extends Fragment {

    private static final String TAG = PlaylistFragment.class.getName();
    View view;
    ListView lvPlaylist;
    TextView tvTitle, tvSeeMore;
    ArrayList<Playlist> playlists;
    PlaylistAdapter playlistAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);

        lvPlaylist = view.findViewById(R.id.lvPlaylist);
        setListViewHeightBasedOnChildren(lvPlaylist);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvSeeMore = view.findViewById(R.id.tvViewMorePlaylist);
        getData();
        return view;
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.getPlaylistCurrentDay();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                playlists = (ArrayList<Playlist>) response.body();
                Log.d(TAG, playlists.toString());

                playlistAdapter = new PlaylistAdapter(getActivity(), android.R.layout.simple_list_item_1, playlists);
                lvPlaylist.setAdapter(playlistAdapter);
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
