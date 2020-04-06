package com.example.estructuracovid19nuble.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.estructuracovid19nuble.R;
import com.example.estructuracovid19nuble.adapters.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        newsViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<NewsDataModel>>() {
            @Override
            public void onChanged(ArrayList<NewsDataModel> newsDataModels) {
                recyclerView.setHasFixedSize(true);

                // use a linear layout manager

                mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);

                // specify an adapter and pass in our data model list

                mAdapter = new NewsAdapter(newsDataModels, getActivity());
                recyclerView.setAdapter(mAdapter);

            }
        });

      /*  newsViewModel =
                ViewModelProviders.of(this).get(NewsViewModel.class);
        final TextView textView = root.findViewById(R.id.text_news);
        newsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
*/



        return root;
    }
}
