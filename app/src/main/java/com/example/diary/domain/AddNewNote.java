package com.example.diary.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diary.R;
import com.example.diary.data.DiaryContract;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddNewNote extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private EditText titleEditText;
    private EditText noteEditText;
    private TimePicker timePicker;
    private CalendarDay selectedDay;

    Uri currentNoteUri;
    private static final int EDIT_NOTE_LOADER = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        Intent intent = getIntent();

        currentNoteUri = intent.getData();

        if (currentNoteUri == null){
            setTitle("Додати запис");
            invalidateOptionsMenu();
        } else{
            setTitle("Редагувати запис");
            getSupportLoaderManager().initLoader(EDIT_NOTE_LOADER, null,  this);
        }

        titleEditText = findViewById(R.id.titleEditText);
        noteEditText = findViewById(R.id.editText);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        selectedDay = getIntent().getParcelableExtra("CalendarDay");
        Log.i("Day", selectedDay+"");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_member:
                saveMember();
                return true;
            case R.id.delete_member:
                showDeleteNoteDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentNoteUri == null){
            MenuItem menuItem = menu.findItem(R.id.delete_member);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void saveMember() {
        String title = titleEditText.getText().toString().trim();
        String noteText = noteEditText.getText().toString().trim();
        String timeString = timePicker.getHour() + ":" + timePicker.getMinute();

        if (TextUtils.isEmpty(title)){
            Toast.makeText(this, "Input the title", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(noteText)){
            Toast.makeText(this, "Input the noteText", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(timeString)){
            Toast.makeText(this, "Input the alert time", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DiaryContract.NewNote.KEY_TITLE, title);
        contentValues.put(DiaryContract.NewNote.KEY_TEXT, noteText);
        contentValues.put(DiaryContract.NewNote.KEY_DATE, selectedDay+"");
        contentValues.put(DiaryContract.NewNote.KEY_ALERT_TIME, timeString);
        if (currentNoteUri == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(DiaryContract.NewNote.CONTENT_URI, contentValues);

            if (uri == null) {
                Toast.makeText(this, "Saving of data in the table failed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show();
            }
        } else{
            int rowsChanged = getContentResolver().update(currentNoteUri, contentValues, null, null);

            if (rowsChanged == 0){
                Toast.makeText(this, "Updated of data in the table failed", Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(this, "Data update", Toast.LENGTH_LONG).show();

            }
        }

        finish();
    }


    private void showDeleteNoteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Ви бажаэте видалити цей запис?");
        builder.setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMember();
            }
        });

        builder.setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteMember() {
        if (currentNoteUri != null){
            int rowsDeleted = getContentResolver().delete(currentNoteUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Deleting of data in the table failed", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Data deleted", Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                DiaryContract.NewNote.KEY_ID,
                DiaryContract.NewNote.KEY_TITLE,
                DiaryContract.NewNote.KEY_TEXT,
                DiaryContract.NewNote.KEY_DATE,
                DiaryContract.NewNote.KEY_ALERT_TIME
        };

        CursorLoader cursorLoader = new CursorLoader(this,
                currentNoteUri,
                projection,
                null,
                null,
                null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()){
            int titleColumnIndex = data.getColumnIndex(DiaryContract.NewNote.KEY_TITLE);
            int noteTextColumnIndex = data.getColumnIndex(DiaryContract.NewNote.KEY_TEXT);
            int dateColumnIndex = data.getColumnIndex(DiaryContract.NewNote.KEY_DATE);
            int timeColumnIndex = data.getColumnIndex(DiaryContract.NewNote.KEY_ALERT_TIME);

            String title = data.getString(titleColumnIndex);
            String noteText = data.getString(noteTextColumnIndex);
            String date = data.getString(dateColumnIndex);
            String time = data.getString(timeColumnIndex);

//            DateFormat formatter = new SimpleDateFormat("");
//            Date parseTime= (Date) formatter.parse(time);

//            parseTime.getHours()
            titleEditText.setText(title);
            noteEditText.setText(noteText);
//            timePicker.set;


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}