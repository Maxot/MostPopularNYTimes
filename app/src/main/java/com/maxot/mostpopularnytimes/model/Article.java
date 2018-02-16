package com.maxot.mostpopularnytimes.model;



import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Article {
    @SerializedName("url")
    private String url;

    @SerializedName("title")
    private String title;

    @SerializedName("abstract")
    private String abstractText;

    @SerializedName("byline")
    private String byLine;

    @SerializedName("published_date")
    private Date publishedDate;

    @SerializedName("media")
    private MediaList media;

    public Article(){}

    public Article(String url, String title, String abstractText, String byLine, Date publishedDate) {
        this.url = url;
        this.title = title;
        this.abstractText = abstractText;
        this.byLine = byLine;
        this.publishedDate = publishedDate;
    }
    public Article(String url, String title, String abstractText, String byLine) {
        this.url = url;
        this.title = title;
        this.abstractText = abstractText;
        this.byLine = byLine;
    }


    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public String getByLine() {
        return byLine;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public MediaList getMedia() {
        return media;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public void setByLine(String byLine) {
        this.byLine = byLine;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

//    public void setMedia() {
//
//    }
}
