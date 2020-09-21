package com.internet.speed.test.analyzer.wifi.key.generator.app.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.interfaces.OnRecyclerItemClickeListener;
import com.internet.speed.test.analyzer.wifi.key.generator.app.models.ModelMain;

import java.util.List;


public class AdapterBluetooth extends RecyclerView.Adapter<AdapterBluetooth.MyViewHolder> {

    private Context context;
    private List<String> bluetoothDeviceList;
    private OnRecyclerItemClickeListener onRecyclerItemClickeListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickeListener onRecyclerItemClickeListener) {
        this.onRecyclerItemClickeListener = onRecyclerItemClickeListener;
    }

    public AdapterBluetooth(Context context, List<String> modelMainList) {
        this.context = context;
        this.bluetoothDeviceList = bluetoothDeviceList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public Button btnConnect;


        public MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.itemView_bluetooth_nbDevice_tvName);
            btnConnect = view.findViewById(R.id.itemView_bluetooth_nbDevice_btnConnect);


        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_main, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        holder.textView.setText(BluetoothDevice.);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != RecyclerView.NO_POSITION) {
                    onRecyclerItemClickeListener.onItemClicked(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
