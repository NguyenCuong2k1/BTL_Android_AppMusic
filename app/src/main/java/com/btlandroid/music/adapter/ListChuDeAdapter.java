package com.btlandroid.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.activity.TheLoaiTheoChuDeActivity;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.ChuDe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListChuDeAdapter extends RecyclerView.Adapter<ListChuDeAdapter.ViewHolder> {
    Context context;
    ArrayList<ChuDe> listChuDe;

    public ListChuDeAdapter(Context context, ArrayList<ChuDe> listChuDe) {
        this.context = context;
        this.listChuDe = listChuDe;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_chu_de, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChuDe chuDe = listChuDe.get(position);

//        Log.d("ListChuDeAdapter", Config.domain + chuDe.getHinhChuDe());
        Picasso.get().load(Config.domain + chuDe.getHinhChuDe()).into(holder.imvChuDe);
    }

    @Override
    public int getItemCount() {
        if(listChuDe != null) {
            return listChuDe.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imvChuDe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imvChuDe = itemView.findViewById(R.id.imvChuDe);

            imvChuDe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TheLoaiTheoChuDeActivity.class);
                    intent.putExtra("ChuDe", listChuDe.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
