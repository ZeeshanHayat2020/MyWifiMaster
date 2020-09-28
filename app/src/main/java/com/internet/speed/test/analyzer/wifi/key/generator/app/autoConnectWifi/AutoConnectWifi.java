package com.internet.speed.test.analyzer.wifi.key.generator.app.autoConnectWifi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.activities.ActivityBase;

import java.util.Locale;

import static android.os.Build.VERSION.SDK_INT;

public class AutoConnectWifi extends ActivityBase {

    private RelativeLayout layoutHeader;
    public ImageView headerItemMenu;
    public ImageView headerItemCenterLeft;
    public ImageView headerItemCenterRight;
    public ImageView headerItemBottomLeft;
    public ImageView headerItemBottomRigth;
    public TextView headerItemTextViewFirst;
    public TextView headerItemTextViewSecond;

    private ToggleButton btnOnOff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this, R.color.colorWhite, R.color.colorWhite);
        setContentView(R.layout.activity_auto_connect_wifi);
        setUpHeader();

        btnOnOff = findViewById(R.id.acAutoWifi_btnOnOff);
        btnOnOff.setOnClickListener(onClickListener);

        Boolean check = ConnectToWifi_Service.isServiceRunning(AutoConnectWifi.this.getApplicationContext(), ConnectToWifi_Service.class);
        btnOnOff.setChecked(check);

    }

    void turnOnOffWifi(boolean isChecked) {
        if (isChecked) {
            Log.d("AutoConnectWifi", "turnOnOffWifi: Checked ");
            startService(new Intent(AutoConnectWifi.this, ConnectToWifi_Service.class));

        } else {
            stopService(new Intent(AutoConnectWifi.this, ConnectToWifi_Service.class));
            Log.d("AutoConnectWifi", "turnOnOffWifi: unChecked ");
        }
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


        headerItemCenterLeft.setVisibility(View.INVISIBLE);
        headerItemBottomLeft.setVisibility(View.INVISIBLE);
        headerItemBottomRigth.setVisibility(View.INVISIBLE);

        headerItemCenterRight.setImageResource(R.drawable.ic_header_item_auto_wifi);
        headerItemTextViewFirst.setText(R.string.auto_wifi);
        headerItemTextViewSecond.setVisibility(View.INVISIBLE);


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (((ToggleButton) view).isChecked()) {
                turnOnOffWifi(true);
            } else {
                turnOnOffWifi(false);
            }


        }
    };
}
