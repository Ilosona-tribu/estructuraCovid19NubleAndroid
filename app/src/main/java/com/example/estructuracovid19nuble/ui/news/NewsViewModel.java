package com.example.estructuracovid19nuble.ui.news;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewsViewModel extends ViewModel {

    MutableLiveData<ArrayList<NewsDataModel>> newsLiveData;
    ArrayList<NewsDataModel> newsDataModelArrayList;

    public NewsViewModel() {
        newsLiveData = new MutableLiveData<>();
        // call your Rest API in init method
        init();
    }

    public MutableLiveData<ArrayList<NewsDataModel>> getUserMutableLiveData() {
        return newsLiveData;
    }

    public void init(){
        populateList();
        newsLiveData.setValue(newsDataModelArrayList);
    }
    public void populateList(){

        NewsDataModel news = new NewsDataModel();
        news.setTitle("Darknight");
        news.setSubTitle("Best rating movie");

        newsDataModelArrayList = new ArrayList<>();
        newsDataModelArrayList.add(news);
        newsDataModelArrayList.add(news);

    }
}