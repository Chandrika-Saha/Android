package com.example.legendstale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Blob;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "legendtale.db";
    public static final String TABLE_NAME = "legend";
    public static final String COL_1 = "name";
    public static final String COL_2 = "description";
    public static final String COL_3 = "twitter";
    public static final String COL_4 = "web";
    public static final String COL_5 = "image";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,description TEXT,twitter TEXT,web TEXT,image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String description, String twitter, String web, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,name);
        contentValues.put(COL_2,description);
        contentValues.put(COL_3,twitter);
        contentValues.put(COL_4,web);
        contentValues.put(COL_5,image);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME,null);
        return res;
    }

    public Cursor getOneData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where id='"+id+"'",null);
        return res;
    }


}
