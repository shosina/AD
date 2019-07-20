package com.adcompany.androidapp;

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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
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

import static com.adcompany.androidapp.MainActivity.IsForgetPass;

public class EnterPhoneNumberActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;


    EditText PhoneNumberText;

    Typeface font_Medium;
    Typeface font_Bold;

    TextView TextViewEnterNumber;

    Button SendCodeButton;

    public Dialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone_number);

        context=getApplicationContext();

        setStatusBarGradiant(EnterPhoneNumberActivity.this);

        PhoneNumberText=(EditText)findViewById(R.id.PhoneNumberText);
        SendCodeButton=(Button) findViewById(R.id.SendCodeButton);
        TextViewEnterNumber=(TextView)findViewById(R.id.TextViewEnterNumber);

        font_Medium= Typeface.createFromAsset(EnterPhoneNumberActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(EnterPhoneNumberActivity.context.getAssets(),"fonts/BHoma-ExtraBold.ttf");

        TextViewEnterNumber.setTypeface(font_Medium);
        PhoneNumberText.setTypeface(font_Bold);
        SendCodeButton.setTypeface(font_Bold);

        CheckPermissionAndStartIntent();

        PhoneNumberText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PhoneNumberText.removeTextChangedListener(this);
                String Text = PhoneNumberText.getText().toString();
                if (!TextUtils.isEmpty(Text))
                {
                    String check=FarsiNumberChange.changefarsi(Text);
                    PhoneNumberText.setText(check);
                    PhoneNumberText.setSelection(check.length());
                }
                PhoneNumberText.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (PhoneNumberText.length() == 0) {
                        PhoneNumberText.setError("شماره موبایل خود را وارد کنید");
                        PhoneNumberText.requestFocus();
                    } else if (PhoneNumberText.length()!=11) {
                        PhoneNumberText.setError("شماره موبایل باید ۱۱ رقم باشد");
                        PhoneNumberText.requestFocus();
                    } else if (!PhoneNumberText.getText().toString().startsWith("۰۹")) {
                        PhoneNumberText.setError("شماره موبایل نامعتبر می باشد");
                        PhoneNumberText.requestFocus();
                    } else {
                        loading = new Dialog(EnterPhoneNumberActivity.this);
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

                                    String PhoneNumberEnglish = EnglishNumberChange.changeEnglish(PhoneNumberText.getText().toString());
                                    Log.d("looog", PhoneNumberEnglish);

                                    String url="";
                                    if (IsForgetPass)
                                    {
                                       url="http://adcompany.ir/api/user/forger/password";
                                    }
                                    else
                                    {
                                        url="http://adcompany.ir/api/user/register";
                                    }

                                    OkHttpClient client = new OkHttpClient();

                                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                    builder.connectTimeout(15, TimeUnit.SECONDS);
                                    builder.readTimeout(15, TimeUnit.SECONDS);
                                    builder.writeTimeout(15, TimeUnit.SECONDS);

                                    RequestBody body = new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("mobile", PhoneNumberEnglish)
                                            .build();

                                    Request request = new Request.Builder()
                                            .url(url)
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
                                                    Toast.makeText(EnterPhoneNumberActivity.this, "اتصال با مشکل مواجه شد.", Toast.LENGTH_LONG).show();
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
                                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("phone_number",PhoneNumberEnglish).apply();
                                                                    if (IsForgetPass)
                                                                    {
                                                                        Toast.makeText(EnterPhoneNumberActivity.this, "پسورد شما با موفقیت تغییر یافت و نتیجه از تغییر پیامک ارسال خواهد شد", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                    else
                                                                    {
                                                                        int verify_code = jObject.getInt("verify_code");
                                                                        Intent intent = new Intent(getBaseContext(), VerifyActivity.class);
                                                                        intent.putExtra("VERIFY_CODE", verify_code);
                                                                        intent.putExtra("IsMissPhone",false);
                                                                        intent.putExtra("IsDontLogin",false);
                                                                        startActivity(intent);
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
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                        JSONObject jObject = new JSONObject(myResponce);
                                                        try {
                                                            JSONArray error = jObject.getJSONArray("error");
                                                            String ErrorMessage = (String) error.get(0);
                                                            runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    try {
                                                                        PhoneNumberText.setError(ErrorMessage);
                                                                        PhoneNumberText.requestFocus();
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            String error = jObject.getString("error");
                                                            runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    try {
                                                                        Toast.makeText(EnterPhoneNumberActivity.this, error, Toast.LENGTH_LONG).show();
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });

                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }

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
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    private void CheckPermissionAndStartIntent() {
        if (ContextCompat.checkSelfPermission(EnterPhoneNumberActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(EnterPhoneNumberActivity.this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(EnterPhoneNumberActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 2);
            Log.d("loooog", "1");
        } else {
            Log.d("loooog", "2");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else{

                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (IsForgetPass)
        {
            startActivity(new Intent(EnterPhoneNumberActivity.this, CantLoginActivity.class));
            finish();
        }
        else
        {
            startActivity(new Intent(EnterPhoneNumberActivity.this, MainActivity.class));
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
