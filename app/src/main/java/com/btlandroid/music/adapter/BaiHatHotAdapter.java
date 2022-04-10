package com.btlandroid.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
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

public class BaiHatHotAdapter extends RecyclerView.Adapter<BaiHatHotAdapter.BaiHatViewHolder> {
    Context context;
    ArrayList<BaiHat> listSong;

    public BaiHatHotAdapter(Context context, ArrayList<BaiHat> listSong) {
        this.context = context;
        this.listSong = listSong;
    }

    @NonNull
    @Override
    public BaiHatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_baihathot, parent, false);

        return new BaiHatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaiHatViewHolder holder, int position) {
        BaiHat baiHat = listSong.get(position);
        holder.tvNameSong.setText(baiHat.getTenBaiHat());
        holder.tvNameSinger.setText(baiHat.getSinger());
        Picasso.get().load(Config.domain + baiHat.getImageBaiHat()).into(holder.imvSong);


    }

    @Override
    public int getItemCount() {
        if(listSong != null) {
            return listSong.size();
        }
        return 0;
    }

    public class BaiHatViewHolder extends RecyclerView.ViewHolder {

        TextView tvNameSong, tvNameSinger;
        ImageView imvSong, imvLike, imvMore;

        public BaiHatViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameSong = itemView.findViewById(R.id.tvBaiHatHot);
            tvNameSinger = itemView.findViewById(R.id.tvNameSinger);
            imvSong = itemView.findViewById(R.id.imvBaiHatHot);
            imvLike = itemView.findViewById(R.id.imvLike);
            imvMore = itemView.findViewById(R.id.imvMore);

            imvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    imvLike.setImageResource(R.drawable.ic_like);
//                    DataService dataService = APIService.getService();
//                    Call<String> callback = dataService.updateCountLike("1", listSong.get(getPosition()).getIdBaiHat());
//                    callback.enqueue(new Callback<String>() {
//                        @Override
//                        public void onResponse(Call<String> call, Response<String> response) {
//                            String results = response.body();
//                            if(results.equals("success")){
//                                Toast.makeText(context, "Đã thích", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(context, "Lỗi", Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<String> call, Throwable t) {
//
//                        }
//                    });
//                    imvLike.setEnabled(false);

                    LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_login, null);
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
                    bottomSheetDialog.setContentView(view);

                    Button btnFacebook, btnGoogle;
                    btnFacebook = view.findViewById(R.id.btnFacebook);
                    btnGoogle = view.findViewById(R.id.btnGoogle);

                    btnFacebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity)context).onLoginByFacebook();
                            bottomSheetDialog.cancel();
                        }

                    });

                    btnGoogle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity)context).onLoginByGoogle();
                            bottomSheetDialog.cancel();
                        }
                    });
                    bottomSheetDialog.show();

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
                            ((MainActivity)context).onDownload(Config.domain + listSong.get(getPosition()).getLinkBaiHat());

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlaySongActivity.class);
                    intent.putExtra("song", listSong.get(getPosition()));
                    context.startActivity(intent);
                }
            });

        }
    }

    public interface IDownload {
        void onDownload(String url);
    }

    public interface ILogin {
        void onLoginByFacebook();
        void onLoginByGoogle();
    }
}
