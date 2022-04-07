package com.btlandroid.music.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerPlaySong extends FragmentStateAdapter {

    ArrayList<Fragment> listFragment = new ArrayList<>();

    public ViewPagerPlaySong(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getItemCount() {
        return listFragment.size();
    }

    public void addFragment(Fragment fragment) {
        listFragment.add(fragment);
    }

    public Fragment getItem(int index) {
        return listFragment.get(index);
    }

}
