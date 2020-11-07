package com.example.diary.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.diary.R;
import com.example.diary.data.DiaryContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor> {
    CalendarDay currentDay;

    private static final int NOTES_LOADER = 123;
    NoteCursorAdapter noteCursorAdapter;
    ListView lvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Щоденник");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MaterialCalendarView calendarView =(MaterialCalendarView) findViewById(R.id.calendarView);

        Date date = new Date();
        calendarView.setSelectedDate(date);
        FloatingActionButton addNote = findViewById(R.id.floatingActionButton);


        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotesForDay.class);
                currentDay = calendarView.getSelectedDate();
                intent.putExtra("CalendarDay", currentDay);

                startActivity(intent);
            }
        });

        lvItems = findViewById(R.id.allNoteListView);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (MainActivity.this, AddNewNote.class);

                Uri currentMemberUri = ContentUris.withAppendedId(DiaryContract.NewNote.CONTENT_URI, id);

                intent.setData(currentMemberUri);
                startActivity(intent);
            }
        });

        noteCursorAdapter = new NoteCursorAdapter(this, null);
        lvItems.setAdapter(noteCursorAdapter);
        getSupportLoaderManager().initLoader(NOTES_LOADER, null, this);

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
        CursorLoader cursorLoader = new CursorLoader( this,
                DiaryContract.NewNote.CONTENT_URI,
                projection,
                null,
                null,
                null);
        return  cursorLoader;
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
