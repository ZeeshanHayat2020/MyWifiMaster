package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.models.ChannelInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityWifiChannels extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_info);
        banner = (AdView) findViewById(R.id.banner_ad);

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setMax(5);
        tvWifiInfo = (TextView) findViewById(R.id.tv_wifi_info);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        signalLevelsAll();


        myHandler.postDelayed(runableWifiInfo, 2000);
        adview();
        try {
            getWifiInfo();
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    public void getWifiInfo() {
        if (wifiManager.isWifiEnabled()) {

            wifiInfo = wifiManager.getConnectionInfo();
            int level = checkfrequencyLevel(wifiInfo.getFrequency());
//            checkfrequencyLevelAndRateIt(wifiInfo.getFrequency(), level);
            ChannelInfo channelinfo = new ChannelInfo();
            channelinfo = currentChannelfrequency(wifiInfo.getFrequency());

            String freqlevel = channelinfo.getChannelLevel();
            int channelNum = channelinfo.getChannelNumber();

            if (String.valueOf(wifiInfo.getSupplicantState()).equals("COMPLETED")) {
                tvWifiInfo.setText("SSID: " + wifiInfo.getSSID() + "\n" +
                        "BSSID: " + wifiInfo.getBSSID() + "\n" +
                        "Mac Address: " + wifiInfo.getMacAddress() + "\n" +
                        "Describe Content: " + wifiInfo.describeContents() + "\n" +
                        "Hidden SSID: " + wifiInfo.getHiddenSSID() + "\n" +
                        "IP Address: " + wifiInfo.getIpAddress() + "\n" +
                        "Link Speed: " + wifiInfo.getLinkSpeed() + "\n" +
                        "Link Frequency: " + wifiInfo.getFrequency() + "\n" +
                        "RSSI: " + wifiInfo.getRssi() + "\n" +
                        "wifi channel frequency: " + freqlevel + "\n" +
                        "wifi channel Number: " + channelNum + "\n" +
                        "Signal Strength: " + WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5) + "\n");

                ratingBar.setRating(WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5));
            }
        } else {
            Toast.makeText(this, "Plz connect to a wifi network", Toast.LENGTH_SHORT).show();
        }
        signalLevelsAll();
    }

    public void adview() {
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);
    }

    Handler myHandler = new Handler();
    Runnable runableWifiInfo = new Runnable() {
        public void run() {
            getWifiInfo();
            Log.i(Tag, "wifi signal runnalble running");
            myHandler.postDelayed(runableWifiInfo, 2000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(Tag, "wifi signal runnalble removeCallbacks");
        myHandler.removeCallbacks(runableWifiInfo);
    }

    int frequencyListSize = loverFrequency.size();

    private int checkfrequencyLevel(int channelFreqP) {
        for (int i = 1; i < frequencyListSize; i++) {
            if (channelFreqP == loverFrequency.get(i)) {
                return 1;
            } else if (channelFreqP == centerFrequency.get(i)) {
                return 2;
            } else if (channelFreqP == upperFrequency.get(i)) {
                return 3;
            } else {
            }
        }
        return 0;
    }

    private ChannelInfo currentChannelfrequency(int channelFreqP) {
        ChannelInfo channelinfo = new ChannelInfo();

        for (int i = 0; i < frequencyListSize; i++) {
            if (channelFreqP == loverFrequency.get(i)) {
                channelinfo.setChannelLevel("Lower");
                channelinfo.setChannelNumber(i + 1);

                return channelinfo;

            } else if (channelFreqP == centerFrequency.get(i)) {
                channelinfo.setChannelLevel("Center");
                channelinfo.setChannelNumber(i + 1);
                return channelinfo;

            } else if (channelFreqP == upperFrequency.get(i)) {
                channelinfo.setChannelLevel("Upper");
                channelinfo.setChannelNumber(i + 1);
                return channelinfo;
            } else {

            }
        }
        return channelinfo;
    }

    private String bestWifiChannel() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> mScanResults = wifiManager.getScanResults();
        ScanResult bestResult = null;
        for (ScanResult results : mScanResults) {
            Log.i(Tag, "result: " + results.SSID);
            if (bestResult == null || WifiManager.compareSignalLevel(bestResult.level,
                    results.level) < 0) {
                bestResult = results;
            }
        }


        String message = String.format("%s networks found. %s is the strongest.",
                mScanResults.size(), bestResult.SSID);
        Log.i(Tag, "best network : " + message);

        return message;
    }

    private void signalLevelsAll() {
        List<ScanResult> wifiList = wifiManager.getScanResults();
        for (ScanResult scanResult : wifiList) {
            int level = WifiManager.calculateSignalLevel(scanResult.level, 5);

            Log.i(Tag, "BSSID " + scanResult.BSSID);
            Log.i(Tag, "SSID " + scanResult.SSID);
            Log.i(Tag, "channelWidth " + scanResult.channelWidth);
            Log.i(Tag, "Signal Level: " + level + " out of 5");
        }
    }

}
