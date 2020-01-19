package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.app.Application;
import android.content.Context;

import com.internet.speed.test.analyzer.wifi.key.generator.app.Helper.LocalHelper;

public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalHelper.onAttach(base,"en"));
    }
}
