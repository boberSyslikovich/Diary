package com.example.diary.domain;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.diary.R;
import com.example.diary.data.DiaryContract;

public class NoteCursorAdapter extends CursorAdapter {
    public NoteCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.note, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView noteTextView = view.findViewById(R.id.textView);
        TextView timeTextView = view.findViewById(R.id.timeTextView);

        String title = cursor.getString((cursor.getColumnIndexOrThrow(DiaryContract.NewNote.KEY_TITLE)));
        String noteText = cursor.getString((cursor.getColumnIndexOrThrow(DiaryContract.NewNote.KEY_TEXT)));
        String time = cursor.getString((cursor.getColumnIndexOrThrow(DiaryContract.NewNote.KEY_ALERT_TIME)));

        titleTextView.setText(title);
        noteTextView.setText(noteText);
        timeTextView.setText(time);

    }
}
