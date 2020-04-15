package com.example.estructuracovid19nuble.utils;

import android.app.Application;

import com.amazonaws.amplify.generated.graphql.ListAdvicesQuery;
import com.amazonaws.amplify.generated.graphql.ListNewssQuery;
import com.amazonaws.amplify.generated.graphql.ListQuestionnairesQuery;

import java.util.ArrayList;

public class MyApp extends Application {
    private static MyApp mInstance;
    public ArrayList<ListNewssQuery.Item> news;
    public ArrayList<ListAdvicesQuery.Item> advices;
    public int clicked_news;
    public int clicked_advices;
    public ArrayList<ListQuestionnairesQuery.Item1> question_1;
    public boolean is_question_loaded = false;
    public ArrayList<Reply> replies;

    //Volley variables
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApp getInstance() {
        return mInstance;
    }
    public ListNewssQuery.Item get_current_news(){
        if (news != null){
            return news.get(clicked_news);
        }
        return  null;
    }
}
