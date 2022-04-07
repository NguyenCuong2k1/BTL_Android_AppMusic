package com.btlandroid.music.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.MyViewPagerAdapter;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.BaiHat;
import com.btlandroid.music.service.PlaySongService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;
    ImageView imvPlayPause, imvNext, imvPrev, imvClose, imvSong;
    TextView tvNameSong, tvNameSinger;
    ConstraintLayout clPlaySongCollapse;
    BaiHat song;
    boolean isPlaying = false;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleLayoutMusic(intent);
        }
    };
    private PlaySongService playSongService;
    private boolean isServiceConnected;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlaySongService.MyBinder myBinder = (PlaySongService.MyBinder) service;
            playSongService = myBinder.getPlaySongService();
            isServiceConnected = true;

            if (playSongService.mediaPlayer != null && playSongService.listSong != null) {
                clPlaySongCollapse.setVisibility(View.VISIBLE);
                BaiHat song = playSongService.listSong.get(playSongService.position);
                Picasso.get().load(Config.domain + song.getImageBaiHat()).into(imvSong);
                tvNameSinger.setText(song.getSinger());
                tvNameSong.setText(song.getTenBaiHat());
                if(playSongService.mediaPlayer.isPlaying()) {
                    imvPlayPause.setImageResource(R.drawable.ic_pause);
                } else {
                    imvPlayPause.setImageResource(R.drawable.ic_play);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mapping();
        initView();
        addEvent();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("data_to_activity"));

    }

    private void addEvent() {
        imvPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceConnected) {
                    if (playSongService.mediaPlayer != null && playSongService.mediaPlayer.isPlaying()) {
                        playSongService.pauseMusic();
                    } else if(playSongService.mediaPlayer != null){
                        playSongService.resumeMusic();
                    }
                }
            }
        });

        imvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceConnected) {
                    playSongService.nextMusic();
                }
            }
        });

        imvPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceConnected) {
                    playSongService.prevMusic();
                }
            }
        });

        imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServiceConnected) {
                    playSongService.stopSelf();
                    playSongService.sendDataToActivity(playSongService.ACTION_CANCEL);

                }
            }
        });
    }

    private void initView() {
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
//        myViewPagerAdapter.addFragment(new HomeFragment());
//        myViewPagerAdapter.addFragment(new SearchFragment());
        viewPager2.setAdapter(myViewPagerAdapter);


        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
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

        imvClose = findViewById(R.id.imvCloseActMain);
        imvNext = findViewById(R.id.imvNextActMain);
        imvPlayPause = findViewById(R.id.imvPlayPauseActMain);
        imvPrev = findViewById(R.id.imvPrevActMain);
        imvSong = findViewById(R.id.imvSongActMain);

        tvNameSong = findViewById(R.id.tvNameSongActMain);
        tvNameSinger = findViewById(R.id.tvNameSingerActMain);
        clPlaySongCollapse = findViewById(R.id.clPlaySongCollapse);
    }

    @Override
    protected void onStart() {

//        getDataSongPlaying();

        Intent intentService = new Intent(MainActivity.this, PlaySongService.class);
        bindService(intentService, serviceConnection, BIND_AUTO_CREATE);

//        if (isServiceConnected) {
//            if (playSongService.mediaPlayer != null && playSongService.listSong != null) {
//                clPlaySongCollapse.setVisibility(View.VISIBLE);
//                BaiHat song = playSongService.listSong.get(playSongService.position);
//                Picasso.get().load(Config.domain + song.getImageBaiHat()).into(imvSong);
//                tvNameSinger.setText(song.getSinger());
//                tvNameSong.setText(song.getTenBaiHat());
//            }
//        }

        super.onStart();
    }

    private void getDataSongPlaying() {
//        Gson gson = new Gson();
//        SharedPreferences sharedPreferences = getSharedPreferences("DataSongPlaying", MODE_PRIVATE);
//        String json = sharedPreferences.getString("ListSong", null);
//        int position = sharedPreferences.getInt("position", -1);
//
//        if(json != null) {
//            Type type = new TypeToken<ArrayList<BaiHat>>() {
//            }.getType();
//            ArrayList<BaiHat> listSong = gson.fromJson(json, type);
//
//            if(position != -1 && listSong != null && listSong.size() > 0) {
//                BaiHat song = listSong.get(position);
//                Picasso.get().load(Config.domain + song.getImageBaiHat()).into(imvSong);
//                tvNameSinger.setText(song.getSinger());
//                tvNameSong.setText(song.getTenBaiHat());

        clPlaySongCollapse.setVisibility(View.VISIBLE);

//
//            }
//        }
    }

    private void handleLayoutMusic(Intent intent) {

        Bundle bundle = intent.getExtras();
        int action = bundle.getInt("action", 0);
        switch (action) {
            case PlaySongService.ACTION_START:
                setDataLayoutByActionStart(bundle);
                setStatusPlayOrPause(bundle);
                break;
            case PlaySongService.ACTION_RESUME:
                setStatusPlayOrPause(bundle);
                break;
            case PlaySongService.ACTION_PAUSE:
                setStatusPlayOrPause(bundle);
                break;
            case PlaySongService.ACTION_NEXT:
                setDataLayoutByActionStart(bundle);
                break;
            case PlaySongService.ACTION_PREV:
                setDataLayoutByActionStart(bundle);
                break;
            case PlaySongService.ACTION_CANCEL:
                clPlaySongCollapse.setVisibility(View.GONE);
                if (isServiceConnected) {
                    unbindService(serviceConnection);
                    isServiceConnected = false;
                }
                break;
            default:
                break;
        }
    }

    private void setStatusPlayOrPause(Bundle bundle) {
        isPlaying = bundle.getBoolean("status", false);
        if (isPlaying) {
            imvPlayPause.setImageResource(R.drawable.ic_pause);
        } else {
            imvPlayPause.setImageResource(R.drawable.ic_play);
        }
    }

    private void setDataLayoutByActionStart(Bundle bundle) {
        song = (BaiHat) bundle.getParcelable("song");
        isPlaying = bundle.getBoolean("status", false);

        if (song != null) {
            clPlaySongCollapse.setVisibility(View.VISIBLE);
//            getSupportActionBar().setTitle(song.getTenBaiHat());
            imvPlayPause.setImageResource(R.drawable.ic_pause);
            Picasso.get().load(Config.domain + song.getImageBaiHat()).into(imvSong);
            tvNameSinger.setText(song.getSinger());
            tvNameSong.setText(song.getTenBaiHat());

        } else {
            clPlaySongCollapse.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        SharedPreferences sharedPreferences = getSharedPreferences("DataSongPlaying", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear().commit();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        if (isServiceConnected) {
            unbindService(serviceConnection);
            isServiceConnected = false;
        }

        super.onDestroy();
    }
}