package com.example.helloworld;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NoteEditorActivity extends AppCompatActivity {

    private static final String TAG = "NoteEditorActivity";

    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldValue;

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

        } else {

            action = Intent.ACTION_EDIT;
            noteFilter = DatabaseOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(
                    uri,
                    DatabaseOpenHelper.ALL_COLS,
                    noteFilter,
                    null,
                    null
            );

            cursor.moveToFirst();
            oldValue = cursor.getString(
                    cursor.getColumnIndex(DatabaseOpenHelper.NOTE_BODY)
            );

            editor.setText(oldValue);

            editor.requestFocus();

        }

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {

        if (action.equals(Intent.ACTION_EDIT)) {

            getMenuInflater().inflate(R.menu.note_editor_menu, menu);

        }

        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                finishEditing();
                break;

            case R.id.action_delete:
                deleteNote();
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
                break;

            case Intent.ACTION_EDIT:

                if (noteBody.length() == 0) {

                    deleteNote();

                } else if (oldValue.equals(noteBody)) {

                    setResult(RESULT_CANCELED);

                } else {
                    
                    updateNote(noteBody);
                    
                }

        }

        finish();

    }

    private void updateNote(String value) {

        ContentValues content = new ContentValues();
        content.put(DatabaseOpenHelper.NOTE_BODY, value);
        getContentResolver().update(
                DatabaseContentProvider.CONTENT_URI,
                content,
                noteFilter,
                null
        );

        Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);

    }

    private void deleteNote() {

        getContentResolver().delete(
                DatabaseContentProvider.CONTENT_URI,
                noteFilter,
                null
        );

        Toast.makeText(this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);
        finish();

    }

    private void saveNote(String value) {

        ContentValues content = new ContentValues();
        content.put(DatabaseOpenHelper.NOTE_BODY, value);
        Uri noteUri = getContentResolver().insert(
                DatabaseContentProvider.CONTENT_URI,
                content
        );

        if (noteUri != null) {

            Log.d(TAG,"Inserted Note ID: " + noteUri.getLastPathSegment());

        }

        setResult(RESULT_OK);

    }

    @Override public void onBackPressed() {

        finishEditing();

    }

}
