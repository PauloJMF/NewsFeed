package com.example.newsfeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newsfeed.models.News;
import com.example.newsfeed.rest.RetroFitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final String API_KEY = "08f52c1dfdbd4876a8ebedbf36ebc339";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText search = findViewById(R.id.etMain_search);
        final TextView resp = findViewById(R.id.etMain_response);
        final Button searchBt = findViewById(R.id.btnMain_search);
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<News> call = new RetroFitConfig().getRetrofit().searchNews(search.getText().toString(), API_KEY);

                call.enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call, Response<News> response) {
                        if(response.body().getStatus() =="ok"){
                            News news = response.body();
                            resp.setText(news.toString());
                        }
                        else resp.setText(response.body().getStatus().toString() + "number" + response.body().getTotalResults());
                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {
                        Log.e("NewsApi   ", "Erro ao buscar sobre:" + t.getMessage());
                    }
                });
            }
        });



    }
}
