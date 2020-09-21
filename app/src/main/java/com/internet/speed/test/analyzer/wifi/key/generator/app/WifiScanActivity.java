package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.drakeet.support.toast.ToastCompat;

import static android.os.Build.VERSION.SDK_INT;

public class WifiScanActivity extends AppCompatActivity {

    ListView listView;
    WifiManager wifiManager;
    String wifis[];
    EditText pass;
    Toolbar toolbar;
    AdView adView;
    DatabaseHelper databaseHelper;

    public static String removeChar(String s, char c) {
        StringBuffer r = new StringBuffer(s.length());
        r.setLength(s.length());
        int current = 0;
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur != c) r.setCharAt(current++, cur);
        }
        return r.toString();
    }
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
        setContentView(R.layout.activity_wifi_scan);
        MobileAds.initialize(this, getResources().getString(R.string.app_id));
        listView = findViewById(R.id.list_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHelper = new DatabaseHelper(this);
        adView = findViewById(R.id.banner_ad);

        if (!InAppPrefManager.getInstance(WifiScanActivity.this).getInAppStatus()) {
            adview();
        }
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        List<ScanResult> wifiScanList = wifiManager.getScanResults();
        wifis = new String[wifiScanList.size()];
        for (int i = 0; i < wifiScanList.size(); i++) {
            wifis[i] = ((wifiScanList.get(i)).toString());
        }
        String filtered[] = new String[wifiScanList.size()];
        int counter = 0;
        for (String eachWifi : wifis) {
            String[] temp = eachWifi.split(",");

            filtered[counter] = temp[0].substring(5).trim();//+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength

            counter++;

        }

        // listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, filtered));
        listView.setAdapter(new LogAdapter1(getApplicationContext(), filtered));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // selected item
                //  String ssid = ((TextView) view).getText().toString();
                String ssid = (String) listView.getItemAtPosition(position);


                if (databaseHelper.Checkname(ssid)) {
                    //Toast.makeText(getApplicationContext(),"Store",Toast.LENGTH_SHORT).show();
                    connectToWifi2(ssid);
                } else {
                    connectToWifi(ssid);
                }
            }
        });
    }

    public void adview() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("C6D2162C49AA13B1C9432BB82D1868A5").build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int error) {
                adView.setVisibility(View.GONE);
            }

        });
    }

    private void connectToWifi(final String wifiSSID) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.connectwifi);
        dialog.setTitle("Connect to Network");
        TextView textSSID = (TextView) dialog.findViewById(R.id.textSSID1);

        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        pass = (EditText) dialog.findViewById(R.id.textPassword);
        textSSID.setText(wifiSSID);

        // if button is clicked, connect to the network;
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String checkPassword = pass.getText().toString();
                if (TextUtils.isEmpty(checkPassword)) {

                } else {
                    finallyConnect(checkPassword, wifiSSID);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void connectToWifi2(final String wifiSSID) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.connectwifi);
        dialog.setTitle("Connect to Network");
        TextView textSSID =  dialog.findViewById(R.id.textSSID1);

        Button dialogButton =  dialog.findViewById(R.id.okButton);
        pass =  dialog.findViewById(R.id.textPassword);
        textSSID.setText(wifiSSID);
        String jinn = databaseHelper.getpwd(wifiSSID);

        pass.setText(jinn);

        // if button is clicked, connect to the network;
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String checkPassword = pass.getText().toString();
                if (TextUtils.isEmpty(checkPassword)) {

                } else {
                    finallyConnect2(checkPassword, wifiSSID);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void finallyConnect(final String networkPass, final String networkSSID) {
        if (databaseHelper.getSSId().equals(networkSSID)) {

            if (android.os.Build.VERSION.SDK_INT == 25) {

                ToastCompat.makeText(this, "Network already added.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Network already added.", Toast.LENGTH_SHORT).show();
            }

        } else {
            WifiConfiguration wifiConfig = new WifiConfiguration();

            wifiConfig.SSID = String.format("\"%s\"", networkSSID);
            wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

            int netId = wifiManager.addNetwork(wifiConfig);

//            if (netId > 0) {

            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);

            wifiManager.reconnect();

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"\"" + networkSSID + "\"\"";
            conf.preSharedKey = "\"" + networkPass + "\"";
            wifiManager.addNetwork(conf);


            final ProgressDialog dialog = ProgressDialog.show(WifiScanActivity.this, "",
                    "Authenticating. Please wait...", true);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    checkWifi(networkSSID, networkPass);
                    dialog.dismiss();

                }
            }, 12000);


