package com.maxot.mostpopularnytimes;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.maxot.mostpopularnytimes.model.Article;

import java.util.Date;

/**
 * A fragment representing a single Article detail screen.
 * This fragment is either contained in a {@link com.maxot.mostpopularnytimes.fragments.ArticleListFragment}
 * in two-pane mode (on tablets) or a {@link ArticleDetailActivity}
 * on handsets.
 */
public class ArticleDetailFragment extends Fragment {
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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (article != null) {
            ((TextView) rootView.findViewById(R.id.article_detail)).setText(article.getAbstractText());

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(article.getTitle());
            }
        }

        return rootView;
    }
}
