package com.btlandroid.music.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.btlandroid.music.R;
import com.btlandroid.music.config.Config;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicDiscFrament extends Fragment {
    private static final String TAG = MusicDiscFrament.class.getName();
    View view;
    CircleImageView circleImageView;
    ObjectAnimator objectAnimator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_disc, container, false);

        circleImageView = view.findViewById(R.id.imvCircle);


//        objectAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0f, 0, 360);
//        objectAnimator.setDuration(10000);
//        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
//        objectAnimator.setInterpolator(new LinearInterpolator());


        objectAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0, 360);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        objectAnimator.setDuration(20000);
        objectAnimator.setInterpolator(new LinearInterpolator());


        return view;
    }

    public void playSong(String urlImage) {
        Picasso.get().load(Config.domain + urlImage).into(circleImageView);

//        RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        rotate.setDuration(10000);
//        rotate.setRepeatCount(ValueAnimator.INFINITE);
//        rotate.setRepeatMode(ValueAnimator.RESTART);
//        rotate.setFillAfter(true);
//        rotate.setInterpolator(new LinearInterpolator());
//
//        circleImageView.startAnimation(rotate);
        objectAnimator.start();

    }

    public void stopSong() {
        objectAnimator.pause();
    }

    public void resumeSong() {
        if(objectAnimator.isRunning()) {
            objectAnimator.resume();
        }

    }
}
