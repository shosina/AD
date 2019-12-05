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
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adcompany.androidapp.MainPage.GuideActivity;

import com.adcompany.androidapp.MainPage.VideoActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import co.ronash.pushe.Pushe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    Button SaveProfileButton;

    Typeface font_Medium;
    Typeface font_Bold;

    Spinner SpinnerState;
    Spinner SpinnerCity;

    public EditText FirstNameText;
    public EditText LastNameText;
    public EditText PasswordText;
    public EditText RepeatPasswordText;
    public EditText FriendCode;

    TextView TextViewSpinnerState;
    TextView TextViewSpinnerCity;
    TextView TextViewState;
    public TextView TextViewPassword;

    String StateId;
    String CityId;

    boolean SmallHint;

   List<ListSpinner> ListSpinnerState=new ArrayList<>();
   List<ListSpinner> ListSpinnerCity=new ArrayList<>();

   public Dialog loading;
   public Dialog ActivityDialogExit;

   public boolean CompleteProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context=getApplicationContext();

        setStatusBarGradiant(ProfileActivity.this);

        SaveProfileButton=(Button)findViewById(R.id.SaveProfileButton);
        SpinnerState=(Spinner)findViewById(R.id.SpinnerState);
        SpinnerCity=(Spinner)findViewById(R.id.SpinnerCity);
        FirstNameText=(EditText) findViewById(R.id.FirstNameText);
        LastNameText=(EditText) findViewById(R.id.LastNameText);
        PasswordText=(EditText) findViewById(R.id.PasswordText);
        RepeatPasswordText=(EditText) findViewById(R.id.RepeatPasswordText);
        FriendCode=(EditText)findViewById(R.id.FriendCode);
        TextViewSpinnerState=(TextView) findViewById(R.id.TextViewSpinnerState);
        TextViewSpinnerCity=(TextView) findViewById(R.id.TextViewSpinnerCity);
        TextViewPassword=(TextView) findViewById(R.id.TextViewPassword);
        TextViewState=(TextView) findViewById(R.id.TextViewState);


        font_Medium= Typeface.createFromAsset(ProfileActivity.context.getAssets(),"fonts/BHoma.ttf");
        font_Bold=Typeface.createFromAsset(ProfileActivity.context.getAssets(),"fonts/BHoma-Bold.ttf");

        SaveProfileButton.setTypeface(font_Bold);
        TextViewPassword.setTypeface(font_Bold);
        FirstNameText.setTypeface(font_Bold);
        LastNameText.setTypeface(font_Bold);
        PasswordText.setTypeface(font_Bold);
        RepeatPasswordText.setTypeface(font_Bold);
        TextViewSpinnerState.setTypeface(font_Bold);
        TextViewSpinnerCity.setTypeface(font_Bold);
        FriendCode.setTypeface(font_Bold);
        TextViewState.setTypeface(font_Bold);

        CompleteProfile = getIntent().getBooleanExtra("CompleteProfile",false);

        if (CompleteProfile)
        {
            PasswordText.setVisibility(View.GONE);
            RepeatPasswordText.setVisibility(View.GONE);
            TextViewPassword.setVisibility(View.GONE);
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
                                        if (loading.isShowing())
                                            loading.dismiss();
                                        Toast.makeText(ProfileActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                            try {
                                                                if (loading.isShowing())
                                                                    loading.dismiss();
                                                            }catch (Exception el)
                                                            {
                                                                el.printStackTrace();
                                                            }
                                                        }
                                                        JSONObject data= jObject.getJSONObject("data");
                                                        String first_name=data.getString("first_name");
                                                        String last_name=data.getString("last_name");
                                                        if (first_name != null)
                                                        {
                                                            FirstNameText.setText(first_name);
                                                        }
                                                        if (last_name != null)
                                                        {
                                                            LastNameText.setText(last_name);
                                                        }

                                                    } catch (Exception e) {
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception el)
                                                        {
                                                            el.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    } catch (Exception e) {
                                        try {
                                            if (loading.isShowing())
                                                loading.dismiss();
                                        }catch (Exception el)
                                        {
                                            el.printStackTrace();
                                        }
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        try {
                            if (loading.isShowing())
                                loading.dismiss();
                        }catch (Exception el)
                        {
                            el.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        float TextSize = FriendCode.getTextSize()/getResources().getDisplayMetrics().scaledDensity;
        if (TextSize<=18)
            SmallHint=true;
        else
            SmallHint=false;

        Log.d("looog", String.valueOf(TextSize));

        FriendCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(FriendCode.length() == 0) {
                    if (SmallHint)
                        FriendCode.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                    else
                        FriendCode.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                } else {
                    if (SmallHint)
                        FriendCode.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                    else
                        FriendCode.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        loading = new Dialog(ProfileActivity.this);
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


                    Request request = new Request.Builder()
                            .url("http://adcompany.ir/api/state/list")
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
                                    Toast.makeText(ProfileActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                    JSONArray data=jObject.getJSONArray("data");
                                                    if (data!=null)
                                                    {
                                                        ListSpinnerState=new ArrayList<>();
                                                        for (int i=0;i<data.length();i++)
                                                        {
                                                            ListSpinner listSpinner = new ListSpinner(data.getJSONObject(i).getInt("id"),data.getJSONObject(i).getString("name"));
                                                            ListSpinnerState.add(listSpinner);
                                                        }
                                                        MySpinnerAdapter<ListSpinner> adapter=new MySpinnerAdapter<ListSpinner>(ProfileActivity.this,android.R.layout.simple_spinner_item,ListSpinnerState);
                                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                        SpinnerState.setAdapter(adapter);
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

        TextViewSpinnerState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerState.performClick();
            }
        });

        TextViewSpinnerCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerCity.performClick();
            }
        });

        SpinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ListSpinner listSpinner=(ListSpinner)parent.getSelectedItem();
                StateId=String.valueOf(listSpinner.getId());
                TextViewSpinnerState.setText(listSpinner.getName());
                Log.d("loooog", String.valueOf(listSpinner.getId()));

                loading = new Dialog(ProfileActivity.this);
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
                                    .addFormDataPart("id", StateId)
                                    .build();


                            Request request = new Request.Builder()
                                    .url("http://adcompany.ir/api/city/list")
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
                                            Toast.makeText(ProfileActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                            JSONArray data=jObject.getJSONArray("data");
                                                            if (data!=null)
                                                            {
                                                                ListSpinnerCity=new ArrayList<>();
                                                                for (int i=0;i<data.length();i++)
                                                                {
                                                                    ListSpinner listSpinner = new ListSpinner(data.getJSONObject(i).getInt("id"),data.getJSONObject(i).getString("name"));
                                                                    ListSpinnerCity.add(listSpinner);
                                                                }
                                                                MySpinnerAdapter<ListSpinner> adapterCity=new MySpinnerAdapter<ListSpinner>(ProfileActivity.this,android.R.layout.simple_spinner_item,ListSpinnerCity);
                                                                adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                                SpinnerCity.setAdapter(adapterCity);
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SpinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ListSpinner listSpinner=(ListSpinner)parent.getSelectedItem();
                CityId=String.valueOf(listSpinner.getId());
                TextViewSpinnerCity.setText(listSpinner.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        SaveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("loooog", PasswordText.getText().toString());
                if (FirstNameText.length()==0 && FirstNameText.getVisibility()==View.VISIBLE)
                {
                    FirstNameText.setError("نام خود را بنویسید");
                    FirstNameText.requestFocus();
                }
                else if (LastNameText.length()==0 && LastNameText.getVisibility()==View.VISIBLE)
                {
                    LastNameText.setError("نام خانوادگی خود را بنویسید");
                    LastNameText.requestFocus();
                }
                else if (PasswordText.length()==0 && PasswordText.getVisibility()==View.VISIBLE)
                {
                    PasswordText.setError("رمز عبور خود را بنویسید");
                    PasswordText.requestFocus();
                }
                else if (PasswordText.length()<8 && PasswordText.getVisibility()==View.VISIBLE)
                {
                    PasswordText.setError("رمز عبور باید حداقل ۸ کاراکتر باشد");
                    PasswordText.requestFocus();
                }
                else if (!PasswordText.getText().toString().equals(RepeatPasswordText.getText().toString()) && PasswordText.getVisibility()==View.VISIBLE)
                {
                    RepeatPasswordText.setError("رمز عبور با تاییدیه مطابقت ندارد");
                    RepeatPasswordText.requestFocus();
                }
                else
                {
                    loading = new Dialog(ProfileActivity.this);
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
                                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("reagent_code",FriendCode.getText().toString()).apply();
                                long user_id = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getLong("user_id",0);
                                String UserIdString= String.valueOf(user_id);

                                String password;
                                String ConfrimPassword;

                                if (CompleteProfile)
                                {
                                    password = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("password","12345678");
                                    ConfrimPassword= getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("password","12345678");
                                }
                                else
                                {
                                    password = PasswordText.getText().toString();
                                    ConfrimPassword = RepeatPasswordText.getText().toString();
                                }

                                RequestBody body = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("user_id", UserIdString)
                                        .addFormDataPart("password",password )
                                        .addFormDataPart("password_confirmation", ConfrimPassword)
                                        .addFormDataPart("first_name", FirstNameText.getText().toString())
                                        .addFormDataPart("last_name", LastNameText.getText().toString())
                                        .addFormDataPart("state_id", StateId)
                                        .addFormDataPart("city_id", CityId)
                                        .addFormDataPart("reagent_code", FriendCode.getText().toString())
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
                                                if (loading.isShowing())
                                                    loading.dismiss();
                                                Toast.makeText(ProfileActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        try {
                                                                            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("password",PasswordText.getText().toString()).apply();
                                                                            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("cityid", String.valueOf(CityId)).apply();
                                                                            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("stateid", String.valueOf(StateId)).apply();
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
                                                                                            Toast.makeText(ProfileActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                                                                            startActivity(new Intent(ProfileActivity.this, GuideActivity.class));
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
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                    JSONObject jObject = new JSONObject(myResponce);
                                                    String error = jObject.getString("error");
                                                    runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            try {
                                                                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_LONG).show();
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
    private static class MySpinnerAdapter<L> extends ArrayAdapter<ListSpinner> {
        // Initialise custom font, for example:
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/BHoma-SemiBold.ttf");

        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

        private MySpinnerAdapter(Context context, int resource, List<ListSpinner> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(font);
            view.setTextSize(20);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityDialogShowExit();
    }

    public void ActivityDialogShowExit()
    {
        try {
            ActivityDialogExit=new Dialog(ProfileActivity.this);
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
                    LogOut();
                }
            });
            ExitNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDialogExit.dismiss();
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

    public void LogOut()
    {
        try {
            if (loading.isShowing())
                loading.dismiss();
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
        loading = new Dialog(ProfileActivity.this);
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
                            Toast.makeText(ProfileActivity.this, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
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
                                                        try {
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                        }catch (Exception el)
                                                        {
                                                            el.printStackTrace();
                                                        }
                                                    }
                                                    Log.d("looog", "logout");
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putLong("user_id",0).apply();
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("cityid", "0").apply();
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("stateid", "0").apply();
                                                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                                    finishAffinity();
                                                } catch (Exception e) {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception el)
                                                    {
                                                        el.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    try {
                                        if (loading.isShowing())
                                            loading.dismiss();
                                    }catch (Exception el)
                                    {
                                        el.printStackTrace();
                                    }
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
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putLong("user_id",0).apply();
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("cityid", "0").apply();
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("stateid", "0").apply();
                                                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                                    finishAffinity();
                                                } catch (Exception e) {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception el)
                                                    {
                                                        el.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    try {
                                        if (loading.isShowing())
                                            loading.dismiss();
                                    }catch (Exception el)
                                    {
                                        el.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    try {
                        if (loading.isShowing())
                            loading.dismiss();
                    }catch (Exception el)
                    {
                        el.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
