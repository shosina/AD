package com.adgameampo.androidapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import co.ronash.pushe.Pushe;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    Typeface font_Medium;
    Typeface font_Bold;
    Typeface font_semi_Bold;

    Button RegisterButton;
    Button LogInMainButton;
    Button CantLoginMainButton;

    public static boolean IsForgetPass;

    public Dialog ActivityDialogExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=getApplicationContext();

        setStatusBarGradiant(MainActivity.this);

        font_Medium= Typeface.createFromAsset(MainActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(MainActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");
        font_semi_Bold=Typeface.createFromAsset(MainActivity.context.getAssets(),"fonts/BHoma-SemiBold.ttf");

        LogInMainButton=(Button)findViewById(R.id.LogInMainButton);
        RegisterButton=(Button)findViewById(R.id.RegisterButton);
        CantLoginMainButton=(Button)findViewById(R.id.CantLoginMainButton);

        LogInMainButton.setTypeface(font_Bold);
        RegisterButton.setTypeface(font_Bold);
        CantLoginMainButton.setTypeface(font_semi_Bold);

        Pushe.initialize(this,true);

        Pushe.subscribe(this,"-1");

        Pushe.unsubscribe(this,"0");

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsForgetPass=false;
                startActivity(new Intent(MainActivity.this, EnterPhoneNumberActivity.class));
                finish();
            }
        });

        LogInMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        CantLoginMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CantLoginActivity.class));
                finish();
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
        ActivityDialogExit=new Dialog(MainActivity.this);
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
                ActivityDialogExit.dismiss();
                finish();
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
}
