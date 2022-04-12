package com.btlandroid.music.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.SearchSongAdapter;
import com.btlandroid.music.model.BaiHat;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {


    private static final String TAG = SearchFragment.class.getName();
    View view;
    Toolbar tbarSearch;
    RecyclerView rvSearch;
    TextView tvAlert;

    SearchSongAdapter searchSongAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        tbarSearch = view.findViewById(R.id.tbarSearch);
        tvAlert = view.findViewById(R.id.tvAlert);
        rvSearch = view.findViewById(R.id.rvSearch);

        ((AppCompatActivity)getContext()).setSupportActionBar(tbarSearch);
        tbarSearch.setTitle("");

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_view, menu);
        MenuItem menuItem = menu.findItem(R.id.itemSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, query);
                searchSong(query);
                searchView.clearFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void searchSong(String key) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> calback = dataService.getListSongBySearch(key);
        calback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                ArrayList<BaiHat> listSong = (ArrayList<BaiHat>) response.body();
                if(listSong.size() > 0) {
                    searchSongAdapter = new SearchSongAdapter(getActivity(), listSong);
                    rvSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rvSearch.setAdapter(searchSongAdapter);

                    tvAlert.setVisibility(View.GONE);
                    rvSearch.setVisibility(View.VISIBLE);
                } else {
                    tvAlert.setVisibility(View.VISIBLE);
                    rvSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });

    }
}