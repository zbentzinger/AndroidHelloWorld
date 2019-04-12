package com.example.helloworld;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";

    private CursorAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteEditorActivity.class));
            }
        });

        adapter = new CustomCursorAdapter(
                this,
                null,
                0
        );

        ListView list = findViewById(android.R.id.list);
        list.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

        Log.d(TAG, "Loading MainActivity");

    }

    private void insertNote(String noteBody) {

        ContentValues content = new ContentValues();
        content.put(DatabaseOpenHelper.NOTE_BODY, noteBody);
        Uri noteUri = getContentResolver().insert(
                DatabaseContentProvider.CONTENT_URI,
                content
        );

        if (noteUri != null) {

            Log.d(TAG,"Inserted Note ID: " + noteUri.getLastPathSegment());

        }

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar list_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all_notes) {

            deleteAllNotes();

        }

        return super.onOptionsItemSelected(item);

    }

    private void deleteAllNotes() {

        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int button) {

                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            getContentResolver().delete(
                                    DatabaseContentProvider.CONTENT_URI,
                                    null,
                                    null
                            );

                            restartLoader();

                            Toast.makeText(
                                    MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }

                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(
                        getString(android.R.string.yes),
                        dialogClickListener
                )
                .setNegativeButton(
                        getString(android.R.string.no),
                        dialogClickListener
                )
                .show();

    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                this,
                DatabaseContentProvider.CONTENT_URI,
                null,
                null,
                null,
                null
        );

    }

    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);

    }

    @Override public void onLoaderReset(Loader<Cursor> loader) {

        adapter.swapCursor(null);

    }
}
