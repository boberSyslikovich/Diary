package com.example.diary.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DiaryContentProvider extends ContentProvider {
    DiaryDBOpenHandler dbOpenHandler;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int NOTES = 111;
    private static final int NOTES_ID = 222;

    static {
        uriMatcher.addURI(DiaryContract.AUTHORITY, DiaryContract.PATH_NOTES, NOTES);

        uriMatcher.addURI(DiaryContract.AUTHORITY, DiaryContract.PATH_NOTES + "/#", NOTES_ID);
    }

    @Override
    public boolean onCreate() {
        dbOpenHandler = new DiaryDBOpenHandler(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbOpenHandler.getReadableDatabase();

        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case NOTES:
                cursor = db.query(DiaryContract.NewNote.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case NOTES_ID:
                selection = DiaryContract.NewNote._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(DiaryContract.NewNote.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cant query incorrect URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }



    @Nullable
    @Override
    public Uri insert( Uri uri,  ContentValues values) {
        String title = values.getAsString(DiaryContract.NewNote.KEY_TITLE);
        if (title == null){
            throw new IllegalArgumentException("You have to input title " + uri);
        }

        String noteText = values.getAsString(DiaryContract.NewNote.KEY_TEXT);
        if (noteText == null){
            throw new IllegalArgumentException("You have to input note text " + uri);
        }

//        String sport = values.getAsString(ClubOpympusContract.MemberEntry.KEY_SPORT);
//        if (sport == null){
//            throw new IllegalArgumentException("You have to input sport " + uri);
//        }

        String time = values.getAsString(DiaryContract.NewNote.KEY_ALERT_TIME);
        if (time == null){
            throw new IllegalArgumentException("You have to input time " + uri);
        }

        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();

        int match = uriMatcher.match(uri);
        switch (match) {
            case NOTES:
                long id = db.insert(DiaryContract.NewNote.TABLE_NAME, null, values);
                if (id == -1) {
                    Log.i("InsertMethod", "Insertion of data in the table failed for " + uri);
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Cant query incorrect URI " + uri);
        }
    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case NOTES:
                rowsDeleted = db.delete(DiaryContract.NewNote.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTES_ID:
                selection = DiaryContract.NewNote._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(DiaryContract.NewNote.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cant delete URI " + uri);
        }
        if (rowsDeleted !=0 ){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update( Uri uri,  ContentValues values,  String selection, @Nullable String[] selectionArgs) {
        if (values.containsKey(DiaryContract.NewNote.KEY_TITLE))
        {
            String title = values.getAsString(DiaryContract.NewNote.KEY_TITLE);
            if (title == null){
                throw new IllegalArgumentException("You have to input title " + uri);
            }
        }



        if (values.containsKey(DiaryContract.NewNote.KEY_TEXT))
        {
            String noteText = values.getAsString(DiaryContract.NewNote.KEY_TEXT);
            if (noteText == null){
                throw new IllegalArgumentException("You have to input text " + uri);
            }
        }


        if (values.containsKey(DiaryContract.NewNote.KEY_DATE))
        {
            String date = values.getAsString(DiaryContract.NewNote.KEY_DATE);
            if (date == null){
                throw new IllegalArgumentException("You have to input date " + uri);
            }
        }


        if (values.containsKey(DiaryContract.NewNote.KEY_ALERT_TIME))
        {
            String alertTime = values.getAsString(DiaryContract.NewNote.KEY_ALERT_TIME);
            if (alertTime == null){
                throw new IllegalArgumentException("You have to input alert time " + uri);
            }
        }


        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case NOTES:

                rowsUpdated = db.update(DiaryContract.NewNote.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOTES_ID:
                selection = DiaryContract.NewNote._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsUpdated = db.update(DiaryContract.NewNote.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cant update URI " + uri);
        }
        if (rowsUpdated !=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Nullable
    @Override
    public String getType( Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case NOTES:
                return DiaryContract.NewNote.CONTENT_MULTIPLE_ITEMS;
            case NOTES_ID:

                return DiaryContract.NewNote.CONTENT_SINGLE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown uri " + uri);
        }

    }
}
