package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.internet.speed.test.analyzer.wifi.key.generator.app.Helper.LocalHelper;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import io.paperdb.Paper;

@SuppressLint("Registered")
public class SelectCountry extends AppCompatActivity implements View.OnClickListener {

    Button Start;
    ImageView Turkish, Russian, Filipino, Indo, Vietnamese, english;
    SharedPreferences prefFirsTime;
    Preferences preferences;
    final String PREFS_NAME = "enter";
    String languageToLoad;
    PrefManager prefManager;
    RecyclerView language_rcv;
    boolean check;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // this.settings = getSharedPreferences("enter", 0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        preferences = new Preferences(this);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.languageToLoad = this.preferences.GetValueStringlang(preferences.LANG_VALUE);
        Locale locale = new Locale(this.languageToLoad);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.selecte_country);
        prefManager = new PrefManager(this);

        if (prefManager.isAcceptedLanguage()) {
            startActivity(new Intent(SelectCountry.this, TermsandConditionActivity.class));
            finish();
        }
        init();
        Turkish.setOnClickListener(this::onClick);
        Russian.setOnClickListener(this::onClick);
        english.setOnClickListener(this::onClick);
        Indo.setOnClickListener(this::onClick);
        Vietnamese.setOnClickListener(this::onClick);
        Filipino.setOnClickListener(this::onClick);

    }

    private void init() {
        Start = findViewById(R.id.start);
        Turkish = findViewById(R.id.turkish);
        Russian = findViewById(R.id.russian);
        Filipino = findViewById(R.id.filpino);
        Indo = findViewById(R.id.indo);
        Vietnamese = findViewById(R.id.vietnamese);
        english = findViewById(R.id.english);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.turkish:
                onTurkishLanguageSelected();
                break;
            case R.id.indo:
                onIndonesianLanguageSelected();
                break;
            case R.id.russian:
                onRussiaLanguageSelected();
                break;
            case R.id.vietnamese:
                onVietnameseLanguageSelected();
                break;
            case R.id.filpino:
                onFilipinoLanguageSelected();
                break;
            case R.id.english:
                onAmericaLanguageSelected();
                break;
        }
    }

    public void firstimepref() {
        SharedPreferences.Editor editor = prefFirsTime.edit();
        editor.putString("firsttimedisp", "yess");
        editor.apply();
    }

    public void onFilipinoLanguageSelected() {
        // this.settings.edit().putString(preferences.LANG_VALUE, "en").apply();
        this.preferences.SetValueStringLang(preferences.LANG_VALUE, "fil");
        Locale locale = new Locale("b+fi");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        recreate();
        startMap();
    }

    public void onIndonesianLanguageSelected() {
        // this.settings.edit().putString(preferences.LANG_VALUE, "en").apply();
        this.preferences.SetValueStringLang(preferences.LANG_VALUE, "in");
        Locale locale = new Locale("in");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        recreate();
        startMap();
    }

    public void onTurkishLanguageSelected() {
        // this.settings.edit().putString(preferences.LANG_VALUE, "en").apply();
        this.preferences.SetValueStringLang(preferences.LANG_VALUE, "tr");
        Locale locale = new Locale("tr");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        recreate();
        startMap();
    }

    public void onVietnameseLanguageSelected() {
        // this.settings.edit().putString(preferences.LANG_VALUE, "en").apply();
        this.preferences.SetValueStringLang(preferences.LANG_VALUE, "vi");
        Locale locale = new Locale("vi");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        recreate();
        startMap();
    }

    public void onAmericaLanguageSelected() {
        // this.settings.edit().putString(preferences.LANG_VALUE, "en").apply();
        this.preferences.SetValueStringLang(preferences.LANG_VALUE, "en");
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        recreate();
        startMap();
    }

    public void onRussiaLanguageSelected() {
        //this.settings.edit().putString(preferences.LANG_VALUE, "ru").apply();
        this.preferences.SetValueStringLang(preferences.LANG_VALUE, "ru");
        Locale locale = new Locale("ru");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        recreate();
        startMap();
    }


    private void startMap() {
        if (preferences.GetValue(preferences.LANG_SELECT)) {
            prefManager.setAcceptLanguage(true);
            startActivity(new Intent(this, TermsandConditionActivity.class));
            finish();

        } else {


            preferences.SetValue(preferences.LANG_SELECT, true);
            prefManager.setAcceptLanguage(true);
            startActivity(new Intent(SelectCountry.this, TermsandConditionActivity.class));
            finish();

        }
    }

}
