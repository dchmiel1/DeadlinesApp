package com.example.deadlinesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "DeadlineApp.db";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + DbNames.TABLE_NAME + "(" +
                    DbNames._ID + " INTEGER PRIMARY KEY, " +
                    DbNames.COLUMN_NAME_DATE + " TEXT, " +
                    DbNames.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                    DbNames.COLUMN_NAME_SUBJECT + " TEXT, " +
                    DbNames.COLUMN_NAME_IS_DONE + " BOOLEAN);";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + DbNames.TABLE_NAME;

    public DbHelper(Context c, MainActivity mainActivity) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    public Cursor getTasks(boolean isDone){
        int isDoneInt = 0;
        if(isDone)
            isDoneInt = 1;

        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + DbNames._ID + ", " + DbNames.COLUMN_NAME_DATE + ", " + DbNames.COLUMN_NAME_SUBJECT + ", " + DbNames.COLUMN_NAME_DESCRIPTION +
                " FROM " + DbNames.TABLE_NAME +
                " WHERE " + DbNames.COLUMN_NAME_IS_DONE + " = " + isDoneInt  +
                " ORDER BY " + DbNames.COLUMN_NAME_DATE, null);
    }

    public Cursor getById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + DbNames.COLUMN_NAME_DATE + ", " + DbNames.COLUMN_NAME_DESCRIPTION + ", " + DbNames.COLUMN_NAME_SUBJECT +
                            " FROM " + DbNames.TABLE_NAME +
                            " WHERE " + DbNames._ID + " = " + id, null);
    }

    public void insertTask(String subject, String date, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbNames.COLUMN_NAME_DATE, date);
        values.put(DbNames.COLUMN_NAME_SUBJECT, subject);
        values.put(DbNames.COLUMN_NAME_DESCRIPTION, description);
        values.put(DbNames.COLUMN_NAME_IS_DONE, false);
        db.insert(DbNames.TABLE_NAME, null, values);
    }

    public void showAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery( "select * from " + DbNames.TABLE_NAME, null );
        while(c.moveToNext()){
            System.out.println("************");
            System.out.println("ID: " + c.getInt(c.getColumnIndexOrThrow(DbNames._ID)));
            System.out.println("subject: " +c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_SUBJECT)));
            System.out.println("date: " + c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_DATE)));
            System.out.println("description: " + c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_DESCRIPTION)));
            System.out.println("is_done: " + c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_IS_DONE)));
        }
    }

    public void changeIsDone(boolean isDone, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbNames.COLUMN_NAME_IS_DONE, isDone);
        db.update(DbNames.TABLE_NAME, values, " _id = ?", new String[]{String.valueOf(id)});
    }

    public void update(int id, String subject, String date, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbNames.COLUMN_NAME_SUBJECT, subject);
        values.put(DbNames.COLUMN_NAME_DESCRIPTION, description);
        values.put(DbNames.COLUMN_NAME_DATE, date);
        db.update(DbNames.TABLE_NAME, values, " _id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tasks",
                "_id = ? ",
                new String[] { Integer.toString(id) });
    }
}
