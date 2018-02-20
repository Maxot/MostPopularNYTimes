package com.maxot.mostpopularnytimes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxot.mostpopularnytimes.DB.ArticleDbHelper;
import com.maxot.mostpopularnytimes.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for showing list of articles.
 */

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder> {

    private List<Article> articles;
    private Context context;


    public ArticleRecyclerViewAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.article = articles.get(position);
        if(articles.get(position).getMedia() != null && !articles.get(position).getMedia().isEmpty() &&
                articles.get(position).getMedia().get(0).getMediaMetaDatas() != null &&
                !articles.get(position).getMedia().get(0).getMediaMetaDatas().isEmpty()) {
            Picasso.with(context).load(articles.get(position).getMedia().get(0).getMediaMetaDatas().get(0).getUrl()).into(holder.imageView);
        }
        holder.titleView.setText(articles.get(position).getTitle());
        holder.bylineView.setText(articles.get(position).getByLine());

        String formattedDate =  android.text.format.DateFormat.format("yyyy-MM-dd", articles.get(position).getPublishedDate()).toString();
        holder.dateView.setText(formattedDate);

        // Check if article exist in DB
        ArticleDbHelper db = new ArticleDbHelper(context);
        if( db.checkArticle(holder.article) == true) {
            holder.mCheckBox.setButtonDrawable(R.drawable.btn_star_big_on);
        }else holder.mCheckBox.setButtonDrawable(R.drawable.btn_star_big_off);

        // Show detail info about article
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ArticleDetailActivity.class);
                    intent.putExtra(ArticleDetailFragment.ARG_ARTICLE_URL, holder.article.getUrl());
                    intent.putExtra(ArticleDetailFragment.ARG_ARTICLE_TITLE, holder.article.getTitle());
                    intent.putExtra(ArticleDetailFragment.ARG_ARTICLE_ABSTRACT, holder.article.getAbstractText());
                    intent.putExtra(ArticleDetailFragment.ARG_ARTICLE_BYLINE, holder.article.getByLine());
                    intent.putExtra(ArticleDetailFragment.ARG_ARTICLE_DATE, holder.article.getPublishedDate());

                    context.startActivity(intent);
            }
        });
        // Add to favorite and checked if article exist in db
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArticleDbHelper db = new ArticleDbHelper(context);
                //db.cleanTable();
                 if( db.checkArticle(holder.article) == true) {
                     db.deleteArticle(holder.article);
                     holder.mCheckBox.setButtonDrawable(R.drawable.btn_star_big_off);
                     Log.d("DB", holder.article.getUrl() + " is delete");
                 }else {
                     db.addArticle(holder.article);
                     holder.mCheckBox.setButtonDrawable(R.drawable.btn_star_big_on);
                     Log.d("DB", holder.article.getUrl() + " is add");
                 }
            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageView;
        public final TextView titleView;
        public final TextView bylineView;
        public final TextView dateView;
        public final AppCompatCheckBox mCheckBox;
        public Article article;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.imageView);
            titleView = (TextView) view.findViewById(R.id.list_title);
            bylineView = (TextView) view.findViewById(R.id.list_byline);
            dateView = (TextView) view.findViewById(R.id.list_date);
            mCheckBox = (AppCompatCheckBox) view.findViewById(R.id.checkStar);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + dateView.getText() + "'";
        }
    }
}
