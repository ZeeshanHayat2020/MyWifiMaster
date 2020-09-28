package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.anastr.speedviewlib.Speedometer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;
import com.internet.speed.test.analyzer.wifi.key.generator.app.activities.ActivityBase;

import java.io.File;
import java.util.Locale;
import java.util.Timer;

import static android.os.Build.VERSION.SDK_INT;

public class Speedtest extends ActivityBase {

    private Handler mHandler;
    private long mStartRX = 0;
    private long mStartTX = 0;
    AdView banner;
    private Speedometer speedometerUpLoading;
    private Speedometer speedometerDownLoading;
    private RelativeLayout layoutHeader;
    public ImageView headerItemMenu;
    public ImageView headerItemCenterLeft;
    public ImageView headerItemCenterRight;
    public ImageView headerItemBottomLeft;
    public ImageView headerItemBottomRigth;
    public TextView headerItemTextViewFirst;
    public TextView headerItemTextViewSecond;
    private Handler myHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this, R.color.colorWhite, R.color.colorWhite);
        setContentView(R.layout.activity_speedtest);
        MobileAds.initialize(this, getResources().getString(R.string.app_id));
        setUpHeader();
        initViews();

    }

    private void initViews() {
        mHandler = new Handler(getMainLooper());
        myHandler = new Handler(getMainLooper());
        speedometerUpLoading = findViewById(R.id.speedView_uploading);
        speedometerDownLoading = findViewById(R.id.speedView_downloading);

        banner = findViewById(R.id.banner_ad);


        if (!InAppPrefManager.getInstance(getApplicationContext()).getInAppStatus()) {

            adview();
        }

    }

    void setUpHeader() {
        layoutHeader = findViewById(R.id.header_acLanugage);
        headerItemMenu = findViewById(R.id.header_item_menu_imageView);
        headerItemCenterLeft = findViewById(R.id.header_item_centerLeft_imageView);
        headerItemCenterRight = findViewById(R.id.header_item_centerRight_imageView);
        headerItemBottomLeft = findViewById(R.id.header_item_bottomLeft_imageView);
        headerItemBottomRigth = findViewById(R.id.header_item_bottomRigth_imageView);
        headerItemTextViewFirst = findViewById(R.id.header_item_textView_First);
        headerItemTextViewSecond = findViewById(R.id.header_item_textView_Second);


        headerItemCenterLeft.setVisibility(View.INVISIBLE);
        headerItemBottomLeft.setVisibility(View.INVISIBLE);
        headerItemBottomRigth.setVisibility(View.INVISIBLE);

        headerItemCenterRight.setImageResource(R.drawable.ic_header_item_speed_test);
        headerItemTextViewFirst.setText(R.string.WIFI);
        headerItemTextViewSecond.setText(R.string.speedTest);


    }

    private void startSpeedMeters() {

        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();
        if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Uh Oh!");
            alert.setMessage("Your device does not support traffic stat monitoring.");
            alert.show();
        } else {
            mHandler.postDelayed(mRunnable, 1000);
        }
    }

    private final Runnable mRunnable = new Runnable() {
        public void run() {
            long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
            speedometerUpLoading.setUnit(getUnit(rxBytes));

            if (rxBytes >= 1024) {
                long upLoadingKb = rxBytes / 1024;
                speedometerUpLoading.speedTo(upLoadingKb);

            }
            long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
            speedometerDownLoading.setUnit(getUnit(txBytes));

            if (txBytes >= 1024) {
                long dowloandingKb = txBytes / 1024;
                speedometerDownLoading.speedTo(dowloandingKb);
            }
            mHandler.postDelayed(mRunnable, 1000);

        }
    };

    public String getUnit(long fileSize) {
        String modifiedFileSize = null;

        if (fileSize < 1024) {
            modifiedFileSize = "bytes/s";

        } else if (fileSize > 1024 && fileSize < (1024 * 1024)) {
            modifiedFileSize = "KB/s";
        } else {
            modifiedFileSize = "MB/s";
        }


        return modifiedFileSize;
    }

    public void adview() {

        AdRequest adRequest = new AdRequest.Builder().build();
        banner.setVisibility(View.VISIBLE);
        banner.loadAd(adRequest);

    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiStateReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiStateReceiver);
    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiStateExtra) {
                case WifiManager.WIFI_STATE_ENABLED:

                    mHandler.removeCallbacks(mRunnable);

                    myHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startSpeedMeters();
                        }
                    }, 2000);

                    Log.d("SpeedtestReceiver", "WiFi is ON");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Log.d("SpeedtestReceiver", "WiFi is Off");

                    mHandler.removeCallbacks(mRunnable);

                    startSpeedMeters();
                    break;
            }
        }
    };

}
