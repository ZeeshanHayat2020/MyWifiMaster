package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import com.airbnb.lottie.LottieAnimationView;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.reward.RewardedVideoAd;
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
import com.internet.speed.test.analyzer.wifi.key.generator.app.GenerateActivity;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Helper.LocalHelper;
import com.internet.speed.test.analyzer.wifi.key.generator.app.ListDataActivity;
import com.internet.speed.test.analyzer.wifi.key.generator.app.PrefManager;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Preferences;
import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.SelectCountry;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Speedtest;
import com.internet.speed.test.analyzer.wifi.key.generator.app.TermsandConditionActivity;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.Custom_Dialog_Class;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;
import com.internet.speed.test.analyzer.wifi.key.generator.app.WifiInfoActivity;
import com.internet.speed.test.analyzer.wifi.key.generator.app.WifiScanActivity;
import com.internet.speed.test.analyzer.wifi.key.generator.app.adapters.AdapterMain;
import com.internet.speed.test.analyzer.wifi.key.generator.app.autoConnectWifi.AutoConnectWifi;
import com.internet.speed.test.analyzer.wifi.key.generator.app.interfaces.OnRecyclerItemClickeListener;
import com.internet.speed.test.analyzer.wifi.key.generator.app.models.ModelMain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.drakeet.support.toast.ToastCompat;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout recyclerRootView;
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private AdapterMain mAdapter;
    private List<ModelMain> bottomViewList = new ArrayList<>();


    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static int flag, keyone;
    WifiManager mainWifiObj;
    String[] title;
    ImageView btnshowpass;
    ImageView btnhotspot;
    ImageView btnspeedtest;
    ImageView btnAutoStartWifi;
    TextView showpass, scanWifi, wifispeed, GenaratePWD, Hotspot, Setting, Info, Share;
    InterstitialAd interstitial;
    AdView adView;
    int countAd = 0;
    AdView banner;
    ImageView btngenerate;
    ImageView btnscanwifi;
    ImageView btnsetting;

    ImageView imgSelectLanguage, imgNoAds;

    Activity activity;
    SharedPreferences preferences;

    Boolean ison = false;
    ImageView btnShowWifiState;
    ImageView btnShowWifiStrengthMeter;
    ImageView btnChangeLanguage;
    ImageView btnSettings;
    FrameLayout frameLayout;
    RewardedVideoAd mAd;
    LottieAnimationView rateUsLottie;
    SelectCountry obj = new SelectCountry();

    boolean isCompleteWatch = false;
    private Context context;

    protected static final String TAG = "LocationOnOff";

    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

    BillingProcessor bp;
    AlertDialog.Builder builder;
    private boolean mShouldAnimateMenuItem = true;
    private UnifiedNativeAd nativeAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preferences preferences1 = new Preferences(this);
        Locale locale = new Locale(preferences1.GetValueStringlang(preferences1.LANG_VALUE));
        setLocale(locale);
        setContentView(R.layout.activity_my_main);
        MobileAds.initialize(this, getResources().getString(R.string.app_id));
        initViews();
        iniRecyclerView();
        setUpRecyclerView();


        context = getApplicationContext();
        activity = this;


        onrequestPermission();

        preferences = getSharedPreferences("PREFS", 0);


        InAppPrefManager.getInstance(this).setInAppStatus(false);

        requestNewInterstitial();

