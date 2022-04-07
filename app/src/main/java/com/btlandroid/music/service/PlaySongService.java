package com.btlandroid.music.service;

import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.btlandroid.music.MyApplication;
import com.btlandroid.music.R;
import com.btlandroid.music.activity.PlaySongActivity;
import com.btlandroid.music.broadcast_receiver.SongReciver;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.BaiHat;
import com.btlandroid.music.task.LoadImageToNotification;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class PlaySongService extends Service {

    public static final int ACTION_RESUME = 1;
    public static final int ACTION_PAUSE = 2;
    public static final int ACTION_CANCEL = 3;
    public static final int ACTION_START = 4;
    public static final int ACTION_NEXT = 5;
    public static final int ACTION_PREV = 6;
    public static final int ACTION_UPDATE_TIME = 7;
    public static final int ACTION_SHUFFLE = 8;
    public static final int ACTION_REPEAT = 9;


    private static final String TAG = PlaySongService.class.getName();
    public ArrayList<BaiHat> listSong = new ArrayList<>();
    public MediaPlayer mediaPlayer;
    public int position = 0;
    public boolean repeat = false;
    public boolean shuffle = false;
    boolean next = false;
    MyBinder myBinder = new MyBinder();
    private boolean isPlaying;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent != null && intent.hasExtra("ListSong")) {
            listSong.clear();
            listSong = intent.getParcelableArrayListExtra("ListSong");
            Log.d(TAG, listSong.toString());

            if (intent.hasExtra("isShuffle")) {
                shuffle = intent.getBooleanExtra("isShuffle", false);
            }
            if (listSong != null && listSong.size() > 0) {
                position = 0;
                addEvent();

                startMusic(listSong.get(0));
//                sendNotification(listSong);
            }
        }

