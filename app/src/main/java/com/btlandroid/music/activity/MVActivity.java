package com.btlandroid.music.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.MyViewPagerAdapter;
import com.btlandroid.music.adapter.MyViewPagerMVAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MVActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerMVAdapter myViewPagerMVAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvactivity);

        mapping();
        initView();


    }


    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("MV");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myViewPagerMVAdapter = new MyViewPagerMVAdapter(getSupportFragmentManager(), getLifecycle());
//        myViewPagerAdapter.addFragment(new HomeFragment());
//        myViewPagerAdapter.addFragment(new SearchFragment());
        viewPager2.setAdapter(myViewPagerMVAdapter);


        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("Tất Cả");
                                break;
                            case 1:
                                tab.setText("Việt Nam");
                                break;
                            case 2:
                                tab.setText("US-UK");
                                break;
                            case 3:
                                tab.setText("Châu Á");
                                break;
                            case 4:
                                tab.setText("Hòa Tấu");
                                break;
                        }
                    }
                }).attach();

        viewPager2.setUserInputEnabled(false);
    }

    private void mapping() {
        tabLayout = findViewById(R.id.mvTabLayout);
        viewPager2 = findViewById(R.id.actMVViewPager);
        toolbar = findViewById(R.id.toolbar);
    }

}