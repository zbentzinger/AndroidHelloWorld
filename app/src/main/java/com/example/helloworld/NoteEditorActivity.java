package com.example.helloworld;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

public class NoteEditorActivity extends AppCompatActivity {

    private static final String TAG = "NoteEditorActivity";

    private String action;
    private EditText editor;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editor = findViewById(R.id.editText2);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(DatabaseContentProvider.CONTENT_ITEM);

        if (uri == null) {

            action = Intent.ACTION_INSERT;
            setTitle(R.string.new_note_title);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                finishEditing();
                break;

        }

        return true;

    }

    private void finishEditing() {

        String noteBody = editor.getText().toString().trim();

        switch (action) {

            case Intent.ACTION_INSERT:

                if (noteBody.length() == 0) {

                    setResult(RESULT_CANCELED);

                } else {

                    saveNote(noteBody);

                }

        }

        finish();

    }

    private void saveNote(String value) {

        ContentValues content = new ContentValues();

        content.put(DatabaseOpenHelper.NOTE_BODY, value);

        Uri noteUri = getContentResolver().insert(
                DatabaseContentProvider.CONTENT_URI,
                content
        );

        setResult(RESULT_OK);

        if (noteUri != null) {

            Log.d(TAG,"Inserted Note ID: " + noteUri.getLastPathSegment());

        }

    }

    @Override public void onBackPressed() {

        finishEditing();

    }

}
