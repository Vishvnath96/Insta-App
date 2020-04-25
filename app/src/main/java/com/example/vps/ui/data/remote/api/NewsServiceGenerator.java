package com.example.vps.ui.data.remote.api;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.vps.ui.data.remote.response.NewsResponse;
import com.example.vps.ui.data.remote.response.NewsSourceResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static com.example.vps.ui.util.common.Constants.API_KEY;
import static com.example.vps.ui.util.common.Constants.FORMAT;
import static com.example.vps.ui.util.common.Constants.SHOW_FIELDS;

public class NewsServiceGenerator {

    private static final String BASE_URL = "https://content.guardianapis.com/";
    private static final Object LOCK = new Object();
    private static NetworkService sNewsApi;
    private static NewsServiceGenerator sInstance;
    public boolean isCacheResponse;


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }


    public static NewsServiceGenerator getInstance(Context context) {
        if (sInstance == null || sNewsApi == null) {
            synchronized (LOCK) {
                // 5 MB of cache
                Cache cache = new Cache(context.getApplicationContext().getCacheDir(), 5 * 1024 * 1024);

                // Used for cache connection
                Interceptor networkInterceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        // set max-age and max-stale properties for cache header
                        CacheControl cacheControl = new CacheControl.Builder()
                                .maxAge(5, TimeUnit.MINUTES)
                                .maxStale(3, TimeUnit.DAYS)
                                .build();
                        return chain.proceed(chain.request())
                                .newBuilder()
                                .removeHeader("Pragma")
                                .header("Cache-Control", cacheControl.toString())
                                .build();
                    }
                };

                // For logging
                HttpLoggingInterceptor loggingInterceptor =
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);


                // Building OkHttp client
                OkHttpClient client = new OkHttpClient.Builder()
                        .cache(cache)
                        .addNetworkInterceptor(networkInterceptor)
                        .addInterceptor(loggingInterceptor)
                        .build();

                // Configure GSON
                Gson gson = new GsonBuilder()
                        .create();

                // Retrofit Builder
                Retrofit.Builder builder =
                        new Retrofit
                                .Builder()
                                .baseUrl(BASE_URL)
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create(gson));
                // Set NewsApi instance
                sNewsApi = builder.build().create(NetworkService.class);
                sInstance = new NewsServiceGenerator();
            }
        }
        return sInstance;
    }

    public LiveData<NewsResponse> getNews(String section, String orderBy, int pageSize) {
        MutableLiveData<NewsResponse> newsLiveData = new MutableLiveData<>();
        Call<NewsSourceResponse> call = null;
        NetworkService client = NewsServiceGenerator.sNewsApi;

        if (section != null) {
            call = client.getNewsJson(section, orderBy, pageSize, SHOW_FIELDS, FORMAT, API_KEY);
        } else {
            call = client.getDefaultNewsJson(orderBy, pageSize, SHOW_FIELDS, FORMAT, API_KEY);
        }
        Timber.d(call.request().url().toString());
        String s = call.request().url().toString();
        call.enqueue(new Callback<NewsSourceResponse>() {
            @Override
            public void onResponse(Call<NewsSourceResponse> call, retrofit2.Response<NewsSourceResponse> response) {
                if (response.body() != null && response.body().getResponse() != null) {
                    NewsResponse resultsList = response.body().getResponse();
                    newsLiveData.setValue(resultsList);
                }
            }

            @Override
            public void onFailure(Call<NewsSourceResponse> call, Throwable t) {
                Timber.e(t);
            }
        });
        return newsLiveData;
    }

}
