package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.internet.speed.test.analyzer.wifi.key.generator.app.activities.MainActivity;

import java.util.Locale;

import static android.os.Build.VERSION.SDK_INT;

public class TermsandConditionActivity extends AppCompatActivity {
    SharedPreferences preferences;
    PrefManager prefManager;

    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    private void setLocale(Locale locale){
        // optional - Helper method to save the selected language to SharedPreferences in case you might need to attach to activity context (you will need to code this)
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(locale);
        } else{
            configuration.locale=locale;
        }
        if (SDK_INT > Build.VERSION_CODES.N){
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration,displayMetrics);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Preferences preferences = new Preferences(this);
        Locale locale = new Locale(preferences.GetValueStringlang(preferences.LANG_VALUE));
        setLocale(locale);
//        Locale.setDefault(locale);
//        Configuration configuration = new Configuration();
//        configuration.locale = locale;
//        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_termsand_condition);
        prefManager = new PrefManager(this);
        if (prefManager.isAccepted())
        {
            startActivity(new Intent(TermsandConditionActivity.this , MainActivity.class));
            finish();
        }


    }

    public void acceptbtn(View view) {
        try {
            prefManager.setAccept(true);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void denybtn(View view) {
        finishAffinity();
    }
}
