package com.example.estructuracovid19nuble.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.amplify.generated.graphql.ListNewssQuery;
import com.example.estructuracovid19nuble.MainActivity;
import com.example.estructuracovid19nuble.databinding.ANewsFragmentBinding;
import com.example.estructuracovid19nuble.utils.MyApp;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ANewsFragment extends Fragment {

    private ANewsViewModel mViewModel;
    private MyApp myApp;
    private ANewsFragmentBinding binding;

    public static ANewsFragment newInstance() {
        return new ANewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myApp = ((MainActivity) getActivity()).getMyApp();
        ListNewssQuery.Item item = myApp.news.get(myApp.clicked_news);

        binding = ANewsFragmentBinding.inflate(inflater, container, false);
        binding.title.setText(item.title());
        binding.detail.setText(item.description());
        Picasso.get().load(item.url_thumbnail_image()).into(binding.imgThumbnail);
        Picasso.get().load(item.url_background_image()).into(binding.imgDetail);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ANewsViewModel.class);
        // TODO: Use the ViewModel
    }

}
