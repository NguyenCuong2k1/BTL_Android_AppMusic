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

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {
    Context context;
    ArrayList<Album> listAlbum;

    public AlbumAdapter(Context context, ArrayList<Album> listAlbum) {
        this.context = context;
        this.listAlbum = listAlbum;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_album, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(holder != null) {
            Album album = listAlbum.get(position);
            holder.tvNameSinger.setText(album.getNameSinger());
            holder.tvNameAlbum.setText(album.getNameAlbum());
            Picasso.get().load(Config.domain + album.getImageAlbum()).into(holder.imvAlbum);
        }
    }

    @Override
    public int getItemCount() {
        if(listAlbum != null) {
            return listAlbum.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imvAlbum;
        TextView tvNameAlbum, tvNameSinger;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imvAlbum = itemView.findViewById(R.id.imvAlbum);
            tvNameAlbum = itemView.findViewById(R.id.tvNameAlbum);
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
