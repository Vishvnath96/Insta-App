package com.example.vps.ui.data.remote.response;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("userTier")
    private String userTier;
    @SerializedName("total")
    private int total;
    @SerializedName("startIndex")
    private int startIndex;
    @SerializedName("pageSize")
    private int pageSize;
    @SerializedName("currentPage")
    private int currentPage;
    @SerializedName("pages")
    private int pages;
    @SerializedName("orderBy")
    private String orderBy;
    @SerializedName("results")
    private List<Results> results;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setUserTier(String userTier) {
        this.userTier = userTier;
    }

    public String getUserTier() {
        return this.userTier;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return this.total;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getStartIndex() {
        return this.startIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPages() {
        return this.pages;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public List<Results> getResults() {
        return this.results;
    }
}