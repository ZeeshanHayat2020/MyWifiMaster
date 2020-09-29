package com.internet.speed.test.analyzer.wifi.key.generator.app.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.internet.speed.test.analyzer.wifi.key.generator.app.R;
import com.internet.speed.test.analyzer.wifi.key.generator.app.database.MyPreferences;
import com.internet.speed.test.analyzer.wifi.key.generator.app.interfaces.OnRecyclerItemClickeListener;
import com.internet.speed.test.analyzer.wifi.key.generator.app.models.ModelMain;

import java.util.List;


public class AdapterMain extends RecyclerView.Adapter<AdapterMain.MyViewHolder> {

    private Context context;

    private List<ModelMain> modelMainList;
    private OnRecyclerItemClickeListener onRecyclerItemClickeListener;
    private MyPreferences myPreferences;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickeListener onRecyclerItemClickeListener) {
        this.onRecyclerItemClickeListener = onRecyclerItemClickeListener;
    }

    public AdapterMain(Context context, List<ModelMain> modelMainList, MyPreferences myPreferences) {
        this.context = context;
        this.modelMainList = modelMainList;
        this.myPreferences = myPreferences;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView newFeaturesView;
        public ImageView imageView;
        public TextView textView;


        public MyViewHolder(View view) {
            super(view);
            newFeaturesView = view.findViewById(R.id.itemView_main_newView);
            textView = view.findViewById(R.id.itemView_main_tv);
            imageView = view.findViewById(R.id.itemView_main_iv);


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
        holder.imageView.setImageResource(modelMainList.get(position).getImgId());
        holder.textView.setText(modelMainList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != RecyclerView.NO_POSITION) {
                    onRecyclerItemClickeListener.onItemClicked(position);
                }
            }
        });


        if (!myPreferences.isNewFeatureChecked()) {
            String title = modelMainList.get(position).getTitle();
            if (title.equals(context.getResources().getString(R.string.wifi_strength))
                    || title.equals(context.getResources().getString(R.string.net_block))
                    || title.equals(context.getResources().getString(R.string.defaul_router_pass))
                    || title.equals(context.getResources().getString(R.string.app_usage))) {
                holder.newFeaturesView.setVisibility(View.VISIBLE);
            }
        }


    }


    @Override
    public int getItemCount() {
        return modelMainList.size();
    }


}
