package com.adcompany.androidapp;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import co.ronash.pushe.PusheListenerService;

public class MyPushListener extends PusheListenerService {
    @Override
    public void onMessageReceived(JSONObject customContent, JSONObject pushMessage) {
        if (customContent == null || customContent.length() == 0)
            return; //json is empty
        android.util.Log.i("Pushe", "Custom json Message: " + customContent.toString()); //print json to logCat
        //Do something with json
        try {
            String Title = customContent.getString("title");
            String Message = customContent.getString("message");

            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("title_message",Title).apply();
            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("text_message",Message).apply();
            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("isnotification",true).apply();

            Intent intent = new Intent("com.adcompany.androidapp_FCM-MESSAGE");
            intent.putExtra("title",Title);
            intent.putExtra("message",Message);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.sendBroadcast(intent);
        } catch (JSONException e) {
            android.util.Log.e("TAG", "Exception in parsing json", e);
        }
    }
}