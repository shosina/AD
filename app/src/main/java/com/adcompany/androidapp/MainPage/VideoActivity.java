package com.adcompany.androidapp.MainPage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcompany.androidapp.CantLoginActivity;
import com.adcompany.androidapp.FarsiNumberChange;

import com.adcompany.androidapp.MainActivity;
import com.adcompany.androidapp.ProfileActivity;
import com.adcompany.androidapp.SplashActivity;
import com.adcompany.androidapp.VerifyActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.adcompany.androidapp.R;

import co.ronash.pushe.Pushe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class VideoActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    BottomNavigationView bottomNavigationView;

    Button CashButton;
    public Button MessageButton;

    TextView HeaderPlayVideo;
    TextView YourCityButton;
    TextView YourStateButton;
    TextView CountryButton;
    TextView TextViewMoneyFarsi;
    TextView TextViewMoneyEnglish;
    TextView TextViewTitleMessage;
    TextView TextViewTextMessage;
    TextView TextViewVideoExplain;
    TextView TextViewVideoExplainPlay;
    TextView TextViewVideoExplainFollow;
    TextView TextViewInstaClick;

    ConstraintLayout ConstraintMoney;
    LinearLayout LinearMessage;
    LinearLayout LinearLayoutInstaClick;

    Typeface font_Medium;
    Typeface font_Bold;

    public static String VideoFrom;

    ImageView PlayeVideoImage;
    ImageView ImageViewAccount;
    ImageView ImageViewBack;

    public String StateName;
    public String CityName;

    public int State_id;
    public int City_id;

    public static String VideoID="";
    public static String VideoLink="";
    public static String VideoImage="";
    public static String VideoName="";
    public static String VideoDes="";
    public static String VideoAndroid="";
    public static String VideoIOS="";
    public static String VideoSite="";

    public Dialog loading;
    public Dialog ActivityDialogExit;
    public Dialog ActivityDialogInternetConnection;

    public boolean IsExit;

    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (ActivityDialogInternetConnection.isShowing())
                ActivityDialogInternetConnection.dismiss();
        }
        catch (Exception e)
        {
            try {
                if (loading.isShowing())
                    loading.dismiss();
            }catch (Exception el)
                {
                    el.printStackTrace();
                }
        }
        super.onStart();
        GetStateCityName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler,new IntentFilter("com.adcompany.androidapp_FCM-MESSAGE"));
        setContentView(R.layout.activity_video);

        context=getApplicationContext();

        IsExit=true;

        setStatusBarGradiant(VideoActivity.this);

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation_view);
        HeaderPlayVideo=(TextView) findViewById(R.id.HeaderPlayVideo);
        YourCityButton=(TextView) findViewById(R.id.YourCityButton);
        YourStateButton=(TextView) findViewById(R.id.YourStateButton);
        CountryButton=(TextView) findViewById(R.id.CountryButton);
        TextViewMoneyFarsi=(TextView)findViewById(R.id.TextViewMoneyFarsi);
        TextViewMoneyEnglish=(TextView)findViewById(R.id.TextViewMoneyEnglish);
        TextViewTitleMessage=(TextView)findViewById(R.id.TextViewTitleMessage);
        TextViewTextMessage=(TextView)findViewById(R.id.TextViewTextMessage);
        TextViewVideoExplain=(TextView)findViewById(R.id.TextViewVideoExplain);
        TextViewVideoExplainPlay=(TextView)findViewById(R.id.TextViewVideoExplainPlay);
        TextViewVideoExplainFollow=(TextView)findViewById(R.id.TextViewVideoExplainFollow);
        TextViewInstaClick=(TextView)findViewById(R.id.TextViewInstaClick);
        LinearMessage=(LinearLayout) findViewById(R.id.LinearMessage);
        LinearLayoutInstaClick=(LinearLayout) findViewById(R.id.LinearLayoutInstaClick);
        ConstraintMoney=(ConstraintLayout) findViewById(R.id.ConstraintMoney);
        PlayeVideoImage=(ImageView) findViewById(R.id.PlayeVideoImage);
        ImageViewAccount=(ImageView) findViewById(R.id.ImageViewAccount);
        ImageViewBack=(ImageView) findViewById(R.id.ImageViewBack);
        CashButton=(Button)findViewById(R.id.CashButton);
        MessageButton=(Button)findViewById(R.id.MessageButton);

        bottomNavigationView.setSelectedItemId(R.id.navigation_video);

        font_Medium= Typeface.createFromAsset(VideoActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(VideoActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");


        HeaderPlayVideo.setTypeface(font_Bold);
        YourCityButton.setTypeface(font_Bold);
        CountryButton.setTypeface(font_Bold);
        YourStateButton.setTypeface(font_Bold);
        CashButton.setTypeface(font_Bold);
        MessageButton.setTypeface(font_Bold);
        TextViewMoneyFarsi.setTypeface(font_Bold);
        TextViewTitleMessage.setTypeface(font_Bold);
        TextViewTextMessage.setTypeface(font_Bold);
        TextViewVideoExplain.setTypeface(font_Bold);
        TextViewVideoExplainPlay.setTypeface(font_Bold);
        TextViewVideoExplainFollow.setTypeface(font_Bold);
        TextViewInstaClick.setTypeface(font_Bold);
//        TextViewMoneyEnglish.setTypeface(font_Medium);

        Pushe.initialize(this,true);

        Pushe.subscribe(this,"0");

        Pushe.unsubscribe(this,"-1");

        long user_id = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",0);
        String UserIdString= String.valueOf(user_id);
        Pushe.subscribe(this,UserIdString);

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
                    IsExit=false;
                    finish();
                }
//                if (menuItem.getItemId()==R.id.navigation_check_out)
//                {
//                    startActivity(new Intent(getBaseContext(), CheckOutActivity.class));
//                    overridePendingTransition(0, 0);
//                    IsExit=false;
//                    finish();
//                }
                if (menuItem.getItemId()==R.id.navigation_list)
                {
                    Intent intent=new Intent(VideoActivity.this,ListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    IsExit = false;
                    finish();
                }
                if (menuItem.getItemId()==R.id.navigation_advertising)
                {
                    startActivity(new Intent(getBaseContext(), AdvertisingActivity.class));
                    overridePendingTransition(0, 0);
                    IsExit=false;
                    finish();
                }
                return false;
            }
        });

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

        LinearLayoutInstaClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(VideoActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    String url= "https://instagram.com/Adcompany.ir";
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityIfNeeded(browserIntent,0);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        YourCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(VideoActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    VideoFrom="city";
                    YourCityButton.setBackgroundResource(R.drawable.select_gradiant_left_bg);
                    YourStateButton.setBackgroundResource(R.color.Transparent);
                    CountryButton.setBackgroundResource(R.color.Transparent);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        YourStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(VideoActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    VideoFrom="state";
                    YourCityButton.setBackgroundResource(R.color.Transparent);
                    YourStateButton.setBackgroundResource(R.drawable.select_center_bg);
                    CountryButton.setBackgroundResource(R.color.Transparent);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        CountryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(VideoActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    VideoFrom="country";
                    YourCityButton.setBackgroundResource(R.color.Transparent);
                    YourStateButton.setBackgroundResource(R.color.Transparent);
                    CountryButton.setBackgroundResource(R.drawable.select_gradiant_right_bg);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        CountryButton.callOnClick();

        ImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VideoActivity.this, AccountActivity.class));
            }
        });

        PlayeVideoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(VideoActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("is_replay_video",false).apply();
                if (VideoFrom.equals("country"))
                {
                    try {
                        if (loading.isShowing())
                            loading.dismiss();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    try {
                        loading = new Dialog(VideoActivity.this);
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.loading_dialog);
                        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loading.setCancelable(false);
                        loading.setCanceledOnTouchOutside(false);
                        loading.show();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
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
                                client.newCall(request).enqueue(new Callback() {
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
                                                Toast.makeText(VideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
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
                                                                    VideoID= String.valueOf(video.getLong("id"));
                                                                    VideoLink=video.getString("video");
                                                                    VideoImage=video.getString("image");
                                                                    VideoName=video.getString("name");
                                                                    VideoDes=video.getString("description");
                                                                    VideoAndroid=video.getString("android");
                                                                    VideoIOS=video.getString("ios");
                                                                    VideoSite=video.getString("site");

                                                                    IsExit=false;
                                                                    Intent intent = new Intent(getBaseContext(), PlayerVideoActivity.class);
                                                                    intent.putExtra("isReplay", false);
                                                                    startActivity(intent);
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
                                                                    Toast.makeText(VideoActivity.this, "خطا در ویدیو ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
                                                                }

                                                            } catch (Exception e) {
                                                                try {
                                                                    if (loading.isShowing())
                                                                        loading.dismiss();
                                                                }catch (Exception el)
                                                                {
                                                                    el.printStackTrace();
                                                                }
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
                                                        Toast.makeText(VideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                        else
                                        {
                                            try {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }
                                                        Toast.makeText(VideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }catch (Exception e)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception el)
                                                {
                                                    el.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                try {
                                    if (loading.isShowing())
                                        loading.dismiss();
                                }catch (Exception el)
                                {
                                    el.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
                else if (VideoFrom.equals("state"))
                {
                    try {
                        if (loading.isShowing())
                            loading.dismiss();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    try {
                        loading = new Dialog(VideoActivity.this);
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.loading_dialog);
                        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loading.setCancelable(false);
                        loading.setCanceledOnTouchOutside(false);
                        loading.show();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
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
                                        .addFormDataPart("state_id", String.valueOf(State_id))
                                        .build();

                                Request request = new Request.Builder()
                                        .url("http://adcompany.ir/api/video/state")
                                        .post(body)
                                        .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImY3ZGFmMmUzMWE4ZmFkZWYzZWU2ZTJjMzRhMjFjNTdkOGI3MjdhZjMxMGE1ZTJlZDM4NmQ4MDQxNDhlZTliNWU1NjVkODMwM2NkODkzYzE4In0.eyJhdWQiOiIxIiwianRpIjoiZjdkYWYyZTMxYThmYWRlZjNlZTZlMmMzNGEyMWM1N2Q4YjcyN2FmMzEwYTVlMmVkMzg2ZDgwNDE0OGVlOWI1ZTU2NWQ4MzAzY2Q4OTNjMTgiLCJpYXQiOjE1NTUwNjE2MjIsIm5iZiI6MTU1NTA2MTYyMiwiZXhwIjoxNTg2Njg0MDIyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.S30gaSmqSaK253snXsnhTvhYgQOcV46m1CY4X_ilRTE0k8OZFO9Dr5Ul3gzNMBpE0kg4j4HGEc4v-DE34n5QyBG-qvg4Hzg4E-G4Yse3ARE-Xam08nHo8055y0rvsEaKElzkccX8KbNeP4bnbK-Fb9J6sF1hOejYqoRxlf8NNAFDMrs2-mqHYWMB5AjZiGugeEIVZU9Aq1SL1KnDIQB-QXbI11yy6KaqZ1XoqvbPWFRSGuYTt_5rzOZhMyuF0EjBCwNTLzSa1WrhCAJimpQgAZljYVmoQsSgdHqQTgbqpYUztonBQ5AAHmcJkW1aEZbAXnDGpewIj2lxUZ_TQe9cAZPItnokor4clXfHHMei8iHK0xQ9v0Lg6I6Jh_M0u71CP-L6lLMgP7B3yhTWJ8PiKO7_NelsP12BNxKTbAj0gkLBEwBiVDPKFTfi7ad_SPwMIxOuFxQwDtFxxIxPoIdr8R0cino9WJzAed0-red6SgXA9JoebDggVSv2EfEI8S2HkMrkfcr_GMnNUSB5indO2bzysrwT_ijLo0ZaJ_DOOY45Q15KexLyl1mjXJt3_fiut-anBVDVewmqwLUXtK9MBcxYwBNPN7ghEogiw9wplrwjGn1D-psbaIGq2UrqZMzJS9msrvk5ygsRzIMyDy0VXUEwQni_KiySkcpev8MEodY")
                                        .addHeader("Accept", "application/json")
                                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                        .build();

                                client = builder.build();
                                client.newCall(request).enqueue(new Callback() {
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
                                                Toast.makeText(VideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
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
                                                                    VideoID= String.valueOf(video.getLong("id"));
                                                                    VideoLink=video.getString("video");
                                                                    VideoImage=video.getString("image");
                                                                    VideoName=video.getString("name");
                                                                    VideoDes=video.getString("description");
                                                                    VideoAndroid=video.getString("android");
                                                                    VideoIOS=video.getString("ios");
                                                                    VideoSite=video.getString("site");

                                                                    IsExit=false;
                                                                    Intent intent = new Intent(getBaseContext(), PlayerVideoActivity.class);
                                                                    intent.putExtra("isReplay", false);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                                else if (result==-1)
                                                                {
                                                                    Toast.makeText(VideoActivity.this, "خطا در ویدیو ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
                                                                }

                                                            } catch (Exception e) {
                                                                try {
                                                                    if (loading.isShowing())
                                                                        loading.dismiss();
                                                                }catch (Exception el)
                                                                {
                                                                    el.printStackTrace();
                                                                }
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
                                                        Toast.makeText(VideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                        else
                                        {
                                            try {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }
                                                        Toast.makeText(VideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }catch (Exception e)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception el)
                                                {
                                                    el.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                try {
                                    if (loading.isShowing())
                                        loading.dismiss();
                                }catch (Exception el)
                                {
                                    el.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
                else if (VideoFrom.equals("city"))
                {
                    try {
                        if (loading.isShowing())
                            loading.dismiss();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    try {
                        loading = new Dialog(VideoActivity.this);
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.loading_dialog);
                        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loading.setCancelable(false);
                        loading.setCanceledOnTouchOutside(false);
                        loading.show();
                    }catch (Exception e)
                    {
                        try {
                            if (loading.isShowing())
                                loading.dismiss();
                        }catch (Exception el)
                        {
                            el.printStackTrace();
                        }
                    }
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
                                        .addFormDataPart("city_id", String.valueOf(City_id))
                                        .build();

                                Request request = new Request.Builder()
                                        .url("http://adcompany.ir/api/video/city")
                                        .post(body)
                                        .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImY3ZGFmMmUzMWE4ZmFkZWYzZWU2ZTJjMzRhMjFjNTdkOGI3MjdhZjMxMGE1ZTJlZDM4NmQ4MDQxNDhlZTliNWU1NjVkODMwM2NkODkzYzE4In0.eyJhdWQiOiIxIiwianRpIjoiZjdkYWYyZTMxYThmYWRlZjNlZTZlMmMzNGEyMWM1N2Q4YjcyN2FmMzEwYTVlMmVkMzg2ZDgwNDE0OGVlOWI1ZTU2NWQ4MzAzY2Q4OTNjMTgiLCJpYXQiOjE1NTUwNjE2MjIsIm5iZiI6MTU1NTA2MTYyMiwiZXhwIjoxNTg2Njg0MDIyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.S30gaSmqSaK253snXsnhTvhYgQOcV46m1CY4X_ilRTE0k8OZFO9Dr5Ul3gzNMBpE0kg4j4HGEc4v-DE34n5QyBG-qvg4Hzg4E-G4Yse3ARE-Xam08nHo8055y0rvsEaKElzkccX8KbNeP4bnbK-Fb9J6sF1hOejYqoRxlf8NNAFDMrs2-mqHYWMB5AjZiGugeEIVZU9Aq1SL1KnDIQB-QXbI11yy6KaqZ1XoqvbPWFRSGuYTt_5rzOZhMyuF0EjBCwNTLzSa1WrhCAJimpQgAZljYVmoQsSgdHqQTgbqpYUztonBQ5AAHmcJkW1aEZbAXnDGpewIj2lxUZ_TQe9cAZPItnokor4clXfHHMei8iHK0xQ9v0Lg6I6Jh_M0u71CP-L6lLMgP7B3yhTWJ8PiKO7_NelsP12BNxKTbAj0gkLBEwBiVDPKFTfi7ad_SPwMIxOuFxQwDtFxxIxPoIdr8R0cino9WJzAed0-red6SgXA9JoebDggVSv2EfEI8S2HkMrkfcr_GMnNUSB5indO2bzysrwT_ijLo0ZaJ_DOOY45Q15KexLyl1mjXJt3_fiut-anBVDVewmqwLUXtK9MBcxYwBNPN7ghEogiw9wplrwjGn1D-psbaIGq2UrqZMzJS9msrvk5ygsRzIMyDy0VXUEwQni_KiySkcpev8MEodY")
                                        .addHeader("Accept", "application/json")
                                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                        .build();

                                client = builder.build();
                                client.newCall(request).enqueue(new Callback() {
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
                                                Toast.makeText(VideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
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
                                                                    VideoID= String.valueOf(video.getLong("id"));
                                                                    VideoLink=video.getString("video");
                                                                    VideoImage=video.getString("image");
                                                                    VideoName=video.getString("name");
                                                                    VideoDes=video.getString("description");
                                                                    VideoAndroid=video.getString("android");
                                                                    VideoIOS=video.getString("ios");
                                                                    VideoSite=video.getString("site");

                                                                    IsExit=false;
                                                                    Intent intent = new Intent(getBaseContext(), PlayerVideoActivity.class);
                                                                    intent.putExtra("isReplay", false);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                                else if (result==-1)
                                                                {
                                                                    Toast.makeText(VideoActivity.this, "خطا در ویدیو ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
                                                                }

                                                            } catch (Exception e) {
                                                                try {
                                                                    if (loading.isShowing())
                                                                        loading.dismiss();
                                                                }catch (Exception el)
                                                                {
                                                                    el.printStackTrace();
                                                                }
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
                                                        Toast.makeText(VideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                        else
                                        {
                                            try {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }
                                                        Toast.makeText(VideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }catch (Exception e)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception el)
                                                {
                                                    el.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                try {
                                    if (loading.isShowing())
                                        loading.dismiss();
                                }catch (Exception el)
                                {
                                    el.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            }
        });
        boolean notification = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isnotification",false);
        if(notification)
        {
            LinearMessage.setVisibility(View.VISIBLE);
            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("isnotification",false).apply();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            ActivityDialogShowExit();
        }catch (Exception e)
        {
            try {
                if (loading.isShowing())
                    loading.dismiss();
            }catch (Exception el)
            {
                el.printStackTrace();
            }
        }
    }

    public void ActivityDialogShowExit()
    {
        try {
            ActivityDialogExit=new Dialog(VideoActivity.this);
            ActivityDialogExit.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(ActivityDialogExit.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ActivityDialogExit.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
            ActivityDialogExit.setContentView(R.layout.question_dialog);

            ImageView ExitYes = (ImageView)ActivityDialogExit.findViewById(R.id.ExitYes);
            ImageView ExitNo = (ImageView)ActivityDialogExit.findViewById(R.id.ExitNo);
            TextView TitleExit=(TextView)ActivityDialogExit.findViewById(R.id.TitleExit);
            TextView TextExit=(TextView)ActivityDialogExit.findViewById(R.id.TextExit);

            ActivityDialogExit.setCancelable(true);

            TitleExit.setTypeface(font_Bold);
            TextExit.setTypeface(font_Bold);

            ExitYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IsExit = true;
                    ActivityDialogExit.dismiss();
                    finishAffinity();
                }
            });
            ExitNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityDialogExit.isShowing())
                        ActivityDialogExit.dismiss();
                }
            });

            ActivityDialogExit.show();
        }catch (Exception e)
        {
            try {
    if (loading.isShowing())
        loading.dismiss();
}catch (Exception el)
{
    el.printStackTrace();
}
        }
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

    public void GetStateCityName()
    {
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
                            .addFormDataPart("id",UserIdString)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://adcompany.ir/api/user/profile")
                            .post(body)
                            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImY3ZGFmMmUzMWE4ZmFkZWYzZWU2ZTJjMzRhMjFjNTdkOGI3MjdhZjMxMGE1ZTJlZDM4NmQ4MDQxNDhlZTliNWU1NjVkODMwM2NkODkzYzE4In0.eyJhdWQiOiIxIiwianRpIjoiZjdkYWYyZTMxYThmYWRlZjNlZTZlMmMzNGEyMWM1N2Q4YjcyN2FmMzEwYTVlMmVkMzg2ZDgwNDE0OGVlOWI1ZTU2NWQ4MzAzY2Q4OTNjMTgiLCJpYXQiOjE1NTUwNjE2MjIsIm5iZiI6MTU1NTA2MTYyMiwiZXhwIjoxNTg2Njg0MDIyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.S30gaSmqSaK253snXsnhTvhYgQOcV46m1CY4X_ilRTE0k8OZFO9Dr5Ul3gzNMBpE0kg4j4HGEc4v-DE34n5QyBG-qvg4Hzg4E-G4Yse3ARE-Xam08nHo8055y0rvsEaKElzkccX8KbNeP4bnbK-Fb9J6sF1hOejYqoRxlf8NNAFDMrs2-mqHYWMB5AjZiGugeEIVZU9Aq1SL1KnDIQB-QXbI11yy6KaqZ1XoqvbPWFRSGuYTt_5rzOZhMyuF0EjBCwNTLzSa1WrhCAJimpQgAZljYVmoQsSgdHqQTgbqpYUztonBQ5AAHmcJkW1aEZbAXnDGpewIj2lxUZ_TQe9cAZPItnokor4clXfHHMei8iHK0xQ9v0Lg6I6Jh_M0u71CP-L6lLMgP7B3yhTWJ8PiKO7_NelsP12BNxKTbAj0gkLBEwBiVDPKFTfi7ad_SPwMIxOuFxQwDtFxxIxPoIdr8R0cino9WJzAed0-red6SgXA9JoebDggVSv2EfEI8S2HkMrkfcr_GMnNUSB5indO2bzysrwT_ijLo0ZaJ_DOOY45Q15KexLyl1mjXJt3_fiut-anBVDVewmqwLUXtK9MBcxYwBNPN7ghEogiw9wplrwjGn1D-psbaIGq2UrqZMzJS9msrvk5ygsRzIMyDy0VXUEwQni_KiySkcpev8MEodY")
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .build();

                    client = builder.build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("looooog", e.toString());
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        if (ActivityDialogInternetConnection.isShowing())
                                            ActivityDialogInternetConnection.dismiss();
                                    }
                                    catch (Exception e)
                                    {
                                        try {
                                            if (loading.isShowing())
                                                loading.dismiss();
                                        }catch (Exception el)
                                        {
                                            el.printStackTrace();
                                        }
                                    }
                                    if(!isFinishing())
                                    {
                                        ActivityDialogShowInternetConnection();
                                    }
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
                                            try {
                                                if (ActivityDialogInternetConnection.isShowing())
                                                    ActivityDialogInternetConnection.dismiss();
                                            }
                                            catch (Exception e)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception el)
                                                {
                                                    el.printStackTrace();
                                                }
                                            }
                                            if (!isFinishing()) {
                                                try {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception e)
                                                    {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception el)
                                                        {
                                                            el.printStackTrace();
                                                        }
                                                    }
                                                    JSONObject data= jObject.getJSONObject("data");
                                                    StateName=data.getString("state");
                                                    CityName=data.getString("city");
                                                    int score=data.getInt("score");
                                                    String status = data.getString("status");
                                                    try {
                                                        String unique = data.getString("unique");
                                                        String UniqueID = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("unique","");
                                                        if (!unique.equals(UniqueID))
                                                        {
                                                            LogOut();
                                                            Toast.makeText(VideoActivity.this, "این حساب کاربری در دستگاه دیگری فعال میباشد", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }catch (Exception e)
                                                    {
                                                        LogOut();
                                                        Toast.makeText(VideoActivity.this, "این حساب کاربری در دستگاه دیگری فعال میباشد", Toast.LENGTH_SHORT).show();
                                                    }
                                                    try {
                                                        if (status.contains("غیر"))
                                                        {
                                                            LogOut();
                                                            Toast.makeText(VideoActivity.this, "حساب فعال نمی باشد", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }catch (Exception e)
                                                    {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception el)
                                                        {
                                                            el.printStackTrace();
                                                        }
                                                    }
                                                    if (StateName.equals("null"))
                                                    {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception e)
                                                        {
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception el)
                                                            {
                                                                el.printStackTrace();
                                                            }
                                                        }
                                                        try {
                                                            if (ActivityDialogInternetConnection.isShowing())
                                                                ActivityDialogInternetConnection.dismiss();
                                                        }catch (Exception e)
                                                        {
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception el)
                                                            {
                                                                el.printStackTrace();
                                                            }
                                                        }
                                                        Toast.makeText(VideoActivity.this, "لطفا پروفایل خود را تکمیل نمایید", Toast.LENGTH_SHORT).show();
                                                        IsExit=false;
                                                        Intent intent = new Intent(VideoActivity.this, ProfileActivity.class);
                                                        intent.putExtra("CompleteProfile",true);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putInt("money",score).apply();
                                                    String money_string=String.valueOf(score);
                                                    String money_farsi=FarsiNumberChange.changefarsi(money_string);
                                                    TextViewMoneyFarsi.setText(money_farsi);
                                                    TextViewMoneyEnglish.setText(money_string);
                                                    try {
                                                        GetStateId(StateName);
                                                    }catch (Exception e)
                                                    {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception el)
                                                        {
                                                            el.printStackTrace();
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception el)
                                                    {
                                                        el.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    try {
                                        if (loading.isShowing())
                                            loading.dismiss();
                                    }catch (Exception el)
                                    {
                                        el.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    try {
                        if (loading.isShowing())
                            loading.dismiss();
                    }catch (Exception el)
                    {
                        el.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void GetStateId(String NameOfState)
    {
        String stateid = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("stateid","0");
        Log.d("looog", stateid);
        if (!stateid.equals("0"))
        {
            State_id= Integer.parseInt(stateid);
        }
        else
        {
            try {
                if (loading.isShowing())
                    loading.dismiss();
            }catch (Exception e)
            {
                try {
                    if (loading.isShowing())
                        loading.dismiss();
                }catch (Exception el)
                {
                    el.printStackTrace();
                }
            }
            try {
                loading = new Dialog(VideoActivity.this);
                loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                loading.setContentView(R.layout.loading_dialog);
                Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);
                loading.show();
            }catch (Exception e)
            {
                try {
                    if (loading.isShowing())
                        loading.dismiss();
                }catch (Exception el)
                {
                    el.printStackTrace();
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();

                        OkHttpClient.Builder builder = new OkHttpClient.Builder();
                        builder.connectTimeout(15, TimeUnit.SECONDS);
                        builder.readTimeout(15, TimeUnit.SECONDS);
                        builder.writeTimeout(15, TimeUnit.SECONDS);


                        Request request = new Request.Builder()
                                .url("http://adcompany.ir/api/state/list")
                                .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImY3ZGFmMmUzMWE4ZmFkZWYzZWU2ZTJjMzRhMjFjNTdkOGI3MjdhZjMxMGE1ZTJlZDM4NmQ4MDQxNDhlZTliNWU1NjVkODMwM2NkODkzYzE4In0.eyJhdWQiOiIxIiwianRpIjoiZjdkYWYyZTMxYThmYWRlZjNlZTZlMmMzNGEyMWM1N2Q4YjcyN2FmMzEwYTVlMmVkMzg2ZDgwNDE0OGVlOWI1ZTU2NWQ4MzAzY2Q4OTNjMTgiLCJpYXQiOjE1NTUwNjE2MjIsIm5iZiI6MTU1NTA2MTYyMiwiZXhwIjoxNTg2Njg0MDIyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.S30gaSmqSaK253snXsnhTvhYgQOcV46m1CY4X_ilRTE0k8OZFO9Dr5Ul3gzNMBpE0kg4j4HGEc4v-DE34n5QyBG-qvg4Hzg4E-G4Yse3ARE-Xam08nHo8055y0rvsEaKElzkccX8KbNeP4bnbK-Fb9J6sF1hOejYqoRxlf8NNAFDMrs2-mqHYWMB5AjZiGugeEIVZU9Aq1SL1KnDIQB-QXbI11yy6KaqZ1XoqvbPWFRSGuYTt_5rzOZhMyuF0EjBCwNTLzSa1WrhCAJimpQgAZljYVmoQsSgdHqQTgbqpYUztonBQ5AAHmcJkW1aEZbAXnDGpewIj2lxUZ_TQe9cAZPItnokor4clXfHHMei8iHK0xQ9v0Lg6I6Jh_M0u71CP-L6lLMgP7B3yhTWJ8PiKO7_NelsP12BNxKTbAj0gkLBEwBiVDPKFTfi7ad_SPwMIxOuFxQwDtFxxIxPoIdr8R0cino9WJzAed0-red6SgXA9JoebDggVSv2EfEI8S2HkMrkfcr_GMnNUSB5indO2bzysrwT_ijLo0ZaJ_DOOY45Q15KexLyl1mjXJt3_fiut-anBVDVewmqwLUXtK9MBcxYwBNPN7ghEogiw9wplrwjGn1D-psbaIGq2UrqZMzJS9msrvk5ygsRzIMyDy0VXUEwQni_KiySkcpev8MEodY")
                                .addHeader("Accept", "application/json")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .build();

                        client = builder.build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("looooog", e.toString());
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        Toast.makeText(VideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
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
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception el)
                                                            {
                                                                el.printStackTrace();
                                                            }
                                                        }
                                                        JSONArray data=jObject.getJSONArray("data");
                                                        if (data!=null)
                                                        {
                                                            for (int i=0;i<data.length();i++)
                                                            {
                                                                String state=data.getJSONObject(i).getString("name");
                                                                if (state.equals(NameOfState))
                                                                {
                                                                    State_id=data.getJSONObject(i).getInt("id");
                                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("stateid", String.valueOf(State_id)).apply();
                                                                    break;
                                                                }
                                                            }

                                                            GetCityId(CityName, String.valueOf(State_id));
                                                        }
                                                    } catch (Exception e) {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception el)
                                                        {
                                                            el.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    } catch (Exception e) {
                                        try {
                                            if (loading.isShowing())
                                                loading.dismiss();
                                        }catch (Exception el)
                                        {
                                            el.printStackTrace();
                                        }
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        try {
                            if (loading.isShowing())
                                loading.dismiss();
                        }catch (Exception el)
                        {
                            el.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
    public void GetCityId(String NameOfcity,String StateId)
    {
        String cityid = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("cityid","0");
        Log.d("looog", cityid);
        if (!cityid.equals("0"))
        {
            City_id= Integer.parseInt(cityid);
        }
        else
        {
            try {
                if (loading.isShowing())
                    loading.dismiss();
            }catch (Exception e)
            {
                try {
                    if (loading.isShowing())
                        loading.dismiss();
                }catch (Exception el)
                {
                    el.printStackTrace();
                }
            }
            try {
                loading = new Dialog(VideoActivity.this);
                loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                loading.setContentView(R.layout.loading_dialog);
                Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);
                loading.show();
            }catch (Exception e)
            {
                try {
                    if (loading.isShowing())
                        loading.dismiss();
                }catch (Exception el)
                {
                    el.printStackTrace();
                }
            }
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
                                .addFormDataPart("id",StateId)
                                .build();

                        Request request = new Request.Builder()
                                .url("http://adcompany.ir/api/city/list")
                                .post(body)
                                .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImY3ZGFmMmUzMWE4ZmFkZWYzZWU2ZTJjMzRhMjFjNTdkOGI3MjdhZjMxMGE1ZTJlZDM4NmQ4MDQxNDhlZTliNWU1NjVkODMwM2NkODkzYzE4In0.eyJhdWQiOiIxIiwianRpIjoiZjdkYWYyZTMxYThmYWRlZjNlZTZlMmMzNGEyMWM1N2Q4YjcyN2FmMzEwYTVlMmVkMzg2ZDgwNDE0OGVlOWI1ZTU2NWQ4MzAzY2Q4OTNjMTgiLCJpYXQiOjE1NTUwNjE2MjIsIm5iZiI6MTU1NTA2MTYyMiwiZXhwIjoxNTg2Njg0MDIyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.S30gaSmqSaK253snXsnhTvhYgQOcV46m1CY4X_ilRTE0k8OZFO9Dr5Ul3gzNMBpE0kg4j4HGEc4v-DE34n5QyBG-qvg4Hzg4E-G4Yse3ARE-Xam08nHo8055y0rvsEaKElzkccX8KbNeP4bnbK-Fb9J6sF1hOejYqoRxlf8NNAFDMrs2-mqHYWMB5AjZiGugeEIVZU9Aq1SL1KnDIQB-QXbI11yy6KaqZ1XoqvbPWFRSGuYTt_5rzOZhMyuF0EjBCwNTLzSa1WrhCAJimpQgAZljYVmoQsSgdHqQTgbqpYUztonBQ5AAHmcJkW1aEZbAXnDGpewIj2lxUZ_TQe9cAZPItnokor4clXfHHMei8iHK0xQ9v0Lg6I6Jh_M0u71CP-L6lLMgP7B3yhTWJ8PiKO7_NelsP12BNxKTbAj0gkLBEwBiVDPKFTfi7ad_SPwMIxOuFxQwDtFxxIxPoIdr8R0cino9WJzAed0-red6SgXA9JoebDggVSv2EfEI8S2HkMrkfcr_GMnNUSB5indO2bzysrwT_ijLo0ZaJ_DOOY45Q15KexLyl1mjXJt3_fiut-anBVDVewmqwLUXtK9MBcxYwBNPN7ghEogiw9wplrwjGn1D-psbaIGq2UrqZMzJS9msrvk5ygsRzIMyDy0VXUEwQni_KiySkcpev8MEodY")
                                .addHeader("Accept", "application/json")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .build();

                        client = builder.build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("looooog", e.toString());
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        Toast.makeText(VideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
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
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception el)
                                                            {
                                                                el.printStackTrace();
                                                            }
                                                        }
                                                        JSONArray data=jObject.getJSONArray("data");
                                                        if (data!=null)
                                                        {
                                                            for (int i=0;i<data.length();i++)
                                                            {
                                                                String city=data.getJSONObject(i).getString("name");
                                                                if (city.equals(NameOfcity))
                                                                {
                                                                    City_id=data.getJSONObject(i).getInt("id");
                                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("cityid", String.valueOf(City_id)).apply();
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception el)
                                                        {
                                                            el.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    } catch (Exception e) {
                                        try {
                                            if (loading.isShowing())
                                                loading.dismiss();
                                        }catch (Exception el)
                                        {
                                            el.printStackTrace();
                                        }
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        try {
                            if (loading.isShowing())
                                loading.dismiss();
                        }catch (Exception el)
                        {
                            el.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onDestroy() {
        if (IsExit)
            SetOffline();
        super.onDestroy();
    }

    public void SetOffline()
    {
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
                            .addFormDataPart("user_id", UserIdString)
                            .build();


                    Request request = new Request.Builder()
                            .url("http://adcompany.ir/api/user/offline")
                            .post(body)
                            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImY3ZGFmMmUzMWE4ZmFkZWYzZWU2ZTJjMzRhMjFjNTdkOGI3MjdhZjMxMGE1ZTJlZDM4NmQ4MDQxNDhlZTliNWU1NjVkODMwM2NkODkzYzE4In0.eyJhdWQiOiIxIiwianRpIjoiZjdkYWYyZTMxYThmYWRlZjNlZTZlMmMzNGEyMWM1N2Q4YjcyN2FmMzEwYTVlMmVkMzg2ZDgwNDE0OGVlOWI1ZTU2NWQ4MzAzY2Q4OTNjMTgiLCJpYXQiOjE1NTUwNjE2MjIsIm5iZiI6MTU1NTA2MTYyMiwiZXhwIjoxNTg2Njg0MDIyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.S30gaSmqSaK253snXsnhTvhYgQOcV46m1CY4X_ilRTE0k8OZFO9Dr5Ul3gzNMBpE0kg4j4HGEc4v-DE34n5QyBG-qvg4Hzg4E-G4Yse3ARE-Xam08nHo8055y0rvsEaKElzkccX8KbNeP4bnbK-Fb9J6sF1hOejYqoRxlf8NNAFDMrs2-mqHYWMB5AjZiGugeEIVZU9Aq1SL1KnDIQB-QXbI11yy6KaqZ1XoqvbPWFRSGuYTt_5rzOZhMyuF0EjBCwNTLzSa1WrhCAJimpQgAZljYVmoQsSgdHqQTgbqpYUztonBQ5AAHmcJkW1aEZbAXnDGpewIj2lxUZ_TQe9cAZPItnokor4clXfHHMei8iHK0xQ9v0Lg6I6Jh_M0u71CP-L6lLMgP7B3yhTWJ8PiKO7_NelsP12BNxKTbAj0gkLBEwBiVDPKFTfi7ad_SPwMIxOuFxQwDtFxxIxPoIdr8R0cino9WJzAed0-red6SgXA9JoebDggVSv2EfEI8S2HkMrkfcr_GMnNUSB5indO2bzysrwT_ijLo0ZaJ_DOOY45Q15KexLyl1mjXJt3_fiut-anBVDVewmqwLUXtK9MBcxYwBNPN7ghEogiw9wplrwjGn1D-psbaIGq2UrqZMzJS9msrvk5ygsRzIMyDy0VXUEwQni_KiySkcpev8MEodY")
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .build();

                    client = builder.build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("looooog", e.toString());
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
                                                    Log.d("looog", "offline");
                                                } catch (Exception e) {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception el)
                                                    {
                                                        el.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    try {
                                        if (loading.isShowing())
                                            loading.dismiss();
                                    }catch (Exception el)
                                    {
                                        el.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    try {
                        if (loading.isShowing())
                            loading.dismiss();
                    }catch (Exception el)
                    {
                        el.printStackTrace();
                    }
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
                try {
                    if (loading.isShowing())
                        loading.dismiss();
                }catch (Exception el)
                {
                    el.printStackTrace();
                }
            }

        }
    };

    public void ActivityDialogShowInternetConnection()
    {
       try {
           ActivityDialogInternetConnection=new Dialog(VideoActivity.this);
           ActivityDialogInternetConnection.requestWindowFeature(Window.FEATURE_NO_TITLE);
           Objects.requireNonNull(ActivityDialogInternetConnection.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

           ActivityDialogInternetConnection.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
           ActivityDialogInternetConnection.setContentView(R.layout.error_connection_dialog);

           TextView TitleErrorConnectionDialog=(TextView)ActivityDialogInternetConnection.findViewById(R.id.TitleErrorConnectionDialog);
           TextView TextErrorConnectionDialog=(TextView)ActivityDialogInternetConnection.findViewById(R.id.TextErrorConnectionDialog);

           TitleErrorConnectionDialog.setTypeface(font_Bold);
           TextErrorConnectionDialog.setTypeface(font_Bold);

           ActivityDialogInternetConnection.setOnCancelListener(new DialogInterface.OnCancelListener() {
               @Override
               public void onCancel(DialogInterface dialog) {
                   GetStateCityName();
               }
           });

           ActivityDialogInternetConnection.show();
       }catch (Exception e)
       {
           try {
                if (loading.isShowing())
                    loading.dismiss();
            }catch (Exception el)
            {
                el.printStackTrace();
            }
       }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mHandler);
    }

    public void LogOut()
    {
        try {
            if (loading.isShowing())
                loading.dismiss();
        }catch (Exception e)
        {
            try {
                if (loading.isShowing())
                    loading.dismiss();
            }catch (Exception el)
            {
                el.printStackTrace();
            }
        }
        loading = new Dialog(VideoActivity.this);
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
                            .addFormDataPart("user_id", UserIdString)
                            .build();


                    Request request = new Request.Builder()
                            .url("http://adcompany.ir/api/user/logout")
                            .post(body)
                            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImY3ZGFmMmUzMWE4ZmFkZWYzZWU2ZTJjMzRhMjFjNTdkOGI3MjdhZjMxMGE1ZTJlZDM4NmQ4MDQxNDhlZTliNWU1NjVkODMwM2NkODkzYzE4In0.eyJhdWQiOiIxIiwianRpIjoiZjdkYWYyZTMxYThmYWRlZjNlZTZlMmMzNGEyMWM1N2Q4YjcyN2FmMzEwYTVlMmVkMzg2ZDgwNDE0OGVlOWI1ZTU2NWQ4MzAzY2Q4OTNjMTgiLCJpYXQiOjE1NTUwNjE2MjIsIm5iZiI6MTU1NTA2MTYyMiwiZXhwIjoxNTg2Njg0MDIyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.S30gaSmqSaK253snXsnhTvhYgQOcV46m1CY4X_ilRTE0k8OZFO9Dr5Ul3gzNMBpE0kg4j4HGEc4v-DE34n5QyBG-qvg4Hzg4E-G4Yse3ARE-Xam08nHo8055y0rvsEaKElzkccX8KbNeP4bnbK-Fb9J6sF1hOejYqoRxlf8NNAFDMrs2-mqHYWMB5AjZiGugeEIVZU9Aq1SL1KnDIQB-QXbI11yy6KaqZ1XoqvbPWFRSGuYTt_5rzOZhMyuF0EjBCwNTLzSa1WrhCAJimpQgAZljYVmoQsSgdHqQTgbqpYUztonBQ5AAHmcJkW1aEZbAXnDGpewIj2lxUZ_TQe9cAZPItnokor4clXfHHMei8iHK0xQ9v0Lg6I6Jh_M0u71CP-L6lLMgP7B3yhTWJ8PiKO7_NelsP12BNxKTbAj0gkLBEwBiVDPKFTfi7ad_SPwMIxOuFxQwDtFxxIxPoIdr8R0cino9WJzAed0-red6SgXA9JoebDggVSv2EfEI8S2HkMrkfcr_GMnNUSB5indO2bzysrwT_ijLo0ZaJ_DOOY45Q15KexLyl1mjXJt3_fiut-anBVDVewmqwLUXtK9MBcxYwBNPN7ghEogiw9wplrwjGn1D-psbaIGq2UrqZMzJS9msrvk5ygsRzIMyDy0VXUEwQni_KiySkcpev8MEodY")
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .build();

                    client = builder.build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            try {
                                if (loading.isShowing())
                                    loading.dismiss();
                            }catch (Exception el)
                            {
                                el.printStackTrace();
                            }
                            Toast.makeText(VideoActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
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
                                }catch (Exception el)
                                {
                                    el.printStackTrace();
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
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception el)
                                                        {
                                                            el.printStackTrace();
                                                        }
                                                    }
                                                    Log.d("looog", "logout");
                                                    long user_id = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",0);
                                                    String UserIdString= String.valueOf(user_id);
                                                    Pushe.unsubscribe(VideoActivity.this,UserIdString);
                                                    Pushe.subscribe(VideoActivity.this,"-1");
                                                    Pushe.unsubscribe(VideoActivity.this,"0");
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putLong("user_id",0).apply();
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("cityid", "0").apply();
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("stateid", "0").apply();
                                                    startActivity(new Intent(VideoActivity.this, MainActivity.class));
                                                    finishAffinity();
                                                } catch (Exception e) {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception el)
                                                    {
                                                        el.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    try {
                                        if (loading.isShowing())
                                            loading.dismiss();
                                    }catch (Exception el)
                                    {
                                        el.printStackTrace();
                                    }
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
                                                    Log.d("looog", "logout");
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putLong("user_id",0).apply();
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("cityid", "0").apply();
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("stateid", "0").apply();
                                                    startActivity(new Intent(VideoActivity.this, MainActivity.class));
                                                    finishAffinity();
                                                } catch (Exception e) {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception el)
                                                    {
                                                        el.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    try {
                                        if (loading.isShowing())
                                            loading.dismiss();
                                    }catch (Exception el)
                                    {
                                        el.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    try {
                        if (loading.isShowing())
                            loading.dismiss();
                    }catch (Exception el)
                    {
                        el.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
