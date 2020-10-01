package com.internet.speed.test.analyzer.wifi.key.generator.app.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
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
        public ImageView btnMenu;


        public MyViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.itemView_show_password_tvName);
            textViewPassword = view.findViewById(R.id.itemView_show_password_tvPassword);
            btnMenu = view.findViewById(R.id.itemView_show_password_btnMenu);


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


        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecyclerItemClickeListener != null) {
                    if (position != RecyclerView.NO_POSITION) {
                        setItemViewMenu(view.getContext(), view, position);
                    }
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return passwordList.size();
    }


    private void setItemViewMenu(Context context, View view, final int position) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.inflate(R.menu.menu_show_passwor);
        popup.setGravity(Gravity.RIGHT);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_showPass_itemView_copy: {

                        if (onRecyclerItemClickeListener != null) {
                            if (position != RecyclerView.NO_POSITION) {
                                onRecyclerItemClickeListener.onItemCopyClicked(position);
                            }
                        }
                    }
                    break;
                    case R.id.menu_showPass_itemView_delete: {
                        if (onRecyclerItemClickeListener != null) {
                            if (position != RecyclerView.NO_POSITION) {
                                onRecyclerItemClickeListener.onItemDeleteClicked(position);
                            }
                        }
                    }
                    break;
                }
                return false;
            }
        });
        popup.show();


    }


}
