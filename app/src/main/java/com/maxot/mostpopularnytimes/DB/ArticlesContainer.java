package com.maxot.mostpopularnytimes.DB;

import android.provider.BaseColumns;

/**
 * Contain names of the table and the columns.
 */

public final class ArticlesContainer {

    public static abstract class Articles implements BaseColumns{
        public static final String TABLE_NAME = "articles";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ABSTRACT = "abstract";
        public static final String COLUMN_NAME_BYLINE = "byline";
        public static final String COLUMN_NAME_PUBLISHED_DATA = "published_data";
    }
}
