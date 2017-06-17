package com.example.lenovo.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lenovo on 15/6/17.
 */

public class MapDbContract {
    public static final String CONTENT_AUTHORITY = "com.example.lenovo.contentprovider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MAP = "map";
    public static final class MapEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MAP)
                .build();
        public static final String TABLE_NAME = "map";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";






    }

}
