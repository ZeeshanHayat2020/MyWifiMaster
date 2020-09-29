package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.internet.speed.test.analyzer.wifi.key.generator.app.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothConnectivity extends ActivityBase implements AdapterView.OnItemClickListener {

    private String TAG = "BluetoothConnectivity";
    private CardView switchCardView;
    private SwitchCompat btnOnOf;
    private Button btnScanDevices;
    private Button btnMakeVisible;
    private ProgressBar scanningLoadingBar;
    private Handler handler;
    private Runnable runnable;

    BluetoothAdapter mBluetoothAdapter;
    ArrayList<String> scannedDeviceList;
    public ArrayList<BluetoothDevice> mBTDevices;
    private ListView nearByListView;


    public ImageView headerItemCenterRight;
    public TextView headerItemTextViewFirst;
    public TextView headerItemTextViewSecond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this, R.color.colorWhite, R.color.colorWhite);
        setContentView(R.layout.activity_bluetooth_connectivity);
        if (haveNetworkConnection()) {
            requestBanner((FrameLayout) findViewById(R.id.bannerContainer));
        }
        setUpHeader();
        checkBTPermissions();
        initViews();


    }

    @Override
    protected void onResume() {
        super.onResume();
        reqNewInterstitial(this);
    }

    void setUpHeader() {

        headerItemCenterRight = findViewById(R.id.header_item_centerRight_imageView);

        headerItemTextViewFirst = findViewById(R.id.header_item_textView_First);
        headerItemTextViewSecond = findViewById(R.id.header_item_textView_Second);
        headerItemCenterRight.setImageResource(R.drawable.ic_header_item_bluetooth);
        headerItemTextViewFirst.setText(R.string.bluetooth);
        headerItemTextViewSecond.setText(R.string.connectivity);


    }


    private final BroadcastReceiver receiverBluetoothOnOff = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        stopScannig();
                        Log.d(TAG, "receiverBluetoothOnOff: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "receiverBluetoothOnOff: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "receiverBluetoothOnOff: STATE ON");
                        startScan();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "receiverBluetoothOnOff: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver receiverDiscoverable = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "receiverDiscoverable: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "receiverDiscoverable: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "receiverDiscoverable: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "receiverDiscoverable: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "receiverDiscoverable: Connected.");
                        break;
                }

            }
        }
    };

    private BroadcastReceiver receiverScannedDevices = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(receiverScannedDevices);
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                scanningLoadingBar.setVisibility(View.INVISIBLE);
                nearByListView.setVisibility(View.VISIBLE);
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "New Incomming device" + device.getName() + "\n" + device.getAddress());

                if (device.getAddress() != null) {

                   /* if (!mBTDevices.isEmpty() && !scannedDeviceList.isEmpty()) {
                        mBTDevices.clear();
                        scannedDeviceList.clear();
                    }
                    mBTDevices.add(device);
                    scannedDeviceList.add(device.getName() + "\n" + device.getAddress());*/
                    boolean isItemMatch = false;
                    for (int i = 0; i < mBTDevices.size(); i++) {
                        Log.d(TAG, "MyOld BluetoothDevices List" + mBTDevices.get(i).getName() + "\n" + mBTDevices.get(i).getAddress());
                        if (device.getAddress().equals(mBTDevices.get(i).getAddress())) {
                            isItemMatch = true;
                            Log.d(TAG, "Address Match");
                        }
                    }
                    if (!isItemMatch) {
                        Log.d(TAG, "Address NOT Match");
                        mBTDevices.add(device);
                        if (device.getName() == null) {
                            scannedDeviceList.add("No Name Set" + "\n" + device.getAddress());
                        } else {
                            scannedDeviceList.add(device.getName() + "\n" + device.getAddress());
                        }
                    }
                    nearByListView.setAdapter(new ArrayAdapter<String>(context,
                            android.R.layout.simple_list_item_1, scannedDeviceList));
                }
            }
        }
    };


    private void initViews() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                hideLoadingBar();
                mBluetoothAdapter.cancelDiscovery();
            }
        };
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBTDevices = new ArrayList<>();
        scannedDeviceList = new ArrayList<>();

        switchCardView = findViewById(R.id.acBluetooth_switchCarView);
        btnOnOf = findViewById(R.id.acBluetooth_OnOff);
        btnScanDevices = findViewById(R.id.acBluetooth_btnScan);
        btnMakeVisible = findViewById(R.id.acBluetooth_btnMakeDescoverable);
        nearByListView = findViewById(R.id.acBluetooth_NearByDevice_listView);
        scanningLoadingBar = findViewById(R.id.acBluetooth_NearByDevice_loadingBar);
        if (mBluetoothAdapter.isEnabled()) {
            btnOnOf.setChecked(true);
        } else {
            btnOnOf.setChecked(false);
        }

        switchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnOnOf.isChecked()) {
                    btnOnOf.setChecked(false);
                } else {
                    btnOnOf.setChecked(true);
                }
            }
        });
        nearByListView.setOnItemClickListener(BluetoothConnectivity.this);
        btnOnOf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    enableBluetooth();
//                    enableDisableBT();
                } else {
//                    enableDisableBT();
                    disableBluetooth();
                }
            }
        });
        btnMakeVisible.setOnClickListener(onButtonsClicked);
        btnScanDevices.setOnClickListener(onButtonsClicked);

    }

    private View.OnClickListener onButtonsClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.acBluetooth_btnMakeDescoverable: {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                EnableDisable_Discoverable();
                            }
                        });
                    } else {
                        EnableDisable_Discoverable();
                    }

                }
                break;
                case R.id.acBluetooth_btnScan: {
                    startScan();
                }
                break;
            }
        }
    };

    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }


    private void enableBluetooth() {
        if (!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(receiverBluetoothOnOff, BTIntent);
        }
    }

    private void disableBluetooth() {
        if (mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(receiverBluetoothOnOff, BTIntent);
        }
    }


    public void EnableDisable_Discoverable() {
        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(receiverDiscoverable, intentFilter);

    }

    private void startScan() {
        Toast.makeText(getApplicationContext(), "Searching Devices", Toast.LENGTH_SHORT).show();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiverScannedDevices, intentFilter);
        showLoadingBar();
        mBluetoothAdapter.startDiscovery();
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 5000);
        }

    }

    private void stopScannig() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
            hideLoadingBar();
        }
    }

    private void showLoadingBar() {
        scanningLoadingBar.setVisibility(View.VISIBLE);
        nearByListView.setVisibility(View.INVISIBLE);
    }

    private void hideLoadingBar() {
        scanningLoadingBar.setVisibility(View.INVISIBLE);
        nearByListView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //first cancel discovery because its very memory intensive.

        stopScannig();
        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = mBTDevices.get(i).getName();
        String deviceAddress = mBTDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.d(TAG, "Trying to pair with " + deviceName);
            mBTDevices.get(i).createBond();

        }
    }


    private void unRegisterReceivers(BroadcastReceiver receiver) {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            // already registered
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceivers(receiverBluetoothOnOff);
        unRegisterReceivers(receiverDiscoverable);
        unRegisterReceivers(receiverScannedDevices);

    }
}