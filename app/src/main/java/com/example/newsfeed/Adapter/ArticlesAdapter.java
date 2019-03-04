package com.example.newsfeed.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsfeed.R;
import com.example.newsfeed.models.Articles;
import com.example.newsfeed.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter {

    private List<Articles> articlesList;
    private OnArticleListener onArticleListener;

    public ArticlesAdapter(List<Articles> articlesList, OnArticleListener onArticleListener) {
        this.articlesList = articlesList;
        this.onArticleListener = onArticleListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        ArticlesViewHolder holder = new ArticlesViewHolder(view, onArticleListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ArticlesViewHolder holder = (ArticlesViewHolder) viewHolder;
        Articles article = articlesList.get(i);
        holder.title.setText(article.getTitle());
        holder.description.setText(article.getDescription());
        holder.sourceName.setText(article.getSource().getName());
        Log.d("picasso", "onBindViewHolder: "+article.getUrlToImage());
        if (article.getUrlToImage() != null && !article.getUrlToImage().equals("")) {
            Picasso.get().load(article.getUrlToImage()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    public class ArticlesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView title;
        final TextView description;
        final TextView sourceName;
        final TextView timepost;
        final ImageView image;
        OnArticleListener onArticleListener;

        public ArticlesViewHolder(@NonNull View itemView, OnArticleListener onArticleListener) {
            super(itemView);
            title = itemView.findViewById(R.id.article_title);
            description = itemView.findViewById(R.id.article_desc);
            image = itemView.findViewById(R.id.img);
            sourceName = itemView.findViewById(R.id.article_source);
            timepost = itemView.findViewById(R.id.article_time);
            this.onArticleListener = onArticleListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onArticleListener.OnArticleClick(getAdapterPosition());
        }

    }

    public interface OnArticleListener {
        void OnArticleClick(int position);
    }

}
