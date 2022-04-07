package com.btlandroid.music.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.MyViewPagerAdapter;
import com.btlandroid.music.fragment.HomeFragment;
import com.btlandroid.music.fragment.SearchFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapping();
        initView();
    }

    private void initView() {
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
//        myViewPagerAdapter.addFragment(new HomeFragment());
//        myViewPagerAdapter.addFragment(new SearchFragment());
        viewPager2.setAdapter(myViewPagerAdapter);


        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("Trang chủ");
                                tab.setIcon(R.drawable.ic_home);
                                break;
                            case 1:
                                tab.setText("Tìm kiếm");
                                tab.setIcon(R.drawable.ic_search);
                                break;
                        }
                    }
                }).attach();

        viewPager2.setUserInputEnabled(false);
    }

    private void mapping() {
        tabLayout = findViewById(R.id.actMainTabLayout);
        viewPager2 = findViewById(R.id.actMainViewPager);
    }
}