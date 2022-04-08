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
import android.view.MotionEvent;
import android.view.View;
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
import com.btlandroid.music.service.APIService;
import com.btlandroid.music.service.DataService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayMVActivity extends AppCompatActivity {

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

        imbEnlarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int orientation = getResources().getConfiguration().orientation;
                if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                    PlayMVActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    PlayMVActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
                    params.removeRule(RelativeLayout.ALIGN_PARENT_START);
                    params.removeRule(RelativeLayout.ALIGN_PARENT_END);
                    params.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                    params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    videoView.setLayoutParams(params);

                    float scale = PlayMVActivity.this.getResources().getDisplayMetrics().density;
                    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) rlAction.getLayoutParams();
                    params1.width = params.MATCH_PARENT;
                    params1.height = (int) (220 * scale);
                    rlAction.setLayoutParams(params);

                    RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
                    params2.width = params.MATCH_PARENT;
                    params2.height = (int) (220 * scale);
                    progressBar.setLayoutParams(params);
                    imbEnlarge.setImageResource(R.drawable.ic_baseline_crop_free_24);
                }else{
                    PlayMVActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
                    PlayMVActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    videoView.setLayoutParams(params);

                    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) rlAction.getLayoutParams();
                    params1.width = params.MATCH_PARENT;
                    params1.height = params.MATCH_PARENT;
                    rlAction.setLayoutParams(params);

                    RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
                    params2.width = params.MATCH_PARENT;
                    params2.height = params.MATCH_PARENT;
                    progressBar.setLayoutParams(params);
                    imbEnlarge.setImageResource(R.drawable.ic_baseline_fullscreen_exit_24);

                }

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