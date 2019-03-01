package com.example.newsfeed.rest;

import com.example.newsfeed.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetroFitInterface {

    @GET("/v2/everything")
    Call<News>searchNews(@Query("q") String q, @Query("apikey") String apikey);

}
