package com.example.animaldispersal.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by user on 23/6/2016.
 */
public class GenderTable {
    public static final String TABLE_GENDER = "GENDER_TABLE";
    public static final String COLUMN_ACTIVE_FLAG = "ACTIVE_FLAG";
    public static final String COLUMN_GENDER_CODE="GENDER_CODE";
    public static final String COLUMN_GENDER="GENDER";
    public static final String COLUMN_LOCALE="LOCALE";
    public static final String COLUMN_LAST_UPDATE_USER="LAST_UPDATE_USER";
    public static final String COLUMN_LAST_UPDATE_TIMESTAMP="LAST_UPDATE_TIMESTAMP";
    public static final String COLUMN_DELETE_USER="DELETE_USER";
    public static final String COLUMN_DELETE_TIMESTAMP="DELETE_TIMESTAMP";
    public static final String COLUMN_CREATE_USER="CREATE_USER";
    public static final String COLUMN_CREATE_TIMESTAMP="CREATE_TIMESTAMP";



    private static final String DATABASE_CREATE = "create table "
            + TABLE_GENDER
            + "("
            +   COLUMN_ACTIVE_FLAG         + " text, "
            +	COLUMN_GENDER_CODE	+ " text not null, "
            +	COLUMN_GENDER	+ " text, "
            +	COLUMN_LOCALE	+ " text, "
            +   COLUMN_LAST_UPDATE_USER + " text, "
            +   COLUMN_LAST_UPDATE_TIMESTAMP + " text, "
            +   COLUMN_DELETE_USER + " text, "
            +   COLUMN_DELETE_TIMESTAMP + " text, "
            +   COLUMN_CREATE_USER + " text, "
            +   COLUMN_CREATE_TIMESTAMP + " text, "
            +   "PRIMARY KEY ("+COLUMN_ACTIVE_FLAG+","+COLUMN_GENDER_CODE+","+COLUMN_LOCALE+")"
            + ");";

    public GenderTable(Context context) {
        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(context);
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(EventTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " );
        onCreate(database);
    }

}
