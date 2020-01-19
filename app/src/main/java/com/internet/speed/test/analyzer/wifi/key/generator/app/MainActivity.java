package com.internet.speed.test.analyzer.wifi.key.generator.app;

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
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
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
import com.internet.speed.test.analyzer.wifi.key.generator.app.Helper.LocalHelper;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import me.drakeet.support.toast.ToastCompat;

public class MainActivity extends AppCompatActivity implements  BillingProcessor.IBillingHandler {
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static int flag, keyone;
    WifiManager mainWifiObj;
    //WifiScanReceiver wifiReciever;
    //ListView list;
    //String wifis[];
    String[] title;
    //DatabaseHelper mDatabaseHelper;
    ImageView btnshowpass;
    ImageView btnhotspot;
    ImageView btnspeedtest;
    TextView showpass, scanWifi, wifispeed, GenaratePWD, Hotspot, Setting, Info, Share;
    InterstitialAd interstitial;
    AdView adView;
    int countAd = 0;
    AdView banner;
    ImageView btngenerate;
    ImageView btnscanwifi;
    ImageView btnsetting;
    Activity activity;
    SharedPreferences preferences;
    // EditText pass;
    Boolean ison = false;
    ImageView btntOn;
    FrameLayout frameLayout;
    RewardedVideoAd mAd;
    SelectCountry obj = new SelectCountry();

    boolean isCompleteWatch = false;
    //private Handler mHandler = new Handler();
    private Context context;

    protected static final String TAG = "LocationOnOff";

    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

    BillingProcessor bp;
    AlertDialog.Builder builder;
    private boolean mShouldAnimateMenuItem = true;
    private UnifiedNativeAd nativeAd;

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

    //myDbAdapter obj;
    @Override
    protected void onPause() {

        if (isInternetIsConnected(getApplicationContext())) {
            if (flag == 1) {
                btntOn.setImageResource(R.drawable.enable);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("enable", "yes");
                editor.apply();
            } else {
                btntOn.setImageResource(R.drawable.disable);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("enable", "no");
                editor.apply();
            }
//        } else {
//            btntOn.setImageResource(R.drawable.disable);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("enable", "no");
//            editor.apply();
        }

        super.onPause();

    }

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

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
//            Toast.makeText(MainActivity.this, "Allow location permission to show available wifi", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @SuppressLint({"WifiManagerLeak", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getResources().getString(R.string.app_id));
        btntOn = findViewById(R.id.button1);
//        //  Button btntOff = (Button) findViewById(R.id.button2);
        btnshowpass = findViewById(R.id.Showpass);
        btnhotspot = findViewById(R.id.Wifihotspot);
        btnspeedtest = findViewById(R.id.Speedtest);
        btngenerate = findViewById(R.id.Generatekey);
        btnscanwifi = findViewById(R.id.Scanwiffi);
        btnsetting = findViewById(R.id.wifisettings);
        frameLayout = findViewById(R.id.fl_adplaceholderf);

        //All the Textviews which need to change languages.
        showpass = findViewById(R.id.tv_showpass);
        scanWifi = findViewById(R.id.tv_scanwifi);
        wifispeed = findViewById(R.id.tv_wifispeed);
        GenaratePWD = findViewById(R.id.tv_generatePWD);
        showpass = findViewById(R.id.tv_showpass);
        Hotspot = findViewById(R.id.tv_hotspot);
        Setting = findViewById(R.id.tv_setting);
        Share = findViewById(R.id.tv_Share);
        Info = findViewById(R.id.tv_wifiIfo);


        Bundle bundle = getIntent().getExtras();
        Paper.init(this);


        //////sherry commit

        bp = new BillingProcessor(this, null, this);
        bp.initialize();


