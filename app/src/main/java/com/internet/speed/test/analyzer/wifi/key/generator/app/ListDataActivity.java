package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;
import com.internet.speed.test.analyzer.wifi.key.generator.app.activities.ActivityBase;
import com.internet.speed.test.analyzer.wifi.key.generator.app.activities.MainActivity;
import com.internet.speed.test.analyzer.wifi.key.generator.app.adapters.AdapterMain;
import com.internet.speed.test.analyzer.wifi.key.generator.app.adapters.AdapterShowPassword;
import com.internet.speed.test.analyzer.wifi.key.generator.app.interfaces.OnRecyclerItemClickeListener;
import com.internet.speed.test.analyzer.wifi.key.generator.app.models.ModelMain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import static android.os.Build.VERSION.SDK_INT;

public class ListDataActivity extends ActivityBase {


    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;
    TextView EmptyText;
    AdView adView;
    //myDbAdapter obj;
    private ListView mListView;
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private AdapterShowPassword mAdapter;

    private RelativeLayout layoutHeader;
    public ImageView headerItemMenu;
    public ImageView headerItemCenterLeft;
    public ImageView headerItemCenterRight;
    public ImageView headerItemBottomLeft;
    public ImageView headerItemBottomRigth;
    public TextView headerItemTextViewFirst;
    public TextView headerItemTextViewSecond;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this, R.color.colorWhite, R.color.colorWhite);
        setContentView(R.layout.list_layout);
        setUpHeader();
        MobileAds.initialize(this, getResources().getString(R.string.app_id));
        mDatabaseHelper = new DatabaseHelper(this);
        EmptyText = findViewById(R.id.EmptyText);
        adView = findViewById(R.id.banner_ad);
        iniRecyclerView();
        setUpRecyclerView();

        if (!InAppPrefManager.getInstance(ListDataActivity.this).getInAppStatus()) {
            adview();
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
        headerItemCenterRight.setImageResource(R.drawable.ic_header_item_generate_passworf);
        headerItemTextViewFirst.setText("WIFI");
        headerItemTextViewSecond.setText("PASSWORD");


    }

    private void iniRecyclerView() {
        recyclerView = findViewById(R.id.acShowPassword_recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void setUpRecyclerView() {
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<String> passwrodList = new ArrayList<>();

        Cursor data = mDatabaseHelper.getData();
        while (data.moveToNext()) {
            nameList.add(data.getString(1));
            passwrodList.add(data.getString(2));
        }
        if (passwrodList.isEmpty() || nameList.isEmpty()) {
            EmptyText.setVisibility(View.VISIBLE);
            EmptyText.setText("List Empty!");
        }
        mAdapter = new AdapterShowPassword(this, nameList, passwrodList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

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


}
