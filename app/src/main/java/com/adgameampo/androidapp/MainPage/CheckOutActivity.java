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
import android.widget.Button;
import android.widget.EditText;
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

public class CheckOutActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    Typeface font_Medium;
    Typeface font_Bold;

    TextView HeaderCheackOut;
    TextView TextViewTitleShaba;
    TextView TextViewIR;
    TextView TextViewMoneyFarsi;
    TextView TextViewMoneyEnglish;
    TextView TextViewTitleMessage;
    TextView TextViewTextMessage;
    TextView TextViewCheackOutExplain;

    ConstraintLayout ConstraintMoney;
    LinearLayout LinearMessage;

    Button CashButton;
    Button MessageButton;
    Button OkAddShabaCardButton;
    Button CancelAddShabaCardButton;
    Button FinalCheckOutButton;

    ImageView ImageViewAccount;
    ImageView ImageViewBack;
    ImageView ImageViewAddShabaCard;

    EditText NameAccountShabaCard;
    EditText ShabaNumber;

    LinearLayout LinearLayoutAddShabaCard;

    BottomNavigationView bottomNavigationView;
    ListView ListViewShabaCard;
    AdapterShabaList adapterShabaList;

    public static long UserID;

    public static ArrayList<ListShaba> ShabaList=new ArrayList<ListShaba>();

    public Dialog loading;
    public Dialog ActivityDialogExit;

    public boolean IsExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler,new IntentFilter("com.adgameampo.androidapp_FCM-MESSAGE"));
        setContentView(R.layout.activity_cheack_out);

        IsExit=true;

        context=getApplicationContext();

        setStatusBarGradiant(CheckOutActivity.this);

        HeaderCheackOut=(TextView)findViewById(R.id.HeaderCheackOut);
        TextViewTitleShaba=(TextView)findViewById(R.id.TextViewTitleShaba);
        TextViewIR=(TextView)findViewById(R.id.TextViewIR);
        TextViewMoneyFarsi=(TextView)findViewById(R.id.TextViewMoneyFarsi);
        TextViewMoneyEnglish=(TextView)findViewById(R.id.TextViewMoneyEnglish);
        TextViewTitleMessage=(TextView)findViewById(R.id.TextViewTitleMessage);
        TextViewTextMessage=(TextView)findViewById(R.id.TextViewTextMessage);
        TextViewCheackOutExplain=(TextView)findViewById(R.id.TextViewCheackOutExplain);
        CashButton=(Button)findViewById(R.id.CashButton);
        MessageButton=(Button)findViewById(R.id.MessageButton);
        OkAddShabaCardButton=(Button)findViewById(R.id.OkAddShabaCardButton);
        CancelAddShabaCardButton=(Button)findViewById(R.id.CancelAddShabaCardButton);
        FinalCheckOutButton=(Button)findViewById(R.id.FinalCheckOutButton);
        ImageViewAccount=(ImageView)findViewById(R.id.ImageViewAccount);
        ImageViewBack=(ImageView)findViewById(R.id.ImageViewBack);
        ImageViewAddShabaCard=(ImageView)findViewById(R.id.ImageViewAddShabaCard);
        NameAccountShabaCard=(EditText)findViewById(R.id.NameAccountShabaCard);
        ShabaNumber=(EditText)findViewById(R.id.ShabaNumber);
        ListViewShabaCard=(ListView) findViewById(R.id.ListViewShabaCard);
        LinearLayoutAddShabaCard=(LinearLayout)findViewById(R.id.LinearLayoutAddShabaCard);
        LinearMessage=(LinearLayout) findViewById(R.id.LinearMessage);
        ConstraintMoney=(ConstraintLayout) findViewById(R.id.ConstraintMoney);

        adapterShabaList=new AdapterShabaList(getApplicationContext(),ShabaList);
        ListViewShabaCard.setAdapter(adapterShabaList);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation_view);

        LinearLayoutAddShabaCard.setVisibility(View.GONE);

        font_Medium= Typeface.createFromAsset(CheckOutActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(CheckOutActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");


        HeaderCheackOut.setTypeface(font_Bold);
        TextViewTitleShaba.setTypeface(font_Bold);
        TextViewIR.setTypeface(font_Bold);
        CashButton.setTypeface(font_Bold);
        CancelAddShabaCardButton.setTypeface(font_Bold);
        OkAddShabaCardButton.setTypeface(font_Bold);
        FinalCheckOutButton.setTypeface(font_Bold);
        CashButton.setTypeface(font_Bold);
        MessageButton.setTypeface(font_Bold);
        NameAccountShabaCard.setTypeface(font_Bold);
        ShabaNumber.setTypeface(font_Bold);
        TextViewMoneyFarsi.setTypeface(font_Bold);
        TextViewTitleMessage.setTypeface(font_Bold);
        TextViewTextMessage.setTypeface(font_Bold);
        TextViewCheackOutExplain.setTypeface(font_Bold);
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

        TextViewCheackOutExplain.setVisibility(View.VISIBLE);


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

        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View view = bottomNavigationMenuView.getChildAt(1);

        view.setBackgroundResource(R.drawable.check_out_bg);

        for (int i = 0; i <bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            SpannableStringBuilder spannableTitle = new SpannableStringBuilder(menuItem.getTitle());
            spannableTitle.setSpan(new CustomTypefaceSpan("" , font_Bold), 0 , spannableTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            menuItem.setTitle(spannableTitle);
        }

        UserID=getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",15);

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
                if (menuItem.getItemId()==R.id.navigation_video)
                {
                    startActivity(new Intent(getBaseContext(), VideoActivity.class));
                    overridePendingTransition(0, 0);
                    IsExit=false;
                    finish();
                }
                if (menuItem.getItemId()==R.id.navigation_list)
                {
                    Intent intent=new Intent(CheckOutActivity.this,ListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    IsExit = false;
                    finish();
                }
//                if (menuItem.getItemId()==R.id.navigation_advertising)
//                {
//                    startActivity(new Intent(getBaseContext(), AdvertisingActivity.class));
//                    overridePendingTransition(0, 0);
//                    IsExit=false;
//                    finish();
//                }
                return false;
            }
        });

        ImageViewAddShabaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(CheckOutActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (LinearLayoutAddShabaCard.getVisibility()==View.GONE)
                            {
                                LinearLayoutAddShabaCard.setVisibility(View.VISIBLE);
                                try {
                                    if (TextViewCheackOutExplain.getVisibility()==View.VISIBLE)
                                        TextViewCheackOutExplain.setVisibility(View.GONE);
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },500);
            }
        });

        CancelAddShabaCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(CheckOutActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (LinearLayoutAddShabaCard.getVisibility()==View.VISIBLE)
                            {
                                LinearLayoutAddShabaCard.setVisibility(View.GONE);
                                try {
                                    if (TextViewCheackOutExplain.getVisibility()==View.GONE)
                                        TextViewCheackOutExplain.setVisibility(View.VISIBLE);
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },500);
            }
        });

        OkAddShabaCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(CheckOutActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    if (NameAccountShabaCard.length() == 0) {
                        NameAccountShabaCard.setError("نام صاحب حساب را وارد کنید");
                        NameAccountShabaCard.requestFocus();
                    }
                    else if (ShabaNumber.length() == 0) {
                        ShabaNumber.setError("شماره حساب را وارد کنید");
                        ShabaNumber.requestFocus();
                    }
                    else if (ShabaNumber.length() != 24) {
                        ShabaNumber.setError("شماره حساب نباید کمتر از ۲۴ عدد باشد");
                        ShabaNumber.requestFocus();
                    }
                    else
                    {
                        boolean HaveEnglishChar = NameAccountShabaCard.getText().toString().matches("^[a-zA-Z1234567890.]*$");
                        if (HaveEnglishChar)
                        {
                            NameAccountShabaCard.setError("نام صاحب حساب باید شامل حروف فارسی باشد");
                            NameAccountShabaCard.requestFocus();
                        }
                        else
                        {
                            SetShabaCard(NameAccountShabaCard.getText().toString(),ShabaNumber.getText().toString());
                        }
                    }
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
                    final Animation Click = AnimationUtils.loadAnimation(CheckOutActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                startActivity(new Intent(CheckOutActivity.this, AccountActivity.class));
            }
        });

        FinalCheckOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CheckOutActivity.this, FinalCheackOutActivity.class));
            }
        });
        ImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(CheckOutActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetShabaCrad();
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
            ActivityDialogExit=new Dialog(CheckOutActivity.this);
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
    public void GetShabaCrad()
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
                                    Toast.makeText(CheckOutActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                    JSONArray data=jObject.getJSONArray("data");
                                                    try {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }
                                                        if (!ShabaList.isEmpty())
                                                            ShabaList.clear();
                                                        if (data!=null)
                                                        {
                                                            for (int i=0;i<data.length();i++)
                                                            {
                                                                long id = data.getJSONObject(i).getLong("id");
                                                                String title=data.getJSONObject(i).getString("title");
                                                                String number=data.getJSONObject(i).getString("number");

                                                                ListShaba listShaba = new ListShaba(id,title,number);
                                                                ShabaList.add(listShaba);
                                                            }
                                                            adapterShabaList=new AdapterShabaList(CheckOutActivity.this,ShabaList);
                                                            ListViewShabaCard.setAdapter(adapterShabaList);
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
                                        Toast.makeText(CheckOutActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
    public void SetShabaCard(String title,String number)
    {
        try {
            if (loading.isShowing())
                loading.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        loading = new Dialog(CheckOutActivity.this);
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
                            .addFormDataPart("number",number)
                            .addFormDataPart("title",title)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://adcompany.ir/api/shaba/store")
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
                                    Toast.makeText(CheckOutActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                    if(!ShabaList.isEmpty())
                                                        ShabaList.clear();
                                                    startActivity(new Intent(getBaseContext(),CheckOutActivity.class));
                                                    overridePendingTransition(0, 0);
                                                    finish();
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
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        try {
                                            if (loading.isShowing())
                                                loading.dismiss();
                                        }catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(CheckOutActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
