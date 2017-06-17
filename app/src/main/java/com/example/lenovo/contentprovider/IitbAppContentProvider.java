package com.example.lenovo.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;

import static com.example.lenovo.contentprovider.MapDbContract.MapEntry.TABLE_NAME;


public class IitbAppContentProvider extends ContentProvider {
    public static final int LOCS = 100;
    public static final int LOC_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MapDbHelper mapDbHelper;
    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MapDbContract.CONTENT_AUTHORITY, MapDbContract.PATH_MAP, LOCS);
        matcher.addURI(MapDbContract.CONTENT_AUTHORITY, MapDbContract.PATH_MAP + "/#", LOC_WITH_ID);
        return matcher;

    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mapDbHelper = new MapDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mapDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case LOCS:
                cursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case LOC_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String[] selectionArguments = new String[]{id};
                cursor = db.query(TABLE_NAME,
                        projection,
                        "_id=?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new SQLException("Wrong Uri: " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        /*This method gives a way to standardize the data formats the
          provider accesses, and this can be useful for data organization.*/
        int match = sUriMatcher.match(uri);
        switch (match) {
            case LOCS:
                return "vnd.android.cursor.dir" + "/" + MapDbContract.CONTENT_AUTHORITY + "/" + MapDbContract.PATH_MAP;
            case LOC_WITH_ID:
                return "vnd.android.cursor.item" + "/" + MapDbContract.CONTENT_AUTHORITY + "/" + MapDbContract.PATH_MAP;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mapDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch(match){
            case LOCS:
                long id = db.insert(TABLE_NAME, null, values);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(MapDbContract.MapEntry.CONTENT_URI, id);
                }
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new SQLException("Wrong uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mapDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case LOCS:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {


                        long _id = db.insert(TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case LOCS:
                numRowsDeleted = mapDbHelper.getWritableDatabase().delete(
                        TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            case LOC_WITH_ID:

                String id = uri.getPathSegments().get(1);

                numRowsDeleted = mapDbHelper.getWritableDatabase().delete(TABLE_NAME, "_id=?", new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }



    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int itemsUpdated;


        int match = sUriMatcher.match(uri);

        switch (match) {
            case LOC_WITH_ID:

                String id = uri.getPathSegments().get(1);

                itemsUpdated = mapDbHelper.getWritableDatabase().update(TABLE_NAME, values, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (itemsUpdated != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }


        return itemsUpdated;
    }
}
