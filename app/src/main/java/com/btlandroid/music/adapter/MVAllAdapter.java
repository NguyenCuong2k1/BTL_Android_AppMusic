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
import com.btlandroid.music.activity.PlayMVActivity;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.Mv;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MVAllAdapter extends RecyclerView.Adapter<MVAllAdapter.ViewHolder>{
    Context context;
    ArrayList<Mv> mvArrayList;

    public MVAllAdapter(Context context, ArrayList<Mv> mvArrayList) {
        this.context = context;
        this.mvArrayList = mvArrayList;
    }

    @NonNull
    @Override
    public MVAllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_all_mv, parent, false);

        return new MVAllAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MVAllAdapter.ViewHolder holder, int position) {
        Mv mv = mvArrayList.get(position);
        holder.txtCasiMV.setText(mv.getTencasi());
        holder.txtTenMV.setText(mv.getTen());
        Picasso.get().load(Config.domain + mv.getHinh()).into(holder.imgMV);
    }

    @Override
    public int getItemCount() {
        return mvArrayList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgMV;
        TextView txtTenMV, txtCasiMV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMV = itemView.findViewById(R.id.imageallMV);
            txtTenMV = itemView.findViewById(R.id.txtallTenMV);
            txtCasiMV = itemView.findViewById(R.id.txtallCasiMV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayMVActivity.class);
                    intent.putExtra("MV", mvArrayList.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
