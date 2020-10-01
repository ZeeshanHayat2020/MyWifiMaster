package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "Database Helper";

    private static final String TABLE_NAME = "Show_password";
    private static final String COl1 = "ID";
    private static final String COL2 = "name";
    private static final String COL3 = "password";
    private static final String COlasterik = "*";
    private static final String TABLE_NAME1 = "Show_SSID";
    private static final String COlID = "ID";
    private static final String COLName = "name";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT," + COL3 + " TEXT)";
        db.execSQL(createTable);
        String createTable1 = "CREATE TABLE " + TABLE_NAME1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLName + " TEXT)";
        db.execSQL(createTable1);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        onCreate(db);
    }

    public boolean addSSID(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues2 = new ContentValues();

        contentValues.put(COLName, item);

        Log.e(TAG, "addData: Adding " + item + " " + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME1, null, contentValues);
        db.insert(TABLE_NAME1, null, contentValues2);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addData(String item, String item2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        ContentValues contentValues2 = new ContentValues();

        contentValues.put(COL2, item);
        contentValues.put(COL3, item2);
        Log.e(TAG, "addData: Adding " + item + " " + item2 + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteData(String columnName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL2 + " = ?",
                new String[]{String.valueOf(columnName)});
        db.close();
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean getItemID(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COlasterik + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'" +
                " OR "
                + COL3 + " = '" + password + "'";
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        if (data.getCount() > 0) {
            return true;
        }
        data.close();
        db.close();
        return false;
    }

    public boolean getItemcheck(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COlasterik + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'" +
                " AND "
                + COL3 + " = '" + password + "'";
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        if (data.getCount() > 0) {
            return true;
        }
        data.close();
        db.close();
        return false;
    }

    public void updateName(String newName, int id, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COl1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.e(TAG, "updateName: query: " + query);
        Log.e(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    public void deleteName(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COl1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.e(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }

    //TODO: check pwd if exist
    public boolean Checkpwd(String pwd) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Get data from table
        String query = "select * from  " + TABLE_NAME + " where " +
                COL3 + " = " + "'" + pwd + "'";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return true;
        }
        cursor.close();
        db.close();

        return false;
    }

    //TODO: check name if exist
    public boolean Checkname(String pwd) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Get data from table

        String query = "select * from  " + TABLE_NAME + " where " + COL2 + " = " + "'" + pwd + "'";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return true;
        }
        cursor.close();
        db.close();

        return false;
    }

    public String getpwd(String pwd) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Get data from table
        String query = "select * from  " + TABLE_NAME + " where " +
                COL2 + " = " + "'" + pwd + "'";
        Cursor cursor = db.rawQuery(query, null);
        StringBuffer stringBuffer = new StringBuffer();
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            String cmdname = cursor.getString(cursor.getColumnIndex(COL3));
            stringBuffer.append(cmdname);
        }
        cursor.close();
        db.close();

        return stringBuffer.toString();
    }


    //todo: get pass number
    public String getSSId() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "select * from  " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {
            try {
                String cmdname = cursor.getString(cursor.getColumnIndex(COL2));
                stringBuffer.append(cmdname);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return stringBuffer.toString();
    }
}
