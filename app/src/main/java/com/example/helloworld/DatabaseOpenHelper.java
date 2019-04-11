package com.example.helloworld;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseOpenHelper";

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String NOTE_TABLE = "notes";
    public static final String NOTE_ID = "_id";
    public static final String NOTE_BODY = "body";
    public static final String NOTE_CREATED_AT = "created_at";

    public static final String[] ALL_COLS = {NOTE_ID, NOTE_BODY, NOTE_CREATED_AT};

    private static final String CREATE_TABLE =
            "CREATE TABLE " + NOTE_TABLE + " (" +
                    NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTE_BODY + " TEXT, " +
                    NOTE_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP" +
                    ")";

    public DatabaseOpenHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);

        Log.d(TAG, "Creating the database");

    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);
        onCreate(db);

        Log.d(TAG, "Updating the database schema");

    }

}
