package com.btlandroid.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.activity.ListSongActivity;
import com.btlandroid.music.activity.MainActivity;
import com.btlandroid.music.activity.PlaySongActivity;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.BaiHat;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSongAdapter extends RecyclerView.Adapter<ListSongAdapter.ViewHolder> {
    Context context;
    ArrayList<BaiHat> listSong;

    public ListSongAdapter(Context context, ArrayList<BaiHat> listSong) {
        this.context = context;
        this.listSong = listSong;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_listsong, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat song = listSong.get(position);
        if(song != null) {
            holder.tvNameSong.setText(song.getTenBaiHat());
            holder.tvNameSinger.setText(song.getSinger());
            holder.tvIndex.setText(position + 1 + "");

        }
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
        ImageView imvLike, imvMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameSong = itemView.findViewById(R.id.tvNameSong);
            tvNameSinger = itemView.findViewById(R.id.tvNameSinger);
            tvIndex = itemView.findViewById(R.id.tvIndex);
            imvLike = itemView.findViewById(R.id.imvLike);
            imvMore = itemView.findViewById(R.id.imvMore);

            imvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imvLike.setImageResource(R.drawable.ic_like);
                    DataService dataService = APIService.getService();
                    Call<String> callback = dataService.updateCountLike("1", listSong.get(getPosition()).getIdBaiHat());
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String results = response.body();
                            if(results.equals("success")){
                                Toast.makeText(context, "Đã thích", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Lỗi", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                    imvLike.setEnabled(false);



                }
            });



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlaySongActivity.class);
                    intent.putExtra("song", listSong.get(getPosition()));
                    context.startActivity(intent);
                }
            });

            imvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null);
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
                    bottomSheetDialog.setContentView(view);

                    ImageView imvSong = view.findViewById(R.id.imvSongDialog);
                    TextView tvNamgSong = view.findViewById(R.id.tvNameSongDialog);
                    TextView tvNameSinger = view.findViewById(R.id.tvNameSingerDialog);
                    TextView tvDownload = view.findViewById(R.id.tvDownloadSong);
                    TextView tvAddToPlaylist = view.findViewById(R.id.tvAddToPlaylist);

                    tvNamgSong.setText(listSong.get(getPosition()).getTenBaiHat());
                    tvNameSinger.setText(listSong.get(getPosition()).getSinger());
                    Picasso.get().load(Config.domain + listSong.get(getPosition()).getImageBaiHat()).into(imvSong);

                    tvDownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "Da toi day", Toast.LENGTH_LONG).show();
                            ((ListSongActivity)context).onDownload(Config.domain + listSong.get(getPosition()).getLinkBaiHat());

                        }
                    });

                    tvAddToPlaylist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    bottomSheetDialog.show();
                }
            });

        }
    }

    public interface IDownload {
        void onDownload(String url);

    }
}
