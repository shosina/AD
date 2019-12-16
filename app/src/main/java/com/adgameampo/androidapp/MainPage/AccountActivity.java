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
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import com.adgameampo.androidapp.ChangePasswordActivity;
import com.adgameampo.androidapp.FarsiNumberChange;
import com.adgameampo.androidapp.MainActivity;


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

public class AccountActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    Typeface font_Medium;
    Typeface font_Bold;

    public TextView HeaderAccount;
    public TextView TextViewChangePasswordAccount;
    public TextView TextViewChangeUserNameAccount;
    public TextView TextViewPhoneNumberTitleAccount;
    public TextView TextViewPhoneNumberAccount;
    public TextView TextViewShareAccount;
    public TextView TextViewReagentTitleAccount;
    public TextView TextViewReagentAccount;
    public TextView TextViewStateTitleAccount;
    public TextView TextViewStateAccount;
    public TextView TextViewCityTitleAccount;
    public TextView TextViewCityAccount;
    public TextView TextViewLogOutAccount;
    TextView TextViewMoneyFarsi;
    TextView TextViewMoneyEnglish;
    TextView TextViewTitleMessage;
    TextView TextViewTextMessage;

    ConstraintLayout ConstraintMoney;
    LinearLayout LinearMessage;

    public Button CashButton;
    public Button MessageButton;

    public EditText EditTextFirstNameAccount;
    public EditText EditTextLastNameAccount;

    public ImageView ImageViewBack;
    public ImageView ImageViewAccount;
    public ImageView ChangePasswordAccountButton;
    public ImageView ShareAccountButton;
    public ImageView LogOutAccountButton;

    public String first_name;
    public String last_name ;
    public String mobile ;
    public String city ;
    public String state ;
    public String reagent ;

    Intent ShareIntent;

    public Dialog loading;
    public Dialog ActivityDialogExit;
    public Dialog ActivityDialogExitAccount;


    @Override
    protected void onStart() {
        super.onStart();
        GetAccount();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler,new IntentFilter("com.adgameampo.androidapp_FCM-MESSAGE"));
        setContentView(R.layout.activity_account);

        context=getApplicationContext();

        setStatusBarGradiant(AccountActivity.this);

        font_Medium= Typeface.createFromAsset(AccountActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(AccountActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");

        HeaderAccount=(TextView)findViewById(R.id.HeaderAccount);
        TextViewChangePasswordAccount=(TextView)findViewById(R.id.TextViewChangePasswordAccount);
        TextViewChangeUserNameAccount=(TextView)findViewById(R.id.TextViewChangeUserNameAccount);
        TextViewPhoneNumberTitleAccount=(TextView)findViewById(R.id.TextViewPhoneNumberTitleAccount);
        TextViewPhoneNumberAccount=(TextView)findViewById(R.id.TextViewPhoneNumberAccount);
        TextViewShareAccount=(TextView)findViewById(R.id.TextViewShareAccount);
        TextViewReagentTitleAccount=(TextView)findViewById(R.id.TextViewReagentTitleAccount);
        TextViewReagentAccount=(TextView)findViewById(R.id.TextViewReagentAccount);
        TextViewStateAccount=(TextView)findViewById(R.id.TextViewStateAccount);
        TextViewStateTitleAccount=(TextView)findViewById(R.id.TextViewStateTitleAccount);
        TextViewCityTitleAccount=(TextView)findViewById(R.id.TextViewCityTitleAccount);
        TextViewCityAccount=(TextView)findViewById(R.id.TextViewCityAccount);
        TextViewLogOutAccount=(TextView)findViewById(R.id.TextViewLogOutAccount);
        TextViewMoneyFarsi=(TextView)findViewById(R.id.TextViewMoneyFarsi);
        TextViewMoneyEnglish=(TextView)findViewById(R.id.TextViewMoneyEnglish);
        TextViewTitleMessage=(TextView)findViewById(R.id.TextViewTitleMessage);
        TextViewTextMessage=(TextView)findViewById(R.id.TextViewTextMessage);
        CashButton=(Button) findViewById(R.id.CashButton);
        MessageButton=(Button) findViewById(R.id.MessageButton);
        EditTextFirstNameAccount=(EditText) findViewById(R.id.EditTextFirstNameAccount);
        EditTextLastNameAccount=(EditText) findViewById(R.id.EditTextLastNameAccount);
        ImageViewBack=(ImageView) findViewById(R.id.ImageViewBack);
        ImageViewAccount=(ImageView) findViewById(R.id.ImageViewAccount);
        ChangePasswordAccountButton=(ImageView) findViewById(R.id.ChangePasswordAccountButton);
        ShareAccountButton=(ImageView) findViewById(R.id.ShareAccountButton);
        LogOutAccountButton=(ImageView) findViewById(R.id.LogOutAccountButton);
        LinearMessage=(LinearLayout) findViewById(R.id.LinearMessage);
        ConstraintMoney=(ConstraintLayout) findViewById(R.id.ConstraintMoney);

        HeaderAccount.setTypeface(font_Bold);
        TextViewChangePasswordAccount.setTypeface(font_Bold);
        TextViewChangeUserNameAccount.setTypeface(font_Bold);
        TextViewPhoneNumberTitleAccount.setTypeface(font_Bold);
        TextViewPhoneNumberAccount.setTypeface(font_Bold);
        TextViewShareAccount.setTypeface(font_Bold);
        TextViewReagentTitleAccount.setTypeface(font_Bold);
        TextViewReagentAccount.setTypeface(font_Bold);
        TextViewStateAccount.setTypeface(font_Bold);
        TextViewStateTitleAccount.setTypeface(font_Bold);
        TextViewCityTitleAccount.setTypeface(font_Bold);
        TextViewCityAccount.setTypeface(font_Bold);
        TextViewLogOutAccount.setTypeface(font_Bold);
        CashButton.setTypeface(font_Bold);
        MessageButton.setTypeface(font_Bold);
        EditTextFirstNameAccount.setTypeface(font_Bold);
        EditTextLastNameAccount.setTypeface(font_Bold);
        TextViewMoneyFarsi.setTypeface(font_Bold);
        TextViewTitleMessage.setTypeface(font_Bold);
        TextViewTextMessage.setTypeface(font_Bold);
//        TextViewMoneyEnglish.setTypeface(font_Bold);

        Pushe.initialize(this,true);

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

        int money = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getInt("money",0);
        String money_string=String.valueOf(money);
        String money_farsi= FarsiNumberChange.changefarsi(money_string);
        TextViewMoneyFarsi.setText(money_farsi);
        TextViewMoneyEnglish.setText(money_string);

        String Title = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("title_message","خــالی");
        String Message = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("text_message","در حال حاضر هیچ پیامی وجود ندارد.");

        TextViewTitleMessage.setText(Title);
        TextViewTextMessage.setText(Message);


        LinearMessage.setVisibility(View.GONE);
        ConstraintMoney.setVisibility(View.GONE);

        ChangePasswordAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(AccountActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    Intent intent = new Intent(getBaseContext(), ChangePasswordActivity.class);
                    intent.putExtra("isAccountActivity", true);
                    startActivity(intent);
                    finish();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        ShareAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(AccountActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    String Link ="www.adcompany.ir";
                    String text = "شما به نصب اپلیکیشن آد دعوت شده اید. لطفا در زمان ثبت نام کد زیر را وارد کنید."+"\n"+"کد دعوت :"+"\n"+reagent+"\n"+"برای دانلود اپلیکیشن به سایت زیر مراجعه کنید :"+"\n"+Link;
                    ShareIntent = new Intent(Intent.ACTION_SEND);
                    ShareIntent.setType("text/plain");
                    ShareIntent.putExtra(Intent.EXTRA_SUBJECT,"شما به اپلیکیشن آد دعوت شده اید !!!!!");
                    ShareIntent.putExtra(Intent.EXTRA_TEXT,text);
                    startActivity(Intent.createChooser(ShareIntent,"اشتراک از طریق"));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        LogOutAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(AccountActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    ActivityDialogShowExitAccount();
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
                    final Animation Click = AnimationUtils.loadAnimation(AccountActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try
                {
                    startActivity(new Intent(getBaseContext(), AccountActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        ImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(AccountActivity.this,R.anim.click);
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
        ActivityDialogShowExit();
    }

    public void ActivityDialogShowExit()
    {
        try {
            ActivityDialogExit=new Dialog(AccountActivity.this);
            ActivityDialogExit.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(ActivityDialogExit.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ActivityDialogExit.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
            ActivityDialogExit.setContentView(R.layout.question_dialog);

            ImageView ExitYes = (ImageView)ActivityDialogExit.findViewById(R.id.ExitYes);
            ImageView ExitNo = (ImageView)ActivityDialogExit.findViewById(R.id.ExitNo);
            TextView TitleExit=(TextView)ActivityDialogExit.findViewById(R.id.TitleExit);
            TextView TextExit=(TextView)ActivityDialogExit.findViewById(R.id.TextExit);

            ActivityDialogExit.setCancelable(true);

            TitleExit.setText("ذخــــــیره");
            TextExit.setText("آیا می خواهید تغییرات ذخیره شود؟");

            TitleExit.setTypeface(font_Bold);
            TextExit.setTypeface(font_Bold);

            ExitYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDialogExit.dismiss();
                    SaveChange();
                }
            });
            ExitNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDialogExit.dismiss();
                    finish();
                }
            });

            ActivityDialogExit.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void ActivityDialogShowExitAccount()
    {
        try {
            ActivityDialogExitAccount=new Dialog(AccountActivity.this);
            ActivityDialogExitAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(ActivityDialogExitAccount.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ActivityDialogExitAccount.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
            ActivityDialogExitAccount.setContentView(R.layout.question_dialog);

            ImageView ExitYes = (ImageView)ActivityDialogExitAccount.findViewById(R.id.ExitYes);
            ImageView ExitNo = (ImageView)ActivityDialogExitAccount.findViewById(R.id.ExitNo);
            TextView TitleExit=(TextView)ActivityDialogExitAccount.findViewById(R.id.TitleExit);
            TextView TextExit=(TextView)ActivityDialogExitAccount.findViewById(R.id.TextExit);

            ActivityDialogExitAccount.setCancelable(false);

            TextExit.setText("در صورت خروج از حساب تغییرات ذخیره نمی شود. آیا می خواهید از حساب خود خارج شوید؟");

            TitleExit.setTypeface(font_Bold);
            TextExit.setTypeface(font_Bold);

            ExitYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDialogExitAccount.dismiss();

                    LogOut();

                }
            });
            ExitNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDialogExitAccount.dismiss();
                }
            });

            ActivityDialogExitAccount.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void GetAccount()
    {
        try {
            if (loading.isShowing())
                loading.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        loading = new Dialog(AccountActivity.this);
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
                                    Toast.makeText(AccountActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                        e.printStackTrace();
                                                    }
                                                    JSONObject data=jObject.getJSONObject("data");
                                                    try {
                                                        first_name = data.getString("first_name");
                                                        last_name = data.getString("last_name");
                                                        mobile = data.getString("mobile");
                                                        mobile= FarsiNumberChange.changefarsi(mobile);
                                                        city = data.getString("city");
                                                        state = data.getString("state");
                                                        try {
                                                            reagent = data.getString("reagent");
                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }
                                                        String status = data.getString("status");
                                                        try {
                                                            String unique = data.getString("unique");
                                                            String UniqueID = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("unique","");
                                                            if (!unique.equals(UniqueID))
                                                            {
                                                                LogOut();
                                                                Toast.makeText(AccountActivity.this, "حساب متعلق به دستگاه دیگری می باشد", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }catch (Exception e)
                                                        {
                                                            LogOut();
                                                            Toast.makeText(AccountActivity.this, "حساب متعلق به دستگاه دیگری می باشد", Toast.LENGTH_SHORT).show();
                                                        }
                                                        if (status.contains("غیر"))
                                                        {
                                                            LogOut();
                                                            Toast.makeText(AccountActivity.this, "حساب فعال نمی باشد", Toast.LENGTH_SHORT).show();
                                                        }
                                                        int score=data.getInt("score");
                                                        getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putInt("money",score).apply();
                                                        if (reagent!=null)
                                                            reagent=FarsiNumberChange.changefarsi(reagent);
                                                        EditTextFirstNameAccount.setText(first_name);
                                                        EditTextLastNameAccount.setText(last_name);
                                                        TextViewPhoneNumberAccount.setText(mobile);
                                                        TextViewReagentAccount.setText(reagent);
                                                        TextViewStateAccount.setText(state);
                                                        TextViewCityAccount.setText(city);

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
    public void SaveChange()
    {
        try {
            if (EditTextFirstNameAccount.length() == 0) {
                EditTextFirstNameAccount.setError("نام خود را وارد کنید");
                EditTextFirstNameAccount.requestFocus();
            }
            else if (EditTextLastNameAccount.length() == 0) {
                EditTextLastNameAccount.setError("نام خانوادگی خود را وارد کنید");
                EditTextLastNameAccount.requestFocus();
            }
            else
            {
                try {
                    if (loading.isShowing())
                        loading.dismiss();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                loading = new Dialog(AccountActivity.this);
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

                            OkHttpClient client = new OkHttpClient();

                            OkHttpClient.Builder builder = new OkHttpClient.Builder();
                            builder.connectTimeout(15, TimeUnit.SECONDS);
                            builder.readTimeout(15, TimeUnit.SECONDS);
                            builder.writeTimeout(15, TimeUnit.SECONDS);

                            long user_id = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",0);
                            String password = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("password","12345678");
                            String stateid = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("stateid","0");
                            String cityid = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("cityid","0");
                            String UserIdString= String.valueOf(user_id);
                            RequestBody body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("user_id", UserIdString)
                                    .addFormDataPart("password", password)
                                    .addFormDataPart("password_confirmation", password)
                                    .addFormDataPart("first_name", EditTextFirstNameAccount.getText().toString())
                                    .addFormDataPart("last_name", EditTextLastNameAccount.getText().toString())
                                    .addFormDataPart("state_id", stateid)
                                    .addFormDataPart("city_id", cityid)
                                    .build();

                            Request request = new Request.Builder()
                                    .url("http://adcompany.ir/api/user/register/complete")
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
                                            Toast.makeText(AccountActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                                e.printStackTrace();
                                                            }
                                                            Toast.makeText(AccountActivity.this, "تغییرات با موفقیت ذخیره شد", Toast.LENGTH_LONG).show();
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
                                    } else {
                                        if (!isFinishing()) {
                                            try {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }
                                                JSONObject jObject = new JSONObject(myResponce);
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            Toast.makeText(AccountActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
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
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void LogOut()
    {
        try {
            if (loading.isShowing())
                loading.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        loading = new Dialog(AccountActivity.this);
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
                            Toast.makeText(AccountActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                e.printStackTrace();
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
                                                    Log.d("looog", "logout");
                                                    long user_id = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",0);
                                                    String UserIdString= String.valueOf(user_id);
                                                    Pushe.unsubscribe(AccountActivity.this,UserIdString);
                                                    Pushe.subscribe(AccountActivity.this,"-1");
                                                    Pushe.unsubscribe(AccountActivity.this,"0");
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putLong("user_id",0).apply();
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("cityid", "0").apply();
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("stateid", "0").apply();
                                                    startActivity(new Intent(AccountActivity.this, MainActivity.class));
                                                    finishAffinity();
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
                                                    Log.d("looog", "logout");
                                                    long user_id = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",0);
                                                    String UserIdString= String.valueOf(user_id);
                                                    Pushe.unsubscribe(AccountActivity.this,UserIdString);
                                                    Pushe.subscribe(AccountActivity.this,"-1");
                                                    Pushe.unsubscribe(AccountActivity.this,"0");
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putLong("user_id",0).apply();
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("cityid", "0").apply();
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("stateid", "0").apply();
                                                    startActivity(new Intent(AccountActivity.this, MainActivity.class));
                                                    finishAffinity();
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
    public static void setStatusBarGradiant(Activity activity) {
        try
        {
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
