package com.example.estructuracovid19nuble.ui.advice;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.ListAdvicesQuery;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.estructuracovid19nuble.R;
import com.example.estructuracovid19nuble.utils.ClientFactory;
import com.example.estructuracovid19nuble.utils.MyApp;
import com.example.estructuracovid19nuble.utils.RecyclerTouchListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class AdviceFragmentDetail extends Fragment {

    private AdviceViewModel adviceViewModel;
    private AWSAppSyncClient mAWSAppSyncClient;
    private RecyclerView recyclerView;
    private final String TAG = AdviceFragmentDetail.class.getSimpleName();
    private ArrayList<ListAdvicesQuery.Item> advices;
    private AdviceAdapter adapter;
    private MyApp myApp;

    private TextInputEditText txt_search;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myApp = MyApp.getInstance();

        adviceViewModel =
                ViewModelProviders.of(this).get(AdviceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_advice_detail, container, false);
//        View root = inflater.inflate(R.layout.fragment_map, container, false);


        txt_search = root.findViewById(R.id.txt_search);
        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, s.toString());
                filter(s.toString());

            }
        });

        return root;
    }

    private void filter(String text) {
        if (advices == null) {
            return;
        }
        ArrayList<ListAdvicesQuery.Item> filter_list = new ArrayList<>();
        for (ListAdvicesQuery.Item item : advices) {
            if (item.title().toLowerCase().contains(text.toLowerCase())) {
                filter_list.add(item);
            }
        }
        adapter.update(filter_list);
        myApp.advices = filter_list;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (txt_search != null && !txt_search.getText().toString().isEmpty()) {
            filter(txt_search.getText().toString());
        }
        queryAdvices();
    }

    public void queryAdvices() {
        if (mAWSAppSyncClient == null) {
            mAWSAppSyncClient = ClientFactory.getInstance(getActivity().getApplicationContext());
        }
        mAWSAppSyncClient.query(ListAdvicesQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(advicesCallback);
    }

    private GraphQLCall.Callback<ListAdvicesQuery.Data> advicesCallback = new GraphQLCall.Callback<ListAdvicesQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListAdvicesQuery.Data> response) {
            advices = new ArrayList<>(response.data().listAdvices().items());
            for (ListAdvicesQuery.Item item : advices) {
                Log.i(TAG, item.toString());
            }
            advices.addAll((ArrayList<ListAdvicesQuery.Item>) advices.clone());
            myApp.advices = advices;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (txt_search != null && !txt_search.getText().toString().isEmpty()) {
                        filter(txt_search.getText().toString());
                    } else {
                        adapter.setItems(advices);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };
}
