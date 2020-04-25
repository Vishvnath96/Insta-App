package com.example.vps.ui.data.repository;


import android.app.Application;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.vps.ui.data.AppExecuters;
import com.example.vps.ui.data.local.db.NewsArticleDatabase;
import com.example.vps.ui.data.local.db.dao.NewsArticleDao;
import com.example.vps.ui.data.local.db.entity.NewsSavedSuggestion;
import com.example.vps.ui.data.remote.api.NetworkService;
import com.example.vps.ui.data.remote.api.NewsServiceGenerator;
import com.example.vps.ui.data.remote.response.NewsResponse;
import com.example.vps.ui.util.NetworkUtil;



public class NewsRepository {
    private NewsArticleDao newsArticleDao;
    private NetworkService networkService;
    private NewsServiceGenerator newsServiceGenerator;
    private Context context;
    private MutableLiveData<NewsResponse> newsListLiveData = new MutableLiveData<>();
    private AppExecuters mExecutor;


    public NewsRepository(Application context) {
        NewsArticleDatabase db = NewsArticleDatabase.getDatabase(context);
        newsServiceGenerator = NewsServiceGenerator.getInstance(context);
        networkService = NewsServiceGenerator.createService(NetworkService.class);
        this.newsArticleDao = db.newsArticleDao();
        this.mExecutor = new AppExecuters();
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public LiveData<NewsResponse> getNewsFromSource(String section, String orderBy, int pageSize) {
        if (!NetworkUtil.isNetworkConnected(context)) {
            return getResponseFromDb();
        }
        final LiveData<NewsResponse> listResults = newsServiceGenerator.getNews(section, orderBy, pageSize);
        listResults.observeForever(new Observer<NewsResponse>() {
            @Override
            public void onChanged(NewsResponse results) {
                if (results != null) {
                    newsListLiveData.setValue(results);
                }
            }
        });
        return newsListLiveData;
    }

    private MutableLiveData<NewsResponse> getResponseFromDb() {
        MutableLiveData<NewsResponse> newsResponse = new MutableLiveData<>();
        mExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                LiveData<NewsSavedSuggestion> listResults = newsArticleDao.getNewsArticles();
                if (listResults.getValue() != null && listResults.getValue().getResults() != null) {
                    newsResponse.setValue(listResults.getValue().getResults());
                }
            }
        });
        return newsResponse;
    }

    public void saveNewsInDb(NewsResponse newsResponse) {
        mExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                newsArticleDao.clearAndCacheArticles(NetworkUtil.getNewsSavedSuggestion(newsResponse));
            }
        });
    }

    public void deleteFromDb() {
        mExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                newsArticleDao.clearAllArticles();
            }
        });
    }

}


