package com.btlandroid.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.activity.ListSongActivity;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.Playlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListPlaylistAdapter extends RecyclerView.Adapter<ListPlaylistAdapter.ViewHolder> {
    Context context;
    ArrayList<Playlist> listPlaylist;

    public ListPlaylistAdapter(Context context, ArrayList<Playlist> listPlaylist) {
        this.context = context;
        this.listPlaylist = listPlaylist;
    }

    @NonNull
    @Override
    public ListPlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_playlist_act_playlist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPlaylistAdapter.ViewHolder holder, int position) {

        Playlist playlist = listPlaylist.get(position);
        Picasso.get().load(Config.domain + playlist.getHinh()).into(holder.imvImage);
        holder.tvNamePlaylist.setText(playlist.getTen());
//        playlist.ge

    }

    @Override
    public int getItemCount() {
        if(listPlaylist != null) {
            return listPlaylist.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamePlaylist;
        ImageView imvImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNamePlaylist = itemView.findViewById(R.id.tvNamePlaylist);
            imvImage = itemView.findViewById(R.id.imvImagePlaylist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ListSongActivity.class);
                    intent.putExtra("ItemPlaylist", listPlaylist.get(getPosition()));
                    context.startActivity(intent);
                }
            });

        }
    }
}
