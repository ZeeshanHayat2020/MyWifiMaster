package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.adapters.AppAdapter;

import java.util.List;

import bot.box.appusage.contract.UsageContracts;
import bot.box.appusage.handler.Monitor;
import bot.box.appusage.model.AppData;
import bot.box.appusage.utils.Duration;

public class ActivityAppUsage extends ActivityBase implements UsageContracts.View
        , AdapterView.OnItemSelectedListener {

    private AppAdapter mAdapter;
    private ProgressBar loadingBar;
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
        setContentView(R.layout.activity_app_usage);
        setUpHeader();
        loadingBar = findViewById(R.id.acAppDataUsage_loadingBar);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Monitor.hasUsagePermission()) {
            Monitor.scan().getAppLists(this).fetchFor(Duration.TODAY);
            init();
        } else {
            Monitor.requestUsagePermission();
        }
    }

    void setUpHeader() {
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

        headerItemCenterRight.setImageResource(R.drawable.ic_header_item_app_data_usage);
        headerItemTextViewFirst.setText(R.string.application);
        headerItemTextViewSecond.setText(R.string.data_usage);

    }

    private void init() {

        RecyclerView mRecycler = findViewById(R.id.recycler);
        mAdapter = new AppAdapter(this);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.duration));
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Monitor.scan().getAppLists(this).fetchFor(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    @Override
    public void showProgress() {
        loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loadingBar.setVisibility(View.INVISIBLE);
    }

    /**
     * @param usageData   list of application that has been within the duration for which query has been made.
     * @param mTotalUsage a sum total of the usage by each and every app with in the request duration.
     * @param duration    the same duration for which query has been made i.e.fetchFor(Duration...)
     */

    @Override
    public void getUsageData(List<AppData> usageData, long mTotalUsage, int duration) {
        mAdapter.updateData(usageData);
    }
}