package com.example.estructuracovid19nuble.ui.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.estructuracovid19nuble.databinding.TestSelectFragmentBinding;
import com.example.estructuracovid19nuble.utils.MyApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class TestSelect extends Fragment {

    private TestSelectViewModel mViewModel;
    private TestSelectFragmentBinding binding;
    private MyApp myApp;

    public static TestSelect newInstance() {
        return new TestSelect();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TestSelectFragmentBinding.inflate(inflater, null, false);

        binding.btnTestMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeSurvey(v);
            }
        });
        binding.btnTestOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeSurvey(v);
            }
        });

        myApp = MyApp.getInstance();

        return binding.getRoot();
    }
    private void takeSurvey(View v){
        if (myApp.question_1.isEmpty()){
            Toast.makeText(getActivity(), "No questions available, try to restart app!", Toast.LENGTH_SHORT).show();
            return;
        }
        NavDirections action = TestSelectDirections.actionTestSelectToQuestionairesFragment();
        Navigation.findNavController(v).navigate(action);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TestSelectViewModel.class);
        // TODO: Use the ViewModel
    }

}
