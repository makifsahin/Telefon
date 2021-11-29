package com.example.telefon;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


class Database extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "sqllite_database";

    private static final String TABLE_NAME = "numaralar";
    private static String NUMARA_ID = "id";
    private static String NUMARA = "numara";
    private static String AD = "ad";
    private static String GRUP_ID = "grup_id";

    private static final String TABLE_NOTE_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    NUMARA_ID+ " INTEGER PRIMARY KEY , " +
                    NUMARA+ " TEXT , " +
                    AD+ " TEXT , " +
                    GRUP_ID + " TEXT"+
                    ") ";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        db.execSQL(TABLE_NOTE_CREATE);
    }



    public Boolean numaraEkle(String numara, String ad, String grup_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NUMARA, numara);
        values.put(AD, ad);
        values.put(GRUP_ID, grup_id);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result > -1;
    }

    public Boolean numaraSil(String numara) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME,NUMARA + "='" + numara+"'", null);
        db.close();
        return   result > 0;
    }

    @SuppressLint("Range")
    public String numaralarGrup(String grup){
        String numaralar = "";
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE "+ GRUP_ID +" = '"+grup+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Integer ints = cursor.getCount();
        while (cursor.moveToNext()){
            numaralar += cursor.getString(cursor.getColumnIndex(NUMARA)) +";";
        }
        cursor.close();
        db.close();
        return numaralar;
    }

    public Boolean numaraVarmi(String numara){

        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE "+ NUMARA+"='"+numara+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean durum = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return durum;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

}