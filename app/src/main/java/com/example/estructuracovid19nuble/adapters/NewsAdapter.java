package com.example.estructuracovid19nuble.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.estructuracovid19nuble.R;
import com.example.estructuracovid19nuble.ui.news.NewsDataModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<NewsDataModel> dataModelList;
    private Context mContext;

    // View holder class whose objects represent each list item

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView subTitleTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.card_title);
            subTitleTextView = itemView.findViewById(R.id.card_subtitle);
        }

        public void bindData(NewsDataModel dataModel, Context context) {
            titleTextView.setText(dataModel.getTitle());
            subTitleTextView.setText(dataModel.getSubTitle());
        }
    }
    public NewsAdapter(List<NewsDataModel> modelList, Context context) {
        dataModelList = modelList;
        mContext = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_item, parent, false);
        // Return a new view holder
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Bind data for the item at position
        holder.bindData(dataModelList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return dataModelList.size();
    }
}