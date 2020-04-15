package com.example.estructuracovid19nuble.ui.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.amplify.generated.graphql.ListNewssQuery;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.estructuracovid19nuble.R;
import com.example.estructuracovid19nuble.utils.ClientFactory;
import com.example.estructuracovid19nuble.utils.MyApp;
import com.example.estructuracovid19nuble.utils.RecyclerTouchListener;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class NewsFragment extends Fragment {

//    private NewsViewModel newsViewModel;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView recyclerView;
    private final String TAG = NewsFragment.class.getSimpleName();
    private AWSAppSyncClient mAWSAppSyncClient;
    private ArrayList<ListNewssQuery.Item> news;
    private NewsAdapterAlter adapter;
    private MyApp myApp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        myApp = MyApp.getInstance();
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        adapter = new NewsAdapterAlter(getActivity());
        recyclerView.setAdapter(adapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            public void onClick(View view, int position) {
                ListNewssQuery.Item item = news.get(position);
                //Toast.makeText(getActivity(), item.detail() + " is selected!", Toast.LENGTH_SHORT).show();
                myApp.clicked_news = position;
                NewsFragmentDirections.ActionNewsToANews action = NewsFragmentDirections.actionNewsToANews(position);
                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        queryNews();
    }

    public void queryNews() {
        if (mAWSAppSyncClient == null) {
            mAWSAppSyncClient = ClientFactory.getInstance(getActivity().getApplicationContext());
        }
        mAWSAppSyncClient.query(ListNewssQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(newsCallback);
    }

    private GraphQLCall.Callback<ListNewssQuery.Data> newsCallback = new GraphQLCall.Callback<ListNewssQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListNewssQuery.Data> response) {
            news = new ArrayList<>(response.data().listNewss().items());
            for (ListNewssQuery.Item item: news){
                Log.i(TAG, item.description() + " " + item.toString());
            }
            //news.addAll((ArrayList<ListNewssQuery.Item>) news.clone());
            myApp.news = news;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setItems(news);
                    adapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };

}

