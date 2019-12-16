package com.adgameampo.androidapp.MainPage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adgameampo.androidapp.FarsiNumberChange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.adgameampo.androidapp.R;

import co.ronash.pushe.Pushe;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ExplainVideoActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    Typeface font_Medium;
    Typeface font_Bold;

    BottomNavigationView bottomNavigationView;

    TextView HeaderExplainVideo;
    TextView TextViewTitleExplain;
    TextView TextViewDesExplain;
    TextView TextViewMoneyFarsi;
    TextView TextViewMoneyEnglish;
    TextView TextViewTitleMessage;
    TextView TextViewTextMessage;
    TextView TextViewNextVideo;
    TextView TextViewReplayVideo;

    public TextView TextViewTimeVideoCode;

    ConstraintLayout ConstraintMoney;
    LinearLayout LinearMessage;

    ImageView ImageExplain;
    ImageView ImageViewAccount;
    ImageView ImageViewBack;
    ImageView NextVideoButton;
    ImageView ReplayVideoButton;

    Button CashButton;
    Button MessageButton;
    Button SiteLinkButton;
    Button IOSAppDownloadButton;
    Button AndroidAppDownloadButton;


    public Dialog loading;
    public Dialog ActivityDialogVideoCode;

    CountDownTimer countDownTimer;

    long  timeLeftInMiliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler,new IntentFilter("com.adgameampo.androidapp_FCM-MESSAGE"));
        setContentView(R.layout.activity_explain_video);

        context=getApplicationContext();

        setStatusBarGradiant(ExplainVideoActivity.this);

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation_view);
        HeaderExplainVideo=(TextView) findViewById(R.id.HeaderExplainVideo);
        ImageExplain=(ImageView) findViewById(R.id.ImageExplain);
        ImageViewAccount=(ImageView) findViewById(R.id.ImageViewAccount);
        ImageViewBack=(ImageView) findViewById(R.id.ImageViewBack);
        NextVideoButton=(ImageView) findViewById(R.id.NextVideoButton);
        ReplayVideoButton=(ImageView) findViewById(R.id.ReplayVideoButton);
        CashButton=(Button) findViewById(R.id.CashButton);
        MessageButton=(Button) findViewById(R.id.MessageButton);
        SiteLinkButton=(Button) findViewById(R.id.SiteLinkButton);
        IOSAppDownloadButton=(Button) findViewById(R.id.IOSAppDownloadButton);
        AndroidAppDownloadButton=(Button) findViewById(R.id.AndroidAppDownloadButton);
        TextViewTitleExplain=(TextView) findViewById(R.id.TextViewTitleExplain);
        TextViewDesExplain=(TextView) findViewById(R.id.TextViewDesExplain);
        TextViewMoneyFarsi=(TextView)findViewById(R.id.TextViewMoneyFarsi);
        TextViewMoneyEnglish=(TextView)findViewById(R.id.TextViewMoneyEnglish);
        TextViewTitleMessage=(TextView)findViewById(R.id.TextViewTitleMessage);
        TextViewTextMessage=(TextView)findViewById(R.id.TextViewTextMessage);
        TextViewNextVideo=(TextView)findViewById(R.id.TextViewNextVideo);
        TextViewReplayVideo=(TextView)findViewById(R.id.TextViewReplayVideo);
        LinearMessage=(LinearLayout) findViewById(R.id.LinearMessage);
        ConstraintMoney=(ConstraintLayout) findViewById(R.id.ConstraintMoney);

        bottomNavigationView.setSelectedItemId(R.id.navigation_video);

        font_Medium= Typeface.createFromAsset(ExplainVideoActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(ExplainVideoActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");

        HeaderExplainVideo.setTypeface(font_Bold);
        CashButton.setTypeface(font_Bold);
        MessageButton.setTypeface(font_Bold);
        SiteLinkButton.setTypeface(font_Bold);
        IOSAppDownloadButton.setTypeface(font_Bold);
        AndroidAppDownloadButton.setTypeface(font_Bold);
        TextViewTitleExplain.setTypeface(font_Bold);
        TextViewDesExplain.setTypeface(font_Bold);
        TextViewMoneyFarsi.setTypeface(font_Bold);
        TextViewTitleMessage.setTypeface(font_Bold);
        TextViewTextMessage.setTypeface(font_Bold);
        TextViewNextVideo.setTypeface(font_Bold);
        TextViewReplayVideo.setTypeface(font_Bold);
//        TextViewMoneyEnglish.setTypeface(font_Bold);

        Pushe.initialize(this,true);

        int money = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getInt("money",0);
        String money_string=String.valueOf(money);
        String money_farsi=FarsiNumberChange.changefarsi(money_string);
        TextViewMoneyFarsi.setText(money_farsi);
        TextViewMoneyEnglish.setText(money_string);

        String Title = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("title_message","خــالی");
        String Message = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("text_message","در حال حاضر هیچ پیامی وجود ندارد.");

        TextViewTitleMessage.setText(Title);
        TextViewTextMessage.setText(Message);


        LinearMessage.setVisibility(View.GONE);
        ConstraintMoney.setVisibility(View.GONE);


//        final Animation SlideOpenMessage= AnimationUtils.loadAnimation(this,R.anim.slide_open_message);
        final Animation SlideOpenMessageFirst= AnimationUtils.loadAnimation(this,R.anim.slide_open_message_first);
//        final Animation SlideOpenButton= AnimationUtils.loadAnimation(this,R.anim.slide_open_button);
        final Animation SlideOpenButtonFirst= AnimationUtils.loadAnimation(this,R.anim.slide_open_button_first);
        final Animation SlideCloseButton= AnimationUtils.loadAnimation(this,R.anim.slide_close_button);
        final Animation SlideCloseButtonFirst= AnimationUtils.loadAnimation(this,R.anim.slide_close_button_first);
        final Animation SlideCloseMessage= AnimationUtils.loadAnimation(this,R.anim.slide_close_message);
        final Animation SlideCloseMessageFirst= AnimationUtils.loadAnimation(this,R.anim.slide_close_message_first);

//        final Animation SlideOpenMoney= AnimationUtils.loadAnimation(this,R.anim.slide_open_money);
        final Animation SlideOpenMoneyFirst= AnimationUtils.loadAnimation(this,R.anim.slide_open_money_first);
//        final Animation SlideOpenButtonMoney= AnimationUtils.loadAnimation(this,R.anim.slide_open_button_money);
        final Animation SlideOpenButtonMoneyFirst= AnimationUtils.loadAnimation(this,R.anim.slide_open_button_money_first);
        final Animation SlideCloseButtonMoney= AnimationUtils.loadAnimation(this,R.anim.slide_close_button_money);
        final Animation SlideCloseButtonMoneyFirst= AnimationUtils.loadAnimation(this,R.anim.slide_close_button_money_first);
        final Animation SlideCloseMoney= AnimationUtils.loadAnimation(this,R.anim.slide_close_money);
        final Animation SlideCloseMoneyFirst= AnimationUtils.loadAnimation(this,R.anim.slide_close_money_first);


        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View view = bottomNavigationMenuView.getChildAt(1);

        view.setBackgroundResource(R.drawable.video_bg);

        for (int i = 0; i <bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            SpannableStringBuilder spannableTitle = new SpannableStringBuilder(menuItem.getTitle());
            spannableTitle.setSpan(new CustomTypefaceSpan("" , font_Bold), 0 , spannableTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            menuItem.setTitle(spannableTitle);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.navigation_guide)
                {
                    startActivity(new Intent(getBaseContext(), GuideActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
//                if (menuItem.getItemId()==R.id.navigation_check_out)
//                {
//                    startActivity(new Intent(getBaseContext(), CheckOutActivity.class));
//                    overridePendingTransition(0, 0);
//                    finish();
//                }
                if (menuItem.getItemId()==R.id.navigation_list)
                {
                    Intent intent=new Intent(ExplainVideoActivity.this,ListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
//                if (menuItem.getItemId()==R.id.navigation_advertising)
//                {
//                    startActivity(new Intent(getBaseContext(), AdvertisingActivity.class));
//                    overridePendingTransition(0, 0);
//                    finish();
//                }
                return false;
            }
        });

        try {
            if (VideoActivity.VideoAndroid.equals(""))
            {
                AndroidAppDownloadButton.setVisibility(View.INVISIBLE);
            }
            else
            {
                AndroidAppDownloadButton.setVisibility(View.VISIBLE);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            if (VideoActivity.VideoIOS.equals(""))
            {
                IOSAppDownloadButton.setVisibility(View.INVISIBLE);
            }
            else
            {
                IOSAppDownloadButton.setVisibility(View.VISIBLE);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            if (VideoActivity.VideoSite.equals(""))
            {
                SiteLinkButton.setVisibility(View.INVISIBLE);
            }
            else
            {
                SiteLinkButton.setVisibility(View.VISIBLE);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        CashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ConstraintMoney.getVisibility()==View.GONE)
                    {
                        CashButton.setEnabled(false);
                        ConstraintMoney.startAnimation(SlideOpenMoneyFirst);
                        CashButton.startAnimation(SlideOpenButtonMoneyFirst);
                        ConstraintMoney.setVisibility(View.VISIBLE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ConstraintMoney.startAnimation(SlideCloseMoneyFirst);
                                CashButton.startAnimation(SlideCloseButtonMoneyFirst);
                                CashButton.setEnabled(true);
                            }
                        }, 50);
                    }
                    else
                    {
                        CashButton.setEnabled(false);
                        ConstraintMoney.setVisibility(View.GONE);
                        ConstraintMoney.startAnimation(SlideCloseMoney);
                        CashButton.startAnimation(SlideCloseButtonMoney);
                        CashButton.setEnabled(true);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        MessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (LinearMessage.getVisibility()==View.GONE)
                    {
                        MessageButton.setEnabled(false);
                        LinearMessage.startAnimation(SlideOpenMessageFirst);
                        MessageButton.startAnimation(SlideOpenButtonFirst);
                        LinearMessage.setVisibility(View.VISIBLE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LinearMessage.startAnimation(SlideCloseMessageFirst);
                                MessageButton.startAnimation(SlideCloseButtonFirst);
                                MessageButton.setEnabled(true);
                            }
                        }, 50);

                    }
                    else
                    {
                        MessageButton.setEnabled(false);
                        LinearMessage.setVisibility(View.GONE);
                        LinearMessage.startAnimation(SlideCloseMessage);
                        MessageButton.startAnimation(SlideCloseButton);
                        MessageButton.setEnabled(true);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        try {
            if (loading.isShowing())
                loading.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        loading = new Dialog(ExplainVideoActivity.this);
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.loading_dialog);
        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        TextViewTitleExplain.setText(VideoActivity.VideoName);
        TextViewDesExplain.setText(VideoActivity.VideoDes);
        LoadImageFromUrl(VideoActivity.VideoImage);

        ImageViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(ExplainVideoActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                startActivity(new Intent(ExplainVideoActivity.this, AccountActivity.class));
            }
        });

        ImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(ExplainVideoActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });

        SiteLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(ExplainVideoActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    String url= VideoActivity.VideoSite;
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityIfNeeded(browserIntent,0);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        AndroidAppDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(ExplainVideoActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    String url= VideoActivity.VideoAndroid;
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityIfNeeded(browserIntent,0);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        IOSAppDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(ExplainVideoActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    String url= VideoActivity.VideoIOS;
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityIfNeeded(browserIntent,0);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        NextVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("is_replay_video",false).apply();
                    if (VideoActivity.VideoFrom.equals("country"))
                    {
                        try {
                            if (loading.isShowing())
                                loading.dismiss();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        loading = new Dialog(ExplainVideoActivity.this);
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.loading_dialog);
                        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loading.setCancelable(false);
                        loading.setCanceledOnTouchOutside(false);
                        loading.show();
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            long user_id = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",0);
                                            String UserIdString= String.valueOf(user_id);
                                            OkHttpClient client = new OkHttpClient();

                                            OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                            builder.connectTimeout(15, TimeUnit.SECONDS);
                                            builder.readTimeout(15, TimeUnit.SECONDS);
                                            builder.writeTimeout(15, TimeUnit.SECONDS);

                                            RequestBody body = new MultipartBody.Builder()
                                                    .setType(MultipartBody.FORM)
                                                    .addFormDataPart("user_id",UserIdString)
                                                    .build();

                                            Request request = new Request.Builder()
                                                    .url("http://adcompany.ir/api/video/country")
                                                    .post(body)
                                                    .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImY3ZGFmMmUzMWE4ZmFkZWYzZWU2ZTJjMzRhMjFjNTdkOGI3MjdhZjMxMGE1ZTJlZDM4NmQ4MDQxNDhlZTliNWU1NjVkODMwM2NkODkzYzE4In0.eyJhdWQiOiIxIiwianRpIjoiZjdkYWYyZTMxYThmYWRlZjNlZTZlMmMzNGEyMWM1N2Q4YjcyN2FmMzEwYTVlMmVkMzg2ZDgwNDE0OGVlOWI1ZTU2NWQ4MzAzY2Q4OTNjMTgiLCJpYXQiOjE1NTUwNjE2MjIsIm5iZiI6MTU1NTA2MTYyMiwiZXhwIjoxNTg2Njg0MDIyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.S30gaSmqSaK253snXsnhTvhYgQOcV46m1CY4X_ilRTE0k8OZFO9Dr5Ul3gzNMBpE0kg4j4HGEc4v-DE34n5QyBG-qvg4Hzg4E-G4Yse3ARE-Xam08nHo8055y0rvsEaKElzkccX8KbNeP4bnbK-Fb9J6sF1hOejYqoRxlf8NNAFDMrs2-mqHYWMB5AjZiGugeEIVZU9Aq1SL1KnDIQB-QXbI11yy6KaqZ1XoqvbPWFRSGuYTt_5rzOZhMyuF0EjBCwNTLzSa1WrhCAJimpQgAZljYVmoQsSgdHqQTgbqpYUztonBQ5AAHmcJkW1aEZbAXnDGpewIj2lxUZ_TQe9cAZPItnokor4clXfHHMei8iHK0xQ9v0Lg6I6Jh_M0u71CP-L6lLMgP7B3yhTWJ8PiKO7_NelsP12BNxKTbAj0gkLBEwBiVDPKFTfi7ad_SPwMIxOuFxQwDtFxxIxPoIdr8R0cino9WJzAed0-red6SgXA9JoebDggVSv2EfEI8S2HkMrkfcr_GMnNUSB5indO2bzysrwT_ijLo0ZaJ_DOOY45Q15KexLyl1mjXJt3_fiut-anBVDVewmqwLUXtK9MBcxYwBNPN7ghEogiw9wplrwjGn1D-psbaIGq2UrqZMzJS9msrvk5ygsRzIMyDy0VXUEwQni_KiySkcpev8MEodY")
                                                    .addHeader("Accept", "application/json")
                                                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                                    .build();

                                            client = builder.build();
                                            client.newCall(request).enqueue(new okhttp3.Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    Log.d("looooog", e.toString());
                                                    runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                            Toast.makeText(ExplainVideoActivity.this, "دوباره بزن تا برنده شی\n تا ۱۸:۱۸ وقت داری.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    String myResponce = response.body().string();
                                                    try {
                                                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                                        JsonParser jp = new JsonParser();
                                                        JsonElement je = jp.parse(myResponce);
                                                        myResponce = gson.toJson(je);
                                                    }catch (Exception e)
                                                    {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception e1)
                                                        {
                                                            e1.printStackTrace();
                                                        }
                                                    }
                                                    Log.d("looog", myResponce);
                                                    if (response.isSuccessful()) {
                                                        try {
                                                            JSONObject jObject = new JSONObject(myResponce);
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    if (!isFinishing()) {
                                                                        try {
                                                                            try {
                                                                                if (loading.isShowing())
                                                                                    loading.dismiss();
                                                                            }catch (Exception e)
                                                                            {
                                                                                e.printStackTrace();
                                                                            }
                                                                            int result= jObject.getInt("result");
                                                                            if (result==1)
                                                                            {
                                                                                JSONObject video= jObject.getJSONObject("video");
                                                                                try {
                                                                                    VideoActivity.VideoID= String.valueOf(video.getLong("id"));
                                                                                    VideoActivity.VideoLink=video.getString("video");
                                                                                    VideoActivity.VideoImage=video.getString("image");
                                                                                    VideoActivity.VideoName=video.getString("name");
                                                                                    VideoActivity.VideoDes=video.getString("description");
                                                                                }catch (Exception e)
                                                                                {
                                                                                    e.printStackTrace();
                                                                                }
                                                                                try {
                                                                                    VideoActivity.VideoAndroid=video.getString("android");
                                                                                }catch (Exception e)
                                                                                {
                                                                                    e.printStackTrace();
                                                                                }
                                                                                try {
                                                                                    VideoActivity.VideoIOS=video.getString("ios");
                                                                                }catch (Exception e)
                                                                                {
                                                                                    e.printStackTrace();
                                                                                }
                                                                                try {
                                                                                    VideoActivity.VideoSite=video.getString("site");
                                                                                }catch (Exception e)
                                                                                {
                                                                                    e.printStackTrace();
                                                                                }

                                                                                startActivity(new Intent(ExplainVideoActivity.this, PlayerVideoActivity.class));
                                                                                finish();
                                                                            }
                                                                            else if (result==-1)
                                                                            {
                                                                                try {
                                                                                    if (loading.isShowing())
                                                                                        loading.dismiss();
                                                                                }catch (Exception e)
                                                                                {
                                                                                    e.printStackTrace();
                                                                                }
                                                                                Toast.makeText(ExplainVideoActivity.this, "دوباره بزن تا برنده شی\n تا ۱۸:۱۸ وقت داری.", Toast.LENGTH_LONG).show();
                                                                            }

                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                        } catch (Exception e) {
                                                            runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    try {
                                                                        if (loading.isShowing())
                                                                            loading.dismiss();
                                                                    }catch (Exception e)
                                                                    {
                                                                        e.printStackTrace();
                                                                    }
                                                                    Toast.makeText(ExplainVideoActivity.this, "دوباره بزن تا برنده شی\n تا ۱۸:۱۸ وقت داری.", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    }
                                                    else
                                                    {
                                                        runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                try {
                                                                    if (loading.isShowing())
                                                                        loading.dismiss();
                                                                }catch (Exception e)
                                                                {
                                                                    e.printStackTrace();
                                                                }
                                                                Toast.makeText(ExplainVideoActivity.this, "دوباره بزن تا برنده شی\n تا ۱۸:۱۸ وقت داری.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        },3500);

                    }
                    else if (VideoActivity.VideoFrom.equals("state"))
                    {
                        try {
                            if (loading.isShowing())
                                loading.dismiss();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        loading = new Dialog(ExplainVideoActivity.this);
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.loading_dialog);
                        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loading.setCancelable(false);
                        loading.setCanceledOnTouchOutside(false);
                        loading.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    long user_id = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",0);
                                    String stateid = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("stateid","0");
                                    String UserIdString= String.valueOf(user_id);
                                    OkHttpClient client = new OkHttpClient();

                                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                    builder.connectTimeout(15, TimeUnit.SECONDS);
                                    builder.readTimeout(15, TimeUnit.SECONDS);
                                    builder.writeTimeout(15, TimeUnit.SECONDS);

                                    RequestBody body = new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("user_id",UserIdString)
                                            .addFormDataPart("state_id", String.valueOf(stateid))
                                            .build();

                                    Request request = new Request.Builder()
                                            .url("http://adcompany.ir/api/video/state")
                                            .post(body)
                                            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImY3ZGFmMmUzMWE4ZmFkZWYzZWU2ZTJjMzRhMjFjNTdkOGI3MjdhZjMxMGE1ZTJlZDM4NmQ4MDQxNDhlZTliNWU1NjVkODMwM2NkODkzYzE4In0.eyJhdWQiOiIxIiwianRpIjoiZjdkYWYyZTMxYThmYWRlZjNlZTZlMmMzNGEyMWM1N2Q4YjcyN2FmMzEwYTVlMmVkMzg2ZDgwNDE0OGVlOWI1ZTU2NWQ4MzAzY2Q4OTNjMTgiLCJpYXQiOjE1NTUwNjE2MjIsIm5iZiI6MTU1NTA2MTYyMiwiZXhwIjoxNTg2Njg0MDIyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.S30gaSmqSaK253snXsnhTvhYgQOcV46m1CY4X_ilRTE0k8OZFO9Dr5Ul3gzNMBpE0kg4j4HGEc4v-DE34n5QyBG-qvg4Hzg4E-G4Yse3ARE-Xam08nHo8055y0rvsEaKElzkccX8KbNeP4bnbK-Fb9J6sF1hOejYqoRxlf8NNAFDMrs2-mqHYWMB5AjZiGugeEIVZU9Aq1SL1KnDIQB-QXbI11yy6KaqZ1XoqvbPWFRSGuYTt_5rzOZhMyuF0EjBCwNTLzSa1WrhCAJimpQgAZljYVmoQsSgdHqQTgbqpYUztonBQ5AAHmcJkW1aEZbAXnDGpewIj2lxUZ_TQe9cAZPItnokor4clXfHHMei8iHK0xQ9v0Lg6I6Jh_M0u71CP-L6lLMgP7B3yhTWJ8PiKO7_NelsP12BNxKTbAj0gkLBEwBiVDPKFTfi7ad_SPwMIxOuFxQwDtFxxIxPoIdr8R0cino9WJzAed0-red6SgXA9JoebDggVSv2EfEI8S2HkMrkfcr_GMnNUSB5indO2bzysrwT_ijLo0ZaJ_DOOY45Q15KexLyl1mjXJt3_fiut-anBVDVewmqwLUXtK9MBcxYwBNPN7ghEogiw9wplrwjGn1D-psbaIGq2UrqZMzJS9msrvk5ygsRzIMyDy0VXUEwQni_KiySkcpev8MEodY")
                                            .addHeader("Accept", "application/json")
                                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                            .build();

                                    client = builder.build();
                                    client.newCall(request).enqueue(new okhttp3.Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.d("looooog", e.toString());
                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                    Toast.makeText(ExplainVideoActivity.this, "دوباره بزن تا برنده شی\n تا ۱۸:۱۸ وقت داری.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String myResponce = response.body().string();
                                            try {
                                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                                JsonParser jp = new JsonParser();
                                                JsonElement je = jp.parse(myResponce);
                                                myResponce = gson.toJson(je);
                                            }catch (Exception e)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception e1)
                                                {
                                                    e1.printStackTrace();
                                                }
                                            }
                                            Log.d("looog", myResponce);
                                            if (response.isSuccessful()) {
                                                try {
                                                    JSONObject jObject = new JSONObject(myResponce);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (!isFinishing()) {
                                                                try {
                                                                    try {
                                                                        if (loading.isShowing())
                                                                            loading.dismiss();
                                                                    }catch (Exception e)
                                                                    {
                                                                        e.printStackTrace();
                                                                    }
                                                                    int result= jObject.getInt("result");
                                                                    if (result==1)
                                                                    {
                                                                        JSONObject video= jObject.getJSONObject("video");
                                                                        try {
                                                                            VideoActivity.VideoID= String.valueOf(video.getLong("id"));
                                                                            VideoActivity.VideoLink=video.getString("video");
                                                                            VideoActivity.VideoImage=video.getString("image");
                                                                            VideoActivity.VideoName=video.getString("name");
                                                                            VideoActivity.VideoDes=video.getString("description");
                                                                        }catch (Exception e)
                                                                        {
                                                                            e.printStackTrace();
                                                                        }
                                                                        try {
                                                                            VideoActivity.VideoAndroid=video.getString("android");
                                                                        }catch (Exception e)
                                                                        {
                                                                            e.printStackTrace();
                                                                        }
                                                                        try {
                                                                            VideoActivity.VideoIOS=video.getString("ios");
                                                                        }catch (Exception e)
                                                                        {
                                                                            e.printStackTrace();
                                                                        }
                                                                        try {
                                                                            VideoActivity.VideoSite=video.getString("site");
                                                                        }catch (Exception e)
                                                                        {
                                                                            e.printStackTrace();
                                                                        }

                                                                        startActivity(new Intent(ExplainVideoActivity.this, PlayerVideoActivity.class));
                                                                        finish();
                                                                    }
                                                                    else if (result==-1)
                                                                    {
                                                                        Toast.makeText(ExplainVideoActivity.this, "دوباره بزن تا برنده شی\n تا ۱۸:۱۸ وقت داری.", Toast.LENGTH_LONG).show();
                                                                    }

                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                    });
                                                } catch (Exception e) {
                                                    runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                            Toast.makeText(ExplainVideoActivity.this, "دوباره بزن تا برنده شی\n تا ۱۸:۱۸ وقت داری.", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            }
                                            else
                                            {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }
                                                        Toast.makeText(ExplainVideoActivity.this, "دوباره بزن تا برنده شی\n تا ۱۸:۱۸ وقت داری.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    else if (VideoActivity.VideoFrom.equals("city"))
                    {
                        try {
                            if (loading.isShowing())
                                loading.dismiss();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        loading = new Dialog(ExplainVideoActivity.this);
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.loading_dialog);
                        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loading.setCancelable(false);
                        loading.setCanceledOnTouchOutside(false);
                        loading.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    long user_id = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",0);
                                    String cityid = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("cityid","0");
                                    String UserIdString= String.valueOf(user_id);
                                    OkHttpClient client = new OkHttpClient();

                                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                    builder.connectTimeout(15, TimeUnit.SECONDS);
                                    builder.readTimeout(15, TimeUnit.SECONDS);
                                    builder.writeTimeout(15, TimeUnit.SECONDS);

                                    RequestBody body = new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("user_id",UserIdString)
                                            .addFormDataPart("city_id", String.valueOf(cityid))
                                            .build();

                                    Request request = new Request.Builder()
                                            .url("http://adcompany.ir/api/video/city")
                                            .post(body)
                                            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImY3ZGFmMmUzMWE4ZmFkZWYzZWU2ZTJjMzRhMjFjNTdkOGI3MjdhZjMxMGE1ZTJlZDM4NmQ4MDQxNDhlZTliNWU1NjVkODMwM2NkODkzYzE4In0.eyJhdWQiOiIxIiwianRpIjoiZjdkYWYyZTMxYThmYWRlZjNlZTZlMmMzNGEyMWM1N2Q4YjcyN2FmMzEwYTVlMmVkMzg2ZDgwNDE0OGVlOWI1ZTU2NWQ4MzAzY2Q4OTNjMTgiLCJpYXQiOjE1NTUwNjE2MjIsIm5iZiI6MTU1NTA2MTYyMiwiZXhwIjoxNTg2Njg0MDIyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.S30gaSmqSaK253snXsnhTvhYgQOcV46m1CY4X_ilRTE0k8OZFO9Dr5Ul3gzNMBpE0kg4j4HGEc4v-DE34n5QyBG-qvg4Hzg4E-G4Yse3ARE-Xam08nHo8055y0rvsEaKElzkccX8KbNeP4bnbK-Fb9J6sF1hOejYqoRxlf8NNAFDMrs2-mqHYWMB5AjZiGugeEIVZU9Aq1SL1KnDIQB-QXbI11yy6KaqZ1XoqvbPWFRSGuYTt_5rzOZhMyuF0EjBCwNTLzSa1WrhCAJimpQgAZljYVmoQsSgdHqQTgbqpYUztonBQ5AAHmcJkW1aEZbAXnDGpewIj2lxUZ_TQe9cAZPItnokor4clXfHHMei8iHK0xQ9v0Lg6I6Jh_M0u71CP-L6lLMgP7B3yhTWJ8PiKO7_NelsP12BNxKTbAj0gkLBEwBiVDPKFTfi7ad_SPwMIxOuFxQwDtFxxIxPoIdr8R0cino9WJzAed0-red6SgXA9JoebDggVSv2EfEI8S2HkMrkfcr_GMnNUSB5indO2bzysrwT_ijLo0ZaJ_DOOY45Q15KexLyl1mjXJt3_fiut-anBVDVewmqwLUXtK9MBcxYwBNPN7ghEogiw9wplrwjGn1D-psbaIGq2UrqZMzJS9msrvk5ygsRzIMyDy0VXUEwQni_KiySkcpev8MEodY")
                                            .addHeader("Accept", "application/json")
                                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                            .build();

                                    client = builder.build();
                                    client.newCall(request).enqueue(new okhttp3.Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.d("looooog", e.toString());
                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                    Toast.makeText(ExplainVideoActivity.this, "دوباره بزن تا برنده شی\n تا ۱۸:۱۸ وقت داری.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String myResponce = response.body().string();
                                            try {
                                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                                JsonParser jp = new JsonParser();
                                                JsonElement je = jp.parse(myResponce);
                                                myResponce = gson.toJson(je);
                                            }catch (Exception e)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception e1)
                                                {
                                                    e1.printStackTrace();
                                                }
                                            }
                                            Log.d("looog", myResponce);
                                            if (response.isSuccessful()) {
                                                try {
                                                    JSONObject jObject = new JSONObject(myResponce);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (!isFinishing()) {
                                                                try {
                                                                    try {
                                                                        if (loading.isShowing())
                                                                            loading.dismiss();
                                                                    }catch (Exception e)
                                                                    {
                                                                        e.printStackTrace();
                                                                    }
                                                                    int result= jObject.getInt("result");
                                                                    if (result==1)
                                                                    {
                                                                        JSONObject video= jObject.getJSONObject("video");
                                                                        try {
                                                                            VideoActivity.VideoID= String.valueOf(video.getLong("id"));
                                                                            VideoActivity.VideoLink=video.getString("video");
                                                                            VideoActivity.VideoImage=video.getString("image");
                                                                            VideoActivity.VideoName=video.getString("name");
                                                                            VideoActivity.VideoDes=video.getString("description");
                                                                        }catch (Exception e)
                                                                        {
                                                                            e.printStackTrace();
                                                                        }
                                                                        try {
                                                                            VideoActivity.VideoAndroid=video.getString("android");
                                                                        }catch (Exception e)
                                                                        {
                                                                            e.printStackTrace();
                                                                        }
                                                                        try {
                                                                            VideoActivity.VideoIOS=video.getString("ios");
                                                                        }catch (Exception e)
                                                                        {
                                                                            e.printStackTrace();
                                                                        }
                                                                        try {
                                                                            VideoActivity.VideoSite=video.getString("site");
                                                                        }catch (Exception e)
                                                                        {
                                                                            e.printStackTrace();
                                                                        }
                                                                        startActivity(new Intent(ExplainVideoActivity.this, PlayerVideoActivity.class));
                                                                        finish();
                                                                    }
                                                                    else if (result==-1)
                                                                    {
                                                                        Toast.makeText(ExplainVideoActivity.this, "دوباره بزن تا برنده شی\n تا ۱۸:۱۸ وقت داری.", Toast.LENGTH_LONG).show();
                                                                    }

                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                    });
                                                } catch (Exception e) {
                                                    runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                            Toast.makeText(ExplainVideoActivity.this, "دوباره بزن تا برنده شی\n تا ۱۸:۱۸ وقت داری.", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            }
                                            else
                                            {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }
                                                        Toast.makeText(ExplainVideoActivity.this, "دوباره بزن تا برنده شی\n تا ۱۸:۱۸ وقت داری.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        boolean notification = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isnotification",false);
        if(notification)
        {
            LinearMessage.setVisibility(View.VISIBLE);
            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("isnotification",false).apply();
        }

        ReplayVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("is_replay_video",true).apply();
                Intent intent = new Intent(getBaseContext(), PlayerVideoActivity.class);
                startActivity(intent);
                finish();
            }
        });
        boolean isReplay = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("is_replay_video",false);
        if (!isReplay)
        {
            ActivityDialogShowVideoCode();
        }
        else
            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("is_replay_video",false).apply();
    }
    private void LoadImageFromUrl(String url)
    {
        try {
            Picasso.with(this).load(url).placeholder(R.drawable.ad)
                    .error(R.drawable.ad)
                    .into(ImageExplain, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("looog", "onSuccess: ");
                            try {
                                if (loading.isShowing())
                                    loading.dismiss();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError() {
                            Log.d("loog", "onError: ");
                            try {
                                if (loading.isShowing())
                                    loading.dismiss();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            Toast.makeText(ExplainVideoActivity.this, "اتصال در هنگام باز کردن تصویر با مشکل مواجه شد.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ExplainVideoActivity.this, VideoActivity.class));
        finish();
    }
    public static void setStatusBarGradiant(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                Drawable background = activity.getResources().getDrawable(R.drawable.state_bar_bg);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
                window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
                window.setBackgroundDrawable(background);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void ActivityDialogShowVideoCode()
    {
        try {
            ActivityDialogVideoCode=new Dialog(ExplainVideoActivity.this);
            ActivityDialogVideoCode.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(ActivityDialogVideoCode.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ActivityDialogVideoCode.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
            ActivityDialogVideoCode.setContentView(R.layout.video_code_dialog);

            ImageView OkButtonVideoCode = (ImageView)ActivityDialogVideoCode.findViewById(R.id.OkButtonVideoCode);
            TextView TitleVideoCode=(TextView)ActivityDialogVideoCode.findViewById(R.id.TitleVideoCode);
            TextViewTimeVideoCode=(TextView)ActivityDialogVideoCode.findViewById(R.id.TextViewTimeVideoCode);
            EditText EditTextCodeVideo=(EditText)ActivityDialogVideoCode.findViewById(R.id.EditTextCodeVideo);

            ActivityDialogVideoCode.setCancelable(false);

            TitleVideoCode.setTypeface(font_Bold);
            TextViewTimeVideoCode.setTypeface(font_Bold);
            EditTextCodeVideo.setTypeface(font_Bold);

            Random r = new Random();
            int low = 1001;
            int high = 9999;
            int result = r.nextInt(high-low) + low;

            TitleVideoCode.setText(String.valueOf(result));

            OkButtonVideoCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TitleVideoCode.getText().toString().equals(EditTextCodeVideo.getText().toString()))
                    {
                        EditTextCodeVideo.setError("کد اشتباه است");
                        EditTextCodeVideo.requestFocus();
                    }
                    else
                    {
                        if (ActivityDialogVideoCode.isShowing())
                            ActivityDialogVideoCode.dismiss();
                        if (SiteLinkButton.getVisibility()==View.VISIBLE)
                        {
                            VisitVideo();
                        }
                        else
                        {
                            VisitVideo();
                        }
                    }
                }
            });
            StartTimer();
            ActivityDialogVideoCode.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void StartTimer ()
    {
        try {
            timeLeftInMiliseconds=30000;
            countDownTimer = new CountDownTimer(timeLeftInMiliseconds,1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    try {
                        timeLeftInMiliseconds=millisUntilFinished;

                        int CurrentTimeInt=(int)(timeLeftInMiliseconds/1000);
                        String CurrentTimeString = String.valueOf(CurrentTimeInt);

                        if (CurrentTimeString.contains("0"))
                            CurrentTimeString=CurrentTimeString.replaceAll("0","۰");
                        if (CurrentTimeString.contains("1"))
                            CurrentTimeString=CurrentTimeString.replaceAll("1","۱");
                        if (CurrentTimeString.contains("2"))
                            CurrentTimeString=CurrentTimeString.replaceAll("2","۲");
                        if (CurrentTimeString.contains("3"))
                            CurrentTimeString=CurrentTimeString.replaceAll("3","۳");
                        if (CurrentTimeString.contains("4"))
                            CurrentTimeString=CurrentTimeString.replaceAll("4","۴");
                        if (CurrentTimeString.contains("5"))
                            CurrentTimeString=CurrentTimeString.replaceAll("5","۵");
                        if (CurrentTimeString.contains("6"))
                            CurrentTimeString=CurrentTimeString.replaceAll("6","۶");
                        if (CurrentTimeString.contains("7"))
                            CurrentTimeString=CurrentTimeString.replaceAll("7","۷");
                        if (CurrentTimeString.contains("8"))
                            CurrentTimeString=CurrentTimeString.replaceAll("8","۸");
                        if (CurrentTimeString.contains("9"))
                            CurrentTimeString=CurrentTimeString.replaceAll("9","۹");

                        TextViewTimeVideoCode.setText(CurrentTimeString + " ثانیه");
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onFinish() {
                    try {
                        countDownTimer.cancel();
                        if (ActivityDialogVideoCode.isShowing())
                            ActivityDialogVideoCode.dismiss();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void VisitVideo()
    {
        try {
            if (loading.isShowing())
                loading.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        loading = new Dialog(ExplainVideoActivity.this);
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.loading_dialog);
        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long user_id = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",0);
                    String UserIdString= String.valueOf(user_id);
                    OkHttpClient client = new OkHttpClient();

                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.connectTimeout(15, TimeUnit.SECONDS);
                    builder.readTimeout(15, TimeUnit.SECONDS);
                    builder.writeTimeout(15, TimeUnit.SECONDS);

                    RequestBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("user_id",UserIdString)
                            .addFormDataPart("video_id", VideoActivity.VideoID)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://adcompany.ir/api/video/visit")
                            .post(body)
                            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImY3ZGFmMmUzMWE4ZmFkZWYzZWU2ZTJjMzRhMjFjNTdkOGI3MjdhZjMxMGE1ZTJlZDM4NmQ4MDQxNDhlZTliNWU1NjVkODMwM2NkODkzYzE4In0.eyJhdWQiOiIxIiwianRpIjoiZjdkYWYyZTMxYThmYWRlZjNlZTZlMmMzNGEyMWM1N2Q4YjcyN2FmMzEwYTVlMmVkMzg2ZDgwNDE0OGVlOWI1ZTU2NWQ4MzAzY2Q4OTNjMTgiLCJpYXQiOjE1NTUwNjE2MjIsIm5iZiI6MTU1NTA2MTYyMiwiZXhwIjoxNTg2Njg0MDIyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.S30gaSmqSaK253snXsnhTvhYgQOcV46m1CY4X_ilRTE0k8OZFO9Dr5Ul3gzNMBpE0kg4j4HGEc4v-DE34n5QyBG-qvg4Hzg4E-G4Yse3ARE-Xam08nHo8055y0rvsEaKElzkccX8KbNeP4bnbK-Fb9J6sF1hOejYqoRxlf8NNAFDMrs2-mqHYWMB5AjZiGugeEIVZU9Aq1SL1KnDIQB-QXbI11yy6KaqZ1XoqvbPWFRSGuYTt_5rzOZhMyuF0EjBCwNTLzSa1WrhCAJimpQgAZljYVmoQsSgdHqQTgbqpYUztonBQ5AAHmcJkW1aEZbAXnDGpewIj2lxUZ_TQe9cAZPItnokor4clXfHHMei8iHK0xQ9v0Lg6I6Jh_M0u71CP-L6lLMgP7B3yhTWJ8PiKO7_NelsP12BNxKTbAj0gkLBEwBiVDPKFTfi7ad_SPwMIxOuFxQwDtFxxIxPoIdr8R0cino9WJzAed0-red6SgXA9JoebDggVSv2EfEI8S2HkMrkfcr_GMnNUSB5indO2bzysrwT_ijLo0ZaJ_DOOY45Q15KexLyl1mjXJt3_fiut-anBVDVewmqwLUXtK9MBcxYwBNPN7ghEogiw9wplrwjGn1D-psbaIGq2UrqZMzJS9msrvk5ygsRzIMyDy0VXUEwQni_KiySkcpev8MEodY")
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .build();

                    client = builder.build();
                    client.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("looooog", e.toString());
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    VisitVideo();
                                    Toast.makeText(ExplainVideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String myResponce = response.body().string();
                            try {
                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                JsonParser jp = new JsonParser();
                                JsonElement je = jp.parse(myResponce);
                                myResponce = gson.toJson(je);
                            }catch (Exception e)
                            {
                                try {
                                    if (loading.isShowing())
                                        loading.dismiss();
                                }catch (Exception e1)
                                {
                                    e1.printStackTrace();
                                }
                            }
                            Log.d("looog", myResponce);
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jObject = new JSONObject(myResponce);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!isFinishing()) {
                                                try {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                    int score = jObject.getInt("score");
                                                    int user_score = jObject.getInt("user_score");
                                                    if (ConstraintMoney.getVisibility() == View.GONE)
                                                        CashButton.callOnClick();
                                                    final Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                int money = Integer.parseInt(TextViewMoneyEnglish.getText().toString());
                                                                ValueAnimator animator = ValueAnimator.ofInt(money, user_score);
                                                                animator.setDuration(2000); //Duration is in milliseconds
                                                                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                                    public void onAnimationUpdate(ValueAnimator animation) {
                                                                        TextViewMoneyEnglish.setText(animation.getAnimatedValue().toString());
                                                                        String money_farsi=FarsiNumberChange.changefarsi(TextViewMoneyEnglish.getText().toString());
                                                                        TextViewMoneyFarsi.setText(money_farsi);

                                                                    }
                                                                });
                                                                animator.addListener(new AnimatorListenerAdapter()
                                                                {
                                                                    @Override
                                                                    public void onAnimationEnd(Animator animation)
                                                                    {
                                                                        final Handler handler1 = new Handler();
                                                                        handler1.postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                try {
                                                                                    if (ConstraintMoney.getVisibility() == View.VISIBLE)
                                                                                        CashButton.callOnClick();

                                                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putInt("money",user_score).apply();
                                                                                    int money = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getInt("money",0);
                                                                                    String money_string=String.valueOf(money);
                                                                                    String money_farsi=FarsiNumberChange.changefarsi(money_string);
                                                                                    TextViewMoneyFarsi.setText(money_farsi);
                                                                                    TextViewMoneyEnglish.setText(money_string);

                                                                                }catch (Exception e)
                                                                                {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        },1000);
                                                                    }
                                                                });
                                                                animator.start();
                                                            }catch (Exception e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    },2000);
                                                    countDownTimer.cancel();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                try {
                                    JSONObject jObject = new JSONObject(myResponce);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!isFinishing()) {
                                                try {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                    countDownTimer.cancel();
                                                    Toast.makeText(ExplainVideoActivity.this, "شما قبلا این ویدیو را مشاهده کرده اید", Toast.LENGTH_SHORT).show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String Title = intent.getStringExtra("title");
                String Message = intent.getStringExtra("message");

                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("title_message",Title).apply();
                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("text_message",Message).apply();

                TextViewTitleMessage.setText(Title);
                TextViewTextMessage.setText(Message);


                MessageButton.callOnClick();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mHandler);
    }
}
