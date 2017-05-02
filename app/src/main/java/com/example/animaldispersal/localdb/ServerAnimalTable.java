package com.example.animaldispersal.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by user on 28/5/2016.
 */
public class ServerAnimalTable {


    public static final String TABLE_SERVER_ANIMAL = "SERVER_ANIMAL_TABLE";
    public static final String COLUMN_ACTIVE_FLAG = "ACTIVE_FLAG";
    public static final String COLUMN_ANIMAL_ID="ANIMAL_ID";
    public static final String COLUMN_SUPERVISOR="SUPERVISOR";
    public static final String COLUMN_CARETAKER_UID="CARETAKER_UID";
    public static final String COLUMN_ANIMAL_TYPE="ANIMAL_TYPE";
    public static final String COLUMN_RECORD_TYPE = "RECORD_TYPE";
    public static final String COLUMN_GENDER="GENDER";
    public static final String COLUMN_DATE_OF_BIRTH="DATE_OF_BIRTH";
    public static final String COLUMN_COUNTRY="COUNTRY";
    public static final String COLUMN_DATE_PURCHASED="DATE_PURCHASED";
    public static final String COLUMN_PURCHASE_PRICE="PURCHASE_PRICE";
    public static final String COLUMN_DATE_DISTRIBUTED="DATE_DISTRIBUTED";
    public static final String COLUMN_DATE_SOLD="DATE_SOLD";
    public static final String COLUMN_SALE_PRICE="SALE_PRICE";
    public static final String COLUMN_LAST_UPDATE_USER="LAST_UPDATE_USER";
    public static final String COLUMN_LAST_UPDATE_TIMESTAMP="LAST_UPDATE_TIMESTAMP";
    public static final String COLUMN_DELETE_USER="DELETE_USER";
    public static final String COLUMN_DELETE_TIMESTAMP="DELETE_TIMESTAMP";
    public static final String COLUMN_CREATE_USER="CREATE_USER";
    public static final String COLUMN_CREATE_TIMESTAMP="CREATE_TIMESTAMP";
    public static final String COLUMN_NFC_SCAN_ENTRY_TIMESTAMP="NFC_SCAN_ENTRY_TIMESTAMP";
    public static final String COLUMN_NFC_SCAN_SAVE_TIMESTAMP="NFC_SCAN_SAVE_TIMESTAMP";
    public static final String COLUMN_SYNC= "SYNC";
    public static final String COLUMN_SYNC_MESSAGE= "SYNC_MESSAGE";


    private static final String DATABASE_CREATE = "create table "
            + TABLE_SERVER_ANIMAL
            + "("
            +   COLUMN_ACTIVE_FLAG  + " text, "
            +	COLUMN_ANIMAL_ID	+ " text not null, "
            +	COLUMN_SUPERVISOR	+ " text not null, "
            +	COLUMN_CARETAKER_UID	+ " text, "
            +	COLUMN_ANIMAL_TYPE	+ " text not null, "
            +   COLUMN_RECORD_TYPE + " text, "
            +	COLUMN_GENDER	+ " text, "
            +	COLUMN_DATE_OF_BIRTH	+ " text, "
            +	COLUMN_COUNTRY	+ " text, "
            +	COLUMN_DATE_PURCHASED	+ " text, "
            +	COLUMN_PURCHASE_PRICE	+ " text, "
            +	COLUMN_DATE_DISTRIBUTED	+ " text, "
            +	COLUMN_DATE_SOLD	+ " text, "
            +	COLUMN_SALE_PRICE	+ " text, "
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

    public ServerAnimalTable(Context context) {
        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(context);
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(AnimalTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " );
        onCreate(database);
    }
}
