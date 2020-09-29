package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.internet.speed.test.analyzer.wifi.key.generator.app.Helper.LocalHelper;

public class MainApplication extends Application {
    public static final String CHANNEL_ID = "com.internet.speed.test.analyzer.wifi.key.generator.app";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Wifi Master",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}