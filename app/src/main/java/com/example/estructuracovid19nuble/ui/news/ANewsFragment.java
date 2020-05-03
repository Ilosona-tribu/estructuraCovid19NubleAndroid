package com.example.estructuracovid19nuble.ui.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.amplify.generated.graphql.ListAdvicesQuery;
import com.amazonaws.amplify.generated.graphql.ListNewssQuery;
import com.example.estructuracovid19nuble.MainActivity;
import com.example.estructuracovid19nuble.databinding.ANewsFragmentBinding;
import com.example.estructuracovid19nuble.ui.advice.AnAdviceFragmentArgs;
import com.example.estructuracovid19nuble.utils.MyApp;
import com.example.estructuracovid19nuble.utils.Settings;
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
        //Data from deeplink first.
        //ilosona://topic/a_news?timeid=xxx
        String timeId = ANewsFragmentArgs.fromBundle(getArguments()).getTimeid();
        ListNewssQuery.Item item = null;
        if (timeId != "0") {
            //load deeplink info
            String imageUrl = Settings.getKeyOnce(getActivity(), Settings.CAMPAIGN_IMAGE_PUSH_KEY, timeId);
            String imageIconUrl = Settings.getKeyOnce(getActivity(), Settings.CAMPAIGN_IMAGE_ICON_PUSH_KEY, timeId);
            String imageSmallIconUrl = Settings.getKeyOnce(getActivity(), Settings.CAMPAIGN_IMAGE_SMALL_ICON_PUSH_KEY, timeId);
            String title = Settings.getKeyOnce(getActivity(), Settings.NOTIFICATION_TITLE_PUSH_KEY, timeId);
            String message = Settings.getKeyOnce(getActivity(), Settings.NOTIFICATION_BODY_PUSH_KEY, timeId);
            Log.e("Message: ", timeId + title + message + imageIconUrl);

            item = new ListNewssQuery.Item("type_name", "id", title, message, message, imageUrl, imageIconUrl, -1, false, -1);
        } else{
            //Data from application
            myApp = ((MainActivity) getActivity()).getMyApp();
            if (!myApp.news.isEmpty()) {
                item = myApp.news.get(myApp.clicked_news);
            }
        }

        binding = ANewsFragmentBinding.inflate(inflater, container, false);
        binding.title.setText(item.title());
        binding.detail.setText(item.description());
        if (item.url_thumbnail_image() != null && !item.url_thumbnail_image().isEmpty()){
            Picasso.get().load(item.url_thumbnail_image()).into(binding.imgThumbnail);
        }
        if (item.url_background_image() != null && !item.url_background_image().isEmpty()){
            Picasso.get().load(item.url_background_image()).into(binding.imgDetail);
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
        mViewModel = ViewModelProviders.of(this).get(ANewsViewModel.class);
        // TODO: Use the ViewModel
    }

}
