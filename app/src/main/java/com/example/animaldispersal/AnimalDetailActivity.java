package com.example.animaldispersal;

import org.apache.commons.lang.RandomStringUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animaldispersal.adapter.EventAdapter;
import com.example.animaldispersal.dataobject.Animal;
import com.example.animaldispersal.dataobject.Caretaker;
import com.example.animaldispersal.localdb.LocalDBHelper;
import com.example.animaldispersal.nfc.NfcWriteActivity;
import com.example.davaodemo.R;
import com.example.animaldispersal.dataobject.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ndeftools.Message;
import org.ndeftools.MimeRecord;
import org.ndeftools.externaltype.AndroidApplicationRecord;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.nfc.NdefMessage;

import android.util.Log;


public class AnimalDetailActivity extends NfcWriteActivity {

    private static final String TAG = AnimalDetailActivity.class.getName();

    private CoordinatorLayout coordinatorLayout;

    private EditText mAnimalId;
    private EditText mSupervisor;
    //private EditText mAnimalType;
    private EditText mCountry;
    private EditText mDateOfBirth;
    private EditText mDatePurchased;
    private EditText mPurchasePrice;
    private EditText mDateDistributed;
    private EditText mDateSold;
    private EditText mSalePrice;

    private EditText mCaretakerId;
    private EditText mCaretakerName;
    private EditText mCaretakerTel;
    private EditText mCaretakerAddr1;
    private EditText mCaretakerAddr2;
    private EditText mCaretakerAddr3;

    private RadioGroup mGenderRadioGroup;
    private RadioButton mGenderRadioButton;
    private RadioButton mRadioButton;
    private RadioButton fRadioButton;

    private RadioGroup mAnimalTypeRadioGroup;
    private RadioButton mAnimalTypeSelection;
    private RadioButton cowRadioButton;
    private RadioButton goatRadioButton;
    private RadioButton pigRadioButton;

    private TextView noEventsTextView;
    private TextView addEventLink;
    private TextView infoTextView;
    private TextView dateOfBirthLabel;
    private TextView datePurchasedLabel;
    private TextView purchasePriceLabel;
    private TextView dateDistributedLabel;
    private TextView dateSoldLabel;
    private TextView salePriceLabel;
    private TextView caretakerIdLabel;
    private TextView caretakerNameLabel;
    private TextView caretakerTelephoneLabel;
    private TextView caretakerAddrLine1Label;
    private TextView caretakerAddrLine2Label;
    private TextView caretakerAddrLine3Label;


    private Button saveButton;
    private Button saveButton2;
    private Button saveButton3;
    private Button saveButton4;
    private Button editButton;
    private Button editButton2;
    private Button editButton3;
    private Button addEventButton;
    private View doneActionView;
    private View cancelActionView;

    private RelativeLayout relativeLayout;
    private RelativeLayout relativeLayout2;
    private RelativeLayout relativeLayout3;
    private RelativeLayout relativeLayout4;

    private String animalRecordType;
    private String caretakerRecordType;
    private String caretakerUid;
    //private Boolean animalChanged=false;

    private SharedPreferences mPrefs;
    //private ViewUtils vu;
    private String mode;
    private Animal existingAnimal;
    private Animal originalServerAnimal;
    private Caretaker originalServerCaretaker;
    private Caretaker existingCaretaker;
    private String userCountry;
    private int userRole;
    private String userName;
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Event> deletedEventList = new ArrayList<>();

    private int selectedEventIndex;
    //private String eventSno;

    private int colorEmphasis;
    private int colorPrimaryDark;
    private Drawable disabledEditext;
    private Drawable enabledEdittext;

    private ProgressDialog dialog;

    //nfc
    private NfcAdapter mNfcAdapter;
    public static final String MIME_TEXT_PLAIN = "dispersal/id";

