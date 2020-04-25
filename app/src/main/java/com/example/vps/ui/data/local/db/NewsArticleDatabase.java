package com.example.vps.ui.data.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.vps.ui.data.local.db.dao.NewsArticleDao;
import com.example.vps.ui.data.local.db.entity.NewsSavedSuggestion;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NewsSavedSuggestion.class}, version = 1, exportSchema = false)
@TypeConverters({NewsSavedSuggestionConverter.class, TimeStampConverter.class})
public abstract class NewsArticleDatabase extends RoomDatabase {

    public abstract NewsArticleDao newsArticleDao();
    private static volatile NewsArticleDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static NewsArticleDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NewsArticleDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NewsArticleDatabase.class, "news_database.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
