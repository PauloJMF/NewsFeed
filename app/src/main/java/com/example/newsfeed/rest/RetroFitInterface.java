package com.example.newsfeed.rest;

import com.example.newsfeed.models.NewsApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetroFitInterface {

    @GET("/v2/everything")
    Call<NewsApiResponse>searchNews(@Query("q") String q, @Query("apikey") String apikey, @Query("pageSize") int pageSize, @Query("page") int page, @Query("sortBy") String sortBy, @Query("language") String language);
    @GET("/v2/top-headlines")
    Call<NewsApiResponse>searchRecent(@Query("country") String country , @Query("apikey") String apikey, @Query("pageSize") int pageSize, @Query("page") int page);

}