        if(bp.isPurchased("android.test.purchased")){

            InAppPrefManager.getInstance(getApplicationContext()).setInAppStatus(true);
        }



//
//        @Override
//        public void onBillingInitialized() {
//            /*
//             * Called when BillingProcessor was initialized and it's ready to purchase
//             */
//        }
//
//        @Override
//        public void onProductPurchased(String productId, TransactionDetails details) {
//            /*
//             * Called when requested PRODUCT ID was successfully purchased
//             */
//        }
//
//        @Override
//        public void onBillingError(int errorCode, Throwable error) {
//            /*
//             * Called when some error occurred. See Constants class for more details
//             *
//             * Note - this includes handling the case where the user canceled the buy dialog:
//             * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
//             */
//        }
//
//        @Override
//        public void onPurchaseHistoryRestored() {
//            /*
//             * Called when purchase history was restored and the list of all owned PRODUCT ID's
//             * was loaded from Google Play
//             */
//        }



        String language = Paper.book().read("language");
//        if (language == null)
//            Paper.book().write("language", "en");

//        updateView((String) Paper.book().read("language"));
        if (bundle.getString("language") == "selected") {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            if (bundle.getString("language").equals("tr")) {
                Paper.book().write("language", "tr");
                updateView(Paper.book().read("language"));
            } else if (bundle.getString("language").equals("ru")) {
                Paper.book().write("language", "ru");
                updateView(Paper.book().read("language"));
            } else if (bundle.getString("language").equals("in")) {
                Paper.book().write("language", "in");
                updateView(Paper.book().read("language"));
            } else if (bundle.getString("language").equals("vi")) {
                Paper.book().write("language", "vi");
                updateView(Paper.book().read("language"));
            } else if (bundle.getString("language").equals("b+fil")) {
                Paper.book().write("language", "fil");
                updateView(Paper.book().read("language"));
            }
            else if (bundle.getString("language").equals("eng")) {
               // Paper.book().write("language", "en");
               // updateView(Paper.book().read("language"));
            }
        }

        context = getApplicationContext();
        activity = this;


        onrequestPermission();
//        requestPermission();
//        checkPermission();

        preferences = getSharedPreferences("PREFS", 0);
        if (!InAppPrefManager.getInstance(getApplicationContext()).getInAppStatus()) {

            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(this.getResources().getString(R.string.Interstitial));
            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    requestNewInterstitial();
                }
            });
        }

        if (!InAppPrefManager.getInstance(getApplicationContext()).getInAppStatus()) {

            requestNewInterstitial();
        }

        if (!InAppPrefManager.getInstance(getApplicationContext()).getInAppStatus()) {

            refreshAd();
        }

        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Boolean test = wifi.setWifiEnabled(true);

//        Toast.makeText(getApplicationContext(), "" + test, Toast.LENGTH_LONG).show();
        // wifi.getWifiState();

        if (isInternetIsConnected(getApplicationContext()) || test == true) {
            //if (flag == 1) {
            btntOn.setImageResource(R.drawable.enable);
            keyone = 1;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("enable", "yes");
            editor.apply();
//            } else {
//                btntOn.setImageResource(R.drawable.disable);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("Disable", "no");
//                editor.apply();
//            }

//        } else if() {
//            btntOn.setImageResource(R.drawable.disable);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("Disable", "no");
//            editor.apply();
        }
//
//
        btntOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
////                Toast.makeText(MainActivity.this,"Wifi is ON"  ,Toast.LENGTH_SHORT).show();
////                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
////                wifi.setWifiEnabled(true);

                if (keyone == 0) {
                    keyone = 1;
                    if (android.os.Build.VERSION.SDK_INT == 25) {

                        ToastCompat.makeText(context, "Wifi is ON", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Wifi is ON", Toast.LENGTH_SHORT).show();

                    }
                    WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);
                    btntOn.setImageResource(R.drawable.enable);
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
                    btntOn.setImageResource(R.drawable.disable);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("enable", "no");
                    editor.apply();
                }
                ison = !ison;
            }
        });
