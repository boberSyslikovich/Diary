package com.example.diary.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.diary.R;
import com.example.diary.data.DiaryContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class NotesForDay extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>{

    private FloatingActionButton addNote;
    private CalendarDay currentDay;

    private static final int NOTES_LOADER = 123;
     NoteCursorAdapter noteCursorAdapter;
     ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_for_day);

        currentDay = (CalendarDay) getIntent().getParcelableExtra("CalendarDay");
        setTitle(currentDay+"");
        Log.i("Day", currentDay+"");

        addNote = findViewById(R.id.addNoteFloatingActionButton);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesForDay.this, AddNewNote.class);

                intent.putExtra("CalendarDay", currentDay);

                startActivity(intent);
            }
        });


        lvItems = findViewById(R.id.listView);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NotesForDay.this, AddNewNote.class);

                Uri currentMemberUri = ContentUris.withAppendedId(DiaryContract.NewNote.CONTENT_URI, id);

                intent.setData(currentMemberUri);
                intent.putExtra("CalendarDay", currentDay);
                startActivity(intent);
            }
        });

        noteCursorAdapter = new NoteCursorAdapter(this, null);
        lvItems.setAdapter(noteCursorAdapter);
        getSupportLoaderManager().initLoader(NOTES_LOADER, null, this);

        lvItems.setEmptyView(findViewById(R.id.textView2));
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                DiaryContract.NewNote.KEY_ID,
                DiaryContract.NewNote.KEY_TITLE,
                DiaryContract.NewNote.KEY_TEXT,
                DiaryContract.NewNote.KEY_DATE,
                DiaryContract.NewNote.KEY_ALERT_TIME,
        };

        String selection = "(" + DiaryContract.NewNote.KEY_DATE + " =\'" +currentDay+"\')";

        CursorLoader cursorLoader = new CursorLoader(this,
                DiaryContract.NewNote.CONTENT_URI,
                projection,
                selection,
                null,
                null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        noteCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        noteCursorAdapter.swapCursor(null);
    }
}