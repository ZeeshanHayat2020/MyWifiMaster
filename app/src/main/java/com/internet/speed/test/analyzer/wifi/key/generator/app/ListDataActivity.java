package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
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
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;
import com.internet.speed.test.analyzer.wifi.key.generator.app.activities.ActivityBase;
import com.internet.speed.test.analyzer.wifi.key.generator.app.activities.MainActivity;
import com.internet.speed.test.analyzer.wifi.key.generator.app.adapters.AdapterMain;
import com.internet.speed.test.analyzer.wifi.key.generator.app.adapters.AdapterShowPassword;
import com.internet.speed.test.analyzer.wifi.key.generator.app.interfaces.OnRecyclerItemClickeListener;
import com.internet.speed.test.analyzer.wifi.key.generator.app.models.ModelMain;
import com.internet.speed.test.analyzer.wifi.key.generator.app.wifiAvailable.AvailableWifiActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.os.Build.VERSION.SDK_INT;

public class ListDataActivity extends ActivityBase {


    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;
    TextView EmptyText;
    private Button btnGotoScanWifi;
    AdView adView;
    //myDbAdapter obj;
    private ListView mListView;
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private AdapterShowPassword mAdapter;

    private RelativeLayout layoutHeader;

    public ImageView headerItemCenterRight;
    public TextView headerItemTextViewFirst;
    public TextView headerItemTextViewSecond;

    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this, R.color.colorWhite, R.color.colorWhite);
        setContentView(R.layout.list_layout);
        if (haveNetworkConnection()) {
            requestBanner((FrameLayout) findViewById(R.id.bannerContainer));
        }
        setUpHeader();
        mDatabaseHelper = new DatabaseHelper(this);
        EmptyText = findViewById(R.id.EmptyText);
        btnGotoScanWifi = findViewById(R.id.btnGo_to_ScanWIFI);
        adView = findViewById(R.id.banner_ad);
        iniRecyclerView();
        setUpRecyclerView();

        btnGotoScanWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasGPSDevice(ListDataActivity.this)) {
                    if (hasGpsEnable()) {
                        Intent intent = new Intent(ListDataActivity.this, AvailableWifiActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        enableLoc();

                    }
                }
            }
        });

    }


    void setUpHeader() {
        layoutHeader = findViewById(R.id.header_acLanugage);

        headerItemCenterRight = findViewById(R.id.header_item_centerRight_imageView);

        headerItemTextViewFirst = findViewById(R.id.header_item_textView_First);
        headerItemTextViewSecond = findViewById(R.id.header_item_textView_Second);
        headerItemCenterRight.setImageResource(R.drawable.ic_header_item_generate_passworf);
        headerItemTextViewFirst.setText(getResources().getString(R.string.WIFI));
        headerItemTextViewSecond.setText(R.string.PASSWORD);


    }

    private void iniRecyclerView() {
        recyclerView = findViewById(R.id.acShowPassword_recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void setUpRecyclerView() {
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<String> passwrodList = new ArrayList<>();

        Cursor data = mDatabaseHelper.getData();
        while (data.moveToNext()) {
            nameList.add(data.getString(1));
            passwrodList.add(data.getString(2));
        }
        if (passwrodList.isEmpty() || nameList.isEmpty()) {
            EmptyText.setVisibility(View.VISIBLE);
            btnGotoScanWifi.setVisibility(View.VISIBLE);
            EmptyText.setText("List Empty!");
        }
        mAdapter = new AdapterShowPassword(this, nameList, passwrodList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

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

    boolean hasGpsEnable() {
        final LocationManager manager = (LocationManager) ListDataActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }


    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(ListDataActivity.this)
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
                                status.startResolutionForResult(ListDataActivity.this, REQUEST_LOCATION);

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


}
