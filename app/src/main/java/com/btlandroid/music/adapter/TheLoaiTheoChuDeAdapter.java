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
import com.btlandroid.music.activity.TheLoaiTheoChuDeActivity;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.TheLoai;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TheLoaiTheoChuDeAdapter extends RecyclerView.Adapter<TheLoaiTheoChuDeAdapter.ViewHolder> {
    Context context;
    ArrayList<TheLoai> listTheLoai;

    public TheLoaiTheoChuDeAdapter(Context context, ArrayList<TheLoai> listTheLoai) {
        this.context = context;
        this.listTheLoai = listTheLoai;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_the_loai_theo_chu_de, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TheLoai theLoai = listTheLoai.get(position);

        Picasso.get().load(Config.domain + theLoai.getHinhTheLoai()).into(holder.imvImage);
        holder.tvName.setText(theLoai.getTenTheLoai());
    }

    @Override
    public int getItemCount() {
        return listTheLoai.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imvImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvTenTheLoai);
            imvImage = itemView.findViewById(R.id.imvTheLoai);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ListSongActivity.class);
                    intent.putExtra("IdTheLoai", listTheLoai.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
