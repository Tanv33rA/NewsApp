package com.example.tanv3.newsapp.Interface;

import com.example.tanv3.newsapp.Model.IconBest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IconBestServices {
    @GET
    Call<IconBest>getIconUrl(@Url String url);
}
