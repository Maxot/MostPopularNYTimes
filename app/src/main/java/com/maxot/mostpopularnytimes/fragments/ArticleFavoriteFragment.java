package com.maxot.mostpopularnytimes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxot.mostpopularnytimes.ArticleRecyclerViewAdapter;
import com.maxot.mostpopularnytimes.DB.ArticleDbHelper;
import com.maxot.mostpopularnytimes.R;
import com.maxot.mostpopularnytimes.model.Article;

import java.util.List;

/**
 * A fragment representing a list of Articles from DB.
 */

public class ArticleFavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Add swipe refresher
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
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
        recyclerView = (RecyclerView) view.findViewById(R.id.article_list);
        assert recyclerView != null;
        return view;
    }

    void refreshItems() {
        ArticleDbHelper db = new ArticleDbHelper(getContext());
        List<Article> articles = db.getAllArticle();
        recyclerView.setAdapter(new ArticleRecyclerViewAdapter(articles, getActivity()));

        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

}
