package com.adcompany.androidapp.MainPage;

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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcompany.androidapp.FarsiNumberChange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.adcompany.androidapp.R;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import co.ronash.pushe.Pushe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ListActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    Typeface font_Medium;
    Typeface font_Bold;

    BottomNavigationView bottomNavigationView;

    TextView HeaderCheackOut;
    public TextView TextViewMore;
    TextView TextViewMoneyFarsi;
    TextView TextViewMoneyEnglish;
    TextView TextViewTitleMessage;
    TextView TextViewTextMessage;

    ConstraintLayout ConstraintMoney;
    LinearLayout LinearMessage;
    LinearLayout LinearLayoutList;
    public LinearLayout LinearMore;
    public LinearLayout LinearLayoutCategory;
    public LinearLayout LinearLayoutBillboard;

    public HorizontalScrollView ScrollCategory;

    Button CashButton;
    Button MessageButton;

    ImageView ImageViewAccount;
    ImageView ImageViewBack;
    public ImageView ImageViewMore;

    public int CategoryID=-1;

    public boolean IsExit;

    public Dialog loading;
    public Dialog ActivityDialogExit;
    public Dialog ActivityDialogBilboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler,new IntentFilter("com.adcompany.androidapp_FCM-MESSAGE"));
        setContentView(R.layout.activity_list);

        context=getApplicationContext();

        IsExit=true;

        setStatusBarGradiant(ListActivity.this);

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation_view);
        HeaderCheackOut=(TextView)findViewById(R.id.HeaderCheackOut);
        TextViewMoneyFarsi=(TextView)findViewById(R.id.TextViewMoneyFarsi);
        TextViewMoneyEnglish=(TextView)findViewById(R.id.TextViewMoneyEnglish);
        TextViewMore=(TextView)findViewById(R.id.TextViewMore);
        TextViewTitleMessage=(TextView)findViewById(R.id.TextViewTitleMessage);
        TextViewTextMessage=(TextView)findViewById(R.id.TextViewTextMessage);
        LinearMessage=(LinearLayout) findViewById(R.id.LinearMessage);
        LinearLayoutCategory=(LinearLayout) findViewById(R.id.LinearLayoutCategory);
        LinearLayoutBillboard=(LinearLayout) findViewById(R.id.LinearLayoutBillboard);
        LinearLayoutList=(LinearLayout) findViewById(R.id.LinearLayoutList);
        LinearMore=(LinearLayout) findViewById(R.id.LinearMore);
        ConstraintMoney=(ConstraintLayout) findViewById(R.id.ConstraintMoney);
        CashButton=(Button) findViewById(R.id.CashButton);
        MessageButton=(Button) findViewById(R.id.MessageButton);
        ImageViewAccount=(ImageView) findViewById(R.id.ImageViewAccount);
        ImageViewBack=(ImageView) findViewById(R.id.ImageViewBack);
        ImageViewMore=(ImageView) findViewById(R.id.ImageViewMore);
        ScrollCategory=(HorizontalScrollView)findViewById(R.id.ScrollCategory);

        font_Medium= Typeface.createFromAsset(ListActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(ListActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");

        HeaderCheackOut.setTypeface(font_Bold);
        TextViewMore.setTypeface(font_Bold);
        CashButton.setTypeface(font_Bold);
        MessageButton.setTypeface(font_Bold);
        TextViewMoneyFarsi.setTypeface(font_Bold);
        TextViewTitleMessage.setTypeface(font_Bold);
        TextViewTextMessage.setTypeface(font_Bold);

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

        LinearMore.setVisibility(View.GONE);


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


        bottomNavigationView.setSelectedItemId(R.id.navigation_list);

        for (int i = 0; i <bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            SpannableStringBuilder spannableTitle = new SpannableStringBuilder(menuItem.getTitle());
            spannableTitle.setSpan(new CustomTypefaceSpan("" , font_Bold), 0 , spannableTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            menuItem.setTitle(spannableTitle);
        }

        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View view = bottomNavigationMenuView.getChildAt(3);

        view.setBackgroundResource(R.drawable.list_bg);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.navigation_guide)
                {
                    startActivity(new Intent(getBaseContext(), GuideActivity.class));
                    overridePendingTransition(0, 0);
                    IsExit = false;
                }
                if (menuItem.getItemId()==R.id.navigation_check_out)
                {
                    startActivity(new Intent(getBaseContext(), CheckOutActivity.class));
                    overridePendingTransition(0, 0);
                    IsExit = false;
                }
                if (menuItem.getItemId()==R.id.navigation_video)
                {
                    startActivity(new Intent(getBaseContext(), VideoActivity.class));
                    overridePendingTransition(0, 0);
                    IsExit = false;
                }
                if (menuItem.getItemId()==R.id.navigation_advertising)
                {
                    startActivity(new Intent(getBaseContext(), AdvertisingActivity.class));
                    overridePendingTransition(0, 0);
                    IsExit = false;
                }
                return false;
            }
        });

        ImageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextViewMore.callOnClick();
            }
        });

        TextViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int visible = 0;
                    final int childCount = LinearLayoutList.getChildCount();
                    for (int j = 0; j < childCount; j++) {
                        View view1 = LinearLayoutList.getChildAt(j);
                        if (view1.getVisibility() == View.GONE && visible <=10 )
                        {
                            visible ++ ;
                            view1.setVisibility(View.VISIBLE);
                        }
                        if (visible == 10)
                            break;
                    }
                    boolean MoreHidden = false;
                    for (int j = 0; j < childCount; j++) {
                        View view1 = LinearLayoutList.getChildAt(j);
                        if (view1.getVisibility() == View.GONE )
                        {
                            MoreHidden = false;
                        }
                        else
                        {
                            MoreHidden = true;
                        }
                    }
                    if (MoreHidden)
                    {
                        TextViewMore.setVisibility(View.INVISIBLE);
                        ImageViewMore.setVisibility(View.INVISIBLE);
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
                            .url("http://adcompany.ir/api/billboard/category")
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
                                    Toast.makeText(ListActivity.this, "اتصال با مشکل مواجه شد.", Toast.LENGTH_LONG).show();
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
                                                    if (data!=null)
                                                    {
                                                        LinearLayout.LayoutParams dim=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        for (int i=0;i<data.length();i++)
                                                        {
                                                            String title=data.getJSONObject(i).getString("title");
                                                            int id=data.getJSONObject(i).getInt("id");

                                                            TextView textView = new TextView(ListActivity.this);
                                                            textView.setText(" "+title);
                                                            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                                            textView.setPadding(10,10,10,10);
                                                            textView.setTextColor(ListActivity.this.getResources().getColor(R.color.text_color_category));
                                                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                            textView.setTextSize(17);
                                                            textView.setTypeface(font_Bold, Typeface.BOLD);
                                                            textView.setBackgroundResource(R.drawable.black_around_white_bg);
                                                            textView.setLayoutParams(dim);
                                                            textView.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    try {
                                                                        TextViewMore.setVisibility(View.VISIBLE);
                                                                        ImageViewMore.setVisibility(View.VISIBLE);
                                                                        final int childCount = LinearLayoutCategory.getChildCount();
                                                                        for (int j = 0; j < childCount; j++) {
                                                                            View view1 = LinearLayoutCategory.getChildAt(j);
                                                                            view1.setBackgroundResource(R.drawable.black_around_white_bg);
                                                                        }
                                                                        CategoryID=id;
                                                                        v.setBackgroundResource(R.drawable.select_gradiant_center_bg);
                                                                        GetBillboard(CategoryID);
                                                                    }catch (Exception e)
                                                                    {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                            LinearLayoutCategory.addView(textView);
                                                        }

                                                        TextView textView = new TextView(ListActivity.this);
                                                        dim.setMargins(20,10,20,10);
                                                        textView.setText(" همه");
                                                        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                                        textView.setPadding(10,10,10,10);
                                                        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                        textView.setTextSize(17);
                                                        textView.setTextColor(ListActivity.this.getResources().getColor(R.color.text_color_category));
                                                        textView.setTypeface(font_Bold, Typeface.BOLD);
                                                        textView.setBackgroundResource(R.drawable.black_around_white_bg);
                                                        textView.setLayoutParams(dim);
                                                        textView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                try {
                                                                    TextViewMore.setVisibility(View.VISIBLE);
                                                                    ImageViewMore.setVisibility(View.VISIBLE);
                                                                    final int childCount = LinearLayoutCategory.getChildCount();
                                                                    for (int j = 0; j < childCount; j++) {
                                                                        View view1 = LinearLayoutCategory.getChildAt(j);
                                                                        view1.setBackgroundResource(R.drawable.black_around_white_bg);
                                                                    }
                                                                    CategoryID=-1;
                                                                    v.setBackgroundResource(R.drawable.select_gradiant_center_bg);
                                                                    GetBillboard(CategoryID);
                                                                }catch (Exception e)
                                                                {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                        LinearLayoutCategory.addView(textView);


                                                        textView.callOnClick();

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
                                if (!isFinishing())
                                { try
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
                                                Toast.makeText(ListActivity.this, "مشکلی در سرور به وجود آمده لطفا چند دقیقه دیگر امتحان کنید", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }catch (Exception e)
                                    {
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


        ImageViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(ListActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                startActivity(new Intent(ListActivity.this, AccountActivity.class));
            }
        });
        ImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation Click = AnimationUtils.loadAnimation(ListActivity.this,R.anim.click);
                    v.startAnimation(Click);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });
    }

    public void GetBillboard(int category_id)
    {
        Log.d("looog", String.valueOf(category_id));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient();

                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.connectTimeout(15, TimeUnit.SECONDS);
                    builder.readTimeout(15, TimeUnit.SECONDS);
                    builder.writeTimeout(15, TimeUnit.SECONDS);

                    RequestBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("category_id", String.valueOf(category_id))
                            .build();

                    Request request = new Request.Builder()
                            .url("http://adcompany.ir/api/billboard/list")
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
                                    Toast.makeText(ListActivity.this, "اتصال با مشکل مواجه شد.", Toast.LENGTH_LONG).show();
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
                                                    LinearLayoutList.removeAllViews();
                                                    if (data!=null)
                                                    {
                                                        if (data.length()==0)
                                                        {
                                                            LinearMore.setVisibility(View.GONE);

                                                            Toast.makeText(ListActivity.this, "بیلبوردی وجود ندارد", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                            LinearMore.setVisibility(View.VISIBLE);

                                                        for (int i=0;i<data.length();i++)
                                                        {
                                                            int id=data.getJSONObject(i).getInt("id");
                                                            String title=data.getJSONObject(i).getString("title");
                                                            String phone=data.getJSONObject(i).getString("phone");

                                                            View inflatedView = getLayoutInflater().inflate(R.layout.bilboard_layout, LinearLayoutList, false);

                                                            ImageView ImageBillboard = (ImageView) inflatedView.findViewById(R.id.ImageBillboard);
                                                            ImageView ImageViewOpen = (ImageView) inflatedView.findViewById(R.id.ImageViewOpen);
                                                            LinearLayout LinearLayoutBillboardDetails = (LinearLayout) inflatedView.findViewById(R.id.LinearLayoutBillboardDetails);

                                                            TextView TextViewTitleBillboard = (TextView) inflatedView.findViewById(R.id.TextViewTitleBillboard);
                                                            TextView TextViewDesBillboard = (TextView) inflatedView.findViewById(R.id.TextViewDesBillboard);
                                                            TextView TextViewLinkBillboard = (TextView) inflatedView.findViewById(R.id.TextViewLinkBillboard);

                                                            TextViewTitleBillboard.setTypeface(font_Bold);
                                                            TextViewDesBillboard.setTypeface(font_Bold);
                                                            TextViewLinkBillboard.setTypeface(font_Bold);

                                                            TextViewTitleBillboard.setTypeface(font_Bold);

                                                            try {
                                                                LoadImageFromUrl(phone,ImageBillboard);
                                                            }catch (Exception e)
                                                            {
                                                                e.printStackTrace();
                                                            }

                                                            TextViewTitleBillboard.setText(title);

                                                            ImageViewOpen.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    if (LinearLayoutBillboardDetails.getVisibility() == View.VISIBLE)
                                                                    {
                                                                        LinearLayoutBillboardDetails.setVisibility(View.GONE);
                                                                        ImageViewOpen.setImageResource(R.drawable.more);
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
                                                                        loading = new Dialog(ListActivity.this);
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

                                                                                    RequestBody body = new MultipartBody.Builder()
                                                                                            .setType(MultipartBody.FORM)
                                                                                            .addFormDataPart("id", String.valueOf(id))
                                                                                            .build();

                                                                                    Request request = new Request.Builder()
                                                                                            .url("http://adcompany.ir/api/billboard/detail")
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
                                                                                                    Toast.makeText(ListActivity.this, "اتصال با مشکل مواجه شد.", Toast.LENGTH_LONG).show();
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                        @Override
                                                                                        public void onResponse(Call call, Response response) throws IOException {
                                                                                            try {
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
                                                                                                                        if (data!=null)
                                                                                                                        {
                                                                                                                            String title=data.getJSONObject(0).getString("title");
                                                                                                                            String phone=data.getJSONObject(0).getString("phone");
                                                                                                                            String link=data.getJSONObject(0).getString("link");
                                                                                                                            String description=data.getJSONObject(0).getString("description");

                                                                                                                            LinearLayoutBillboardDetails.setVisibility(View.VISIBLE);
                                                                                                                            ImageViewOpen.setImageResource(R.drawable.up);

                                                                                                                            try {
                                                                                                                                LoadImageFromUrl(phone,ImageBillboard);
                                                                                                                            }catch (Exception e)
                                                                                                                            {
                                                                                                                                e.printStackTrace();
                                                                                                                            }

                                                                                                                            try {
                                                                                                                                TextViewTitleBillboard.setText(title);
                                                                                                                            }catch (Exception e)
                                                                                                                            {
                                                                                                                                e.printStackTrace();
                                                                                                                            }

                                                                                                                            try {
                                                                                                                                TextViewDesBillboard.setText(description);
                                                                                                                            }catch (Exception e)
                                                                                                                            {
                                                                                                                                e.printStackTrace();
                                                                                                                            }

                                                                                                                            try {
                                                                                                                                TextViewLinkBillboard.setText(link);
                                                                                                                            }catch (Exception e)
                                                                                                                            {
                                                                                                                                e.printStackTrace();
                                                                                                                            }

                                                                                                                            ActivityDialogBilboard.show();
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
                                                                                                    if (!isFinishing())
                                                                                                    {
                                                                                                        try
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
                                                                                                                    Toast.makeText(ListActivity.this, "مشکلی در سرور به وجود آمده لطفا چند دقیقه دیگر امتحان کنید", Toast.LENGTH_LONG).show();
                                                                                                                }
                                                                                                            });
                                                                                                        }catch (Exception e)
                                                                                                        {
                                                                                                            e.printStackTrace();
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }catch (Exception e)
                                                                                            {
                                                                                                try {
                                                                                                    if (loading.isShowing())
                                                                                                        loading.dismiss();
                                                                                                }catch (Exception e1)
                                                                                                {
                                                                                                    e1.printStackTrace();
                                                                                                }
                                                                                                Toast.makeText(ListActivity.this, "مشکلی در سرور به وجود آمده لطفا چند دقیقه دیگر امتحان کنید", Toast.LENGTH_LONG).show();
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

                                                            LinearLayoutList.addView(inflatedView);
                                                        }

                                                        try {
                                                            final int childCount = LinearLayoutList.getChildCount();
                                                            for (int j = 0; j < childCount; j++) {
                                                                View view1 = LinearLayoutList.getChildAt(j);
                                                                view1.setVisibility(View.GONE);
                                                            }
                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }

                                                        try {
                                                            for (int j = 0; j < 3; j++) {
                                                                View view1 = LinearLayoutList.getChildAt(j);
                                                                view1.setVisibility(View.VISIBLE);
                                                            }
                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }

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
                                if (!isFinishing())
                                {
                                    try
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
                                                Toast.makeText(ListActivity.this, "مشکلی در سرور به وجود آمده لطفا چند دقیقه دیگر امتحان کنید", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }catch (Exception e)
                                    {
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
            ActivityDialogExit=new Dialog(ListActivity.this);
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
    private void LoadImageFromUrl(String url,ImageView imageView)
    {
        try {
            Picasso.with(this).load(url).placeholder(R.drawable.ad)
                    .error(R.drawable.ad)
                    .into(imageView, new com.squareup.picasso.Callback() {
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
                            Toast.makeText(ListActivity.this, "اتصال در هنگام باز کردن تصویر با مشکل مواجه شد.", Toast.LENGTH_LONG).show();
                        }
                    });
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
