package com.adgameampo.androidapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adgameampo.androidapp.MainPage.VideoActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
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

public class LoginActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    EditText PhoneNumberLoginText;
    EditText PasswordLoginText;

    Button LoginButton;
    Button CantLoginButton;

    Typeface font_Medium;
    Typeface font_Bold;

    public Dialog loading;

    public String UniqueID;
    public String UniqueAndroidID;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context=getApplicationContext();

        setStatusBarGradiant(LoginActivity.this);

        PhoneNumberLoginText=(EditText) findViewById(R.id.PhoneNumberLoginText);
        PasswordLoginText=(EditText) findViewById(R.id.PasswordLoginText);
        CantLoginButton=(Button)findViewById(R.id.CantLoginButton);
        LoginButton=(Button)findViewById(R.id.LoginButton);

        font_Medium= Typeface.createFromAsset(LoginActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(LoginActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");

        PhoneNumberLoginText.setTypeface(font_Bold);
        PasswordLoginText.setTypeface(font_Bold);
        CantLoginButton.setTypeface(font_Bold);
        LoginButton.setTypeface(font_Bold);

        UniqueAndroidID = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        CheckPermissionAndStartIntent();

        PhoneNumberLoginText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PhoneNumberLoginText.removeTextChangedListener(this);
                String Text = PhoneNumberLoginText.getText().toString();
                if (!TextUtils.isEmpty(Text))
                {
                    String check=FarsiNumberChange.changefarsi(Text);
                    PhoneNumberLoginText.setText(check);
                    PhoneNumberLoginText.setSelection(check.length());
                }
                PhoneNumberLoginText.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        CantLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CantLoginActivity.class));
                finish();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (PhoneNumberLoginText.length() == 0) {
                        PhoneNumberLoginText.setError("شماره موبایل خود را وارد کنید");
                        PhoneNumberLoginText.requestFocus();
                    } else if (PhoneNumberLoginText.length()!=11) {
                        PhoneNumberLoginText.setError("شماره موبایل باید ۱۱ رقم باشد");
                        PhoneNumberLoginText.requestFocus();
                    } else if (!PhoneNumberLoginText.getText().toString().startsWith("۰۹")) {
                        PhoneNumberLoginText.setError("شماره موبایل نامعتبر می باشد");
                        PhoneNumberLoginText.requestFocus();
                    }else if (PasswordLoginText.length() == 0) {
                        PasswordLoginText.setError("رمز عبور خود را وارد کنید");
                        PasswordLoginText.requestFocus();
                    } else if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED)
                    {
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                    } else {
                        loading = new Dialog(LoginActivity.this);
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

                                    String PhoneNumberEnglish = EnglishNumberChange.changeEnglish(PhoneNumberLoginText.getText().toString());
                                    Log.d("looog", PhoneNumberEnglish);

                                    if (UniqueID==null)
                                        doSomthing();

                                    Log.d("looog", UniqueID);

                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("unique",UniqueID).apply();

                                    OkHttpClient client = new OkHttpClient();

                                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                    builder.connectTimeout(15, TimeUnit.SECONDS);
                                    builder.readTimeout(15, TimeUnit.SECONDS);
                                    builder.writeTimeout(15, TimeUnit.SECONDS);

                                    RequestBody body = new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("mobile",PhoneNumberEnglish)
                                            .addFormDataPart("password",PasswordLoginText.getText().toString())
                                            .addFormDataPart("unique",UniqueID)
                                            .build();

                                    Request request = new Request.Builder()
                                            .url("http://adcompany.ir/api/user/auth")
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
                                                    Toast.makeText(LoginActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                                    long user_id= jObject.getLong("user_id");
                                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putLong("user_id",user_id).apply();
                                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("password",PasswordLoginText.getText().toString()).apply();
                                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("phone_number",PhoneNumberEnglish).apply();
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
                                                                                        Log.d("looooog", e.toString());
                                                                                        runOnUiThread(new Runnable() {
                                                                                            public void run() {
                                                                                                if (loading.isShowing())
                                                                                                    loading.dismiss();
                                                                                                Toast.makeText(LoginActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                                                                                startActivity(new Intent(LoginActivity.this, VideoActivity.class));
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
                                                                                    }
                                                                                });
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }).start();

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

                                                        String error = jObject.getString("error");

                                                        runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                try {
                                                                    if (loading.isShowing())
                                                                        loading.dismiss();
                                                                    if (error.contains("این حساب کاربری مطعلق به"))
                                                                    {
                                                                        Toast.makeText(LoginActivity.this, "این حساب کاربری در دستگاه دیگری فعال میباشد", Toast.LENGTH_LONG).show();
                                                                    }
                                                                    else
                                                                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });

                                                    } catch (Exception e) {
                                                        JSONObject jObject = null;
                                                        try {
                                                            jObject = new JSONObject(myResponce);
                                                            String message = jObject.getString("message");
                                                            runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    try {
                                                                        if (loading.isShowing())
                                                                            loading.dismiss();
                                                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                        } catch (JSONException e1) {
                                                            e1.printStackTrace();
                                                        }

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
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    private void CheckPermissionAndStartIntent() {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            doSomthing();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doSomthing();
                }
                else{
                    Toast.makeText(LoginActivity.this, "لطفا برای مطابقت دستگاه با اکانت این دسترسی را فعال کنید", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    public void doSomthing() {
        try {
            UniqueID = getDeviceIMEI(LoginActivity.this);
            if (UniqueID==null)
                UniqueID=UniqueAndroidID;
        }
        catch (Exception e)
        {
            if (UniqueID==null)
                UniqueID=UniqueAndroidID;
        }
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceIMEI(Activity activity) {
        try {
            String deviceUniqueIdentifier = null;
            TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                else
                    deviceUniqueIdentifier = tm.getDeviceId();
                if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length())
                    deviceUniqueIdentifier = null;
            }
            return deviceUniqueIdentifier;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
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
