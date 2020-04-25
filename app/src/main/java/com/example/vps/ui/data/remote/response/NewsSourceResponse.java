package com.example.vps.ui.data.remote.response;


import com.google.gson.annotations.SerializedName;

public class NewsSourceResponse {
    @SerializedName("response")
    private NewsResponse response;

    public NewsResponse getResponse() {
        return response;
    }

    public void setResponse(NewsResponse response) {
        this.response = response;
    }

}
