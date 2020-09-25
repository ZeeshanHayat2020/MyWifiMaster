package com.internet.speed.test.analyzer.wifi.key.generator.app.wifiAvailable;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.example.flatdialoglibrary.dialog.FlatDialog;
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
import com.internet.speed.test.analyzer.wifi.key.generator.app.activities.ActivityBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AvailableWifiActivity extends ActivityBase implements Available_Wifi_ListAdapter.OnWifiClickListener {

    private WifiManager wifi_Manager;
    private ArrayList<ScanResult> scan_Results;
    private RecyclerView wifi_recyclerview;
    private Available_Wifi_ListAdapter adapter;
    private wifi_BroadCast_Receiver wifi_broadcast_receiver;
    private Handler handler_obj;
    private final String URL = "http://emphirezone.com/wifimaster/SavePassword.php?";
    SwipeRefreshLayout swipeRefreshLayout;
    Location location;
    private RelativeLayout recyclerViewRoot;
    private LottieAnimationView animationView;
    private RelativeLayout animRootView;
    private Button btnScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this, R.color.colorWhite, R.color.white);
        setContentView(R.layout.activity_available_wifi);

      /*  Toolbar toolbar = findViewById(R.id.toolbarAvailableWifi);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        LocationManager lm = (LocationManager) AvailableWifiActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(AvailableWifiActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AvailableWifiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please Enable location", Toast.LENGTH_SHORT).show();
        }

        location = Objects.requireNonNull(lm).getLastKnownLocation(LocationManager.GPS_PROVIDER);


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Saving Password");
        progressDialog.setMessage("Please Wait For A while");

        wifi_Manager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        scan_Results = new ArrayList<>();
        wifi_recyclerview = findViewById(R.id.wifi_recyclerview);
        wifi_broadcast_receiver = new wifi_BroadCast_Receiver();
        final TextView txt = findViewById(R.id.list_empty_txt);
        swipeRefreshLayout = findViewById(R.id.swipe_Refresh_layout);

        recyclerViewRoot = findViewById(R.id.acScanWifi_recyclerViewRoot);
        animRootView = findViewById(R.id.acScanWifi_scanAnimRoot);
        animationView = findViewById(R.id.acScanWifi_scanAnim);
        btnScan = findViewById(R.id.acScanWifi_btnScan);
        btnScan.setOnClickListener(MyOnClickListener);


        if (!wifi_Manager.isWifiEnabled()) {
            if (Build.VERSION.SDK_INT >= 29) {
                Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                startActivityForResult(panelIntent, REQUEST_CODE_FOR_ENABLE_WIFI);
            } else {
                Toast.makeText(this, "Turning WiFi ON...", Toast.LENGTH_LONG).show();
                wifi_Manager.setWifiEnabled(true);
            }

        }
        wifi_Manager.startScan();
        get_All_Wifi_list();
        loadingWifiListAgain();
        if (scan_Results.isEmpty()) {
            txt.setVisibility(View.GONE);
        }
        ScanNetworks();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        get_All_Wifi_list();
                        ScanNetworks();
                        loadingWifiListAgain();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }


    View.OnClickListener MyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            animationView.playAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animRootView.setVisibility(View.INVISIBLE);
                    recyclerViewRoot.setVisibility(View.VISIBLE);
                }
            }, 3000);
        }
    };

    @Override
    public void OnWifiClickListener(final int position) {

        final String selectedSSID = scan_Results.get(position).SSID;

        final FlatDialog flatDialog = new FlatDialog(AvailableWifiActivity.this);
        flatDialog.setTitle(getString(R.string.please_enter_password))
                .setTitleColor(getResources().getColor(R.color.colorPrimary))
                .setFirstTextFieldHint(getString(R.string.eg_wifi_password))
                .setFirstTextFieldHintColor(getResources().getColor(R.color.colorPrimary))
                .setFirstTextFieldBorderColor(getResources().getColor(R.color.colorPrimaryDark))
                .setFirstButtonText(getString(R.string.connect))
                .setFirstTextFieldTextColor(getResources().getColor(R.color.colorPrimary))
                .setSecondButtonText(getString(R.string.cancel))
                .setBackgroundColor(getResources().getColor(R.color.white))
                .setFirstButtonTextColor(getResources().getColor(R.color.white))
                .setSecondButtonTextColor(getResources().getColor(R.color.white))
                .setFirstButtonColor(getResources().getColor(R.color.colorAccent))
                .setSecondButtonColor(getResources().getColor(R.color.colorAccent))
                .withFirstButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String password = flatDialog.getFirstTextField();
                        if (password.isEmpty()) {
                            Toast.makeText(AvailableWifiActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!ConnectToNetworkWPA(selectedSSID, password)) {
                                Toast.makeText(AvailableWifiActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                flatDialog.dismiss();
                            }
                            flatDialog.dismiss();
                        }


                        //===========================connect to wifi===============================

                    }
                }).withSecondButtonListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flatDialog.dismiss();
            }
        }).show();

    }


    private boolean ConnectToNetworkWPA(String networkSSID, String password) {
        try {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";
            conf.preSharedKey = "\"" + password + "\"";
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            Log.d("connecting", conf.SSID + " " + conf.preSharedKey);

            WifiManager wifiManager = (WifiManager) AvailableWifiActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Objects.requireNonNull(wifiManager).addNetwork(conf);

            Log.d("after connecting", conf.SSID + " " + conf.preSharedKey);

            @SuppressLint("MissingPermission")
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    Log.d("re connecting", i.SSID + " " + conf.preSharedKey);
                    break;
                }
            }
            //WiFi Connection success, return true
            return true;
        } catch (Exception ex) {
            Toast.makeText(AvailableWifiActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    //==============================Save Wifi Password to phpMyadmin======================================

 /*   private void Save_Password_to_localHost(final String name, final String lat, final String lon, final String password) {
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AvailableWifiActivity.this, response, Toast.LENGTH_LONG).show();
                        Log.d("TAG", "onResponse: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AvailableWifiActivity.this, "Something went wrong\n" + error.toString(), Toast.LENGTH_LONG).show();
                        Log.d("volleyError", error.toString(), error);
                        Log.e("TAG", "onErrorResponse: " + error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> key = new HashMap<>();
                key.put("name", name);
                key.put("password", password);
                key.put("lati", lat);
                key.put("longi", lon);
                return key;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(this).getApplicationContext(), new HurlStack());
        requestQueue.add(request);


    }*/

    //==================================================In App Purchase=================================================

    //========================================End=========================================================

    public class wifi_BroadCast_Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            scan_Results = (ArrayList<ScanResult>) wifi_Manager.getScanResults();
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResume() {
        this.registerReceiver(wifi_broadcast_receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    public void onPause() {
        this.unregisterReceiver(wifi_broadcast_receiver);
        super.onPause();
    }


    private void loadingWifiListAgain() {

        handler_obj = new Handler();
        // do your work
        Runnable runnable_obj = new Runnable() {
            @Override
            public void run() {
                // do your work
                get_All_Wifi_list();
                adapter.notifyDataSetChanged();
                handler_obj.postDelayed(this, 1000);
            }
        };
        handler_obj.postDelayed(runnable_obj, 1000);
    }

    private void get_All_Wifi_list() {
        scan_Results = (ArrayList<ScanResult>) wifi_Manager.getScanResults();
        adapter = new Available_Wifi_ListAdapter(this, scan_Results, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        wifi_recyclerview.setLayoutManager(mLayoutManager);
        wifi_recyclerview.setAdapter(adapter);
    }


    private void ScanNetworks() {
        boolean scan = wifi_Manager.startScan();
        if (!scan) {
            scan_Results = (ArrayList<ScanResult>) wifi_Manager.getScanResults();
            adapter.notifyDataSetChanged();
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
                        ScanNetworks();
                    }
                }, 5000);
            }
            break;
        }
    }
}
