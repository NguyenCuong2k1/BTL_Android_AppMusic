package com.btlandroid.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.btlandroid.music.R;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.Playlist;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {


    public PlaylistAdapter(@NonNull Context context, int resource, @NonNull List<Playlist> objects) {
        super(context, resource, objects);
    }

    class ViewHolder {
        TextView tvNamePlayList;
        ImageView imvBackground, imvPlaylist;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_playlist, null);
            viewHolder = new ViewHolder();
            viewHolder.tvNamePlayList = convertView.findViewById(R.id.tvPlaylist);
            viewHolder.imvBackground = convertView.findViewById(R.id.imvBackgroundPlaylist);
            viewHolder.imvPlaylist = convertView.findViewById(R.id.imvPlaylist);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Playlist playlist = getItem(position);
        Picasso.get().load(Config.domain + playlist.getHinh()).into(viewHolder.imvBackground);
        Picasso.get().load(Config.domain + playlist.getIcon()).into(viewHolder.imvPlaylist);
        viewHolder.tvNamePlayList.setText(playlist.getTen());


        return convertView;
    }
}
