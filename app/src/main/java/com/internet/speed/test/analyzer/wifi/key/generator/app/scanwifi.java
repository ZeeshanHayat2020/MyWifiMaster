package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;

import java.util.List;

import me.drakeet.support.toast.ToastCompat;

public class scanwifi extends ListActivity {
    static int keyone2 = 0;
    WifiManager mainWifiObj;
    WifiScanReceiver wifiReciever;
    ListView list;
    String wifis[];
    DatabaseHelper mDatabaseHelper;
    AdView banner;
    SharedPreferences preferences;
    EditText pass;
    AdView adView;

    public static boolean isInternetIsConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                keyone2 = 1;
                return true;

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                keyone2 = 0;
                return true;
            }
        } else {
            keyone2 = 0;
            return false;
        }
        return false;
    }

    public void adview() {
        adView = findViewById(R.id.banner_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanwifi);
        MobileAds.initialize(this, getResources().getString(R.string.app_id));

        banner = findViewById(R.id.banner_ad);
        mDatabaseHelper = new DatabaseHelper(this);

        preferences = getSharedPreferences("PREFS", 0);

        if (!InAppPrefManager.getInstance(scanwifi.this).getInAppStatus()) {
            adview();
        }

        list = getListView();
        registerReceiver(wifiReciever,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        mainWifiObj.startScan();

        String chk = preferences.getString("enable", "0");
        String dis = preferences.getString("disable", "0");

        if (chk.equals("yes")) {
            list.setVisibility(View.VISIBLE);
        } else if (chk.equals("no")) {
            list.setVisibility(View.INVISIBLE);
        } else {

        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String ssid = ((TextView) view).getText().toString();

                connectToWifi(ssid);
//                Toast.makeText(scanwifi.this, "Wifi SSID : " + ssid, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    private void finallyConnect(String networkPass, String networkSSID) {


        WifiConfiguration wifiConfig = new WifiConfiguration();


        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);
        int netId = mainWifiObj.addNetwork(wifiConfig);
        mainWifiObj.disconnect();
        mainWifiObj.enableNetwork(netId, true);
        mainWifiObj.reconnect();
        WifiConfiguration conf = new WifiConfiguration();

        conf.SSID = "\"\"" + networkSSID + "\"\"";
        conf.preSharedKey = "\"" + networkPass + "\"";
        mainWifiObj.addNetwork(conf);
        if (keyone2 == 1) {
            Boolean tm = isInternetIsConnected(getApplicationContext());

            if (android.os.Build.VERSION.SDK_INT == 25) {

                ToastCompat.makeText(getApplicationContext(), "value conncetion" + tm, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "value conncetion" + tm, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void connectToWifi(final String wifiSSID) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.connect);
        dialog.setTitle("Connect to Network");
        TextView textSSID = dialog.findViewById(R.id.textSSID1);
        Button dialogButton =  dialog.findViewById(R.id.okButton);
        Button btndisconnect = dialog.findViewById(R.id.btn_disconnect);
        pass = (EditText) dialog.findViewById(R.id.textPassword);
        CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.cb_show);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        textSSID.setText(wifiSSID);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkpassword = pass.getText().toString();

                if (TextUtils.isEmpty(checkpassword)) {
                    pass.setError("Insert password");

                } else {
                    String checkPassword = pass.getText().toString();
                    finallyConnect(checkPassword, wifiSSID);
                    //Boolean chkconection=isInternetIsConnected(getApplicationContext());


                    // if(chkconection==true) {
                    AddData(wifiSSID, checkPassword);
                    // Toast.makeText(scanwifi.this, " Connected To" + wifiSSID , Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
//                   }
//                   else
//                   {
//                      Toast.makeText(scanwifi.this, "Not Connected" + wifiSSID , Toast.LENGTH_SHORT).show();
//                       dialog.dismiss();
//                   }

                }
            }
        });
        btndisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void AddData(String newEntry, String newEntry2) {

        if (mDatabaseHelper.getItemID(newEntry, newEntry2)) {
            toastMessage("Already Exist..!!");
        }
      /*  else if (mDatabaseHelper.Checkpwd(newEntry2)){
            toastMessage("Password or SSID Already Exist..!!");
        }
        else if (mDatabaseHelper.Checkname(newEntry)){
            toastMessage("Password or SSID Already Exist..!!");
        }*/
        else if (mDatabaseHelper.getItemcheck(newEntry, newEntry2)) {
            toastMessage("Already Exist..!!");
        } else {
            boolean insertData = mDatabaseHelper.addData(newEntry, newEntry2);

            if (insertData) {
                toastMessage("Show password Successfully!");
            } else {
                toastMessage("Something went wrong");
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            wifis = new String[wifiScanList.size()];
            for (int i = 0; i < wifiScanList.size(); i++) {
                wifis[i] = ((wifiScanList.get(i)).toString());
            }
            String filtered[] = new String[wifiScanList.size()];
            String filtered2[] = new String[filtered.length];
            int counter = 0;
            for (String eachWifi : wifis) {
                String[] temp = eachWifi.split(",");

                filtered[counter] = temp[0].substring(5).trim();

                counter++;

            }
            list.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.label, filtered));
//            SharedPreferences.Editor editor = preferences.edit();
//
//            int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE ,
//                    WifiManager.WIFI_STATE_UNKNOWN);
//
//            switch(extraWifiState){
//                case WifiManager.WIFI_STATE_DISABLED:
//                   // WifiState.setText("WIFI STATE DISABLED");
//                    break;
//                case WifiManager.WIFI_STATE_DISABLING:
//                    //WifiState.setText("WIFI STATE DISABLING");
//                    break;
//                case WifiManager.WIFI_STATE_ENABLED:
//
//                    editor.putString("enable", "yes");
//                    editor.apply();
//                    //WifiState.setText("WIFI STATE ENABLED");
//                    break;
//                case WifiManager.WIFI_STATE_ENABLING:
//                    editor.putString("enable", "yes");
//                    editor.apply();
//                    //WifiState.setText("WIFI STATE ENABLING");
//                    break;
//                case WifiManager.WIFI_STATE_UNKNOWN:
//                    //WifiState.setText("WIFI STATE UNKNOWN");
//                    break;
//            }
//            String chk = preferences.getString("enable", "0");
//            if (chk.equals("yes")) {
//
//                list.setVisibility(View.VISIBLE);
//            } else
//            {
//                list.setVisibility(View.INVISIBLE);
//            }
        }
    }

}
