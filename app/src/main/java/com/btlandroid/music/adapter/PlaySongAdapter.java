package com.btlandroid.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.model.BaiHat;

import java.util.ArrayList;

public class PlaySongAdapter extends RecyclerView.Adapter<PlaySongAdapter.ViewHolder> {
    Context context;
    ArrayList<BaiHat> listSong;

    public PlaySongAdapter(Context context, ArrayList<BaiHat> listSong) {
        this.context = context;
        this.listSong = listSong;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_play_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = listSong.get(position);
        holder.tvIndex.setText((String.valueOf(position + 1)));
        holder.tvNameSinger.setText(baiHat.getSinger());
        holder.tvNameSong.setText(baiHat.getTenBaiHat());

    }

    @Override
    public int getItemCount() {
        if(listSong != null) {
            return listSong.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameSong, tvNameSinger, tvIndex;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameSong = itemView.findViewById(R.id.tvNameSong);
            tvNameSinger = itemView.findViewById(R.id.tvNameSinger);
            tvIndex = itemView.findViewById(R.id.tvIndex);
        }
    }
}
