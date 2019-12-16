package com.adgameampo.androidapp.MainPage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.adgameampo.androidapp.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.adgameampo.androidapp.MainPage.CheckOutActivity.ShabaList;
import static com.adgameampo.androidapp.MainPage.CheckOutActivity.UserID;



public class AdapterShabaList extends BaseAdapter {

    Context context;
    ArrayList<ListShaba> listShaba;
    LayoutInflater inflater;

    Typeface font_Bold;

    public Dialog loading;
    public Dialog ActivityDialogDelete;


    public AdapterShabaList(Context context, ArrayList<ListShaba> listShaba){

        this.context = context;
        this.listShaba=listShaba;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listShaba.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder holder;

            if (convertView==null)
            {
                holder=new ViewHolder();
                convertView=inflater.inflate(R.layout.shaba_item,null);

                holder.TextViewShabaCarditem=(TextView)convertView.findViewById(R.id.TextViewShabaCarditem);
                holder.ImageViewShabaCardItem=(ImageView) convertView.findViewById(R.id.ImageViewShabaCardItem);
                convertView.setTag(holder);
            }
            else
            {
                holder=(ViewHolder)convertView.getTag();
            }
            font_Bold=Typeface.createFromAsset(context.getAssets(),"fonts/BHoma-Bold.ttf");
            String number=listShaba.get(position).getNumber();
            String FirstNumber=number.substring(0,5);
            String LastNumber=number.substring(number.length()-5);
            holder.TextViewShabaCarditem.setText("IR"+FirstNumber+"----------"+LastNumber);


            holder.ImageViewShabaCardItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDialogShowDelete(position);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return convertView;
    }
    private class ViewHolder{
        TextView TextViewShabaCarditem;
        ImageView ImageViewShabaCardItem;
    }

    public void ActivityDialogShowDelete(int position)
    {
        try {
            ActivityDialogDelete=new Dialog(context);
            ActivityDialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(ActivityDialogDelete.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ActivityDialogDelete.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
            ActivityDialogDelete.setContentView(R.layout.question_dialog);

            ImageView ExitYes = (ImageView)ActivityDialogDelete.findViewById(R.id.ExitYes);
            ImageView ExitNo = (ImageView)ActivityDialogDelete.findViewById(R.id.ExitNo);
            TextView Title=(TextView)ActivityDialogDelete.findViewById(R.id.TitleExit);
            TextView Text=(TextView)ActivityDialogDelete.findViewById(R.id.TextExit);

            ActivityDialogDelete.setCancelable(false);

            String ShabaNum="\n"+"IR"+listShaba.get(position).getNumber()+"\n";
            String title="آیا می خواهید شماره شبا با شماره "+ShabaNum+" را حذف کنید؟";

            Title.setText("حــــــذف");
            Text.setText(title);

            Title.setTypeface(font_Bold);
            Text.setTypeface(font_Bold);

            ExitYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String CardID= String.valueOf(listShaba.get(position).getId());
                    try {
                        try {
                            if (loading.isShowing())
                                loading.dismiss();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        ActivityDialogDelete.dismiss();
                        loading = new Dialog(context);
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.loading_dialog);
                        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loading.setCancelable(false);
                        loading.setCanceledOnTouchOutside(false);
                        loading.show();
                        ShabaList.remove(position);
                        notifyDataSetChanged();
                        String UserIdString= String.valueOf(UserID);
                        OkHttpClient client = new OkHttpClient();

                        OkHttpClient.Builder builder = new OkHttpClient.Builder();
                        builder.connectTimeout(15, TimeUnit.SECONDS);
                        builder.readTimeout(15, TimeUnit.SECONDS);
                        builder.writeTimeout(15, TimeUnit.SECONDS);

                        RequestBody body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("user_id",UserIdString)
                                .addFormDataPart("id",CardID)
                                .build();

                        Request request = new Request.Builder()
                                .url("http://adcompany.ir/api/shaba/destroy")
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
                                try
                                {
                                    try {
                                        if (loading.isShowing())
                                            loading.dismiss();
                                    }catch (Exception el)
                                    {
                                        el.printStackTrace();
                                    }
                                    Toast.makeText(context, "خطا در ارتباط ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();

                                }catch (Exception el)
                                {
                                    el.printStackTrace();
                                }

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
                                if (!response.isSuccessful()) {
                                    try {
                                        try {
                                            if (loading.isShowing())
                                                loading.dismiss();
                                        }catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(context, "شماره شبا با موفقیت حذف شد", Toast.LENGTH_LONG).show();
                                        JSONObject jObject = new JSONObject(myResponce);
                                        Log.d("loooog", "deleted");

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
            });
            ExitNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDialogDelete.dismiss();
                }
            });

            ActivityDialogDelete.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
