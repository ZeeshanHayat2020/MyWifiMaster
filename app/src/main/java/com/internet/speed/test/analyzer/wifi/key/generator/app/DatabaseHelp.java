package com.internet.speed.test.analyzer.wifi.key.generator.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by HP# on 11/07/2018.
 */

public class DatabaseHelp extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="FindApp.db";
    //table and column name for trusted contact num table
    private static final String TABLE_NAME="trustedcontct_tbl";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_MESSAGE = "message";

    public int id= 1;
    public String cmdtype="LostPhone";
    public String cmdtype1="RingSilent";
    public String cmdtype2="CameraOn";
    public String cmdtype3="FlashOn";
    public String cmdtype4="LockPhone";
    public String cmdtype5="WifiOn";
    public String cmdtype6="WifiOff";
    public String cmdtype7="SimInfo";
    public String cmdtype8="SimIMEI";
    public String cmdtype9="ringplay";
    public String cmdtype10="WipeData";
    //table and column name for Commands table
    private static final String TABLE_NAME_1="lostphn_tbl";
   public static final String COLUMN_ID1 = "_id";
    public static final String COLUMN_CMD_TYPE = "cmd_type";
    public static final String COLUMN_CMD_NAME = "cmd_name";


    //table and column name for password table
    private static final String TABLE_NAME_2="pwd_tbl";
     public static final String COLUMN_ID2 = "_id";

    public static final String COLUMN_PWD= "password_field";


    private static final String TABLE_NAME_3="ringsilent_tbl";
    public static final String COLUMN_ID3 = "_id";
    public static final String COLUMN_CMD_TYPE3 = "cmd_type";
    public static final String COLUMN_CMD_NAME3 = "cmd_name";

    private static final String TABLE_NAME_4="cameraon_tbl";
    public static final String COLUMN_ID4 = "_id";
    public static final String COLUMN_CMD_TYPE4 = "cmd_type";
    public static final String COLUMN_CMD_NAME4 = "cmd_name";

    private static final String TABLE_NAME_5="flashon_tbl";
     public static final String COLUMN_ID5 = "_id";
    public static final String COLUMN_CMD_TYPE5 = "cmd_type";
    public static final String COLUMN_CMD_NAME5 = "cmd_name";

    private static final String TABLE_NAME_6="lockphn_tbl";
     public static final String COLUMN_ID6 = "_id";
    public static final String COLUMN_CMD_TYPE6 = "cmd_type";
    public static final String COLUMN_CMD_NAME6 = "cmd_name";

    private static final String TABLE_NAME_7="wifion_tbl";
   public static final String COLUMN_ID7 = "_id";
    public static final String COLUMN_CMD_TYPE7 = "cmd_type";
    public static final String COLUMN_CMD_NAME7 = "cmd_name";

    private static final String TABLE_NAME_8="wifioff_tbl";
    public static final String COLUMN_ID8 = "_id";
    public static final String COLUMN_CMD_TYPE8 = "cmd_type";
    public static final String COLUMN_CMD_NAME8 = "cmd_name";

    private static final String TABLE_NAME_9="siminfo_tbl";
     public static final String COLUMN_ID9 = "_id";
    public static final String COLUMN_CMD_TYPE9 = "cmd_type";
    public static final String COLUMN_CMD_NAME9 = "cmd_name";

    private static final String TABLE_NAME_10="simimei_tbl";
    public static final String COLUMN_ID10 = "_id";
    public static final String COLUMN_CMD_TYPE10 = "cmd_type";
    public static final String COLUMN_CMD_NAME10 = "cmd_name";

    private static final String TABLE_NAME_11="ringplay_tbl";
    public static final String COLUMN_ID11 = "_id";
    public static final String COLUMN_CMD_TYPE11 = "cmd_type";
    public static final String COLUMN_CMD_NAME11 = "cmd_name";

    private static final String TABLE_NAME_12="wipedata_tbl";
    public static final String COLUMN_ID12 = "_id";
    public static final String COLUMN_CMD_TYPE12 = "cmd_type";
    public static final String COLUMN_CMD_NAME12 = "cmd_name";

    private static final String TABLE_NAME_13="appdetails";
    public static final String COLUMN_ID13 = "_id";
    public static final String Trustedcontact = "trustedcntct";
    public static final String apppassword = "apppassword";
    public static final String activationcode = "activationcode";

    //table to save sim serial number
    private static final String TABLE_NAME_14="simserialtbl";
    public static final String COLUMN_ID14 = "_id";
    public static final String serialnum = "serialnumcolumn";



    public DatabaseHelp(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create table serial number
        String CREATE_TABLE_SIM_SERIAL = "CREATE TABLE " + TABLE_NAME_14 + "("
                + COLUMN_ID14 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + serialnum + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_SIM_SERIAL);
        //crerate trusted contact table
        String CREATE_TABLE_TRUSTED_CONTACT = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NUMBER + " TEXT,"
                + COLUMN_MESSAGE + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_TRUSTED_CONTACT);
        //crerate command table
        String CREATE_TABLE_COMMAND = "CREATE TABLE " + TABLE_NAME_1 + "("
                + COLUMN_ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CMD_TYPE + " TEXT,"
                + COLUMN_CMD_NAME + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_COMMAND);
        //crerate pwd table
        String CREATE_TABLE_PWD = "CREATE TABLE " + TABLE_NAME_2 + "("
                + COLUMN_PWD + " NUMBER)";

        sqLiteDatabase.execSQL(CREATE_TABLE_PWD);

        String CREATE_TABLE_3 =  "CREATE TABLE " + TABLE_NAME_3 + "("
                + COLUMN_ID3 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CMD_TYPE3 + " TEXT,"
                + COLUMN_CMD_NAME3 + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_3);

        String CREATE_TABLE_4 =  "CREATE TABLE " + TABLE_NAME_4 + "("
                + COLUMN_ID4 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CMD_TYPE4 + " TEXT,"
                + COLUMN_CMD_NAME4 + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_4);

        String CREATE_TABLE_5 =  "CREATE TABLE " + TABLE_NAME_5 + "("
                + COLUMN_ID5 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CMD_TYPE5 + " TEXT,"
                + COLUMN_CMD_NAME5 + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_5);

        String CREATE_TABLE_6 =  "CREATE TABLE " + TABLE_NAME_6 + "("
                + COLUMN_ID6 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CMD_TYPE6 + " TEXT,"
                + COLUMN_CMD_NAME6 + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_6);

        String CREATE_TABLE_7 =  "CREATE TABLE " + TABLE_NAME_7 + "("
                + COLUMN_ID7 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CMD_TYPE7 + " TEXT,"
                + COLUMN_CMD_NAME7 + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_7);

        String CREATE_TABLE_8 =  "CREATE TABLE " + TABLE_NAME_8 + "("
                + COLUMN_ID8 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CMD_TYPE8 + " TEXT,"
                + COLUMN_CMD_NAME8 + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_8);

        String CREATE_TABLE_9 =  "CREATE TABLE " + TABLE_NAME_9 + "("
                + COLUMN_ID9 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CMD_TYPE9 + " TEXT,"
                + COLUMN_CMD_NAME9 + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_9);

        String CREATE_TABLE_10 =  "CREATE TABLE " + TABLE_NAME_10 + "("
                + COLUMN_ID10 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CMD_TYPE10 + " TEXT,"
                + COLUMN_CMD_NAME10 + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_10);

        String CREATE_TABLE_11 =  "CREATE TABLE " + TABLE_NAME_11 + "("
                + COLUMN_ID11 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CMD_TYPE11 + " TEXT,"
                + COLUMN_CMD_NAME11 + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_11);

        String CREATE_TABLE_12 =  "CREATE TABLE " + TABLE_NAME_12 + "("
                + COLUMN_ID12 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CMD_TYPE12 + " TEXT,"
                + COLUMN_CMD_NAME12 + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_12);

        String CREATE_TABLE_13 =  "CREATE TABLE " + TABLE_NAME_13 + "("
                + COLUMN_ID13 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Trustedcontact + " TEXT,"
                + apppassword + " TEXT,"
                + activationcode + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_13);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_5);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_6);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_7);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_8);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_9);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_10);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_11);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_12);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_13);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_14);

        onCreate(sqLiteDatabase);
    }
    //insert sim serial number to db
    public boolean serialnumberadd(String serialnumber){

        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(serialnum,serialnumber);

        long result=  sqLiteDatabase.insert(TABLE_NAME_14,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted");
            return false;
        }
        else {
            Log.d("DBHelper","inserted");
            return true;
        }
    }
    //insert pwd in db
    public boolean pwd(String pwd){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_PWD,pwd);

        long result=  sqLiteDatabase.insert(TABLE_NAME_2,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted");
            return false;
        }
        else {
            Log.d("DBHelper","inserted");
            return true;
        }
    }

    //insert trusted contact in db
    public boolean trustedContact(String number,String msg){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_NUMBER,number);
        contentValues.put(COLUMN_MESSAGE,msg);
        long result=  sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted");
            return false;
        }
        else {
            Log.d("DBHelper","inserted");
            return true;
        }
    }

    //insert commands in db
    public boolean commands3(String cmdtype,String command){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_CMD_TYPE3,cmdtype);
        contentValues.put(COLUMN_CMD_NAME3,command);
        long result=  sqLiteDatabase.insert(TABLE_NAME_3,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted in command");
            return false;
        }
        else {
            Log.d("DBHelper","inserted in command");
            return true;
        }
    }

    //insert commands in db
    public boolean commands4(String cmdtype,String command){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_CMD_TYPE4,cmdtype);
        contentValues.put(COLUMN_CMD_NAME4,command);
        long result=  sqLiteDatabase.insert(TABLE_NAME_4,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted in command");
            return false;
        }
        else {
            Log.d("DBHelper","inserted in command");
            return true;
        }
    }

    //insert commands in db
    public boolean commands5(String cmdtype,String command){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_CMD_TYPE5,cmdtype);
        contentValues.put(COLUMN_CMD_NAME5,command);
        long result=  sqLiteDatabase.insert(TABLE_NAME_5,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted in command");
            return false;
        }
        else {
            Log.d("DBHelper","inserted in command");
            return true;
        }
    }

    //insert commands in db
    public boolean commands6(String cmdtype,String command){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_CMD_TYPE6,cmdtype);
        contentValues.put(COLUMN_CMD_NAME6,command);
        long result=  sqLiteDatabase.insert(TABLE_NAME_6,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted in command");
            return false;
        }
        else {
            Log.d("DBHelper","inserted in command");
            return true;
        }
    }

    //insert commands in db
    public boolean commands7(String cmdtype,String command){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_CMD_TYPE7,cmdtype);
        contentValues.put(COLUMN_CMD_NAME7,command);
        long result=  sqLiteDatabase.insert(TABLE_NAME_7,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted in command");
            return false;
        }
        else {
            Log.d("DBHelper","inserted in command");
            return true;
        }
    }

    //insert commands in db
    public boolean commands8(String cmdtype,String command){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_CMD_TYPE8,cmdtype);
        contentValues.put(COLUMN_CMD_NAME8,command);
        long result=  sqLiteDatabase.insert(TABLE_NAME_8,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted in command");
            return false;
        }
        else {
            Log.d("DBHelper","inserted in command");
            return true;
        }
    }

    //insert commands in db
    public boolean commands9(String cmdtype,String command){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_CMD_TYPE9,cmdtype);
        contentValues.put(COLUMN_CMD_NAME9,command);
        long result=  sqLiteDatabase.insert(TABLE_NAME_9,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted in command");
            return false;
        }
        else {
            Log.d("DBHelper","inserted in command");
            return true;
        }
    }
    //insert commands in db
    public boolean commands10(String cmdtype,String command){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_CMD_TYPE10,cmdtype);
        contentValues.put(COLUMN_CMD_NAME10,command);
        long result=  sqLiteDatabase.insert(TABLE_NAME_10,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted in command");
            return false;
        }
        else {
            Log.d("DBHelper","inserted in command");
            return true;
        }
    }
    //insert commands in db
    public boolean commands11(String cmdtype,String command){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_CMD_TYPE11,cmdtype);
        contentValues.put(COLUMN_CMD_NAME11,command);
        long result=  sqLiteDatabase.insert(TABLE_NAME_11,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted in command");
            return false;
        }
        else {
            Log.d("DBHelper","inserted in command");
            return true;
        }
    }
    //insert commands in db
    public boolean commands12(String cmdtype,String command){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_CMD_TYPE12,cmdtype);
        contentValues.put(COLUMN_CMD_NAME12,command);
        long result=  sqLiteDatabase.insert(TABLE_NAME_12,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted in command");
            return false;
        }
        else {
            Log.d("DBHelper","inserted in command");
            return true;
        }
    }
    //insert commands in db
    public boolean commands(String cmdtype,String command){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_CMD_TYPE,cmdtype);
        contentValues.put(COLUMN_CMD_NAME,command);
        long result=  sqLiteDatabase.insert(TABLE_NAME_1,null,contentValues);
        sqLiteDatabase.close();

        if (result==-1) {
            Log.d("DBHelper", "not inserted in command");
            return false;
        }
        else {
            Log.d("DBHelper","inserted in command");
            return true;
        }
    }
    //fectch trusted contact
    public Cursor getContact(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        String query = "select * from  " + TABLE_NAME + " where " +
                COLUMN_ID + " = " + "'"+id+"'";
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        return cursor;
    }

    //fectch lost phone command
    public String getLostCommands(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        String query = "select * from  " + TABLE_NAME_1;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
             String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_CMD_NAME));
             stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
    //fectch ring command
    public String getRingCommands(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        String query = "select * from  " + TABLE_NAME_3;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_CMD_NAME3));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }

    //fectch wifi on command
    public String getWifiOnCommands(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();

        String query = "select * from  " + TABLE_NAME_7;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_CMD_NAME7));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }

    //fectch wifi off command
    public String getWifiOffCommands(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();

        String query = "select * from  " + TABLE_NAME_8;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_CMD_NAME8));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
    //fectch Camera On command
    public String getCameraONCommands(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();

        String query = "select * from  " + TABLE_NAME_4;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_CMD_NAME4));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
    //fectch Flash On command
    public String getFlashONCommands(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();

        String query = "select * from  " + TABLE_NAME_5;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_CMD_NAME5));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
    //fectch ringtone command
    public String getRingToneCommands(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();

        String query = "select * from  " + TABLE_NAME_11;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_CMD_NAME11));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
    //fectch lock phone command
    public String getLockPhoneommands(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();

        String query = "select * from  " + TABLE_NAME_6;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_CMD_NAME6));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
    //fectch sim info command
    public String getSimInfoeommands(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();

        String query = "select * from  " + TABLE_NAME_9;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_CMD_NAME9));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
    //fectch sim imei command
    public String getSimImeieommands(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();

        String query = "select * from  " + TABLE_NAME_10;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_CMD_NAME10));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
    //fectch wipe data command
    public String getWipeDataCommands(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();

        String query = "select * from  " + TABLE_NAME_12;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_CMD_NAME12));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
    //login to account
    public boolean loginuser(String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Get data from table
        String query = "select * from  " + TABLE_NAME_2 + " where " +
                COLUMN_PWD + " = " + "'"+password+"'";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return true;
        }
        cursor.close();
        db.close();

        return false;
    }

    //fectch password
    public String getpassword(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        String query = "select * from  " + TABLE_NAME_2;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_PWD));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
    //fectch wipe data command
    public String getNumber(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        String query = "select * from  " + TABLE_NAME + " where " +
                COLUMN_ID + " = " + "'"+id+"'";
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }

    //update number
    public boolean updatenumber(String id,String num,String msg){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID, id);
        args.put(COLUMN_NUMBER, num);
        args.put(COLUMN_MESSAGE, msg);
        int i =  sqLiteDatabase.update(TABLE_NAME, args, COLUMN_ID + "=" + id, null);
        return i > 0;

    }
    //update lostphone
    public boolean updatelostphone(String id,String type,String name){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID, id);
        args.put(COLUMN_CMD_TYPE, type);
        args.put(COLUMN_CMD_NAME, name);
        int i =  sqLiteDatabase.update(TABLE_NAME_1, args, COLUMN_ID1 + "=" + id, null);
        Log.d("DBHelper","Updated table"+TABLE_NAME_1);
        return i > 0;

    }
    //update ring silent
    public boolean updateringsilent(String id,String type,String name){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID3, id);
        args.put(COLUMN_CMD_TYPE3, type);
        args.put(COLUMN_CMD_NAME3, name);
        int i =  sqLiteDatabase.update(TABLE_NAME_3, args, COLUMN_ID3 + "=" + id, null);
        Log.d("DBHelper","Updated table"+TABLE_NAME_3);
        return i > 0;

    }
    //update wifi on
    public boolean wifion(String id,String type,String name){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID7, id);
        args.put(COLUMN_CMD_TYPE7, type);
        args.put(COLUMN_CMD_NAME7, name);
        int i =  sqLiteDatabase.update(TABLE_NAME_7, args, COLUMN_ID7 + "=" + id, null);
        Log.d("DBHelper","Updated table"+TABLE_NAME_7);
        return i > 0;

    }
    //update wifioff
    public boolean updatewifioff(String id,String type,String name){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID8, id);
        args.put(COLUMN_CMD_TYPE8, type);
        args.put(COLUMN_CMD_NAME8, name);
        int i =  sqLiteDatabase.update(TABLE_NAME_8, args, COLUMN_ID8 + "=" + id, null);
        Log.d("DBHelper","Updated table"+TABLE_NAME_8);
        return i > 0;

    }
    //update camera on
    public boolean updatecameraon(String id,String type,String name){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID4, id);
        args.put(COLUMN_CMD_TYPE4, type);
        args.put(COLUMN_CMD_NAME4, name);
        int i =  sqLiteDatabase.update(TABLE_NAME_4, args, COLUMN_ID4 + "=" + id, null);
        Log.d("DBHelper","Updated table"+TABLE_NAME_4);
        return i > 0;

    }
    //update flash on
    public boolean updateflashon(String id,String type,String name){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID5, id);
        args.put(COLUMN_CMD_TYPE5, type);
        args.put(COLUMN_CMD_NAME5, name);
        int i =  sqLiteDatabase.update(TABLE_NAME_5, args, COLUMN_ID5 + "=" + id, null);
        Log.d("DBHelper","Updated table"+TABLE_NAME_5);
        return i > 0;

    }
    //update play ring
    public boolean updateplayring(String id,String type,String name){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID11, id);
        args.put(COLUMN_CMD_TYPE11, type);
        args.put(COLUMN_CMD_NAME11, name);
        int i =  sqLiteDatabase.update(TABLE_NAME_11, args, COLUMN_ID11 + "=" + id, null);
        Log.d("DBHelper","Updated table"+TABLE_NAME_11);
        return i > 0;

    }
    //update lock phone
    public boolean updatelockphone(String id,String type,String name){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID6, id);
        args.put(COLUMN_CMD_TYPE6, type);
        args.put(COLUMN_CMD_NAME6, name);
        int i =  sqLiteDatabase.update(TABLE_NAME_6, args, COLUMN_ID6 + "=" + id, null);
        Log.d("DBHelper","Updated table"+TABLE_NAME_6);
        return i > 0;

    }
    //update imei num
    public boolean updateimeinum(String id,String type,String name){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID10, id);
        args.put(COLUMN_CMD_TYPE10, type);
        args.put(COLUMN_CMD_NAME10, name);
        int i =  sqLiteDatabase.update(TABLE_NAME_10, args, COLUMN_ID10 + "=" + id, null);
        Log.d("DBHelper","Updated table"+TABLE_NAME_10);
        return i > 0;

    }
    //update imei num
    public boolean updatesiminfo(String id,String type,String name){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID9, id);
        args.put(COLUMN_CMD_TYPE9, type);
        args.put(COLUMN_CMD_NAME9, name);
        int i =  sqLiteDatabase.update(TABLE_NAME_9, args, COLUMN_ID9 + "=" + id, null);
        Log.d("DBHelper","Updated table"+TABLE_NAME_9);
        return i > 0;

    }
    //update wipe daa
    public boolean updatewipedata(String id,String type,String name){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID12, id);
        args.put(COLUMN_CMD_TYPE12, type);
        args.put(COLUMN_CMD_NAME12, name);
        int i =  sqLiteDatabase.update(TABLE_NAME_12, args, COLUMN_ID12 + "=" + id, null);
        Log.d("DBHelper","Updated table"+TABLE_NAME_12);
        return i > 0;

    }
    //fetch imei num
    public String getIMEINumber(){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();

        String query = "select * from  " + TABLE_NAME_14;
        Cursor cursor= sqLiteDatabase.rawQuery(query , null);
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            try {
                String cmdname=   cursor.getString(cursor.getColumnIndex(serialnum));
                stringBuffer.append(cmdname);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
}
