package com.example.estructuracovid19nuble.ui.test;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.estructuracovid19nuble.R;
import com.example.estructuracovid19nuble.adapters.ResultViewModel;
import com.example.estructuracovid19nuble.databinding.ResultFragmentBinding;
import com.example.estructuracovid19nuble.utils.MyApp;
import com.example.estructuracovid19nuble.utils.Reply;

import java.util.ArrayList;

public class ResultFragment extends Fragment {

    private ResultViewModel mViewModel;
    private ResultFragmentBinding binding;
    private MyApp myApp;

    public static ResultFragment newInstance() {
        return new ResultFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myApp = MyApp.getInstance();

        binding = ResultFragmentBinding.inflate(inflater, container, false);

        binding.txtResult.setText(getScore(myApp.replies));

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return binding.getRoot();
    }

    private String getScore(ArrayList<Reply> replies) {
        //"Suspect" "Urgency" "Normal"
//        normal -> if  responses array has "no" or "ninguna"
//        suspected -> if responses array has "fiebre" or "dolor de garganta" or "tos seca"
//        urgency -> if there's no "ninguna" and array has "dificultad respiratoria" (edited)
        String result = replies.toString().toLowerCase();
        if (result.contains("ninguna") || result.contains("no")) {
            return getString(R.string.text_result_normal);
        }

        if (result.contains("fiebre") || result.contains("dolor de garganta") || result.contains("tos seca")) {
            return getString(R.string.text_result_suspect);
        }

        if (!result.contains("ninguna") && result.contains("dificultad respiratoria")) {
            return getString(R.string.text_result_urgency);
        }
        return getString(R.string.text_result_normal);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        // TODO: Use the ViewModel
    }

}
