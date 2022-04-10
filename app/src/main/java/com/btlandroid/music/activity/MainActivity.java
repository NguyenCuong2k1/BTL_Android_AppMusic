package com.btlandroid.music.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.btlandroid.music.fragment.AccountFragment;
import com.btlandroid.music.fragment.HomeFragment;
import com.btlandroid.music.fragment.SearchFragment;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.btlandroid.music.R;
import com.btlandroid.music.adapter.BaiHatHotAdapter;
import com.btlandroid.music.adapter.MyViewPagerAdapter;
import com.btlandroid.music.config.Config;
import com.btlandroid.music.model.BaiHat;
import com.btlandroid.music.model.User;
import com.btlandroid.music.retrofit.APIService;
import com.btlandroid.music.retrofit.DataService;
import com.btlandroid.music.service.PlaySongService;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BaiHatHotAdapter.IDownload, BaiHatHotAdapter.ILogin {
//    private static final String EMAIL = "email";
    private static final int RC_SIGN_IN = 100;
    CallbackManager callbackManager;
    LoginButton loginButton;

    private GoogleSignInClient mGoogleSignInClient;

    SignInButton signInButton;

    private static final String TAG = MainActivity.class.getName();
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;
    ImageView imvPlayPause, imvNext, imvPrev, imvClose, imvSong;
    TextView tvNameSong, tvNameSinger;
    ConstraintLayout clPlaySongCollapse;
    BaiHat song;
    boolean isPlaying = false;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleLayoutMusic(intent);
        }
    };
    private PlaySongService playSongService;
    private boolean isServiceConnected;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlaySongService.MyBinder myBinder = (PlaySongService.MyBinder) service;
            playSongService = myBinder.getPlaySongService();
            isServiceConnected = true;

            if (playSongService.mediaPlayer != null && playSongService.listSong != null) {
                clPlaySongCollapse.setVisibility(View.VISIBLE);
                BaiHat song = playSongService.listSong.get(playSongService.position);
                Picasso.get().load(Config.domain + song.getImageBaiHat()).into(imvSong);
                tvNameSinger.setText(song.getSinger());
                tvNameSong.setText(song.getTenBaiHat());
                if(playSongService.mediaPlayer.isPlaying()) {
                    imvPlayPause.setImageResource(R.drawable.ic_pause);
                } else {
                    imvPlayPause.setImageResource(R.drawable.ic_play);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        mapping();
        initView();
        addEvent();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("data_to_activity"));

//        List<AudioModel> list = getAllAudioFromDevice(this);
//        for(int i = 0; i < list.size(); i++) {
//            Log.d(TAG, list.get(i).getaAlbum() + "\t" + list.get(i).getaPath() + "\t" + list.get(i).getaName() + "\n");
//        }
//        Log.d(TAG, list.toString());

        // Login facebook
        printHashKey(this);



    }

    private void addEvent() {
        imvPlayPause.setOnClickListener(v -> {
            if (isServiceConnected) {
                if (playSongService.mediaPlayer != null && playSongService.mediaPlayer.isPlaying()) {
                    playSongService.pauseMusic();
                } else if(playSongService.mediaPlayer != null){
                    playSongService.resumeMusic();
                }
            }
        });

        imvNext.setOnClickListener(v -> {
            if (isServiceConnected) {
                playSongService.nextMusic();
            }
        });

        imvPrev.setOnClickListener(v -> {
            if (isServiceConnected) {
                playSongService.prevMusic();
            }
        });

        imvClose.setOnClickListener(v -> {
            if(isServiceConnected) {
                playSongService.stopSelf();
                playSongService.sendDataToActivity(PlaySongService.ACTION_CANCEL);

            }
        });

        callbackManager = CallbackManager.Factory.create();

//        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//        if(isLoggedIn) {
//            Log.d(TAG, "da login");
//            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
//        }

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday"));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, loginResult.toString());
//                Log.d(TAG, loginResult.getJSONObject().toString());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d(TAG, response.toString());
//                                showProcessDialog();
                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String id = object.getString("id");
                                    String name = response.getJSONObject().getString("name");
                                    String profileImageUrl = ImageRequest.getProfilePictureUri(object.optString("id"), 500, 500).toString();
                                    Log.d(TAG, email + "\n" + id + "\n" + name + "\n" + profileImageUrl);

                                    User user = new User(name, id, profileImageUrl);
                                    processLoginByFacebook(user);

//                                    Log.v("Login", profileImageUrl);
//                                    User user = new User(name, email, null, id, null, profileImageUrl, true);
//                                    startActivityMainWithLoginFacebook(user);
                                } catch (JSONException e) {
//                                    stopDialogProcess();
                                    e.printStackTrace();
                                }
