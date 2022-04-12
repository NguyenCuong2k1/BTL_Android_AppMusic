package com.btlandroid.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.activity.MainActivity;
import com.btlandroid.music.activity.PlaySongActivity;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.BaiHat;
import com.btlandroid.music.model.User;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiHatHotAdapter extends RecyclerView.Adapter<BaiHatHotAdapter.BaiHatViewHolder> {
    private static final int TYPE_LOGIN_FACEBOOK = 0;
    private static final int TYPE_LOGIN_GOOGLE = 1;
    private static final String TAG = BaiHatHotAdapter.class.getName();
    Context context;
    ArrayList<BaiHat> listSong;
    ArrayList<BaiHat> listSongLiked;

    public BaiHatHotAdapter(Context context, ArrayList<BaiHat> listSong) {
        this.context = context;
        this.listSong = listSong;

        SharedPreferences sharedPreferences = context.getSharedPreferences("DataUser", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("User", "");
        User user = gson.fromJson(json, User.class);
        if(user != null) {
            // handle like
            DataService dataService = APIService.getService();
            Call<Integer> callback;
            if (user.getUser_IdFacebook() != null && !user.getUser_IdFacebook().equals("")) {
                callback = dataService.getUserId(user.getUser_IdFacebook(), TYPE_LOGIN_FACEBOOK);
            } else {
                callback = dataService.getUserId(user.getUser_IdGoogle(), TYPE_LOGIN_GOOGLE);
            }
            callback.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    Integer user_id = response.body();
                    Log.d(TAG, user_id.toString());

                    getListSongLiked(user_id);
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }
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


//        if(listSongLiked)
        if(listSongLiked != null) {
            for(int i = 0; i < listSongLiked.size(); i++) {
                if(listSong.get(position).getIdBaiHat().equals(listSongLiked.get(i).getIdBaiHat())) {
                    holder.imvLike.setImageResource(R.drawable.ic_like);
                    holder.imvLike.setEnabled(false);
                }
            }
        }

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
                    // login
                    SharedPreferences sharedPreferences = context.getSharedPreferences("DataUser", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("User", "");
                    User user = gson.fromJson(json, User.class);

                    if(user != null) {
                        // handle like
                        DataService dataService = APIService.getService();
                        Call<Integer> call;
                        if (user.getUser_IdFacebook() != null && !user.getUser_IdFacebook().equals("")) {
                            call = dataService.getUserId(user.getUser_IdFacebook(), TYPE_LOGIN_FACEBOOK);
                        } else {
                            call = dataService.getUserId(user.getUser_IdGoogle(), TYPE_LOGIN_GOOGLE);
                        }
                        call.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                Integer user_id = response.body();
                                Log.d(TAG, user_id.toString());

                                imvLike.setImageResource(R.drawable.ic_like);
                                imvLike.setEnabled(false);
                                Log.d(TAG, "ID Song: "+ listSong.get(getPosition()).getIdBaiHat());
                                handleLike(user_id, listSong.get(getPosition()).getIdBaiHat());
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });

                    } else {
                        showUILogin();
                    }


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

    private void handleLike(Integer user_id, String idBaiHat) {
        DataService dataService = APIService.getService();
        Call<String> call = dataService.handleLike(user_id, Integer.parseInt(idBaiHat));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                if(result.equals("success")) {
                    Toast.makeText(context, "Đã thích", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, result.toString());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                Log.e(TAG, t.toString());
            }
        });

    }

    private void showUILogin() {
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

    private void getListSongLiked(Integer user_id) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> call = dataService.getListSongLiked(user_id);
        call.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                listSongLiked = (ArrayList<BaiHat>) response.body();
                notifyItemRangeChanged(0, listSong.size());
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    public interface IDownload {
        void onDownload(String url);
    }

    public interface ILogin {
        void onLoginByFacebook();
        void onLoginByGoogle();
    }
}
