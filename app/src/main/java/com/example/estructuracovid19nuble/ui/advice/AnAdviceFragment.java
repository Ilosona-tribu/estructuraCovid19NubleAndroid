package com.example.estructuracovid19nuble.ui.advice;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.amplify.generated.graphql.ListAdvicesQuery;
import com.amazonaws.amplify.generated.graphql.ListNewssQuery;
import com.example.estructuracovid19nuble.MainActivity;
import com.example.estructuracovid19nuble.R;
import com.example.estructuracovid19nuble.databinding.ANewsFragmentBinding;
import com.example.estructuracovid19nuble.databinding.AnAdviceFragmentBinding;
import com.example.estructuracovid19nuble.utils.MyApp;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

public class AnAdviceFragment extends Fragment {

    private AnAdviceViewModel mViewModel;
    private AnAdviceFragmentBinding binding;
    private MyApp myApp;

    public static AnAdviceFragment newInstance() {
        return new AnAdviceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myApp = ((MainActivity) getActivity()).getMyApp();
        ListAdvicesQuery.Item item = myApp.advices.get(myApp.clicked_advices);

        binding = AnAdviceFragmentBinding.inflate(inflater, container, false);
        binding.detail.setText(item.detail());
        binding.title.setText(item.title());
        Picasso.get().load(item.url_thumbnail_image()).into(binding.cardThumbnail);
        Picasso.get().load(item.url_background_image()).into(binding.bgImg);

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
        mViewModel = ViewModelProviders.of(this).get(AnAdviceViewModel.class);
        // TODO: Use the ViewModel
    }

}