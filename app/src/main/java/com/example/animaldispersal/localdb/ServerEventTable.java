package com.example.animaldispersal.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by user on 23/6/2016.
 */
public class ServerEventTable {
    public static final String TABLE_SERVER_EVENT = "SERVER_EVENT_TABLE";
    public static final String COLUMN_ACTIVE_FLAG = "ACTIVE_FLAG";
    public static final String COLUMN_ANIMAL_ID="ANIMAL_ID";
    public static final String COLUMN_EVENT_ID="EVENT_ID";
    public static final String COLUMN_EVENT_TYPE="EVENT_TYPE";
    public static final String COLUMN_EVENT_DATE="EVENT_DATE";
    public static final String COLUMN_EVENT_TIMESTAMP="EVENT_TIMESTAMP";
    public static final String COLUMN_EVENT_REMARKS="EVENT_REMARKS";
    public static final String COLUMN_RECORD_TYPE="RECORD_TYPE";
    public static final String COLUMN_LAST_UPDATE_USER="LAST_UPDATE_USER";
    public static final String COLUMN_LAST_UPDATE_TIMESTAMP="LAST_UPDATE_TIMESTAMP";
    public static final String COLUMN_DELETE_USER="DELETE_USER";
    public static final String COLUMN_DELETE_TIMESTAMP="DELETE_TIMESTAMP";
    public static final String COLUMN_CREATE_USER="CREATE_USER";
    public static final String COLUMN_CREATE_TIMESTAMP="CREATE_TIMESTAMP";
    public static final String COLUMN_SYNC="SYNC";
    public static final String COLUMN_SYNC_MESSAGE="SYNC_MESSAGE";
    public static final String COLUMN_NFC_SCAN_ENTRY_TIMESTAMP="NFC_SCAN_ENTRY_TIMESTAMP";
    public static final String COLUMN_NFC_SCAN_SAVE_TIMESTAMP="NFC_SCAN_SAVE_TIMESTAMP";


    private static final String DATABASE_CREATE = "create table "
            + TABLE_SERVER_EVENT
            + "("
            +   COLUMN_ACTIVE_FLAG         + " text, "
            +	COLUMN_ANIMAL_ID	+ " text not null, "
            +	COLUMN_EVENT_ID	+ " text not null, "
            +	COLUMN_EVENT_TYPE	+ " text not null, "
            +	COLUMN_EVENT_DATE	+ " text not null, "
            +	COLUMN_EVENT_TIMESTAMP	+ " text not null, "
            +	COLUMN_EVENT_REMARKS	+ " text, "
            +   COLUMN_RECORD_TYPE + " text, "
            +   COLUMN_SYNC + " text, "
            +   COLUMN_SYNC_MESSAGE + " text, "
            +   COLUMN_LAST_UPDATE_USER + " text, "
            +   COLUMN_LAST_UPDATE_TIMESTAMP + " text, "
            +   COLUMN_DELETE_USER + " text, "
            +   COLUMN_DELETE_TIMESTAMP + " text, "
            +   COLUMN_CREATE_USER + " text, "
            +   COLUMN_CREATE_TIMESTAMP + " text, "
            +   COLUMN_NFC_SCAN_ENTRY_TIMESTAMP + " text, "
            +   COLUMN_NFC_SCAN_SAVE_TIMESTAMP + " text "
            + ");";

    public ServerEventTable(Context context) {
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
