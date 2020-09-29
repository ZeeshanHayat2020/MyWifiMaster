package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SignalGraphActivity extends ActivityBase {


    private Timer timer;
    private WifiManager wifiManager;
    private ArrayList<ScanResult> scan_Results;
    WifiBroadCastReceiver wifiBroadCastReceiver;
    public static final String graphhh = "dfffdfdf";
    BarChart mBarChart;
    private GoogleApiClient googleApiClient;
    private int REQUEST_LOCATION = 7172;



    public ImageView headerItemCenterRight;
    public TextView headerItemTextViewFirst;
    public TextView headerItemTextViewSecond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this, R.color.colorWhite, R.color.colorWhite);
        setContentView(R.layout.activity_signal_graph);
        if (haveNetworkConnection()) {
            requestBanner((FrameLayout) findViewById(R.id.bannerContainer));
        }
        setUpHeader();
        initialWork();

    }

    void setUpHeader() {

        headerItemCenterRight = findViewById(R.id.header_item_centerRight_imageView);
        headerItemTextViewFirst = findViewById(R.id.header_item_textView_First);
        headerItemTextViewSecond = findViewById(R.id.header_item_textView_Second);

        headerItemCenterRight.setImageResource(R.drawable.ic_header_item_signal_strength);
        headerItemTextViewFirst.setText(getResources().getString(R.string.WIFI));
        headerItemTextViewSecond.setText(R.string.signal_stregnth);


    }

    private void initialWork() {

        mBarChart = findViewById(R.id.stackedBarChart);
        scan_Results = new ArrayList<>();

        statusCheck();

        wifiManager = (WifiManager) Objects.requireNonNull(getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        wifiBroadCastReceiver = new WifiBroadCastReceiver();

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            getWifiList();
        } else {
            getWifiList();
        }
    }

    public class WifiBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            Log.d(graphhh, "onReceive: onReceive");
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
        setGraph();
        Log.d(graphhh, "onSuccess: Success adapter change " + scan_Results.size());
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

    private void getWifiList() {
        wifiManager.startScan();
        Log.d(graphhh, "getWifiList: called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void setGraph() {
        ArrayList<BarEntry> values = new ArrayList<>();
        ArrayList<String> barEntryLabels = new ArrayList<>();

        for (int i = 0; i < scan_Results.size(); i++) {
            String name = scan_Results.get(i).SSID;
            float level = (float) (WifiManager.calculateSignalLevel(scan_Results.get(i).level, 40) * 2.5);

            values.add(new BarEntry(level, i));
            barEntryLabels.add(name);
        }

        int[] colors = {
                R.color.colorBarChart1,
                R.color.colorBarChart2,
                R.color.colorBarChart3,
                R.color.colorBarChart4,
                R.color.colorBarChart5,
                R.color.colorBarChart6,

        };

        BarDataSet bardataset = new BarDataSet(values, "Available Wifi Signal Strengths");
        bardataset.setValueTextSize(6f);
        BarData BARDATA = new BarData(barEntryLabels, bardataset);
        bardataset.setColors(ColorTemplate.createColors(getResources(), colors));
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setDescription("");
        mBarChart.setData(BARDATA);
        mBarChart.getXAxis().mLabelWidth = 20;
        mBarChart.getXAxis().setLabelsToSkip(0);
        mBarChart.animateY(3000);

    }

    private void configureChartAppearance() {
        mBarChart.setDrawGridBackground(false);
        mBarChart.setDrawValueAboveBar(false);

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
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
                                status.startResolutionForResult(SignalGraphActivity.this, REQUEST_LOCATION);
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

}