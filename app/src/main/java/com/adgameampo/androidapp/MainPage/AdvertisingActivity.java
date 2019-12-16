package com.adgameampo.androidapp.MainPage;

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
import android.os.Handler;
import android.support.annotation.NonNull;
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

import com.adgameampo.androidapp.FarsiNumberChange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.adgameampo.androidapp.R;

import co.ronash.pushe.Pushe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdvertisingActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    Typeface font_Medium;
    Typeface font_Bold;

    TextView HeaderAdvertising;
    TextView TextViewAdvertising;
    TextView TextViewMoneyFarsi;
    TextView TextViewMoneyEnglish;
    TextView TextViewTitleMessage;
    TextView TextViewTextMessage;

    ConstraintLayout ConstraintMoney;
    LinearLayout LinearMessage;

    Button CashButton;
    Button MessageButton;
    Button OrderAdvertisingButton;
    Button ContactWithUsButton;
    Button SMSNumberButton;

    ImageView ImageViewAccount;
    ImageView ImageViewBack;
    ImageView ImageViewTelegram;
    ImageView ImageViewInstagram;

    BottomNavigationView bottomNavigationView;

    public boolean IsExit;

    public Dialog loading;
    public Dialog ActivityDialogExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler,new IntentFilter("com.adgameampo.androidapp_FCM-MESSAGE"));
        setContentView(R.layout.activity_advertising);

        context=getApplicationContext();

        IsExit=true;

        setStatusBarGradiant(AdvertisingActivity.this);

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation_view);
        HeaderAdvertising=(TextView) findViewById(R.id.HeaderAdvertising);
        TextViewAdvertising=(TextView) findViewById(R.id.TextViewAdvertising);
        TextViewMoneyFarsi=(TextView)findViewById(R.id.TextViewMoneyFarsi);
        TextViewMoneyEnglish=(TextView)findViewById(R.id.TextViewMoneyEnglish);
        TextViewTitleMessage=(TextView)findViewById(R.id.TextViewTitleMessage);
        TextViewTextMessage=(TextView)findViewById(R.id.TextViewTextMessage);
        CashButton=(Button) findViewById(R.id.CashButton);
        MessageButton=(Button) findViewById(R.id.MessageButton);
        OrderAdvertisingButton=(Button) findViewById(R.id.OrderAdvertisingButton);
        ContactWithUsButton=(Button) findViewById(R.id.ContactWithUsButton);
        SMSNumberButton=(Button) findViewById(R.id.SMSNumberButton);
        ImageViewAccount=(ImageView) findViewById(R.id.ImageViewAccount);
        ImageViewBack=(ImageView) findViewById(R.id.ImageViewBack);
        ImageViewTelegram=(ImageView) findViewById(R.id.ImageViewTelegram);
        ImageViewInstagram=(ImageView) findViewById(R.id.ImageViewInstagram);
        LinearMessage=(LinearLayout) findViewById(R.id.LinearMessage);
        ConstraintMoney=(ConstraintLayout) findViewById(R.id.ConstraintMoney);


        font_Medium= Typeface.createFromAsset(AdvertisingActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(AdvertisingActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");

        HeaderAdvertising.setTypeface(font_Bold);
        TextViewAdvertising.setTypeface(font_Bold);
        CashButton.setTypeface(font_Bold);
        MessageButton.setTypeface(font_Bold);
        OrderAdvertisingButton.setTypeface(font_Bold);
        ContactWithUsButton.setTypeface(font_Bold);
        SMSNumberButton.setTypeface(font_Bold);
        TextViewMoneyFarsi.setTypeface(font_Bold);
        TextViewTitleMessage.setTypeface(font_Bold);
        TextViewTextMessage.setTypeface(font_Bold);
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

//        bottomNavigationView.setSelectedItemId(R.id.navigation_advertising);

        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View view = bottomNavigationMenuView.getChildAt(3);
        view.setBackgroundResource(R.drawable.advertising_bg);

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
                if (menuItem.getItemId()==R.id.navigation_video)
                {
                    startActivity(new Intent(getBaseContext(), VideoActivity.class));
                    overridePendingTransition(0, 0);
                    IsExit=false;
                    finish();
                }
                if (menuItem.getItemId()==R.id.navigation_list)
                {
                    Intent intent=new Intent(AdvertisingActivity.this,ListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    IsExit = false;
                    finish();
                }
                return false;
            }
        });

        String text = "هدف مجموعه ما کمک به برند سازی و شناساندن کسب و کارهای ایرانی به مخاطبان با تبلیغاتی پربازده و در عین حال به صرفه و ارزان است . تمام تلاش مجموعه در جهت افزایش کیفیت تبلیغات با ایجاد محیطی جذاب و پرهیجان و تاثیرگذار در اپلیکیشن آد است .";
        TextViewAdvertising.setText(text);

        OrderAdvertisingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(AdvertisingActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    String url= "http://adcompany.ir";
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityIfNeeded(browserIntent,0);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        ContactWithUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(AdvertisingActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:02176871986"));
                    startActivity(intent);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        SMSNumberButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(AdvertisingActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    Uri uri = Uri.parse("smsto:10000002200330");
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", "شماره پیامکی : 10000002200330");
                    startActivity(it);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        ImageViewTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(AdvertisingActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    String url= "https://t.me/adcompanytel";
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityIfNeeded(browserIntent,0);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        ImageViewInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(AdvertisingActivity.this,R.anim.click);
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

        ImageViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(AdvertisingActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                startActivity(new Intent(AdvertisingActivity.this, AccountActivity.class));
            }
        });

        ImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(AdvertisingActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                onBackPressed();
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

    }
    @Override
    public void onBackPressed() {
        try {
            ActivityDialogShowExit();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void ActivityDialogShowExit()
    {
        try {
            ActivityDialogExit=new Dialog(AdvertisingActivity.this);
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
            e.printStackTrace();
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
