package com.example.animaldispersal.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.animaldispersal.dataobject.Animal;
import com.example.animaldispersal.dataobject.Caretaker;
import com.example.animaldispersal.dataobject.Event;
import com.example.animaldispersal.dataobject.SystemProperty;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class LocalDBHelper extends SQLiteOpenHelper {

    private static final String TAG = LocalDBHelper.class.getName();

    public static final String LOCAL_ANIMAL_TABLE = "ANIMAL_TABLE";
    public static final String LOCAL_EVENT_TABLE = "EVENT_TABLE";
    public static final String SERVER_ANIMAL_TABLE = "SERVER_ANIMAL_TABLE";

    private static LocalDBHelper sInstance;
    private static String country;
    private static String username;
    private static SharedPreferences mPrefs;

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    public static synchronized LocalDBHelper getInstance(Context context) {

        mPrefs = context.getSharedPreferences("animalDispersalPrefs", Context.MODE_PRIVATE);
        //country = mPrefs.getString("country","");
        username = mPrefs.getString ("username","SYSTEM");
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new LocalDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public LocalDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        AnimalTable.onCreate(database);
        EventTable.onCreate(database);
        ServerAnimalTable.onCreate(database);
        ServerEventTable.onCreate(database);
        CaretakerTable.onCreate(database);
        ServerCaretakerTable.onCreate(database);
        SystemPropertiesTable.onCreate(database);
        GenderTable.onCreate(database);
        CountryTable.onCreate(database);
        AnimalTypeTable.onCreate(database);

        insertSystemProperty(database, "LAST_SYNC_TIMESTAMP","");
        insertSystemProperty(database, "LAST_SYNC_MESSAGE","");

        insertGender(database, "0","Male","en");
        insertGender(database, "1","Female","en");
        insertGender(database, "0","এঁড়ে","bn");
        insertGender(database, "1","মাদী","bn");
        insertGender(database, "0","نر","ur");
        insertGender(database, "1","مادہ","ur");

        insertCountry(database, "1","Bangladesh","en");
        insertCountry(database, "2","Pakistan","en");
        insertCountry(database, "3","Philippines","en");
        insertCountry(database, "1","বাংলাদেশ","bn");
        insertCountry(database, "2","পাকিস্তান","bn");
        insertCountry(database, "3","ফিলিপাইন","bn");
        insertCountry(database, "1","بنگلا دیش","ur");
        insertCountry(database, "2","پاکستان","ur");
        insertCountry(database, "3","فلپائن","ur");

        insertAnimalType(database, "0","Cow","en");
        insertAnimalType(database, "1","Goat","en");
        insertAnimalType(database, "2","Pig","en");
        insertAnimalType(database, "0","গরু","bn");
        insertAnimalType(database, "1","ছাগল","bn");
        insertAnimalType(database, "2","শুকর","bn");
        insertAnimalType(database, "0","گائے","ur");
        insertAnimalType(database, "1","بکری","ur");
        insertAnimalType(database, "2","خنزیر","ur");
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        AnimalTable.onUpgrade(database, oldVersion, newVersion);
        EventTable.onUpgrade(database,oldVersion,newVersion);
        ServerAnimalTable.onUpgrade(database,oldVersion,newVersion);
        ServerEventTable.onUpgrade(database,oldVersion,newVersion);
        CaretakerTable.onUpgrade(database,oldVersion,newVersion);
        ServerCaretakerTable.onUpgrade(database,oldVersion,newVersion);
        SystemPropertiesTable.onUpgrade(database,oldVersion,newVersion);
    }

    public boolean insertAnimal (Animal animal){
        SQLiteDatabase database = this.getWritableDatabase();

        //add record
        ContentValues contentValues = new ContentValues();
        contentValues.put("ACTIVE_FLAG", "A");
        contentValues.put("ANIMAL_ID", animal.getAnimalId());
        contentValues.put("SUPERVISOR", animal.getSupervisor());
        contentValues.put("CARETAKER_UID", animal.getCaretakerUid());
        contentValues.put("ANIMAL_TYPE", animal.getAnimalType());
        contentValues.put("GENDER", animal.getGender());
        contentValues.put("DATE_OF_BIRTH",animal.getDateOfBirth());
        contentValues.put("COUNTRY", animal.getCountry());
        contentValues.put("DATE_PURCHASED", animal.getDatePurchased());
        contentValues.put("PURCHASE_PRICE", animal.getPurchasePrice());
        contentValues.put("DATE_DISTRIBUTED", animal.getDateDistributed());
        contentValues.put("PURCHASE_WEIGHT", animal.getPurchaseWeight());
        contentValues.put("PURCHASE_WEIGHT_UNIT", animal.getPurchaseWeightUnit());
        contentValues.put("PURCHASE_HEIGHT", animal.getPurchaseHeight());
        contentValues.put("PURCHASE_HEIGHT_UNIT", animal.getPurchaseHeightUnit());
        contentValues.put("DATE_SOLD", animal.getDateSold());
        contentValues.put("SALE_PRICE", animal.getSalePrice());
        contentValues.put("SALE_WEIGHT", animal.getSaleWeight());
        contentValues.put("SALE_WEIGHT_UNIT", animal.getSaleWeightUnit());
        contentValues.put("SALE_HEIGHT", animal.getSaleHeight());
        contentValues.put("SALE_HEIGHT_UNIT", animal.getSaleHeightUnit());
        contentValues.put("RECORD_TYPE", animal.getRecordType());
        contentValues.put("NFC_SCAN_ENTRY_TIMESTAMP", animal.getNfcScanEntryTimestamp());
        contentValues.put("NFC_SCAN_SAVE_TIMESTAMP", animal.getNfcScanSaveTimestamp());

        Calendar cal = Calendar.getInstance();;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        contentValues.put("CREATE_USER", username);
        contentValues.put("CREATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

        database.insert("ANIMAL_TABLE", null, contentValues);
        return true;
    }

    /*
    public boolean insertEvents(ArrayList<Event> arrayList) {
        //ArrayList<Event> arrayList = saveBundle.getParcelableArrayList("EVENTS_ARRAY");

        if (arrayList.size() == 0)
            return true;
        else {
            SQLiteDatabase eventTableDB = this.getWritableDatabase();
            ContentValues eventsContentValues = new ContentValues();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            Event j;
            for (int i = 0; i < arrayList.size(); i++) {
                j = arrayList.get(i);
                if ("N".equals(j.getRecordType())) {
                    eventsContentValues.put("ACTIVE_FLAG", "A");
                    eventsContentValues.put("ANIMAL_ID", j.getAnimalId());
                    eventsContentValues.put("EVENT_ID", j.getEventId());
                    eventsContentValues.put("EVENT_TYPE", j.getEventType());
                    eventsContentValues.put("EVENT_TIMESTAMP", j.getEventDate());
                    eventsContentValues.put("EVENT_REMARKS", j.getEventRemarks());
                    eventsContentValues.put("RECORD_TYPE", "D");

                    eventsContentValues.put("CREATE_USER", username);
                    eventsContentValues.put("CREATE_TIMESTAMP", dateFormatter.format(cal.getTime()));

                    eventTableDB.insert("EVENT_TABLE", null, eventsContentValues);
                }
            }
            return true;

        }
    }
    */

    public boolean insertEvent(Event event) {

        SQLiteDatabase eventTableDB = this.getWritableDatabase();
        ContentValues eventsContentValues = new ContentValues();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        eventsContentValues.put("ACTIVE_FLAG", "A");
        eventsContentValues.put("ANIMAL_ID", event.getAnimalId());
        eventsContentValues.put("EVENT_ID", event.getEventId());
        eventsContentValues.put("EVENT_TYPE", event.getEventType());
        eventsContentValues.put("EVENT_DATE", event.getEventDate());
        eventsContentValues.put("EVENT_TIMESTAMP", event.getEventTime());
        eventsContentValues.put("EVENT_REMARKS", event.getEventRemarks());
        eventsContentValues.put("RECORD_TYPE", event.getRecordType());
        eventsContentValues.put("NFC_SCAN_ENTRY_TIMESTAMP", event.getNfcScanEntryTimestamp());
        eventsContentValues.put("NFC_SCAN_SAVE_TIMESTAMP", event.getNfcScanSaveTimestamp());

        eventsContentValues.put("CREATE_USER", username);
        eventsContentValues.put("CREATE_TIMESTAMP", dateFormatter.format(cal.getTime()));

        eventTableDB.insert("EVENT_TABLE", null, eventsContentValues);

        return true;
    }

    public boolean insertCaretaker (Caretaker caretaker){
        SQLiteDatabase database = this.getWritableDatabase();

        //add record
        ContentValues contentValues = new ContentValues();
        contentValues.put("ACTIVE_FLAG", "A");
        contentValues.put("ANIMAL_ID", caretaker.getAnimalId());
        contentValues.put("CARETAKER_ID", caretaker.getCaretakerId());
        contentValues.put("CARETAKER_UID", caretaker.getCaretakerUid());
        contentValues.put("CARETAKER_NAME", caretaker.getCaretakerName());
        contentValues.put("CARETAKER_TEL", caretaker.getCaretakerTel());
        contentValues.put("CARETAKER_ADDR_1", caretaker.getCaretakerAddr1());
        contentValues.put("CARETAKER_ADDR_2", caretaker.getCaretakerAddr2());
        contentValues.put("CARETAKER_ADDR_3", caretaker.getCaretakerAddr3());
        contentValues.put("RECORD_TYPE", caretaker.getRecordType());
        contentValues.put("NFC_SCAN_ENTRY_TIMESTAMP", caretaker.getNfcScanEntryTimestamp());
        contentValues.put("NFC_SCAN_SAVE_TIMESTAMP", caretaker.getNfcScanSaveTimestamp());


        Calendar cal = Calendar.getInstance();;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        contentValues.put("CREATE_USER", username);
        contentValues.put("CREATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

        database.insert("CARETAKER_TABLE", null, contentValues);
        return true;
    }

    public void insertAnimalFromServer (HashMap<String, String> queryvalues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ACTIVE_FLAG","A");
        contentValues.put("ANIMAL_ID", queryvalues.get("ANIMAL_ID"));
        contentValues.put("SUPERVISOR", queryvalues.get("SUPERVISOR"));
        contentValues.put("CARETAKER_UID", queryvalues.get("CARETAKER_UID"));
        contentValues.put("ANIMAL_TYPE", queryvalues.get("ANIMAL_TYPE"));
        contentValues.put("RECORD_TYPE", queryvalues.get("RECORD_TYPE"));
        contentValues.put("GENDER", queryvalues.get("GENDER"));
        contentValues.put("DATE_OF_BIRTH", queryvalues.get("DATE_OF_BIRTH"));
        contentValues.put("COUNTRY", queryvalues.get("COUNTRY"));
        contentValues.put("DATE_PURCHASED", queryvalues.get("DATE_PURCHASED"));
        contentValues.put("PURCHASE_PRICE", queryvalues.get("PURCHASE_PRICE"));
        contentValues.put("PURCHASE_WEIGHT", queryvalues.get("PURCHASE_WEIGHT"));
        contentValues.put("PURCHASE_WEIGHT_UNIT", queryvalues.get("PURCHASE_WEIGHT_UNIT"));
        contentValues.put("PURCHASE_HEIGHT", queryvalues.get("PURCHASE_HEIGHT"));
        contentValues.put("PURCHASE_HEIGHT_UNIT", queryvalues.get("PURCHASE_HEIGHT_UNIT"));
        contentValues.put("DATE_DISTRIBUTED", queryvalues.get("DATE_DISTRIBUTED"));
        contentValues.put("DATE_SOLD", queryvalues.get("DATE_SOLD"));
        contentValues.put("SALE_PRICE", queryvalues.get("SALE_PRICE"));
        contentValues.put("SALE_WEIGHT", queryvalues.get("SALE_WEIGHT"));
        contentValues.put("SALE_WEIGHT_UNIT", queryvalues.get("SALE_WEIGHT_UNIT"));
        contentValues.put("SALE_HEIGHT", queryvalues.get("SALE_HEIGHT"));
        contentValues.put("SALE_HEIGHT_UNIT", queryvalues.get("SALE_HEIGHT_UNIT"));
        contentValues.put("NFC_SCAN_ENTRY_TIMESTAMP", queryvalues.get("NFC_SCAN_ENTRY_TIMESTAMP"));
        contentValues.put("NFC_SCAN_SAVE_TIMESTAMP", queryvalues.get("NFC_SCAN_SAVE_TIMESTAMP"));
        contentValues.put("CREATE_USER", queryvalues.get("CREATE_USER"));
        contentValues.put("CREATE_TIMESTAMP", queryvalues.get("CREATE_TIMESTAMP"));

        database.insert("SERVER_ANIMAL_TABLE", null, contentValues);

    }

    public void insertEventFromServer (HashMap<String, String> queryvalues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ACTIVE_FLAG","A");
        contentValues.put("ANIMAL_ID", queryvalues.get("ANIMAL_ID"));
        contentValues.put("EVENT_ID", queryvalues.get("EVENT_ID"));
        contentValues.put("EVENT_TYPE", queryvalues.get("EVENT_TYPE"));
        contentValues.put("EVENT_DATE", queryvalues.get("EVENT_DATE"));
        contentValues.put("EVENT_TIMESTAMP", queryvalues.get("EVENT_TIME"));
        contentValues.put("EVENT_REMARKS", queryvalues.get("EVENT_REMARKS"));
        contentValues.put("RECORD_TYPE", queryvalues.get("RECORD_TYPE"));
        contentValues.put("NFC_SCAN_ENTRY_TIMESTAMP", queryvalues.get("NFC_SCAN_ENTRY_TIMESTAMP"));
        contentValues.put("NFC_SCAN_SAVE_TIMESTAMP", queryvalues.get("NFC_SCAN_SAVE_TIMESTAMP"));
        contentValues.put("CREATE_USER", queryvalues.get("CREATE_USER"));
        contentValues.put("CREATE_TIMESTAMP", queryvalues.get("CREATE_TIMESTAMP"));
        database.insert("SERVER_EVENT_TABLE", null, contentValues);

    }

    public void insertCaretakerFromServer (HashMap<String, String> queryvalues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ACTIVE_FLAG","A");
        contentValues.put("CARETAKER_ID", queryvalues.get("CARETAKER_ID"));
        contentValues.put("CARETAKER_UID", queryvalues.get("CARETAKER_UID"));
        contentValues.put("ANIMAL_ID", queryvalues.get("ANIMAL_ID"));
        contentValues.put("CARETAKER_NAME", queryvalues.get("CARETAKER_NAME"));
        contentValues.put("CARETAKER_TEL", queryvalues.get("CARETAKER_TEL"));
        contentValues.put("CARETAKER_ADDR_1", queryvalues.get("CARETAKER_ADDR_1"));
        contentValues.put("CARETAKER_ADDR_2", queryvalues.get("CARETAKER_ADDR_2"));
        contentValues.put("CARETAKER_ADDR_3", queryvalues.get("CARETAKER_ADDR_3"));
        contentValues.put("RECORD_TYPE", queryvalues.get("RECORD_TYPE"));
        contentValues.put("NFC_SCAN_ENTRY_TIMESTAMP", queryvalues.get("NFC_SCAN_ENTRY_TIMESTAMP"));
        contentValues.put("NFC_SCAN_SAVE_TIMESTAMP", queryvalues.get("NFC_SCAN_SAVE_TIMESTAMP"));
        contentValues.put("CREATE_USER", queryvalues.get("CREATE_USER"));
        contentValues.put("CREATE_TIMESTAMP", queryvalues.get("CREATE_TIMESTAMP"));

        database.insert("SERVER_CARETAKER_TABLE", null, contentValues);

    }

    public boolean insertSystemProperty( String key, String value){
        SQLiteDatabase SystemPropertiesTableDB = this.getWritableDatabase();
        ContentValues systemPropertyContentValues = new ContentValues();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        systemPropertyContentValues.put("ACTIVE_FLAG", "A");
        systemPropertyContentValues.put("KEY", key);
        systemPropertyContentValues.put("VALUE", value);
        systemPropertyContentValues.put("CREATE_USER", "SYSTEM");
        systemPropertyContentValues.put("CREATE_TIMESTAMP", dateFormatter.format(cal.getTime()));

        SystemPropertiesTableDB.insert("SYSTEM_PROPERTIES_TABLE", null, systemPropertyContentValues);

        return true;
    }

    public boolean insertSystemProperty(SQLiteDatabase database, String role, String key) {

        //SQLiteDatabase SystemPropertiesTableDB = this.getWritableDatabase();
        ContentValues systemPropertyContentValues = new ContentValues();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        systemPropertyContentValues.put("ACTIVE_FLAG", "A");
        systemPropertyContentValues.put("KEY", role);
        systemPropertyContentValues.put("VALUE", key);
        systemPropertyContentValues.put("CREATE_USER", "SYSTEM");
        systemPropertyContentValues.put("CREATE_TIMESTAMP", dateFormatter.format(cal.getTime()));

        database.insert("SYSTEM_PROPERTIES_TABLE", null, systemPropertyContentValues);

        return true;
    }

    public boolean insertGender(SQLiteDatabase database, String genderCode, String gender, String locale) {

        ContentValues genderContentValues = new ContentValues();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        genderContentValues.put("ACTIVE_FLAG", "A");
        genderContentValues.put("GENDER_CODE", genderCode);
        genderContentValues.put("GENDER", gender);
        genderContentValues.put("LOCALE", locale);
        genderContentValues.put("CREATE_USER", "SYSTEM");
        genderContentValues.put("CREATE_TIMESTAMP", dateFormatter.format(cal.getTime()));

        database.insert("GENDER_TABLE", null, genderContentValues);

        return true;
    }

    public boolean insertCountry(SQLiteDatabase database, String countryCode, String country, String locale) {

        ContentValues countryContentValues = new ContentValues();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        countryContentValues.put("ACTIVE_FLAG", "A");
        countryContentValues.put("COUNTRY_CODE", countryCode);
        countryContentValues.put("COUNTRY", country);
        countryContentValues.put("LOCALE", locale);
        countryContentValues.put("CREATE_USER", "SYSTEM");
        countryContentValues.put("CREATE_TIMESTAMP", dateFormatter.format(cal.getTime()));

        database.insert("COUNTRY_TABLE", null, countryContentValues);

        return true;
    }

    public boolean insertAnimalType(SQLiteDatabase database, String animalTypeCode, String animalType, String locale) {

        ContentValues animalTypeContentValues = new ContentValues();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        animalTypeContentValues.put("ACTIVE_FLAG", "A");
        animalTypeContentValues.put("ANIMAL_TYPE_CODE", animalTypeCode);
        animalTypeContentValues.put("ANIMAL_TYPE", animalType);
        animalTypeContentValues.put("LOCALE", locale);
        animalTypeContentValues.put("CREATE_USER", "SYSTEM");
        animalTypeContentValues.put("CREATE_TIMESTAMP", dateFormatter.format(cal.getTime()));

        database.insert("ANIMAL_TYPE_TABLE", null, animalTypeContentValues);

        return true;
    }


    public boolean insertSystemPropertyFromServer(HashMap<String, String> queryvalues) {

        SQLiteDatabase SystemPropertiesTableDB = this.getWritableDatabase();
        ContentValues systemPropertyContentValues = new ContentValues();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        systemPropertyContentValues.put("ACTIVE_FLAG", "A");
        systemPropertyContentValues.put("KEY", queryvalues.get("ROLE"));
        systemPropertyContentValues.put("VALUE", queryvalues.get("KEY"));
        systemPropertyContentValues.put("CREATE_USER", username);
        systemPropertyContentValues.put("CREATE_TIMESTAMP", dateFormatter.format(cal.getTime()));

        SystemPropertiesTableDB.insert("SYSTEM_PROPERTIES_TABLE", null, systemPropertyContentValues);

        return true;
    }

    public Animal getAnimalByIdRaw(String id){

        Animal newAnimal = null;

        String ANIMAL_SELECT_QUERY =
                String.format("SELECT * FROM ANIMAL_TABLE WHERE ANIMAL_ID = ? AND " +
                        "ACTIVE_FLAG = 'A'" +
                        "UNION " +
                        "SELECT * FROM SERVER_ANIMAL_TABLE WHERE ANIMAL_ID = ? AND " +
                        "ACTIVE_FLAG = 'A'");


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ANIMAL_SELECT_QUERY, new String[]{id, id});
        String price;
        String date;
        try {
            if (cursor.moveToFirst()) {
                do {
                    newAnimal = new Animal();
                    newAnimal.setAnimalId(cursor.getString(cursor.getColumnIndexOrThrow("ANIMAL_ID")));
                    newAnimal.setSupervisor(cursor.getString(cursor.getColumnIndexOrThrow("SUPERVISOR")));
                    newAnimal.setCaretakerUid(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_UID")));
                    newAnimal.setAnimalType(cursor.getString(cursor.getColumnIndexOrThrow("ANIMAL_TYPE")));
                    newAnimal.setGender(cursor.getString(cursor.getColumnIndexOrThrow("GENDER")));
                    newAnimal.setCountry(cursor.getString(cursor.getColumnIndexOrThrow("COUNTRY")));
                    newAnimal.setRecordType(cursor.getString(cursor.getColumnIndexOrThrow("RECORD_TYPE")));
                    newAnimal.setLastUpdateUser(cursor.getString(cursor.getColumnIndexOrThrow("LAST_UPDATE_USER")));
                    newAnimal.setLastUpdateTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("LAST_UPDATE_TIMESTAMP")));
                    newAnimal.setCreateUser(cursor.getString(cursor.getColumnIndexOrThrow("CREATE_USER")));
                    newAnimal.setCreateTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("CREATE_TIMESTAMP")));


                    //set float
                    price = cursor.getString(cursor.getColumnIndexOrThrow("PURCHASE_PRICE"));
                    if (price!=null) newAnimal.setPurchasePrice(price);
                    else newAnimal.setPurchasePrice("0");

                    price = cursor.getString(cursor.getColumnIndexOrThrow("SALE_PRICE"));
                    if (price!=null) newAnimal.setSalePrice(price);
                    else newAnimal.setSalePrice("0");

                    //set dates
                    date = cursor.getString(cursor.getColumnIndexOrThrow("DATE_OF_BIRTH"));
                    if (date!= null) newAnimal.setDateOfBirth(date);
                    else newAnimal.setDateOfBirth(null);

                    date = cursor.getString(cursor.getColumnIndexOrThrow("DATE_PURCHASED"));
                    if (date!= null) newAnimal.setDatePurchased(date);
                    else newAnimal.setDatePurchased(null);

                    date = cursor.getString(cursor.getColumnIndexOrThrow("DATE_DISTRIBUTED"));
                    if (date!= null) newAnimal.setDateDistributed(date);
                    else newAnimal.setDateDistributed(null);

                    date = cursor.getString(cursor.getColumnIndexOrThrow("DATE_SOLD"));
                    if (date!= null) newAnimal.setDateSold(date);
                    else newAnimal.setDateSold(null);

                    //newAnimal.setEvents(eventArrayList);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in getALL");
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return newAnimal;
    }

    public Animal getAnimalById(String id) {

        Animal newAnimal = null;
        /*
        System.out.println("COUNTRY COUNTRY COUNTRY COUNTRY COUNTRY COUNTRY COUNTRY "+country);
        String countryClause="";
        if (!("All".equals(country))){
            countryClause = "COUNTRY = '" +country+"' AND ";
        }
        */

        Cursor cursor;
        Cursor animalCursor;
        Cursor animalServerCursor = null;

        String ANIMAL_TABLE_SELECT_QUERY =
                String.format("SELECT * FROM ANIMAL_TABLE WHERE ANIMAL_ID = ? AND " +
                        //countryClause +
                        "ACTIVE_FLAG = 'A'");
        SQLiteDatabase db = getReadableDatabase();

         animalCursor = db.rawQuery(ANIMAL_TABLE_SELECT_QUERY, new String[]{id});

        if (animalCursor.getCount()==0) {

            String SERVER_ANIMAL_TABLE_SELECT_QUERY =
                    String.format(
                            "SELECT * FROM SERVER_ANIMAL_TABLE WHERE ANIMAL_ID = ? AND " +
                                    //countryClause +
                                    "ACTIVE_FLAG = 'A'");
            animalServerCursor = db.rawQuery(SERVER_ANIMAL_TABLE_SELECT_QUERY, new String[]{id});
            cursor = animalServerCursor;
        }
        else {
            cursor = animalCursor;
        }

        String price;
        String date;
        String weight;
        String height;

        try {
            if (cursor.moveToFirst()) {
                do {
                    newAnimal = new Animal();
                    newAnimal.setAnimalId(cursor.getString(cursor.getColumnIndexOrThrow("ANIMAL_ID")));
                    newAnimal.setSupervisor(cursor.getString(cursor.getColumnIndexOrThrow("SUPERVISOR")));
                    newAnimal.setCaretakerUid(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_UID")));
                    newAnimal.setAnimalType(cursor.getString(cursor.getColumnIndexOrThrow("ANIMAL_TYPE")));
                    newAnimal.setGender(cursor.getString(cursor.getColumnIndexOrThrow("GENDER")));
                    newAnimal.setCountry(cursor.getString(cursor.getColumnIndexOrThrow("COUNTRY")));
                    newAnimal.setRecordType(cursor.getString(cursor.getColumnIndexOrThrow("RECORD_TYPE")));
                    newAnimal.setLastUpdateUser(cursor.getString(cursor.getColumnIndexOrThrow("LAST_UPDATE_USER")));
                    newAnimal.setLastUpdateTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("LAST_UPDATE_TIMESTAMP")));
                    newAnimal.setCreateUser(cursor.getString(cursor.getColumnIndexOrThrow("CREATE_USER")));
                    newAnimal.setCreateTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("CREATE_TIMESTAMP")));


                    //set float
                    weight = cursor.getString(cursor.getColumnIndexOrThrow("PURCHASE_WEIGHT"));
                    if (weight!=null) newAnimal.setPurchaseWeight(weight);
                    else newAnimal.setPurchaseWeight(null);
                    newAnimal.setPurchaseWeightUnit(cursor.getString(cursor.getColumnIndexOrThrow("PURCHASE_WEIGHT_UNIT")));

                    height = cursor.getString(cursor.getColumnIndexOrThrow("PURCHASE_HEIGHT"));
                    if (height!=null) newAnimal.setPurchaseHeight(height);
                    else newAnimal.setPurchaseHeight(null);
                    newAnimal.setPurchaseHeightUnit(cursor.getString(cursor.getColumnIndexOrThrow("PURCHASE_HEIGHT_UNIT")));

                    weight = cursor.getString(cursor.getColumnIndexOrThrow("SALE_WEIGHT"));
                    if (weight!=null) newAnimal.setSaleWeight(weight);
                    else newAnimal.setSaleWeight(null);
                    newAnimal.setSaleWeightUnit(cursor.getString(cursor.getColumnIndexOrThrow("SALE_WEIGHT_UNIT")));

                    height = cursor.getString(cursor.getColumnIndexOrThrow("SALE_HEIGHT"));
                    if (height!=null) newAnimal.setSaleHeight(height);
                    else newAnimal.setSaleHeight(null);
                    newAnimal.setSaleHeightUnit(cursor.getString(cursor.getColumnIndexOrThrow("SALE_HEIGHT_UNIT")));

                    price = cursor.getString(cursor.getColumnIndexOrThrow("PURCHASE_PRICE"));
                    if (price!=null) newAnimal.setPurchasePrice(price);
                    else newAnimal.setPurchasePrice(null);

                    price = cursor.getString(cursor.getColumnIndexOrThrow("SALE_PRICE"));
                    if (price!=null) newAnimal.setSalePrice(price);
                    else newAnimal.setSalePrice(null);

                    //set dates
                    date = cursor.getString(cursor.getColumnIndexOrThrow("DATE_OF_BIRTH"));
                    if (date!= null) newAnimal.setDateOfBirth(date);
                    else newAnimal.setDateOfBirth(null);

                    date = cursor.getString(cursor.getColumnIndexOrThrow("DATE_PURCHASED"));
                    if (date!= null) newAnimal.setDatePurchased(date);
                    else newAnimal.setDatePurchased(null);

                    date = cursor.getString(cursor.getColumnIndexOrThrow("DATE_DISTRIBUTED"));
                    if (date!= null) newAnimal.setDateDistributed(date);
                    else newAnimal.setDateDistributed(null);

                    date = cursor.getString(cursor.getColumnIndexOrThrow("DATE_SOLD"));
                    if (date!= null) newAnimal.setDateSold(date);
                    else newAnimal.setDateSold(null);

                    //newAnimal.setEvents(eventArrayList);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in getALL");
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (animalCursor != null && !animalCursor.isClosed()) {
                cursor.close();
            }
            if (animalServerCursor != null && !animalServerCursor.isClosed()) {
                cursor.close();
            }
        }

        return newAnimal;
    }

    public Animal getOriginalServerAnimalById(String id) {

        Animal newAnimal = null;

        String ANIMAL_SELECT_QUERY =
                String.format(
                        "SELECT * FROM SERVER_ANIMAL_TABLE WHERE ANIMAL_ID = ? "
                                //+ countryClause
                );


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ANIMAL_SELECT_QUERY, new String[]{id});
        String price;
        String date;
        String weight;
        String height;
        try {
            if (cursor.moveToFirst()) {
                do {
                    newAnimal = new Animal();
                    newAnimal.setAnimalId(cursor.getString(cursor.getColumnIndexOrThrow("ANIMAL_ID")));
                    newAnimal.setSupervisor(cursor.getString(cursor.getColumnIndexOrThrow("SUPERVISOR")));
                    newAnimal.setCaretakerUid(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_UID")));
                    newAnimal.setAnimalType(cursor.getString(cursor.getColumnIndexOrThrow("ANIMAL_TYPE")));
                    newAnimal.setGender(cursor.getString(cursor.getColumnIndexOrThrow("GENDER")));
                    newAnimal.setCountry(cursor.getString(cursor.getColumnIndexOrThrow("COUNTRY")));
                    newAnimal.setRecordType(cursor.getString(cursor.getColumnIndexOrThrow("RECORD_TYPE")));
                    newAnimal.setLastUpdateUser(cursor.getString(cursor.getColumnIndexOrThrow("LAST_UPDATE_USER")));
                    newAnimal.setLastUpdateTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("LAST_UPDATE_TIMESTAMP")));
                    newAnimal.setCreateUser(cursor.getString(cursor.getColumnIndexOrThrow("CREATE_USER")));
                    newAnimal.setCreateTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("CREATE_TIMESTAMP")));


                    //set float
                    weight = cursor.getString(cursor.getColumnIndexOrThrow("PURCHASE_WEIGHT"));
                    if (weight!=null) newAnimal.setPurchaseWeight(weight);
                    else newAnimal.setPurchaseWeight(null);
                    newAnimal.setPurchaseWeightUnit(cursor.getString(cursor.getColumnIndexOrThrow("PURCHASE_WEIGHT_UNIT")));

                    height = cursor.getString(cursor.getColumnIndexOrThrow("PURCHASE_HEIGHT"));
                    if (height!=null) newAnimal.setPurchaseHeight(height);
                    else newAnimal.setPurchaseHeight(null);
                    newAnimal.setPurchaseHeightUnit(cursor.getString(cursor.getColumnIndexOrThrow("PURCHASE_HEIGHT_UNIT")));

                    weight = cursor.getString(cursor.getColumnIndexOrThrow("SALE_WEIGHT"));
                    if (weight!=null) newAnimal.setSaleWeight(weight);
                    else newAnimal.setSaleWeight(null);
                    newAnimal.setSaleWeightUnit(cursor.getString(cursor.getColumnIndexOrThrow("SALE_WEIGHT_UNIT")));

                    height = cursor.getString(cursor.getColumnIndexOrThrow("SALE_HEIGHT"));
                    if (height!=null) newAnimal.setSaleHeight(height);
                    else newAnimal.setSaleHeight(null);
                    newAnimal.setSaleHeightUnit(cursor.getString(cursor.getColumnIndexOrThrow("SALE_HEIGHT_UNIT")));

                    price = cursor.getString(cursor.getColumnIndexOrThrow("PURCHASE_PRICE"));
                    if (price!=null) newAnimal.setPurchasePrice(price);
                    else newAnimal.setPurchasePrice("0");

                    price = cursor.getString(cursor.getColumnIndexOrThrow("SALE_PRICE"));
                    if (price!=null) newAnimal.setSalePrice(price);
                    else newAnimal.setSalePrice("0");

                    //set dates
                    date = cursor.getString(cursor.getColumnIndexOrThrow("DATE_OF_BIRTH"));
                    if (date!= null) newAnimal.setDateOfBirth(date);
                    else newAnimal.setDateOfBirth(null);

                    date = cursor.getString(cursor.getColumnIndexOrThrow("DATE_PURCHASED"));
                    if (date!= null) newAnimal.setDatePurchased(date);
                    else newAnimal.setDatePurchased(null);

                    date = cursor.getString(cursor.getColumnIndexOrThrow("DATE_DISTRIBUTED"));
                    if (date!= null) newAnimal.setDateDistributed(date);
                    else newAnimal.setDateDistributed(null);

                    date = cursor.getString(cursor.getColumnIndexOrThrow("DATE_SOLD"));
                    if (date!= null) newAnimal.setDateSold(date);
                    else newAnimal.setDateSold(null);

                    //newAnimal.setEvents(eventArrayList);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in getALL");
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return newAnimal;
    }

    public Caretaker getOriginalCaretakerByUidFromServer(String uid) {

        if (uid == null) return null;

        Caretaker caretaker = null;
        String CARETAKER_SELECT_QUERY =
                String.format("SELECT * FROM SERVER_CARETAKER_TABLE WHERE CARETAKER_UID = ?");


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(CARETAKER_SELECT_QUERY, new String[]{uid});
        try {
            if (cursor.moveToFirst()) {
                do {
                    caretaker = new Caretaker();
                    caretaker.setCaretakerId(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_ID")));
                    caretaker.setCaretakerUid(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_UID")));
                    caretaker.setAnimalId(cursor.getString(cursor.getColumnIndexOrThrow("ANIMAL_ID")));
                    caretaker.setCaretakerTel(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_TEL")));
                    caretaker.setCaretakerName(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_NAME")));
                    caretaker.setCaretakerAddr1(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_ADDR_1")));
                    caretaker.setCaretakerAddr2(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_ADDR_2")));
                    caretaker.setCaretakerAddr3(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_ADDR_3")));
                    caretaker.setRecordType(cursor.getString(cursor.getColumnIndexOrThrow("RECORD_TYPE")));
                    caretaker.setLastUpdateUser(cursor.getString(cursor.getColumnIndexOrThrow("LAST_UPDATE_USER")));
                    caretaker.setLastUpdateTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("LAST_UPDATE_TIMESTAMP")));
                    caretaker.setCreateUser(cursor.getString(cursor.getColumnIndexOrThrow("CREATE_USER")));
                    caretaker.setCreateTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("CREATE_TIMESTAMP")));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in getCaretakerByUid");
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return caretaker;
    }

    public Caretaker getCaretakerByUidFromDevice(String uid) {

        if (uid == null) return null;

        Caretaker caretaker = null;
        String CARETAKER_SELECT_QUERY =
                String.format("SELECT * FROM CARETAKER_TABLE WHERE CARETAKER_UID = ? AND ACTIVE_FLAG = 'A'");


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(CARETAKER_SELECT_QUERY, new String[]{uid});
        try {
            if (cursor.moveToFirst()) {
                do {
                    caretaker = new Caretaker();
                    caretaker.setCaretakerId(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_ID")));
                    caretaker.setCaretakerUid(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_UID")));
                    caretaker.setAnimalId(cursor.getString(cursor.getColumnIndexOrThrow("ANIMAL_ID")));
                    caretaker.setCaretakerTel(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_TEL")));
                    caretaker.setCaretakerName(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_NAME")));
                    caretaker.setCaretakerAddr1(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_ADDR_1")));
                    caretaker.setCaretakerAddr2(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_ADDR_2")));
                    caretaker.setCaretakerAddr3(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_ADDR_3")));
                    caretaker.setRecordType(cursor.getString(cursor.getColumnIndexOrThrow("RECORD_TYPE")));
                    caretaker.setLastUpdateUser(cursor.getString(cursor.getColumnIndexOrThrow("LAST_UPDATE_USER")));
                    caretaker.setLastUpdateTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("LAST_UPDATE_TIMESTAMP")));
                    caretaker.setCreateUser(cursor.getString(cursor.getColumnIndexOrThrow("CREATE_USER")));
                    caretaker.setCreateTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("CREATE_TIMESTAMP")));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in getCaretakerByUid");
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return caretaker;
    }

    public Caretaker getCaretakerByUidFromServer(String uid) {

        if (uid == null) return null;

        Caretaker caretaker = null;
        String CARETAKER_SELECT_QUERY =
                String.format("SELECT * FROM SERVER_CARETAKER_TABLE WHERE CARETAKER_UID = ? AND ACTIVE_FLAG = 'A'");


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(CARETAKER_SELECT_QUERY, new String[]{uid});
        try {
            if (cursor.moveToFirst()) {
                do {
                    caretaker = new Caretaker();
                    caretaker.setCaretakerId(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_ID")));
                    caretaker.setCaretakerUid(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_UID")));
                    caretaker.setAnimalId(cursor.getString(cursor.getColumnIndexOrThrow("ANIMAL_ID")));
                    caretaker.setCaretakerTel(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_TEL")));
                    caretaker.setCaretakerName(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_NAME")));
                    caretaker.setCaretakerAddr1(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_ADDR_1")));
                    caretaker.setCaretakerAddr2(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_ADDR_2")));
                    caretaker.setCaretakerAddr3(cursor.getString(cursor.getColumnIndexOrThrow("CARETAKER_ADDR_3")));
                    caretaker.setRecordType(cursor.getString(cursor.getColumnIndexOrThrow("RECORD_TYPE")));
                    caretaker.setLastUpdateUser(cursor.getString(cursor.getColumnIndexOrThrow("LAST_UPDATE_USER")));
                    caretaker.setLastUpdateTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("LAST_UPDATE_TIMESTAMP")));
                    caretaker.setCreateUser(cursor.getString(cursor.getColumnIndexOrThrow("CREATE_USER")));
                    caretaker.setCreateTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("CREATE_TIMESTAMP")));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in getCaretakerByUid");
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return caretaker;
    }

    public Caretaker getCaretakerByUid(String uid) {
        if (getCaretakerByUidFromDevice(uid) != null)
            return getCaretakerByUidFromDevice(uid);
        else return getCaretakerByUidFromServer(uid);
    }

    public ArrayList<Event> getEventsById(String id) {

        SQLiteDatabase db = getReadableDatabase();
        String EVENT_SELECT_QUERY =
                String.format("SELECT * FROM EVENT_TABLE WHERE ANIMAL_ID = ? AND ACTIVE_FLAG = 'A'" +
                        "UNION " +
                        "SELECT * FROM SERVER_EVENT_TABLE WHERE ANIMAL_ID = ? AND ACTIVE_FLAG = 'A'");

        ArrayList<Event> eventArrayList = new ArrayList<Event>();
        Cursor eventCursor = db.rawQuery(EVENT_SELECT_QUERY, new String[]{id, id});

        if (eventCursor.moveToFirst()) {
            do {
                Event newEvent = new Event();
                newEvent.setAnimalId(id);
                newEvent.setEventId(eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_ID")));
                newEvent.setEventType(eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_TYPE")));
                newEvent.setEventDate(eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_DATE")));
                newEvent.setEventTime(eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_TIMESTAMP")));
                newEvent.setEventRemarks(eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_REMARKS")));
                newEvent.setRecordType(eventCursor.getString(eventCursor.getColumnIndexOrThrow("RECORD_TYPE")));
                newEvent.setLastUpdateUser(eventCursor.getString(eventCursor.getColumnIndexOrThrow("LAST_UPDATE_USER")));
                newEvent.setLastUpdateTimestamp(eventCursor.getString(eventCursor.getColumnIndexOrThrow("LAST_UPDATE_TIMESTAMP")));
                newEvent.setCreateUser(eventCursor.getString(eventCursor.getColumnIndexOrThrow("CREATE_USER")));
                newEvent.setCreateTimestamp(eventCursor.getString(eventCursor.getColumnIndexOrThrow("CREATE_TIMESTAMP")));
                eventArrayList.add(newEvent);
            } while (eventCursor.moveToNext());
        }
        return eventArrayList;
    }

    public ArrayList<SystemProperty> getSystemPropertyByKey(String role) {

        SQLiteDatabase db = getReadableDatabase();
        String SYSTEM_PROPERTY_SELECT_QUERY =
                String.format("SELECT * FROM SYSTEM_PROPERTIES_TABLE WHERE KEY = ? AND ACTIVE_FLAG = 'A'");

        ArrayList<SystemProperty> systemPropertyList = new ArrayList<SystemProperty>();
        Cursor systemPropertyCursor = db.rawQuery(SYSTEM_PROPERTY_SELECT_QUERY, new String[]{role});

        if (systemPropertyCursor.moveToFirst()) {
            do {
                SystemProperty newProperty = new SystemProperty();
                newProperty.setKey(role);
                newProperty.setValue(systemPropertyCursor.getString(systemPropertyCursor.getColumnIndexOrThrow("VALUE")));
                newProperty.setLastUpdateUser(systemPropertyCursor.getString(systemPropertyCursor.getColumnIndexOrThrow("LAST_UPDATE_USER")));
                newProperty.setLastUpdateTimestamp(systemPropertyCursor.getString(systemPropertyCursor.getColumnIndexOrThrow("LAST_UPDATE_TIMESTAMP")));
                newProperty.setCreateUser(systemPropertyCursor.getString(systemPropertyCursor.getColumnIndexOrThrow("CREATE_USER")));
                newProperty.setCreateTimestamp(systemPropertyCursor.getString(systemPropertyCursor.getColumnIndexOrThrow("CREATE_TIMESTAMP")));
                systemPropertyList.add(newProperty);
            } while (systemPropertyCursor.moveToNext());
        }
        return systemPropertyList;
    }

    public int getCountUnsyncRecords(){

        String count="0";
        SQLiteDatabase db = getReadableDatabase();
        String COUNT_UNSYNC_RECORDS = "SELECT (SELECT count(*) FROM ANIMAL_TABLE WHERE ACTIVE_FLAG = 'A') + " +
                "(SELECT count(*) FROM EVENT_TABLE WHERE ACTIVE_FLAG = 'A') + " +
                "(SELECT count(*) FROM CARETAKER_TABLE WHERE ACTIVE_FLAG = 'A') " +
                "AS COUNT";

        Cursor countCursor = db.rawQuery(COUNT_UNSYNC_RECORDS, new String[]{});

        if (countCursor.moveToFirst()) {
            do {
                count = countCursor.getString(countCursor.getColumnIndexOrThrow("COUNT"));
                break;
            } while (countCursor.moveToNext());
        }
        return Integer.valueOf(count);
    }

    public Cursor getSearchResult(String searchString){

        SQLiteDatabase db = getReadableDatabase();
        String locale = mPrefs.getString("locale","en");

        Log.d(TAG,"locale "+locale);
        /*
        String query =
                "SELECT  "+
                "'' as _id,  " +
                "A.ANIMAL_ID, A.ANIMAL_TYPE, A.SUPERVISOR, C.CARETAKER_NAME, A.DATE_DISTRIBUTED " +
                "FROM  " +
                "(SELECT * FROM ANIMAL_TABLE  " +
                "WHERE ACTIVE_FLAG = 'A'  " +
                "UNION  " +
                "SELECT * FROM SERVER_ANIMAL_TABLE  " +
                "WHERE ACTIVE_FLAG = 'A') AS A  " +
                "LEFT OUTER JOIN  " +
                "(SELECT * FROM CARETAKER_TABLE  " +
                "WHERE ACTIVE_FLAG = 'A'  " +
                "UNION  " +
                "SELECT * FROM SERVER_CARETAKER_TABLE  " +
                "WHERE ACTIVE_FLAG = 'A') AS C  "  +
                "ON A.ANIMAL_ID = C.ANIMAL_ID  " +
                "LEFT OUTER JOIN  " +
                "(SELECT * FROM EVENT_TABLE  " +
                "WHERE ACTIVE_FLAG = 'A'  " +
                "UNION  " +
                "SELECT * FROM SERVER_EVENT_TABLE  " +
                "WHERE ACTIVE_FLAG = 'A') AS E  " +
                "ON A.ANIMAL_ID = E.ANIMAL_ID " +
                "WHERE  " +
                "A.ANIMAL_ID COLLATE NOCASE like '%" +searchString+"%' OR    " +
                "A.SUPERVISOR COLLATE NOCASE like '%" +searchString+"%' OR    " +
                "A.ANIMAL_TYPE COLLATE NOCASE like '%" +searchString+"%' OR    " +
                "A.GENDER COLLATE NOCASE like '%" +searchString+"%' OR    " +
                "A.DATE_OF_BIRTH COLLATE NOCASE like '%" +searchString+"%' OR    " +
                "A.COUNTRY COLLATE NOCASE like '%" +searchString+"%'   OR  " +
                "A.DATE_PURCHASED COLLATE NOCASE like '%" +searchString+"%' OR    " +
                "A.PURCHASE_PRICE COLLATE NOCASE like '%" +searchString+"%' OR    " +
                "A.DATE_DISTRIBUTED COLLATE NOCASE like '%" +searchString+"%' OR    " +
                "A.DATE_SOLD COLLATE NOCASE like '%" +searchString+"%' OR    " +
                "A.SALE_PRICE COLLATE NOCASE like '%" +searchString+"%' OR " +
                "C.CARETAKER_ID COLLATE NOCASE LIKE '%" +searchString+"%' OR " +
                "C.CARETAKER_NAME COLLATE NOCASE LIKE '%" +searchString+"%' OR " +
                "C.CARETAKER_TEL COLLATE NOCASE LIKE '%" +searchString+"%' OR " +
                "C.CARETAKER_ADDR_1 COLLATE NOCASE LIKE '%" +searchString+"%' OR " +
                "C.CARETAKER_ADDR_2 COLLATE NOCASE LIKE '%" +searchString+"%' OR " +
                "C.CARETAKER_ADDR_3 COLLATE NOCASE LIKE '%" +searchString+"%' OR " +
                "E.EVENT_TYPE COLLATE NOCASE LIKE '%" +searchString+"%' OR " +
                "E.EVENT_TIMESTAMP COLLATE NOCASE LIKE '%" +searchString+"%' OR " +
                "E.EVENT_REMARKS COLLATE NOCASE LIKE '%" +searchString+"%' " +
                "GROUP BY  " +
                "A.ANIMAL_ID,  " +
                "A.ANIMAL_TYPE,  " +
                "A.SUPERVISOR,  " +
                "C.CARETAKER_NAME,  " +
                "A.DATE_DISTRIBUTED " +
                "ORDER BY A.ANIMAL_ID ";
                */
        String query =
                "SELECT    "+
                "'' as _id,   "+
                "ACE.ANIMAL_ID AS ANIMAL_ID, " +
                "AT.ANIMAL_TYPE AS ANIMAL_TYPE, " +
                "ACE.SUPERVISOR AS SUPERVISOR, " +
                "ACE.CARETAKER_NAME AS CARETAKER_NAME, " +
                "ACE.DATE_DISTRIBUTED AS DATE_DISTRIBUTED "+
                "FROM    "+
                "((SELECT * FROM ANIMAL_TABLE    "+
                "WHERE ACTIVE_FLAG = 'A'    "+
                "UNION    "+
                "SELECT * FROM SERVER_ANIMAL_TABLE    "+
                "WHERE ACTIVE_FLAG = 'A') AS A    "+
                "LEFT OUTER JOIN    "+
                "(SELECT * FROM CARETAKER_TABLE    "+
                "WHERE ACTIVE_FLAG = 'A'    "+
                "UNION    "+
                "SELECT * FROM SERVER_CARETAKER_TABLE    "+
                "WHERE ACTIVE_FLAG = 'A') AS C     "+
                "ON A.ANIMAL_ID = C.ANIMAL_ID    "+
                "LEFT OUTER JOIN    "+
                "(SELECT * FROM EVENT_TABLE    "+
                "WHERE ACTIVE_FLAG = 'A'    "+
                "UNION    "+
                "SELECT * FROM SERVER_EVENT_TABLE    "+
                "WHERE ACTIVE_FLAG = 'A') AS E  " +
                "ON A.ANIMAL_ID = E.ANIMAL_ID) " +
                "AS ACE,  "+
                "(SELECT * FROM GENDER_TABLE WHERE LOCALE = '"+locale+"') AS G,  "+
                "(SELECT * FROM ANIMAL_TYPE_TABLE WHERE LOCALE = '"+locale+"') AS AT,  "+
                "(SELECT * FROM COUNTRY_TABLE WHERE LOCALE = '"+locale+"') AS CT  "+
                "WHERE ACE.GENDER = G.GENDER_CODE  "+
                "AND ACE.ANIMAL_TYPE = AT.ANIMAL_TYPE_CODE  "+
                "AND ACE.COUNTRY = CT.COUNTRY_CODE "+
                "AND ( "+
                "G.GENDER COLLATE NOCASE LIKE '%"+searchString+"%' OR "+
                "AT.ANIMAL_TYPE COLLATE NOCASE LIKE '%"+searchString+"%' OR "+
                "CT.COUNTRY COLLATE NOCASE LIKE '%"+searchString+"%' OR  "+
                "ACE.ANIMAL_ID COLLATE NOCASE like '%"+searchString+"%' OR      "+
                "ACE.SUPERVISOR COLLATE NOCASE like '%"+searchString+"%' OR      "+
                "ACE.DATE_OF_BIRTH COLLATE NOCASE like '%"+searchString+"%' OR      "+
                "ACE.DATE_PURCHASED COLLATE NOCASE like '%"+searchString+"%' OR      "+
                "ACE.PURCHASE_PRICE COLLATE NOCASE like '%"+searchString+"%' OR      "+
                "ACE.PURCHASE_WEIGHT COLLATE NOCASE like '%"+searchString+"%' OR      "+
                "ACE.PURCHASE_HEIGHT COLLATE NOCASE like '%"+searchString+"%' OR      "+
                "ACE.DATE_DISTRIBUTED COLLATE NOCASE like '%"+searchString+"%' OR      "+
                "ACE.DATE_SOLD COLLATE NOCASE like '%"+searchString+"%' OR      "+
                "ACE.SALE_PRICE COLLATE NOCASE like '%"+searchString+"%' OR   "+
                "ACE.SALE_WEIGHT COLLATE NOCASE like '%"+searchString+"%' OR      "+
                "ACE.SALE_HEIGHT COLLATE NOCASE like '%"+searchString+"%' OR      "+
                "ACE.CARETAKER_ID COLLATE NOCASE LIKE '%"+searchString+"%' OR   "+
                "ACE.CARETAKER_NAME COLLATE NOCASE LIKE '%"+searchString+"%' OR   "+
                "ACE.CARETAKER_TEL COLLATE NOCASE LIKE '%"+searchString+"%' OR   "+
                "ACE.CARETAKER_ADDR_1 COLLATE NOCASE LIKE '%"+searchString+"%' OR   "+
                "ACE.CARETAKER_ADDR_2 COLLATE NOCASE LIKE '%"+searchString+"%' OR   "+
                "ACE.CARETAKER_ADDR_3 COLLATE NOCASE LIKE '%"+searchString+"%' OR   "+
                "ACE.EVENT_TYPE COLLATE NOCASE LIKE '%"+searchString+"%' OR   "+
                "ACE.EVENT_TIMESTAMP COLLATE NOCASE LIKE '%"+searchString+"%' OR   "+
                "ACE.EVENT_REMARKS COLLATE NOCASE LIKE '%"+searchString+"%' ) "+
                "GROUP BY    "+
                "ACE.ANIMAL_ID,    "+
                "AT.ANIMAL_TYPE,    "+
                "ACE.SUPERVISOR,    "+
                "ACE.CARETAKER_NAME,    "+
                "ACE.DATE_DISTRIBUTED   "+
                "ORDER BY ACE.ANIMAL_ID ";
        //Log.d(TAG, query);
        Cursor animalCursor = db.rawQuery(query, null);
        Log.d(TAG, "animalCursor.getCount() "+animalCursor.getCount());
        return animalCursor;
    }

    /*REDUNDANT
    public Cursor getAll(){

        SQLiteDatabase db = getReadableDatabase();

        Log.d(TAG, "Inside getAll");


        String query =
                "SELECT "+
                "'' as _id,  " +
                "AT.ANIMAL_ID AS ANIMAL_ID,  " +
                "AT.ANIMAL_TYPE AS ANIMAL_TYPE,  " +
                "AT.SUPERVISOR AS SUPERVISOR,  " +
                "CR.CARETAKER_NAME AS CARETAKER_NAME,  " +
                "AT.DATE_DISTRIBUTED AS DATE_DISTRIBUTED  " +
                "FROM  " +
                "(SELECT * FROM ANIMAL_TABLE A " +
                "WHERE  " +
                //countryClause +
                "A.ACTIVE_FLAG = 'A'  " +
                ") AS AT  " +
                "LEFT OUTER JOIN  " +
                "(SELECT * FROM CARETAKER_TABLE C  " +
                "WHERE  " +
                "C.ACTIVE_FLAG = 'A'  " +
                "UNION  " +
                "SELECT * FROM SERVER_CARETAKER_TABLE SC  " +
                "WHERE SC.ACTIVE_FLAG = 'A' ) CR  " +
                "ON AT.CARETAKER_UID = CR.CARETAKER_UID  " +
                "UNION  " +
                "SELECT  " +
                "'' as _id,  " +
                "SAT.ANIMAL_ID AS ANIMAL_ID,  " +
                "SAT.ANIMAL_TYPE AS ANIMAL_TYPE,  " +
                "SAT.SUPERVISOR AS SUPERVISOR,  " +
                "SC.CARETAKER_NAME AS CARETAKER_NAME,  " +
                "SAT.DATE_DISTRIBUTED AS DATE_DISTRIBUTED  " +
                "FROM  " +
                "( " +
                "SELECT * FROM SERVER_ANIMAL_TABLE  SA " +
                "WHERE " +
                //countryClause +
                "ACTIVE_FLAG = 'A' " +
                ") AS SAT " +
                "LEFT OUTER JOIN  " +
                "(SELECT * FROM CARETAKER_TABLE C  " +
                "WHERE  " +
                "C.ACTIVE_FLAG = 'A'  " +
                "UNION  " +
                "SELECT * FROM SERVER_CARETAKER_TABLE SC  " +
                "WHERE SC.ACTIVE_FLAG = 'A'  " +
                ") AS SC " +
                "ON  " +
                "SAT.CARETAKER_UID = SC.CARETAKER_UID  ";

                Cursor animalCursor = db.rawQuery(query, null);
        return animalCursor;
    }
    */

    public Map getAllAnimalMap(){

        SQLiteDatabase db = getReadableDatabase();

        Cursor animalCursor = db.rawQuery("SELECT * FROM ANIMAL_TABLE WHERE " +
                //countryClause +
                "ACTIVE_FLAG = 'A' "
                , null);

        JSONObject jsonAnimals = new JSONObject();
        JSONObject animalJson = null;
        String price;
        String date;
        int i = 1;
        try {
            if (animalCursor.moveToFirst()) {
                do {
                    animalJson = new JSONObject();

                    animalJson.put("animal_id",animalCursor.getString(animalCursor.getColumnIndexOrThrow("ANIMAL_ID")));
                    animalJson.put("supervisor",animalCursor.getString(animalCursor.getColumnIndexOrThrow("SUPERVISOR")));
                    animalJson.put("caretaker_uid",animalCursor.getString(animalCursor.getColumnIndexOrThrow("CARETAKER_UID")));
                    animalJson.put("animal_type",animalCursor.getString(animalCursor.getColumnIndexOrThrow("ANIMAL_TYPE")));
                    animalJson.put("gender",animalCursor.getString(animalCursor.getColumnIndexOrThrow("GENDER")));
                    animalJson.put("date_of_birth",animalCursor.getString(animalCursor.getColumnIndexOrThrow("DATE_OF_BIRTH")));
                    animalJson.put("country",animalCursor.getString(animalCursor.getColumnIndexOrThrow("COUNTRY")));
                    animalJson.put("date_purchased",animalCursor.getString(animalCursor.getColumnIndexOrThrow("DATE_PURCHASED")));
                    animalJson.put("purchase_price",animalCursor.getString(animalCursor.getColumnIndexOrThrow("PURCHASE_PRICE")));

                    animalJson.put("purchase_weight",animalCursor.getString(animalCursor.getColumnIndexOrThrow("PURCHASE_WEIGHT")));
                    animalJson.put("purchase_weight_unit",animalCursor.getString(animalCursor.getColumnIndexOrThrow("PURCHASE_WEIGHT_UNIT")));
                    animalJson.put("purchase_height",animalCursor.getString(animalCursor.getColumnIndexOrThrow("PURCHASE_HEIGHT")));
                    animalJson.put("purchase_height_unit",animalCursor.getString(animalCursor.getColumnIndexOrThrow("PURCHASE_HEIGHT_UNIT")));

                    animalJson.put("date_distributed",animalCursor.getString(animalCursor.getColumnIndexOrThrow("DATE_DISTRIBUTED")));
                    animalJson.put("date_sold",animalCursor.getString(animalCursor.getColumnIndexOrThrow("DATE_SOLD")));
                    animalJson.put("sale_price",animalCursor.getString(animalCursor.getColumnIndexOrThrow("SALE_PRICE")));

                    animalJson.put("sale_weight",animalCursor.getString(animalCursor.getColumnIndexOrThrow("SALE_WEIGHT")));
                    animalJson.put("sale_weight_unit",animalCursor.getString(animalCursor.getColumnIndexOrThrow("SALE_WEIGHT_UNIT")));
                    animalJson.put("sale_height",animalCursor.getString(animalCursor.getColumnIndexOrThrow("SALE_HEIGHT")));
                    animalJson.put("sale_height_unit",animalCursor.getString(animalCursor.getColumnIndexOrThrow("SALE_HEIGHT_UNIT")));


                    animalJson.put("create_user",animalCursor.getString(animalCursor.getColumnIndexOrThrow("CREATE_USER")));
                    animalJson.put("create_timestamp",animalCursor.getString(animalCursor.getColumnIndexOrThrow("CREATE_TIMESTAMP")));
                    animalJson.put("record_type",animalCursor.getString(animalCursor.getColumnIndexOrThrow("RECORD_TYPE")));
                    animalJson.put("nfc_scan_entry_timestamp",animalCursor.getString(animalCursor.getColumnIndexOrThrow("NFC_SCAN_ENTRY_TIMESTAMP")));
                    animalJson.put("nfc_scan_save_timestamp",animalCursor.getString(animalCursor.getColumnIndexOrThrow("NFC_SCAN_SAVE_TIMESTAMP")));
                    //animalJson.put("editable",animalCursor.getString(animalCursor.getColumnIndexOrThrow("ANIMAL_EDITABLE")));

                    /*
                    //set float
                    price = animalCursor.getString(animalCursor.getColumnIndexOrThrow("PURCHASE_PRICE"));
                    if (price!=null) animalJson.put("purchase_price",price);
                    else animalJson.put("purchase_price","0");

                    price = animalCursor.getString(animalCursor.getColumnIndexOrThrow("SALE_PRICE"));
                    if (price!=null) animalJson.put("sale_price",price);
                    else animalJson.put("sale_price","0");

                    //set dates
                    date = animalCursor.getString(animalCursor.getColumnIndexOrThrow("DATE_OF_BIRTH"));
                    if (date!= null) animalJson.put("date_of_birth",date);
                    else animalJson.put("date_of_birth","null");

                    date = animalCursor.getString(animalCursor.getColumnIndexOrThrow("DATE_PURCHASED"));
                    if (date!= null) animalJson.put("date_purchased",date);
                    else animalJson.put("date_purchased","null");

                    date = animalCursor.getString(animalCursor.getColumnIndexOrThrow("DATE_DISTRIBUTED"));
                    if (date!= null) animalJson.put("date_distributed",date);
                    else animalJson.put("date_distributed","null");

                    date = animalCursor.getString(animalCursor.getColumnIndexOrThrow("DATE_SOLD"));
                    if (date!= null) animalJson.put("date_sold",date);
                    else animalJson.put("date_sold","null");
                    */

                    jsonAnimals.put("param_"+i,animalJson);
                    i++;

                } while(animalCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in Map:getAllAnimals");
            e.printStackTrace();
        }
        finally {
            if (animalCursor != null && !animalCursor.isClosed()) {
                animalCursor.close();
            }
        }

        Cursor eventCursor = db.rawQuery("SELECT * FROM " +
                "EVENT_TABLE E " +
                "WHERE " +
                "E.ACTIVE_FLAG = 'A' ", null);
        JSONObject jsonEvents = new JSONObject();
        JSONObject eventJson = null;
        int j = 1;
        try {
            if (eventCursor.moveToFirst()) {
                do {
                    eventJson = new JSONObject();

                    eventJson.put("animal_id",eventCursor.getString(eventCursor.getColumnIndexOrThrow("ANIMAL_ID")));
                    eventJson.put("event_id",eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_ID")));
                    eventJson.put("event_type",eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_TYPE")));
                    eventJson.put("event_date",eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_DATE")));
                    eventJson.put("event_time",eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_TIMESTAMP")));
                    eventJson.put("event_remarks",eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_REMARKS")));
                    eventJson.put("create_user",eventCursor.getString(eventCursor.getColumnIndexOrThrow("CREATE_USER")));
                    eventJson.put("create_timestamp",eventCursor.getString(eventCursor.getColumnIndexOrThrow("CREATE_TIMESTAMP")));
                    eventJson.put("record_type",eventCursor.getString(eventCursor.getColumnIndexOrThrow("RECORD_TYPE")));
                    eventJson.put("nfc_scan_entry_timestamp",eventCursor.getString(eventCursor.getColumnIndexOrThrow("NFC_SCAN_ENTRY_TIMESTAMP")));
                    eventJson.put("nfc_scan_save_timestamp",eventCursor.getString(eventCursor.getColumnIndexOrThrow("NFC_SCAN_SAVE_TIMESTAMP")));

                    jsonEvents.put("param_"+j,eventJson);
                    j++;

                } while(eventCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in Map:getAllEvents");
            e.printStackTrace();
        }
        finally {
            if (eventCursor != null && !eventCursor.isClosed()) {
                eventCursor.close();
            }
        }

        Cursor caretakerCursor = db.rawQuery("SELECT * FROM CARETAKER_TABLE C " +
                "WHERE " +
                "C.ACTIVE_FLAG = 'A' "
                , null);
        JSONObject jsonCaretakers = new JSONObject();
        JSONObject caretakerJson = null;
        int c = 1;
        try {
            if (caretakerCursor.moveToFirst()) {
                do {
                    caretakerJson = new JSONObject();
                    caretakerJson.put("animal_id",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("ANIMAL_ID")));
                    caretakerJson.put("caretaker_id",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("CARETAKER_ID")));
                    caretakerJson.put("caretaker_uid",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("CARETAKER_UID")));
                    caretakerJson.put("caretaker_name",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("CARETAKER_NAME")));
                    caretakerJson.put("caretaker_tel",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("CARETAKER_TEL")));
                    caretakerJson.put("caretaker_addr_1",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("CARETAKER_ADDR_1")));
                    caretakerJson.put("caretaker_addr_2",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("CARETAKER_ADDR_2")));
                    caretakerJson.put("caretaker_addr_3",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("CARETAKER_ADDR_3")));
                    caretakerJson.put("create_user",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("CREATE_USER")));
                    caretakerJson.put("create_timestamp",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("CREATE_TIMESTAMP")));
                    caretakerJson.put("record_type",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("RECORD_TYPE")));
                    caretakerJson.put("nfc_scan_entry_timestamp",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("NFC_SCAN_ENTRY_TIMESTAMP")));
                    caretakerJson.put("nfc_scan_save_timestamp",caretakerCursor.getString(caretakerCursor.getColumnIndexOrThrow("NFC_SCAN_SAVE_TIMESTAMP")));

                    jsonCaretakers.put("param_"+c,caretakerJson);
                    c++;

                } while(caretakerCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in Map:getAllCaretaker");
            e.printStackTrace();
        }
        finally {
            if (caretakerCursor != null && !caretakerCursor.isClosed()) {
                caretakerCursor.close();
            }
        }

        HashMap<String, String> params = new HashMap<String, String>();
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("animal", jsonAnimals);
            jsonParam.put("event", jsonEvents);
            jsonParam.put("caretaker",jsonCaretakers);
            params.put("params", jsonParam.toString());

        } catch (Exception e) {
            Log.d(TAG, "Error in Map:getAll");
            e.printStackTrace();
        }
        return params;
    }

    public void deleteAllAnimalFromServer(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("SERVER_ANIMAL_TABLE",null,null);
    }

    public void deleteAllEventFromServer(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("SERVER_EVENT_TABLE",null,null);
    }

    public void deleteAllCaretakerFromServer(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("SERVER_CARETAKER_TABLE",null,null);
    }

    public int deleteAllAnimalFromDevice(){

        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("ACTIVE_FLAG" , "*");
        values.put("LAST_UPDATE_USER", username);
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

        // updating row
        return db.update(LOCAL_ANIMAL_TABLE, values, "ACTIVE_FLAG = ?", new String[] {"A"});
    }

    public int deleteAllEventFromDevice(){

        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("ACTIVE_FLAG" , "*");
        values.put("LAST_UPDATE_USER", username);
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

        // updating row
        return db.update(LOCAL_EVENT_TABLE, values, "ACTIVE_FLAG = ?", new String[] {"A"});
    }

    public int deleteAllCaretakerFromDevice(){

        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("ACTIVE_FLAG" , "*");
        values.put("LAST_UPDATE_USER", username);
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

        // updating row
        return db.update("CARETAKER_TABLE", values, "ACTIVE_FLAG = ?", new String[] {"A"});
    }

    public int deleteAnimalbyAnimalIDFromDevice(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("ACTIVE_FLAG", "*");
        values.put("LAST_UPDATE_USER", username);
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

        //updating row
        return db.update(LOCAL_ANIMAL_TABLE, values, "ANIMAL_ID = ? AND ACTIVE_FLAG = ?", new String[] {id,"A"});
    }

    public int deleteAnimalbyAnimalIDFromServer(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("ACTIVE_FLAG", "*");
        values.put("LAST_UPDATE_USER", username);
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

        //updating row
        return db.update(SERVER_ANIMAL_TABLE, values, "ANIMAL_ID = ? AND ACTIVE_FLAG = ?", new String[] {id,"A"});
    }

    public int deleteEventbyEventIdFromDevice(String eventId, String animalId){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("ACTIVE_FLAG", "*");
        values.put("LAST_UPDATE_USER", "SYSTEM");
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

        //updating row
        return db.update("EVENT_TABLE", values, "ACTIVE_FLAG = ? AND ANIMAL_ID = ? AND EVENT_ID = ?", new String[] {"A",animalId, eventId});
    }

    public int deleteEventbyEventIdFromServer(String eventId, String animalId){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("ACTIVE_FLAG", "*");
        values.put("LAST_UPDATE_USER", "SYSTEM");
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

        //updating row
        return db.update("SERVER_EVENT_TABLE", values, "ACTIVE_FLAG = ? AND ANIMAL_ID = ? AND EVENT_ID = ?", new String[] {"A",animalId, eventId});
    }

    public int deleteCaretakerbyCaretakerUidFromDevice(String caretakerUid, String animalId){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("ACTIVE_FLAG", "*");
        values.put("LAST_UPDATE_USER", "SYSTEM");
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

        //updating row
        return db.update("CARETAKER_TABLE", values, "ACTIVE_FLAG = ? AND CARETAKER_UID = ? AND ANIMAL_ID= ?", new String[] {"A",caretakerUid, animalId});
    }

    public int deleteCaretakerbyCaretakerUidFromServer(String caretakerUid, String animalId){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("ACTIVE_FLAG", "*");
        values.put("LAST_UPDATE_USER", "SYSTEM");
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

        //updating row
        return db.update("SERVER_CARETAKER_TABLE", values, "ACTIVE_FLAG = ? AND CARETAKER_UID = ? AND ANIMAL_ID= ?", new String[] {"A",caretakerUid, animalId});
    }

    public int deleteKey(String key){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("ACTIVE_FLAG", "*");
        values.put("LAST_UPDATE_USER", "SYSTEM");
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

        //updating row
        return db.update("SYSTEM_PROPERTIES_TABLE", values, "ACTIVE_FLAG = ? AND KEY = ?", new String[] {"A",key});
    }

    public int updateSystemProperties(String key, String value){

        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("VALUE",value);
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));
        values.put("LAST_UPDATE_USER","SYSTEM");

        return db.update("SYSTEM_PROPERTIES_TABLE", values, "ACTIVE_FLAG = ? AND KEY = ?", new String[]{"A", key});
    }

    public int updateSystemSyncTimestamp(Calendar syncTimestamp){

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return updateSystemProperties("LAST_SYNC_TIMESTAMP", dateFormatter.format(syncTimestamp.getTime()));

    }

    public int updateSystemSyncMessage(String message){

        return updateSystemProperties("LAST_SYNC_MESSAGE", message);

    }


    public int updateAnimalSyncFail(String animalId, String message){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("SYNC","N");
        values.put("SYNC_MESSAGE",message);
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));
        values.put("LAST_UPDATE_USER","SYNC");

        return db.update(LOCAL_ANIMAL_TABLE, values, "ACTIVE_FLAG = ? AND ANIMAL_ID = ?", new String[]{"A", animalId});
    }

    public int updateAnimalSyncSuccess(String animalId){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("SYNC","Y");
        values.put("SYNC_MESSAGE","Sync Success");
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));
        values.put("LAST_UPDATE_USER","SYNC");

        return db.update(LOCAL_ANIMAL_TABLE, values, "ACTIVE_FLAG = ? AND ANIMAL_ID = ?", new String[]{"A", animalId});
    }

    public int updateEventSyncFail(String eventId, String animalId, String message){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("SYNC","N");
        values.put("SYNC_MESSAGE",message);
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));
        values.put("LAST_UPDATE_USER","SYNC");

        return db.update(LOCAL_EVENT_TABLE, values, "ACTIVE_FLAG = ? AND EVENT_ID = ? AND ANIMAL_ID =?", new String[]{"A", eventId, animalId});
    }

    public int updateEventSyncSuccess(String eventId, String animalId){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("SYNC","Y");
        values.put("SYNC_MESSAGE","Sync Success");
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));
        values.put("LAST_UPDATE_USER","SYNC");

        return db.update(LOCAL_EVENT_TABLE, values, "ACTIVE_FLAG = ? AND EVENT_ID = ? AND ANIMAL_ID = ?", new String[]{"A", eventId, animalId});
    }

    public int updateCaretakerSyncFail(String caretakerUid, String animalId, String message){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("SYNC","N");
        values.put("SYNC_MESSAGE",message);
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));
        values.put("LAST_UPDATE_USER","SYNC");

        return db.update("CARETAKER_TABLE", values, "ACTIVE_FLAG = ? AND CARETAKER_UID = ? AND ANIMAL_ID = ?", new String[]{"A", caretakerUid, animalId});
    }

    public int updateCaretakerSyncSuccess(String caretakerUid, String animalId){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put("SYNC","Y");
        values.put("SYNC_MESSAGE","Sync success");
        values.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));
        values.put("LAST_UPDATE_USER","SYNC");

        return db.update("CARETAKER_TABLE", values, "ACTIVE_FLAG = ? AND CARETAKER_UID = ? AND ANIMAL_ID = ?", new String[]{"A", caretakerUid, animalId});
    }


    public void deleteEvents(ArrayList<Event> deletedEventList){
        if (deletedEventList.size() == 0)
            return;
        else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues eventsContentValues = new ContentValues();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            Event j;
            for (int i = 0; i < deletedEventList.size(); i++) {
                j = deletedEventList.get(i);
                eventsContentValues.put("ACTIVE_FLAG", "*");
                eventsContentValues.put("LAST_UPDATE_USER", username);
                eventsContentValues.put("LAST_UPDATE_TIMESTAMP",dateFormatter.format(cal.getTime()));

                db.update(LOCAL_EVENT_TABLE, eventsContentValues, "ANIMAL_ID = ? AND EVENT_ID = ?",
                        new String[] {j.getAnimalId(), j.getEventId()});
            }
        }
    }
    /*
    public List<Animal> getAll() {

        List<Animal> animalList = new ArrayList<Animal>();
         // SELECT * FROM ANIMAL_TABLE
        // WHERE ANIMAL_ID = id
        String ANIMAL_SELECT_QUERY =
                String.format("SELECT * FROM ANIMAL_TABLE A ");

        String EVENT_SELECT_QUERY =
                String.format("SELECT * FROM EVENT_TABLE WHERE ANIMAL_ID = ?");


        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ANIMAL_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Animal newAnimal = new Animal();
                    newAnimal.setAnimalId(cursor.getString(cursor.getColumnIndexOrThrow("ANIMAL_ID")));
                    newAnimal.setAnimalType(cursor.getString(cursor.getColumnIndexOrThrow("ANIMAL_TYPE")));

                    List<Event> animalEventArrayList = new ArrayList<Event>();
                    Cursor eventCursor = db.rawQuery(EVENT_SELECT_QUERY, new String[]{newAnimal.getAnimalId()});
                    if (eventCursor.moveToFirst()) {
                        do {
                            Event newEvent = new Event();
                            newEvent.setEventType(eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_TYPE")));
                            newEvent.setEventDateTime(eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_TIMESTAMP")));
                            newEvent.setEventRemarks(eventCursor.getString(eventCursor.getColumnIndexOrThrow("EVENT_REMARKS")));
                            animalEventArrayList.add(newEvent);
                        } while (eventCursor.moveToNext());
                    }
                    newAnimal.setEvents(animalEventArrayList);
                    animalList.add(newAnimal);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return animalList;
    }*/

    //In house Database Manager
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }
}
