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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adgameampo.androidapp.FarsiNumberChange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

public class FinalCheackOutActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    Typeface font_Medium;
    Typeface font_Bold;

    TextView HeaderFinalCheackOut;
    TextView TextViewTitleShaba;
    TextView TextViewCashNumberTitle;
    TextView TextViewCashNumber;
    TextView TextViewCashTitle;
    TextView TextViewCash;
    TextView TextViewMoneyFarsi;
    TextView TextViewMoneyEnglish;
    TextView TextViewTitleMessage;
    TextView TextViewTextMessage;

    ConstraintLayout ConstraintMoney;
    LinearLayout LinearMessage;

    ImageView ImageViewAccount;
    ImageView ImageViewBack;

    Button CashButton;
    Button MessageButton;
    Button OkFinalCheackOutButton;

    BottomNavigationView bottomNavigationView;
    ListView ListViewShabaCard;
    AdapterShabaSelect adapterShabaSelect;

    public Dialog loading;
    public Dialog ActivityDialogSuccess;
    public Dialog ActivityDialogUnsuccess;

    public static ArrayList<ListShabaSelect> ShabaSelect=new ArrayList<ListShabaSelect>();

    int PreSelectIndex=0;
    public String ShabaCardIDSelect=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler,new IntentFilter("com.adgameampo.androidapp_FCM-MESSAGE"));
        setContentView(R.layout.activity_final_cheack_out);

        context=getApplicationContext();

        setStatusBarGradiant(FinalCheackOutActivity.this);

        HeaderFinalCheackOut=(TextView)findViewById(R.id.HeaderFinalCheackOut);
        TextViewTitleShaba=(TextView)findViewById(R.id.TextViewTitleShaba);
        TextViewCashNumberTitle=(TextView)findViewById(R.id.TextViewCashNumberTitle);
        TextViewCashNumber=(TextView)findViewById(R.id.TextViewCashNumber);
        TextViewCashTitle=(TextView)findViewById(R.id.TextViewCashTitle);
        TextViewCash=(TextView)findViewById(R.id.TextViewCash);
        TextViewMoneyFarsi=(TextView)findViewById(R.id.TextViewMoneyFarsi);
        TextViewMoneyEnglish=(TextView)findViewById(R.id.TextViewMoneyEnglish);
        TextViewTitleMessage=(TextView)findViewById(R.id.TextViewTitleMessage);
        TextViewTextMessage=(TextView)findViewById(R.id.TextViewTextMessage);
        LinearMessage=(LinearLayout) findViewById(R.id.LinearMessage);
        ConstraintMoney=(ConstraintLayout) findViewById(R.id.ConstraintMoney);
        ImageViewAccount=(ImageView) findViewById(R.id.ImageViewAccount);
        ImageViewBack=(ImageView) findViewById(R.id.ImageViewBack);
        CashButton=(Button) findViewById(R.id.CashButton);
        MessageButton=(Button) findViewById(R.id.MessageButton);
        OkFinalCheackOutButton=(Button) findViewById(R.id.OkFinalCheackOutButton);
        ListViewShabaCard=(ListView) findViewById(R.id.ListViewShabaCard);
        adapterShabaSelect=new AdapterShabaSelect(getApplicationContext(),ShabaSelect);
        ListViewShabaCard.setAdapter(adapterShabaSelect);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation_view);

        font_Medium= Typeface.createFromAsset(FinalCheackOutActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(FinalCheackOutActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");

        HeaderFinalCheackOut.setTypeface(font_Bold);
        TextViewTitleShaba.setTypeface(font_Bold);
        TextViewCashNumberTitle.setTypeface(font_Medium);
        TextViewCashNumber.setTypeface(font_Medium);
        TextViewCashTitle.setTypeface(font_Medium);
        TextViewCash.setTypeface(font_Medium);
        CashButton.setTypeface(font_Bold);
        MessageButton.setTypeface(font_Bold);
        OkFinalCheackOutButton.setTypeface(font_Bold);
        TextViewMoneyFarsi.setTypeface(font_Bold);
        TextViewTitleMessage.setTypeface(font_Bold);
        TextViewTextMessage.setTypeface(font_Bold);
//        TextViewMoneyEnglish.setTypeface(font_Bold);

        Pushe.initialize(this,true);

        int money = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getInt("money",0);
        String money_String=String.valueOf(money);
        String money_farsi=FarsiNumberChange.changefarsi(money_String);
        TextViewMoneyFarsi.setText(money_farsi);
        TextViewMoneyEnglish.setText(money_String);

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

//        bottomNavigationView.setSelectedItemId(R.id.navigation_check_out);

        for (int i = 0; i <bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            SpannableStringBuilder spannableTitle = new SpannableStringBuilder(menuItem.getTitle());
            spannableTitle.setSpan(new CustomTypefaceSpan("" , font_Bold), 0 , spannableTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            menuItem.setTitle(spannableTitle);
        }

        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View view = bottomNavigationMenuView.getChildAt(1);

        view.setBackgroundResource(R.drawable.check_out_bg);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.navigation_guide)
                {
                    startActivity(new Intent(getBaseContext(), GuideActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (menuItem.getItemId()==R.id.navigation_video)
                {
                    startActivity(new Intent(getBaseContext(), VideoActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (menuItem.getItemId()==R.id.navigation_list)
                {
                    startActivity(new Intent(getBaseContext(), ListActivity.class));
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
        ListViewShabaCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    ListShabaSelect list = ShabaSelect.get(position);

                    list.setSelect(true);

                    ShabaSelect.set(position,list);

                    if (PreSelectIndex>-1)
                    {
                        ListShabaSelect PreRecord = ShabaSelect.get(PreSelectIndex);
                        PreRecord.setSelect(false);

                        ShabaSelect.set(PreSelectIndex,PreRecord);
                    }

                    PreSelectIndex = position;

                    ShabaCardIDSelect= String.valueOf(ShabaSelect.get(position).getId());
                    Log.d("loooog", ShabaCardIDSelect);

                    adapterShabaSelect.updateRecords(ShabaSelect);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
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

        ImageViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(FinalCheackOutActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                startActivity(new Intent(FinalCheackOutActivity.this, AccountActivity.class));
            }
        });
        ImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(FinalCheackOutActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });

        OkFinalCheackOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShabaCardIDSelect == null)
                {
                    Toast.makeText(FinalCheackOutActivity.this, "لطفا یک شماره شبا ثبت کنید", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loading = new Dialog(FinalCheackOutActivity.this);
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
                                        .addFormDataPart("shaba_id",ShabaCardIDSelect)
                                        .build();

                                Request request = new Request.Builder()
                                        .url("http://adcompany.ir/api/required/pay")
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
                                                Toast.makeText(FinalCheackOutActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                                ActivityDialogShowSuccess();
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
                                                                String error=jObject.getString("error");
                                                                ActivityDialogShowUnsuccess(error);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                });
                                            }catch (Exception e)
                                            {
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
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetShabaCrad();
        GetSumCash();
    }

    public void GetShabaCrad()
    {
        try {
            if (loading.isShowing())
                loading.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        loading = new Dialog(FinalCheackOutActivity.this);
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
                            .build();

                    Request request = new Request.Builder()
                            .url("http://adcompany.ir/api/shaba/list")
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
                                    Toast.makeText(FinalCheackOutActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                    JSONArray data=jObject.getJSONArray("data");
                                                    try {
                                                        if (!ShabaSelect.isEmpty())
                                                            ShabaSelect.clear();
                                                        if (data!=null)
                                                        {
                                                            for (int i=0;i<data.length();i++)
                                                            {
                                                                try {
                                                                    long id = data.getJSONObject(i).getLong("id");
                                                                    String title=data.getJSONObject(i).getString("title");
                                                                    String number=data.getJSONObject(i).getString("number");
                                                                    if (i==0)
                                                                    {
                                                                        ListShabaSelect listShabaSelect = new ListShabaSelect(id,title,number,true);
                                                                        ShabaSelect.add(listShabaSelect);
                                                                        ShabaCardIDSelect= String.valueOf(id);
                                                                    }
                                                                    else
                                                                    {
                                                                        ListShabaSelect listShabaSelect = new ListShabaSelect(id,title,number,false);
                                                                        ShabaSelect.add(listShabaSelect);
                                                                    }
                                                                }catch (Exception e)
                                                                {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                            adapterShabaSelect=new AdapterShabaSelect(FinalCheackOutActivity.this,ShabaSelect);
                                                            ListViewShabaCard.setAdapter(adapterShabaSelect);
                                                        }
                                                    }catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
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
    public void GetSumCash()
    {
        try {
            if (loading.isShowing())
                loading.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        loading = new Dialog(FinalCheackOutActivity.this);
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
                                        if (loading.isShowing())
                                            loading.dismiss();
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(FinalCheackOutActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    JSONObject data=jObject.getJSONObject("data");
                                                    int score = data.getInt("score");
                                                    DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
                                                    String FormattedString = formatter.format(score);
                                                    TextViewCashNumber.setText(FormattedString);
                                                    String text = String.valueOf(score);
                                                    text=GetFigures(text)+"تومان";
                                                    TextViewCash.setText(text);

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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FinalCheackOutActivity.this, CheckOutActivity.class));
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
    public String GetFigures(String x)
    {
        try {
            String text7;
            String text10;

            String[] textArray0_10 = new String[11];
            textArray0_10[0] = "صفر";
            textArray0_10[1] = "یک";
            textArray0_10[2] = "دو";
            textArray0_10[3] = "سه";
            textArray0_10[4] = "چهار";
            textArray0_10[5] = "پنج";
            textArray0_10[6] = "شش";
            textArray0_10[7] = "هفت";
            textArray0_10[8] = "هشت";
            textArray0_10[9] = "نه";

            String[] textArray10_19 = new String[11];
            textArray10_19[0] = "ده";
            textArray10_19[1] = "یازده";
            textArray10_19[2] = "دوازده";
            textArray10_19[3] = "سیزده";
            textArray10_19[4] = "چهارده";
            textArray10_19[5] = "پانزده";
            textArray10_19[6] = "شانزده";
            textArray10_19[7] = "هفده";
            textArray10_19[8] = "هجده";
            textArray10_19[9] = "نوزده";

            String[] textArray20_90 = new String[11];
            textArray20_90[2] = "بیست";
            textArray20_90[3] = "سی";
            textArray20_90[4] = "چهل";
            textArray20_90[5] = "پنجاه";
            textArray20_90[6] = "شصت";
            textArray20_90[7] = "هفتاد";
            textArray20_90[8] = "هشتاد";
            textArray20_90[9] = "نود";

            String[] textArray100_900 = new String[11];
            textArray100_900[1] = "صد";
            textArray100_900[2] = "دویست";
            textArray100_900[3] = "سیصد";
            textArray100_900[4] = "چهارصد";
            textArray100_900[5] = "پانصد";
            textArray100_900[6] = "ششصد";
            textArray100_900[7] = "هفتصد";
            textArray100_900[8] = "هشتصد";
            textArray100_900[9] = "نهصد";

            String strHezar = "هزار";
            String strHezar_ = "هزار" + " ";
            String strMeliun = "میلیون";
            String strMiliard = "میلیارد";
            String strMiliard_ = "میلیارد" + " ";
            String strTriliun = "تریلیون";
            String strTriliun_ = "تریلیون" + " ";

            String text4 = x;
            text7 = "";
            switch (text4.length())
            {
                case 1:
                    if (x != "")
                    {
                        text7 = textArray0_10[Integer.parseInt(x)];
                    }
                    break;

                case 2:
                    if ((Integer.parseInt(text4.substring(text4.length() - 1)) > 0) & (Integer.parseInt(text4.substring(0, 1)) > 1))
                    {
                        text10 = (text4.substring( text4.length() - 1));
                        text7 = " و " + GetFigures(text10);
                    }
                    if (Integer.parseInt(text4.substring(0, 1)) > 1)
                    {
                        text7 = textArray20_90[Integer.parseInt(text4.substring(0, 1))] + text7;
                    }
                    if (Integer.parseInt(text4.substring(0, 1)) == 1)
                    {
                        text7 = textArray10_19[Integer.parseInt(text4.substring(text4.length() - 1))];
                    }
                    break;

                case 3:
                    if (Integer.parseInt(text4.substring(text4.length() - 2)) > 0)
                    {
                        text10 = String.valueOf(Integer.parseInt(text4.substring(text4.length() - 2)));
                        text7 = " و " + GetFigures(text10);
                    }
                    text7 = textArray100_900[(int)Math.round(Integer.parseInt(text4.substring(0  , 1)))] + text7;
                    break;

                case 4:
                    if (Integer.parseInt(text4.substring(text4.length() - 3)) > 0)
                    {
                        text10 = String.valueOf(Integer.parseInt(text4.substring(  text4.length() - 3)));
                        text7 = " و " + GetFigures(text10);
                    }
                    text10 = String.valueOf(Integer.parseInt(text4.substring(  0, 1)));
                    text7 = GetFigures(text10) + strHezar + text7;
                    break;

                case 5:
                    if (Integer.parseInt(text4.substring(text4.length() - 3)) > 0)
                    {
                        text10 = String.valueOf(Integer.parseInt(text4.substring(  text4.length() - 3)));
                        text7 = " و " + GetFigures(text10);
                    }
                    text10 = String.valueOf(Integer.parseInt(text4.substring(  0, 2)));
                    text7 = GetFigures(text10) + strHezar + text7;
                    break;

                case 6:
                    if (Integer.parseInt(text4.substring(text4.length() - 5)) != 0)
                    {
                        if (Integer.parseInt(text4.substring(text4.length() - 3)) > 0)
                        {
                            text10 = String.valueOf(Integer.parseInt(text4.substring(text4.length() - 3)));
                            text7 = " و " + GetFigures(text10);
                        }
                        text10 = String.valueOf(Integer.parseInt(text4.substring(  0, 3)));
                        text7 = GetFigures(text10) + strHezar_ + text7;
                        break;
                    }
                    text7 = textArray100_900[(int)Math.round(Integer.parseInt(text4.substring(0  , 1)))] + strHezar_;
                    break;


                case 7:
                    if (Integer.parseInt(text4.substring(text4.length() - 6)) != 0)
                    {
                        text10 = String.valueOf(Integer.parseInt(text4.substring(text4.length() - 6)));
                        text7 = " و " + GetFigures(text10);
                    }
                    text10 = String.valueOf(Integer.parseInt(text4.substring(  0, 1)));
                    text7 = GetFigures(text10) + strMeliun + text7;
                    break;

                case 8:
                    if (Integer.parseInt(text4.substring(text4.length() - 6)) > 0)
                    {
                        text10 = String.valueOf(Integer.parseInt(text4.substring(text4.length() - 6)));
                        text7 = " و " + GetFigures(text10);
                    }
                    text10 = String.valueOf(Integer.parseInt(text4.substring(  0, 2)));
                    text7 = GetFigures(text10) + strMeliun + text7;
                    break;

                case 9:
                    if (Integer.parseInt(text4.substring(text4.length() - 6)) > 0)
                    {
                        text10 = String.valueOf(Integer.parseInt(text4.substring(text4.length() - 6)));
                        text7 = " و " + GetFigures(text10);
                    }
                    text10 = String.valueOf(Integer.parseInt(text4.substring(  0, 3)));
                    text7 = GetFigures(text10) + strMeliun + text7;
                    break;

                case 10:
                    if (Integer.parseInt(text4.substring(text4.length() - 9, 9)) > 0)
                    {
                        text10 = String.valueOf(Integer.parseInt(text4.substring(text4.length() - 9, 9)));
                        text7 = " و " + GetFigures(text10);
                    }
                    text10 = String.valueOf(Integer.parseInt(text4.substring(  0, 1)));
                    text7 = GetFigures(text10) + strMiliard + text7;
                    break;

                case 11:
                    if (Integer.parseInt(text4.substring(text4.length() - 9)) > 0)
                    {
                        text10 = String.valueOf(Integer.parseInt(text4.substring(text4.length() - 9)));
                        text7 = " و " + GetFigures(text10);
                    }
                    text10 = String.valueOf(Integer.parseInt(text4.substring(  0, 2)));
                    text7 = GetFigures(text10) + strMiliard + text7;
                    break;
                case 12:
                    if (Integer.parseInt(text4.substring(text4.length() - 9)) > 0)
                    {
                        text10 = text4.substring(text4.length() - 9);
                        text7 = " و " + GetFigures(text10);
                    }
                    text10 = String.valueOf(Integer.parseInt(text4.substring(  0, 3)));
                    text7 = GetFigures(text10) + strMiliard_ + text7;
                    break;

                case 13:
                    if (Integer.parseInt(text4.substring(text4.length() - 12)) > 0)
                    {
                        text10 = text4.substring(text4.length() - 12);
                        text7 = " و " + GetFigures(text10);
                    }
                    text10 = String.valueOf(Integer.parseInt(text4.substring(  0, 1)));
                    text7 = GetFigures(text10) + strTriliun + text7;

                    break;

                case 14:
                    if (Integer.parseInt(text4.substring(text4.length() - 12)) > 0)
                    {
                        text10 = text4.substring(text4.length() - 12);
                        text7 = " و " + GetFigures(text10);
                    }
                    text10 = String.valueOf(Integer.parseInt(text4.substring(  0, 2)));
                    text7 = GetFigures(text10) + strTriliun + text7;
                    break;

                case 15:
                    if (Integer.parseInt(text4.substring(text4.length() - 12)) > 0)
                    {
                        text10 = text4.substring(text4.length() - 12);
                        text7 = " و " + GetFigures(text10);
                    }
                    text10 = String.valueOf(Integer.parseInt(text4.substring(  0, 3)));
                    text7 = GetFigures(text10) + strTriliun_ + text7;
                    break;
            }
            String text3 = text7 + " ";

            return text3;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
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

    public void ActivityDialogShowSuccess()
    {
        try {
            ActivityDialogSuccess=new Dialog(FinalCheackOutActivity.this);
            ActivityDialogSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(ActivityDialogSuccess.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ActivityDialogSuccess.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
            ActivityDialogSuccess.setContentView(R.layout.success_dialog);

            TextView TitleSuccessDialog=(TextView)ActivityDialogSuccess.findViewById(R.id.TitleSuccessDialog);
            TextView TextSuccessDialog=(TextView)ActivityDialogSuccess.findViewById(R.id.TextSuccessDialog);

            TitleSuccessDialog.setTypeface(font_Bold);
            TextSuccessDialog.setTypeface(font_Bold);

            ActivityDialogSuccess.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void ActivityDialogShowUnsuccess(String text)
    {
        try {
            ActivityDialogUnsuccess=new Dialog(FinalCheackOutActivity.this);
            ActivityDialogUnsuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(ActivityDialogUnsuccess.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ActivityDialogUnsuccess.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
            ActivityDialogUnsuccess.setContentView(R.layout.unsuccess_dialog);

            TextView TitleUnsuccessDialog=(TextView)ActivityDialogUnsuccess.findViewById(R.id.TitleUnsuccessDialog);
            TextView TextUnsuccessDialog=(TextView)ActivityDialogUnsuccess.findViewById(R.id.TextUnsuccessDialog);

            TitleUnsuccessDialog.setTypeface(font_Bold);
            TextUnsuccessDialog.setTypeface(font_Bold);

            TextUnsuccessDialog.setText(text);

            ActivityDialogUnsuccess.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
