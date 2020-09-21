package com.internet.speed.test.analyzer.wifi.key.generator.app.autoConnectWifi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.internet.speed.test.analyzer.wifi.key.generator.app.Preferences;
import com.internet.speed.test.analyzer.wifi.key.generator.app.R;

import java.util.Locale;

import static android.os.Build.VERSION.SDK_INT;

public class AutoConnectWifi extends AppCompatActivity {

    Button buttonOn , buttonOFF;

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
        Preferences preferences1 = new Preferences(this);
        Locale locale = new Locale(preferences1.GetValueStringlang(preferences1.LANG_VALUE));
        setLocale(locale);
        setContentView(R.layout.activity_auto_connect_wifi);
        buttonOn = findViewById(R.id.turnServiceOn);
        buttonOFF = findViewById(R.id.turnServiceOff);
        Boolean check = ConnectToWifi_Service.isServiceRunning(AutoConnectWifi.this.getApplicationContext() , ConnectToWifi_Service.class);
        if (check)
        {
            buttonOn.setVisibility(View.GONE);
            buttonOFF.setVisibility(View.VISIBLE);
        }else
        {
            buttonOn.setVisibility(View.VISIBLE);
            buttonOFF.setVisibility(View.GONE);
        }

        buttonOFF.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                    stopService(new Intent(AutoConnectWifi.this , ConnectToWifi_Service.class));
                    buttonOFF.setVisibility(View.GONE);
                    buttonOn.setVisibility(View.VISIBLE);
            }
        });
        buttonOn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                startService(new Intent(AutoConnectWifi.this , ConnectToWifi_Service.class));
                buttonOn.setVisibility(View.GONE);
                buttonOFF.setVisibility(View.VISIBLE);
            }
        });

    }
}
