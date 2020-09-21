package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.internet.speed.test.analyzer.wifi.key.generator.app.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PasswordGeneratorActivity extends AppCompatActivity implements View.OnClickListener {

    Switch chkUpperCase, chkLowerCase, chkNumber, chkSymbols;
    Button btnIncrement, btnDecrement, btnGenerate;
    ImageView btnCopy;
    EditText etLength;
    TextView tvPassword;
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";
    FrameLayout adContainerView;
    public InterstitialAd mInterstitialAd;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_generator);

        initialWork();
        Listeners();
        loadBanner();
        reqNewInterstitial();

    }

    private void initialWork() {


        chkUpperCase = findViewById(R.id.chkUpperCase);
        chkLowerCase = findViewById(R.id.chkLowerCase);
        chkNumber = findViewById(R.id.chkNumber);
        chkSymbols = findViewById(R.id.chkSymbols);
        btnIncrement = findViewById(R.id.btnIncrement);
        btnDecrement = findViewById(R.id.btnDecrement);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnCopy = findViewById(R.id.btnCopy);
        etLength = findViewById(R.id.etLength);
        tvPassword = findViewById(R.id.tvPassword);

        etLength.setText("8");
    }

    private void Listeners() {
        btnIncrement.setOnClickListener(this);
        btnDecrement.setOnClickListener(this);
        btnGenerate.setOnClickListener(this);
        btnCopy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnIncrement:
                incrementFunc();
                break;
            case R.id.btnDecrement:
                decrementFunc();
                break;
            case R.id.btnGenerate:
                generateFunc();
                break;
            case R.id.btnCopy:
                copyFunc();
                break;
        }
    }

    private void incrementFunc() {
        int current = Integer.parseInt(etLength.getText().toString()) + 1;
        if (current < 31) {
            etLength.setText("" + current);
        }
    }

    private void decrementFunc() {
        int current = Integer.parseInt(etLength.getText().toString());
        if (current > 8) {
            int newVal = current - 1;
            etLength.setText("" + newVal);
        }
    }

    private void generateFunc() {
        int length = Integer.parseInt(etLength.getText().toString());
        StringBuilder password = new StringBuilder(length);
        Random random = new Random(System.nanoTime());

        List<String> charCategories = new ArrayList<>(4);
        if (chkLowerCase.isChecked()) {
            charCategories.add(LOWER);
        }
        if (chkUpperCase.isChecked()) {
            charCategories.add(UPPER);
        }
        if (chkNumber.isChecked()) {
            charCategories.add(DIGITS);
        }
        if (chkSymbols.isChecked()) {
            charCategories.add(PUNCTUATION);
        }

        if (chkLowerCase.isChecked() || chkUpperCase.isChecked() || chkNumber.isChecked() || chkSymbols.isChecked()) {
            if (length < 50) {
                for (int i = 0; i < length; i++) {
                    String charCategory = charCategories.get(random.nextInt(charCategories.size()));
                    int position = random.nextInt(charCategory.length());
                    password.append(charCategory.charAt(position));
                }
                tvPassword.setText(password);
            } else {
                Toast.makeText(this, "password length is to long..", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select atleast one Group..!", Toast.LENGTH_SHORT).show();
        }


    }

    private void copyFunc() {
        String pass = tvPassword.getText().toString();
        if (!pass.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Password", pass);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Password copied..!", Toast.LENGTH_SHORT).show();
        }

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();

        } else {
            reqNewInterstitial();

        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reqNewInterstitial();
            }
        });
    }

    public void loadBanner() {
        // Create an ad request.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("139DE8BC25455F67E5571129931C56ED")).build());
        adContainerView = findViewById(R.id.acPassGenerator_bannerContainer);
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                adView = new AdView(PasswordGeneratorActivity.this);
                adView.setAdUnitId(getString(R.string.Banner));
                adContainerView.removeAllViews();
                adContainerView.addView(adView);
                AdSize adSize = getAdSize();
                adView.setAdSize(adSize);
                AdRequest adRequest = new AdRequest.Builder().build();
                // Start loading the ad in the background.
                adView.loadAd(adRequest);
            }
        });
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = outMetrics.density;
        float adWidthPixels = adContainerView.getWidth();
        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationBannerAdSizeWithWidth(this, adWidth);
    }

    public void reqNewInterstitial() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.Interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

}