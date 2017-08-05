package com.example.animaldispersal.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by user on 23/6/2016.
 */
public class AnimalTypeTable {
    public static final String TABLE_GENDER = "ANIMAL_TYPE_TABLE";
    public static final String COLUMN_ACTIVE_FLAG = "ACTIVE_FLAG";
    public static final String COLUMN_ANIMAL_TYPE_CODE="ANIMAL_TYPE_CODE";
    public static final String COLUMN_ANIMAL_TYPE="ANIMAL_TYPE";
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
            +	COLUMN_ANIMAL_TYPE_CODE	+ " text not null, "
            +	COLUMN_ANIMAL_TYPE	+ " text, "
            +	COLUMN_LOCALE	+ " text, "
            +   COLUMN_LAST_UPDATE_USER + " text, "
            +   COLUMN_LAST_UPDATE_TIMESTAMP + " text, "
            +   COLUMN_DELETE_USER + " text, "
            +   COLUMN_DELETE_TIMESTAMP + " text, "
            +   COLUMN_CREATE_USER + " text, "
            +   COLUMN_CREATE_TIMESTAMP + " text, "
            +   "PRIMARY KEY ("+COLUMN_ACTIVE_FLAG+","+COLUMN_ANIMAL_TYPE_CODE+","+COLUMN_LOCALE+")"
            + ");";

    public AnimalTypeTable(Context context) {
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