    private static final int STATIC_INTEGER_VALUE = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);
        setDetecting(true);
        setScanOff();


        mAnimalId = (EditText)findViewById(R.id.animal_id);
        mSupervisor = (EditText)findViewById(R.id.supervisor);
        //mAnimalType = (EditText)findViewById(R.id.animal_type);
        mDateOfBirth = (EditText)findViewById(R.id.date_of_birth);
        mDatePurchased = (EditText)findViewById(R.id.date_purchased);
        mPurchasePrice = (EditText)findViewById(R.id.purchase_price);
        mDateDistributed = (EditText)findViewById(R.id.date_distributed);
        mDateSold = (EditText)findViewById(R.id.date_sold);
        mSalePrice = (EditText)findViewById(R.id.sale_price);
        mCountry = (EditText)findViewById(R.id.country);


        mGenderRadioGroup = (RadioGroup)findViewById(R.id.gender_radio_group);
        mRadioButton = (RadioButton)findViewById(R.id.male_radio_button);
        fRadioButton = (RadioButton)findViewById(R.id.female_radio_button);

        mAnimalTypeRadioGroup = (RadioGroup)findViewById(R.id.animal_type_radiogroup);
        cowRadioButton = (RadioButton) findViewById(R.id.cow_radio_button);
        goatRadioButton = (RadioButton) findViewById(R.id.goat_radio_button);
        pigRadioButton = (RadioButton) findViewById(R.id.pig_radio_button);

        mCaretakerId = (EditText)findViewById (R.id.caretaker_id);
        mCaretakerName = (EditText)findViewById(R.id.caretaker_name);
        mCaretakerTel = (EditText)findViewById(R.id.caretaker_tel);
        mCaretakerAddr1 = (EditText)findViewById(R.id.caretaker_addr1);
        mCaretakerAddr2 = (EditText)findViewById(R.id.caretaker_addr2);
        mCaretakerAddr3 = (EditText)findViewById(R.id.caretaker_addr3);

        infoTextView           =(TextView)findViewById(R.id.infotext);
        dateOfBirthLabel       =(TextView)findViewById(R.id.dateOfBirthLabel);
        datePurchasedLabel     =(TextView)findViewById(R.id.datePurchasedLabel);
        purchasePriceLabel     =(TextView)findViewById(R.id.purchasePriceLabel);
        dateDistributedLabel   =(TextView)findViewById(R.id.dateDistributedLabel);
        dateSoldLabel          =(TextView)findViewById(R.id.dateSoldLabel);
        salePriceLabel         =(TextView)findViewById(R.id.salePriceLabel);
        caretakerIdLabel       =(TextView)findViewById(R.id.caretakerIdLabel);
        caretakerNameLabel     =(TextView)findViewById(R.id.caretakerNameLabel);
        caretakerTelephoneLabel=(TextView)findViewById(R.id.caretakerTelephoneLabel);
        caretakerAddrLine1Label=(TextView)findViewById(R.id.caretakerAddrLine1Label);
        caretakerAddrLine2Label=(TextView)findViewById(R.id.caretakerAddrLine2Label);
        caretakerAddrLine3Label=(TextView)findViewById(R.id.caretakerAddrLine3Label);

        colorEmphasis = ContextCompat.getColor(this, R.color.emphasis);
        colorPrimaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        enabledEdittext = mAnimalId.getBackground();
        disabledEditext = ContextCompat.getDrawable(this, R.drawable.disabled_edittext);

        saveButton = (Button) findViewById(R.id.savebutton);
        saveButton.setEnabled(true);
        saveButton2 = (Button) findViewById(R.id.savebutton2);
        saveButton2.setEnabled(true);
        saveButton3 = (Button) findViewById(R.id.savebutton3);
        saveButton3.setEnabled(true);
        saveButton4 = (Button) findViewById(R.id.savebutton4);
        saveButton4.setEnabled(true);

        editButton = (Button) findViewById(R.id.editbutton);
        editButton.setVisibility(View.GONE);
        editButton2 = (Button) findViewById(R.id.editbutton2);
        editButton2.setVisibility(View.GONE);
        editButton3 = (Button) findViewById(R.id.editbutton3);
        editButton3.setVisibility(View.GONE);

        //addEventLink = (TextView)findViewById(R.id.add_event_link);
        addEventButton = (Button)findViewById(R.id.addEventButton);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutAnimalDetailsI);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayoutAnimalDetailsII);
        relativeLayout3 = (RelativeLayout) findViewById(R.id.relativeLayoutCaretaker);
        relativeLayout4 = (RelativeLayout) findViewById(R.id.relativeLayoutEvent);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        final ListView eventsView = (ListView) findViewById(R.id.listView1);
        final EventAdapter eventAdapter = new EventAdapter(this, events);
        eventsView.setAdapter(eventAdapter);

        Bundle extras = getIntent().getExtras();

        mPrefs = getSharedPreferences("animalDispersalPrefs", Context.MODE_PRIVATE);
        userCountry = mPrefs.getString("country", "");
        userName = mPrefs.getString("username", "");
        userRole = mPrefs.getInt("role",0);

        if (extras ==null){
            fillNewAnimal(false,null);
        }
        else {
            String extrasAnimalId = extras.getString("SELECTED_ANIMAL_ID");
            Log.d(TAG, "extrasAnimalId: "+extrasAnimalId);
            if ((isExistingAnimal(extrasAnimalId))){
                fillExistingAnimal(extrasAnimalId);
            }else fillNewAnimal(true, extrasAnimalId);

            /*
            //New animal id scanned by NFC
            if ((extras.getString("NEW_ANIMAL_ID"))!=null) {
                fillNewAnimal(true, extras.getString("NEW_ANIMAL_ID"));
            }
            //Existing animal
            if ((extras.getString("SELECTED_ANIMAL_ID"))!=null) {
                fillExistingAnimal(extras.getString("SELECTED_ANIMAL_ID"));
            }
            */
        }



        //12OCT2016 INITIALISE FIELDS
        final Calendar cal = Calendar.getInstance();
        mDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), datePickerListener,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
                DatePicker picker = dialog.getDatePicker();
                picker.setId(mDateOfBirth.getId());
                dialog.show();
            }
        });

        mDatePurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), datePickerListener,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
                DatePicker picker = dialog.getDatePicker();
                picker.setId(mDatePurchased.getId());
                dialog.show();
            }
        });

        mDateDistributed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), datePickerListener,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
                DatePicker picker = dialog.getDatePicker();
                picker.setId(mDateDistributed.getId());
                dialog.show();
            }
        });

        mDateSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), datePickerListener,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
                DatePicker picker = dialog.getDatePicker();
                picker.setId(mDateSold.getId());
                dialog.show();
            }
        });

        // Entry point 1of2 to event detail activity
        //addEventLink.setOnClickListener(new View.OnClickListener(){
        addEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(),EventDetailActivity.class);
                String eventSno = RandomStringUtils.randomAlphanumeric(10);
                i.putExtra("EVENT_ID", eventSno);
                i.putExtra("MODE", mode);
                //i.putExtra("EDIT_MODE", editMode);
                startActivityForResult(i, STATIC_INTEGER_VALUE);
            }
        });

        //12OCT2016 INITIALISE BUTTONS
        saveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.performClick();
            }
        });
        saveButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.performClick();
            }
        });
        saveButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.performClick();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = getCurrentFocus();
                Log.d(TAG,"Current Focus: "+view.getId());

                switch (mode){
                    case "AR":
                        //Check duplicate animal id record
                        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(v.getContext());
                        Animal animal = localDBHelper.getAnimalById(mAnimalId.getText().toString().trim());
                        if (animal != null) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Error");
                            builder.setMessage("Duplicate Animal ID found.")
                                    .setCancelable(true)
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        //Check mandatory fields are filled
                        else {
                            if (TextUtils.isEmpty(mAnimalId.getText().toString().trim()) ||
                                    TextUtils.isEmpty(mSupervisor.getText().toString().trim()) ||
                                    TextUtils.isEmpty(mCountry.getText().toString().trim()) ||
                                    mAnimalTypeRadioGroup.getCheckedRadioButtonId() == -1 ||
                                    mGenderRadioGroup.getCheckedRadioButtonId() == -1
                                    ){
                                toast(getString(R.string.fill_in_mandatory_fields));
                                break;
                            }

                            if (!"Pakistan".equalsIgnoreCase(getText(mCountry)) &&
                                    !"Philippines".equalsIgnoreCase(getText(mCountry)) &&
                                    !"Bangladesh".equalsIgnoreCase(getText(mCountry))) {
                                toast(getString(R.string.invalid_country));
                                break;
                            }
                            //saveRecords();
                            writeAndLockTag();
                            //setResult(RESULT_OK);
                            //finish();
                        }
                        break;

                    case "N":
                            if (isCaretakerIdEmpty()) {
                                toast(getString(R.string.fill_in_caretaker_id));
                                break;
                            }
                            saveRecords();
                            notifyAnimalRecordSaved();
                        break;
                    default:
                        //Check mandatory fields are filled

                        if (isCaretakerIdEmpty()) {
                            Toast toast = Toast.makeText(AnimalDetailActivity.this, getString(R.string.fill_in_caretaker_id), Toast.LENGTH_LONG);
                            toast.show();
                            break;
                        }
                        if (TextUtils.isEmpty(mAnimalId.getText().toString().trim()) ||
                                    TextUtils.isEmpty(mSupervisor.getText().toString().trim()) ||
                                    TextUtils.isEmpty(mCountry.getText().toString().trim()) ||
                                    mAnimalTypeRadioGroup.getCheckedRadioButtonId() == -1 ||
                                    mGenderRadioGroup.getCheckedRadioButtonId() == -1
                                    ) {
                            makeToast();
                            break;
                        }

                        if (!"Pakistan".equalsIgnoreCase(getText(mCountry)) &&
                                !"Philippines".equalsIgnoreCase(getText(mCountry)) &&
                                !"Bangladesh".equalsIgnoreCase(getText(mCountry))) {
                            Toast toast = Toast.makeText(AnimalDetailActivity.this, getString(R.string.invalid_country), Toast.LENGTH_LONG);
                            toast.show();
                            break;
                        }

                        saveRecords();
                        //setResult(RESULT_OK);
                        //finish();
                        notifyAnimalRecordSaved();
                        break;
                }
                view.requestFocus();
            }
        }
        );

        //editMode = false;
        editButton2.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  editButton.performClick();
              }
        });
        editButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton.performClick();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editMode = true;

                //why cant we just adopt the mode from the animal record??
                if (existingCaretaker != null) {
                    switch (caretakerRecordType) {
                        case "S":
                        case "US":
                            caretakerRecordType = "US";
                            mode = "US";
                            break;
                        case "D":
                        case "UD":
                            caretakerRecordType = "UD";
                            mode = "UD";
                            break;
                    }
                }

                switch (animalRecordType){
                    case "S":case "US":
                        animalRecordType = "US";
                        mode = "US";
                        break;
                    case"D":case"UD":
                        animalRecordType = "UD";
                        mode = "UD";
                        break;
                }

                setTitle("ANIMAL RECORD");
                refreshLayout();
            }
        });

        /* 12OCT2016 NEW BUTTONS ON HOLD
        // add the custom view to the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.menu_animal_detail);
        cancelActionView = actionBar.getCustomView().findViewById(R.id.action_cancel);
        cancelActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        doneActionView = actionBar.getCustomView().findViewById(R.id.action_done);
        doneActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mAnimalId.getText().toString().trim()) ||
                        TextUtils.isEmpty(mAnimalType.getText().toString().trim()) ||
                        TextUtils.isEmpty(mSupervisor.getText().toString().trim()))
                {
                    makeToast();
                }
                else {

                    saveRecords();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        */
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String dateStr = String.valueOf(selectedYear) + "-"+
                    String.valueOf(selectedMonth+1) +"-"+
                    String.valueOf(selectedDay);
            EditText dateText = (EditText)findViewById(view.getId());
            dateText.setText(dateStr);
        }
    };

    @Override
    protected void onSaveInstanceState (Bundle outState){

        outState.putParcelableArrayList("EVENTS_ARRAY", events);
        outState.putString("MODE",mode);
        //outState.putBoolean("EDIT_MODE",editMode);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState){

        super.onRestoreInstanceState(saveInstanceState);

            events = saveInstanceState.getParcelableArrayList("EVENTS_ARRAY");
            mode = saveInstanceState.getString("MODE");
            //editMode = saveInstanceState.getBoolean("EDIT_MODE");
            fillEvents(events);
            refreshLayout();

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.animal_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_icon:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AnimalDetailActivity.this);
                alertDialog.setTitle(getString(R.string.home));
                switch (mode){
                    case "N":case "US":case "UD":
                        alertDialog.setMessage(getString(R.string.leave_despite_not_save));
                        alertDialog.setIcon(R.drawable.ic_action_back);
                        alertDialog.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        alertDialog.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                        alertDialog.show();
                        break;
                    default:
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onPause(){
        super.onPause();

        if (dialog != null){

        }
    }

    @Override
    // Exit point 1of1 from EventDetailActivity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode) {
            case (STATIC_INTEGER_VALUE) : {
                if (resultCode == Activity.RESULT_OK) {

                    Event event = data.getParcelableExtra("EVENT");
                    if (event.getEventUpdated()) {
                        Event deleteEvent = (Event)data.getParcelableExtra("DELETED_EVENT");
                        deletedEventList.add(deleteEvent);
                        events.remove(selectedEventIndex);
                    }

                    //need to set animal id here.
                    events.add(event);
                    fillEvents(events);
                    editButton.callOnClick();
                    //refreshLayout();
                    ListView eventsView = (ListView) findViewById(R.id.listView1);
                    eventsView.requestFocus();


                }
                break;
            }
        }


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, "Delete Event");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /*TODO Should be on hold. not on selected
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Event itemSelected = (Event)displayEventList.remove(info.position);
        if ("D".equals(itemSelected.getRecordType())) {
            switch (item.getItemId()) {
                case DELETE_ID:
                    displayEventList.remove(info.position);
                    fillEvents(displayEventList);
                    refreshLayout();
                    System.out.println("onContextItemSelected");
                    return true;
            }
        }
        */
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        fillEvents(events);
        refreshLayout();

    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        if (dialog!= null) {
            dialog.dismiss();
            Log.d(TAG, "NFC Write Complete" );
            saveRecords();
            notifyAnimalRecordSaved();
        }
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AnimalDetailActivity.this);
        alertDialog.setTitle(getString(R.string.back));
        switch (mode){
            case "N":case "US":case "UD":
                alertDialog.setMessage(getString(R.string.leave_despite_not_save));
                alertDialog.setIcon(R.drawable.ic_action_back);
                alertDialog.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
                break;
            default:
                finish();
                break;
        }
    }

    //12OCT2016 POPULATE FIELDS FOR NEW ANIMAL RECORD NFC and NO NFC x2 CALL
    private void fillNewAnimal(boolean byNFC, String animalID) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF8800")));
        setTitle("NEW ANIMAL REGISTRATION");

        //animalRecordType = "N";
        //caretakerRecordType = "N";
        mode = "AR";

        if (byNFC) {
            mAnimalId.setText(animalID);
            mAnimalId.setEnabled(false);
        }
        else mAnimalId.setEnabled(true);


        if (!(userRole ==2)) {
            mCountry.setText(userCountry);
        }
        mSupervisor.setText(userName);

    }

    //12OCT2016 POPULATE FIELDS FOR EXISTING ANIMAL RECORD x1 CALL
    private void fillExistingAnimal(String selectedAnimalId){

        mode = "V";
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)));
        setTitle("ANIMAL RECORD");

        /**************OBTAIN RECORD***************/
        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(this);
        existingAnimal = localDBHelper.getAnimalById(selectedAnimalId);
        existingCaretaker = localDBHelper.getCaretakerByUid(existingAnimal.getCaretakerUid());
        originalServerAnimal = localDBHelper.getOriginalServerAnimalById(selectedAnimalId);
        originalServerCaretaker = localDBHelper.getOriginalCaretakerByUidFromServer(existingAnimal.getCaretakerUid());
        events = localDBHelper.getEventsById(selectedAnimalId);

        /********* BASIC POPULATION OF FIELDS***********/
        //FILL ANIMAL DETAILS
        animalRecordType = existingAnimal.getRecordType();

        mAnimalId.setText(existingAnimal.getAnimalId());
        mSupervisor.setText(existingAnimal.getSupervisor());
        switch (existingAnimal.getAnimalType()) {
            case "Cow":
                cowRadioButton.setChecked(true);
                goatRadioButton.setChecked(false);
                pigRadioButton.setChecked(false);
                break;
            case "Goat":
                cowRadioButton.setChecked(false);
                goatRadioButton.setChecked(true);
                pigRadioButton.setChecked(false);
                break;
            case "Pig":
                cowRadioButton.setChecked(false);
                goatRadioButton.setChecked(false);
                pigRadioButton.setChecked(true);
                break;
        }

        if ("Female".equals(existingAnimal.getGender())) {
            fRadioButton.setChecked(true);
            mRadioButton.setChecked(false);
        }
        else{
            mRadioButton.setChecked(true);
            fRadioButton.setChecked(false);
        }

        if (existingAnimal.getDateOfBirth() != null)
            mDateOfBirth.setText(existingAnimal.getDateOfBirth());
        if (existingAnimal.getCountry() != null)
            mCountry.setText(existingAnimal.getCountry());
        if (existingAnimal.getDatePurchased() != null)
            mDatePurchased.setText(existingAnimal.getDatePurchased());
        if (existingAnimal.getPurchasePrice() != null)
            mPurchasePrice.setText(existingAnimal.getPurchasePrice());
        if (existingAnimal.getDateDistributed()!= null)
            mDateDistributed.setText(existingAnimal.getDateDistributed());
        if (existingAnimal.getSalePrice() != null)
            mSalePrice.setText(existingAnimal.getSalePrice());
        if (existingAnimal.getDateSold()!= null)
            mDateSold.setText(existingAnimal.getDateSold());

        //FILL CARETAKER DETAILS
        /*if (existingCaretaker == null) {
            caretakerRecordType = "N";
        }
        else {*/
        if (existingCaretaker != null) {
            caretakerRecordType = existingCaretaker.getRecordType();
            mCaretakerId.setText(existingCaretaker.getCaretakerId());
            mCaretakerName.setText(existingCaretaker.getCaretakerName());
            mCaretakerTel.setText(existingCaretaker.getCaretakerTel());
            if (existingCaretaker.getCaretakerAddr1() != null)
                mCaretakerAddr1.setText(existingCaretaker.getCaretakerAddr1());
            if (existingCaretaker.getCaretakerAddr2() != null)
                mCaretakerAddr2.setText(existingCaretaker.getCaretakerAddr2());
            if (existingCaretaker.getCaretakerAddr3() != null)
                mCaretakerAddr3.setText(existingCaretaker.getCaretakerAddr3());
        }

        //FILL EVENT DETAILS
        fillEvents(events);
        refreshLayout();
    }

    private void fillEvents(List<Event> eventList){

        noEventsTextView = (TextView)findViewById(R.id.no_events_text);
        if (eventList == null ) {
            noEventsTextView.setVisibility(View.VISIBLE);
            return;
        }
        if (eventList.size()==0)
            noEventsTextView.setVisibility(View.VISIBLE);
        else noEventsTextView.setVisibility(View.GONE);

        ListView eventsView = (ListView) findViewById(R.id.listView1);
        EventAdapter eventAdapter = new EventAdapter(this, eventList);
        eventsView.setAdapter(eventAdapter);
        setListViewHeightBasedOnChildren(eventsView);
        registerForContextMenu(eventsView);

        eventsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Entry point 2of2 to EventDetailActivity
                selectedEventIndex = position;
                Intent i = new Intent(getApplicationContext(),EventDetailActivity.class);
                Event itemSelected = (Event)events.get(selectedEventIndex);
                i.putExtra("SELECTED_EVENT",itemSelected);
                i.putExtra("MODE", mode);
                //i.putExtra("EDIT_MODE",editMode);

                String eventSno = itemSelected.getEventId();
                i.putExtra("EVENT_ID", eventSno);

                startActivityForResult(i, STATIC_INTEGER_VALUE);
            }
        });

    }

    private void refreshLayout(){

        switch (mode) {
            case "AR":
                relativeLayout2.setVisibility(View.GONE);
                relativeLayout3.setVisibility(View.GONE);
                relativeLayout4.setVisibility(View.GONE);

                editButton.setVisibility(View.GONE);
                saveButton2.setVisibility(View.VISIBLE);

                if (userRole ==2)
                    mCountry.setEnabled(true);
                else mCountry.setEnabled(false);

                saveButton2.setText("REGISTER NEW ANIMAL");
                saveButton2.setBackgroundColor(Color.parseColor("#FF8800"));

                break;
            case "N":
                relativeLayout2.setVisibility(View.VISIBLE);
                relativeLayout3.setVisibility(View.VISIBLE);
                relativeLayout4.setVisibility(View.VISIBLE);

                editButton.setVisibility(View.GONE);
                editButton2.setVisibility(View.GONE);
                editButton3.setVisibility(View.GONE);
                addEventButton.setVisibility(View.VISIBLE);
                //addEventLink.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                saveButton2.setVisibility(View.VISIBLE);
                saveButton3.setVisibility(View.VISIBLE);
                saveButton4.setVisibility(View.VISIBLE);

                break;

            case "UD":
                infoTextView.setText("Info: This record has not been synced. You may update open fields.");
                infoTextView.setVisibility(View.VISIBLE);
                relativeLayout2.setVisibility(View.VISIBLE);
                relativeLayout3.setVisibility(View.VISIBLE);
                relativeLayout4.setVisibility(View.VISIBLE);

                editButton.setVisibility(View.GONE);
                editButton2.setVisibility(View.GONE);
                editButton3.setVisibility(View.GONE);
                addEventButton.setVisibility(View.VISIBLE);
                //addEventLink.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                saveButton2.setVisibility(View.VISIBLE);
                saveButton3.setVisibility(View.VISIBLE);
                saveButton4.setVisibility(View.VISIBLE);

                saveButton2.setText("SAVE");
                saveButton2.setBackgroundResource(android.R.drawable.btn_default_small);

                //set mandatory fields also enabled//
                if (userRole ==2) {
                    mCountry.setEnabled(true);
                    mCountry.setBackground(enabledEdittext);
                }
                else{
                    mCountry.setEnabled(false);
                    mCountry.setBackground(disabledEditext);
                }
                cowRadioButton.setEnabled(true);
                goatRadioButton.setEnabled(true);
                pigRadioButton.setEnabled(true);
                mRadioButton.setEnabled(true);
                fRadioButton.setEnabled(true);
                mDateOfBirth.setEnabled(true);

                mDateOfBirth.setEnabled(true);
                mDatePurchased.setEnabled(true);
                mPurchasePrice.setEnabled(true);
                mDateDistributed.setEnabled(true);
                mDateSold.setEnabled(true);
                mSalePrice.setEnabled(true);

                mCaretakerId.setEnabled(true);
                mCaretakerName.setEnabled(true);
                mCaretakerTel.setEnabled(true);
                mCaretakerAddr1.setEnabled(true);
                mCaretakerAddr2.setEnabled(true);
                mCaretakerAddr3.setEnabled(true);

                mDateOfBirth.setBackground(enabledEdittext);
                mDatePurchased.setBackground(enabledEdittext);
                mPurchasePrice.setBackground(enabledEdittext);
                mDateDistributed.setBackground(enabledEdittext);
                mDateSold.setBackground(enabledEdittext);
                mSalePrice.setBackground(enabledEdittext);

                mCaretakerId.setBackground(enabledEdittext);
                mCaretakerName.setBackground(enabledEdittext);
                mCaretakerTel.setBackground(enabledEdittext);
                mCaretakerAddr1.setBackground(enabledEdittext);
                mCaretakerAddr2.setBackground(enabledEdittext);
                mCaretakerAddr3.setBackground(enabledEdittext);

                break;

            case "US":
                infoTextView.setText("Info: This record has been synced. You may only update the fields in blue.");
                infoTextView.setVisibility(View.VISIBLE);
                relativeLayout2.setVisibility(View.VISIBLE);
                relativeLayout3.setVisibility(View.VISIBLE);
                relativeLayout4.setVisibility(View.VISIBLE);

                editButton.setVisibility(View.GONE);
                editButton2.setVisibility(View.GONE);
                editButton3.setVisibility(View.GONE);
                addEventButton.setVisibility(View.VISIBLE);
                //addEventLink.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                saveButton2.setVisibility(View.VISIBLE);
                saveButton3.setVisibility(View.VISIBLE);
                saveButton4.setVisibility(View.VISIBLE);

                saveButton2.setText("SAVE");
                saveButton2.setBackgroundResource(android.R.drawable.btn_default_small);

                mAnimalId.setEnabled(false);
                mCountry.setEnabled(false);
                mSupervisor.setEnabled(false);
                cowRadioButton.setEnabled(false);
                goatRadioButton.setEnabled(false);
                pigRadioButton.setEnabled(false);
                mRadioButton.setEnabled(false);
                fRadioButton.setEnabled(false);
                mDateOfBirth.setEnabled(false);

                if (originalServerAnimal == null) break;

                if (originalServerAnimal.getDateOfBirth() == null){
                    mDateOfBirth.setEnabled(true);
                    mDateOfBirth.setBackground(enabledEdittext);
                    dateOfBirthLabel.setTextColor(colorEmphasis);

                }
                if (originalServerAnimal.getDatePurchased() == null){
                    mDatePurchased.setEnabled(true);
                    mDatePurchased.setBackground(enabledEdittext);
                    datePurchasedLabel.setTextColor(colorEmphasis);
                }
                if (originalServerAnimal.getPurchasePrice() == null ||
                        "0".equals(originalServerAnimal.getPurchasePrice())){
                    mPurchasePrice.setEnabled(true);
                    mPurchasePrice.setBackground(enabledEdittext);
                    purchasePriceLabel.setTextColor(colorEmphasis);
                }
                if (originalServerAnimal.getDateDistributed() == null){
                    mDateDistributed.setEnabled(true);
                    mDateDistributed.setBackground(enabledEdittext);
                    dateDistributedLabel.setTextColor(colorEmphasis);
                }
                if (originalServerAnimal.getDateSold() == null){
                    mDateSold.setEnabled(true);
                    mDateSold.setBackground(enabledEdittext);
                    dateSoldLabel.setTextColor(colorEmphasis);
                }
                if (originalServerAnimal.getSalePrice() == null ||
                        "0".equals(originalServerAnimal.getSalePrice())){
                    mSalePrice.setEnabled(true);
                    mSalePrice.setBackground(enabledEdittext);
                    salePriceLabel.setTextColor(colorEmphasis);
                }

                if (originalServerCaretaker == null) {
                    mCaretakerId.setEnabled(true);
                    mCaretakerName.setEnabled(true);
                    mCaretakerTel.setEnabled(true);
                    mCaretakerAddr1.setEnabled(true);
                    mCaretakerAddr2.setEnabled(true);
                    mCaretakerAddr3.setEnabled(true);

                    caretakerIdLabel.setTextColor(colorEmphasis);
                    caretakerNameLabel.setTextColor(colorEmphasis);
                    caretakerTelephoneLabel.setTextColor(colorEmphasis);
                    caretakerAddrLine1Label.setTextColor(colorEmphasis);
                    caretakerAddrLine2Label.setTextColor(colorEmphasis);
                    caretakerAddrLine3Label.setTextColor(colorEmphasis);

                    mCaretakerId.setBackground(enabledEdittext);
                    mCaretakerName.setBackground(enabledEdittext);
                    mCaretakerTel.setBackground(enabledEdittext);
                    mCaretakerAddr1.setBackground(enabledEdittext);
                    mCaretakerAddr2.setBackground(enabledEdittext);
                    mCaretakerAddr3.setBackground(enabledEdittext);
                }
                else {

                    if (originalServerCaretaker.getCaretakerId() == null) {
                        mCaretakerId.setEnabled(true);
                        mCaretakerId.setBackground(enabledEdittext);
                        caretakerIdLabel.setTextColor(colorEmphasis);
                    }
                    if (originalServerCaretaker.getCaretakerName() == null) {
                        mCaretakerName.setEnabled(true);
                        mCaretakerName.setBackground(enabledEdittext);
                        caretakerNameLabel.setTextColor(colorEmphasis);
                    }
                    if (originalServerCaretaker.getCaretakerTel() == null) {
                        mCaretakerTel.setEnabled(true);
                        mCaretakerTel.setBackground(enabledEdittext);
                        caretakerTelephoneLabel.setTextColor(colorEmphasis);
                    }
                    if (originalServerCaretaker.getCaretakerAddr1() == null) {
                        mCaretakerAddr1.setEnabled(true);
                        mCaretakerAddr1.setBackground(enabledEdittext);
                        caretakerAddrLine1Label.setTextColor(colorEmphasis);
                    }
                    if (originalServerCaretaker.getCaretakerAddr2() == null) {
                        mCaretakerAddr2.setEnabled(true);
                        mCaretakerAddr2.setBackground(enabledEdittext);
                        caretakerAddrLine2Label.setTextColor(colorEmphasis);
                    }
                    if (originalServerCaretaker.getCaretakerAddr3() == null) {
                        mCaretakerAddr3.setEnabled(true);
                        mCaretakerAddr3.setBackground(enabledEdittext);
                        caretakerAddrLine3Label.setTextColor(colorEmphasis);
                    }
                }
                break;

            case "V":
                relativeLayout2.setVisibility(View.VISIBLE);
                relativeLayout3.setVisibility(View.VISIBLE);
                relativeLayout4.setVisibility(View.VISIBLE);

                switch (animalRecordType) {
                    case "D":
                        infoTextView.setText("Info: This record has not been synced.");
                        infoTextView.setVisibility(View.VISIBLE);
                        break;
                    case "US":
                        infoTextView.setText("Info: This record has been synced.");
                        infoTextView.setVisibility(View.VISIBLE);
                        break;
                    case "S":
                        if (recordEditable()){
                            infoTextView.setText("Info: This record has been synced. ");
                            infoTextView.setVisibility(View.VISIBLE);
                        }
                        else {
                            infoTextView.setText("Info: This record has been synced. It is a fully filled record so it cannot be edited.");
                            infoTextView.setVisibility(View.VISIBLE);
                        }
                        break;
                    default: break;
                }

                if (recordEditable()){
                    editButton.setVisibility(View.VISIBLE);
                    editButton2.setVisibility(View.VISIBLE);
                    editButton3.setVisibility(View.VISIBLE);
                }
                else {
                    editButton.setVisibility(View.GONE);
                    editButton2.setVisibility(View.GONE);
                    editButton3.setVisibility(View.GONE);
                }
                addEventButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
                saveButton2.setVisibility(View.GONE);
                saveButton3.setVisibility(View.GONE);
                saveButton4.setVisibility(View.GONE);

                dateOfBirthLabel.setTextColor(colorPrimaryDark);
                datePurchasedLabel.setTextColor(colorPrimaryDark);
                purchasePriceLabel.setTextColor(colorPrimaryDark);
                dateDistributedLabel.setTextColor(colorPrimaryDark);
                dateSoldLabel.setTextColor(colorPrimaryDark);
                salePriceLabel.setTextColor(colorPrimaryDark);

                caretakerIdLabel.setTextColor(colorPrimaryDark);
                caretakerNameLabel.setTextColor(colorPrimaryDark);
                caretakerTelephoneLabel.setTextColor(colorPrimaryDark);
                caretakerAddrLine1Label.setTextColor(colorPrimaryDark);
                caretakerAddrLine2Label.setTextColor(colorPrimaryDark);
                caretakerAddrLine3Label.setTextColor(colorPrimaryDark);

                mAnimalId.setEnabled(false);
                mCountry.setEnabled(false);
                mSupervisor.setEnabled(false);

                cowRadioButton.setEnabled(false);
                goatRadioButton.setEnabled(false);
                pigRadioButton.setEnabled(false);
                mRadioButton.setEnabled(false);
                fRadioButton.setEnabled(false);

                mDateOfBirth.setEnabled(false);
                mDatePurchased.setEnabled(false);
                mPurchasePrice.setEnabled(false);
                mDateDistributed.setEnabled(false);
                mDateSold.setEnabled(false);
                mSalePrice.setEnabled(false);

                mCaretakerId.setEnabled(false);
                mCaretakerName.setEnabled(false);
                mCaretakerTel.setEnabled(false);
                mCaretakerAddr1.setEnabled(false);
                mCaretakerAddr2.setEnabled(false);
                mCaretakerAddr3.setEnabled(false);

                mAnimalId.setBackground(disabledEditext);
                mCountry.setBackground(disabledEditext);
                mSupervisor.setBackground(disabledEditext);

                mDateOfBirth.setBackground(disabledEditext);
                mDatePurchased.setBackground(disabledEditext);
                mPurchasePrice.setBackground(disabledEditext);
                mDateDistributed.setBackground(disabledEditext);
                mDateSold.setBackground(disabledEditext);
                mSalePrice.setBackground(disabledEditext);

                mCaretakerId.setBackground(disabledEditext);
                mCaretakerName.setBackground(disabledEditext);
                mCaretakerTel.setBackground(disabledEditext);
                mCaretakerAddr1.setBackground(disabledEditext);
                mCaretakerAddr2.setBackground(disabledEditext);
                mCaretakerAddr3.setBackground(disabledEditext);

                break;

        }

    }

    private void saveAnimal(){

        Log.d(TAG, "ENTERING SAVE ANIMAL");
        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(this);

        int selectedAnimalTypeId = mAnimalTypeRadioGroup.getCheckedRadioButtonId();
        mAnimalTypeSelection = (RadioButton) findViewById(selectedAnimalTypeId);

        int selectedId = mGenderRadioGroup.getCheckedRadioButtonId();
        mGenderRadioButton = (RadioButton) findViewById(selectedId);

        if (existingAnimal==null) {

            animalRecordType = "D";
        }
        if (existingCaretaker ==null) {
            if (getText(mCaretakerId) != null)
                caretakerUid = RandomStringUtils.randomAlphanumeric(10);
            else caretakerUid = null;
        }
        else caretakerUid = existingAnimal.getCaretakerUid();

        Log.d(TAG, mAnimalId.getText().toString());

        Animal newAnimal = new Animal(
                getText(mAnimalId),
                getText(mSupervisor),
                getText(mAnimalTypeSelection),
                getText(mGenderRadioButton),
                getText(mDateOfBirth),
                getText(mCountry),
                getText(mDatePurchased),
                getText(mPurchasePrice),
                getText(mDateDistributed),
                getText(mDateSold),
                getText(mSalePrice),
                caretakerUid,
                animalRecordType

        );
        if (!newAnimal.equals(existingAnimal)) {
            switch (animalRecordType){
                case "D": case "UD":
                    localDBHelper.deleteAnimalbyAnimalIDFromDevice(newAnimal.getAnimalId());
                    break;
                case "US":
                    int deleteServerAnimal = localDBHelper.deleteAnimalbyAnimalIDFromServer(newAnimal.getAnimalId());
                    if (deleteServerAnimal==0)
                        localDBHelper.deleteAnimalbyAnimalIDFromDevice(newAnimal.getAnimalId());
                    break;
                default:
                    break;
            }

            localDBHelper.insertAnimal(newAnimal);
        }
    }

    private void saveCaretaker(){

        Log.d(TAG,"ENTERING SAVE CARETAKER");

        if (getText(mCaretakerId)==null) return;

        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(this);

        if (existingCaretaker==null) caretakerRecordType = "D";

        Caretaker caretaker = new Caretaker(
                getText(mAnimalId),
                getText(mCaretakerId),
                caretakerUid,
                getText(mCaretakerName),
                getText(mCaretakerTel),
                getText(mCaretakerAddr1),
                getText(mCaretakerAddr2),
                getText(mCaretakerAddr3),
                caretakerRecordType
        );
        if (!caretaker.equals(existingCaretaker)){
            if (existingCaretaker != null) {
                switch (caretakerRecordType) {
                    case "D": case "UD":
                        localDBHelper.deleteCaretakerbyCaretakerUidFromDevice(existingCaretaker.getCaretakerUid(),
                                existingCaretaker.getAnimalId());
                        break;
                    case "US":
                        int deleteServerCaretaker = localDBHelper.deleteCaretakerbyCaretakerUidFromServer
                                (existingCaretaker.getCaretakerUid(),existingCaretaker.getAnimalId());
                        if (deleteServerCaretaker ==0)
                            localDBHelper.deleteCaretakerbyCaretakerUidFromDevice(existingCaretaker.getCaretakerUid(),
                                    existingCaretaker.getAnimalId());
                        break;
                    default: break;

                }
            }
            localDBHelper.insertCaretaker(caretaker);
        }

    }

    private void saveEvents(){

        Log.d(TAG, "ENTERING SAVE EVENTS");
        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(this);
        for (int i = 0; i < events.size(); i++){
            Log.d(TAG,"event_i "+i);


            //If the event was updated
            if (events.get(i).getEventUpdated()) {
                Log.d(TAG, "events.get(i)"+events.get(i).toString());

                //check nature of updated record
                switch (events.get(i).getRecordType()) {

                    //was previously a device unsynced record. delete device record. insert new record
                    case "UD":
                        localDBHelper.deleteEventbyEventIdFromDevice(events.get(i).getEventId(),getText(mAnimalId));
                        localDBHelper.insertEvent(events.get(i));
                        break;

                    //was previously a server synced record. delete the server record. insert new record
                    case "US":
                        int deleteServerEvent = localDBHelper.deleteEventbyEventIdFromServer
                                (events.get(i).getEventId(),getText(mAnimalId));
                        if (deleteServerEvent ==0)
                            localDBHelper.deleteEventbyEventIdFromDevice(events.get(i).getEventId(),getText(mAnimalId));
                        localDBHelper.insertEvent(events.get(i));
                        break;

                    //a new unsaved record. insert the record
                    case "N":
                        events.get(i).setAnimalId(getText(mAnimalId));
                        events.get(i).setRecordType("D");
                        localDBHelper.deleteEventbyEventIdFromDevice(events.get(i).getEventId(),getText(mAnimalId));
                        localDBHelper.insertEvent(events.get(i));
                        break;
                    default:
                        break;
                }
            }
            //record was not updated
            else {
                switch (events.get(i).getRecordType()) {
                    //Completely new unamended record. insert the record
                    case "N":
                        events.get(i).setAnimalId(getText(mAnimalId));
                        events.get(i).setRecordType("D");
                        localDBHelper.insertEvent(events.get(i));
                        break;
                    //do nothing with the old record.
                    default:
                        break;
                }

            }
/*
            if ("D".equals(events.get(i).getRecordType()) ||
                    events.get(i).getEventUpdated()) {
                localDBHelper.insertEvent(events.get(i));
            }*/
        }
    }

    private void saveRecords(){

        saveAnimal();
        //save animal
        /*
        if there is an existing animal, is it different?
            if animal exists in device, delete animal
            if animal exists in server, delete animal

            save entire animal into device
        not different, do nothing

         */

        saveCaretaker();
        //save existingCaretaker
        /*
        if there is an existing existingCaretaker, is it different?
            if existingCaretaker exists in device, delete existingCaretaker
            if existingCaretaker exists in server, delete existingCaretaker

            save entire existingCaretaker into device
        not different, do nothing
         */

        saveEvents();
        //save events
        /*
        loop through all events
            if there is an existing event, is it different?
                if event exists in device, delete event
                if event exists in server, delete event

                save event into device
            not different, do nothing
         */

        /* START OVERHAUL
        switch (mode) {
            case "D":case "UD": case "US":
                localDBHelper.deleteAnimalbyAnimalIDFromDevice(mAnimalId.getText().toString().trim());
                if (existingCaretaker == null) break;
                if (saveCaretaker()) localDBHelper.deleteCaretakerbyCaretakerUidFromDevice(mCaretakerId.getText().toString().trim(),
                        mAnimalId.getText().toString().trim());
                break;
            case "S":
                localDBHelper.deleteAnimalbyAnimalIDFromServer(mAnimalId.getText().toString().trim());
                if (existingCaretaker == null) break;
                if (saveCaretaker()) localDBHelper.deleteCaretakerbyCaretakerUidFromServer(mCaretakerId.getText().toString().trim(),
                        mAnimalId.getText().toString().trim());
                break;
        }

        //If it is a "N" animal record OR "D"/"S"/"UD"/"US" animal record is amended save the animal, event, existingCaretaker
        if (!"V".equals(mode)) {


            //INSERT ANIMAL


            newAnimal.setAnimalId(mAnimalId.getText().toString().trim());
            newAnimal.setSupervisor(vu.getText(mSupervisor));
            newAnimal.setCaretakerUid(vu.getText(mCaretakerId));
            if (!TextUtils.isEmpty(mAnimalTypeSelection.getText().toString().trim()))
                existingAnimal.setAnimalType(mAnimalTypeSelection.getText().toString().trim());
            if (!TextUtils.isEmpty(mGenderRadioButton.getText().toString().trim()))
                existingAnimal.setGender(mGenderRadioButton.getText().toString().trim());
            if (!TextUtils.isEmpty(mDateOfBirth.getText().toString().trim()))
                existingAnimal.setDateOfBirth(mDateOfBirth.getText().toString().trim());
            if (!TextUtils.isEmpty(mCountry.getText().toString().trim()))
                existingAnimal.setCountry(mCountry.getText().toString().trim());
            if (!TextUtils.isEmpty(mDatePurchased.getText().toString().trim()))
                existingAnimal.setDatePurchased(mDatePurchased.getText().toString().trim());
            if (!TextUtils.isEmpty(mPurchasePrice.getText().toString().trim()))
                existingAnimal.setPurchasePrice(mPurchasePrice.getText().toString().trim());
            if (!TextUtils.isEmpty(mDateDistributed.getText().toString().trim()))
                existingAnimal.setDateDistributed(mDateDistributed.getText().toString().trim());
            if (!TextUtils.isEmpty(mDateSold.getText().toString().trim()))
                existingAnimal.setDateSold(mDateSold.getText().toString().trim());
            if (!TextUtils.isEmpty(mSalePrice.getText().toString().trim()))
                existingAnimal.setSalePrice(mSalePrice.getText().toString().trim());
            if ("N".equals(animalRecordType)) existingAnimal.setRecordType("D");
            else existingAnimal.setRecordType(animalRecordType);
            //localDBHelper.insertAnimal(existingAnimal);


            //INSERT EVENTS
            Event k;
            for (int i = 0; i < deletedEventList.size(); i++) {
                k = deletedEventList.get(i);
                k.setAnimalId(mAnimalId.getText().toString().trim());
            }
            localDBHelper.deleteEvents(deletedEventList);

            //Did not save animal id in event detail page, in case animal id changes after events are added
            Event j;
            for (int i = 0; i < events.size(); i++) {
                j = events.get(i);
                switch (j.getRecordType()){
                    //then D/UD/US records will always be saved again and again. Need to differentiate new&changed vs existing records
                    case "D":case "UD":case "US":
                        j.setAnimalId(mAnimalId.getText().toString().trim());
                        localDBHelper.insertEvent(j);
                        break;
                }

                /*
                if ("N".equals(j.getRecordType())) {
                    j.setAnimalId(mAnimalId.getText().toString().trim());
                    //j.setRecordType("D");
                    localDBHelper.insertEvent(j);
                }
            }



            //INSERT CARETAKER
            //need to check if existingCaretaker is created on a new animal record or an edited animal record
            Caretaker newCaretaker = new Caretaker();
            //If the record type is US/UD, insert a new existingCaretaker record IF the existingCaretaker has changed.
            // If not during sync a duplicate record data is sent up for nothing

            System.out.println("saveCaretaker:" +saveCaretaker());
            if (saveCaretaker()){
                newCaretaker.setAnimalId(mAnimalId.getText().toString().trim());
                newCaretaker.setCaretakerUid(mCaretakerId.getText().toString().trim());

                if (!TextUtils.isEmpty(mCaretakerName.getText().toString().trim()))
                    newCaretaker.setCaretakerName(mCaretakerName.getText().toString().trim());
                if (!TextUtils.isEmpty(mCaretakerTel.getText().toString().trim()))
                    newCaretaker.setCaretakerTel(mCaretakerTel.getText().toString().trim());
                if (!TextUtils.isEmpty(mCaretakerAddr1.getText().toString().trim()))
                    newCaretaker.setCaretakerAddr1(mCaretakerAddr1.getText().toString().trim());
                if (!TextUtils.isEmpty(mCaretakerAddr2.getText().toString().trim()))
                    newCaretaker.setCaretakerAddr2(mCaretakerAddr2.getText().toString().trim());
                if (!TextUtils.isEmpty(mCaretakerAddr3.getText().toString().trim()))
                    newCaretaker.setCaretakerAddr3(mCaretakerAddr3.getText().toString().trim());
                //if (existingCaretaker.getRecordType()== null) existingCaretaker.setRecordType("D");
                if ("N".equals(caretakerRecordType)) newCaretaker.setRecordType("D");
                else newCaretaker.setRecordType(caretakerRecordType);
                localDBHelper.insertCaretaker(newCaretaker);
            }
        } END OVERHAUL*/

    }

    private void makeToast() {
        Toast.makeText(AnimalDetailActivity.this, "Please fill in Country, Animal Type and Gender." , Toast.LENGTH_LONG).show();
    }

    private boolean isCaretakerIdEmpty(){

        if(TextUtils.isEmpty(mCaretakerId.getText().toString().trim()) && (
                (!TextUtils.isEmpty(mCaretakerName.getText().toString().trim())) ||
                        (!TextUtils.isEmpty(mCaretakerTel.getText().toString().trim()))
                ))
            return true;
        return false;

    }

    private boolean saveCaretaker2(){

        if ("N".equals(mode) && !TextUtils.isEmpty(mCaretakerId.getText().toString().trim()))
            return true;
        if ("N".equals(mode) && TextUtils.isEmpty(mCaretakerId.getText().toString().trim()))
            return false;

        if (existingCaretaker == null && (!TextUtils.isEmpty(mCaretakerId.getText().toString().trim())))
            return true;
        if (existingCaretaker == null && (TextUtils.isEmpty(mCaretakerId.getText().toString().trim())))
            return false;

        if (existingCaretaker.getCaretakerId() != null ) {
            if (!existingCaretaker.getCaretakerId().equals(mCaretakerId.getText().toString().trim()))
                return true;
        }
        else if (!"".equals(mCaretakerId.getText().toString().trim())) return true;

        if (existingCaretaker.getCaretakerName() != null ) {
            if (!existingCaretaker.getCaretakerName().equals(mCaretakerName.getText().toString().trim()))
                return true;
        }
        else if (!"".equals(mCaretakerName.getText().toString().trim())) return true;

        if (existingCaretaker.getCaretakerTel() != null ) {
            if (!existingCaretaker.getCaretakerTel().equals(mCaretakerTel.getText().toString().trim()))
                return true;
        }
        else if (!"".equals(mCaretakerTel.getText().toString().trim())) return true;

        if (existingCaretaker.getCaretakerAddr1() != null ) {
            if (!existingCaretaker.getCaretakerAddr1().equals(mCaretakerAddr1.getText().toString().trim()))
                return true;
        }
        else if (!"".equals(mCaretakerAddr1.getText().toString().trim())) return true;

        if (existingCaretaker.getCaretakerAddr2() != null ) {
            if (!existingCaretaker.getCaretakerAddr2().equals(mCaretakerAddr2.getText().toString().trim()))
                return true;
        }
        else if (!"".equals(mCaretakerAddr2.getText().toString().trim())) return true;

        if (existingCaretaker.getCaretakerAddr3() != null ) {
            if (!existingCaretaker.getCaretakerAddr3().equals(mCaretakerAddr3.getText().toString().trim()))
                return true;
        }
        else if (!"".equals(mCaretakerAddr3.getText().toString().trim())) return true;

        return false;
    }

    //Fully filled server records are not editable - so the edit button should not be visible
    private boolean recordEditable(){
        if("S".equals(animalRecordType)) {
            if (originalServerAnimal == null)
                return true;
            if (originalServerAnimal.getDateOfBirth()==null)
                return true;
            if (originalServerAnimal.getDatePurchased()==null)
                return true;
            if (originalServerAnimal.getPurchasePrice()==null)
                return true;
            if (originalServerAnimal.getDateDistributed()==null)
                return true;
            if (originalServerAnimal.getDateSold()==null)
                return true;
            if (originalServerAnimal.getSalePrice()==null)
                return true;
            if (existingCaretaker.getCaretakerName() ==null)
                return true;
            if (existingCaretaker.getCaretakerTel() ==null)
                return true;
            if (existingCaretaker.getCaretakerAddr1() ==null)
                return true;
            if (existingCaretaker.getCaretakerAddr2() ==null)
                return true;
            if (existingCaretaker.getCaretakerAddr3() ==null)
                return true;

            return false;
        }
        return true;

    }

    private void writeAndLockTag(){

        setNfcWriteValues(false, getText(mAnimalId));
        setScanOn();

        dialog = new ProgressDialog(AnimalDetailActivity.this);
        SpannableString ss1=  new SpannableString("Rescan Tag To Confirm Animal Registration");
        ss1.setSpan(new RelativeSizeSpan(1.5f), 0, ss1.length(), 0);
        ss1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), 0, ss1.length(), 0);
        dialog.setMessage(ss1);
        dialog.setCancelable(false);
        dialog.show();
    }

    private String getText(TextView textView){
        if (textView == null) return null;
        if (!TextUtils.isEmpty(textView.getText().toString().trim()))
            return textView.getText().toString().trim();
        else return null;
    }

    private String getText(RadioButton radioButton){
        if (radioButton == null) return null;
        if (!TextUtils.isEmpty(radioButton.getText().toString().trim()))
            return radioButton.getText().toString().trim();
        else return null;
    }

    private boolean isExistingAnimal(String animalId){
        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(AnimalDetailActivity.this);
        if (localDBHelper.getAnimalById(animalId)!= null)
            return true;
        else return false;
    }

    private void notifyAnimalRecordSaved(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AnimalDetailActivity.this);
        builder.setMessage("Animal Record has been saved.")
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mode = "V";
                        fillExistingAnimal(getText(mAnimalId));
                        dialog.cancel();

                        //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(intent);
                        //finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            Log.d(TAG, "View "+String.valueOf(view.getId()));
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     *
     * Create an NDEF message to be written when a tag is within range.
     *
     * @return the message to be written
     */

    @Override
    protected NdefMessage createNdefMessage() {

        // compose our own message
        Message message = new Message();

        /*
        // add an Android Application Record so that this app is launches if a tag is scanned :-)
        AndroidApplicationRecord androidApplicationRecord = new AndroidApplicationRecord();
        androidApplicationRecord.setPackageName(getPlayIdentifier());
        message.add(androidApplicationRecord);
        */

        try {
            // add a Text Record with the message which is entered
            //EditText text = (EditText) findViewById(R.id.text);
            MimeRecord animalIdRecord = new MimeRecord();
            animalIdRecord.setMimeType("cci/animalid");
            animalIdRecord.setData(getText(mAnimalId).getBytes("UTF-8"));
            message.add(animalIdRecord);

            MimeRecord supervisorRecord = new MimeRecord();
            supervisorRecord.setMimeType("cci/animaldispersalic");
            supervisorRecord.setData(getText(mSupervisor).getBytes("UTF-8"));
            message.add(supervisorRecord);

            MimeRecord countryRecord = new MimeRecord();
            countryRecord.setMimeType("cci/animalcountry");
            countryRecord.setData(getText(mCountry).getBytes("UTF-8"));
            message.add(countryRecord);

        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, "message.getNdefMessage()"+message.getNdefMessage().toString());
        return message.getNdefMessage();
    }

    /**
     * Get Google Play application identifier
     *
     * @return
     */

    private String getPlayIdentifier() {
        PackageInfo pi;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.applicationInfo.packageName;
        } catch (final NameNotFoundException e) {
            return getClass().getPackage().getName();
        }
    }

    /**
     *
     * Writing NDEF message to tag failed.
     *
     * @param e exception
     */

    @Override
    protected void writeNdefFailed(Exception e) {
        toast(getString(R.string.ndefWriteFailed, e.toString()));
        if (dialog!= null) {
            dialog.dismiss();
            dialog=null;
            Log.d(TAG, "NFC Write Failed"+e.toString() );
        }
    }

    /**
     *
     * Tag is not writable or write-protected.
     *
     * @return
     */

    @Override
    public void writeNdefNotWritable() {
        toast(getString(R.string.tagNotWritable));
    }

    /**
     *
     * Tag capacity is lower than NDEF message size.
     *
     * @return
     */

    @Override
    public void writeNdefTooSmall(int required, int capacity) {
        toast(getString(R.string.tagTooSmallMessage,  required, capacity));
    }


    /**
     *
     * Unable to write this type of tag.
     *
     */

    @Override
    public void writeNdefCannotWriteTech() {
        toast(getString(R.string.cannotWriteTechMessage));
    }

    /**
     *
     * Successfully wrote NDEF message to tag.
     *
     */

    @Override
    protected void writeNdefSuccess() {
        //toast(getString(R.string.ndefWriteSuccess));
    }

    /**
     *
     * NFC feature was found and is currently enabled
     *
     */

    @Override
    protected void onNfcStateEnabled() {
        //toast(getString(R.string.nfcAvailableEnabled));
    }

    /**
     *
     * NFC feature was found but is currently disabled
     *
     */

    @Override
    protected void onNfcStateDisabled() {
        //toast(getString(R.string.nfcAvailableDisabled));
    }

    /**
     *
     * NFC setting changed since last check. For example, the user enabled NFC in the wireless settings.
     *
     */

    @Override
    protected void onNfcStateChange(boolean enabled) {
        if(enabled) {
            toast(getString(R.string.nfcSettingEnabled));
        } else {
            toast(getString(R.string.nfcSettingDisabled));
        }
    }

    /**
     *
     * This device does not have NFC hardware
     *
     */

    @Override
    protected void onNfcFeatureNotFound() {
        toast(getString(R.string.noNfcMessage));
    }

    @Override
    protected void onTagLost() {
        toast(getString(R.string.tagLost));
    }

    @Override
    protected boolean nfcFailAnimalIdCheck(){
        if (dialog!= null) {
            dialog.dismiss();
            dialog=null;
            Log.d(TAG, "Animal ID in the scanned tag is not the same as the original animal record");
        }
        toast(getString(R.string.nfcFailAnimalIDCheck));
        return false;
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }


}
