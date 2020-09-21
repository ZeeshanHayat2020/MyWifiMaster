package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import me.drakeet.support.toast.ToastCompat;

import static android.os.Build.VERSION.SDK_INT;

public class WifiInfoActivity extends AppCompatActivity {


    private final String Tag = this.getClass().getName();
    TextView tvWifiInfo;
    WifiManager wifiManager;
    WifiInfo wifiInfo;
    AdView banner;
    RatingBar ratingBar;
    int channelNum = 0;


    final ArrayList<Integer> loverFrequency = new ArrayList<Integer>(
            Arrays.asList(2401, 2406, 2411, 2416, 2421, 2426, 2431, 2436, 2441, 2446, 2451, 2456, 2461, 2473));

    final ArrayList<Integer> centerFrequency = new ArrayList<Integer>(
            Arrays.asList(2412, 2417, 2422, 2427, 2432, 2437, 2442, 2447, 2452, 2457, 2462, 2467, 2472, 2484));

    final ArrayList<Integer> upperFrequency = new ArrayList<Integer>(
            Arrays.asList(2423, 2428, 2433, 2438, 2443, 2448, 2453, 2458, 2463, 2468, 2473, 2478, 2483, 2495));


    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    private void setLocale(Locale locale) {
        // optional - Helper method to save the selected language to SharedPreferences in case you might need to attach to activity context (you will need to code this)
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        if (SDK_INT > Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preferences preferences = new Preferences(this);
        Locale locale = new Locale(preferences.GetValueStringlang(preferences.LANG_VALUE));
        setLocale(locale);
        setContentView(R.layout.activity_wifi_info);
        MobileAds.initialize(this, getResources().getString(R.string.app_id));

        banner = (AdView) findViewById(R.id.banner_ad);
        tvWifiInfo = (TextView) findViewById(R.id.tv_wifi_info);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setMax(5);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!InAppPrefManager.getInstance(WifiInfoActivity.this).getInAppStatus()) {
            adview();
        }

        try {
            getWifiInfo();
        } catch (Exception e) {
            if (android.os.Build.VERSION.SDK_INT == 25) {

                ToastCompat.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    public void getWifiInfo() {
        if (wifiManager.isWifiEnabled()) {
            wifiInfo = wifiManager.getConnectionInfo();
            if (String.valueOf(wifiInfo.getSupplicantState()).equals("COMPLETED")) {

                tvWifiInfo.setText("SSID: " + wifiInfo.getSSID() + "\n" +
                        "BSSID: " + wifiInfo.getBSSID() + "\n" +
                        "Mac Address: " + wifiInfo.getMacAddress() + "\n" +
                        "Describe Content: " + wifiInfo.describeContents() + "\n" +
                        "Hidden SSID: " + wifiInfo.getHiddenSSID() + "\n" +
                        "IP Address: " + wifiInfo.getIpAddress() + "\n" +
                        "Link Speed: " + wifiInfo.getLinkSpeed() + "\n" +
                        "RSSI: " + wifiInfo.getRssi() + "\n" +
                        "Signal Strength: " + WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5) + "\n");


            }
        } else {
            Toast.makeText(this, "Plz connect to a wifi network", Toast.LENGTH_SHORT).show();
        }
    }

    public void adview() {

        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

    }
}
