package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.internet.speed.test.analyzer.wifi.key.generator.app.services.GpsTracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.internet.speed.test.analyzer.wifi.key.generator.app.activities.MainActivity.REQUEST_LOCATION;

public class ActivityLiveLocation extends ActivityBase {


    public static final int REQUEST_CODE_FOR_LOCATION_PERMISSIO = 121;
    private Button btnSearchLocation;
    private GpsTracker gpsTracker;

    private CardView rootViewTableLayout;
    private ProgressBar loadingBar;
    private TextView tvCity;
    private TextView tvState;
    private TextView tvCountry;
    private TextView tvPostalCode;
    private TextView tvLat;
    private TextView tvLong;

    private GoogleApiClient googleApiClient;

    private WifiManager wifiManager;


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
        setContentView(R.layout.activity_live_location);
        checkLocationPermission();
        setUpHeader();
        iniViews();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
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
        headerItemTextViewSecond.setVisibility(View.INVISIBLE);
        headerItemCenterRight.setImageResource(R.drawable.ic_header_item_livelocation);
        headerItemTextViewFirst.setText(R.string.libve_location);


    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean canAccessCoreLocation() {
        return (hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(ActivityLiveLocation.this, perm));
    }

    public void checkLocationPermission() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void iniViews() {
        rootViewTableLayout = findViewById(R.id.acLL_rootView_table_layout);
        loadingBar = findViewById(R.id.acLL_loadingBar);
        tvCity = findViewById(R.id.acLL_tv_city);
        tvState = findViewById(R.id.acLL_tv_state);
        tvCountry = findViewById(R.id.acLL_tv_contry);
        tvPostalCode = findViewById(R.id.acLL_tv_postalCode);
        tvLat = findViewById(R.id.acLL_tv_Latitude);
        tvLong = findViewById(R.id.acLL_tv_longitude);
        rootViewTableLayout.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);

        btnSearchLocation = findViewById(R.id.acLiveLocation_btn_searchLocation);
        btnSearchLocation.setOnClickListener(onButtonClicked);

    }


    private View.OnClickListener onButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!canAccessLocation() || !canAccessCoreLocation()) {
                checkLocationPermission();
            } else {
                getLocation();
            }
        }
    };

    private void getLocation() {
        loadingBar.setVisibility(View.VISIBLE);
        rootViewTableLayout.setVisibility(View.INVISIBLE);
        gpsTracker = new GpsTracker(this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            tvLat.setText(String.valueOf(latitude));
            tvLong.setText(String.valueOf(longitude));
            getAddressFromLocation2(latitude, longitude);
        } else {
//            gpsTracker.showSettingsAlert();
            enableLoc();
        }
    }

    public void getAddressFromLocation2(final double latitude, final double longitude) {
        Geocoder geocoder = new Geocoder(ActivityLiveLocation.this, Locale.getDefault());
        String city = null;
        String state = null;
        String country = null;
        String postal = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                city = address.getLocality();
                state = address.getAdminArea();
                country = address.getCountryName();
                postal = address.getPostalCode();
            }
        } catch (IOException e) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        } finally {
            if (city != null) {
                tvCity.setText(city);
            } else {
                tvCity.setText("Failed to get City Name");
            }
            if (state != null) {
                tvState.setText(state);
            } else {
                tvState.setText("Failed to get State Name");
            }
            if (country != null) {
                tvCountry.setText(country);
            } else {
                tvCountry.setText("Failed to get Country Name");
            }
            if (postal != null) {
                tvPostalCode.setText(postal);
            } else {
                tvPostalCode.setText("Failed to get Postal Code");
            }
            new Handler(getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingBar.setVisibility(View.INVISIBLE);
                    rootViewTableLayout.setVisibility(View.VISIBLE);
                }
            }, 2000);


        }


    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(ActivityLiveLocation.this)
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
                                status.startResolutionForResult(ActivityLiveLocation.this, REQUEST_LOCATION);

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_FOR_LOCATION_PERMISSIO: {
                if (canAccessLocation() && canAccessCoreLocation()) {
                    getLocation();
                }
            }
            break;
        }
    }


}