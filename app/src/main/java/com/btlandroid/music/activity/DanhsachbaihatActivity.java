package com.btlandroid.music.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.btlandroid.music.R;

public class DanhsachbaihatActivity extends AppCompatActivity {

    Quangcao quangcao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsachbaihat);
        DateIntent();
    }

    private void DateIntent() {
        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("banner")){
                quangcao= (Quangcao) intent.getSerializableExtra("banner");
                Toast.makeText(this,quangcao.getnameBaiHat(), Toast.LENGTH_SHORT).show();

            }
        }
    }

}