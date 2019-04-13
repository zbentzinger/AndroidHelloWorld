package com.example.helloworld;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class DatabaseContentProvider extends ContentProvider {

    private static final String TAG = "DatabaseContentProvider";

    private static final String AUTHORITY = "com.example.helloworld.databasecontentprovider";
    private static final String BASE_PATH = "notes";

    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    private SQLiteDatabase database;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM = "foo";

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES_ID);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID);
    }

    @Override public boolean onCreate() {

        DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(getContext());
        database = dbHelper.getWritableDatabase();

        Log.d(TAG, "Connecting to the  database");

        return true;

    }

    @Override public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Log.d(TAG, "Running query against the database");

        return database.query(
                DatabaseOpenHelper.NOTE_TABLE,
                DatabaseOpenHelper.ALL_COLS,
                selection,
                null,
                null,
                null,
                DatabaseOpenHelper.NOTE_CREATED_AT + " DESC"
        );

    }

    @Override public String getType(Uri uri) {

        return null;

    }

    @Override public Uri insert(Uri uri, ContentValues values) {

        long id = database.insert(
                DatabaseOpenHelper.NOTE_TABLE,
                null,
                values
        );

        Log.d(TAG, "Inserting row into the database");

        return Uri.parse(BASE_PATH + "/" + id);

    }

    @Override public int delete(Uri uri, String selection, String[] selectionArgs) {

        Log.d(TAG, "Deleting row from database");

        return database.delete(
                DatabaseOpenHelper.NOTE_TABLE,
                selection,
                selectionArgs
        );
    }

    @Override public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        Log.d(TAG, "Updating row from the database");

        return database.update(
                DatabaseOpenHelper.NOTE_TABLE,
                values,
                selection,
                selectionArgs
        );


    }

}
