package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;

import java.util.ArrayList;
import java.util.Locale;

import static android.os.Build.VERSION.SDK_INT;

public class ListDataActivity extends AppCompatActivity {


    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;
    TextView EmptyText;
    AdView adView;
    //myDbAdapter obj;
    private ListView mListView;

    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    private void setLocale(Locale locale) {
        // optional - Helper method to save the selected language to SharedPreferences in case you might need to attach to activity context (you will need to code this)
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        if (SDK_INT > Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_layout);
        MobileAds.initialize(this, getResources().getString(R.string.app_id));

        mListView = findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);
        EmptyText = findViewById(R.id.EmptyText);
        adView = findViewById(R.id.banner_ad);

        populateListView();
        if (!InAppPrefManager.getInstance(ListDataActivity.this).getInAppStatus()) {
            adview();
        }
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

    private void populateListView() {

        Log.e(TAG, "populateListView: Displaying data in the ListView.");
        Cursor data = mDatabaseHelper.getData();

        //     Cursor data = obj.getData().toString();

        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            listData.add("Name : " + data.getString(1) + " \n" + "Password : " + data.getString(2));
            //   listData.add(obj.getData()+"pw "+"/n"+obj.gettodo() );

        }

        if (listData.isEmpty()) {
            EmptyText.setVisibility(View.VISIBLE);
            EmptyText.setText("List Empty!");
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String name = adapterView.getItemAtPosition(i).toString();
//                String password=adapterView.getItemAtPosition(i).toString();
//                Log.e(TAG, "onItemClick: You Clicked on " + name );
//
//                Cursor data = mDatabaseHelper.getItemID(name , password);
//                int itemID = -1;
//                while(data.moveToNext()){
//                    itemID = data.getInt(0);
//                }
//                if(itemID > -1){
//                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
//                    Intent editScreenIntent = new Intent(ListDataActivity.this, ListDataActivity.class);
//                    editScreenIntent.putExtra("id",itemID);
//                    editScreenIntent.putExtra("name",name);
//                    editScreenIntent.putExtra("password",password);
//                    startActivity(editScreenIntent);
//                }
//                else{
//                    toastMessage("No ID associated with that name");
//                }
//            }
//        });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
