package com.example.bookkeeping;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpwnHelper extends SQLiteOpenHelper {
    public MyOpwnHelper(Context context){
        super(context, "BookKeep.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(uid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tel VARCHAR(20), password VARCHAR(20), name VARCHAR(20), assets REAL)");
        db.execSQL("CREATE TABLE income(icid INTEGER PRIMARY KEY AUTOINCREMENT, uid INTEGER, mount REAL, comment TEXT, time TEXT)");
        db.execSQL("CREATE TABLE output(opid INTEGER PRIMARY KEY AUTOINCREMENT, uid INTEGER, mount REAL, comment TEXT, time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
