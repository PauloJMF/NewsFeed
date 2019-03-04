package com.example.newsfeed;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.newsfeed.Adapter.ArticlesAdapter;
import com.example.newsfeed.models.Articles;
import com.example.newsfeed.models.News;
import com.example.newsfeed.rest.RetroFitConfig;
import com.example.newsfeed.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ArticlesAdapter.OnArticleListener, SwipeRefreshLayout.OnRefreshListener {
    private final String API_KEY = "08f52c1dfdbd4876a8ebedbf36ebc339";
    private List<Articles> articlesList;
    private RecyclerView resp;
    private int mPageNumber;
    private Toolbar mToolbar;
    private ArticlesAdapter adapter;
    private SwipeRefreshLayout mswipeRefreshLayout;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mswipeRefreshLayout = findViewById(R.id.swp_refresh);
        mswipeRefreshLayout.setOnRefreshListener(this);
        resp = findViewById(R.id.rv_articles);
        resp.setHasFixedSize(true);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        this.mPageNumber = 1;
        onLoadingSwipeRefresh("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onLoadingSwipeRefresh(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setQueryHint("Search");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void OnArticleClick(int position) {
        Log.e("NewsApi   ", "Article number :" + position);
        Articles articles = articlesList.get(position);
        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
        intent.putExtra("urlTo", articles.getUrl());
        startActivity(intent);

    }

    public void populateArticles(News news) {
        if (articlesList!= null && !articlesList.isEmpty()) {
            articlesList.clear();
        }
        articlesList = news.getArticles();
        adapter = new ArticlesAdapter(articlesList, MainActivity.this);
        resp.setAdapter(adapter);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        resp.setLayoutManager(layout);
    }

    public void makeSearchRequest(String query) {
        Call<News> call = new RetroFitConfig().getRetrofit().searchNews(query, API_KEY, 20, mPageNumber, "publishedAt");
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                Log.e("NewsApi   ", "News = :" + response.toString());
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    News news = response.body();
                    populateArticles(news);
                    mswipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                    toast.show();
                    mswipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("NewsApi   ", "Erro ao buscar sobre:" + t.getMessage());
                mswipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void makeRecentRequest() {
        Call<News> call = new RetroFitConfig().getRetrofit().searchRecent(Utils.getCountry(), API_KEY, 20, mPageNumber);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                Log.e("NewsApi   ", "News = :" + response.toString());
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    News news = response.body();
                    populateArticles(news);
                    mswipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                    toast.show();
                    mswipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("NewsApi   ", "Erro ao buscar sobre:" + t.getMessage());
                mswipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        mswipeRefreshLayout.setRefreshing(true);
        if (mSearchView.getQuery().toString().equals("")) {
            makeRecentRequest();
        } else makeSearchRequest(mSearchView.getQuery().toString());
    }

    public void onLoadingSwipeRefresh(final String query) {
        mswipeRefreshLayout.setRefreshing(true);
        mswipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if (query.equals("")) makeRecentRequest();
                        else makeSearchRequest(query);
                    }
                }
        );

    }
}
