package com.btlandroid.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.activity.ListSongActivity;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.Playlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    Context context;
    ArrayList<Playlist> listPlaylist;

    public PlaylistAdapter(Context context, ArrayList<Playlist> listPlaylist) {
        this.context = context;
        this.listPlaylist = listPlaylist;
    }

    @NonNull
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.ViewHolder holder, int position) {
        Playlist playlist = listPlaylist.get(position);
        Picasso.get().load(Config.domain + playlist.getHinh()).into(holder.imvBackground);
        Picasso.get().load(Config.domain + playlist.getIcon()).into(holder.imvPlaylist);
        holder.tvNamePlayList.setText(playlist.getTen());
    }

    @Override
    public int getItemCount() {
        return listPlaylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamePlayList;
        ImageView imvBackground, imvPlaylist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNamePlayList = itemView.findViewById(R.id.tvPlaylist);
            imvBackground = itemView.findViewById(R.id.imvBackgroundPlaylist);
            imvPlaylist = itemView.findViewById(R.id.imvPlaylist);

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


//    public PlaylistAdapter(@NonNull Context context, int resource, @NonNull List<Playlist> objects) {
//        super(context, resource, objects);
//    }
//
//    class ViewHolder {
//        TextView tvNamePlayList;
//        ImageView imvBackground, imvPlaylist;
//
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        ViewHolder viewHolder = null;
//        if(convertView == null) {
//            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//            convertView = layoutInflater.inflate(R.layout.item_playlist, null);
//            viewHolder = new ViewHolder();
//            viewHolder.tvNamePlayList = convertView.findViewById(R.id.tvPlaylist);
//            viewHolder.imvBackground = convertView.findViewById(R.id.imvBackgroundPlaylist);
//            viewHolder.imvPlaylist = convertView.findViewById(R.id.imvPlaylist);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        Playlist playlist = getItem(position);
//        Picasso.get().load(Config.domain + playlist.getHinh()).into(viewHolder.imvBackground);
//        Picasso.get().load(Config.domain + playlist.getIcon()).into(viewHolder.imvPlaylist);
//        viewHolder.tvNamePlayList.setText(playlist.getTen());
//
//
//        return convertView;
//    }
}
