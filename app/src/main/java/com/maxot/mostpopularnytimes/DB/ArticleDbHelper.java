package com.maxot.mostpopularnytimes.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.maxot.mostpopularnytimes.model.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.provider.BaseColumns._ID;
import static com.maxot.mostpopularnytimes.DB.ArticlesContainer.Articles.COLUMN_NAME_ABSTRACT;
import static com.maxot.mostpopularnytimes.DB.ArticlesContainer.Articles.COLUMN_NAME_BYLINE;
import static com.maxot.mostpopularnytimes.DB.ArticlesContainer.Articles.COLUMN_NAME_PUBLISHED_DATA;
import static com.maxot.mostpopularnytimes.DB.ArticlesContainer.Articles.COLUMN_NAME_TITLE;
import static com.maxot.mostpopularnytimes.DB.ArticlesContainer.Articles.COLUMN_NAME_URL;
import static com.maxot.mostpopularnytimes.DB.ArticlesContainer.Articles.TABLE_NAME;

/**
 * Created by maxot on 15.02.18.
 */

public class ArticleDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";

    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ArticlesContainer.Articles._ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_ABSTRACT + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_BYLINE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_PUBLISHED_DATA + TEXT_TYPE +
                    " )";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Articles.db";

    public  ArticleDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);

    }
    // Adding new article to DB
    public long addArticle(Article article){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_URL, article.getUrl());
        values.put(COLUMN_NAME_TITLE, article.getTitle());
        values.put(COLUMN_NAME_ABSTRACT, article.getAbstractText());
        values.put(COLUMN_NAME_BYLINE, article.getByLine());
        values.put(COLUMN_NAME_PUBLISHED_DATA, article.getPublishedDate().toString());

        long newRowId;
        newRowId = db.insert(TABLE_NAME,
                null,
                values);
        db.close();
        return newRowId;
    }
    // Getting article by ID
    public Article getArticle(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Article article = new Article();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COLUMN_NAME_URL,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_ABSTRACT,
                COLUMN_NAME_BYLINE,
                COLUMN_NAME_PUBLISHED_DATA
        };
        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                _ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
        );
        if (cursor != null)
            cursor.moveToFirst();
        // For parse Date to string
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            article = new Article(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    format.parse(cursor.getString(4)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        db.close();
        return  article;
    }

    //Getting all articles
    public List<Article> getAllArticle(){
        List<Article> articleList = new ArrayList<Article>();
        //Select all query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.ENGLISH);

        if (cursor.moveToFirst()){
            do{
                try{
                    Article article = new Article();
                    article.setUrl(cursor.getString(1));
                    article.setTitle(cursor.getString(2));
                    article.setAbstractText(cursor.getString(3));
                    article.setByLine(cursor.getString(4));
                    article.setPublishedDate(format.parse(cursor.getString(5)));
                    articleList.add(article);
                }catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return articleList;
    }
    // Deleting an article
    public void deleteArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME_URL + " = ?",
                new String[] { String.valueOf(article.getUrl()) });
        db.close();
    }
    // Clean the table
    public void cleanTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
    // Check the article in DB?
    public boolean checkArticle(Article article){
        boolean check = false;
        SQLiteDatabase db = this.getReadableDatabase();
        List<Article> articleList = getAllArticle();
        for (Article a:
                articleList) {
            if (article.getUrl().equals(a.getUrl()))  {
                check = true;
            }
        }
        db.close();
        return check;
    }
}
