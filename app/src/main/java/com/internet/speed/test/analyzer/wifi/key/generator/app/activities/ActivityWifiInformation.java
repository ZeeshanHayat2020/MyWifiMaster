package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.models.ChannelInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityWifiInformation extends ActivityBase {


    public static final int REQUEST_CODE_LOCATION = 1212;
    private TextView txt_ssId;
    private TextView txt_ip_address;
    private TextView txt_mac_address;
    private TextView txt_bssId;
    private TextView txt_linkSpeed;
    private TextView txt_networkId;
    private TextView txt_rssId;
    private TextView txt_channel_level;
    private TextView txt_channel_num;
    private GoogleApiClient googleApiClient;

    private Timer timer;
    private WifiManager wifiManager;
    private ArrayList<ScanResult> scan_Results;
    ActivityWifiInformation.WifiBroadCastReceiver wifiBroadCastReceiver;

    final ArrayList<Integer> loverFrequency = new ArrayList<Integer>(
            Arrays.asList(2401, 2406, 2411, 2416, 2421, 2426, 2431, 2436, 2441, 2446, 2451, 2456, 2461, 2473));

    final ArrayList<Integer> centerFrequency = new ArrayList<Integer>(
            Arrays.asList(2412, 2417, 2422, 2427, 2432, 2437, 2442, 2447, 2452, 2457, 2462, 2467, 2472, 2484));

    final ArrayList<Integer> upperFrequency = new ArrayList<Integer>(
            Arrays.asList(2423, 2428, 2433, 2438, 2443, 2448, 2453, 2458, 2463, 2468, 2473, 2478, 2483, 2495));

    int frequencyListSize = loverFrequency.size();


    public ImageView headerItemMenu;
    public ImageView headerItemCenterLeft;
    public ImageView headerItemCenterRight;
    public ImageView headerItemBottomLeft;
    public ImageView headerItemBottomRigth;
    public TextView headerItemTextViewFirst;
    public TextView headerItemTextViewSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this, R.color.colorWhite, R.color.colorWhite);
        setContentView(R.layout.activity_wifi_information);
        setUpHeader();
        initViews();
        initialWork();
    }

    @Override
    public void onResume() {
        super.onResume();

        timer = new Timer("ScanResult");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiBroadCastReceiver, intentFilter);

        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getWifiList();
                    }
                });
            }
        };
        timer.schedule(updateTask, 10000L, 10000L);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Objects.requireNonNull(this).unregisterReceiver(wifiBroadCastReceiver);
        unregisterReceiver(wifiBroadCastReceiver);
        if (timer != null) {
            timer.cancel();
        }
    }

    void setUpHeader() {
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

        headerItemCenterRight.setImageResource(R.drawable.ic_header_item_wifi_info);
        headerItemTextViewFirst.setText(getResources().getString(R.string.wifi));
        headerItemTextViewSecond.setText(R.string.deatail);


    }

    private void initViews() {
        txt_ssId = findViewById(R.id.ssid);
        txt_ip_address = findViewById(R.id.ipaddress);
        txt_mac_address = findViewById(R.id.macaddress);
        txt_bssId = findViewById(R.id.bssidbn);
        txt_linkSpeed = findViewById(R.id.linkspeed);
        txt_networkId = findViewById(R.id.networkid);
        txt_rssId = findViewById(R.id.rssid);
        txt_channel_level = findViewById(R.id.freq_level);
        txt_channel_num = findViewById(R.id.freq_num);
    }

    private void initialWork() {
        scan_Results = new ArrayList<>();

        statusCheck();

        getWifiInformation();

        wifiManager = (WifiManager) Objects.requireNonNull(getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        wifiBroadCastReceiver = new WifiBroadCastReceiver();

        if (!wifiManager.isWifiEnabled()) {
            if (Build.VERSION.SDK_INT >= 29) {
                Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                startActivityForResult(panelIntent, REQUEST_CODE_FOR_ENABLE_WIFI);
            } else {
                wifiManager.setWifiEnabled(true);
            }
            getWifiList();
        } else {
            getWifiList();
        }


    }

    public class WifiBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                scanSuccess();
            } else {
                scanSuccess();
            }
        }
    }

    private void scanSuccess() {
        scan_Results = (ArrayList<ScanResult>) wifiManager.getScanResults();
        HashSet<ScanResult> hashSet = new HashSet<>();
        hashSet.addAll(scan_Results);
        scan_Results.clear();
        scan_Results.addAll(hashSet);
    }

    private void getWifiList() {
        wifiManager.startScan();
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

    private void getWifiInformation() {
        WifiManager wifiManager = (WifiManager) Objects.requireNonNull(getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        ChannelInfo channelinfo = new ChannelInfo();
        channelinfo = currentChannelfrequency(wifiInfo.getFrequency());

        String freqlevel = channelinfo.getChannelLevel();
        int channelNum = channelinfo.getChannelNumber();

        int ipAddress = wifiInfo.getIpAddress();
        String macAddress = wifiInfo.getMacAddress();
        String bss_id = wifiInfo.getBSSID();
        String ss_id = wifiInfo.getSSID();
        int link_speed = wifiInfo.getLinkSpeed();
        int rss_id = wifiInfo.getRssi();
        int network_id = wifiInfo.getNetworkId();

        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));

        txt_ssId.setText("   " + ss_id);
        txt_ip_address.setText("   " + ip);
        txt_mac_address.setText("   " + macAddress);
        txt_linkSpeed.setText("   " + link_speed);
        txt_rssId.setText("   " + rss_id);
        txt_networkId.setText("   " + network_id);
        txt_bssId.setText("   " + bss_id);
        txt_channel_level.setText("   " + freqlevel);
        txt_channel_num.setText("   " + channelNum);
    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) Objects.requireNonNull(this).getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        hasGPSDevice(Objects.requireNonNull(this));
        enableLoc();
    }

    public boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(this))
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(ActivityWifiInformation.this, REQUEST_CODE_LOCATION);
//                                finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_FOR_ENABLE_WIFI: {
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWifiList();
                    }
                }, 5000);
            }
            break;
        }
    }


}