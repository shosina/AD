package com.adcompany.androidapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import co.ronash.pushe.Pushe;

import com.adcompany.androidapp.MainPage.VideoActivity;
import com.cunoraz.gifview.library.GifView;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SplashActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    Typeface font_Bold;

    boolean isExit;

    public Dialog ActivityDialogInternetConnection;

    @Override
    protected void onResume() {
        isExit=false;
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.d("looog", "onStart: ");
        try {
            if (ActivityDialogInternetConnection.isShowing())
                ActivityDialogInternetConnection.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        isExit=false;
        Connection();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context=getApplicationContext();

        font_Bold= Typeface.createFromAsset(SplashActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");

        Pushe.initialize(this,true);

        setStatusBarGradiant(SplashActivity.this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    GifView gifView1 = (GifView) findViewById(R.id.gifView);
                    gifView1.setVisibility(View.VISIBLE);
                    gifView1.setGifResource(R.drawable.loading);
                    gifView1.play();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        },10);

    }

    @Override
    protected void onPause() {
        isExit=true;
        super.onPause();
    }

    @Override
    protected void onStop() {
        isExit=true;
        try {
            if (ActivityDialogInternetConnection.isShowing())
                ActivityDialogInternetConnection.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onStop();
    }

    public void Connection()
    {
        Log.d("loog", "Connection: ");
        long user_id = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",0);
        String UserIdString= String.valueOf(user_id);

        OkHttpClient client = new OkHttpClient();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", UserIdString)
                .build();


        Request request = new Request.Builder()
                .url("http://adcompany.ir/api/user/online")
                .post(body)
                .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImY3ZGFmMmUzMWE4ZmFkZWYzZWU2ZTJjMzRhMjFjNTdkOGI3MjdhZjMxMGE1ZTJlZDM4NmQ4MDQxNDhlZTliNWU1NjVkODMwM2NkODkzYzE4In0.eyJhdWQiOiIxIiwianRpIjoiZjdkYWYyZTMxYThmYWRlZjNlZTZlMmMzNGEyMWM1N2Q4YjcyN2FmMzEwYTVlMmVkMzg2ZDgwNDE0OGVlOWI1ZTU2NWQ4MzAzY2Q4OTNjMTgiLCJpYXQiOjE1NTUwNjE2MjIsIm5iZiI6MTU1NTA2MTYyMiwiZXhwIjoxNTg2Njg0MDIyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.S30gaSmqSaK253snXsnhTvhYgQOcV46m1CY4X_ilRTE0k8OZFO9Dr5Ul3gzNMBpE0kg4j4HGEc4v-DE34n5QyBG-qvg4Hzg4E-G4Yse3ARE-Xam08nHo8055y0rvsEaKElzkccX8KbNeP4bnbK-Fb9J6sF1hOejYqoRxlf8NNAFDMrs2-mqHYWMB5AjZiGugeEIVZU9Aq1SL1KnDIQB-QXbI11yy6KaqZ1XoqvbPWFRSGuYTt_5rzOZhMyuF0EjBCwNTLzSa1WrhCAJimpQgAZljYVmoQsSgdHqQTgbqpYUztonBQ5AAHmcJkW1aEZbAXnDGpewIj2lxUZ_TQe9cAZPItnokor4clXfHHMei8iHK0xQ9v0Lg6I6Jh_M0u71CP-L6lLMgP7B3yhTWJ8PiKO7_NelsP12BNxKTbAj0gkLBEwBiVDPKFTfi7ad_SPwMIxOuFxQwDtFxxIxPoIdr8R0cino9WJzAed0-red6SgXA9JoebDggVSv2EfEI8S2HkMrkfcr_GMnNUSB5indO2bzysrwT_ijLo0ZaJ_DOOY45Q15KexLyl1mjXJt3_fiut-anBVDVewmqwLUXtK9MBcxYwBNPN7ghEogiw9wplrwjGn1D-psbaIGq2UrqZMzJS9msrvk5ygsRzIMyDy0VXUEwQni_KiySkcpev8MEodY")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        client = builder.build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            if (ActivityDialogInternetConnection.isShowing())
                                ActivityDialogInternetConnection.dismiss();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
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
                if (response.isSuccessful()){
                    final String myResponce=response.body().string();
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (ActivityDialogInternetConnection.isShowing())
                                    ActivityDialogInternetConnection.dismiss();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            Pushe.setNotificationOn(SplashActivity.this);

                            Pushe.subscribe(SplashActivity.this,"0");

                            Pushe.unsubscribe(SplashActivity.this,"-1");

                            long user_id = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",0);
                            String UserIdString= String.valueOf(user_id);
                            Pushe.subscribe(SplashActivity.this,UserIdString);
                            try {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void run() {
                                        if(!isFinishing() && !isExit)
                                        {
                                            startActivity(new Intent(SplashActivity.this, VideoActivity.class));
                                            finish();
                                        }
                                    }
                                }, 4000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else {
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (ActivityDialogInternetConnection.isShowing())
                                    ActivityDialogInternetConnection.dismiss();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            Pushe.setNotificationOn(SplashActivity.this);

                            Pushe.subscribe(SplashActivity.this,"-1");

                            Pushe.unsubscribe(SplashActivity.this,"0");
                            try {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void run() {
                                        if(!isFinishing() && !isExit)
                                        {
                                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
                                            {
                                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                startActivity(intent, options.toBundle());
                                                finish();
                                            }
                                            else
                                            {
                                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        }
                                    }
                                }, 4000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.state_bar_bg);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }
    public void ActivityDialogShowInternetConnection()
    {
        ActivityDialogInternetConnection=new Dialog(SplashActivity.this);
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

                Connection();
            }
        });

        ActivityDialogInternetConnection.show();
    }
}