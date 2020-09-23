package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.database.MyPreferences;



public class ActivityPrivacyPolicy extends ActivityBase {

    private TextView tvPrivacyPolicy;
    private TextView tvPrivacyPolicyLinkView;

    private Button btnAccept;
    private Button bntDecline;
    private MyPreferences myPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this, R.color.colorPrimary, R.color.colorPrimary);
        setContentView(R.layout.activity_privacy_policy);
        myPreferences = new MyPreferences(this);
        initViews();

    }


    private void initViews() {

        btnAccept = (Button) findViewById(R.id.acPP_btnAccept);
        bntDecline = (Button) findViewById(R.id.acPP_btnDecline);
        btnAccept.setOnClickListener(onClickListener);
        bntDecline.setOnClickListener(onClickListener);
    }

    /*private void setPrivacyPolicyText() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvPrivacyPolicy.setText(Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_LEGACY));
            tvPrivacyPolicyLinkView.setText(Html.fromHtml(getString(R.string.privacy_policy_linkText), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvPrivacyPolicy.setText(Html.fromHtml(getString(R.string.privacy_policy_text)));
            tvPrivacyPolicyLinkView.setText(Html.fromHtml(getString(R.string.privacy_policy_linkText)));
        }
        tvPrivacyPolicyLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.pp_url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }*/

    private GradientDrawable getGradient(int color1, int color2) {
        int[] colors = {Integer.parseInt(String.valueOf(color1)),
                Integer.parseInt(String.valueOf(color2))
        };
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                colors);
        float radius = getResources().getDimension(R.dimen._6sdp);
        gd.setCornerRadius(radius);
        return gd;
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    private void setAcceptance(boolean isAccept) {
        myPreferences.setPrivacyPolicyAcceptance(isAccept);
        if (isAccept) {
            startMainActivity();
        } else {
            this.finish();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.acPP_btnAccept: {
                    setAcceptance(true);
                }
                break;
                case R.id.acPP_btnDecline: {
                    setAcceptance(false);
                }
                break;
            }
        }
    };


}