package com.internet.speed.test.analyzer.wifi.key.generator.app.allRouterPassword;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.activities.ActivityBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class AllRouterPasswords extends ActivityBase {


    ArrayList<All_Router_Model_Class> arrayList;
    RouterAdapterClass adapter;
    RecyclerView recyclerView;
    android.widget.SearchView searchView;
    private ImageView btnMenu;


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
        setStatusBarGradient(this, R.color.colorPrimaryDark, R.color.colorWhite);
        setContentView(R.layout.activity_all_router_password);
        setUpHeader();
        arrayList = new ArrayList<>();
        try {

            JSONObject obj = new JSONObject(readJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("modem");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);

                String brand = jo_inside.getString("brand");
                String model = jo_inside.getString("model");
                String protocol = jo_inside.getString("protocol");
                String username = jo_inside.getString("username");
                String password = jo_inside.getString("password");

                All_Router_Model_Class object = new All_Router_Model_Class(brand, password, username, protocol, model);

                arrayList.add(object);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        searchView = (android.widget.SearchView) findViewById(R.id.acNetBlock_searchView);
        recyclerView = findViewById(R.id.allRouterRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RouterAdapterClass(arrayList, this);
        recyclerView.setAdapter(adapter);


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
        headerItemTextViewSecond.setVisibility(View.INVISIBLE);
        headerItemCenterRight.setImageResource(R.drawable.ic_header_item_net_block);
        headerItemTextViewFirst.setText(R.string.net_blocker);


    }

    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("modem");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.all_router_menu, menu);

        // Search
        MenuItem searchItem = menu.findItem(R.id.router_menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Enter Brand Name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapter.updateData(arrayList);
                }
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        SearchView.OnCloseListener closeListener = new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {

                adapter.updateData(arrayList);
                return false;
            }
        };
        searchView.setOnCloseListener(closeListener);
        return true;
    }
}
