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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adgameampo.androidapp.MainPage.AccountActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

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

public class ChangePasswordActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    TextView TextViewNewPassword;

    EditText NewPasswordText;
    EditText RepeatNewPasswordText;

    Button ChangePasswordButton;

    Typeface font_Medium;
    Typeface font_Bold;

    public Dialog loading;

    public boolean isAccountActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        context=getApplicationContext();

        setStatusBarGradiant(ChangePasswordActivity.this);

        NewPasswordText=(EditText)findViewById(R.id.NewPasswordText);
        RepeatNewPasswordText=(EditText)findViewById(R.id.RepeatNewPasswordText);
        ChangePasswordButton=(Button) findViewById(R.id.ChangePasswordButton);
        TextViewNewPassword=(TextView) findViewById(R.id.TextViewNewPassword);


        font_Medium= Typeface.createFromAsset(ChangePasswordActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(ChangePasswordActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");

        NewPasswordText.setTypeface(font_Bold);
        RepeatNewPasswordText.setTypeface(font_Bold);
        TextViewNewPassword.setTypeface(font_Bold);
        ChangePasswordButton.setTypeface(font_Bold);

        isAccountActivity=getIntent().getBooleanExtra("isAccountActivity",false);

        ChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NewPasswordText.length()==0)
                {
                    NewPasswordText.setError("رمز عبور خود را بنویسید");
                    NewPasswordText.requestFocus();
                }
                else if (NewPasswordText.length()<8)
                {
                    NewPasswordText.setError("رمز عبور باید حداقل ۸ کاراکتر باشد");
                    NewPasswordText.requestFocus();
                }
                else if (!NewPasswordText.getText().toString().equals(RepeatNewPasswordText.getText().toString()))
                {
                    RepeatNewPasswordText.setError("رمز عبور با تاییدیه مطابقت ندارد");
                    RepeatNewPasswordText.requestFocus();
                }
                else
                {
                    loading = new Dialog(ChangePasswordActivity.this);
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

                                String PhoneNumber=getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("phone_number","0");

                                RequestBody body = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("password", NewPasswordText.getText().toString())
                                        .addFormDataPart("password_confirmation", RepeatNewPasswordText.getText().toString())
                                        .addFormDataPart("mobile", PhoneNumber)
                                        .build();

                                Request request = new Request.Builder()
                                        .url("http://adcompany.ir/api/user/change/password")
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
                                                if (loading.isShowing())
                                                    loading.dismiss();
                                                Toast.makeText(ChangePasswordActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                                String message= jObject.getString("message");
                                                                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("password",NewPasswordText.getText().toString()).apply();
                                                                Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                                                                if (isAccountActivity)
                                                                {
                                                                    startActivity(new Intent(ChangePasswordActivity.this, AccountActivity.class));
                                                                    finish();
                                                                }
                                                                else
                                                                {
                                                                    Toast.makeText(ChangePasswordActivity.this, "رمز عبور با موفقیت تغییر یافت.", Toast.LENGTH_LONG).show();
                                                                    startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                                                                    finish();
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
                                        } else {
                                            if (!isFinishing()) {
                                                try {
                                                    JSONObject jObject = new JSONObject(myResponce);
                                                    runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                                Toast.makeText(ChangePasswordActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (isAccountActivity)
        {
            startActivity(new Intent(ChangePasswordActivity.this, AccountActivity.class));
            finish();
        }
        else
        {
            startActivity(new Intent(ChangePasswordActivity.this, CantLoginActivity.class));
            finish();
        }
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
