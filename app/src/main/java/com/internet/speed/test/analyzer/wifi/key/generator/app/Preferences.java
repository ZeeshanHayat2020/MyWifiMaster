package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.content.Context;
import android.content.SharedPreferences;


public class Preferences {
    Context context;
    SharedPreferences _prefes;
    SharedPreferences.Editor editor;
    public String LANG_SELECT = "LANG_SELECT";
    public String LANG_VALUE = "lang";
    private static final String PREF_NAME = "LANGUAGE";

    private static final String PREF_NAME2 = "privacyPolicy";
    private final SharedPreferences.Editor editor2;
    private static final String IS_Accepted = "isAccepted";
    private final SharedPreferences pref2;


    public Preferences(Context _context) {
        this.context = _context;
        int PRIVATE_MODE = 0;
        _prefes = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = _prefes.edit();

        pref2 = context.getSharedPreferences(PREF_NAME2, PRIVATE_MODE);
        editor2 = pref2.edit();
    }

    public void SetValue(String name, boolean value) {
        editor.putBoolean(name, value);
        editor.commit();
        editor.apply();
    }
    public void SetValueStringLang(String name, String value) {
        editor.putString(name, value);
        editor.commit();
        editor.apply();
    }

    public boolean GetValue(String name) {

        return _prefes.getBoolean(name, false);
    }

    public String GetValueStringlang(String name) {

        return _prefes.getString(name, "en");
    }
}
