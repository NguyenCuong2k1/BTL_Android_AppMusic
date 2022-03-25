package com.btlandroid.music.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.btlandroid.music.fragment.HomeFragment;
import com.btlandroid.music.fragment.SearchFragment;

import java.util.ArrayList;

public class MyViewPagerAdapter extends FragmentStateAdapter {
//    private ArrayList<Fragment> arrayList = new ArrayList<>();

    public MyViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


//    public void addFragment(Fragment fragment) {
//        arrayList.add(fragment);
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new SearchFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
