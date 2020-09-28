package com.internet.speed.test.analyzer.wifi.key.generator.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.database.MyPreferences;


public class ActivityPrivacyPolicy extends ActivityBase {


    private RelativeLayout layoutHeader;
    public ImageView headerItemMenu;
    public ImageView headerItemCenterLeft;
    public ImageView headerItemCenterRight;
    public ImageView headerItemBottomLeft;
    public ImageView headerItemBottomRigth;
    public TextView headerItemTextViewFirst;
    public TextView headerItemTextViewSecond;

    private TextView tvPrivacyPolicyLinkView;
    private Button btnAccept;
    private Button bntDecline;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;

    private MyPreferences myPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this, R.color.colorWhite, R.color.colorWhite);
        setContentView(R.layout.activity_privacy_policy);
        myPreferences = new MyPreferences(this);
        setUpHeader();
        initViews();
        setPrivacyPolicyText();

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

        headerItemMenu.setVisibility(View.INVISIBLE);
        headerItemCenterLeft.setVisibility(View.INVISIBLE);
        headerItemBottomLeft.setVisibility(View.INVISIBLE);
        headerItemBottomRigth.setVisibility(View.INVISIBLE);
        headerItemTextViewSecond.setVisibility(View.INVISIBLE);
        headerItemCenterRight.setImageResource(R.drawable.ic_header_privacy_policy);
        headerItemTextViewFirst.setText(R.string.pp_heading);


    }

    private void initViews() {

        btnAccept = (Button) findViewById(R.id.acPP_btnAccept);
        bntDecline = (Button) findViewById(R.id.acPP_btnDecline);
        tvPrivacyPolicyLinkView = findViewById(R.id.acPrivacyPolicy_linkView);
        checkBox1 = findViewById(R.id.acPP_CheckBox1);
        checkBox2 = findViewById(R.id.acPP_CheckBox2);
        checkBox3 = findViewById(R.id.acPP_CheckBox3);
        checkBox4 = findViewById(R.id.acPP_CheckBox4);
        btnAccept.setOnClickListener(onClickListener);
        bntDecline.setOnClickListener(onClickListener);
        checkBox1.setOnCheckedChangeListener(onCheckBoxChangeListener);
        checkBox2.setOnCheckedChangeListener(onCheckBoxChangeListener);
        checkBox3.setOnCheckedChangeListener(onCheckBoxChangeListener);
        checkBox4.setOnCheckedChangeListener(onCheckBoxChangeListener);

    }

    CompoundButton.OnCheckedChangeListener onCheckBoxChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (compoundButton.getId()) {
                case R.id.acPP_CheckBox1: {
                    enableAcceptBtn();
                }
                break;
                case R.id.acPP_CheckBox2: {
                    enableAcceptBtn();
                }
                break;
                case R.id.acPP_CheckBox3: {
                    enableAcceptBtn();
                }
                break;
                case R.id.acPP_CheckBox4: {
                    enableAcceptBtn();
                }
                break;
            }
        }
    };

    private void enableAcceptBtn() {
        btnAccept.setEnabled(checkBox1.isChecked() && checkBox2.isChecked() && checkBox3.isChecked() && checkBox4.isChecked());
    }

    private void setPrivacyPolicyText() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvPrivacyPolicyLinkView.setText(Html.fromHtml(getString(R.string.privacy_policy_linkText), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvPrivacyPolicyLinkView.setText(Html.fromHtml(getString(R.string.privacy_policy_linkText)));
        }
        tvPrivacyPolicyLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.privacy_policy_URL);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

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