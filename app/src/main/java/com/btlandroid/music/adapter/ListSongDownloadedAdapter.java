package com.btlandroid.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.model.AudioModel;

import java.util.ArrayList;

public class ListSongDownloadedAdapter extends RecyclerView.Adapter<ListSongDownloadedAdapter.ViewHolder> {
    Context context;
    ArrayList<AudioModel> listAudio;

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

        }
    }
}
