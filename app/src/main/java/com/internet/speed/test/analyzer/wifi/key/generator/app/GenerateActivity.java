package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.internet.speed.test.analyzer.wifi.key.generator.app.Utils.InAppPrefManager;

import me.drakeet.support.toast.ToastCompat;

public class GenerateActivity extends AppCompatActivity {
    TextView t1;
    Button b1;
    String string_key;
    String[] Textlist;
    ClipboardManager myClipboard;
    ClipData myClip;
    AdView banner;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        MobileAds.initialize(this, getResources().getString(R.string.app_id));

        banner = (AdView) findViewById(R.id.banner_ad);
        t1=(TextView) findViewById(R.id.generatestring);
        b1=(Button)findViewById(R.id.key);
        if (!InAppPrefManager.getInstance(GenerateActivity.this).getInAppStatus()) {
            adview();
        }

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    t1.setText(getRandomString());
                }catch (Exception e){e.printStackTrace();}

            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = t1.getText().toString();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                if (android.os.Build.VERSION.SDK_INT == 25) {

                    ToastCompat.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    static String getRandomString(){
        int r = (int) (Math.random()*100);
        String name = new String [] {
                "myhome_mefiase",
                "wsdasd_039427r23",
                "leather123",
                "textile321",
                "ttuiop",
                "merlin",
                "methew432",
                "mywifi_password1212",
                "brazil560",
                "12121212",
                "13131313",
                "56575657",
                "orange",
                "freedom",
                "123456",
                "paasword",
                "Azweyes_enfowefnas"
                ,"Wufwiuefbaasdasd",
                "123456789",
                "qwerty",
                "livelishell"
                ,"abc123"
                ,"12345678"
                ,"111111"
                ,"jorden"
                ,"nearstreet123"
                ,"123123"
                ,"admin"
                ,"letmein"
                ,"hello"
                ,"strettt_anskndas"
                ,"wasjbfsounsdjf"
                ,"shop"
                ,"cuttingboard"
                ,"password1"
                ,"monkey"
                ,"shadow"
                ,"sunshine"
                ,"likewise"
                ,"tommorrow"
                ,"today"
                ,"yesplease"
                ,"sareygamapa"
                ,"000000"
                ,"trustno1"
                ,"princess"
                ,"zaq1zaq1"
                ,"charming"
                ,"nightrider"
                ,"kjk4v,ns2b7"
                ,"publicplace"
                ,"jolta123"
                ,"azerty"
                ,"liberty"
                ,"market"
                ,"place"
                ,"bukingumpalace"
                ,"newpassword"
                ,"southperth"
                ,"random123"
                ,"google"
                ,"googlechro"
                ,"NETGEAR"
                ,"belkin54g"
                ,"MiniAP"
                ,"AppleNetwork0273df"
                ,"public"
                ,"user"
                ,"p4@$W0Rd"
                ,"Wireless"
                ,"77777777"
                ,"88888888"
                ,"66666666"
                ,"hatershateme"
                ,"login"
                ,"master"
                ,"hellowWorld"
                ,"give_me_ldnas"
                ,"qazwsx"
                ,"whatsapp"
                ,"lily"
                ,"plantytong"
                ,"chuenli"
                ,"bottle"
                ,"lamp121"
                ,"121321432"
                ,"0987654321"
                ,"poiuytrewq"
                ,"lkjhgfdsa"
                ,"mnbvcxz_mnbvcxz"
                ,"wasnaodfune_oeifjw"
                ,"myownpaswwrod_1234123"
                ,"trustno1"
                ,"football"
                ,"1q2w3e4r"
                ,"18atcskd2w"
                ,"3rjs1la7qe"
                ,"zxcvbnm"
                ,"123qwe"
                ,"superman"
                ,"yankees"
        }[r];
        return name;
    }
    public void adview() {

        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

    }
}

