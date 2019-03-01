package com.example.newsfeed.models;

import java.util.List;

public class News {
    private String status;
    private int totalResults;
    private List<Articles> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Articles> getArticles() {
        return articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "News{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                '}';
    }
}