//                                String birthday = object.getString("birthday"); // 01/31/1980 format
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "loginButton.registerCallback");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        // Login google
        signInButton.setOnClickListener(v -> signIn());
    }

    private void processLoginByFacebook(User user) {
        DataService dataService = APIService.getService();
        Call<String> callback = dataService.loginByFacebook(user.getUser_IdFacebook(), user.getUser_name(), user.getUser_url_image());

        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String respon = response.body();
                if(respon != null && respon.equalsIgnoreCase("Success")) {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();

                    AccountFragment accountFragment = (AccountFragment) myViewPagerAdapter.getFragment(2);
                    accountFragment.updateUI(user);

                    SharedPreferences sharedPreferences = getSharedPreferences("DataUser", MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    prefsEditor.putString("User", json);
                    prefsEditor.apply();
                } else {
                    Toast.makeText(MainActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, respon.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, t.toString());
                Toast.makeText(MainActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        myViewPagerAdapter.addFragment(new HomeFragment());
        myViewPagerAdapter.addFragment(new SearchFragment());
        myViewPagerAdapter.addFragment(new AccountFragment());
        viewPager2.setAdapter(myViewPagerAdapter);


        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("Trang chủ");
                                tab.setIcon(R.drawable.ic_home);
                                break;
                            case 1:
                                tab.setText("Tìm kiếm");
                                tab.setIcon(R.drawable.ic_search);
                                break;
                            case 2:
                                tab.setText("Cá nhân");
                                tab.setIcon(R.drawable.ic_account_circle);
                                break;
                        }
                    }
                }).attach();

        viewPager2.setUserInputEnabled(false);
        isStoragePermissionGranted();

        // Login google
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

// Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            String urlPhoto = "";
            if(personPhoto != null) {
                urlPhoto = personPhoto.toString();
            }
            User user = new User(personName, personEmail, personId, urlPhoto);
            processLoginByGoogle(user);
            Log.d(TAG, personName + "\n" + personId + "\n" + personEmail);
        }
