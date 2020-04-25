package com.example.vps.ui.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class Results {
    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("sectionId")
    private String sectionId;

    @SerializedName("sectionName")
    private String sectionName;

    @SerializedName("webPublicationDate")
    private String webPublicationDate;

    @SerializedName("webTitle")
    private String webTitle;

    @SerializedName("webUrl")
    private String webUrl;

    @SerializedName("apiUrl")
    private String apiUrl;

    @SerializedName("isHosted")
    private boolean isHosted;

    @SerializedName("pillarId")
    private String pillarId;

    @SerializedName("fields")
    private Fields fields;

    @SerializedName("pillarName")
    private String pillarName;

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionId() {
        return this.sectionId;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionName() {
        return this.sectionName;
    }

    public void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    public String getWebPublicationDate() {
        return this.webPublicationDate;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getWebTitle() {
        return this.webTitle;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebUrl() {
        return this.webUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiUrl() {
        return this.apiUrl;
    }

    public void setIsHosted(boolean isHosted) {
        this.isHosted = isHosted;
    }

    public boolean getIsHosted() {
        return this.isHosted;
    }

    public void setPillarId(String pillarId) {
        this.pillarId = pillarId;
    }

    public String getPillarId() {
        return this.pillarId;
    }

    public void setPillarName(String pillarName) {
        this.pillarName = pillarName;
    }

    public String getPillarName() {
        return this.pillarName;
    }
}
