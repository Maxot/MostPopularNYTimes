package com.maxot.mostpopularnytimes.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maxot.mostpopularnytimes.ArticleRecyclerViewAdapter;
import com.maxot.mostpopularnytimes.R;
import com.maxot.mostpopularnytimes.model.Article;
import com.maxot.mostpopularnytimes.model.ArticleResponse;
import com.maxot.mostpopularnytimes.network.ApiClient;
import com.maxot.mostpopularnytimes.network.ApiInterface;

import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * A fragment representing a list of Articles.
 */

public class ArticleListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static String API_KEY = "";
    private static final String BUNDLE_CONTENT = "bundle_content";
    private String content;
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ARTICLE_URL = "article_url";
    public static final String ARG_ARTICLE_TITLE = "article_title";
    public static final String ARG_ARTICLE_ABSTRACT = "article_abstract";
    public static final String ARG_ARTICLE_DATE = "article_date";
    public static final String ARG_ARTICLE_BYLINE = "article_byline";

    /**
     * The dummy content this fragment is presenting.
     */
    private Article article;

    public static ArticleListFragment newInstance(final String content) {
        final ArticleListFragment fragment = new ArticleListFragment();
        final Bundle arguments = new Bundle();
        arguments.putString(BUNDLE_CONTENT, content);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(BUNDLE_CONTENT)) {
            content = getArguments().getString(BUNDLE_CONTENT);
        } else {
            throw new IllegalArgumentException("Must be created through newInstance(...)");
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Add swipe refresher
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

        if (getArguments().containsKey(ARG_ARTICLE_URL) && getArguments().containsKey(ARG_ARTICLE_TITLE) &&
                getArguments().containsKey(ARG_ARTICLE_ABSTRACT) && getArguments().containsKey(ARG_ARTICLE_BYLINE) &&
                getArguments().containsKey(ARG_ARTICLE_DATE)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            article = new Article(getArguments().getString(ARG_ARTICLE_URL), getArguments().getString(ARG_ARTICLE_TITLE),
                    getArguments().getString(ARG_ARTICLE_ABSTRACT), getArguments().getString(ARG_ARTICLE_BYLINE),
                    (Date)getArguments().getSerializable(ARG_ARTICLE_DATE));
        }
        // Show the dummy content as text in a TextView.
        if (article != null) {
            ((TextView) view.findViewById(R.id.article_detail)).setText(article.getAbstractText());

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(article.getTitle());
            }
        }

        return view;
    }

    void refreshItems() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleResponse> call = apiService.getArticles(content,"all-sections", 7, API_KEY);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse>call, Response<ArticleResponse> response) {
                int statusCode = response.code();
                if(statusCode == HttpURLConnection.HTTP_OK) {
                    List<Article> articles = response.body().getArticles();
                    recyclerView.setAdapter(new ArticleRecyclerViewAdapter(articles, getActivity()));
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
