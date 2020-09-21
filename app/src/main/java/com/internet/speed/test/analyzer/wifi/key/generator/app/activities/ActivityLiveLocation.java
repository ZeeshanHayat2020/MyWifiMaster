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
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.services.GpsTracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ActivityLiveLocation extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_location);
        checkLocationPermission();
        iniViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                getLocation();
            }
        }, 1000);

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

    private void checkLocationPermission() {
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
        gpsTracker = new GpsTracker(this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            tvLat.setText(String.valueOf(latitude));
            tvLong.setText(String.valueOf(longitude));
            getAddressFromLocation2(latitude, longitude);
        } else {
            gpsTracker.showSettingsAlert();
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
            loadingBar.setVisibility(View.INVISIBLE);
            rootViewTableLayout.setVisibility(View.VISIBLE);

        }


    }


    public void getAddressFromLocation(final double latitude, final double longitude, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                String fulAddress = null;
                String city = null;
                String state = null;
                String country = null;
                String postal = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)); //.append("\n");
                            fulAddress = address.getAddressLine(i);
                        }

                        city = address.getLocality();
                        state = address.getAdminArea();
                        country = address.getCountryName();
                        postal = address.getPostalCode();

                        sb.append(address.getLocality()).append(",");
                        sb.append(address.getPostalCode()).append(",");
                        sb.append(address.getCountryName());
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e("Location Address Loader", "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (fulAddress != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", fulAddress);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        fulAddress = " Unable to get address for this location.";
                        bundle.putString("address", fulAddress);
                        message.setData(bundle);
                    }
                    if (city != null) {
                        message.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putString("city", city);
                        message.setData(bundle);
                    } else {
                        message.what = 2;
                        Bundle bundle = new Bundle();
                        city = " Unable to get city name.";
                        bundle.putString("city", city);
                        message.setData(bundle);
                    }
                    if (state != null) {
                        message.what = 3;
                        Bundle bundle = new Bundle();
                        bundle.putString("state", state);
                        message.setData(bundle);
                    } else {
                        message.what = 3;
                        Bundle bundle = new Bundle();
                        state = " Unable to get state Name.";
                        bundle.putString("state", state);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
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