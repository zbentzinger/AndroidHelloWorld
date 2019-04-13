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
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";

    private static final int EDITOR_REQUEST_CODE = 1337;

    private CursorAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivityForResult(
                        new Intent(
                                MainActivity.this,
                                NoteEditorActivity.class
                        ),
                        EDITOR_REQUEST_CODE
                );
            }
        });

        adapter = new CustomCursorAdapter(
                this,
                null,
                0
        );

        ListView list = findViewById(android.R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);

                Uri uri = Uri.parse(DatabaseContentProvider.CONTENT_URI + "/" + id);

                intent.putExtra(DatabaseContentProvider.CONTENT_ITEM, uri);

                startActivityForResult(intent, EDITOR_REQUEST_CODE);

            }
        });

        getLoaderManager().initLoader(0, null, this);

        Log.d(TAG, "Loading MainActivity");

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

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {

            restartLoader();

        }

    }

}
