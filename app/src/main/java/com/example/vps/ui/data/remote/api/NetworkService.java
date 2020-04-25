package com.example.vps.ui.data.remote.api;

import com.example.vps.ui.data.remote.response.NewsSourceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkService {


    @GET("search")
    Call<NewsSourceResponse> getNewsJson(
            // Type should always be "Article" for the purpose of this demo
            @Query("section") String section,
            // Show-blocks should always be "Body" to get the main text of an article
            @Query("order-by") String orderBy,
            @Query("page-size") int pageSize,
            @Query("show-fields") String field,
            @Query("format") String format,
            @Query("api-key") String apiKey);


    @GET("search")
    Call<NewsSourceResponse> getDefaultNewsJson(
            // Show-blocks should always be "Body" to get the main text of an article
            @Query("order-by") String orderBy,
            @Query("page-size") int pageSize,
            @Query("show-fields") String field,
            @Query("format") String format,
            // API key should always be constant
            @Query("api-key") String apiKey);

}
