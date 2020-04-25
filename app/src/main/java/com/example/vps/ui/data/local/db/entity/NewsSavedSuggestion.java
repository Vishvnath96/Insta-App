package com.example.vps.ui.data.local.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.vps.ui.data.remote.response.NewsResponse;

import java.util.Date;

@Entity(tableName = "newsSavedSuggestion")
public class NewsSavedSuggestion {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "createdAt")
    private Date createdAt;

    private NewsResponse results;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public NewsResponse getResults() {
        return results;
    }

    public void setResults(NewsResponse results) {
        this.results = results;
    }

}
