package com.example.vps.ui.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class Fields {
    @SerializedName("trailText")
    private String trailText;
    @SerializedName("thumbnail")
    private String thumbnail;

    public String getTrailText() {
        return trailText;
    }

    public void setTrailText(String trailText) {
        this.trailText = trailText;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
