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
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

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

public class PasswordGeneratorActivity extends ActivityBase implements View.OnClickListener {

    CardView cardViewUpperCase, cardViewLowerCase, cardViewNumber, cardViewSymbols;
    SwitchCompat chkUpperCase, chkLowerCase, chkNumber, chkSymbols;
    Button btnIncrement, btnDecrement, btnGenerate;
    ImageView btnCopy;
    EditText etLength;
    TextView tvPassword;
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";

    private RelativeLayout layoutHeader;
    public ImageView headerItemCenterRight;
    public TextView headerItemTextViewFirst;
    public TextView headerItemTextViewSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this, R.color.colorWhite, R.color.white);
        setContentView(R.layout.activity_password_generator);
        reqNewInterstitial(this);
        if (haveNetworkConnection()) {
            requestBanner((FrameLayout) findViewById(R.id.bannerContainer));
        }
        setUpHeader();
        initialWork();
        Listeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
        reqNewInterstitial(this);
    }

    void setUpHeader() {
        layoutHeader = findViewById(R.id.header_acLanugage);
        headerItemCenterRight = findViewById(R.id.header_item_centerRight_imageView);
        headerItemTextViewFirst = findViewById(R.id.header_item_textView_First);
        headerItemTextViewSecond = findViewById(R.id.header_item_textView_Second);
        headerItemCenterRight.setImageResource(R.drawable.ic_header_item_generate_password);
        headerItemTextViewFirst.setText(getResources().getString(R.string.WIFI));
        headerItemTextViewSecond.setText(R.string.PASS_GENERATE);


    }


    private void initialWork() {


        cardViewUpperCase = findViewById(R.id.cardUpperCase);
        cardViewLowerCase = findViewById(R.id.cardLowerCase);
        cardViewNumber = findViewById(R.id.cardNumber);
        cardViewSymbols = findViewById(R.id.cardSymbols);
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

        cardViewUpperCase.setOnClickListener(this);
        cardViewLowerCase.setOnClickListener(this);
        cardViewNumber.setOnClickListener(this);
        cardViewSymbols.setOnClickListener(this);
    }

    private void setChkBxUpperCase() {

        if (!chkUpperCase.isChecked()) {
            chkUpperCase.setChecked(true);
        } else {
            chkUpperCase.setChecked(false);
        }
    }

    private void setChkBxLowerCase() {

        if (!chkLowerCase.isChecked()) {
            chkLowerCase.setChecked(true);
        } else {
            chkLowerCase.setChecked(false);
        }
    }

    private void setChkBxNumbers() {

        if (!chkNumber.isChecked()) {
            chkNumber.setChecked(true);
        } else {
            chkNumber.setChecked(false);
        }
    }

    private void setChkBxSymbols() {

        if (!chkSymbols.isChecked()) {
            chkSymbols.setChecked(true);
        } else {
            chkSymbols.setChecked(false);
        }
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
            case R.id.cardUpperCase: {
                setChkBxUpperCase();
            }
            break;
            case R.id.cardLowerCase: {
                setChkBxLowerCase();
            }
            break;
            case R.id.cardNumber: {
                setChkBxNumbers();
            }
            break;
            case R.id.cardSymbols: {
                setChkBxSymbols();
            }
            break;
        }
    }

    private void incrementFunc() {
        int current = Integer.parseInt(etLength.getText().toString()) + 1;
        if (current < 25) {
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
    }


}