package com.example.diary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.diary.data.DiaryContract;

import androidx.annotation.Nullable;

public class DiaryDBOpenHandler extends SQLiteOpenHelper {
    public DiaryDBOpenHandler(Context context) {
        super(context, DiaryContract.DATABASE_NAME, null, DiaryContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    String CREATE_TABLE = "CREATE TABLE " + DiaryContract.NewNote.TABLE_NAME + "("
            + DiaryContract.NewNote.KEY_ID + " INTEGER PRIMARY KEY, "
            + DiaryContract.NewNote.KEY_TITLE + " TEXT, "
            + DiaryContract.NewNote.KEY_TEXT + " TEXT, "
            + DiaryContract.NewNote.KEY_DATE + " TEXT, "
            + DiaryContract.NewNote.KEY_ALERT_TIME + " TEXT)";
//            + DiaryContract.NewNote.KEY_SECTION + " TEXT, "
//            + DiaryContract.NewNote.KEY_TYPE_OF_NOTE + " TEXT)";
    db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DiaryContract.NewNote.TABLE_NAME);

        onCreate(db);
    }
}
