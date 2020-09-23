package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.LanguageManager;
import com.internet.speed.test.analyzer.wifi.key.generator.app.database.MyPreferences;

public class ActivityLanguage extends ActivityBase implements View.OnClickListener {
    private Button bntEng, btnArabic, btnChina,
            btnFrench, btnGerman, btnIndonesian, btnItalian,
            btnPolish, btnPortuguese, btnRussian, btnSpanish;
    private MyPreferences myPreferences;
    private RelativeLayout layoutHeader;
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
        setContentView(R.layout.activity_language);
        myPreferences = new MyPreferences(this);
        iniViews();
        setUpHeader();
    }

    private void iniViews() {
        bntEng = (Button) findViewById(R.id.acLanguage_btnEnglish);
        btnArabic = (Button) findViewById(R.id.acLanguage_btnArabic);
        btnChina = (Button) findViewById(R.id.acLanguage_btnChina);
        btnFrench = (Button) findViewById(R.id.acLanguage_btnFrance);
        btnGerman = (Button) findViewById(R.id.acLanguage_btnGerman);
        btnIndonesian = (Button) findViewById(R.id.acLanguage_btnIndonesian);
        btnItalian = (Button) findViewById(R.id.acLanguage_btnItalian);
        btnPolish = (Button) findViewById(R.id.acLanguage_btnPolish);
        btnPortuguese = (Button) findViewById(R.id.acLanguage_btnPortuguese);
        btnRussian = (Button) findViewById(R.id.acLanguage_btnRussian);
        btnSpanish = (Button) findViewById(R.id.acLanguage_btnSpanish);


        bntEng.setOnClickListener(this);
        btnArabic.setOnClickListener(this);
        btnFrench.setOnClickListener(this);
        btnChina.setOnClickListener(this);

        btnArabic.setOnClickListener(this);
        btnGerman.setOnClickListener(this);
        btnGerman.setOnClickListener(this);
        btnIndonesian.setOnClickListener(this);
        btnItalian.setOnClickListener(this);
        btnPolish.setOnClickListener(this);
        btnPortuguese.setOnClickListener(this);
        btnRussian.setOnClickListener(this);
        btnSpanish.setOnClickListener(this);

    }


    void setUpHeader() {
        layoutHeader = findViewById(R.id.header_acLanugage);
        headerItemMenu = findViewById(R.id.header_item_menu_imageView);
        headerItemCenterLeft = findViewById(R.id.header_item_centerLeft_imageView);
        headerItemCenterRight = findViewById(R.id.header_item_centerRight_imageView);
        headerItemBottomLeft = findViewById(R.id.header_item_bottomLeft_imageView);
        headerItemBottomRigth = findViewById(R.id.header_item_bottomRigth_imageView);
        headerItemTextViewFirst = findViewById(R.id.header_item_textView_First);
        headerItemTextViewSecond = findViewById(R.id.header_item_textView_Second);

        headerItemMenu.setVisibility(View.INVISIBLE);
        headerItemCenterLeft.setVisibility(View.INVISIBLE);
        headerItemBottomLeft.setVisibility(View.INVISIBLE);
        headerItemBottomRigth.setVisibility(View.INVISIBLE);

        headerItemCenterRight.setImageResource(R.drawable.ic_language);
        headerItemTextViewFirst.setText("LANGUAGE");
        headerItemTextViewSecond.setText("SETTING");


    }

    private void setLanguage(boolean isSelected) {
        myPreferences.setLanguageSelected(isSelected);
        startIntroSlidesActivity();
    }


    private void setNewLocal(String languageKey) {
        LanguageManager.setNewLocale(this, languageKey);
    }

    private void startIntroSlidesActivity() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Intent intent;
                if (myPreferences.isFirstTimeLaunch()) {
                    intent = new Intent(ActivityLanguage.this, ActivityIntroSLides.class);
                } else {
                    if (myPreferences.isPrivacyPolicyAccepted()) {
                        intent = new Intent(ActivityLanguage.this, MainActivity.class);
                    } else {
                        intent = new Intent(ActivityLanguage.this, ActivityPrivacyPolicy.class);
                    }
                }
                startActivity(intent);
                finish();
                return null;
            }
        }.execute();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.acLanguage_btnEnglish: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_ENGLISH);
            }
            break;
            case R.id.acLanguage_btnArabic: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_ARABIC);
            }
            break;
            case R.id.acLanguage_btnChina: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_CHINES);
            }
            break;
            case R.id.acLanguage_btnFrance: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_FRENCH);
            }
            break;
            case R.id.acLanguage_btnGerman: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_GERMAN);
            }
            break;
            case R.id.acLanguage_btnIndonesian: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_INDONESIAN);
            }
            break;
            case R.id.acLanguage_btnItalian: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_ITALIAN);
            }
            break;
            case R.id.acLanguage_btnPolish: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_POLISH);
            }
            break;
            case R.id.acLanguage_btnPortuguese: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_PORTUGUESE);
            }
            break;
            case R.id.acLanguage_btnRussian: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_RUSSIA);
            }
            break;

            case R.id.acLanguage_btnSpanish: {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_SPANISH);
            }
            break;
        }
    }
}