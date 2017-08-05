package com.example.animaldispersal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animaldispersal.adapter.EventAdapter;
import com.example.animaldispersal.dataobject.Animal;
import com.example.animaldispersal.dataobject.Caretaker;
import com.example.animaldispersal.dataobject.Event;
import com.example.animaldispersal.localdb.LocalDBHelper;
import com.example.animaldispersal.nfc.NfcWriteActivity;
import com.example.davaodemo.R;

import org.apache.commons.lang.RandomStringUtils;
import org.ndeftools.Message;
import org.ndeftools.MimeRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AnimalDetailActivity extends NfcWriteActivity {

    private static final String TAG = AnimalDetailActivity.class.getName();

    private CoordinatorLayout coordinatorLayout;

    private EditText mAnimalId;
    private EditText mSupervisor;
    //private EditText mAnimalType;
    private EditText mDateOfBirth;
    private EditText mDatePurchased;
    private EditText mPurchasePrice;
    private EditText mDateDistributed;
    private EditText mPurchaseWeight;
    private EditText mPurchaseHeight;
    private EditText mSaleWeight;
    private EditText mSaleHeight;
    private EditText mDateSold;
    private EditText mSalePrice;

    private EditText mCaretakerId;
    private EditText mCaretakerName;
    private EditText mCaretakerTel;
    private EditText mCaretakerAddr1;
    private EditText mCaretakerAddr2;
    private EditText mCaretakerAddr3;

    private Spinner mCountry2;
    private Spinner mPurchaseWeightUnit;
    private Spinner mPurchaseHeightUnit;
    private Spinner mSaleWeightUnit;
    private Spinner mSaleHeightUnit;

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
    private TextView purchaseWeightLabel;
    private TextView purchaseHeightLabel;
    private TextView saleWeightLabel;
    private TextView saleHeightLabel;
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

    private LinearLayout linearLayout;
    private LinearLayout linearLayout2;
    private LinearLayout linearlayout3;
    private LinearLayout linearlayout4;
    private LinearLayout ll2header ;
    private LinearLayout ll2;
    private LinearLayout llcheader ;
    private LinearLayout llc;
    private LinearLayout lleheader ;
    private LinearLayout lle;

    private ImageView image;
    private ImageView image2;
    private ImageView image3;

    private String animalRecordType;
    private String caretakerRecordType;
    private String caretakerUid;
    private String nfcScanEntryTimestamp;
    private String nfcScanSaveTimestamp;
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
        setScanOn(false);


        mAnimalId = (EditText)findViewById(R.id.animal_id);
        mSupervisor = (EditText)findViewById(R.id.supervisor);
        //mAnimalType = (EditText)findViewById(R.id.animal_type);
        mDateOfBirth = (EditText)findViewById(R.id.date_of_birth);
        mDatePurchased = (EditText)findViewById(R.id.date_purchased);
        mPurchasePrice = (EditText)findViewById(R.id.purchase_price);
        mPurchaseWeight = (EditText)findViewById(R.id.purchase_weight);
        mPurchaseHeight = (EditText)findViewById(R.id.purchase_height);
        mPurchaseWeightUnit = (Spinner) findViewById(R.id.purchase_weight_unit);
        mPurchaseHeightUnit = (Spinner)findViewById(R.id.purchase_height_unit);
        mSaleWeight = (EditText)findViewById(R.id.sale_weight);
        mSaleHeight = (EditText)findViewById(R.id.sale_height);
        mSaleWeightUnit = (Spinner) findViewById(R.id.sale_weight_unit);
        mSaleHeightUnit = (Spinner)findViewById(R.id.sale_height_unit);
        mDateDistributed = (EditText)findViewById(R.id.date_distributed);
        mDateSold = (EditText)findViewById(R.id.date_sold);
        mSalePrice = (EditText)findViewById(R.id.sale_price);
        mCountry2 = (Spinner)findViewById(R.id.country2);

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
        purchaseWeightLabel =(TextView)findViewById(R.id.purchaseWeightLabel);
        purchaseHeightLabel =(TextView)findViewById(R.id.purchaseHeightLabel);
        dateSoldLabel          =(TextView)findViewById(R.id.dateSoldLabel);
        salePriceLabel         =(TextView)findViewById(R.id.salePriceLabel);
        saleWeightLabel =(TextView)findViewById(R.id.saleWeightLabel);
        saleHeightLabel =(TextView)findViewById(R.id.saleHeightLabel);
        caretakerIdLabel       =(TextView)findViewById(R.id.caretakerIdLabel);
        caretakerNameLabel     =(TextView)findViewById(R.id.caretakerNameLabel);
        caretakerTelephoneLabel=(TextView)findViewById(R.id.caretakerTelephoneLabel);
        caretakerAddrLine1Label=(TextView)findViewById(R.id.caretakerAddrLine1Label);
        caretakerAddrLine2Label=(TextView)findViewById(R.id.caretakerAddrLine2Label);
        caretakerAddrLine3Label=(TextView)findViewById(R.id.caretakerAddrLine3Label);

        colorEmphasis = ContextCompat.getColor(this, R.color.emphasis);
        colorPrimaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        //enabledEdittext = mAnimalId.getBackground();
        //disabledEditext = ContextCompat.getDrawable(this, R.drawable.disabled_edittext);

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

        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutAnimalDetailsI);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayoutAnimalDetailsII);
        linearlayout3 = (LinearLayout) findViewById(R.id.linearLayoutCaretaker);
        linearlayout4 = (LinearLayout) findViewById(R.id.linearLayoutEvent);

        image = (ImageView) findViewById(R.id.imageView);
        image2 = (ImageView) findViewById(R.id.imageView2);
        image3 = (ImageView) findViewById(R.id.imageView3);

        ll2 = (LinearLayout) findViewById(R.id.AnimalDetailsII);
        ll2header = (LinearLayout) findViewById(R.id.linearLayoutAnimalDetailsIIHeader);
        ll2header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (View.GONE == ll2.getVisibility()) {
                    ll2.setVisibility(View.VISIBLE);
                    image.setImageResource(R.mipmap.ic_expanded);
                }
                else {
                    ll2.setVisibility(View.GONE);
                    image.setImageResource(R.mipmap.ic_collapsed);
                }
            }
        });

        llc = (LinearLayout) findViewById(R.id.caretaker);
        llcheader = (LinearLayout) findViewById(R.id.linearLayoutCaretakerHeader);
        llcheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (View.GONE == llc.getVisibility()) {
                    llc.setVisibility(View.VISIBLE);
                    image2.setImageResource(R.mipmap.ic_expanded);
                }
                else {
                    llc.setVisibility(View.GONE);
                    image2.setImageResource(R.mipmap.ic_collapsed);
                }
            }
        });

        lle = (LinearLayout) findViewById(R.id.event);
        lleheader = (LinearLayout) findViewById(R.id.linearLayoutEventHeader);
        lleheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (View.GONE == lle.getVisibility()) {
                    lle.setVisibility(View.VISIBLE);
                    image3.setImageResource(R.mipmap.ic_expanded);
                }
                else {
                    lle.setVisibility(View.GONE);
                    image3.setImageResource(R.mipmap.ic_collapsed);
                }
            }
        });



        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcScanSaveTimestamp = null;

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
            nfcScanEntryTimestamp = null;
        }
        else {
            String extrasAnimalId = extras.getString("SELECTED_ANIMAL_ID");
            Log.d(TAG, "extrasAnimalId: "+extrasAnimalId);
            if ((isExistingAnimal(extrasAnimalId))){
                try {
                    fillExistingAnimal(extrasAnimalId);
                } catch (Exception e){
                    toast("An unexpected error occurred. Please contact the administrator.");
                }
            }
            else fillNewAnimal(true, extrasAnimalId);

            nfcScanEntryTimestamp = extras.getString("NFC_SCAN_ENTRY_TIMESTAMP");
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

                switch (mode){
                    case "AR":
                        //Check duplicate animal id record
                        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(v.getContext());
                        Animal animal = localDBHelper.getAnimalById(mAnimalId.getText().toString().trim());
                        if (animal != null) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle(getString(R.string.error));
                            builder.setMessage(getString(R.string.error_duplicate_animal_id))
                                    .setCancelable(true)
                                    .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
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
                                    mCountry2.getSelectedItemPosition() == 0 ||
                                    mAnimalTypeRadioGroup.getCheckedRadioButtonId() == -1 ||
                                    mGenderRadioGroup.getCheckedRadioButtonId() == -1
                                    ){
                                toast(getString(R.string.fill_in_mandatory_fields));
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
                            //Toast toast = Toast.makeText(AnimalDetailActivity.this, getString(R.string.fill_in_caretaker_id), Toast.LENGTH_LONG);
                            //toast.show();
                            toast(getString(R.string.fill_in_caretaker_id));
                            break;
                        }
                        if (TextUtils.isEmpty(mAnimalId.getText().toString().trim()) ||
                                    TextUtils.isEmpty(mSupervisor.getText().toString().trim()) ||
                                    mCountry2.getSelectedItemPosition() == 0 ||
                                    mAnimalTypeRadioGroup.getCheckedRadioButtonId() == -1 ||
                                    mGenderRadioGroup.getCheckedRadioButtonId() == -1
                                    ) {
                            //makeToast();
                            toast(getString(R.string.fill_in_mandatory_fields));
                            break;
                        }
                        saveRecords();
                        //setResult(RESULT_OK);
                        //finish();
                        notifyAnimalRecordSaved();
                        break;
                }
                if (!(view==null))
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

                setTitle(getString(R.string.heading_animal_record));
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

                    //refreshLayout();
                    if (View.GONE == lle.getVisibility()) lleheader.callOnClick();
                    lle.requestFocus();
                    editButton.callOnClick();

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
        /*Should be on hold. not on selected
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

        //PROBLEM: when a tag is read at animaldetailactivity when it doesnt include writing, the extras are erased.
        //So the best case is animal still can be saved
        //but worse case is when the screen is rotated then the app will crash because
        //oncreate uses the value in the extras to prepare the data
        //is there a way that oncreate is not called?
        //SOLUTION: I set onconfig in manifest to bar from calling oncreate when orientation is changed
        if (!getScanOn())
            toast(getString(R.string.nfc_location_scan_error));

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
        setTitle(getString(R.string.heading_new_animal_registration));

        //animalRecordType = "N";
        //caretakerRecordType = "N";
        mode = "AR";

        if (byNFC) {
            mAnimalId.setText(animalID);
            mAnimalId.setEnabled(false);
        }
        else mAnimalId.setEnabled(true);


        if (!(userRole ==2)) {
            mCountry2.setSelection(Integer.valueOf(userCountry));
        }
        else mCountry2.setSelection(0);
        mSupervisor.setText(userName);

    }

    //12OCT2016 POPULATE FIELDS FOR EXISTING ANIMAL RECORD x1 CALL
    private void fillExistingAnimal(String selectedAnimalId){

        mode = "V";

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)));
        setTitle(getString(R.string.heading_animal_record));

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
        nfcScanSaveTimestamp = null;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (nfcScanEntryTimestamp != null)
            nfcScanEntryTimestamp = dateFormatter.format(cal.getTime());

        mAnimalId.setText(existingAnimal.getAnimalId());
        mSupervisor.setText(existingAnimal.getSupervisor());


        mAnimalTypeRadioGroup.clearCheck();
        int animalTypeIndex = Integer.parseInt(existingAnimal.getAnimalType());
        Log.d(TAG, "animalTypeIndex: "+animalTypeIndex);
        RadioButton radioButton = (RadioButton)mAnimalTypeRadioGroup.getChildAt(animalTypeIndex);
        mAnimalTypeRadioGroup.check(radioButton.getId());

        /*
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
        */

        mGenderRadioGroup.clearCheck();
        int genderIndex = Integer.parseInt(existingAnimal.getGender());
        Log.d(TAG, "genderIndex: "+genderIndex);
        RadioButton gRadioButton = (RadioButton)mGenderRadioGroup.getChildAt(genderIndex);
        mGenderRadioGroup.check(gRadioButton.getId());
        /*
        if ("Female".equals(existingAnimal.getGender())) {
            fRadioButton.setChecked(true);
            mRadioButton.setChecked(false);
        }
        else{
            mRadioButton.setChecked(true);
            fRadioButton.setChecked(false);
        }
        */

        if (existingAnimal.getDateOfBirth() != null)
            mDateOfBirth.setText(existingAnimal.getDateOfBirth());
        if (existingAnimal.getCountry() != null) {
            mCountry2.setSelection(Integer.parseInt(existingAnimal.getCountry()));
        }
        if (existingAnimal.getDatePurchased() != null)
            mDatePurchased.setText(existingAnimal.getDatePurchased());
        if (existingAnimal.getPurchasePrice() != null)
            mPurchasePrice.setText(existingAnimal.getPurchasePrice());
        if (existingAnimal.getPurchaseWeight() != null)
            mPurchaseWeight.setText(existingAnimal.getPurchaseWeight());
        if (existingAnimal.getPurchaseWeightUnit() != null) {
            mPurchaseWeightUnit.setSelection(Integer.parseInt(existingAnimal.getPurchaseWeightUnit()));}
        if (existingAnimal.getPurchaseHeight() != null)
            mPurchaseHeight.setText(existingAnimal.getPurchaseHeight());
        if (existingAnimal.getPurchaseHeightUnit() != null) {
            mPurchaseHeightUnit.setSelection(Integer.parseInt(existingAnimal.getPurchaseHeightUnit()));}
        if (existingAnimal.getDateDistributed()!= null)
            mDateDistributed.setText(existingAnimal.getDateDistributed());
        if (existingAnimal.getSalePrice() != null)
            mSalePrice.setText(existingAnimal.getSalePrice());
        if (existingAnimal.getDateSold()!= null)
            mDateSold.setText(existingAnimal.getDateSold());
        if (existingAnimal.getSaleWeight() != null)
            mSaleWeight.setText(existingAnimal.getSaleWeight());
        if (existingAnimal.getSaleWeightUnit() != null) {
            mSaleWeightUnit.setSelection(Integer.parseInt(existingAnimal.getSaleWeightUnit()));}
        if (existingAnimal.getSaleHeight() != null)
            mSaleHeight.setText(existingAnimal.getSaleHeight());
        if (existingAnimal.getSaleHeightUnit() != null) {
            mSaleHeightUnit.setSelection(Integer.parseInt(existingAnimal.getSaleHeightUnit()));}

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

        Log.d(TAG, "In refreshLayout, mode="+mode);
        switch (mode) {
            case "AR":
                linearLayout2.setVisibility(View.GONE);
                linearlayout3.setVisibility(View.GONE);
                linearlayout4.setVisibility(View.GONE);

                editButton.setVisibility(View.GONE);
                saveButton2.setVisibility(View.VISIBLE);

                if (userRole ==2) {
                    mCountry2.setEnabled(true);
                }
                else {
                    mCountry2.setEnabled(false);
                }

                saveButton2.setText(R.string.btn_register_animal);
                saveButton2.setPadding(8,8,8,8);
                saveButton2.setBackgroundColor(Color.parseColor("#FF8800"));

                break;
            case "N":
                linearLayout2.setVisibility(View.VISIBLE);
                linearlayout3.setVisibility(View.VISIBLE);
                linearlayout4.setVisibility(View.VISIBLE);

                editButton.setVisibility(View.GONE);
                editButton2.setVisibility(View.GONE);
                editButton3.setVisibility(View.GONE);
                addEventButton.setVisibility(View.VISIBLE);
                //addEventLink.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                saveButton2.setVisibility(View.VISIBLE);
                saveButton3.setVisibility(View.VISIBLE);
                saveButton4.setVisibility(View.VISIBLE);

                saveButton2.setText(getString(R.string.save));
                saveButton2.setBackgroundColor(Color.LTGRAY);

                break;

            case "UD":
                infoTextView.setText(R.string.update_record_not_synced);
                infoTextView.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                linearlayout3.setVisibility(View.VISIBLE);
                linearlayout4.setVisibility(View.VISIBLE);

                editButton.setVisibility(View.GONE);
                editButton2.setVisibility(View.GONE);
                editButton3.setVisibility(View.GONE);
                addEventButton.setVisibility(View.VISIBLE);
                //addEventLink.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                saveButton2.setVisibility(View.VISIBLE);
                saveButton3.setVisibility(View.VISIBLE);
                saveButton4.setVisibility(View.VISIBLE);

                saveButton2.setText(getString(R.string.save));
                saveButton2.setBackgroundColor(Color.LTGRAY);
                //saveButton2.setBackgroundResource(android.R.drawable.btn_default_small);

                //set mandatory fields also enabled//
                if (userRole ==2) {
                    mCountry2.setEnabled(true);
                }
                else{
                    mCountry2.setEnabled(false);
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
                mPurchaseWeight.setEnabled(true);
                mPurchaseWeightUnit.setEnabled(true);
                mPurchaseHeight.setEnabled(true);
                mPurchaseHeightUnit.setEnabled(true);
                mDateDistributed.setEnabled(true);
                mDateSold.setEnabled(true);
                mSalePrice.setEnabled(true);
                mSaleWeight.setEnabled(true);
                mSaleWeightUnit.setEnabled(true);
                mSaleHeight.setEnabled(true);
                mSaleHeightUnit.setEnabled(true);

                mDateOfBirth.getBackground().clearColorFilter();
                mDatePurchased.getBackground().clearColorFilter();
                mPurchasePrice.getBackground().clearColorFilter();
                mDateSold.getBackground().clearColorFilter();
                mPurchaseWeight.getBackground().clearColorFilter();
                mPurchaseHeight.getBackground().clearColorFilter();
                mDateDistributed.getBackground().clearColorFilter();
                mSalePrice.getBackground().clearColorFilter();
                mSaleWeight.getBackground().clearColorFilter();
                mSaleHeight.getBackground().clearColorFilter();

                mCaretakerId.setEnabled(true);
                mCaretakerName.setEnabled(true);
                mCaretakerTel.setEnabled(true);
                mCaretakerAddr1.setEnabled(true);
                mCaretakerAddr2.setEnabled(true);
                mCaretakerAddr3.setEnabled(true);

                /*
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
                */

                break;

            case "US":
                infoTextView.setText(R.string.update_record_synced);
                infoTextView.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                linearlayout3.setVisibility(View.VISIBLE);
                linearlayout4.setVisibility(View.VISIBLE);

                editButton.setVisibility(View.GONE);
                editButton2.setVisibility(View.GONE);
                editButton3.setVisibility(View.GONE);
                addEventButton.setVisibility(View.VISIBLE);
                //addEventLink.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                saveButton2.setVisibility(View.VISIBLE);
                saveButton3.setVisibility(View.VISIBLE);
                saveButton4.setVisibility(View.VISIBLE);

                saveButton2.setText(getString(R.string.save));
                saveButton2.setBackgroundColor(Color.LTGRAY);
                //saveButton2.setBackgroundResource(android.R.drawable.btn_default_small);

                mAnimalId.setEnabled(false);
                mCountry2.setEnabled(false);
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
                    dateOfBirthLabel.setTextColor(colorEmphasis);

                }
                if (originalServerAnimal.getDatePurchased() == null){
                    mDatePurchased.setEnabled(true);
                    datePurchasedLabel.setTextColor(colorEmphasis);
                }
                if (originalServerAnimal.getPurchasePrice() == null ||
                        "0".equals(originalServerAnimal.getPurchasePrice())){
                    mPurchasePrice.setEnabled(true);
                    mPurchasePrice.getBackground().clearColorFilter();
                    purchasePriceLabel.setTextColor(colorEmphasis);
                }
                if (originalServerAnimal.getPurchaseWeight() == null){
                    mPurchaseWeight.setEnabled(true);
                    purchaseWeightLabel.setTextColor(colorEmphasis);
                    mPurchaseWeightUnit.setEnabled(true);
                }
                if (originalServerAnimal.getPurchaseHeight() == null){
                    mPurchaseHeight.setEnabled(true);
                    purchaseHeightLabel.setTextColor(colorEmphasis);
                    mPurchaseHeightUnit.setEnabled(true);
                }
                if (originalServerAnimal.getSaleWeight() == null){
                    mSaleWeight.setEnabled(true);
                    saleWeightLabel.setTextColor(colorEmphasis);
                    mSaleWeightUnit.setEnabled(true);
                }
                if (originalServerAnimal.getSaleHeight() == null){
                    mSaleHeight.setEnabled(true);
                    saleHeightLabel.setTextColor(colorEmphasis);
                    mSaleHeightUnit.setEnabled(true);
                }
                if (originalServerAnimal.getDateDistributed() == null){
                    mDateDistributed.setEnabled(true);
                    dateDistributedLabel.setTextColor(colorEmphasis);
                }
                if (originalServerAnimal.getDateSold() == null){
                    mDateSold.setEnabled(true);
                    dateSoldLabel.setTextColor(colorEmphasis);
                }
                if (originalServerAnimal.getSalePrice() == null ||
                        "0".equals(originalServerAnimal.getSalePrice())){
                    mSalePrice.setEnabled(true);
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

                    /*
                    mCaretakerId.setBackground(enabledEdittext);
                    mCaretakerName.setBackground(enabledEdittext);
                    mCaretakerTel.setBackground(enabledEdittext);
                    mCaretakerAddr1.setBackground(enabledEdittext);
                    mCaretakerAddr2.setBackground(enabledEdittext);
                    mCaretakerAddr3.setBackground(enabledEdittext);
                    */
                }
                else {

                    if (originalServerCaretaker.getCaretakerId() == null) {
                        mCaretakerId.setEnabled(true);
                        //mCaretakerId.setBackground(enabledEdittext);
                        caretakerIdLabel.setTextColor(colorEmphasis);
                    }
                    if (originalServerCaretaker.getCaretakerName() == null) {
                        mCaretakerName.setEnabled(true);
                        caretakerNameLabel.setTextColor(colorEmphasis);
                    }
                    if (originalServerCaretaker.getCaretakerTel() == null) {
                        mCaretakerTel.setEnabled(true);
                        caretakerTelephoneLabel.setTextColor(colorEmphasis);
                    }
                    if (originalServerCaretaker.getCaretakerAddr1() == null) {
                        mCaretakerAddr1.setEnabled(true);
                        caretakerAddrLine1Label.setTextColor(colorEmphasis);
                    }
                    if (originalServerCaretaker.getCaretakerAddr2() == null) {
                        mCaretakerAddr2.setEnabled(true);
                        caretakerAddrLine2Label.setTextColor(colorEmphasis);
                    }
                    if (originalServerCaretaker.getCaretakerAddr3() == null) {
                        mCaretakerAddr3.setEnabled(true);
                        caretakerAddrLine3Label.setTextColor(colorEmphasis);
                    }
                }
                break;

            case "V":
                linearLayout2.setVisibility(View.VISIBLE);
                linearlayout3.setVisibility(View.VISIBLE);
                linearlayout4.setVisibility(View.VISIBLE);

                switch (animalRecordType) {
                    case "D":
                        infoTextView.setText(R.string.view_record_not_synced);
                        infoTextView.setVisibility(View.VISIBLE);
                        break;
                    case "US":
                        infoTextView.setText(R.string.info_record_synced);
                        infoTextView.setVisibility(View.VISIBLE);
                        break;
                    case "S":
                        if (recordEditable()){
                            infoTextView.setText(R.string.info_record_synced);
                            infoTextView.setVisibility(View.VISIBLE);
                        }
                        else {
                            infoTextView.setText(R.string.info_record_fully_filled);
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
                purchaseWeightLabel.setTextColor(colorPrimaryDark);
                purchaseHeightLabel.setTextColor(colorPrimaryDark);
                dateSoldLabel.setTextColor(colorPrimaryDark);
                salePriceLabel.setTextColor(colorPrimaryDark);
                saleWeightLabel.setTextColor(colorPrimaryDark);
                saleHeightLabel.setTextColor(colorPrimaryDark);

                caretakerIdLabel.setTextColor(colorPrimaryDark);
                caretakerNameLabel.setTextColor(colorPrimaryDark);
                caretakerTelephoneLabel.setTextColor(colorPrimaryDark);
                caretakerAddrLine1Label.setTextColor(colorPrimaryDark);
                caretakerAddrLine2Label.setTextColor(colorPrimaryDark);
                caretakerAddrLine3Label.setTextColor(colorPrimaryDark);

                mAnimalId.setEnabled(false);
                mCountry2.setEnabled(false);
                mSupervisor.setEnabled(false);

                cowRadioButton.setEnabled(false);
                goatRadioButton.setEnabled(false);
                pigRadioButton.setEnabled(false);
                mRadioButton.setEnabled(false);
                fRadioButton.setEnabled(false);

                mDateOfBirth.setEnabled(false);
                mDatePurchased.setEnabled(false);
                mPurchasePrice.setEnabled(false);
                mPurchaseWeight.setEnabled(false);
                mPurchaseWeightUnit.setEnabled(false);
                mPurchaseHeight.setEnabled(false);
                mPurchaseHeightUnit.setEnabled(false);
                mDateDistributed.setEnabled(false);
                mDateSold.setEnabled(false);
                mSalePrice.setEnabled(false);
                mSaleWeight.setEnabled(false);
                mSaleWeightUnit.setEnabled(false);
                mSaleHeight.setEnabled(false);
                mSaleHeightUnit.setEnabled(false);


                mCaretakerId.setEnabled(false);
                mCaretakerName.setEnabled(false);
                mCaretakerTel.setEnabled(false);
                mCaretakerAddr1.setEnabled(false);
                mCaretakerAddr2.setEnabled(false);
                mCaretakerAddr3.setEnabled(false);

                //mAnimalId.setBackground(disabledEditext);
                //mSupervisor.setBackground(disabledEditext);

                /*
                mDateOfBirth.getBackground().setColorFilter(Color.parseColor("#00FFFFFF"), PorterDuff.Mode.SRC_IN);
                mDatePurchased.getBackground().setColorFilter(Color.parseColor("#00FFFFFF"), PorterDuff.Mode.SRC_IN);
                mPurchasePrice.getBackground().setColorFilter(Color.parseColor("#00FFFFFF"), PorterDuff.Mode.SRC_IN);
                mDateDistributed.getBackground().setColorFilter(Color.parseColor("#00FFFFFF"), PorterDuff.Mode.SRC_IN);
                mDateSold.getBackground().setColorFilter(Color.parseColor("#00FFFFFF"), PorterDuff.Mode.SRC_IN);
                mSalePrice.getBackground().setColorFilter(Color.parseColor("#00FFFFFF"), PorterDuff.Mode.SRC_IN);
                */

                /*
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
                */

                break;

        }

    }

    private void saveAnimal(){

        Log.d(TAG, "ENTERING SAVE ANIMAL");
        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(this);

        int selectedAnimalTypeId = mAnimalTypeRadioGroup.getCheckedRadioButtonId();
        mAnimalTypeSelection = (RadioButton) findViewById(selectedAnimalTypeId);
        int animalTypeIndex = mAnimalTypeRadioGroup.indexOfChild(mAnimalTypeSelection);

        int selectedId = mGenderRadioGroup.getCheckedRadioButtonId();
        mGenderRadioButton = (RadioButton) findViewById(selectedId);
        int genderIndex = mGenderRadioGroup.indexOfChild(mGenderRadioButton);

        if (existingAnimal==null) {

            animalRecordType = "D";
        }
        if (existingCaretaker ==null) {
            if (getText(mCaretakerId) != null)
                caretakerUid = RandomStringUtils.randomAlphanumeric(10);
            else caretakerUid = null;
        }
        else caretakerUid = existingAnimal.getCaretakerUid();

        Animal newAnimal = new Animal(
                getText(mAnimalId),
                getText(mSupervisor),
                //getText(mAnimalTypeSelection),
                String.valueOf(animalTypeIndex),
                //getText(mGenderRadioButton),
                String.valueOf(genderIndex),
                getText(mDateOfBirth),
                String.valueOf(mCountry2.getSelectedItemPosition()),
                getText(mDatePurchased),
                getText(mPurchasePrice),
                getText(mPurchaseWeight),
                getUnit(mPurchaseWeight, mPurchaseWeightUnit),
                //String.valueOf(mPurchaseWeightUnit.getSelectedItemPosition()),
                getText(mPurchaseHeight),
                getUnit(mPurchaseHeight, mPurchaseHeightUnit),
                //String.valueOf(mPurchaseHeightUnit.getSelectedItemPosition()),
                getText(mDateDistributed),
                getText(mDateSold),
                getText(mSalePrice),
                getText(mSaleWeight),
                getUnit(mSaleWeight, mSaleWeightUnit),
                //String.valueOf(mSaleWeightUnit.getSelectedItemPosition()),
                getText(mSaleHeight),
                getUnit(mSaleHeight, mSaleHeightUnit),
                //String.valueOf(mSaleHeightUnit.getSelectedItemPosition()),
                caretakerUid,
                animalRecordType,
                nfcScanEntryTimestamp,
                nfcScanSaveTimestamp
        );
        Log.d(TAG, newAnimal.toString());

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
                caretakerRecordType,
                nfcScanEntryTimestamp,
                nfcScanSaveTimestamp
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

            events.get(i).setNfcScanEntryTimestamp(nfcScanEntryTimestamp);
            events.get(i).setNfcScanSaveTimestamp(nfcScanSaveTimestamp);

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

    /* REDUNDANT
    private void makeToast() {
        Toast.makeText(AnimalDetailActivity.this, R.string.mandatory_fields_not_filled_error , Toast.LENGTH_LONG).show();
    }
    */

    private boolean isCaretakerIdEmpty(){

        if(TextUtils.isEmpty(mCaretakerId.getText().toString().trim()) && (
                (!TextUtils.isEmpty(mCaretakerName.getText().toString().trim())) ||
                (!TextUtils.isEmpty(mCaretakerTel.getText().toString().trim())) ||
                (!TextUtils.isEmpty(mCaretakerAddr1.getText().toString().trim())) ||
                (!TextUtils.isEmpty(mCaretakerAddr2.getText().toString().trim())) ||
                (!TextUtils.isEmpty(mCaretakerAddr3.getText().toString().trim()))
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
            if (existingCaretaker==null)
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
        setScanOn(true);

        dialog = new ProgressDialog(AnimalDetailActivity.this);
        SpannableString ss1=  new SpannableString(getString(R.string.alert_rescan_tag));
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

    private String getUnit(TextView textView, Spinner spinner){
        if (textView == null) return null;
        if (!TextUtils.isEmpty(textView.getText().toString().trim()))
            return String.valueOf(spinner.getSelectedItemPosition());
        else return null;
    }

    /*
    private String getText(Spinner spinner){
        if (spinner == null) return null;
        if (!spinner.getSelectedItemPosition()
            return spinner.getText().toString().trim();
        else return null;
    }
    */

    private boolean isExistingAnimal(String animalId){
        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(AnimalDetailActivity.this);
        if (localDBHelper.getAnimalById(animalId)!= null)
            return true;
        else return false;
    }

    private void notifyAnimalRecordSaved(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AnimalDetailActivity.this);
        builder.setMessage(getString(R.string.alert_record_saved))
                .setCancelable(false)
                .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
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
            countryRecord.setData(String.valueOf(mCountry2.getSelectedItemPosition()).getBytes("UTF-8"));
            message.add(countryRecord);

            MimeRecord versionRecord = new MimeRecord();
            versionRecord.setMimeType("cci/version");
            versionRecord.setData("1.1".getBytes("UTF-8"));
            message.add(versionRecord);

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
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        nfcScanSaveTimestamp=dateFormatter.format(cal.getTime());
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
