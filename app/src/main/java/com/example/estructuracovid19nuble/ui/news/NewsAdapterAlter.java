package com.example.estructuracovid19nuble.ui.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.ListNewssQuery;
import com.example.estructuracovid19nuble.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapterAlter extends RecyclerView.Adapter<NewsAdapterAlter.ViewHolder> {

    private List<ListNewssQuery.Item> news = new ArrayList<>();
    private LayoutInflater mInflater;

    // data is passed into the constructor
    NewsAdapterAlter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.cell_item, parent, false);
        View view = mInflater.inflate(R.layout.cell_news, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListNewssQuery.Item item = news.get(position);
        holder.title.setText(item.title());
        holder.subTitle.setText(item.description());
        if (item.url_background_image() != null){
//            Glide.with(holder.itemView).load(item.url_background_image()).thumbnail(0.1f).into(holder.bgImg);
            Picasso.get().load(item.url_background_image()).into(holder.bgImg);
        } else{
//            Glide.with(holder.itemView).load("https://www.dw.com/image/52700426_304.jpg").thumbnail(0.1f).into(holder.bgImg);
            Picasso.get().load("https://www.dw.com/image/52700426_304.jpg").into(holder.bgImg);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return news.size();
    }

    // resets the list with a new set of data
    public void setItems(List<ListNewssQuery.Item> items) {
        news = items;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subTitle;
        ImageView bgImg;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_title);
            subTitle = itemView.findViewById(R.id.card_subtitle);
            bgImg = itemView.findViewById(R.id.card_bg);
        }
    }
}
