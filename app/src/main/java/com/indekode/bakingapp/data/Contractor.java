package com.indekode.bakingapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contractor {

    static final String CONTENT_AUTHORITY = "com.indekode.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WIDGET = "recipes";

    public static final class IngredientsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WIDGET).build();

        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WIDGET;
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WIDGET;

        static final String TABLE_NAME = "recipes";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_INGREDIENT_INGREDIENT = "ingredient";
        public static final String COLUMN_INGREDIENT_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT_QUANTITY = "quantity";
    }
}