//
//               /*list = getListView();
//                registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//                mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                wifiReciever = new WifiScanReceiver();
//                mainWifiObj.startScan();
//                list.setVisibility(View.VISIBLE);
//
//                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                        String ssid = ((TextView) view).getText().toString();
//                        connectToWifi(ssid);
//
//                        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                        Toast.makeText(MainActivity.this, "Wifi SSID : " + ssid, Toast.LENGTH_SHORT).show();
//                    }
//                });*/
//            }
//        });
//        // btntOff.setOnClickListener(new View.OnClickListener() {
//        //    @Override
//        //public void onClick(View v) {
//        //       Toast.makeText(MainActivity.this,"Wifi is OFF",Toast.LENGTH_SHORT).show();
//        //     WifiManager wmgr = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        //   wmgr.setWifiEnabled(false);
////                list.setVisibility(View.INVISIBLE);
//        // }
//        //});
//
//
//
        btnscanwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(interstitial != null) {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                    } else {

                        if (checkPermission()) {

                            startActivity(new Intent(MainActivity.this, WifiScanActivity.class));
                        } else {

                            requestPermission();
                            // onrequestPermission();
//                        Toast.makeText(MainActivity.this, "Allow location permission to show available wifi", Toast.LENGTH_LONG).show();

                        }


//
//                    if((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager
//                            .PERMISSION_GRANTED))
//                    {
//                        startActivity(new Intent(MainActivity.this, WifiScanActivity.class));
//                    }
//                    else if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
//                            .PERMISSION_GRANTED)) {
//                        Toast.makeText(MainActivity.this, "Allow location permission to show available wifi", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package", getPackageName(), null);
//                        intent.setData(uri);
//                        startActivityForResult(intent, 4);
//                    }
                    }
                    interstitial.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            requestNewInterstitial();


                            if (checkPermission()) {
//                            bp.purchase(MainActivity.this, "android.test.purchased");
                                startActivity(new Intent(MainActivity.this, WifiScanActivity.class));
                            } else {

                                requestPermission();
                                // onrequestPermission();
//                            Toast.makeText(MainActivity.this, "Allow location permission to show available wifi", Toast.LENGTH_LONG).show();
                            }


//                        if((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager
//                                .PERMISSION_GRANTED))
//                        {
//                            startActivity(new Intent(MainActivity.this, WifiScanActivity.class));
//                        }
//                        else if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
//                                .PERMISSION_GRANTED)) {
//                            Toast.makeText(MainActivity.this, "Allow location permission to show available wifi", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", getPackageName(), null);
//                            intent.setData(uri);
//                            startActivityForResult(intent, 4);
//                        }

                        }
                    });
                }else{


                    if (checkPermission()) {

                        startActivity(new Intent(MainActivity.this, WifiScanActivity.class));
                    } else {

                        requestPermission();
                        // onrequestPermission();
//                        Toast.makeText(MainActivity.this, "Allow location permission to show available wifi", Toast.LENGTH_LONG).show();

                    }

                }
            }
        });

        btnshowpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (interstitial != null) {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                    } else {
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
                }else{

                    startActivity(new Intent(MainActivity.this, ListDataActivity.class));


                }
            }
        });


        btnhotspot.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if (!InAppPrefManager.getInstance(getApplicationContext()).getInAppStatus()) {
                    requestNewInterstitial();
                }
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        btnsetting.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_MAIN, null);
//                intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
//                intent.setComponent(cn);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        btnspeedtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (interstitial != null) {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                    } else {
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

                }else{

                    Intent i = new Intent(MainActivity.this, Speedtest.class);
                    startActivity(i);

                }
            }
        });

        btngenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (interstitial != null) {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                    } else {
                        Intent i = new Intent(MainActivity.this, GenerateActivity.class);
                        startActivity(i);
                    }
                    interstitial.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            requestNewInterstitial();

                            Intent i = new Intent(MainActivity.this, GenerateActivity.class);
                            startActivity(i);
                        }
                    });
                }else{

                    Intent i = new Intent(MainActivity.this, GenerateActivity.class);
                    startActivity(i);

                }
            }
        });


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

