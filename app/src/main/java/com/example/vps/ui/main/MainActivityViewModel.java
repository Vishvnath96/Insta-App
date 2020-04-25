package com.example.vps.ui.main;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.vps.ui.data.remote.response.NewsResponse;
import com.example.vps.ui.data.repository.NewsRepository;


public class MainActivityViewModel extends AndroidViewModel {
    public static final int PAGE_SIZE = 10;
    private static final String ORDER_BY = "newest";

    public MutableLiveData<NewsResponse> newsResult;
    NewsRepository newsRepository;
    private String section;



    @RequiresApi(api = Build.VERSION_CODES.M)
    public MainActivityViewModel(@NonNull Application application, String section) {
        super(application);
        newsRepository = new NewsRepository(application);
        this.section = section;
        initData(PAGE_SIZE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initData(int pageSize){
        newsResult = (MutableLiveData<NewsResponse>) newsRepository.getNewsFromSource(section, ORDER_BY, pageSize);
    }

    public void clearAndSaveNewData(){
        if(newsResult.getValue() != null && newsResult.getValue().getResults().size() > 0){
            newsRepository.saveNewsInDb(newsResult.getValue());
        }
    }

}
