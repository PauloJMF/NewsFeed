package com.example.newsfeed.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitConfig {

    private String BASE_URL = "https://newsapi.org/";
    private final Retrofit retrofit;

    public RetroFitConfig() {
        this.retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

    }

    public RetroFitInterface getRetrofit() {
        return this.retrofit.create(RetroFitInterface.class);
    }
}
