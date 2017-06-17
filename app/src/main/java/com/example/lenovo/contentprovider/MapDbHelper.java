package com.example.lenovo.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 15/6/17.
 */

public class MapDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mapDb.db";
    private static final int VERSION = 1;

    MapDbHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + MapDbContract.MapEntry.TABLE_NAME + " (" +
                MapDbContract.MapEntry._ID                + " INTEGER PRIMARY KEY, " +
                MapDbContract.MapEntry.COLUMN_LATITUDE + " DOUBLE NOT NULL, " +
                MapDbContract.MapEntry.COLUMN_LONGITUDE + " DOUBLE NOT NULL, " +
                MapDbContract.MapEntry.COLUMN_NAME + " TEXT NOT NULL, " +

                MapDbContract.MapEntry.COLUMN_TYPE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MapDbContract.MapEntry.TABLE_NAME);
        onCreate(db);
    }
}
