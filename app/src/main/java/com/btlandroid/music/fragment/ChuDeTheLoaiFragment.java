package com.btlandroid.music.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.btlandroid.music.R;
import com.btlandroid.music.activity.ListChuDeActivity;
import com.btlandroid.music.activity.ListSongActivity;
import com.btlandroid.music.activity.TheLoaiTheoChuDeActivity;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.ChuDe;
import com.btlandroid.music.model.TheLoai;
import com.btlandroid.music.model.TheLoaiTrongNgay;
import com.btlandroid.music.service.APIService;
import com.btlandroid.music.service.DataService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChuDeTheLoaiFragment extends Fragment {
    View view;
    HorizontalScrollView horizontalScrollView;
    TextView tvSeeMore;
    private String TAG = ChuDeTheLoaiFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chude_theloai, container, false);

        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
        tvSeeMore = view.findViewById(R.id.tvSeeMore);

        tvSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListChuDeActivity.class);
                startActivity(intent);
            }
        });
        getData();
        return view;
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<TheLoaiTrongNgay> call = dataService.getCategoryMusic();
        call.enqueue(new Callback<TheLoaiTrongNgay>() {
            @Override
            public void onResponse(Call<TheLoaiTrongNgay> call, Response<TheLoaiTrongNgay> response) {
                TheLoaiTrongNgay theLoaiTrongNgay = response.body();
//                Log.d(TAG, theLoaiTrongNgay.toString());

                ArrayList<ChuDe> listChuDe = new ArrayList<>();
                listChuDe.addAll(theLoaiTrongNgay.getListChuDe());

                ArrayList<TheLoai> listTheLoai = new ArrayList<>();
                listTheLoai.addAll(theLoaiTrongNgay.getListTheLoai());

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(614, 384);
                params.setMargins(16, 16, 16, 16);
                for(int i = 0; i < listChuDe.size(); i++) {
                    CardView cardView = new CardView(getActivity());
                    cardView.setRadius(16);
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    if(listChuDe.get(i).getHinhChuDe() != null) {
                        Picasso.get().load(Config.domain + listChuDe.get(i).getHinhChuDe()).into(imageView);
                    }
                    cardView.setLayoutParams(params);
                    cardView.addView(imageView);
                    linearLayout.addView(cardView);

                    int finalI = i;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), TheLoaiTheoChuDeActivity.class);
                            intent.putExtra("ChuDe", listChuDe.get(finalI));
                            getActivity().startActivity(intent);
                        }
                    });


                }

                for(int j = 0; j < listTheLoai.size(); j++) {
                    CardView cardView = new CardView(getActivity());
                    cardView.setRadius(16);
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    if(listTheLoai.get(j).getHinhTheLoai() != null) {
                        Picasso.get().load(Config.domain + listTheLoai.get(j).getHinhTheLoai()).into(imageView);
                    }
                    cardView.setLayoutParams(params);
                    cardView.addView(imageView);
                    linearLayout.addView(cardView);

                    int finalJ = j;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ListSongActivity.class);
                            intent.putExtra("IdTheLoai", listTheLoai.get(finalJ));
                            startActivity(intent);
                        }
                    });

                }

                horizontalScrollView.addView(linearLayout);
            }

            @Override
            public void onFailure(Call<TheLoaiTrongNgay> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });

    }
}
