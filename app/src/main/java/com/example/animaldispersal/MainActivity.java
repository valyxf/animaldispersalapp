package com.example.animaldispersal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.appcompat.BuildConfig;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animaldispersal.localdb.LocalDBHelper;
import com.example.animaldispersal.nfc.NfcReadActivity;
import com.example.animaldispersal.remotelogger.RemoteLogger;
import com.example.davaodemo.R;
import com.example.animaldispersal.http.SyncHelper;
import com.example.animaldispersal.localdb.AnimalTable;

import org.json.JSONArray;

import org.ndeftools.Message;
import org.ndeftools.MimeRecord;
import org.ndeftools.Record;
import org.ndeftools.externaltype.ExternalTypeRecord;
import org.ndeftools.wellknown.TextRecord;

import java.io.File;
import java.io.IOException;

public class MainActivity extends NfcReadActivity {

    private String country;
    private String supervisor;
    private String nfcMessage;

    private static final int STATIC_INTEGER_VALUE = 1;

    //private ListView animalView;
    private TextView internet;
    private TextView countRecordsToSync;
    private Button syncButton;
    private Button refreshNFCButton;
    private TextView noRecordTextView;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ANIMAL = "animal";
    public static final String TAG_ANIMAL_ID = "animal_id";
    public static final String TAG_SUPERVISOR = "supervisor";
    public static final String TAG_ANIMAL_TYPE = "animal_type";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_DATE_OF_BIRTH = "date_of_birth";
    public static final String TAG_DATE_PURCHASED = "date_purchased";
    public static final String TAG_PURCHASE_PRICE = "purchase_price";
    public static final String TAG_DATE_DISTRIBUTED = "date_distributed";
    public static final String TAG_DATE_SOLD = "date_sold";
    public static final String TAG_SALE_PRICE = "sale_price";
    // animals JSONArray
    JSONArray animals = null;

    //nfc
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private static final String TAG = MainActivity.class.getName();
    protected Message message;

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;

    private LocalDBHelper localDBHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // lets start detecting NDEF message using foreground mode
        setDetecting(true);

