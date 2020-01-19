package com.internet.speed.test.analyzer.wifi.key.generator.app.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class InAppPrefManager {


    private static final String SHARED_PREF_NAME = "InAppSharedPref";
    private static final String TAG_STATUS = "appstatus";

    private static InAppPrefManager mInstance;
    private static Context mCtx;

    private InAppPrefManager(Context context) {
        mCtx = context;
    }


    public static synchronized InAppPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new InAppPrefManager(context);
        }
        return mInstance;
    }

    public boolean setInAppStatus(boolean status){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TAG_STATUS, status);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public boolean getInAppStatus(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(TAG_STATUS, false);
    }



}
