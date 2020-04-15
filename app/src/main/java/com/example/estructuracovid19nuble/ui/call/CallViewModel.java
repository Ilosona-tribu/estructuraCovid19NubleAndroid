package com.example.estructuracovid19nuble.ui.call;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CallViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CallViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}