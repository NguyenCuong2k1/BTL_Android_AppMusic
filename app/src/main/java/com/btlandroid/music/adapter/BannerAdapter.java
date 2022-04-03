package com.btlandroid.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.btlandroid.music.R;
import com.btlandroid.music.activity.ListSongActivity;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.QuangCao;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BannerAdapter extends PagerAdapter {
    private static final String TAG = BannerAdapter.class.getName();
    ArrayList<QuangCao> listBanner;
    Context context;

    public BannerAdapter(ArrayList<QuangCao> listBanner, Context context) {
        this.listBanner = listBanner;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(listBanner != null) {
            return listBanner.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_banner, null);

        ImageView imvBackgroundBanner = view.findViewById(R.id.imvBackgroundBanner);
        ImageView imvBanner = view.findViewById(R.id.imvBanner);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvContent = view.findViewById(R.id.tvContent);

        Picasso.get().load(Config.domain + listBanner.get(position).getImage()).into(imvBackgroundBanner);
        Picasso.get().load(Config.domain + listBanner.get(position).getImageBaiHat()).into(imvBanner);
        tvTitle.setText(listBanner.get(position).getNameBaiHat());
        tvContent.setText(listBanner.get(position).getNoiDung());

        container.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListSongActivity.class);
                intent.putExtra("banner", listBanner.get(position));
                context.startActivity(intent);

            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
