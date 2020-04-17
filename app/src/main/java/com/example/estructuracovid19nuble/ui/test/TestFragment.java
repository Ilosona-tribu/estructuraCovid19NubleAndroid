package com.example.estructuracovid19nuble.ui.test;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.GetQuestionnaireQuery;
import com.amazonaws.amplify.generated.graphql.ListQuestionnairesQuery;
import com.amazonaws.amplify.generated.graphql.ListQuestionsQuery;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.estructuracovid19nuble.databinding.FragmentTestBinding;
import com.example.estructuracovid19nuble.utils.ClientFactory;
import com.example.estructuracovid19nuble.utils.MyApp;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class TestFragment extends Fragment {

    private AWSAppSyncClient mAWSAppSyncClient;
    private TestViewModel testViewModel;
    private FragmentTestBinding binding;
    private final String TAG = TestFragment.class.getSimpleName();
    private MyApp myApp;

    ArrayList<ListQuestionsQuery.Item> questions;
    //List of (set of questions)
    ArrayList<ListQuestionnairesQuery.Item> questionnaires;
    //First element of List quesionnaires
    ArrayList<ListQuestionnairesQuery.Item1> questions_1;
    //A set of questions
    private GetQuestionnaireQuery.GetQuestionnaire covid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        testViewModel =
                ViewModelProviders.of(this).get(TestViewModel.class);

        binding = FragmentTestBinding.inflate(inflater, container, false);

        binding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myApp.question_1.isEmpty()){
                    Toast.makeText(getActivity(), "No questions available, try to restart app", Toast.LENGTH_SHORT).show();
                    return;
                }
                NavDirections action = TestFragmentDirections.actionNavigationTestToQuestionairesFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });

        myApp = MyApp.getInstance();

        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!myApp.is_question_loaded) {
            queryQuestionnaires();
//        queryQuestions();
        }
    }


    private AWSAppSyncClient getAWS() {
        if (mAWSAppSyncClient == null) {
            mAWSAppSyncClient = ClientFactory.getInstance(getActivity());
        }
        return mAWSAppSyncClient;
    }

    public void queryQuestionnaires() {
        getAWS().query(ListQuestionnairesQuery.builder().build()).responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK).enqueue(questionairesCallback);
    }

    private GraphQLCall.Callback<ListQuestionnairesQuery.Data> questionairesCallback = new GraphQLCall.Callback<ListQuestionnairesQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListQuestionnairesQuery.Data> response) {
            questionnaires = new ArrayList<>(response.data().listQuestionnaires().items());
            for (ListQuestionnairesQuery.Item item : questionnaires) {
                Log.i(TAG, "questionnaires: " + item.title_questionnaire());
            }
            //Choose a questionnaires to survey:
            if (!questionnaires.isEmpty()) {
                String q_id = questionnaires.get(0).id();
                questions_1 = new ArrayList<>(questionnaires.get(0).question().items());
                if (!questions_1.isEmpty()) {
                    myApp.is_question_loaded = true;
                    myApp.question_1 = questions_1;
                }
                //queryQuestionnaireById(q_id);
            }

            for (ListQuestionnairesQuery.Item1 item : questions_1) {
                Log.e("a question: ", item.toString());
                Log.e("a question: ", item.question_type().toString());
                Log.e("a question: ", item.text_question());
                Log.e("a question: ", item.list_options() + " ");
            }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };

//Not necessary to query again a Questionnaires, we can extract directly from the list of questionnaires.
//    public void queryQuestionnaireById(String q_id) {
//        getAWS().query(GetQuestionnaireQuery.builder().id(q_id).build()).responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK).enqueue(questionnairesByIdCallback);
//    }
//
//    private GraphQLCall.Callback<GetQuestionnaireQuery.Data> questionnairesByIdCallback = new GraphQLCall.Callback<GetQuestionnaireQuery.Data>() {
//        @Override
//        public void onResponse(@Nonnull Response<GetQuestionnaireQuery.Data> response) {
//            covid = response.data().getQuestionnaire();
//            Log.e("set covid: ", covid.toString());
//            Log.e("set covid: ", covid.question().items().toString());
//        }
//
//        @Override
//        public void onFailure(@Nonnull ApolloException e) {
//            Log.e("ERROR", e.toString());
//        }
//    };

//    public void queryQuestions() {
//        getAWS().query(ListQuestionsQuery.builder().build())
//                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
//                .enqueue(questionsCallback);
//    }
//
//    private GraphQLCall.Callback<ListQuestionsQuery.Data> questionsCallback = new GraphQLCall.Callback<ListQuestionsQuery.Data>() {
//        @Override
//        public void onResponse(@Nonnull Response<ListQuestionsQuery.Data> response) {
//            questions = new ArrayList<>(response.data().listQuestions().items());
//            for (ListQuestionsQuery.Item item : questions) {
//                Log.i(TAG, item.toString());
//            }
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    binding.textQuestions.setText(questions.toString());
//                }
//            });
//        }
//
//        @Override
//        public void onFailure(@Nonnull ApolloException e) {
//            Log.e("ERROR", e.toString());
//        }
//    };
}
