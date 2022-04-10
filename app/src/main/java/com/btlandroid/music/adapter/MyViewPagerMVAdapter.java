package com.btlandroid.music.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.btlandroid.music.fragment.HomeFragment;
import com.btlandroid.music.fragment.MVFragment_all;
import com.btlandroid.music.fragment.MVFragment_asia;
import com.btlandroid.music.fragment.MVFragment_hoatau;
import com.btlandroid.music.fragment.MVFragment_usuk;
import com.btlandroid.music.fragment.MVFragment_vn;
import com.btlandroid.music.fragment.SearchFragment;

public class MyViewPagerMVAdapter extends FragmentStateAdapter {

    public MyViewPagerMVAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MVFragment_all();
            case 1:
                return new MVFragment_vn();
            case 2:
                return new MVFragment_usuk();
            case 3:
                return new MVFragment_asia();
            case 4:
                return new MVFragment_hoatau();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
