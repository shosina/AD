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
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.poovam.pinedittextfield.LinePinField;

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

public class VerifyActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    CountDownTimer countDownTimer;

    long  timeLeftInMiliseconds;

    TextView  TimerText;

    Button SendCodeButton;

    LinePinField PinCode;

    TextView TextViewVerifyTitle;
    TextView TextViewVerifyExplain;

    Typeface font_Bold;

    String verify_code;
    public String UniqueID=null;
    public String UniqueAndroidID;

    public Dialog loading;
    public Dialog ActivityDialogExit;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        context=getApplicationContext();


        setStatusBarGradiant(VerifyActivity.this);

        TimerText=(TextView)findViewById(R.id.TimerText);
        SendCodeButton=(Button) findViewById(R.id.SendCodeButton);
        TextViewVerifyTitle=(TextView) findViewById(R.id.TextViewVerifyTitle);
        TextViewVerifyExplain=(TextView) findViewById(R.id.TextViewVerifyExplain);
        PinCode=(LinePinField) findViewById(R.id.PinCode);

        font_Bold=Typeface.createFromAsset(VerifyActivity.context.getAssets(),"fonts/BHoma-ExtraBold.ttf");

        TextViewVerifyTitle.setTypeface(font_Bold);
        TextViewVerifyExplain.setTypeface(font_Bold);
        TimerText.setTypeface(font_Bold);
        PinCode.setTypeface(font_Bold);
        SendCodeButton.setTypeface(font_Bold);


        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void onMessageReceived(String messageText) {
                try {
                    if (!isFinishing())
                    {
                        Log.d("Text",messageText);
                        if (messageText.contains("آد"))
                        {
                            String CodeFromMessage= messageText.replaceAll("[^0-9]", "");
                            Log.d("Text",CodeFromMessage);

                            PinCode.setText(CodeFromMessage);

                            if (ContextCompat.checkSelfPermission(VerifyActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(VerifyActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED)
                                Log.d("looog", "permission not allow");
                            else
                                SendCodeButton.callOnClick();
                        }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        UniqueAndroidID = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        CheckPermissionAndStartIntent();

        StartTimer();

        verify_code= String.valueOf(getIntent().getIntExtra("VERIFY_CODE",0));

        PinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PinCode.removeTextChangedListener(this);
                String Text = PinCode.getText().toString();
                if (!TextUtils.isEmpty(Text))
                {
                    String check=FarsiNumberChange.changefarsi(Text);
                    PinCode.setText(check);
                    PinCode.setSelection(check.length());
                }
                PinCode.addTextChangedListener(this);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        TimerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimerText.getText()=="ارسال مجدد کد")
                {
                    boolean IsMissPhone = getIntent().getBooleanExtra("IsMissPhone",false);
                    boolean IsDontLogin = getIntent().getBooleanExtra("IsDontLogin",false);
                    if (IsMissPhone)
                    {
                        loading = new Dialog(VerifyActivity.this);
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.loading_dialog);
                        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loading.setCancelable(false);
                        loading.setCanceledOnTouchOutside(false);
                        loading.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                String PhoneNumber=getIntent().getStringExtra("PhoneNumberMiss");

                                OkHttpClient client = new OkHttpClient();

                                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                builder.connectTimeout(15, TimeUnit.SECONDS);
                                builder.readTimeout(15, TimeUnit.SECONDS);
                                builder.writeTimeout(15, TimeUnit.SECONDS);

                                assert PhoneNumber != null;
                                RequestBody body = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("mobile",PhoneNumber)
                                        .addFormDataPart("reason", "حساب کاربری غیر فعال شود")
                                        .build();

                                Request request = new Request.Builder()
                                        .url("http://adcompany.ir/api/user/lost/request/device")
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
                                                    Toast.makeText(VerifyActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
                                                }catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }
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
                                        Log.d("looog",myResponce);
                                        if (response.isSuccessful()) {
                                            try {
                                                JSONObject jObject = new JSONObject(myResponce);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if(!isFinishing())
                                                        {
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception e){
                                                                e.printStackTrace();
                                                            }
                                                            JSONObject data = null;
                                                            try {
                                                                data = jObject.getJSONObject("data");
                                                                verify_code = String.valueOf(data.getInt("code"));
                                                                TimerText.setBackgroundResource(R.color.Transparent);
                                                                StartTimer();
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }
                                                });
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            if (!isFinishing()) {
                                                try {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                    JSONObject jObject = new JSONObject(myResponce);

                                                    String error = jObject.getString("error");

                                                    runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            try {
                                                                Toast.makeText(VerifyActivity.this, error, Toast.LENGTH_LONG).show();
                                                            } catch (Exception e) {
                                                                Toast.makeText(VerifyActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                            }
                        }).start();
                    }
                    if (IsDontLogin)
                    {
                        loading = new Dialog(VerifyActivity.this);
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.loading_dialog);
                        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loading.setCancelable(false);
                        loading.setCanceledOnTouchOutside(false);
                        loading.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                String PhoneNumber=getIntent().getStringExtra("PhoneNumberMiss");
                                String Reason=getIntent().getStringExtra("Reason");

                                OkHttpClient client = new OkHttpClient();

                                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                builder.connectTimeout(15, TimeUnit.SECONDS);
                                builder.readTimeout(15, TimeUnit.SECONDS);
                                builder.writeTimeout(15, TimeUnit.SECONDS);

                                assert PhoneNumber != null;
                                RequestBody body = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("mobile",PhoneNumber)
                                        .addFormDataPart("reason", Reason)
                                        .build();

                                Request request = new Request.Builder()
                                        .url("http://adcompany.ir/api/user/lost/request/device")
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
                                                    Toast.makeText(VerifyActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
                                                }catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }
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
                                        Log.d("looog",myResponce);
                                        if (response.isSuccessful()) {
                                            try {
                                                JSONObject jObject = new JSONObject(myResponce);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if(!isFinishing())
                                                        {
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception e){
                                                                e.printStackTrace();
                                                            }
                                                            JSONObject data = null;
                                                            try {
                                                                data = jObject.getJSONObject("data");
                                                                verify_code = String.valueOf(data.getInt("code"));
                                                                TimerText.setBackgroundResource(R.color.Transparent);
                                                                StartTimer();
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }
                                                });
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            if (!isFinishing()) {
                                                try {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                    JSONObject jObject = new JSONObject(myResponce);

                                                    String error = jObject.getString("error");

                                                    runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            try {
                                                                Toast.makeText(VerifyActivity.this, error, Toast.LENGTH_LONG).show();
                                                            } catch (Exception e) {
                                                                Toast.makeText(VerifyActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                            }
                        }).start();
                    }
                    else
                    {
                        loading = new Dialog(VerifyActivity.this);
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.loading_dialog);
                        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loading.setCancelable(false);
                        loading.setCanceledOnTouchOutside(false);
                        loading.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                String PhoneNumber=getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("phone_number","0");

                                OkHttpClient client = new OkHttpClient();

                                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                builder.connectTimeout(15, TimeUnit.SECONDS);
                                builder.readTimeout(15, TimeUnit.SECONDS);
                                builder.writeTimeout(15, TimeUnit.SECONDS);

                                assert PhoneNumber != null;
                                RequestBody body = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("mobile",PhoneNumber)
                                        .build();

                                Request request = new Request.Builder()
                                        .url("http://adcompany.ir/api/user/verify/resend")
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
                                                    Toast.makeText(VerifyActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
                                                }catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }
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
                                        Log.d("looog",myResponce);
                                        if (response.isSuccessful()) {
                                            try {
                                                JSONObject jObject = new JSONObject(myResponce);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if(!isFinishing())
                                                        {
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception e){
                                                                e.printStackTrace();
                                                            }
                                                            try {
                                                                verify_code= String.valueOf(jObject.getInt("verify_code"));
                                                                TimerText.setBackgroundResource(R.color.Transparent);
                                                                StartTimer();
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                });
                                            }catch (Exception e){
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
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                            Toast.makeText(VerifyActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                            }
                        }).start();
                    }
                }
            }
        });

        SendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EnglishPinCode="";

                EnglishPinCode=EnglishNumberChange.changeEnglish(PinCode.getText().toString());

                if (EnglishPinCode.length()==0)
                {
                    Toast.makeText(VerifyActivity.this, "کد پنج رقمی ارسال شده را وارد کنید", Toast.LENGTH_LONG).show();
                }
                else if (EnglishPinCode.length()!=5)
                {
                    Toast.makeText(VerifyActivity.this, "کد نباید کمتر از پنج رقم باشد", Toast.LENGTH_LONG).show();
                }
                else if (EnglishPinCode.contains("."))
                {
                    Toast.makeText(VerifyActivity.this, "کد فقط شامل رقم می باشد", Toast.LENGTH_LONG).show();
                }
                else 
                {
                    boolean IsMissPhone = getIntent().getBooleanExtra("IsMissPhone",false);
                    boolean IsDontLogin = getIntent().getBooleanExtra("IsDontLogin",false);
                    if (IsMissPhone)
                    {
                        if (verify_code.equals(Objects.requireNonNull(EnglishPinCode)))
                        {
                            loading = new Dialog(VerifyActivity.this);
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

                                        String PhoneNumberEnglish = getIntent().getStringExtra("PhoneNumberMiss");
                                        Log.d("looog", PhoneNumberEnglish);

                                        OkHttpClient client = new OkHttpClient();

                                        OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                        builder.connectTimeout(15, TimeUnit.SECONDS);
                                        builder.readTimeout(15, TimeUnit.SECONDS);
                                        builder.writeTimeout(15, TimeUnit.SECONDS);

                                        RequestBody body = new MultipartBody.Builder()
                                                .setType(MultipartBody.FORM)
                                                .addFormDataPart("mobile", PhoneNumberEnglish)
                                                .addFormDataPart("reason", "حساب کاربری غیر فعال شود")
                                                .addFormDataPart("code", verify_code)
                                                .addFormDataPart("code_user", verify_code)
                                                .build();

                                        Request request = new Request.Builder()
                                                .url("http://adcompany.ir/api/user/lost/device")
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
                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                        Toast.makeText(VerifyActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                                        try {
                                                                            if (loading.isShowing())
                                                                                loading.dismiss();
                                                                        }catch (Exception e){
                                                                            e.printStackTrace();
                                                                        }
                                                                        String message=jObject.getString("message");
                                                                        Toast.makeText(VerifyActivity.this, message, Toast.LENGTH_LONG).show();
                                                                        startActivity(new Intent(VerifyActivity.this, CantLoginActivity.class));
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
                                                else {
                                                    if (!isFinishing()) {
                                                        try {
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception e){
                                                                e.printStackTrace();
                                                            }
                                                            JSONObject jObject = new JSONObject(myResponce);

                                                            String error = jObject.getString("error");

                                                            runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    try {
                                                                        Toast.makeText(VerifyActivity.this, error, Toast.LENGTH_LONG).show();
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
                        else
                        {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        Toast.makeText(VerifyActivity.this, "کد وارد شده صحیح نمی باشد.", Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                    else if (IsDontLogin)
                    {
                        if (verify_code.equals(Objects.requireNonNull(EnglishPinCode)))
                        {
                            loading = new Dialog(VerifyActivity.this);
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

                                        String PhoneNumberEnglish = getIntent().getStringExtra("PhoneNumberMiss");
                                        String Reason = getIntent().getStringExtra("Reason");
                                        Log.d("looog", PhoneNumberEnglish);
                                        Log.d("looog", Reason);

                                        OkHttpClient client = new OkHttpClient();

                                        OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                        builder.connectTimeout(15, TimeUnit.SECONDS);
                                        builder.readTimeout(15, TimeUnit.SECONDS);
                                        builder.writeTimeout(15, TimeUnit.SECONDS);

                                        RequestBody body = new MultipartBody.Builder()
                                                .setType(MultipartBody.FORM)
                                                .addFormDataPart("mobile", PhoneNumberEnglish)
                                                .addFormDataPart("reason", Reason)
                                                .build();

                                        Request request = new Request.Builder()
                                                .url("http://adcompany.ir/api/user/not/login")
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
                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                        Toast.makeText(VerifyActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                                        try {
                                                                            if (loading.isShowing())
                                                                                loading.dismiss();
                                                                        }catch (Exception e){
                                                                            e.printStackTrace();
                                                                        }
                                                                        String message=jObject.getString("message");
                                                                        Toast.makeText(VerifyActivity.this, message, Toast.LENGTH_LONG).show();
                                                                        startActivity(new Intent(VerifyActivity.this, CantLoginActivity.class));
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
                                                else {
                                                    if (!isFinishing()) {
                                                        try {
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception e){
                                                                e.printStackTrace();
                                                            }
                                                            JSONObject jObject = new JSONObject(myResponce);

                                                            String error = jObject.getString("error");

                                                            runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    try {
                                                                        Toast.makeText(VerifyActivity.this, error, Toast.LENGTH_LONG).show();
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
                        else
                        {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        Toast.makeText(VerifyActivity.this, "کد وارد شده صحیح نمی باشد.", Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                    else 
                    {
                        if (ContextCompat.checkSelfPermission(VerifyActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(VerifyActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED)
                        {
                            ActivityCompat.requestPermissions(VerifyActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                        }
                        else if (verify_code.equals(Objects.requireNonNull(EnglishPinCode)))
                        {
                            loading = new Dialog(VerifyActivity.this);
                            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            loading.setContentView(R.layout.loading_dialog);
                            Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            loading.setCancelable(false);
                            loading.setCanceledOnTouchOutside(false);
                            loading.show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String PhoneNumber=getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("phone_number","0");

                                    if (UniqueID==null)
                                        doSomthing();

                                    Log.d("looog", UniqueID);

                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("unique",UniqueID).apply();

                                    OkHttpClient client = new OkHttpClient();

                                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                    builder.connectTimeout(15, TimeUnit.SECONDS);
                                    builder.readTimeout(15, TimeUnit.SECONDS);
                                    builder.writeTimeout(15, TimeUnit.SECONDS);

                                    assert PhoneNumber != null;
                                    RequestBody body = new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("mobile",PhoneNumber)
                                            .addFormDataPart("verify_code",verify_code)
                                            .addFormDataPart("unique",UniqueID)
                                            .build();

                                    Request request = new Request.Builder()
                                            .url("http://adcompany.ir/api/user/register/verify")
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
                                                    Toast.makeText(VerifyActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                            Log.d("looog",myResponce);
                                            if (response.isSuccessful()) {
                                                try {
                                                    JSONObject jObject = new JSONObject(myResponce);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if(!isFinishing())
                                                            {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                                try {
                                                                    long user_id= jObject.getLong("user_id");
                                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putLong("user_id",user_id).apply();

                                                                    Toast.makeText(VerifyActivity.this, "حساب کاربری شما با موفقیت فعال شد", Toast.LENGTH_LONG).show();
                                                                    Intent intent = new Intent(VerifyActivity.this, ProfileActivity.class);
                                                                    intent.putExtra("CompleteProfile",false);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }catch (Exception e){
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                    });
                                                }catch (Exception e){
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
                                                                    Toast.makeText(VerifyActivity.this, "این دستگاه قبلا در سیستم ثبت شده است", Toast.LENGTH_LONG).show();
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
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
                                }
                            }).start();
                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        Toast.makeText(VerifyActivity.this, "کد وارد شده صحیح نمی باشد.", Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }
                

            }
        });
    }

    public void StartTimer ()
    {
        timeLeftInMiliseconds=60000;
        countDownTimer = new CountDownTimer(timeLeftInMiliseconds,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMiliseconds=millisUntilFinished;

                int CurrentTimeInt=(int)(timeLeftInMiliseconds/1000);
                String CurrentTimeString = String.valueOf(CurrentTimeInt);

                if (CurrentTimeString.contains("0"))
                    CurrentTimeString=CurrentTimeString.replaceAll("0","۰");
                if (CurrentTimeString.contains("1"))
                    CurrentTimeString=CurrentTimeString.replaceAll("1","۱");
                if (CurrentTimeString.contains("2"))
                    CurrentTimeString=CurrentTimeString.replaceAll("2","۲");
                if (CurrentTimeString.contains("3"))
                    CurrentTimeString=CurrentTimeString.replaceAll("3","۳");
                if (CurrentTimeString.contains("4"))
                    CurrentTimeString=CurrentTimeString.replaceAll("4","۴");
                if (CurrentTimeString.contains("5"))
                    CurrentTimeString=CurrentTimeString.replaceAll("5","۵");
                if (CurrentTimeString.contains("6"))
                    CurrentTimeString=CurrentTimeString.replaceAll("6","۶");
                if (CurrentTimeString.contains("7"))
                    CurrentTimeString=CurrentTimeString.replaceAll("7","۷");
                if (CurrentTimeString.contains("8"))
                    CurrentTimeString=CurrentTimeString.replaceAll("8","۸");
                if (CurrentTimeString.contains("9"))
                    CurrentTimeString=CurrentTimeString.replaceAll("9","۹");

                TimerText.setText(CurrentTimeString + " ثانیه");

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFinish() {
                TimerText.setText("ارسال مجدد کد");
                TimerText.setBackgroundResource(R.drawable.black_around_bg);
            }
        }.start();
    }

    private void CheckPermissionAndStartIntent() {
        if (ContextCompat.checkSelfPermission(VerifyActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(VerifyActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(VerifyActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
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
                    Toast.makeText(VerifyActivity.this, "لطفا برای مطابقت دستگاه با اکانت این دسترسی را در تنظیمات فعال کنید تا اکانت شما روی دستگاه خودتان ثبت شود و در زمان ورود به مشکل نخورید", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void doSomthing() {
        try {
            UniqueID = getDeviceIMEI(VerifyActivity.this);
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
        ActivityDialogShowExit();
    }

    public void ActivityDialogShowExit()
    {
        ActivityDialogExit=new Dialog(VerifyActivity.this);
        ActivityDialogExit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogExit.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogExit.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
        ActivityDialogExit.setContentView(R.layout.question_dialog);

        ImageView ExitYes = (ImageView)ActivityDialogExit.findViewById(R.id.ExitYes);
        ImageView ExitNo = (ImageView)ActivityDialogExit.findViewById(R.id.ExitNo);
        TextView TitleExit=(TextView)ActivityDialogExit.findViewById(R.id.TitleExit);
        TextView TextExit=(TextView)ActivityDialogExit.findViewById(R.id.TextExit);

        ActivityDialogExit.setCancelable(true);

        TextExit.setText("آیا خارج میشوید؟");

        TitleExit.setTypeface(font_Bold);
        TextExit.setTypeface(font_Bold);

        ExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDialogExit.dismiss();
                startActivity(new Intent(VerifyActivity.this, EnterPhoneNumberActivity.class));
                finish();
            }
        });
        ExitNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
