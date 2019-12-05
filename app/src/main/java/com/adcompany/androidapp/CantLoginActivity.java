package com.adcompany.androidapp;

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

public class CantLoginActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    Button ForgetPasswordButton;
    Button NotifyMissingButton;
    Button OkDontLoginButton;

    TextView TextViewCantLogin;

    public EditText ReasonCantLogin;
    EditText PhoneNumberCantLoginText;

    Typeface font_Medium;
    Typeface font_Bold;

    public Dialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cant_login);

        context=getApplicationContext();

        setStatusBarGradiant(CantLoginActivity.this);

        ForgetPasswordButton=(Button)findViewById(R.id.ForgetPasswordButton);
        NotifyMissingButton=(Button)findViewById(R.id.NotifyMissingButton);
        OkDontLoginButton=(Button)findViewById(R.id.OkDontLoginButton);
        TextViewCantLogin=(TextView)findViewById(R.id.TextViewCantLogin);
        ReasonCantLogin=(EditText) findViewById(R.id.ReasonCantLogin);
        PhoneNumberCantLoginText=(EditText) findViewById(R.id.PhoneNumberCantLoginText);

        font_Medium= Typeface.createFromAsset(CantLoginActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(CantLoginActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");

        ForgetPasswordButton.setTypeface(font_Bold);
        NotifyMissingButton.setTypeface(font_Bold);
        OkDontLoginButton.setTypeface(font_Bold);
        TextViewCantLogin.setTypeface(font_Bold);
        ReasonCantLogin.setTypeface(font_Bold);
        PhoneNumberCantLoginText.setTypeface(font_Bold);

        PhoneNumberCantLoginText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PhoneNumberCantLoginText.removeTextChangedListener(this);
                String Text = PhoneNumberCantLoginText.getText().toString();
                if (!TextUtils.isEmpty(Text))
                {
                    String check=FarsiNumberChange.changefarsi(Text);
                    PhoneNumberCantLoginText.setText(check);
                    PhoneNumberCantLoginText.setSelection(check.length());
                }
                PhoneNumberCantLoginText.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        OkDontLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PhoneNumberCantLoginText.length() == 0) {
                    PhoneNumberCantLoginText.setError("شماره موبایل خود را وارد کنید");
                    PhoneNumberCantLoginText.requestFocus();
                } else if (PhoneNumberCantLoginText.length()!=11) {
                    PhoneNumberCantLoginText.setError("شماره موبایل باید ۱۱ رقم باشد");
                    PhoneNumberCantLoginText.requestFocus();
                } else if (!PhoneNumberCantLoginText.getText().toString().startsWith("۰۹")) {
                    PhoneNumberCantLoginText.setError("شماره موبایل نامعتبر می باشد");
                    PhoneNumberCantLoginText.requestFocus();
                }else if (ReasonCantLogin.length()==0)
                {
                    Toast.makeText(CantLoginActivity.this, "لطفا دلیل وارد نشدن به برنامه را بنویسید", Toast.LENGTH_LONG).show();
                    ReasonCantLogin.requestFocus();
                }
                else
                {
                    loading = new Dialog(CantLoginActivity.this);
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

                                String PhoneNumberEnglish = EnglishNumberChange.changeEnglish(PhoneNumberCantLoginText.getText().toString());
                                Log.d("looog", PhoneNumberEnglish);

                                OkHttpClient client = new OkHttpClient();

                                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                builder.connectTimeout(15, TimeUnit.SECONDS);
                                builder.readTimeout(15, TimeUnit.SECONDS);
                                builder.writeTimeout(15, TimeUnit.SECONDS);

                                RequestBody body = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("mobile", PhoneNumberEnglish)
                                        .addFormDataPart("reason", ReasonCantLogin.getText().toString())
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
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                                Toast.makeText(CantLoginActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                                JSONObject data = jObject.getJSONObject("data");
                                                                int verify_code = data.getInt("code");
                                                                IsForgetPass=false;
                                                                Intent intent = new Intent(getBaseContext(), VerifyActivity.class);
                                                                intent.putExtra("PhoneNumberMiss", PhoneNumberEnglish);
                                                                intent.putExtra("VERIFY_CODE", verify_code);
                                                                intent.putExtra("IsMissPhone",false);
                                                                intent.putExtra("IsDontLogin",true);
                                                                intent.putExtra("Reason",ReasonCantLogin.getText().toString());
                                                                startActivity(intent);
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
                                                                Toast.makeText(CantLoginActivity.this, error, Toast.LENGTH_LONG).show();
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

        ForgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsForgetPass=true;
                startActivity(new Intent(CantLoginActivity.this, EnterPhoneNumberActivity.class));
                finish();
            }
        });

        NotifyMissingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CantLoginActivity.this, MissPhoneActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CantLoginActivity.this, MainActivity.class));
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
