package com.example.estructuracovid19nuble.ui.advice;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Locale;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class AdviceFragment extends Fragment {

    private static final int REQ_CODE_SPEECH_INPUT = 1;
    private AdviceViewModel adviceViewModel;
    private AWSAppSyncClient mAWSAppSyncClient;
    private RecyclerView recyclerView;
    private final String TAG = AdviceFragment.class.getSimpleName();
    private ArrayList<ListAdvicesQuery.Item> advices;
    private AdviceAdapter adapter;
    private MyApp myApp;

    private TextInputEditText txt_search;
    private ImageButton btn_back;
    private MaterialButton btn_voice;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myApp = MyApp.getInstance();

        adviceViewModel =
                ViewModelProviders.of(this).get(AdviceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_advice, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        adapter = new AdviceAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            public void onClick(View view, int position) {
                ListAdvicesQuery.Item item = advices.get(position);
                myApp.clicked_advices = position;
                AdviceFragmentDirections.ActionAdviceToAnAdviceFragment action = AdviceFragmentDirections.actionAdviceToAnAdviceFragment(position);
                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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

        btn_back = root.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btn_voice = root.findViewById(R.id.btn_voicesearch);
        btn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerVoiceInput();
            }
        });

        return root;
    }

    private void triggerVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "speech_prompt");
        try {
            getActivity().startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "speech_not_supported",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txt_search.setText(result.get(0));
                }
                break;
            }

        }
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
            //Just double data to test scroll view.
            //advices.addAll((ArrayList<ListAdvicesQueryicesQuery.Item>) advices.clone());
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
