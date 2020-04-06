package com.example.estructuracovid19nuble.ui.advice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.estructuracovid19nuble.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class AdviceFragment extends Fragment {

    private AdviceViewModel adviceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adviceViewModel =
                ViewModelProviders.of(this).get(AdviceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_advice, container, false);
        final TextView textView = root.findViewById(R.id.text_advice);
        adviceViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
