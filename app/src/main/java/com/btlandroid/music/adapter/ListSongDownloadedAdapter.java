package com.btlandroid.music.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.model.AudioModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListSongDownloadedAdapter extends RecyclerView.Adapter<ListSongDownloadedAdapter.ViewHolder> {
    Context context;
    ArrayList<AudioModel> listAudio;
    TextView tenBạHat, tenCaSi, tvTotalTime, tvCurrentPlayTime;
    ImageView imExit, imPlay;
    SeekBar sbSong;

    MediaPlayer mediaPlayer;
    Dialog dialog;

    public ListSongDownloadedAdapter(Context context, ArrayList<AudioModel> listAudio) {
        this.context = context;
        this.listAudio = listAudio;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_song_downloaded, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(listAudio != null) {
            AudioModel audioModel = listAudio.get(position);
            holder.tvIndex.setText(String.valueOf(position + 1));
            holder.tvNameSong.setText(audioModel.getaName());
            holder.tvNameSinger.setText(audioModel.getaArtist());
        }
    }

    @Override
    public int getItemCount() {
        if(listAudio != null) {
            return listAudio.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvIndex, tvNameSong, tvNameSinger;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.tvIndex);
            tvNameSong = itemView.findViewById(R.id.tvNameSong);
            tvNameSinger = itemView.findViewById(R.id.tvNameSinger);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudioModel audioModel = listAudio.get(getPosition());
                    openDialog(Gravity.CENTER, audioModel);
                }
            });
        }
    }

    private void openDialog(int gravity, AudioModel audioModel) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_play_mp3_download);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(false);

        tenBạHat = dialog.findViewById(R.id.tenBH);
        tenCaSi = dialog.findViewById(R.id.tenCaSi);
        imExit = (ImageView) dialog.findViewById(R.id.imbExit);
        imPlay = (ImageView) dialog.findViewById(R.id.isplay);
        tvTotalTime = dialog.findViewById(R.id.tvTotalTime);
        tvCurrentPlayTime = dialog.findViewById(R.id.tvCurrentPlayTime);
        sbSong = dialog.findViewById(R.id.sbSong);

        tenBạHat.setText(audioModel.getaName());
        tenCaSi.setText(audioModel.getaArtist());
        String uri = audioModel.getaPath();

        playMP3(uri);
        process();
        dialog.show();
    }

    private void playMP3(String uri) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(uri);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void process() {
        imPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imPlay.setImageResource(R.drawable.play);
                }else {
                    mediaPlayer.start();
                    imPlay.setImageResource(R.drawable.pause);
                }
            }
        });

        imExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mediaPlayer.stop();
            }
        });

        timeSong();

        sbSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(sbSong.getProgress());
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imPlay.setImageResource(R.drawable.refresh36);
            }
        });
    }


    private void timeSong() {
        SimpleDateFormat simp = new SimpleDateFormat("mm:ss");
        tvTotalTime.setText(simp.format(mediaPlayer.getDuration()));
        sbSong.setMax(mediaPlayer.getDuration());

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                tvCurrentPlayTime.setText(simp.format(mediaPlayer.getCurrentPosition()));
                sbSong.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 1000);
            }
        });
    }

}
