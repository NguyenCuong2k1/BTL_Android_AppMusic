package com.btlandroid.music.activity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.ViewPagerPlaySong;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.fragment.MusicDiscFrament;
import com.btlandroid.music.fragment.PlayListSongFragment;
import com.btlandroid.music.model.BaiHat;

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

    MediaPlayer mediaPlayer;

    int position = 0;
    boolean repeat = false;
    boolean shuffle = false;
    boolean next = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        mapping();
        getData();
        initView();
        addEvent();


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("isShuffle")) {
            boolean isShuffle = intent.getBooleanExtra("isShuffle", false);
            if(isShuffle) {
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewPagerPlaySong.getItem(1) != null) {
                    if (listSong.size() > 0) {
                        musicDiscFrament.playSong(listSong.get(0).getImageBaiHat());
                        handler.removeCallbacks(this);
                    } else {
                        handler.postDelayed(this, 300);
                    }
                }
            }
        }, 500);

        imvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listSong != null && listSong.size() > 0) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        imvPlay.setImageResource(R.drawable.ic_play);
                        musicDiscFrament.stopSong();
                    } else {
                        mediaPlayer.start();
                        imvPlay.setImageResource(R.drawable.ic_pause);
                        musicDiscFrament.resumeSong();

                    }
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    listSong.clear();
                }
                finish();

            }
        });

        imvRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        imvShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer != null) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });

        imvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listSong.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < listSong.size()) {
                        imvPlay.setImageResource(R.drawable.ic_pause);
                        position++;

                        if (repeat == true) {
                            if (position == 0) {
                                position = listSong.size();
                            }
                            position -= 1;
                        }

                        if (shuffle == true) {
                            Random random = new Random();
                            int index = random.nextInt(listSong.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > (listSong.size() - 1)) {
                            position = 0;
                        }
                        new PlayMp3().execute(listSong.get(position).getLinkBaiHat());
                        musicDiscFrament.playSong(listSong.get(position).getImageBaiHat());
                        getSupportActionBar().setTitle(listSong.get(position).getTenBaiHat());
//                        updateTime();
                    }
                }

                imvPlay.setClickable(false);
                imvPrev.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imvPlay.setClickable(true);
                        imvPrev.setClickable(true);
                    }
                }, 3000);
            }

        });

        imvPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listSong.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < listSong.size()) {
                        imvPlay.setImageResource(R.drawable.ic_pause);
                        position--;

                        if (position < 0) {
                            position = listSong.size() - 1;
                        }

                        if (repeat == true) {
                            position += 1;
                        }

                        if (shuffle == true) {
                            Random random = new Random();
                            int index = random.nextInt(listSong.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > (listSong.size() - 1)) {
                            position = 0;
                        }
                        new PlayMp3().execute(listSong.get(position).getLinkBaiHat());
                        musicDiscFrament.playSong(listSong.get(position).getImageBaiHat());
                        getSupportActionBar().setTitle(listSong.get(position).getTenBaiHat());
//                        updateTime();
                    }
                }

                imvPlay.setClickable(false);
                imvPrev.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imvPlay.setClickable(true);
                        imvPrev.setClickable(true);
                    }
                }, 3000);
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

        if (listSong.size() > 0) {
            getSupportActionBar().setTitle(listSong.get(0).getTenBaiHat());
            new PlayMp3().execute(listSong.get(0).getLinkBaiHat());
            imvPlay.setImageResource(R.drawable.ic_pause);

        }

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

    private void timeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        tvTimeTotal.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        sbTime.setMax(mediaPlayer.getDuration());

    }

    private void updateTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    sbTime.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    tvTimeSong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 300);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            next = true;

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }, 300);

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (next == true) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < listSong.size()) {
                        imvPlay.setImageResource(R.drawable.ic_pause);
                        position++;

                        if (repeat == true) {
                            if (position == 0) {
                                position = listSong.size();
                            }
                            position -= 1;
                        }

                        if (shuffle == true) {
                            Random random = new Random();
                            int index = random.nextInt(listSong.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > (listSong.size() - 1)) {
                            position = 0;
                        }
                        new PlayMp3().execute(listSong.get(position).getLinkBaiHat());
                        musicDiscFrament.playSong(listSong.get(position).getImageBaiHat());
                        getSupportActionBar().setTitle(listSong.get(position).getTenBaiHat());
                    }

                    imvPlay.setClickable(false);
                    imvPrev.setClickable(false);
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imvPlay.setClickable(true);
                            imvPrev.setClickable(true);
                        }
                    }, 3000);

                    next = false;
                    handler1.removeCallbacks(this);
                } else {
                    handler1.postDelayed(this, 1000);
                }
            }
        }, 500);
    }

    class PlayMp3 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes
                            .Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            });
            try {
                mediaPlayer.setDataSource(Config.domain + s);
                mediaPlayer.prepare();
                mediaPlayer.start();
                timeSong();
                updateTime();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}