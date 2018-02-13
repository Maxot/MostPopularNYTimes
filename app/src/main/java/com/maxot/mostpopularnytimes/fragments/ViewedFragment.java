package com.maxot.mostpopularnytimes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.maxot.mostpopularnytimes.ArticleRecyclerViewAdapter;
import com.maxot.mostpopularnytimes.R;
import com.maxot.mostpopularnytimes.model.Article;
import com.maxot.mostpopularnytimes.model.ArticleResponse;
import com.maxot.mostpopularnytimes.network.ApiClient;
import com.maxot.mostpopularnytimes.network.ApiInterface;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by maxot on 13.02.18.
 */

public class ViewedFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static String API_KEY = "";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
        refreshItems();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_list, container, false);
        API_KEY = getResources().getString(R.string.nytimes_api_key);
        recyclerView = (RecyclerView) view.findViewById(R.id.article_list);
        assert recyclerView != null;
        if (API_KEY.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Need API-key", Toast.LENGTH_LONG).show();
        }






        return view;
    }

    void refreshItems() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleResponse> call = apiService.getArticles("all-sections", 7, API_KEY);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse>call, Response<ArticleResponse> response) {
                int statusCode = response.code();
                if(statusCode == HttpURLConnection.HTTP_OK) {
                    List<Article> articles = response.body().getArticles();
                    recyclerView.setAdapter(new ArticleRecyclerViewAdapter(articles, getContext()));
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

}
