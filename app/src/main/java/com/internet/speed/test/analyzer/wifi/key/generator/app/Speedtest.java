package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.net.TrafficStats;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;

public class Speedtest extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;
    AdView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedtest);
        MobileAds.initialize(this, getResources().getString(R.string.app_id));

        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();
        banner =  findViewById(R.id.banner_ad);


        if (!InAppPrefManager.getInstance(getApplicationContext()).getInAppStatus()) {

            adview();
        }
        if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Uh Oh!");
            alert.setMessage("Your device does not support traffic stat monitoring.");
            alert.show();
        } else {
            mHandler.postDelayed(mRunnable, 1000);
        }
    }

    private final Runnable mRunnable =new Runnable() {
        public void run() {
            TextView RX =  findViewById(R.id.RX);
            TextView TX =  findViewById(R.id.TX);
            long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
           // RX.setText(Long.toString(rxBytes));
            if(rxBytes>=1024) {
                long rxKb = rxBytes / 1024;
                RX.setText(Long.toString(rxKb) + " KBs");
            }
            long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
            //TX.setText(Long.toString(txBytes));
            if(txBytes>=1024){
                long txkb = txBytes / 1024;
                TX.setText(Long.toString(txkb) + " KBs");
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };
    public void adview() {

        AdRequest adRequest = new AdRequest.Builder().build();
        banner.setVisibility(View.VISIBLE);
        banner.loadAd(adRequest);

    }
    /*long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
     RX.setText(Long.toString(rxBytes) + " bytes");
     if(rxBytes>=1024){
        //KB or more
        long rxKb = rxBytes/1024;
        RX.setText(Long.toString(rxKb) + " KBs");
        if(rxKb>=1024){
            //MB or more
            long rxMB = rxKb/1024;
            RX.setText(Long.toString(rxMB) + " MBs");
            if(rxMB>=1024){
                //GB or more
                long rxGB = rxMB/1024;
                RX.setText(Long.toString(rxGB));
            }//rxMB>1024
        }//rxKb > 1024
    }//rxBytes>=1024*/
}
