package com.internet.speed.test.analyzer.wifi.key.generator.app.Helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocalHelper {

    public static final String SELECTED_LANGUAGE = "Local.Helper.Selected.Language";
    public static SharedPreferences.Editor editor;

    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setlocale(context, lang);
    }

    public static Context onAttach(Context context, String DefaultLanguage) {
        String lang = getPersistedData(context, DefaultLanguage);
        return setlocale(context, lang);
    }

    public static Context setlocale(Context context, String lang) {
        persist(context, lang);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return UpdateResource(context, lang);

        return updateResourceLegacy(context, lang);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context UpdateResource(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourceLegacy(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    public static void persist(Context context, String lang) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
        editor.putString(SELECTED_LANGUAGE, lang);
        editor.apply();
    }

    private static String getPersistedData(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, language);
    }
}
