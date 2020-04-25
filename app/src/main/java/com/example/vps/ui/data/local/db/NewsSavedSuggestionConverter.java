package com.example.vps.ui.data.local.db;

import androidx.room.TypeConverter;

import com.example.vps.ui.data.remote.response.NewsResponse;
import com.example.vps.ui.util.common.GsonUtils;

public class NewsSavedSuggestionConverter {

    @TypeConverter
    public static NewsResponse fromSuggestion(String value) {
        NewsResponse result;
        if (value == null) {
            result = null;
        } else {
            result = GsonUtils.getInstance().deserializeJSON(value, NewsResponse.class);
        }
        return result;
    }

    @TypeConverter
    public static String suggestionToString(NewsResponse newsResponse) {
        return GsonUtils.getInstance().serializeToJson(newsResponse);
    }

}