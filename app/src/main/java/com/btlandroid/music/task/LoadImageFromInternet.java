package com.btlandroid.music.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.io.InputStream;

public class LoadImageFromInternet extends AsyncTask<String, Void, Bitmap> {

    private CollapsingToolbarLayout bmImage;
    private Context context;

    public LoadImageFromInternet(CollapsingToolbarLayout bmImage, Context context) {
        this.bmImage = bmImage;
        this.context = context;
    }

    protected void onPreExecute() {

    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", "image download error");
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        //set image of your imageview
//        bmImage.setImageBitmap(result);
        //close
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), result);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            bmImage.setBackground(bitmapDrawable);
        }
    }
}
