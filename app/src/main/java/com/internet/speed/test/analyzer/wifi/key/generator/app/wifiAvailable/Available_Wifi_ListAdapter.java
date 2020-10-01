package com.internet.speed.test.analyzer.wifi.key.generator.app.wifiAvailable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.internet.speed.test.analyzer.wifi.key.generator.app.R;

import java.util.ArrayList;

import static android.content.Context.WIFI_SERVICE;

public class Available_Wifi_ListAdapter extends RecyclerView.Adapter<Available_Wifi_ListAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<ScanResult> scan_results;
    private final OnWifiClickListener onWifiClickListener;


    public Available_Wifi_ListAdapter(Context context, ArrayList<ScanResult> scan_results, OnWifiClickListener onWifiClickListener) {
        this.context = context;
        this.scan_results = scan_results;
        this.onWifiClickListener = onWifiClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.available_wifi_rcv_list, parent, false);
        return new MyViewHolder(view, onWifiClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ScanResult result = scan_results.get(position);
        int level = result.level;

        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiMgr != null;
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        String currentWifi = wifiInfo.getSSID();
        String current = currentWifi.replace("\"", "");

        holder.txtSS_ID.setText(scan_results.get(position).SSID);
        if (scan_results.get(position).SSID.equals(current)) {
            if (Build.VERSION.SDK_INT < 29) {
                holder.btnConnect.setText("Forget");
            }
        }

        if (level <= 0 && level >= -50) {
            //Best signal
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_item_scan_wifi_image3));
        } else if (level < -50 && level >= -70) {
            //Good signal
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_item_scan_wifi_image3));
        } else if (level < -70 && level >= -80) {
            //Low signal
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_item_scan_wifi_image2));
        } else if (level < -80 && level >= -100) {
            //Very weak signal
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_item_scan_wifi_image1));
        } else {
            // no signals
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_item_scan_wifi_image0));
        }

    }

    @Override
    public int getItemCount() {
        return scan_results.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txtSS_ID;
        //        private final TextView checkConnection;
        private final ImageView imageView;
        private final Button btnConnect;

        final OnWifiClickListener onWifiClickListener;

        MyViewHolder(@NonNull View view, OnWifiClickListener onWifiClickListener) {
            super(view);
            txtSS_ID = view.findViewById(R.id.txtSSID);
//            checkConnection = view.findViewById(R.id.checkConnected);
            imageView = view.findViewById(R.id.itemView_scanWifi_iv);
            btnConnect = view.findViewById(R.id.itemView_scanwifi_btnConnect);
            this.onWifiClickListener = onWifiClickListener;

            btnConnect.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onWifiClickListener.OnWifiClickListener(v, getAdapterPosition());
        }
    }


    public interface OnWifiClickListener {
        void OnWifiClickListener(View view, int position);
    }
}
