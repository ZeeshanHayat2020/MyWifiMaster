package com.internet.speed.test.analyzer.wifi.key.generator.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.interfaces.OnRecyclerItemClickeListener;
import com.internet.speed.test.analyzer.wifi.key.generator.app.models.ModelMain;

import java.util.ArrayList;
import java.util.List;


public class AdapterShowPassword extends RecyclerView.Adapter<AdapterShowPassword.MyViewHolder> {

    private Context context;
    private ArrayList<String> nameLis;
    private ArrayList<String> passwordList;
    private OnRecyclerItemClickeListener onRecyclerItemClickeListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickeListener onRecyclerItemClickeListener) {
        this.onRecyclerItemClickeListener = onRecyclerItemClickeListener;
    }

    public AdapterShowPassword(Context context, ArrayList<String> nameLis, ArrayList<String> passwordList) {
        this.context = context;
        this.nameLis = nameLis;
        this.passwordList = passwordList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewPassword;


        public MyViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.itemView_show_password_tvName);
            textViewPassword = view.findViewById(R.id.itemView_show_password_tvPassword);


        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_show_password, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.textViewName.setText("Name:" + nameLis.get(position));
        holder.textViewPassword.setText("Password:" + passwordList.get(position));


    }


    @Override
    public int getItemCount() {
        return passwordList.size();
    }


}
