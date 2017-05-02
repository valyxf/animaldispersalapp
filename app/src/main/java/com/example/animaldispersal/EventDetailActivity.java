package com.example.animaldispersal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.davaodemo.R;
import com.example.animaldispersal.dataobject.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventDetailActivity extends AppCompatActivity {

    private static final String TAG = EventDetailActivity.class.getName();

    private EditText mEventType;
    private EditText mEventDate;
    private EditText mEventRemarks;
    private EditText mEventTime;

    private Button mDoneButton;
    private Button mCancelButton;

    private String eventRecordType;
    private Event selectedEvent;
    private Boolean editMode;
    private String mode;
    private boolean eventChanged;
    private String eventSno;
    //private ViewUtils vu;
    private String animalId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        mEventType = (EditText)findViewById(R.id.event_type);
        mEventDate = (EditText)findViewById(R.id.event_date);
        mEventTime = (EditText)findViewById(R.id.event_time);
        mEventRemarks = (EditText)findViewById(R.id.event_remarks);

        mDoneButton = (Button)findViewById(R.id.button8);
        mCancelButton = (Button)findViewById(R.id.button9);


        //initialise the variables
        Bundle extras = getIntent().getExtras();
        if (extras != null){

            selectedEvent = (Event)extras.getParcelable("SELECTED_EVENT");
            eventSno = extras.getString("EVENT_ID");
            //editMode = extras.getBoolean("EDIT_MODE");

            //VY 12MAR17 not going to use mode to determine layout of items anymore. Only going to use recordType.
            //S - disabled, not S - enabled
            //mode = extras.getString("MODE");

            //EXISTING EVENT
            if (selectedEvent!= null){
                setTitle(getString(R.string.heading_event_details));
                if ("S".equals(selectedEvent.getRecordType()) ||
                        "US".equals(selectedEvent.getRecordType()))
                    eventRecordType = "US";
                if ("D".equals(selectedEvent.getRecordType()) ||
                        "UD".equals(selectedEvent.getRecordType()))
                    eventRecordType = "UD";
                if ("N".equals(selectedEvent.getRecordType()))
                    eventRecordType = "N";
                fillData(selectedEvent);
            }
            //NEW EVENT
            else{
                setTitle(getString(R.string.heading_new_event_record));
                eventRecordType = "N";
            }
        }
        //THIS LINE SHOULD NEVER BE REACHED. ENTRY INTO EventDetailActivity ALWAYS HAS SOME DATA
        else eventRecordType = "N";


        //initialise the buttons
        final Calendar cal = Calendar.getInstance();

        mEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), datePickerListener,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
                DatePicker picker = dialog.getDatePicker();
                picker.setId(mEventDate.getId());
                dialog.show();
            }
        });

        mEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog
                        = new TimePickerDialog(v.getContext(), AlertDialog.THEME_HOLO_LIGHT,
                        timePickerListener,
                        cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),false);

                dialog.show();
            }
        });

        //Exit 1of1 from AnimalDetailActivity
        mDoneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (TextUtils.isEmpty(mEventType.getText().toString()) ||
                        TextUtils.isEmpty(mEventTime.getText().toString()) ||
                        TextUtils.isEmpty(mEventDate.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Event Type, Date and Time must be filled", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Intent resultIntent = new Intent();
                    Event newEvent = new Event(
                            eventSno,
                            getText(mEventType),
                            getText(mEventDate),
                            getText(mEventTime),
                            getText(mEventRemarks)
                    );
                    if (!newEvent.equals(selectedEvent)){
                        if (selectedEvent != null) {
                            newEvent.setRecordType(eventRecordType);
                            newEvent.setAnimalId(selectedEvent.getAnimalId());
                            newEvent.setEventUpdated(true);
                            resultIntent.putExtra("EVENT_CHANGED", true);
                            resultIntent.putExtra("DELETED_EVENT", selectedEvent);
                        }
                        Log.d(TAG, "newEvent" +newEvent);
                        resultIntent.putExtra("EVENT", newEvent);
                        setResult(Activity.RESULT_OK, resultIntent);
                    }
                    else setResult(Activity.RESULT_CANCELED);

                    /*
                    if (eventChanged || "N".equals(eventRecordType)) {
                        Event newEvent = new Event();
                        newEvent.setEventId(eventSno);
                        newEvent.setEventType(mEventType.getText().toString());
                        newEvent.setEventDateTime(mEventDate.getText().toString());
                        newEvent.setEventRemarks(mEventRemarks.getText().toString());

                        if ("N".equals(eventRecordType)) newEvent.setRecordType("D");
                        else newEvent.setRecordType(eventRecordType);


                        resultIntent.putExtra("EVENT", newEvent);
                    }*/
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mDoneButton.getWindowToken(), 0);

                    finish();
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        //Entry 1of1 from AnimalDetailActivity


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
                Intent i=new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
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


    private TimePickerDialog.OnTimeSetListener timePickerListener
            = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {

            final String OLD_FORMAT = "H:mm";
            final String NEW_FORMAT = "HH:mm:ss";
            String newTimeString;
            String timeString = String.valueOf(selectedHour) + ":"+
                    String.valueOf(selectedMinute) +":00";

            try {
                SimpleDateFormat formatter = new SimpleDateFormat(OLD_FORMAT);
                Date d = formatter.parse(timeString);
                ((SimpleDateFormat) formatter).applyPattern(NEW_FORMAT);
                newTimeString = formatter.format(d);
            }catch (Exception e){
                newTimeString =  timeString;
            }

            mEventTime.setText(newTimeString);
        }
    };

    private void fillData(Event event){

        /*
        final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
        final String NEW_FORMAT = "yyyy-MM-dd";
        String oldDateString = event.getEventDate();
        String newDateString;


        try {

            SimpleDateFormat dateFormatter = new SimpleDateFormat(OLD_FORMAT);
            Date d = dateFormatter.parse(oldDateString);
            ((SimpleDateFormat) dateFormatter).applyPattern(NEW_FORMAT);
            newDateString = dateFormatter.format(d);
        }catch (Exception e){
            newDateString =  event.getEventDate();
        }

        */


        mEventType.setText(event.getEventType());
        mEventDate.setText(event.getEventDate());
        mEventTime.setText(event.getEventTime());
        mEventRemarks.setText(event.getEventRemarks());

        refreshButtons();
    }

    private void refreshButtons(){
        //if ("S".equals(eventRecordType)|| !editMode) {

        //VY 12MAR17 not going to use mode to determine layout of items anymore. Only going to use recordType.
        //S - disabled, not S - enabled
        //if ("V".equals(mode)) {
        if ("S".equals(eventRecordType)){
            mEventType.setEnabled(false);
            mEventDate.setEnabled(false);
            mEventTime.setEnabled(false);
            mEventRemarks.setEnabled(false);
            mDoneButton.setEnabled(false);
    }
        else
            mDoneButton.setEnabled(true);
    }



    private boolean onEventChange(){
        if (selectedEvent!=null) {
            if ((selectedEvent.getEventType() == null) && (mEventType.getText().toString().trim() != null))
                return true;
            if ((selectedEvent.getEventType() != null) &&
                    (!(selectedEvent.getEventType().equals(mEventType.getText().toString().trim()))))
                return true;
            if ((selectedEvent.getEventDate() == null) && (mEventDate.getText().toString().trim() != null))
                return true;
            if ((selectedEvent.getEventDate() != null) &&
                    (!(selectedEvent.getEventDate().equals(mEventDate.getText().toString().trim()))))
                return true;
            if ((selectedEvent.getEventRemarks() == null) && (mEventRemarks.getText().toString().trim() != null))
                return true;
            if ((selectedEvent.getEventRemarks() != null) &&
                    (!(selectedEvent.getEventRemarks().equals(mEventRemarks.getText().toString().trim()))))
                return true;
            return false;
        }
        return false;
    }


    private String getText(TextView textView){
        if (textView == null) return null;
        if (!TextUtils.isEmpty(textView.getText().toString().trim()))
            return textView.getText().toString().trim();
        else return null;
    }

}
