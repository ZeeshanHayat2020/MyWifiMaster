package com.internet.speed.test.analyzer.wifi.key.generator.app.autoConnectWifi;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.internet.speed.test.analyzer.wifi.key.generator.app.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectToWifi_Service extends Service {
    public Boolean has_launched = false;

    public ConnectToWifi_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "001";
            String channelName = "RecordChannel";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                Notification notification;
                notification = new Notification.Builder(getApplicationContext(), channelId)
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.apicon)
                        .setContentTitle("Auto Wifi Service")
                        .setContentText("Your Wifi will be turn On after 30 minutes")
                        .build();
                startForeground(101, notification);
            }
        } else {
            startForeground(101, new Notification());
        }
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                // your code here...
                wificheck();
            }
        };

        timer.schedule (hourlyTask, 0l, 1000*30*60);




        return Service.START_STICKY;
    }
    public void wificheck() {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() && has_launched == false) {
            has_launched = true;
        }
        else if (wifi.isWifiEnabled() == false) {
            has_launched = false;
            wifi.setWifiEnabled(true);

        }
    }

    public static boolean isServiceRunning(Context context,Class<?> serviceClass){
        final ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            Log.d("ServiceRunnning", String.format("Service:%s", runningServiceInfo.service.getClassName()));
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())){
                return true;
            }
        }
        return false;
    }
}
