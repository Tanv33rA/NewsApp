package com.example.tanv3.newsapp.Common;

import android.support.annotation.NonNull;

import com.example.tanv3.newsapp.Interface.IconBestServices;
import com.example.tanv3.newsapp.Interface.NewsService;
import com.example.tanv3.newsapp.Remote.IconBestClient;
import com.example.tanv3.newsapp.Remote.RetrofitClient;

public class Common {

    private static final String BASE_URL="https://newsapi.org/";

    public static final String API_KEY="3819839d3f8b4bc4a38c2afe5ee83b92";

    public static NewsService getNewsServices (){

        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);

    }

    public static IconBestServices getiConServices (){

        return IconBestClient.getClient().create(IconBestServices.class);

    }




    @NonNull
    public static String getApiUrl(String source){

        StringBuilder apiUrl=new StringBuilder("https://newsapi.org/v1/articles?source=");
        return apiUrl.append(source)
                .append("&apiKey")
                .append(API_KEY)
                .toString();

    }


}