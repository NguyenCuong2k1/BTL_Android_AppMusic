package com.btlandroid.music.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.ViewPagerPlaySong;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.fragment.MusicDiscFrament;
import com.btlandroid.music.fragment.PlayListSongFragment;
import com.btlandroid.music.model.BaiHat;
import com.btlandroid.music.service.PlaySongService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class PlaySongActivity extends AppCompatActivity {

    private static final String TAG = PlaySongActivity.class.getName();
    public static ViewPagerPlaySong viewPagerPlaySong;
    public static ArrayList<BaiHat> listSong = new ArrayList<>();
    TextView tvTimeSong, tvTimeTotal;
    SeekBar sbTime;
    ImageButton imvPlay, imvNext, imvPrev, imvShuffle, imvRepeat;
    Toolbar toolbar;
    ViewPager2 viewPager2;
    MusicDiscFrament musicDiscFrament;
    PlayListSongFragment playListSongFragment;
    boolean isPlaying = false;
    int sbTimeMax;
    BaiHat song;

    boolean repeat = false;
    boolean shuffle = false;
    boolean next = false;
    private PlaySongService playSongService;
    private boolean isServiceConnected;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlaySongService.MyBinder myBinder = (PlaySongService.MyBinder) service;
            playSongService = myBinder.getPlaySongService();
            isServiceConnected = true;

            if(PlaySongActivity.this.getIntent().getBooleanExtra("isFromNotification", false)) {
                playSongService.sendDataToActivity(PlaySongService.ACTION_START);
                if(playSongService.shuffle) {
                    imvShuffle.setImageResource(R.drawable.ic_shuffle_on);
                    shuffle = true;
                } else {
                    imvShuffle.setImageResource(R.drawable.ic_shuffle);
                    shuffle = false;
                }

                if(playSongService.repeat) {
                    imvRepeat.setImageResource(R.drawable.ic_repeat_on);
                    repeat = true;
                } else {
                    imvRepeat.setImageResource(R.drawable.ic_repeat);
                    repeat=  false;
                }
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceConnected = false;
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleLayoutMusic(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("data_to_activity"));

        mapping();
        getData();
        initView();
        addEvent();


        Intent intent = getIntent();
        boolean isShuffle = false;
        if (intent != null && intent.hasExtra("isShuffle")) {
            isShuffle = intent.getBooleanExtra("isShuffle", false);
            if (isShuffle) {
                if (!shuffle) {
                    if (repeat == true) {
                        repeat = false;
                        imvRepeat.setImageResource(R.drawable.ic_repeat);
                    }
                    imvShuffle.setImageResource(R.drawable.ic_shuffle_on);
                    shuffle = true;
                } else {
                    imvShuffle.setImageResource(R.drawable.ic_shuffle);
                    shuffle = false;
                }
            }

        }

        Intent intentService = new Intent(PlaySongActivity.this, PlaySongService.class);
        intentService.putExtra("ListSong", listSong);
        intentService.putExtra("isShuffle", isShuffle);
        if(!getIntent().getBooleanExtra("isFromNotification", false)) {
            startService(intentService);

        }
        bindService(intentService, serviceConnection, BIND_AUTO_CREATE);

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
//                layout.setVisibility(View.GONE);
                finish();
                break;
            case PlaySongService.ACTION_UPDATE_TIME:
                updateTimeUI(bundle);
                break;
            default:
                break;
        }
    }

    private void updateTimeUI(Bundle bundle) {
        int sbTimeProgress = bundle.getInt("sbTimeProgress");
        String timeCurrent = bundle.getString("timeCurrent");

        tvTimeSong.setText(timeCurrent);
        sbTime.setProgress(sbTimeProgress);
    }

    private void setDataLayoutByActionStart(Bundle bundle) {
        song = (BaiHat) bundle.getParcelable("song");
        isPlaying = bundle.getBoolean("status", false);
        sbTimeMax = bundle.getInt("sbTimeMax");
        String timeTotal = bundle.getString("timeTotal", "00:00");

        if (song != null) {
            getSupportActionBar().setTitle(song.getTenBaiHat());
            if(isPlaying) {
                imvPlay.setImageResource(R.drawable.ic_pause);
            } else {
                imvPlay.setImageResource(R.drawable.ic_play);
            }

            musicDiscFrament.playSong(song.getImageBaiHat());
            sbTime.setMax(sbTimeMax);
            tvTimeTotal.setText(timeTotal);
        }

    }

    private void setStatusPlayOrPause(Bundle bundle) {
        isPlaying = bundle.getBoolean("status", false);
        if (isPlaying) {
            imvPlay.setImageResource(R.drawable.ic_pause);
            musicDiscFrament.resumeSong();
        } else {
            imvPlay.setImageResource(R.drawable.ic_play);
            musicDiscFrament.stopSong();
        }
    }


    private void getData() {
        Intent intent = getIntent();
        listSong.clear();
        if (intent.hasExtra("song")) {
            BaiHat baiHat = intent.getParcelableExtra("song");
            listSong.add(baiHat);

        }
        if (intent.hasExtra("ListSong")) {
            listSong = intent.getParcelableArrayListExtra("ListSong");
            Log.d(TAG, listSong.toString());
        }
    }

    private void addEvent() {
        imvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceConnected == true) {
                    if (isPlaying) {
                        playSongService.pauseMusic();
                    } else {
                        playSongService.resumeMusic();
                    }
                }

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listSong.clear();
                finish();
            }
        });
