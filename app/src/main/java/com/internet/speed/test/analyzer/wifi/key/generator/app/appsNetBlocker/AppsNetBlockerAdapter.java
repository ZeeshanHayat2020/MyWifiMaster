package com.internet.speed.test.analyzer.wifi.key.generator.app.appsNetBlocker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.internet.speed.test.analyzer.wifi.key.generator.app.R;

import java.util.ArrayList;
import java.util.List;

public class AppsNetBlockerAdapter extends RecyclerView.Adapter<AppsNetBlockerAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "appsNetBlocker";

    private Context context;
    private int colorText;
    private int colorAccent;
    private List<Apps_Model_Class> listAll;
    private List<Apps_Model_Class> listSelected;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ImageView ivIcon;
        public TextView tvName;
        public TextView received;
        public TextView transfer;
        public ToggleButton cbWifi;
        public ToggleButton cbOther;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon2);
            tvName = (TextView) itemView.findViewById(R.id.tvName2);
            cbWifi = (ToggleButton) itemView.findViewById(R.id.cbWifi2);
            cbOther = (ToggleButton) itemView.findViewById(R.id.cbOther2);

        }
    }

    public AppsNetBlockerAdapter(List<Apps_Model_Class> listRule, Context context) {
        this.context = context;
        colorAccent = ContextCompat.getColor(context, R.color.colorAccent);
        TypedArray ta = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.textColorSecondary});
        try {
            colorText = ta.getColor(0, 0);
        } finally {
            ta.recycle();
        }
        listAll = listRule;
        listSelected = new ArrayList<>();
        listSelected.addAll(listRule);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Get rule
        final Apps_Model_Class rule = listSelected.get(position);

        // Rule change listener
        CompoundButton.OnCheckedChangeListener cbListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String network;
                if (buttonView == holder.cbWifi) {
                    network = "wifi";
                    rule.wifi_blocked = isChecked;
                } else {
                    network = "other";
                    rule.mobileData_blocked = isChecked;
                }
                Log.i(TAG, rule.info.packageName + ": " + network + "=" + isChecked);

                SharedPreferences prefs = context.getSharedPreferences(network, Context.MODE_PRIVATE);
                prefs.edit().putBoolean(rule.info.packageName, isChecked).apply();

                AppsNetBlockerService.refreshVpnService(network, context);
            }
        };

        int color = rule.system ? colorAccent : colorText;
        if (rule.disabled)
            color = Color.argb(100, Color.red(color), Color.green(color), Color.blue(color));

        holder.ivIcon.setImageDrawable(rule.getIcon(context));

        //setting name of app
        holder.tvName.setText(rule.name);

        //setting package of app

        //setting wifi on or off for app
        holder.cbWifi.setOnCheckedChangeListener(null);
        holder.cbWifi.setChecked(rule.wifi_blocked);
        holder.cbWifi.setOnCheckedChangeListener(cbListener);

        //setting mobile data on or off for app
        holder.cbOther.setOnCheckedChangeListener(null);
        holder.cbOther.setChecked(rule.mobileData_blocked);
        holder.cbOther.setOnCheckedChangeListener(cbListener);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence query) {
                List<Apps_Model_Class> listResult = new ArrayList<>();
                if (query == null)
                    listResult.addAll(listAll);
                else {
                    query = query.toString().toLowerCase();
                    for (Apps_Model_Class rule : listAll)
                        if (rule.name.toLowerCase().contains(query))
                            listResult.add(rule);
                }

                FilterResults result = new FilterResults();
                result.values = listResult;
                result.count = listResult.size();
                return result;
            }

            @Override
            protected void publishResults(CharSequence query, FilterResults result) {
                listSelected.clear();
                if (result == null)
                    listSelected.addAll(listAll);
                else
                    for (Apps_Model_Class rule : (List<Apps_Model_Class>) result.values)
                        listSelected.add(rule);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public AppsNetBlockerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.apps_net_blocker_item_rcv2, parent, false));
    }

    @Override
    public int getItemCount() {
        return listSelected.size();
    }
}
