package com.example.estructuracovid19nuble.ui.test;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.estructuracovid19nuble.R;
import com.example.estructuracovid19nuble.adapters.QuestionAdapter;
import com.example.estructuracovid19nuble.adapters.QuestionViewpager2Adapter;
import com.example.estructuracovid19nuble.utils.MyApp;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

public class QuestionairesFragment extends Fragment {

    private MyApp myApp;
    private QuestionairesViewModel mViewModel;
    private ViewPager pager;
    private QuestionAdapter adapter;

    private ViewPager2 pager2;
    private QuestionViewpager2Adapter adapter2;

    private MaterialButton btn_next;
    private ImageButton btn_back;
    private MaterialButton btn_cancel;
    private boolean clickable = true;

    public static QuestionairesFragment newInstance() {
        return new QuestionairesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myApp = MyApp.getInstance();
        adapter = new QuestionAdapter(getActivity(), myApp.question_1);
        adapter2 = new QuestionViewpager2Adapter(getActivity(), myApp.question_1);

        View root = inflater.inflate(R.layout.questionaires_fragment, container, false);
//        pager = root.findViewById(R.id.pager_1);
//        pager.setAdapter(adapter);

        pager2 = root.findViewById(R.id.pager_2);
        pager2.setAdapter(adapter2);
        pager2.setUserInputEnabled(false);

        btn_next = root.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("QuestionView: ", pager2.getCurrentItem() + "/" + adapter.getCount());
                if (pager2.getCurrentItem() == adapter.getCount() - 1) {
                    //Finish survey, trigger result fragment.
                    if (clickable){
                        clickable = false;
                        myApp.replies = adapter.getReplies();
                        NavDirections action = QuestionairesFragmentDirections.actionQuestionairesToResult();
                        Navigation.findNavController(v).navigate(action);
                    }
                } else {
                    pager2.setCurrentItem(pager2.getCurrentItem() + 1);
                }
            }
        });

        btn_back = root.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("QuestionView: ", pager2.getCurrentItem() + "/" + adapter.getCount());
                if (pager2.getCurrentItem() > 0) {
                    pager2.setCurrentItem(pager2.getCurrentItem() - 1);
                } else{
                    getActivity().onBackPressed();
                }

            }
        });

        btn_cancel = root.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

//        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e("onPageScrolled ", position +" ");
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.e("onpageselected ", position +" ");
//                if (position == adapter.getCount() -1 ){
//                    btn_next.setText("Finish");
//                } else {
//                    btn_next.setText("Next");
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                Log.e("onstatechanged ", state +" ");
//
//            }
//        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(QuestionairesViewModel.class);
        // TODO: Use the ViewModel
    }

}
