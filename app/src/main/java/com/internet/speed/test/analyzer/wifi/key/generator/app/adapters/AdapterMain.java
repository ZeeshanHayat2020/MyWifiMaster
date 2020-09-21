package com.internet.speed.test.analyzer.wifi.key.generator.app.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.interfaces.OnRecyclerItemClickeListener;
import com.internet.speed.test.analyzer.wifi.key.generator.app.models.ModelMain;

import java.util.List;


public class AdapterMain extends RecyclerView.Adapter<AdapterMain.MyViewHolder> {

    private Context context;
    private List<ModelMain> modelMainList;
    private OnRecyclerItemClickeListener onRecyclerItemClickeListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickeListener onRecyclerItemClickeListener) {
        this.onRecyclerItemClickeListener = onRecyclerItemClickeListener;
    }

    public AdapterMain(Context context, List<ModelMain> modelMainList) {
        this.context = context;
        this.modelMainList = modelMainList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;


        public MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.itemView_main_tv);


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
        holder.textView.setText(modelMainList.get(position).getTitle());
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
        return modelMainList.size();
    }


}
