package com.example.newsfeed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsfeed.Adapter.ArticlesAdapter;
import com.example.newsfeed.models.Articles;
import com.example.newsfeed.models.News;
import com.example.newsfeed.rest.RetroFitConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ArticlesAdapter.OnArticleListener {
    private final String API_KEY = "08f52c1dfdbd4876a8ebedbf36ebc339";
    private List<Articles> articlesList;
    private RecyclerView resp;
    private EditText search;
    private Button searchBt;
    private int mPageNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.etMain_search);
        resp = findViewById(R.id.rv_articles);
        resp.setHasFixedSize(true);
        searchBt = findViewById(R.id.btnMain_search);
        this.mPageNumber = 1;
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSearchRequest();
            }
        });
        if(search.getText().toString().equals(""))
        {
            makeRecentRequest();
        }
    }

    @Override
    public void OnArticleClick(int position) {
        Log.e("NewsApi   ", "Article number :" + position);
        Articles articles = articlesList.get(position);
        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
        intent.putExtra("urlTo", articles.getUrl());
        startActivity(intent);

    }
    public void populateArticles(News news){
        articlesList = news.getArticles();
        ArticlesAdapter adapter;
        adapter = new ArticlesAdapter(articlesList, MainActivity.this);
        resp.setAdapter(adapter);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        resp.setLayoutManager(layout);
        resp.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        adapter.setOnBottomReachedListener(new ArticlesAdapter.OnBottomReachedListener() {
            @Override
            public void OnBottomReached(int position) {
                mPageNumber++;
                makeSearchRequest();
            }
        });
    }
    public void makeSearchRequest()
    {
        Call<News> call = new RetroFitConfig().getRetrofit().searchNews(search.getText().toString(), API_KEY, 20, mPageNumber);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                Log.e("NewsApi   ", "News = :" + response.toString());
                if(response.code()==200) {
                    News news = response.body();
                    populateArticles(news);
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No connection to make request", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("NewsApi   ", "Erro ao buscar sobre:" + t.getMessage());
            }
        });
    }
    public void makeRecentRequest()
    {
        Call<News> call = new RetroFitConfig().getRetrofit().searchRecent("br", API_KEY, 20, mPageNumber);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                Log.e("NewsApi   ", "News = :" + response.toString());
                if(response.code()==200) {
                    News news = response.body();
                    populateArticles(news);
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No connection to make request", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("NewsApi   ", "Erro ao buscar sobre:" + t.getMessage());
            }
        });
    }
}
