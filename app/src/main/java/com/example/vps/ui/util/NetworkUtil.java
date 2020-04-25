package com.example.vps.ui.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.vps.ui.data.local.db.entity.NewsSavedSuggestion;
import com.example.vps.ui.data.remote.response.NewsResponse;

import java.util.Date;

public class NetworkUtil {


    public static NewsSavedSuggestion getNewsSavedSuggestion(NewsResponse newsResponse){
        NewsSavedSuggestion savedSuggestion = new NewsSavedSuggestion();
        savedSuggestion.setCreatedAt(new Date());
        savedSuggestion.setResults(newsResponse);
        return savedSuggestion;
    }

    public static NewsResponse getResponseFromSavedSuggestion(NewsSavedSuggestion newsSavedSuggestion){
        if(newsSavedSuggestion != null){
            return newsSavedSuggestion.getResults();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService((Context.CONNECTIVITY_SERVICE));

        return cm.getActiveNetwork() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
