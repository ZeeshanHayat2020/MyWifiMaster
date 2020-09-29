package com.internet.speed.test.analyzer.wifi.key.generator.app.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.activities.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

import static com.internet.speed.test.analyzer.wifi.key.generator.app.MainApplication.CHANNEL_ID;
import static com.internet.speed.test.analyzer.wifi.key.generator.app.activities.MainActivity.ACTION_INTENT_FILTER_UI_UPDATE_RECEIVER;
import static com.internet.speed.test.analyzer.wifi.key.generator.app.activities.MainActivity.KEY_INTENT_CURRENT_WIFI_NAME;
import static com.internet.speed.test.analyzer.wifi.key.generator.app.activities.MainActivity.KEY_INTENT_CURRENT_WIFI_STRENGTH;
import static com.internet.speed.test.analyzer.wifi.key.generator.app.activities.MainActivity.KEY_INTENT_NOTIFICATION_SERVICE;

public class NotificationService extends Service {
    WifiManager wifiManager;
    private String currentWifiName;
    private String currentWifiStrength;
    private int currentLevel;
    Timer timer;
    TimerTask timerTask;

    @Override
    public void onCreate() {
        super.onCreate();
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getStringExtra(KEY_INTENT_NOTIFICATION_SERVICE);
        String name = intent.getStringExtra(KEY_INTENT_CURRENT_WIFI_NAME);
        String status = intent.getStringExtra(KEY_INTENT_CURRENT_WIFI_STRENGTH);
        switch (action) {
            case "START": {
                updateValues();
            }
            break;
            case "STOP": {
                stopSelf();
            }
            break;
        }
        return START_NOT_STICKY;
    }

    private void setNotification() {
        String title = "Connected to " + currentWifiName;
        String text = "Signal Strength   " + currentLevel + " %";
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.apicon)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }


    private void getSignalStrength() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        currentWifiName = wifiInfo.getSSID();
        currentLevel = (int) (WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 40) * 2.5);
    }

    private void updateValues() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getSignalStrength();
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setNotification();
                        sendBroadCast();
                    }
                }, 1000);


            }
        };
        timer.schedule(timerTask, 2000, 50000);
    }

    private void sendBroadCast() {
        Intent intent = new Intent(ACTION_INTENT_FILTER_UI_UPDATE_RECEIVER);
        intent.putExtra(KEY_INTENT_CURRENT_WIFI_STRENGTH, currentLevel);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}