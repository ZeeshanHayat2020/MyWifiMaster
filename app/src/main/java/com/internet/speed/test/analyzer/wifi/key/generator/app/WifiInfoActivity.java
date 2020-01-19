package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;

import me.drakeet.support.toast.ToastCompat;

public class WifiInfoActivity extends AppCompatActivity {

    TextView tvWifiInfo;
    WifiManager wifiManager;
    WifiInfo wifiInfo;
    AdView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_info);
        MobileAds.initialize(this, getResources().getString(R.string.app_id));

        banner = (AdView) findViewById(R.id.banner_ad);
        tvWifiInfo = (TextView) findViewById(R.id.tv_wifi_info);
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
