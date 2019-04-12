package com.example.helloworld;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter {

    CustomCursorAdapter(Context context, Cursor c, int flags) {

        super(context, c, flags);

    }

    @Override public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(
                R.layout.list_item,
                parent,
                false
        );

    }

    @Override public void bindView(View view, Context context, Cursor cursor) {

        String noteBody = cursor.getString(
                cursor.getColumnIndex(DatabaseOpenHelper.NOTE_BODY)
        );

        int position = noteBody.indexOf(10); // Has a newline.

        if(position != -1) {

            noteBody = noteBody.substring(0, position) + "...";

        }

        TextView item = view.findViewById(R.id.listItem);
        item.setText(noteBody);

    }

}
