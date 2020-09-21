package com.internet.speed.test.analyzer.wifi.key.generator.app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Locale;

import static android.os.Build.VERSION.SDK_INT;

public class SplashScreenActivity extends AppCompatActivity {
    private InterstitialAd interstitialAd;
    private final Handler mHandler = new Handler();
    private PrefManager prefManager;
    ProgressBar progressBar;
    private BillingProcessor bp;

    private boolean ispurchsed, once, isOutSideOfApp;
    int loadattempts;
    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    private void setLocale(Locale locale){
        // optional - Helper method to save the selected language to SharedPreferences in case you might need to attach to activity context (you will need to code this)
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(locale);
        } else{
            configuration.locale=locale;
        }
        if (SDK_INT > Build.VERSION_CODES.N){
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration,displayMetrics);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preferences preferences = new Preferences(this);
        Locale locale = new Locale(preferences.GetValueStringlang(preferences.LANG_VALUE));
        setLocale(locale);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.ProgressBar);
        new DelayTask().execute();
        if (isNetworkConnected()) {
            loadInterstitial();
        } else {
            splashWork();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    void splashWork() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                somework();
            }
        }, 100);
    }

    void somework() {
        finish();
        Intent mainIntent = new Intent(SplashScreenActivity.this, SelectCountry.class);
        startActivity(mainIntent);
    }
    //============================================In App Purchase=====================================================

    void loadInterstitial() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.Interstitial));
        AdRequest adRequestInter = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequestInter);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                loadattempts++;
                if (loadattempts > 2 && !once) {
                    once = true;
                    somework();
                } else {
                    loadInterstitial();

                }
                super.onAdFailedToLoad(i);
            }


            @Override
            public void onAdLoaded() {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    somework();
                }
                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
                somework();
            }

            @Override
            public void onAdLeftApplication() {
                isOutSideOfApp = true;
                super.onAdLeftApplication();
            }
        });


    }


    @SuppressLint("StaticFieldLeak")
    public class DelayTask extends AsyncTask<Void, Integer, String> {
        int count = 0;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            while (count < 5) {
                SystemClock.sleep(1000);
                count++;
                publishProgress(count * 20);
            }
            return "Complete";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
    }

}
