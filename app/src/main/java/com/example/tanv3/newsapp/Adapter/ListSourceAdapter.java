package com.example.tanv3.newsapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tanv3.newsapp.Common.Common;
import com.example.tanv3.newsapp.Interface.IconBestServices;
import com.example.tanv3.newsapp.Interface.ItemClickListener;
import com.example.tanv3.newsapp.ListNews;
import com.example.tanv3.newsapp.Model.IconBest;
import com.example.tanv3.newsapp.Model.WebSite;
import com.example.tanv3.newsapp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder> {

    private Context context;
    private WebSite webSite;
    private IconBestServices mServices;

    public ListSourceAdapter(Context context, WebSite webSite) {
        this.context = context;
        this.webSite = webSite;
        mServices = Common.getiConServices();
    }

    @NonNull
    @Override
    public ListSourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_layout, parent, false);
        return new ListSourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListSourceViewHolder holder, final int position) {

        StringBuilder iconBestAPI = new StringBuilder("https://besticon-demo.herokuapp.com/allicons.json?url=");
        iconBestAPI.append(webSite.getSources().get(position).getUrl());
        mServices.getIconUrl(iconBestAPI.toString()).enqueue(new Callback<IconBest>() {
            @Override
            public void onResponse(Call<IconBest> call, Response<IconBest> response) {
                if (response.body().getIcons().size() > 0) {

                    Picasso.with(context).load(response.body().getIcons().get(0).getUrl())
                            .into(holder.source_image);
                }
                holder.source_title.setText(webSite.getSources().get(position).getName());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent intent = new Intent(context, ListNews.class);
                        String source=webSite.getSources().get(position).getId();
                        intent.putExtra("source",source );
                        //intent.putExtra("sortBy",webSite.getSources().get(position).getSortBysAvailable().get(0));//get default sort by method
                        context.startActivity(intent);


                    }
                });

            }

            @Override
            public void onFailure(Call<IconBest> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return webSite.getSources().size();
    }
}


class ListSourceViewHolder extends ViewHolder implements View.OnClickListener {
    ItemClickListener itemClickListener;

    TextView source_title;
    CircleImageView source_image;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ListSourceViewHolder(View itemView) {
        super(itemView);
        source_title = itemView.findViewById(R.id.source_title);
        source_image = itemView.findViewById(R.id.source_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}