package com.example.newsfeed;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsfeed.adapter.ArticleAdapter;
import com.example.newsfeed.models.Article;
import com.example.newsfeed.models.NewsApiResponse;
import com.example.newsfeed.rest.RetroFitConfig;
import com.example.newsfeed.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ArticleAdapter.OnArticleListener, SwipeRefreshLayout.OnRefreshListener {
    private final String API_KEY = "08f52c1dfdbd4876a8ebedbf36ebc339";
    private final int PAGE_SIZE = 10;
    private int mPageNumber = 1;
    private boolean isLoading, end = false;
    private List<Article> articleList, moreArticles;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private ArticleAdapter mArticleAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SearchView mSearchView;
    private String mQuery;
    private RecyclerView.LayoutManager mLayoutManager;
    private NestedScrollView mNestedScrollView;
    private TextView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = findViewById(R.id.swp_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = findViewById(R.id.rv_articles);
        mRecyclerView.setHasFixedSize(true);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        onLoadingSwipeRefresh(this.mPageNumber);

        mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mNestedScrollView = findViewById(R.id.nestedsv);

        mNestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (!isLoading && !end && mNestedScrollView.getChildAt(0).getBottom()
                        == (mNestedScrollView.getHeight() + mNestedScrollView.getScrollY())) {
                    mPageNumber++;
                    isLoading = true;
                    onLoadingSwipeRefresh(mPageNumber);
                }
            }
        });
        mEmptyView = findViewById(R.id.empty_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        mNestedScrollView = findViewById(R.id.nestedsv);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setTitle("NewsFeed - "+query);
                onLoadingSwipeRefresh(mPageNumber);
                mNestedScrollView.smoothScrollTo(0, 0);
                mSearchView.clearFocus();
                return false;
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
        Article article = articleList.get(position);
        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
        intent.putExtra("urlTo", article.getUrl());
        intent.putExtra("sourceName", article.getSource().getName());
        startActivity(intent);
    }

    public void populateArticles(NewsApiResponse newsApiResponse) {
        if(newsApiResponse.getArticles().isEmpty() && mPageNumber==1)
        {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        articleList = newsApiResponse.getArticles();
        mArticleAdapter = new ArticleAdapter(articleList, MainActivity.this);
        mRecyclerView.setAdapter(mArticleAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void appendArticles(NewsApiResponse newsApiResponse) {
        moreArticles = newsApiResponse.getArticles();
        if (moreArticles.isEmpty()) {
            end = true;
            isLoading = false;
            return;
        }
        articleList.addAll(moreArticles);
        mArticleAdapter = new ArticleAdapter(articleList, MainActivity.this);
        mRecyclerView.setAdapter(mArticleAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        isLoading = false;
    }

    public void resetState() {
        end = false;
        mPageNumber = 1;
        isLoading = false;
        if (articleList != null) {
            articleList.clear();
            mArticleAdapter.notifyDataSetChanged();
        }
    }

    public void makeRequest(final int page) {
        Call<NewsApiResponse> call;
        this.mQuery = (mSearchView == null) ? "" : mSearchView.getQuery().toString();
        if (this.mQuery.equals("")) {
            call = new RetroFitConfig().getRetrofit().searchRecent(Utils.getCountry(), API_KEY, PAGE_SIZE, page);
        } else {
            call = new RetroFitConfig().getRetrofit().searchNews(this.mQuery, API_KEY, PAGE_SIZE, page, "publishedAt", Utils.getLanguage());
        }
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                    NewsApiResponse newsApiResponse = response.body();
                    if (page > 1) appendArticles(newsApiResponse);
                    else {
                        resetState();
                        populateArticles(newsApiResponse);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "Problemas de conexão !", Toast.LENGTH_SHORT);
                toast.show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        makeRequest(mPageNumber);
    }

    public void onLoadingSwipeRefresh(final int mPageNumber) {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        makeRequest(mPageNumber);
                    }
                }
        );

    }

    public void onBackPressed() {
        if (this.mQuery.equals("")) {
            super.onBackPressed();
        }
        setTitle("NewsFeed - Principais Noticias");
        mSearchView.setQuery("", false);
        mSearchView.setIconified(true);
        mSearchView.clearFocus();
        this.mQuery = "";
        resetState();
        onLoadingSwipeRefresh(mPageNumber);
    }

}
