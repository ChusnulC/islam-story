package com.example.joyleap.islamstory.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "IslamStory";
    private static final int DATABASE_VERSION = 1;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE story (story_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, kategori TEXT," +
                        "judul TEXT, deskripsi TEXT, isi TEXT );"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS story");
    }
}

