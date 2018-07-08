package com.example.tanv3.newsapp.Interface;

import com.example.tanv3.newsapp.Model.News;
import com.example.tanv3.newsapp.Model.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsService {

    @GET("v1/sources?language=en")
    Call<WebSite> getSources();

    @GET
    Call<News> getNewsArticles(@Url String url);



}
