package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.database.MyPreferences;

public class ActivitySplash extends ActivityBase {

    private Handler handler;
    private Runnable runnable;
    private int loadAttempts;
    private MyPreferences myPreferences;
    private boolean isAppLeft = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setStatusBarGradient(this, R.color.colorWhite, R.color.colorWhite);
        myPreferences = new MyPreferences(this);
        reqNewInterstitial(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (haveNetworkConnection()) {
            loadInterstitial();
        } else {
            launchWithDelay();
        }
    }

    private void launchWithDelay() {
        runnable = new Runnable() {
            @Override
            public void run() {
                launchLanguageActivity();
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }

    private void launchLanguageActivity() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final Intent intent;
                if (!myPreferences.isLanguageSelected()) {
                    intent = new Intent(ActivitySplash.this, ActivityLanguage.class);
                } else {
                    if (myPreferences.isFirstTimeLaunch()) {
                        intent = new Intent(ActivitySplash.this, ActivityIntroSLides.class);
                    } else {
                        if (myPreferences.isPrivacyPolicyAccepted()) {
                            intent = new Intent(ActivitySplash.this, MainActivity.class);
                        } else {
                            intent = new Intent(ActivitySplash.this, ActivityPrivacyPolicy.class);
                        }
                    }
                }
                startActivity(intent);
                finish();

                return null;
            }
        }.execute();

    }


    void loadInterstitial() {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded() && !isAppLeft) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mInterstitialAd.show();
                        }
                    }, 2000);

                } else {
                    launchLanguageActivity();
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                loadAttempts++;
                if (loadAttempts >= 1) {
                    loadAttempts = 0;
                    launchLanguageActivity();
                } else {
                    reqNewInterstitial(ActivitySplash.this);
                    loadInterstitial();
                }
                super.onAdFailedToLoad(i);
            }


            @Override
            public void onAdClosed() {
                launchLanguageActivity();
            }


            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                isAppLeft = true;

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAppLeft = true;
    }
}