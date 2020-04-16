package com.example.estructuracovid19nuble.ui.advice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.ListAdvicesQuery;
import com.example.estructuracovid19nuble.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AdviceAdapter extends RecyclerView.Adapter<com.example.estructuracovid19nuble.ui.advice.AdviceAdapter.ViewHolder> {

    private List<ListAdvicesQuery.Item> advices = new ArrayList<>();
    private LayoutInflater mInflater;

    // data is passed into the constructor
    AdviceAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public com.example.estructuracovid19nuble.ui.advice.AdviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell_advice, parent, false);
        return new com.example.estructuracovid19nuble.ui.advice.AdviceAdapter.ViewHolder(view);
    }



    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(com.example.estructuracovid19nuble.ui.advice.AdviceAdapter.ViewHolder holder, int position) {
        ListAdvicesQuery.Item item = advices.get(position);

        holder.title.setText(item.title());
        if (item.url_background_image() != null) {
            Picasso.get().load(item.url_background_image()).into(holder.bgImg);
        } else {
            Picasso.get().load("https://www.dw.com/image/52700426_304.jpg").into(holder.bgImg);
        }
        if(item.url_thumbnail_image()!=null){
            Picasso.get().load(item.url_thumbnail_image()).into(holder.bgImg);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return advices.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    // resets the list with a new set of data
    public void setItems(List<ListAdvicesQuery.Item> items) {
        advices = items;
    }

    public void update(ArrayList<ListAdvicesQuery.Item> filter_list) {
        advices = filter_list;
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView bgImg;
        ImageView thumbImg;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_title);
            bgImg = itemView.findViewById(R.id.card_bg);
            thumbImg = itemView.findViewById(R.id.card_thumbnail);
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