//        if (intent != null && intent.hasExtra("isShuffle")) {
//            boolean isShuffle = intent.getBooleanExtra("isShuffle", false);
//            if (isShuffle) {
//                if (!shuffle) {
//                    if (repeat == true) {
//                        repeat = false;
////                        imvRepeat.setImageResource(R.drawable.ic_repeat);
//                    }
////                    imvShuffle.setImageResource(R.drawable.ic_shuffle_on);
//                    shuffle = true;
//                } else {
////                    imvShuffle.setImageResource(R.drawable.ic_shuffle);
//                    shuffle = false;
//                }
//            }
//
//        }


        // idea: when user click view in notification, service will send broadcast, when reciver receive will start service with intent contain action
        // Handle action from notification by use broadcast reciver
        int action = intent.getIntExtra("action", 0);
        handleAction(action);

        return START_NOT_STICKY;
    }

    private void startMusic(BaiHat baiHat) {
        // Update Ui
//        getSupportActionBar().setTitle(listSong.get(0).getTenBaiHat());
        new PlayMp3().execute(listSong.get(0).getLinkBaiHat());


        // Update Ui
//        imvPlay.setImageResource(R.drawable.ic_pause);
    }

    private void handleAction(int action) {
        switch (action) {
            case ACTION_RESUME:
                resumeMusic();
                break;
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_NEXT:
                nextMusic();
                break;
            case ACTION_PREV:
                prevMusic();
                break;
            case ACTION_SHUFFLE:
                onClickShuffle();
                break;
            case ACTION_REPEAT:
                onClickRepeat();
                break;
            case ACTION_CANCEL:
                stopSelf();
                sendDataToActivity(ACTION_CANCEL);
                break;
            default:
                break;
        }
    }

    public void onClickRepeat() {
        if (!repeat) {
            if (shuffle == true) {
                shuffle = false;
//                imvShuffle.setImageResource(R.drawable.ic_shuffle);
            }
//            imvRepeat.setImageResource(R.drawable.ic_repeat_on);
            repeat = true;
        } else {
//            imvRepeat.setImageResource(R.drawable.ic_repeat);
            repeat = false;
        }
    }

    public void onClickShuffle() {
        if (!shuffle) {
            if (repeat == true) {
                repeat = false;
//                imvRepeat.setImageResource(R.drawable.ic_repeat);
            }
//            imvShuffle.setImageResource(R.drawable.ic_shuffle_on);
            shuffle = true;
        } else {
//            imvShuffle.setImageResource(R.drawable.ic_shuffle);
            shuffle = false;
        }
    }

    public void prevMusic() {
        if (listSong.size() > 0) {
            if (mediaPlayer != null || mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (position < listSong.size()) {
                // Update Ui
//                imvPlay.setImageResource(R.drawable.ic_pause);

                if (repeat) {
                    // not change
                } else if (shuffle) {
                    Random random = new Random();
                    int index = random.nextInt(listSong.size());
                    if (index == listSong.size()) {
                        index--;
                    }
                    if (index == position) {
                        position = index - 1;
                    } else {
                        position = index;
                    }
                    if (position > (listSong.size() - 1)) {
                        position = 0;
                    }
                } else {
                    position--;
                }
                if (position < 0) {
                    position = listSong.size() - 1;
                }

                new PlayMp3().execute(listSong.get(position).getLinkBaiHat());
                // Update UI
//                sendDataToActivity(ACTION_PREV);
//                musicDiscFrament.playSong(listSong.get(position).getImageBaiHat());
//                getSupportActionBar().setTitle(listSong.get(position).getTenBaiHat());
//                        updateTime();
            }
        }

        // Update UI
//        imvPlay.setClickable(false);
//        imvPrev.setClickable(false);
//        Handler handler1 = new Handler();
//        handler1.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Update UI
////                imvPlay.setClickable(true);
////                imvPrev.setClickable(true);
//            }
//        }, 3000);
    }

    public void nextMusic() {

        if (listSong != null && listSong.size() > 0) {
            // if MediaPlayer is playing
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (position < listSong.size()) {
                // update UI
//                imvPlay.setImageResource(R.drawable.ic_pause);


                if (repeat) {
                    //not change
                } else if (shuffle) {
                    Random random = new Random();
                    int index = random.nextInt(listSong.size());
                    if (index == listSong.size()) {
                        index = 0;
                    }
                    if (index == position) {
                        position = index - 1;
                    } else {
                        position = index;
                    }

                } else {
                    position++;
                    if (position > (listSong.size() - 1)) {
                        position = 0;
                    }
                }

                if (position < 0) {
                    position = listSong.size() - 1;
                }

                new PlayMp3().execute(listSong.get(position).getLinkBaiHat());

//                sendDataToActivity(ACTION_NEXT);
                // Update Ui
//                musicDiscFrament.playSong(listSong.get(position).getImageBaiHat());
//                getSupportActionBar().setTitle(listSong.get(position).getTenBaiHat());
//                        updateTime();
            }
        }

        // Update Ui
//        imvPlay.setClickable(false);
//        imvPrev.setClickable(false);
//        Handler handler1 = new Handler();
//        handler1.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Update Ui
////                imvPlay.setClickable(true);
////                imvPrev.setClickable(true);
//            }
//        }, 3000);
    }

    public void pauseMusic() {
        if (listSong != null && listSong.size() > 0) {
//                imvPlay.setImageResource(R.drawable.ic_play);
//                musicDiscFrament.stopSong();

            if (mediaPlayer != null && isPlaying) {
                mediaPlayer.pause();
                isPlaying = false;
                sendNotification(listSong);

                // Update UI in Activity
                sendDataToActivity(ACTION_PAUSE);
//                musicDiscFrament.stopSong();
            }
        }
    }

    public void resumeMusic() {
        if (listSong != null && listSong.size() > 0) {
            mediaPlayer.start();
            isPlaying = true;
            sendNotification(listSong);
            sendDataToActivity(ACTION_RESUME);
//            imvPlay.setImageResource(R.drawable.ic_pause);
//            musicDiscFrament.resumeSong();
        }

    }

    public void onStopTrackingTouch(int progress) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(progress);
        }
    }

    private void addEvent() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (viewPagerPlaySong.getItem(1) != null) {
                if (listSong.size() > 0) {
//                        musicDiscFrament.playSong(listSong.get(0).getImageBaiHat());

                    // Update UI in musicDiscFrament use BroadCast Reciver
                    handler.removeCallbacks(this);
                } else {
                    handler.postDelayed(this, 300);
                }
//                }
            }
        }, 500);


    }

    private void sendNotification(ArrayList<BaiHat> listSong) {
        BaiHat song = listSong.get(position);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lemon);
        Bitmap bitmap = null;
        try {
            bitmap = new LoadImageToNotification().execute(Config.domain + song.getImageBaiHat()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, PlaySongActivity.class);
        resultIntent.putExtra("isFromNotification", true);
        resultIntent.putParcelableArrayListExtra("ListSong", listSong);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "TAG");
        NotificationCompat.Builder noti = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle(song.getSinger())
                .setSubText(song.getSinger())
                .setContentText(song.getTenBaiHat())
                .setLargeIcon(bitmap)
                .setContentIntent(resultPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSessionCompat.getSessionToken()));

