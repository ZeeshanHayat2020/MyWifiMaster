package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.internet.speed.test.analyzer.wifi.key.generator.app.Helper.LocalHelper;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

@SuppressLint("Registered")
public class SelectCountry extends AppCompatActivity implements View.OnClickListener {

    Button Start;
    ImageView Turkish, Russian, Filipino, Indo, Vietnamese , english;
    SharedPreferences preferences;
    SharedPreferences prefFirsTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loadLocal();
        prefFirsTime = getSharedPreferences("PREFSs", 0);
        String chk = prefFirsTime.getString("firsttimedisp", "0");

        if (chk.equals("yess")) {
            try {
                Intent intent = new Intent(SelectCountry.this,MainActivity.class);
                intent.putExtra("language","selected");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setContentView(R.layout.selecte_country);
            init();
            Turkish.setOnClickListener(this);
            Russian.setOnClickListener(this);
            Filipino.setOnClickListener(this);
            Indo.setOnClickListener(this);
            Vietnamese.setOnClickListener(this);
            english.setOnClickListener(this);

        }

    }

    private void init() {
        Start = findViewById(R.id.start);
        Turkish = findViewById(R.id.turkish);
        Russian = findViewById(R.id.russian);
        Filipino = findViewById(R.id.filpino);
        Indo = findViewById(R.id.indo);
        Vietnamese = findViewById(R.id.vietnamese);
        english = findViewById(R.id.english);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.turkish:
                Intent intent = new Intent(SelectCountry.this, MainActivity.class);
                intent.putExtra("language", "tr");
                firstimepref();
                startActivity(intent);
//                setlocal("tr");
                break;
            case R.id.indo:
                Intent intentindo = new Intent(SelectCountry.this, MainActivity.class);
                intentindo.putExtra("language", "in");
                firstimepref();
                startActivity(intentindo);
//                setlocal("in");
                break;
            case R.id.russian:
                Intent intentrus = new Intent(SelectCountry.this, MainActivity.class);
                intentrus.putExtra("language", "ru");
                firstimepref();
                startActivity(intentrus);
//                setlocal("ru");
                break;
            case R.id.vietnamese:
                Intent intentvie = new Intent(SelectCountry.this, MainActivity.class);
                intentvie.putExtra("language", "vi");
                firstimepref();
                startActivity(intentvie);
//                setlocal("vi");
                break;
            case R.id.filpino:
                Intent intentfil = new Intent(SelectCountry.this, MainActivity.class);
                intentfil.putExtra("language", "b+fil");
                firstimepref();
                startActivity(intentfil);
//                setlocal("b+fil");
                break;
            case R.id.english:
                Intent intentEng = new Intent(SelectCountry.this, MainActivity.class);
                intentEng.putExtra("language", "eng");
                firstimepref();
                startActivity(intentEng);
//                setlocal("b+fil");
                break;
        }
    }

    public void firstimepref() {
        SharedPreferences.Editor editor = prefFirsTime.edit();
        editor.putString("firsttimedisp", "yess");
        editor.apply();
    }

}
