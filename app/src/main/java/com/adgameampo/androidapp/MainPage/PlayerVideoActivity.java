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
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.adgameampo.androidapp.R;

import java.io.File;
import java.util.Objects;

public class PlayerVideoActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    Typeface font_Bold;

    public CountDownTimer countDownTimer;

    long  timeLeftInMiliseconds;

    public static VideoView videoView;

    public Dialog loading;
    public Dialog ActivityDialogExit;

    ProgressBar VideoProgressBar;

    ImageView CancelButton;

    public int old_current = 0;
    public int Duration=0;

    public boolean isPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler,new IntentFilter("com.adgameampo.androidapp_FCM-MESSAGE"));
        setContentView(R.layout.activity_player_video);

        context=getApplicationContext();

        videoView = (VideoView)findViewById(R.id.video_view);
        VideoProgressBar=(ProgressBar)findViewById(R.id.VideoProgressBar);
        CancelButton=(ImageView) findViewById(R.id.CancelButton);

        setStatusBarGradiant(PlayerVideoActivity.this);

        isPause=false;

        font_Bold= Typeface.createFromAsset(PlayerVideoActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");

        try {
            if (loading.isShowing())
                loading.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        loading = new Dialog(PlayerVideoActivity.this);
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.loading_dialog);
        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(true);
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
            Log.d("looog", "ClearCache");
        } catch (Exception e) { e.printStackTrace();}

        final String VIDEO_PATH = VideoActivity.VideoLink;
        MediaController mediaController;
        videoView.setVideoPath(VIDEO_PATH);
        mediaController = new MediaController(PlayerVideoActivity.this);
        mediaController.setVisibility(View.GONE);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("video", "setOnErrorListener ");
                return true;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                try {
                    if (loading.isShowing())
                        loading.dismiss();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                videoView.start();
                Duration=videoView.getDuration();
                Log.d("looog", String.valueOf(Duration));
                StartTimer(Duration);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                countDownTimer.cancel();
                VideoProgressBar.setProgress(100);
                startActivity(new Intent(PlayerVideoActivity.this, ExplainVideoActivity.class));
                finish();
            }
        });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void StartTimer (int duration)
    {
        try {
            timeLeftInMiliseconds=duration*10;
            countDownTimer = new CountDownTimer(timeLeftInMiliseconds,100) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    try {
                        timeLeftInMiliseconds=millisUntilFinished;
                        int CurrentTimeInt=(int)(timeLeftInMiliseconds/1000);
                        String CurrentTimeString = String.valueOf(CurrentTimeInt);

                        int new_current=videoView.getCurrentPosition();
                        if (new_current!= old_current)
                        {
                            if (loading.isShowing())
                                loading.dismiss();
                            int progreess = (new_current * 100 / duration);
                            VideoProgressBar.setProgress(progreess);
                            old_current =new_current;
                        }
                        else if (new_current!=0)
                        {
                            if (!loading.isShowing() && !isPause)
                            {
                                loading = new Dialog(PlayerVideoActivity.this);
                                loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                loading.setContentView(R.layout.loading_dialog);
                                Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                loading.setCancelable(false);
                                loading.setCanceledOnTouchOutside(false);
                                loading.show();
                            }
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onFinish() {
                }
            }.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            isPause=true;
            videoView.pause();
            ActivityDialogShowExit();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void ActivityDialogShowExit()
    {
        try {
            ActivityDialogExit=new Dialog(PlayerVideoActivity.this);
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

            boolean isReplay = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("is_replay_video",false);
            if (isReplay)
                TextExit.setText("آیا خارج می شوید؟");
            else
                TextExit.setText("با خروج از ویدیو شما امتیاز این بخش را از دست می دهید.آیا خارج می شوید؟");

            ExitYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDialogExit.dismiss();
                    boolean isReplay = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("is_replay_video",false);
                    if (isReplay)
                    {
                        startActivity(new Intent(PlayerVideoActivity.this, ExplainVideoActivity.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(PlayerVideoActivity.this, VideoActivity.class));
                        finish();
                    }
                }
            });
            ExitNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityDialogExit.isShowing())
                        ActivityDialogExit.dismiss();
                    isPause=false;
                    videoView.start();
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
                Drawable background = activity.getResources().getDrawable(R.drawable.state_bar_splash);
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

                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("isnotification",true).apply();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };
}
