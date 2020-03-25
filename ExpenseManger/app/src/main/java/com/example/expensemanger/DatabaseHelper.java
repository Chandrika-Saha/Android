package com.example.expensemanger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "expensemanagerincome.db";
    public static final String TABLE_NAME = "expense";
    public static final String TABLE_INCOME = "income";
    public static final String COL_1 = "email";
    public static final String COL_2 = "category";
    public static final String COL_3 = "name";
    public static final String COL_4 = "description";
    public static final String COL_5 = "price";
    public static final String COL_6 = "date";
    public static final String COL_7 = "payment";
    public static final String COL_2_I = "mail";
    public static final String COL_3_I = "income";
    public static final String COL_4_I = "datein";
    public static final String COL_5_I = "amount";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (id INTEGER PRIMARY KEY AUTOINCREMENT,email TEXT,category TEXT,name TEXT,description TEXT,price INTEGER,date DATE,payment TEXT)");
        db.execSQL("create table " + TABLE_INCOME +" (id_income INTEGER PRIMARY KEY AUTOINCREMENT, mail TEXT,income TEXT,datein DATE, amount INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String email, String category, String name, String description, int price, String date, String pay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,email);
        contentValues.put(COL_2,category);
        contentValues.put(COL_3,name);
        contentValues.put(COL_4,description);
        contentValues.put(COL_5,price);
        contentValues.put(COL_6,date);
        contentValues.put(COL_7,pay);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertDataIncome(String personEmail,String type,String datein, int amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_I,personEmail);
        contentValues.put(COL_3_I,type);
        contentValues.put(COL_4_I,datein);
        contentValues.put(COL_5_I,amount);
        long result = db.insert(TABLE_INCOME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;

    }

    public Cursor getAllData(String eemail) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where email='"+eemail+"'"+"order by date desc",null);
        return res;
    }

    public Cursor getAllDataIncome(String eemail) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_INCOME + " where mail='"+eemail+"'"+"order by datein desc",null);
        return res;
    }

    public Integer deleteData (String email, String cat, String name, String des, int p,String date, String pay) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_1 + "= ? and "+ COL_2 + "= ? and "+COL_3 + "= ? and "+COL_4 + "= ? and "+COL_5 + "= ? and "+COL_6 + "= ? and "+ COL_7 + "= ?",new String[]{email, cat, name, des, String.valueOf(p), date, pay});
    }

    public Integer deleteDataIncome(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_INCOME,  "id_income = ?",new String[]{String.valueOf(id)});
    }
}
