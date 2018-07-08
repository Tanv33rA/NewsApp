package com.example.tanv3.newsapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tanv3.newsapp.Adapter.ListSourceAdapter;
import com.example.tanv3.newsapp.Common.Common;
import com.example.tanv3.newsapp.Interface.NewsService;
import com.example.tanv3.newsapp.Model.WebSite;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView listWebsite;
    RecyclerView.LayoutManager layoutManager;
    NewsService mServices;
    ListSourceAdapter adapter;
    ProgressDialog dialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebSiteSource(true);
            }
        });


        //paper.init
        Paper.init(this);

        //Init service
        mServices = Common.getNewsServices();

        //Init View

        listWebsite = findViewById(R.id.list_sourse);
        listWebsite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listWebsite.setLayoutManager(layoutManager);

        dialog = new ProgressDialog(MainActivity.this);

        loadWebSiteSource(false);


    }

    private void loadWebSiteSource(boolean isRefreshed) {
        if (!isRefreshed) {

            String cache = Paper.book().read("cache");
            if (cache != null && !cache.isEmpty()) {
                WebSite webSite = new Gson().fromJson(cache, WebSite.class); //convert cache from json to object
                adapter = new ListSourceAdapter(getBaseContext(), webSite);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);

            } else {
                dialog.show();
                mServices.getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                        adapter = new ListSourceAdapter(MainActivity.this, response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        //save to the cache
                        Paper.book().write("cache", new Gson().toJson(response.body()));

                    }

                    @Override
                    public void onFailure(Call<WebSite> call, Throwable t) {

                    }
                });

            }


        } else // if from swipe to refresh
        {

            dialog.show();
            mServices.getSources().enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                    adapter = new ListSourceAdapter(getBaseContext(), response.body());
                    adapter.notifyDataSetChanged();
                    listWebsite.setAdapter(adapter);

                    //save to the cache
                    Paper.book().write("cache", new Gson().toJson(response.body()));

                    //set refresh progress false
                    swipeRefreshLayout.setRefreshing(false);

                    dialog.dismiss();

                }

                @Override
                public void onFailure(Call<WebSite> call, Throwable t) {

                }
            });
        }

    }
}
