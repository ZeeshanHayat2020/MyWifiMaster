package com.internet.speed.test.analyzer.wifi.key.generator.app.appsNetBlocker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.TinyDB;

import java.io.IOException;

public class AppsNetBlockerService extends VpnService {
    private static final String TAG = "NetGuard.Service";

    private ParcelFileDescriptor VPN = null;
    private static final String EXTRA_COMMAND = "Command";
    private enum Command {start, reload, stop}


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean enabled = prefs.getBoolean("enabled", false);

        Command cmd = (intent == null ? Command.start : (Command) intent.getSerializableExtra(EXTRA_COMMAND));

        switch (cmd) {
            case start:
                if (enabled && VPN == null)
                    VPN = vpnStart();
                break;

            case reload:
                ParcelFileDescriptor prev = VPN;
                if (enabled)
                    VPN = vpnStart();
                if (prev != null)
                    vpnStop(prev);
                break;

            case stop:
                if (VPN != null) {
                    vpnStop(VPN);
                    VPN = null;
                }
                stopSelf();
                break;
        }

        return START_STICKY;
    }

    private ParcelFileDescriptor vpnStart() {
        Log.i(TAG, "Starting");

        // Check if Wi-Fi
        boolean wifi = helperClass.isWifiActive(this);
        Log.i(TAG, "wifi=" + wifi);

        // Build VPN service
        final Builder builder = new Builder();
        builder.setSession(getString(R.string.app_name));
        builder.addAddress("10.1.10.1", 32);
        builder.addAddress("fd00:1:fd00:1:fd00:1:fd00:1", 128);
        builder.addRoute("0.0.0.0", 0);
        builder.addRoute("0:0:0:0:0:0:0:0", 0);

        // Add list of allowed applications
        for (Apps_Model_Class rule : Apps_Model_Class.getListOfApps(this))
            if (!(wifi ? rule.wifi_blocked : rule.mobileData_blocked)) {
                Log.i(TAG, "Allowing " + rule.info.packageName);
                try {
                    builder.addDisallowedApplication(rule.info.packageName);
                } catch (PackageManager.NameNotFoundException ex) {
                    Log.e(TAG, ex.toString() + "\n" + Log.getStackTraceString(ex));
                }
            }

        // Build configure intent
        Intent configure = new Intent(this, NetBlockerMainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, configure, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setConfigureIntent(pi);

        // Start VPN service
        try {
            return builder.establish();

        } catch (Throwable ex) {
            Log.e(TAG, ex.toString() + "\n" + Log.getStackTraceString(ex));

            // Disable firewall
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putBoolean("enabled", false).apply();

            // Feedback
            helperClass.toast(ex.toString(), Toast.LENGTH_LONG, this);

            return null;
        }
    }

    private void vpnStop(ParcelFileDescriptor pfd) {
        Log.i(TAG, "Stopping");
        try {
            pfd.close();
        } catch (IOException ex) {
            Log.e(TAG, ex.toString() + "\n" + Log.getStackTraceString(ex));
        }
    }

    private BroadcastReceiver connectivityChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Received " + intent);
            helperClass.logExtras(TAG, intent);
            if (intent.hasExtra(ConnectivityManager.EXTRA_NETWORK_TYPE) &&
                    intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, ConnectivityManager.TYPE_DUMMY) == ConnectivityManager.TYPE_WIFI)
                refreshVpnService(null, AppsNetBlockerService.this);
        }
    };

    private BroadcastReceiver packageAddedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Received " + intent);
            helperClass.logExtras(TAG, intent);
            refreshVpnService(null, AppsNetBlockerService.this);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Create");

        // Listen for connectivity updates
        IntentFilter ifConnectivity = new IntentFilter();
        ifConnectivity.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityChangedReceiver, ifConnectivity);

        // Listen for added applications
        IntentFilter ifPackage = new IntentFilter();
        ifPackage.addAction(Intent.ACTION_PACKAGE_ADDED);
        ifPackage.addDataScheme("package");
        registerReceiver(packageAddedReceiver, ifPackage);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Destroy");

        if (VPN != null) {
            vpnStop(VPN);
            VPN = null;
        }

        unregisterReceiver(connectivityChangedReceiver);
        unregisterReceiver(packageAddedReceiver);


        super.onDestroy();
    }

    @Override
    public void onRevoke() {
        Log.i(TAG, "Revoke");

        if (VPN != null) {
            vpnStop(VPN);
            VPN = null;
        }

        // Disable firewall
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("enabled", false).apply();

        super.onRevoke();
    }

    public static void startVpnService(Context context) {
        Intent intent = new Intent(context, AppsNetBlockerService.class);
        intent.putExtra(EXTRA_COMMAND, Command.start);
        context.startService(intent);
    }

    public static void refreshVpnService(String network, Context context) {
        if (network == null || ("wifi".equals(network) ? helperClass.isWifiActive(context) : !helperClass.isWifiActive(context))) {
            Intent intent = new Intent(context, AppsNetBlockerService.class);
            intent.putExtra(EXTRA_COMMAND, Command.reload);
            context.startService(intent);
        }
    }

    public static void stopVpnService(Context context) {
        Intent intent = new Intent(context, AppsNetBlockerService.class);
        intent.putExtra(EXTRA_COMMAND, Command.stop);
        context.startService(intent);
    }
}
