package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
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
import com.internet.speed.test.analyzer.wifi.key.generator.app.Helper.LocalHelper;
import com.internet.speed.test.analyzer.wifi.key.generator.app.ListDataActivity;
import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Speedtest;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.Custom_Dialog_Class;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;
import com.internet.speed.test.analyzer.wifi.key.generator.app.WifiScanActivity;
import com.internet.speed.test.analyzer.wifi.key.generator.app.adapters.AdapterMain;
import com.internet.speed.test.analyzer.wifi.key.generator.app.allRouterPassword.AllRouterPasswords;
import com.internet.speed.test.analyzer.wifi.key.generator.app.appsNetBlocker.NetBlockerMainActivity;
import com.internet.speed.test.analyzer.wifi.key.generator.app.autoConnectWifi.AutoConnectWifi;
import com.internet.speed.test.analyzer.wifi.key.generator.app.interfaces.OnRecyclerItemClickeListener;
import com.internet.speed.test.analyzer.wifi.key.generator.app.models.ModelMain;
import com.internet.speed.test.analyzer.wifi.key.generator.app.wifiAvailable.AvailableWifiActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.drakeet.support.toast.ToastCompat;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends ActivityBase {


    private RelativeLayout recyclerViewRoot;
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private AdapterMain mAdapter;
    private List<ModelMain> bottomViewList = new ArrayList<>();

    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static int flag, keyone;
    TextView showpass, scanWifi, wifispeed, GenaratePWD, Hotspot, Setting, Info, Share;
    InterstitialAd interstitial;
    Activity activity;
    SharedPreferences preferences;
    Boolean ison = false;
    ImageView btnShowWifiState;
    ImageView btnShowWifiStrengthMeter;
    ImageView btnChangeLanguage;
    ImageView btnSettings;
    FrameLayout frameLayout;
    private Context context;
    protected static final String TAG = "LocationOnOff";
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

    BillingProcessor bp;
    AlertDialog.Builder builder;
    private boolean mShouldAnimateMenuItem = true;
    private UnifiedNativeAd nativeAd;

    private CardView permissionRootView;
    private TextView permissionMsgView;
    private Button permissionAllowBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_main);
        MobileAds.initialize(this, getResources().getString(R.string.app_id));
        initViews();
        iniRecyclerView();
        setUpRecyclerView();
        context = getApplicationContext();
        activity = this;