        if (AppController.getInstance().getFirstRun()){
            startActivity(new Intent(MainActivity.this, RegisterActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        localDBHelper = LocalDBHelper.getInstance(MainActivity.this);

        //init animal list view
        fillData();

        /*
        animalView = (ListView)findViewById(R.id.listView);
        animalView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedTextView = (TextView)view.findViewById(R.id.animal_id);
                String selectedAnimalId = selectedTextView.getText().toString();

                Bundle dataBundle = new Bundle();
                dataBundle.putString("SELECTED_ANIMAL_ID", selectedAnimalId);

                Intent intent = new Intent(getApplicationContext(),AnimalDetailActivity.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
        */


        //init upload
        syncButton = (Button)findViewById(R.id.sync_button);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    Log.d(TAG, String.valueOf( localDBHelper.getCountUnsyncRecords())+" record(s) have not been synced -b4");
                    SyncHelper syncHelper = new SyncHelper(MainActivity.this);
                    syncHelper.DeviceDataUpload();
                    //Toast.makeText(MainActivity.this, "Sync Completed", Toast.LENGTH_SHORT).show();
                }
                else{
                    toast(getString(R.string.internet_connection_lost));
                    setupConnectionCheck();
                }

                fillData();
            }
        });

        //init internet connection
        internet = (TextView)findViewById(R.id.internet);
        setupConnectionCheck();

        mTextView = (TextView) findViewById(R.id.nfc_text);
        mTextView.setText(nfcMessage);
        /*
        //init nfc

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        refreshNFC();
        */

        //handleIntent(getIntent());

        refreshNFCButton = (Button) findViewById(R.id.refresh_nfc_button);
        refreshNFCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshNFC();            }
        });
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();

        SharedPreferences mPrefs = getApplicationContext().getSharedPreferences("animalDispersalPrefs", MODE_PRIVATE);
        country = mPrefs.getString("country","");
        supervisor = mPrefs.getString("username","");

        //check Internet Connection
        setupConnectionCheck();

        //fill ListView
        fillData();

        //NFC
        //setupForegroundDispatch(this, mNfcAdapter);

        //refreshNFC();

        countRecordsToSync = (TextView)findViewById(R.id.syncRecordCount);
        int count = localDBHelper.getCountUnsyncRecords();
        countRecordsToSync.setText(String.valueOf(count)+" record(s) have not been synced");
    }

    @Override
    protected void onPause(){
        //NFC
        //stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

        /*
        MenuItem item = menu.findItem(R.id.tag_lock);
        if (AppController.getInstance().isTagLock()){
            item.setTitle("Turn off Tag Lock");
        }
        else item.setTitle("Turn on Tag Lock");

        return true;
        */


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.add_animal:
                Intent i = new Intent(getApplicationContext(),AnimalDetailActivity.class);
                startActivity(i);
                break;
                */
            case R.id.search_icon:
                onSearchRequested();
                break;
            case R.id.settings:
                Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(i);
                finish();
                break;
            /*
            case R.id.tag_lock:
                if(AppController.getInstance().isTagLock()) {
                    AppController.getInstance().setTagLock(false);
                    toast("Tags WILL NOT be locked");
                    item.setTitle("Turn on Tag Lock");
                } else {
                    AppController.getInstance().setTagLock(true);
                    toast("Tags WILL be locked");
                    item.setTitle("Turn off Tag Lock");
                }
                break;
            */
            /*case R.id.db:
                Intent dbmanager = new Intent(getApplicationContext(),AndroidDatabaseManager.class);
                startActivity(dbmanager);
                break;
            */
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    //TODO TO BE REMOVED WHEN LIVE
    //Populate List with Device Records
    private void fillData(){
        /*
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { AnimalTable.COLUMN_ANIMAL_ID , AnimalTable.COLUMN_ANIMAL_TYPE, AnimalTable.COLUMN_SUPERVISOR,
                "CARETAKER_NAME", AnimalTable.COLUMN_DATE_DISTRIBUTED};
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.animal_id, R.id.animal_type, R.id.supervisor,
                R.id.caretaker, R.id.date_distributed };

        // localDBHelper is a SQLiteOpenHelper class connecting to SQLite
        Log.d(TAG, "Entering getAll");
        Cursor animalCursor = localDBHelper.getAll();
        if (animalCursor.getCount()>0)
        {
            adapter = new SimpleCursorAdapter(this, R.layout.item_animal, animalCursor, from, to, 0);
            animalView = (ListView) findViewById(R.id.listView);
            animalView.setAdapter(adapter);
            noRecordTextView = (TextView)findViewById(R.id.no_animals_text);
            noRecordTextView.setVisibility(View.INVISIBLE);
        }
        else
        {
            noRecordTextView = (TextView)findViewById(R.id.no_animals_text);
            noRecordTextView.setVisibility(View.VISIBLE);
        }
        */
    }


    private void refreshNFC(){

        detectNfcStateChanges();
        /*
        if (!mNfcAdapter.isEnabled()) {

            new AlertDialog.Builder(MainActivity.this).setTitle("NFC Disabled")
                    .setMessage(getString(R.string.nfc_not_ready)).setCancelable(false)
                    .setPositiveButton("Go to NFC Settings", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                        }
                    }).create().show();
            //mTextView.setText(R.string.nfc_not_ready);
        } else {
            mTextView.setText(R.string.nfc_ready);
        }*/
    }


    private void startAnimalDetailActivity(String tagAnimalId, String tagSupervisor, String tagCountry){
        Log.d(TAG,"AnimalId read from NFC tag: "+tagAnimalId);

        if (tagAnimalId == null){
            toast(getString(R.string.nfc_tag_no_animal_id));
            return;
        }

        int role = AppController.getInstance().getUserRole();
        switch (role) {
            case 2:
                /*
                if (getIsTagReadOnly()) {
                    if (localDBHelper.getAnimalById(tagAnimalId)== null) {
                        toast("Tag data not synced.");
                        return;
                    }
                }*/
                break;
            case 1:
                if (getIsTagReadOnly()) {
                    if (tagCountry == null) {
                        toast(getString(R.string.nfc_tag_no_country));
                        return;
                    }
                    if (!checkTagCountry(tagCountry)) {
                        toast(getString(R.string.nfc_tag_no_access));
                        return;
                    }
                    /*
                    if (localDBHelper.getAnimalById(tagAnimalId)== null) {
                        toast("Tag data not synced.");
                        return;
                    }*/
                }
                break;
            case 0:
                if (getIsTagReadOnly()) {
                    if (tagSupervisor == null) {
                        toast(getString(R.string.nfc_tag_no_supervisor));
                        return;
                    }
                    if (!checkTagSupervisor(tagSupervisor)) {
                        toast(getString(R.string.nfc_tag_no_access));
                        return;
                    }
                }
                break;
        }

        if (getIsTagReadOnly()) {
            if (localDBHelper.getAnimalById(tagAnimalId)== null) {
                toast(getString(R.string.nfc_tag_not_synced));
                return;
            }
        }

        Bundle dataBundle = new Bundle();
        dataBundle.putString("SELECTED_ANIMAL_ID", tagAnimalId);
        Intent intent = new Intent(getApplicationContext(),AnimalDetailActivity.class);
        intent.putExtras(dataBundle);
        startActivity(intent);
    }

    private boolean checkTagCountry(String tagCountry){
        SharedPreferences mPrefs = getSharedPreferences("animalDispersalPrefs", Context.MODE_PRIVATE);
        String loginCountry = mPrefs.getString("country", "");
        Log.d(TAG, "tagCountry "+tagCountry);
        Log.d(TAG, "loginCountry " +loginCountry);

        if (loginCountry.equalsIgnoreCase(tagCountry))
            return true;
        return false;

    }

    private boolean checkTagSupervisor(String tagSupervisor){
        SharedPreferences mPrefs = getSharedPreferences("animalDispersalPrefs", Context.MODE_PRIVATE);
        String loginUser = mPrefs.getString("username", "");
        if (loginUser.equalsIgnoreCase(tagSupervisor))
            return true;
        return false;

    }

    private void setupConnectionCheck(){
        if (isOnline()) {
            internet.setText("Internet On – Ready to Sync");
            syncButton.setEnabled(true);
        }
        else
        {
            internet.setText("Internet Off – Not Ready to Sync");
            syncButton.setEnabled(false);
        }
    }

    private boolean isOnline(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    //setupforeground dispatch was here.

    //stopForegroundDispatch was here

    @Override
    public void readNdefMessage(Message message) {
        if(message.size() > 1) {
            Log.d(TAG, getString(R.string.readMultipleRecordNDEFMessage));
        } else {
            Log.d(TAG, getString(R.string.readSingleRecordNDEFMessage));
        }

        this.message = message;

        // process message

        // show in log
        // iterate through all records in message
        Log.d(TAG, "Found " + message.size() + " NDEF records");
        String tagAnimalId = null;
        String tagSupervisor = null;
        String tagCountry = null;

        for(int k = 0; k < message.size(); k++) {
            Record record = message.get(k);

            Log.d(TAG, "Record " + k + " type " + record.getClass().getSimpleName());

            try {

                // your own code here, for example:
                if (record instanceof MimeRecord) {
                    MimeRecord mr = (MimeRecord) record;
                    if (mr.getMimeType().equals("cci/animalid")) {
                        tagAnimalId = new String(mr.getData(), "UTF-8");
                    }
                    if (mr.getMimeType().equals("cci/animaldispersalic")) {
                        tagSupervisor = new String(mr.getData(), "UTF-8");
                    }
                    if (mr.getMimeType().equals("cci/animalcountry")) {
                        tagCountry = new String(mr.getData(), "UTF-8");
                    }


                } else if (record instanceof ExternalTypeRecord) {
                    // ..
                } else if (record instanceof TextRecord) {
                    TextRecord tr = (TextRecord) record;
                    tagAnimalId = tr.getText();
                    // ..
                } else { // more else
                    // ..
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Log.e(TAG, e.toString());
            }
        }

        startAnimalDetailActivity(tagAnimalId, tagSupervisor, tagCountry);



        // show in gui
        //showList();

    }


    /**
     * An empty NDEF message was read.
     *
     */

    @Override
    protected void readEmptyNdefMessage() {
        Log.d(TAG, getString(R.string.readEmptyMessage));
    }

    /**
     *
     * Something was read via NFC, but it was not an NDEF message.
     *
     * Handling this situation is out of scope of this project.
     *
     */

    @Override
    protected void readNonNdefMessage() {
        Log.d(TAG, getString(R.string.readNonNDEFMessage));
    }

    /**
     *
     * NFC feature was found and is currently enabled
     *
     */

    @Override
    protected void onNfcStateEnabled() {
        nfcMessage = getString(R.string.nfcAvailableEnabled);
    }

    /**
     *
     * NFC feature was found but is currently disabled
     *
     */

    @Override
    protected void onNfcStateDisabled() {
        nfcMessage = getString(R.string.nfcAvailableDisabled);
    }

    /**
     *
     * NFC setting changed since last check. For example, the user enabled NFC in the wireless settings.
     *
     */

    @Override
    protected void onNfcStateChange(boolean enabled) {
        if (mTextView != null){
            if(enabled) {
                nfcMessage = getString(R.string.nfcAvailableEnabled);

            } else {
                nfcMessage = getString(R.string.nfcAvailableDisabled);
            }
            mTextView.setText(nfcMessage);
        }
    }

    /**
     *
     * This device does not have NFC hardware
     *
     */

    @Override
    protected void onNfcFeatureNotFound() {
        nfcMessage = getString(R.string.noNfcMessage);
    }


    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }


    protected void onTagLost() {
        toast(getString(R.string.tagLost));
    }

}


