package com.btlandroid.music.activity;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.MVAllAdapter;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.Mv;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayMVActivity extends AppCompatActivity {

    private static final String TAG = PlayMVActivity.class.getName();
    TextView tvTenMV, tvTenCasi, tvTotalTime,tvCurrentPlayTime;
    VideoView videoView;
    ImageButton imbPlay, imbArrowBack,imbEnlarge, imbPrev, imbNext;
    RelativeLayout progressBar, rlAction;
    SeekBar sbMV;
    Uri uri;
    RecyclerView recyclerView;

    MVAllAdapter mvAllAdapter;

    Boolean display = false;

    Mv mv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_xem_mv);
        dataIntent();
        mapping();
        getData();

        Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.video_in);
        Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.video_out);

        uri = Uri.parse(Config.domain + mv.getLink());
        videoView.setVideoURI(uri);
        progressBar.setVisibility(VISIBLE);
        tvTenMV.setText(mv.getTen());
        tvTenCasi.setText(mv.getTencasi());
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                imbPlay.setImageResource(R.drawable.pause);
                setTime();
                videoView.start();
            }
        });

        CountDownTimer countDownTimer = new CountDownTimer(7000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                display = false;
                rlAction.setAnimation(animationOut);
                rlAction.setVisibility(View.GONE);
            }
        };

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(display){
                    countDownTimer.cancel();
                    display = false;
                    rlAction.setAnimation(animationOut);
                    rlAction.setVisibility(View.GONE);
                }
                else {
                    display = true;
                    rlAction.setAnimation(animationIn);
                    rlAction.setVisibility(VISIBLE);
                    countDownTimer.start();
                }
                return false;
            }
        });

        imbPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying()){
                    imbPlay.setImageResource(R.drawable.play);
                    videoView.pause();
                }else {
                    imbPlay.setImageResource(R.drawable.pause);
                    videoView.start();
                }
            }
        });

        imbArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sbMV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(sbMV.getProgress());
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imbPlay.setImageResource(R.drawable.refresh36);
                display = true;
                rlAction.setAnimation(animationIn);
                rlAction.setVisibility(VISIBLE);
            }
        });

        imbNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getCurrentPosition() + 5000);
            }
        });

        imbPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getCurrentPosition() - 5000);
            }
        });


    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Mv>> callback = dataService.getMVHot();
        callback.enqueue(new Callback<List<Mv>>() {
            @Override
            public void onResponse(Call<List<Mv>> call, Response<List<Mv>> response) {
                ArrayList<Mv> mvArrayList = (ArrayList<Mv>) response.body();
                mvAllAdapter = new MVAllAdapter(PlayMVActivity.this, mvArrayList);
                LinearLayoutManager linearLayout = new LinearLayoutManager (PlayMVActivity.this);
                linearLayout.setOrientation(linearLayout.VERTICAL);
                recyclerView.setLayoutManager(linearLayout);
                recyclerView.setAdapter(mvAllAdapter);
            }

            @Override
            public void onFailure(Call<List<Mv>> call, Throwable t) {

            }
        });
    }


    private void setTime() {
        SimpleDateFormat simp = new SimpleDateFormat("mm:ss");
        tvTotalTime.setText(simp.format(videoView.getDuration()));
        sbMV.setMax(videoView.getDuration());

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                tvCurrentPlayTime.setText(simp.format(videoView.getCurrentPosition()));
                sbMV.setProgress(videoView.getCurrentPosition());
                handler.postDelayed(this, 1000);
            }
        });
    }


    private void mapping() {
        videoView = findViewById(R.id.video);
        tvTenMV = findViewById(R.id.tvTenMV);
        tvTenCasi = findViewById(R.id.tvTenCaSi);
        progressBar = findViewById(R.id.progressBar);
        rlAction = findViewById(R.id.rlAction);
        imbPlay = findViewById(R.id.imbPlay);
        imbArrowBack = findViewById(R.id.imbArrowBack);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        tvCurrentPlayTime = findViewById(R.id.tvCurrentPlayTime);
        sbMV = findViewById(R.id.sbMV);
        imbEnlarge = findViewById(R.id.imbEnlarge);
        imbPrev = findViewById(R.id.imbPrev);
        imbNext = findViewById(R.id.imbNext);
        recyclerView = findViewById(R.id.lvMV);
    }

    private void dataIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mv = (Mv) intent.getSerializableExtra("MV");
            Toast.makeText(this, mv.getTen(), Toast.LENGTH_SHORT).show();
        }
    }
}