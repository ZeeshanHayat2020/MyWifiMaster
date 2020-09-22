package com.internet.speed.test.analyzer.wifi.key.generator.app.appsNetBlocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.util.Log;

public class VpnBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "NetGuard.Receiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(TAG, "Received " + intent);
        helperClass.logExtras(TAG, intent);

        // Start service
        if (VpnService.prepare(context) == null)
            AppsNetBlockerService.startVpnService(context);
    }
}