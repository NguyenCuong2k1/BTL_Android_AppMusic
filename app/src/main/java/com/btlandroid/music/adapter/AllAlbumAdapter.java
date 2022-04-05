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
import com.btlandroid.music.model.Album;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllAlbumAdapter extends RecyclerView.Adapter<AllAlbumAdapter.ViewHolder> {
    Context context;
    ArrayList<Album> listAlbum;

    public AllAlbumAdapter(Context context, ArrayList<Album> listAlbum) {
        this.context = context;
        this.listAlbum = listAlbum;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_all_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = listAlbum.get(position);
        Picasso.get().load(Config.domain + album.getImageAlbum()).into(holder.imvAlbum);
        holder.tvNameAlbum.setText(album.getNameAlbum());
        holder.tvNameSinger.setText(album.getNameSinger());
    }

    @Override
    public int getItemCount() {
        if(listAlbum != null) {
            return listAlbum.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imvAlbum;
        TextView tvNameAlbum, tvNameSinger;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imvAlbum = itemView.findViewById(R.id.imvAllAlbum);
            tvNameAlbum = itemView.findViewById(R.id.tvNameAllAlbum);
            tvNameSinger = itemView.findViewById(R.id.tvNameSinger);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ListSongActivity.class);
                    intent.putExtra("Album", listAlbum.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
