package com.internet.speed.test.analyzer.wifi.key.generator.app.appsNetBlocker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Apps_Model_Class implements Comparable<Apps_Model_Class> {
    public PackageInfo info;
    public String name;
    public boolean system;
    public boolean disabled;
    public boolean wifi_blocked;
    public boolean mobileData_blocked;
    public boolean changed;
    public long received , transfer;

    private Apps_Model_Class(PackageInfo info, boolean wifi_blocked, boolean mobileData_blocked, boolean changed, Context context) {
        PackageManager pm = context.getPackageManager();
        this.info = info;
        this.name = info.applicationInfo.loadLabel(pm).toString();
        this.system = ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        int setting = pm.getApplicationEnabledSetting(info.packageName);
        if (setting == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
            this.disabled = !info.applicationInfo.enabled;
        else
            this.disabled = (setting != PackageManager.COMPONENT_ENABLED_STATE_ENABLED);

        this.wifi_blocked = wifi_blocked;
        this.mobileData_blocked = mobileData_blocked;
        this.changed = changed;
    }

    public static List<Apps_Model_Class> getListOfApps(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences wifi = context.getSharedPreferences("wifi", Context.MODE_PRIVATE);
        SharedPreferences other = context.getSharedPreferences("other", Context.MODE_PRIVATE);

        boolean wlWifi = prefs.getBoolean("whitelist_wifi", false);
        boolean wlOther = prefs.getBoolean("whitelist_other", false);

        List<Apps_Model_Class> listRules = new ArrayList<>();


        for (PackageInfo info : context.getPackageManager().getInstalledPackages(0)) {
            if(!isSystemPackage(info)){

                boolean blWifi = wifi.getBoolean(info.packageName, wlWifi);
                boolean blOther = other.getBoolean(info.packageName, wlOther);
                boolean changed = (blWifi != wlWifi || blOther != wlOther);


                listRules.add(new Apps_Model_Class(info, blWifi, blOther, changed, context));

            }
        }

        Collections.sort(listRules);

        return listRules;
    }
    static boolean isSystemPackage(PackageInfo pkgInfo){
        if((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)!=0)
            return false;
        else if((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)!=0)
            return true;
        else if((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_INSTALLED)!=0)
            return false;
        else
            return true;

    }
    public Drawable getIcon(Context context) {
        return info.applicationInfo.loadIcon(context.getPackageManager());

    }

    @Override
    public int compareTo(Apps_Model_Class other) {
        if (changed == other.changed) {
            int i = name.compareToIgnoreCase(other.name);
            return (i == 0 ? info.packageName.compareTo(other.info.packageName) : i);
        }
        return (changed ? -1 : 1);
    }
}