//        onrequestPermission();
        preferences = getSharedPreferences("PREFS", 0);

        InAppPrefManager.getInstance(this).setInAppStatus(false);
        requestNewInterstitial();
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Boolean test = wifi.setWifiEnabled(true);

        if (isInternetIsConnected(getApplicationContext()) || test == true) {
            //if (flag == 1) {
            btnShowWifiState.setImageResource(R.drawable.enable);
            keyone = 1;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("enable", "yes");
            editor.apply();
        }


    }

    private void initViews() {
        btnShowWifiState = (ImageView) findViewById(R.id.acMainShowWifiState_imageView);
        btnShowWifiStrengthMeter = (ImageView) findViewById(R.id.acMainShowWifiStrengthMeter_imageView);
        btnChangeLanguage = (ImageView) findViewById(R.id.acMainChangeLanguage_imageView);
        btnSettings = (ImageView) findViewById(R.id.acMainSettings_imageView);
        recyclerViewRoot = findViewById(R.id.acMain_RecyclerView_RootView);
        permissionRootView = findViewById(R.id.acMain_PermissionRootView);
        permissionMsgView = findViewById(R.id.acMain_PermissionMessage);
        permissionAllowBtn = findViewById(R.id.acMain_PermissionButton);

        btnShowWifiState.setOnClickListener(onHeaderItemsClick);
        btnShowWifiStrengthMeter.setOnClickListener(onHeaderItemsClick);
        btnChangeLanguage.setOnClickListener(onHeaderItemsClick);
        btnSettings.setOnClickListener(onHeaderItemsClick);
        permissionAllowBtn.setOnClickListener(onHeaderItemsClick);
        setPermissionMessage();

        if (hasLocationPermission() && hasStoragePermission()) {
            recyclerViewRoot.setVisibility(View.VISIBLE);
            permissionRootView.setVisibility(View.INVISIBLE);
        } else {
            recyclerViewRoot.setVisibility(View.INVISIBLE);
            permissionRootView.setVisibility(View.VISIBLE);
        }

    }


    View.OnClickListener onHeaderItemsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.acMain_PermissionButton: {
                    onPermissionButtonClicked();
                }
                break;
                case R.id.acMainShowWifiStrengthMeter_imageView: {
                }
                break;
                case R.id.acMainShowWifiState_imageView: {
                    if (keyone == 0) {
                        keyone = 1;
                        if (android.os.Build.VERSION.SDK_INT == 25) {

                            ToastCompat.makeText(context, "Wifi is ON", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Wifi is ON", Toast.LENGTH_SHORT).show();

                        }
                        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        wifi.setWifiEnabled(true);
                        btnShowWifiState.setImageResource(R.drawable.enable);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("enable", "yes");
                        editor.apply();

                    } else {
                        keyone = 0;
                        if (android.os.Build.VERSION.SDK_INT == 25) {

                            ToastCompat.makeText(context, "Wifi is OFF", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Wifi is OFF", Toast.LENGTH_SHORT).show();

                        }
                        WifiManager wmgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        wmgr.setWifiEnabled(false);
                        btnShowWifiState.setImageResource(R.drawable.disable);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("enable", "no");
                        editor.apply();
                    }
                    ison = !ison;
                }
                break;
                case R.id.acMainChangeLanguage_imageView: {

                }
                break;
                case R.id.acMainSettings_imageView: {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
                break;

            }
        }
    };

    @Override
    protected void onPause() {

        if (isInternetIsConnected(getApplicationContext())) {
            if (flag == 1) {
                btnShowWifiState.setImageResource(R.drawable.enable);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("enable", "yes");
                editor.apply();
            } else {
                btnShowWifiState.setImageResource(R.drawable.disable);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("enable", "no");
                editor.apply();
            }
        }

        super.onPause();

    }


    private void iniRecyclerView() {
        recyclerView = findViewById(R.id.acMain_RecyclerView);
        mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        bottomViewList = new ArrayList<>();
    }

    private void setUpRecyclerView() {
        int[] imgIds = {
                R.drawable.ic_audiotrack_dark,
                R.drawable.ic_audiotrack_dark,
                R.drawable.ic_audiotrack_dark,
                R.drawable.ic_audiotrack_dark

        };
        String[] title = {
                "Scan Wifi",
                "Auto Wifi",
                "Generate Password",
                "Show Password",
                "Hotspot",
                "Wifi Speed",
                "Wifi information",
                "Bluetooth connect",
                "Live location",
                "Wifi Signal Strength",
                "App Data Usage",
                "Net Block",
                "All Router Password"
        };
        for (int i = 0; i < title.length; i++) {
            bottomViewList.add(new ModelMain(0, title[i]));
        }
        mAdapter = new AdapterMain(this, bottomViewList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickeListener() {
            @Override
            public void onItemClicked(int position) {
                onItemsClick(position);
            }

            @Override
            public void onItemLongClicked(int position) {

            }

            @Override
            public void onItemCheckBoxClicked(View view, int position) {

            }
        });
    }

    private void setPermissionMessage() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            permissionMsgView.setText(Html.fromHtml(getString(R.string.permissionMessage), Html.FROM_HTML_MODE_LEGACY));

        } else {
            permissionMsgView.setText(Html.fromHtml(getString(R.string.permissionMessage)));

        }


    }

    private void intentToLocationRelatedActivities(Activity activity) {
        if (hasGPSDevice(this)) {
            if (hasGpsEnable()) {
                Intent intent = new Intent(this, activity.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            } else {
                enableLoc();

            }
        }
    }

    void onPermissionButtonClicked() {
        if (!hasStoragePermission()) {
            checkStoragePermission();
        } else if (!hasLocationPermission()) {
            checkLocationPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!hasLocationPermission()) {
                    /*recyclerViewRoot.setVisibility(View.VISIBLE);
                    permissionRootView.setVisibility(View.INVISIBLE);*/
                    checkLocationPermission();
                } else {
                    recyclerViewRoot.setVisibility(View.VISIBLE);
                    permissionRootView.setVisibility(View.INVISIBLE);
                }
            }
        } else if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!hasStoragePermission()) {
                    checkStoragePermission();
                } else {
                    recyclerViewRoot.setVisibility(View.VISIBLE);
                    permissionRootView.setVisibility(View.INVISIBLE);
                }
            }
        }

    }


    private void onItemsClick(int position) {
        switch (position) {
            case 0: {
                intentToLocationRelatedActivities(new AvailableWifiActivity());
            }
            break;
            case 1: {
                startActivity(new Intent(MainActivity.this, AutoConnectWifi.class));
            }
            break;
            case 2: {
                if (interstitial != null) {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                    } else {
                        requestNewInterstitial();

                    }
                    interstitial.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            requestNewInterstitial();

                            Intent i = new Intent(MainActivity.this, PasswordGeneratorActivity.class);
                            startActivity(i);
                        }
                    });
                } else {

                    Intent i = new Intent(MainActivity.this, PasswordGeneratorActivity.class);
                    startActivity(i);

                }

            }
            break;
            case 3: {
                if (interstitial != null) {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                    } else {
                        requestNewInterstitial();
                        startActivity(new Intent(MainActivity.this, ListDataActivity.class));
                    }
                    interstitial.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            requestNewInterstitial();
                            startActivity(new Intent(MainActivity.this, ListDataActivity.class));
                        }
                    });
                } else {

                    startActivity(new Intent(MainActivity.this, ListDataActivity.class));


                }
            }
            break;
            case 4: {
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            break;
            case 5: {
                if (interstitial != null) {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                    } else {
                        requestNewInterstitial();
                        Intent i = new Intent(MainActivity.this, Speedtest.class);
                        startActivity(i);
                    }

                    interstitial.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            requestNewInterstitial();
                            Intent i = new Intent(MainActivity.this, Speedtest.class);
                            startActivity(i);
                        }
                    });

                } else {

                    Intent i = new Intent(MainActivity.this, Speedtest.class);
                    startActivity(i);

                }
            }
            break;
            case 6: {
                intentToLocationRelatedActivities(new ActivityWifiInformation());
            }
            break;
            case 7: {
//                startActivity(new Intent(MainActivity.this, BluetoothActivityN.class));
                startActivity(new Intent(MainActivity.this, BluetoothConnectivity.class));
            }
            break;
            case 8: {
//                startActivity(new Intent(MainActivity.this, LocationActivity.class));
                intentToLocationRelatedActivities(new ActivityLiveLocation());
            }
            break;
            case 9: {

                intentToLocationRelatedActivities(new SignalGraphActivity());
            }
            break;
            case 10: {
                Intent intent = new Intent(this, ActivityAppUsage.class);
                startActivity(intent);
            }
            break;
            case 11: {
                Intent intent = new Intent(this, NetBlockerMainActivity.class);
                startActivity(intent);
            }
            break;
            case 12: {
                Intent intent = new Intent(this, AllRouterPasswords.class);
                startActivity(intent);
            }
            break;


        }
    }


    public static boolean isInternetIsConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                flag = 1;
                return true;

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                flag = 0;
                return true;
            }
        } else {
            flag = 0;
            return false;
        }
        return false;
    }

    private boolean checkAndRequestPermissions() {
        int coarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (coarseLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (fineLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
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
            googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
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
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);

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

    boolean hasGpsEnable() {
        final LocationManager manager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    public void onrequestPermission() {
        this.setFinishOnTouchOutside(true);

        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MainActivity.this)) {

        }


        if (!hasGPSDevice(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        } else {
            Toast.makeText(MainActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("permission needed")
                    .setMessage("This permission is needed")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
            // Toast.makeText(context, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    //    @RequiresApi(api = Build.VERSION_CODES.M)
  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            } else {
                if (android.os.Build.VERSION.SDK_INT == 25) {

                    Toast.makeText(activity, "Need Location Permission to show available wifi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Need Location Permission to show available wifi", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
*/
    public void share(View view) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = "https://play.google.com/store/apps/details?id=com.internet.speed.test.analyzer.wifi.key.generator.app";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
    }

    public void changeLang(View view) {

        clearApplicationData();

    }

    private void requestNewInterstitial() {
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(this.getResources().getString(R.string.Interstitial));

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        interstitial.loadAd(adRequest);
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public void exitt() {
        // Build an AlertDialog
        Custom_Dialog_Class cdd = new Custom_Dialog_Class(MainActivity.this);
        cdd.show();
    }

    public void rate_us(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.internet.speed.test.analyzer.wifi.key.generator.app")));
    }

    @Override
    public void onBackPressed() {
        exitt();
    }

    private void inAppAlertDiaolBox() {
        builder = new AlertDialog.Builder(this, R.style.AlertDialog_AppCompat_);

        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage("Alert").setTitle("Alert!");

        //Setting message manually and performing action on button click
        builder.setMessage("You have to restart this Application.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                        inAppAlertDiaolBox();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!");
        alert.show();
    }

    Menu menu;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);

        this.menu = menu;
        menu.findItem(R.id.noads).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_language: {


                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}