//        noti.addAction(R.drawable.ic_prev, "Prev", null);
//        noti.addAction(R.drawable.ic_pause, "PlayOrPause", null);
//        noti.addAction(R.drawable.ic_next, "Next", null);
//        noti.addAction(R.drawable.ic_close, "Close", null);

        noti.addAction(R.drawable.ic_prev, "Prev", getPendingIntent(this, ACTION_PREV));
        if (isPlaying) {
            noti.addAction(R.drawable.ic_pause, "Pause", getPendingIntent(this, ACTION_PAUSE));
        } else {
            noti.addAction(R.drawable.ic_play, "Play", getPendingIntent(this, ACTION_RESUME));
        }
        noti.addAction(R.drawable.ic_next, "Next", getPendingIntent(this, ACTION_NEXT));
        noti.addAction(R.drawable.ic_close, "Close", getPendingIntent(this, ACTION_CANCEL));

        startForeground(1, noti.build());

    }

    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(context, SongReciver.class);
        intent.putExtra("action", action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
            listSong.clear();
        }
    }

    private void timeSong() {
        // Update UI in Act

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//        tvTimeTotal.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
//        sbTime.setMax(mediaPlayer.getDuration());

    }

    public void sendDataToActivity(int action) {
        Intent intent = new Intent("data_to_activity");
        Bundle bundle = new Bundle();
        bundle.putParcelable("song", listSong.get(position));
        bundle.putInt("action", action);
        bundle.putBoolean("status", isPlaying);
        bundle.putString("timeTotal", new SimpleDateFormat("mm:ss").format(mediaPlayer.getDuration()));
        bundle.putInt("sbTimeMax", mediaPlayer.getDuration());

        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void updateTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    // Update Ui
                    sendDataToUpdateTimeSong(ACTION_UPDATE_TIME);
//                    sbTime.setProgress(mediaPlayer.getCurrentPosition());
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//                    tvTimeSong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
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
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < listSong.size()) {
                        // Update UI int ACt
//                        imvPlay.setImageResource(R.drawable.ic_pause);
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

                        // Update Ui in act
//                        musicDiscFrament.playSong(listSong.get(position).getImageBaiHat());
//                        getSupportActionBar().setTitle(listSong.get(position).getTenBaiHat());
                    }

//                    // Update Ui in act
////                    imvPlay.setClickable(false);
////                    imvPrev.setClickable(false);
//                    Handler handler1 = new Handler();
//                    handler1.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            // Update Ui in act
////                            imvPlay.setClickable(true);
////                            imvPrev.setClickable(true);
//                        }
//                    }, 3000);

                    next = false;
                    handler1.removeCallbacks(this);
                } else {
                    handler1.postDelayed(this, 1000);
                }
            }
        }, 500);
    }

    private void sendDataToUpdateTimeSong(int actionUpdateTime) {
        Intent intent = new Intent("data_to_activity");
        Bundle bundle = new Bundle();
        bundle.putInt("action", actionUpdateTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        bundle.putInt("sbTimeProgress", mediaPlayer.getCurrentPosition());
        bundle.putString("timeCurrent", simpleDateFormat.format(mediaPlayer.getCurrentPosition()));

        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public class MyBinder extends Binder {
        public PlaySongService getPlaySongService() {
            return PlaySongService.this;
        }
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

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
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
                    isPlaying = false;
                }
            });
            try {
                mediaPlayer.setDataSource(Config.domain + s);
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying = true;
                sendNotification(listSong);
                sendDataToActivity(ACTION_START);
//                timeSong();
                updateTime();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
