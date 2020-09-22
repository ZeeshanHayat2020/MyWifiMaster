package com.internet.speed.test.analyzer.wifi.key.generator.app.allRouterPassword;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.internet.speed.test.analyzer.wifi.key.generator.app.R;

import java.util.ArrayList;
import java.util.List;

public class RouterAdapterClass extends RecyclerView.Adapter<RouterAdapterClass.MyViewHolder> implements Filterable {

    ArrayList<All_Router_Model_Class> arrayList;
    ArrayList<All_Router_Model_Class> TempArrayList;
    Context context;

    public RouterAdapterClass(ArrayList<All_Router_Model_Class> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        TempArrayList = new ArrayList<>();
        TempArrayList.addAll(arrayList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.router_item_rcv, parent , false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.model.setText(arrayList.get(position).getBrand());
        holder.brand.setText(arrayList.get(position).getModel());
        holder.protocol.setText(arrayList.get(position).getProtocol());
        holder.username.setText(arrayList.get(position).getUsername());
        holder.password.setText(arrayList.get(position).getPassword());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void updateData(ArrayList<All_Router_Model_Class> viewModels) {
        arrayList.clear();
        arrayList = viewModels;
        notifyDataSetChanged();
    }
    @Override
    public Filter getFilter() {
        return ExampleFilter;
    }
    private Filter ExampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<All_Router_Model_Class> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() ==  0)
            {
                filteredList.addAll(TempArrayList);
            }else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (All_Router_Model_Class item : TempArrayList)
                {
                    if (item.getBrand().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item);
                    }
                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            arrayList.clear();
            arrayList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView brand , model , protocol , username , password;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            brand = itemView.findViewById(R.id.brand);
            model = itemView.findViewById(R.id.model);
            protocol = itemView.findViewById(R.id.protocol);
            username = itemView.findViewById(R.id.username);
            password = itemView.findViewById(R.id.password);

        }
    }
}
