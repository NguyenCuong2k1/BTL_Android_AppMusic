package com.btlandroid.music.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.BannerAdapter;
import com.btlandroid.music.model.QuangCao;
import com.btlandroid.music.service.APIService;
import com.btlandroid.music.service.DataService;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannerFragment extends Fragment {
    private static final String TAG = BannerFragment.class.getName();
    View view;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    BannerAdapter bannerAdapter;

    Runnable runnable;
    Handler handler;
    int currentItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_banner, container, false);
//        Toast.makeText(getContext(), "Toi day", Toast.LENGTH_LONG).show();
        mapping();
        getData();
        return view;
    }

    private void mapping() {
        viewPager = view.findViewById(R.id.viewPager);
        circleIndicator = view.findViewById(R.id.indicatorDefault);
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<QuangCao>> callback = dataService.getDataBanner();
        callback.enqueue(new Callback<List<QuangCao>>() {
            @Override
            public void onResponse(Call<List<QuangCao>> call, Response<List<QuangCao>> response) {
                ArrayList<QuangCao> listBanner = (ArrayList<QuangCao>) response.body();
                Log.d(TAG, listBanner.toString());
                bannerAdapter = new BannerAdapter(listBanner, getActivity());
                viewPager.setAdapter(bannerAdapter);
                circleIndicator.setViewPager(viewPager);
//                Log.d(TAG, response.toString());
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        currentItem = viewPager.getCurrentItem();
                        currentItem++;
                        if(currentItem >= viewPager.getAdapter().getCount()) {
                            currentItem = 0;
                        }
                        viewPager.setCurrentItem(currentItem, true);
                        handler.postDelayed(runnable, 4500);
                    }
                };
                handler.postDelayed(runnable, 4500);
            }

            @Override
            public void onFailure(Call<List<QuangCao>> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }
}