//            } else {
//                Toast.makeText(this, "wrong password\n" +
//                        "Can't connect to wifi", Toast.LENGTH_SHORT).show();
//            }

        }
    }

    private void finallyConnect2(final String networkPass, final String networkSSID) {
        if (databaseHelper.getSSId().equals(networkSSID)) {

            if (android.os.Build.VERSION.SDK_INT == 25) {

                ToastCompat.makeText(this, "Network already added.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Network already added.", Toast.LENGTH_SHORT).show();
            }

        } else {
            WifiConfiguration wifiConfig = new WifiConfiguration();

            wifiConfig.SSID = String.format("\"%s\"", networkSSID);
            wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

            int netId = wifiManager.addNetwork(wifiConfig);

//            if (netId > 0) {

            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);

            wifiManager.reconnect();

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"\"" + networkSSID + "\"\"";
            conf.preSharedKey = "\"" + networkPass + "\"";
            wifiManager.addNetwork(conf);


            final ProgressDialog dialog = ProgressDialog.show(WifiScanActivity.this, "",
                    "Authenticating. Please wait...", true);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    checkWifi2(networkSSID, networkPass);
                    dialog.dismiss();

                }
            }, 12000);


//            } else {
//                Toast.makeText(this, "wrong password\n" +
//                        "Can't connect to wifi", Toast.LENGTH_SHORT).show();
//            }

        }
    }

    private void checkWifi(String networkSSID, String networkPass) {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (String.valueOf(wifiInfo.getSupplicantState()).equals("COMPLETED")) {
            String tempp = wifiInfo.getSSID().toString();
            tempp = tempp.replace("\"", "");
            if (tempp.equals(networkSSID)) {

                if (android.os.Build.VERSION.SDK_INT == 25) {

                    ToastCompat.makeText(this, "Connected to: " + networkSSID, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Connected to: " + networkSSID, Toast.LENGTH_SHORT).show();
                }

                databaseHelper.addData(networkSSID, networkPass);
            } else {

                if (android.os.Build.VERSION.SDK_INT == 25) {

                    ToastCompat.makeText(this, "Authentication Failed" + networkSSID, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Authentication Failed" + networkSSID, Toast.LENGTH_SHORT).show();
                }
                Log.e("print", "" + tempp);
                Log.e("print", "" + networkSSID);

            }
        } else {

            if (android.os.Build.VERSION.SDK_INT == 25) {

                ToastCompat.makeText(this, "wrong password\n" +
                        "Can't connect to wifi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "wrong password\n" +
                        "Can't connect to wifi", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void checkWifi2(String networkSSID, String networkPass) {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (String.valueOf(wifiInfo.getSupplicantState()).equals("COMPLETED")) {
            String tempp = wifiInfo.getSSID().toString();
            tempp = tempp.replace("\"", "");
            if (tempp.equals(networkSSID)) {

                if (android.os.Build.VERSION.SDK_INT == 25) {

                    ToastCompat.makeText(this, "Connected to: " + networkSSID, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Connected to: " + networkSSID, Toast.LENGTH_SHORT).show();
                }

            } else {

                if (android.os.Build.VERSION.SDK_INT == 25) {

                    ToastCompat.makeText(this, "Authentication Failed" + networkSSID, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Authentication Failed" + networkSSID, Toast.LENGTH_SHORT).show();
                }
                Log.e("print", "" + tempp);
                Log.e("print", "" + networkSSID);

            }
        } else {

            if (android.os.Build.VERSION.SDK_INT == 25) {

                ToastCompat.makeText(this, "wrong password\n" +
                        "Can't connect to wifi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "wrong password\n" +
                        "Can't connect to wifi", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class LogAdapter1 extends BaseAdapter {
        String[] filtered;
        private Context context;
        private ArrayList<String> messages;

        public LogAdapter1(Context context, String[] filtered) {
            this.context = context;
            this.filtered = filtered;

        }

        @Override
        public int getCount() {
            return filtered.length;
        }

        @Override
        public Object getItem(int position) {
            return filtered[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(context).inflate(R.layout.list2, null, false);
            TextView phn =  convertView.findViewById(R.id.phn);
            phn.setText(filtered[position]);
            return convertView;
        }


    }


}
