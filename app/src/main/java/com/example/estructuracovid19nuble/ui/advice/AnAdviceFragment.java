package com.example.estructuracovid19nuble.ui.advice;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.example.estructuracovid19nuble.utils.Settings;
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
        //Data from deeplink first.
        //ilosona://topic/an_advice?timeid=xxx
        String timeId = AnAdviceFragmentArgs.fromBundle(getArguments()).getTimeid();
        ListAdvicesQuery.Item item = null;
        if (timeId != "0") {
            //load deeplink info
            String imageUrl = Settings.getKeyOnce(getActivity(), Settings.CAMPAIGN_IMAGE_PUSH_KEY, timeId);
            String imageIconUrl = Settings.getKeyOnce(getActivity(), Settings.CAMPAIGN_IMAGE_ICON_PUSH_KEY, timeId);
            String imageSmallIconUrl = Settings.getKeyOnce(getActivity(), Settings.CAMPAIGN_IMAGE_SMALL_ICON_PUSH_KEY, timeId);
            String title = Settings.getKeyOnce(getActivity(), Settings.NOTIFICATION_TITLE_PUSH_KEY, timeId);
            String message = Settings.getKeyOnce(getActivity(), Settings.NOTIFICATION_BODY_PUSH_KEY, timeId);
            Log.e("Message: ", timeId + title + message + imageIconUrl);

            item = new ListAdvicesQuery.Item("type_name", "id", title, message, message, imageUrl, imageIconUrl, -1, false, -1);
        } else{
            //Data from application
            myApp = ((MainActivity) getActivity()).getMyApp();
            if (!myApp.advices.isEmpty()) {
                item = myApp.advices.get(myApp.clicked_advices);
            }
        }


        binding = AnAdviceFragmentBinding.inflate(inflater, container, false);
        if (item != null){
            if (item.detail()!= null) {
                binding.detail.setText(item.detail());
            }
            if (item.title()!= null){
                binding.title.setText(item.title());
            }
            if (item.url_thumbnail_image() != null && !item.url_thumbnail_image().isEmpty()){
                Picasso.get().load(item.url_thumbnail_image()).into(binding.cardThumbnail);
            }
            if (item.url_background_image() != null && !item.url_background_image().isEmpty()){
                Picasso.get().load(item.url_background_image()).into(binding.bgImg);
            }
        }

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