//
        imvRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSongService.onClickRepeat();
                if (!repeat) {
                    if (shuffle == true) {
                        shuffle = false;
                        imvShuffle.setImageResource(R.drawable.ic_shuffle);
                    }
                    imvRepeat.setImageResource(R.drawable.ic_repeat_on);
                    repeat = true;
                } else {
                    imvRepeat.setImageResource(R.drawable.ic_repeat);
                    repeat = false;
                }
            }
        });
//
        imvShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSongService.onClickShuffle();
                if (!shuffle) {
                    if (repeat == true) {
                        repeat = false;
                        imvRepeat.setImageResource(R.drawable.ic_repeat);
                    }
                    imvShuffle.setImageResource(R.drawable.ic_shuffle_on);
                    shuffle = true;
                } else {
                    imvShuffle.setImageResource(R.drawable.ic_shuffle);
                    shuffle = false;
                }
            }
        });
//
        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playSongService.onStopTrackingTouch(seekBar.getProgress());
            }
        });

        imvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceConnected == true) {
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
    }

    private void initView() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        musicDiscFrament = new MusicDiscFrament();
        playListSongFragment = new PlayListSongFragment();
        viewPagerPlaySong = new ViewPagerPlaySong(getSupportFragmentManager(), getLifecycle());
        viewPagerPlaySong.addFragment(musicDiscFrament);
        viewPagerPlaySong.addFragment(playListSongFragment);
        viewPager2.setAdapter(viewPagerPlaySong);
        viewPager2.setOffscreenPageLimit(2);

        musicDiscFrament = (MusicDiscFrament) viewPagerPlaySong.getItem(0);

    }

    private void mapping() {
        toolbar = findViewById(R.id.tbarPlaySong);
        tvTimeSong = findViewById(R.id.tvTimeSong);
        tvTimeTotal = findViewById(R.id.tvTimeTotal);
        imvNext = findViewById(R.id.imvbNext);
        imvPlay = findViewById(R.id.imvbPlay);
        imvPrev = findViewById(R.id.imvbPrev);
        imvShuffle = findViewById(R.id.imvbShuffle);
        imvRepeat = findViewById(R.id.imvbRepeat);
        sbTime = findViewById(R.id.sbSong);

        viewPager2 = findViewById(R.id.vpPlaySong);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        if (isServiceConnected) {
            unbindService(serviceConnection);
            isServiceConnected = false;
        }
    }

}