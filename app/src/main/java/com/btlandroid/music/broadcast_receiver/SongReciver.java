package com.btlandroid.music.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.btlandroid.music.service.PlaySongService;

public class SongReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int action = intent.getIntExtra("action", 0);
        Intent intent1 = new Intent(context, PlaySongService.class);
        intent1.putExtra("action", action);

//        Toast.makeText(context, "Da nhan duoc", Toast.LENGTH_LONG).show();
        context.startService(intent1);
    }
}
