package com.example.tanv3.newsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tanv3.newsapp.Adapter.ListNewsAdapter;
import com.example.tanv3.newsapp.Common.Common;
import com.example.tanv3.newsapp.Interface.NewsService;
import com.example.tanv3.newsapp.Model.Articles;
import com.example.tanv3.newsapp.Model.News;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNews extends AppCompatActivity {

    KenBurnsView kbv;
    ProgressBar dialog;
    NewsService mService;
    TextView top_author, top_tile;
    SwipeRefreshLayout swipeRefreshLayout;
    DiagonalLayout diagonalLayout;

    String source = "", sortBy = "", webHotUrl = "";

    ListNewsAdapter adapter;
    RecyclerView lstNews;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        //services
        mService = Common.getNewsServices();
        dialog = new ProgressBar(this);


        //view
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source, true);

            }
        });


        kbv = findViewById(R.id.top_image);
        top_author = findViewById(R.id.top_Author);
        top_tile = findViewById(R.id.top_title);

        lstNews = findViewById(R.id.IstNews);
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);


        diagonalLayout = (DiagonalLayout) findViewById(R.id.diagonallayout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(getBaseContext(), DetailArticle.class);
                detail.putExtra("webURL", webHotUrl);
                startActivity(detail);

            }
        });


        //Intent

        if (getIntent() != null) {
            source = getIntent().getStringExtra("source");
            // sortBy=getIntent().getStringExtra("sortBy");
            if (!source.isEmpty()) {
                loadNews(source, false);


            }

        }


    }

    private void loadNews(String source, boolean isRefresh) {

        if (!isRefresh) {
            dialog.setVisibility(View.VISIBLE);
            mService.getNewsArticles("https://newsapi.org/v1/articles?source=" + source + "&apiKey=3819839d3f8b4bc4a38c2afe5ee83b92")
                    .enqueue(new Callback<News>() {

                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            dialog.setVisibility(View.GONE);
                            //set First article of position 0 to diagonal layout
                            Picasso.with(getApplicationContext())
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kbv);

                            top_tile.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAuthor());
                            webHotUrl = response.body().getArticles().get(0).getUrl();


                            //Load remaining articles
                            List<Articles> remainingArticle = response.body().getArticles();

                            //Because we already load first article we need to remove it
                            remainingArticle.remove(0);

                            adapter = new ListNewsAdapter(remainingArticle, getBaseContext());
                            adapter.notifyDataSetChanged();
                            lstNews.setAdapter(adapter);


                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
        }
        else {

            dialog.setVisibility(View.VISIBLE);
            mService.getNewsArticles("https://newsapi.org/v1/articles?source=" + source + "&apiKey=3819839d3f8b4bc4a38c2afe5ee83b92")
                    .enqueue(new Callback<News>() {

                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            dialog.setVisibility(View.GONE);
                            //set First article of position 0 to diagonal layout
                            Picasso.with(getApplicationContext())
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kbv);

                            top_tile.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAuthor());
                            webHotUrl = response.body().getArticles().get(0).getUrl();


                            //Load remaining articles
                            List<Articles> remainingArticle = response.body().getArticles();

                            //Because we already load first article we need to remove it
                           // remainingArticle.remove(0);

                            adapter = new ListNewsAdapter(remainingArticle, getBaseContext());
                            adapter.notifyDataSetChanged();
                            lstNews.setAdapter(adapter);


                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });

        }
    }


}