//        updateUI(account);

        // Set the dimensions of the sign-in button.
        signInButton.setSize(SignInButton.SIZE_STANDARD);
    }

    private void mapping() {
        tabLayout = findViewById(R.id.actMainTabLayout);
        viewPager2 = findViewById(R.id.actMainViewPager);

        imvClose = findViewById(R.id.imvCloseActMain);
        imvNext = findViewById(R.id.imvNextActMain);
        imvPlayPause = findViewById(R.id.imvPlayPauseActMain);
        imvPrev = findViewById(R.id.imvPrevActMain);
        imvSong = findViewById(R.id.imvSongActMain);

        tvNameSong = findViewById(R.id.tvNameSongActMain);
        tvNameSinger = findViewById(R.id.tvNameSingerActMain);
        clPlaySongCollapse = findViewById(R.id.clPlaySongCollapse);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        signInButton = findViewById(R.id.sign_in_button);

    }

    @Override
    protected void onStart() {

        Intent intentService = new Intent(MainActivity.this, PlaySongService.class);
        bindService(intentService, serviceConnection, BIND_AUTO_CREATE);


        super.onStart();
    }


    private void handleLayoutMusic(Intent intent) {

        Bundle bundle = intent.getExtras();
        int action = bundle.getInt("action", 0);
        switch (action) {
            case PlaySongService.ACTION_START:
                setDataLayoutByActionStart(bundle);
                setStatusPlayOrPause(bundle);
                break;
            case PlaySongService.ACTION_RESUME:
                setStatusPlayOrPause(bundle);
                break;
            case PlaySongService.ACTION_PAUSE:
                setStatusPlayOrPause(bundle);
                break;
            case PlaySongService.ACTION_NEXT:
                setDataLayoutByActionStart(bundle);
                break;
            case PlaySongService.ACTION_PREV:
                setDataLayoutByActionStart(bundle);
                break;
            case PlaySongService.ACTION_CANCEL:
                clPlaySongCollapse.setVisibility(View.GONE);
                if (isServiceConnected) {
                    unbindService(serviceConnection);
                    isServiceConnected = false;
                }
                break;
            default:
                break;
        }
    }

    private void setStatusPlayOrPause(Bundle bundle) {
        isPlaying = bundle.getBoolean("status", false);
        if (isPlaying) {
            imvPlayPause.setImageResource(R.drawable.ic_pause);
        } else {
            imvPlayPause.setImageResource(R.drawable.ic_play);
        }
    }

    private void setDataLayoutByActionStart(Bundle bundle) {
        song = (BaiHat) bundle.getParcelable("song");
        isPlaying = bundle.getBoolean("status", false);

        if (song != null) {
            clPlaySongCollapse.setVisibility(View.VISIBLE);
//            getSupportActionBar().setTitle(song.getTenBaiHat());
            imvPlayPause.setImageResource(R.drawable.ic_pause);
            Picasso.get().load(Config.domain + song.getImageBaiHat()).into(imvSong);
            tvNameSinger.setText(song.getSinger());
            tvNameSong.setText(song.getTenBaiHat());

        } else {
            clPlaySongCollapse.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        SharedPreferences sharedPreferences = getSharedPreferences("DataSongPlaying", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear().commit();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        if (isServiceConnected) {
            unbindService(serviceConnection);
            isServiceConnected = false;
        }

        super.onDestroy();
    }

    @Override
    public void onDownload(String url) {
        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(uri.getLastPathSegment());
        request.setDescription("Downloading...");
        request.setMimeType("audio/MP3)");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED | DownloadManager.Request.VISIBILITY_VISIBLE);
        if(isStoragePermissionGranted()) {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, uri.getLastPathSegment());
            Log.d(TAG, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_MUSIC + "/" + uri.getLastPathSegment());
//                request.setDestinationUri(Uri.parse("file://" + "music" + uri.getLastPathSegment()));
            downloadmanager.enqueue(request);
            Toast.makeText(this, "Downloading", Toast.LENGTH_LONG).show();
        }
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
//            onDownload();
        }
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoginByFacebook() {
        loginButton.performClick();
    }

    @Override
    public void onLoginByGoogle() {
//        signInButton.performClick();
        signIn();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                String urlPhoto = "";
                if(personPhoto != null) {
                    urlPhoto = personPhoto.toString();
                }
                User user = new User(personName, personEmail, personId, urlPhoto);
                processLoginByGoogle(user);
                Log.d(TAG, personName + "\n" + personId + "\n" + personEmail);
            }

            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    private void processLoginByGoogle(User user) {
        DataService dataService = APIService.getService();
        Call<String> callback = dataService.loginByGoogle(user.getUser_name(), user.getUser_email(), user.getUser_IdGoogle(), user.getUser_url_image());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String respon = response.body();
                if(respon != null && respon.equalsIgnoreCase("Success")) {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                    AccountFragment accountFragment = (AccountFragment) myViewPagerAdapter.getFragment(2);
                    accountFragment.updateUI(user);
//                    myViewPagerAdapter.notifyItemChanged(2, null);
                    SharedPreferences sharedPreferences = getSharedPreferences("DataUser", MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    prefsEditor.putString("User", json);
                    prefsEditor.apply();



                } else {
                    Toast.makeText(MainActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, respon.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_LONG).show();
                Log.d(TAG, t.toString());
            }
        });
    }
}