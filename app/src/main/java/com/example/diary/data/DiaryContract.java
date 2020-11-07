package com.example.diary.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Date;

public final class DiaryContract {
    private DiaryContract(){
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DiaryDB";

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.diary";
    public static final String PATH_NOTES = "notes";

    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    public static final class NewNote implements BaseColumns {
        public static final String TABLE_NAME = "notes";

        public static final String KEY_ID = BaseColumns._ID;
        public static final String KEY_TITLE = "title";
        public static final String KEY_TEXT = "text";
        public static final String KEY_DATE = "date";
        public static final String KEY_ALERT_TIME = "alertTime";
//        public static final String KEY_TYPE_OF_NOTE = "typeOfNote";
//        public static final String KEY_SECTION = "section";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTES);

        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_NOTES;
        public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"+ AUTHORITY + "/" + PATH_NOTES;


    }

//    public static final class TypeOfNote implements BaseColumns{
//        public static final String TABLE_NAME = "typeOfNote";
//
//        public static final String KEY_ID = BaseColumns._ID;
//        public static final String KEY_PERIODICITY_OF_NOTE = "periodicityOfNote";
//    }
//
//    public static final class Section implements BaseColumns{
//        public static final String TABLE_NAME = "section";
//
//        public static final String KEY_ID = BaseColumns._ID;
//        public static final String KEY_NAME_OF_SECTION = "nameOfSection";
//    }
}