//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            // for each permission check if the user granted/denied them
//            // you may want to group the rationale in a single dialog,
//            // this is just an example
//            for (int i = 0, len = permissions.length; i < len; i++) {
//                String permission = permissions[i];
//                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                    // user rejected the permission
//                    boolean showRationale = shouldShowRequestPermissionRationale( permission );
//                    if (! showRationale) {
//                        // user also CHECKED "never ask again"
//                        // you can either enable some fall back,
//                        // disable features of your app
//                        // or open another dialog explaining
//                        // again the permission and directing to
//                        // the app setting
//                    } else if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission)) {
////                        showRationale(permission, R.string.permission_denied_location);
//
//                        Toast.makeText(activity, "Need Location permission", Toast.LENGTH_SHORT).show();
//                        // user did NOT check "never ask again"
//                        // this is a good place to explain the user
//                        // why you need the permission and ask if he wants
//                        // to accept it (the rationale)
//                    }
//                }
//            }
//        }

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
    public void changeLang(View view){
        Intent intent = new Intent(MainActivity.this,TermsandConditionActivity.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_android_alert_dialog, null);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);

        // Get the custom alert dialog view widgets reference
        Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
        Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);


        // Create the alert dialog
        final AlertDialog dialog = builder.create();

        // Set positive/yes button click listener
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate_us(v);
                dialog.dismiss();
//                finish();
            }
        });

        // Set negative/no button click listener
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishAffinity();
                // Dismiss the alert dialog
//                Intent myIntent = new Intent(Intent.ACTION_SEND);
//                PackageManager pm = getPackageManager();
//                Intent tempp = new Intent(Intent.ACTION_SEND);
//                tempp.setType("*/*");
//
//                List<ResolveInfo> resInfo = pm.queryIntentActivities(tempp, 0);
//                for (int i = 0; i < resInfo.size(); i++) {
//                    ResolveInfo ri = resInfo.get(i);
//                    if (ri.activityInfo.packageName.contains("android.gm")) {
//                        myIntent.setComponent(new ComponentName(ri.activityInfo.packageName, ri.activityInfo.name));
//                        myIntent.setAction(Intent.ACTION_SEND);
//                        myIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"gloriajeans121@gmail.com"});
//                        myIntent.setType("message/rfc822");
//                        myIntent.putExtra(Intent.EXTRA_SUBJECT, "Feed Back! How can we improve it!");
//                    }
//                }
//                startActivity(myIntent);
            }
        });

        //Neutral Button


        // Display the custom alert dialog on interface
        dialog.show();
    }

    public void rate_us(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.internet.speed.test.analyzer.wifi.key.generator.app")));
    }


    @Override
    public void onBackPressed() {
        exitt();
    }



    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

       // inAppAlertDiaolBox();

        InAppPrefManager.getInstance(getApplicationContext()).setInAppStatus(true);

        if (interstitial != null){
            interstitial = null;
        }
        if (nativeAd != null){
            nativeAd.destroy();
        }
        frameLayout.setVisibility(View.INVISIBLE);

        //banner.setVisibility(View.INVISIBLE);
        //populateUnifiedNativeAdView(null,null);

    }

    @Override
    public void onPurchaseHistoryRestored() {

        if(bp.isPurchased(getString(R.string.test_product_id))){

            InAppPrefManager.getInstance(getApplicationContext()).setInAppStatus(true);
        }

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

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
                      /*  deleteCache(getApplicationContext());
                        deleteAppData();
                        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
                        startActivity(i);*/
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    Menu menu;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);


        this.menu = menu;

        if (!InAppPrefManager.getInstance(getApplicationContext()).getInAppStatus()) {

            menu.findItem(R.id.noads).setVisible(true);
        }else{

            menu.findItem(R.id.noads).setVisible(false);
        }



        if (mShouldAnimateMenuItem){
            ImageView image = new ImageView(this);
            image.setImageResource(R.drawable.no_ads);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //  String str = getString(R.string.test_product_id);
                    bp.purchase(MainActivity.this, getString(R.string.test_product_id));
                    // Toast.makeText(getApplicationContext(), "in app active",Toast.LENGTH_LONG).show();

                }
            });
            menu.getItem(0).setActionView(image); //item in the 0 position

            // anim.setDuration(700000);
            Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
            animation.setDuration(200); //1 second duration for each animation cycle
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
            animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
            menu.getItem(0).getActionView().startAnimation(animation);
            // menu.getItem(1).getActionView().startAnimation(anim);
            mShouldAnimateMenuItem = false;

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.noads:{

                // Toast.makeText(getApplicationContext(), "in app active",Toast.LENGTH_LONG).show();
                // bp.purchase(MainActivity.this, "android.test.purchased");

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        //  return super.onOptionsItemSelected(item);
    }








}