//        refreshAd();

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

        btnShowWifiState.setOnClickListener(onHeaderItemsClick);
        btnShowWifiStrengthMeter.setOnClickListener(onHeaderItemsClick);
        btnChangeLanguage.setOnClickListener(onHeaderItemsClick);
        btnSettings.setOnClickListener(onHeaderItemsClick);

    }

    View.OnClickListener onHeaderItemsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
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
                    SharedPreferences prefFirsTime = getSharedPreferences("PREFSs", 0);
                    SharedPreferences.Editor editor = prefFirsTime.edit();
                    editor.putString("firsttimedisp", "no");
                    editor.apply();
                    // String chk = prefFirsTime.getString("firsttimedisp", "0");
                    PrefManager prefManager = new PrefManager(MainActivity.this);
                    prefManager.setAcceptLanguage(false);
                    startActivity(new Intent(MainActivity.this, SelectCountry.class));
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
                "Wifi Signal Strength"
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

    private void onItemsClick(int position) {
        switch (position) {
            case 0: {
                if (interstitial != null) {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                    } else {

                        if (checkPermission()) {
                            requestNewInterstitial();
                            startActivity(new Intent(MainActivity.this, WifiScanActivity.class));
                        } else {

                            requestPermission();

                        }
                    }
                    interstitial.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            requestNewInterstitial();


                            if (checkPermission()) {
                                startActivity(new Intent(MainActivity.this, WifiScanActivity.class));
                            } else {

                                requestPermission();
                            }


//
                        }
                    });
                } else {


                    if (checkPermission()) {

                        startActivity(new Intent(MainActivity.this, WifiScanActivity.class));
                    } else {

                        requestPermission();

                    }

                }
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
                        Intent i = new Intent(MainActivity.this, PasswordGeneratorActivity.class);
                        startActivity(i);
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
                startActivity(new Intent(MainActivity.this, ActivityWifiInformation.class));
            }
            break;
            case 7: {
//                startActivity(new Intent(MainActivity.this, BluetoothActivityN.class));
                startActivity(new Intent(MainActivity.this, BluetoothConnectivity.class));
            }
            break;
            case 8: {
//                startActivity(new Intent(MainActivity.this, LocationActivity.class));
                startActivity(new Intent(MainActivity.this, ActivityLiveLocation.class));
            }
            break;
            case 9: {
                Intent intent = new Intent(this, SignalGraphActivity.class);
                startActivity(intent);
            }
            break;


        }
    }

    protected void intentTo(final Context context, final Activity activity) {
        Intent intent = new Intent(context, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
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

    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    private void setLocale(Locale locale) {
        // optional - Helper method to save the selected language to SharedPreferences in case you might need to attach to activity context (you will need to code this)
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        if (SDK_INT > Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }

    private void updateView(String language) {
        Context context = LocalHelper.setlocale(this, language);
        Resources resources = context.getResources();
        scanWifi.setText(resources.getString(R.string.scan_wifi));
        wifispeed.setText(resources.getString(R.string.wifi_speed));
        GenaratePWD.setText(resources.getString(R.string.generate_n_password));
        showpass.setText(resources.getString(R.string.show_npassword));
        Hotspot.setText(resources.getString(R.string.hotspot));
        Setting.setText(resources.getString(R.string.setting));
        Info.setText(resources.getString(R.string.wifi_n_information));
        Share.setText(resources.getString(R.string.share));

    }


    private boolean hasGPSDevice(Context context) {
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

    public void onrequestPermission() {
        this.setFinishOnTouchOutside(true);

        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MainActivity.this)) {

            Toast.makeText(MainActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
//            finish();
        }
        // Todo Location Already on  ... end

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
    @Override
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


    public void wificonnection(View view) {
    }

    public void share(View view) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = "https://play.google.com/store/apps/details?id=com.internet.speed.test.analyzer.wifi.key.generator.app";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
    }

    public void changeLang(View view) {
        Intent intent = new Intent(MainActivity.this, TermsandConditionActivity.class);
        clearApplicationData();

        startActivity(intent);
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

    //TOdo: native ads
    private void populateUnifiedNativeAdView(UnifiedNativeAd native_Ad, UnifiedNativeAdView adView) {
// Get the video controller for the ad. One will always be provided, even if the ad doesn't
// have a video asset.
        this.nativeAd = native_Ad;
        VideoController vc = nativeAd.getVideoController();

// Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
// VideoController will call methods on this object when events occur in the video
// lifecycle.
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
// Publishers should allow native ads to complete video playback before refreshing
// or replacing them with another ad in the same UI location.
// refresh.setEnabled(true);
// videoStatus.setText("Video status: Video playback has ended.");
                super.onVideoEnd();
            }
        });

        MediaView mediaView = adView.findViewById(R.id.ad_media);
        ImageView mainImageView = adView.findViewById(R.id.ad_image);

// Apps can check the VideoController's hasVideoContent property to determine if the
// NativeAppInstallAd has a video asset.
        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mainImageView.setVisibility(View.GONE);
// videoStatus.setText(String.format(Locale.getDefault(),
// "Video status: Ad contains a %.2f:1 video asset.",
// vc.getAspectRatio()));
        } else {
            adView.setImageView(mainImageView);
            mediaView.setVisibility(View.GONE);

// At least one image is guaranteed.
            List<NativeAd.Image> images = nativeAd.getImages();
            mainImageView.setImageDrawable(images.get(0).getDrawable());

// refresh.setEnabled(true);
// videoStatus.setText("Video status: Ad does not contain a video asset.");
        }

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

// Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

// These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
// check before trying to display them.
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }


    //TOdo: refresh native ads
    private void refreshAd() {

        try {

            //refresh.setEnabled(false);
            AdLoader.Builder builder = new AdLoader.Builder(getApplicationContext(), getString(R.string.nativead));
            builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                // OnUnifiedNativeAdLoadedListener implementation.
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    try {

                        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                                .inflate(R.layout.ad_unified, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adView);

                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

            });

// VideoOptions videoOptions = new VideoOptions.Builder()
// .setStartMuted(startVideoAdsMuted.isChecked())
// .build();

// NativeAdOptions adOptions = new NativeAdOptions.Builder()
// .setVideoOptions(videoOptions)
// .build();
//
// builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
// refresh.setEnabled(true);
// Toast.makeText(getContext(), "Failed to load native ad: "
// + errorCode, Toast.LENGTH_SHORT).show();

                    UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                            .inflate(R.layout.ad_unified, null);
                    adView.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.GONE);
                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());

// videoStatus.setText("");

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void onClickWifiInfo(View view) {
        startActivity(new Intent(MainActivity.this, WifiInfoActivity.class));
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

                SharedPreferences prefFirsTime = getSharedPreferences("PREFSs", 0);
                SharedPreferences.Editor editor = prefFirsTime.edit();
                editor.putString("firsttimedisp", "no");
                editor.apply();
                startActivity(new Intent(MainActivity.this, SelectCountry.class));

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}