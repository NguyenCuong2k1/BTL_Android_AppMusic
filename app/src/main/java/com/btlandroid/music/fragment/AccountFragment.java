package com.btlandroid.music.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btlandroid.music.R;
import com.btlandroid.music.activity.MainActivity;
import com.btlandroid.music.adapter.ListSongDownloadedAdapter;
import com.btlandroid.music.adapter.ListSongLikedAdapter;
import com.btlandroid.music.model.AudioModel;
import com.btlandroid.music.model.BaiHat;
import com.btlandroid.music.model.User;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private static final int TYPE_LOGIN_FACEBOOK = 0;
    private static final int TYPE_LOGIN_GOOGLE = 1;
    private static final String TAG = AccountFragment.class.getName();
    View view;
    RecyclerView rvListSong, rvListSongLiked;
    ArrayList<AudioModel> listAudioModel;
    ArrayList<BaiHat> listSongLiked;
    ListSongLikedAdapter listSongLikedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);

        rvListSong = view.findViewById(R.id.rvListSong);
//        rvListSongLiked = view.findViewById(R.id.rvListSongLiked);
//
//        listAudioModel = (ArrayList<AudioModel>) getAllAudioFromDevice(getContext());
//        ListSongDownloadedAdapter adapter = new ListSongDownloadedAdapter(getContext(), (ArrayList<AudioModel>) listAudioModel);
//        rvListSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        rvListSong.setAdapter(adapter);


        return view;
    }

    public void updateUI(User user) {
        if(view != null) {
            CircleImageView imvAccount = view.findViewById(R.id.cimvAccount);
            TextView tvName = view.findViewById(R.id.tvName);
            TextView tvLoginLogout = view.findViewById(R.id.tvLoginLogout);

            if(user != null) {
                tvName.setText(user.getUser_name());
                tvName.setVisibility(View.VISIBLE);
                tvLoginLogout.setText("Đăng xuất");

                tvLoginLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleLogout();
                    }
                });

                if(user.getUser_url_image() != null && !user.getUser_url_image().equals("")) {
                    Picasso.get().load(user.getUser_url_image()).into(imvAccount);
                } else {
                    imvAccount.setImageResource(R.drawable.ic_account_circle);
                }

            } else {
                tvName.setVisibility(View.INVISIBLE);
                tvLoginLogout.setText("Đăng nhập");
                imvAccount.setImageResource(R.drawable.ic_account_circle);

                tvLoginLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleLogin();
                    }
                });
            }
        }

    }

    private void handleLogin() {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_login, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(view);

        Button btnFacebook, btnGoogle;
        btnFacebook = view.findViewById(R.id.btnFacebook);
        btnGoogle = view.findViewById(R.id.btnGoogle);

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getContext()).onLoginByFacebook();
                bottomSheetDialog.cancel();
            }

        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getContext()).onLoginByGoogle();
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    private void handleLogout() {
        LoginManager.getInstance().logOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(getContext(), gso);
        googleSignInClient.signOut();

        Toast.makeText(getContext(), "Đã đăng xuất!", Toast.LENGTH_LONG).show();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("DataUser", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        User user = null;
        updateUI(user);

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("currentItem", 2);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onResume() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("DataUser", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("User", "");
        User user = gson.fromJson(json, User.class);

        updateUI(user);

        listAudioModel = (ArrayList<AudioModel>) getAllAudioFromDevice(getContext());
        ListSongDownloadedAdapter adapter = new ListSongDownloadedAdapter(getContext(), (ArrayList<AudioModel>) listAudioModel);
        rvListSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvListSong.setAdapter(adapter);


        super.onResume();
    }

    public List<AudioModel> getAllAudioFromDevice(final Context context) {
        final List<AudioModel> tempAudioList = new ArrayList<>();


        if(((MainActivity)context).isStoragePermissionGranted()) {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
            Cursor c = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%Music%"}, null);

            if (c != null) {
                while (c.moveToNext()) {

                    AudioModel audioModel = new AudioModel();
                    String path = c.getString(0);
                    String album = c.getString(1);
                    String artist = c.getString(2);

                    String name = path.substring(path.lastIndexOf("/") + 1);

                    audioModel.setaName(name);
                    audioModel.setaAlbum(album);
                    audioModel.setaArtist(artist);
                    audioModel.setaPath(path);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);

                    tempAudioList.add(audioModel);
                }
                c.close();
            }
        }

        return tempAudioList;
    }
}
