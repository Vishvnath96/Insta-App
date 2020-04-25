package com.example.vps.ui.data.local.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.vps.ui.data.local.db.entity.NewsSavedSuggestion;

@Dao
public interface NewsArticleDao {

    /**
     * Insert articles into the table
     */
    @Insert
    void insert(NewsSavedSuggestion newsSavedSuggestion);

    @Query("DELETE FROM newsSavedSuggestion")
    void clearAllArticles();

    @Query("SELECT * FROM newsSavedSuggestion")
    LiveData<NewsSavedSuggestion> getNewsArticles();

    @Transaction
    public default void clearAndCacheArticles(NewsSavedSuggestion articles){
       clearAllArticles();
       insert(articles);
    }


}
