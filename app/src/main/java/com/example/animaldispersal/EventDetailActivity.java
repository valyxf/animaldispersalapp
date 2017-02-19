package com.example.animaldispersal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davaodemo.R;
import com.example.animaldispersal.dataobject.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventDetailActivity extends AppCompatActivity {

    private static final String TAG = EventDetailActivity.class.getName();

    private EditText mEventType;
    private EditText mEventDateTime;
    private EditText mEventRemarks;

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
        mEventDateTime = (EditText)findViewById(R.id.event_date_time);
        mEventRemarks = (EditText)findViewById(R.id.event_remarks);

        mDoneButton = (Button)findViewById(R.id.button8);
        mCancelButton = (Button)findViewById(R.id.button9);

        //initialise the variables
        Bundle extras = getIntent().getExtras();
        if (extras != null){

            selectedEvent = (Event)extras.getParcelable("SELECTED_EVENT");
            eventSno = extras.getString("EVENT_ID");
            //editMode = extras.getBoolean("EDIT_MODE");
            mode = extras.getString("MODE");

            //EXISTING EVENT
            if (selectedEvent!= null){
                setTitle("Event Details");
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
                setTitle("New Event Record");
                eventRecordType = "N";
            }
        }
        //THIS LINE SHOULD NEVER BE REACHED. ENTRY INTO EventDetailActivity ALWAYS HAS SOME DATA
        else eventRecordType = "N";


        //initialise the buttons
        final Calendar cal = Calendar.getInstance();

        mEventDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), datePickerListener,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE));
                DatePicker picker = dialog.getDatePicker();
                picker.setId(mEventDateTime.getId());
                dialog.show();
            }
        });

        //Exit 1of1 from AnimalDetailActivity
        mDoneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (TextUtils.isEmpty(mEventType.getText().toString()) ||
                        TextUtils.isEmpty(mEventDateTime.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Event Type, Event Date must be filled", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Intent resultIntent = new Intent();
                    Event newEvent = new Event(
                            eventSno,
                            getText(mEventType),
                            getText(mEventDateTime),
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
                        newEvent.setEventDateTime(mEventDateTime.getText().toString());
                        newEvent.setEventRemarks(mEventRemarks.getText().toString());

                        if ("N".equals(eventRecordType)) newEvent.setRecordType("D");
                        else newEvent.setRecordType(eventRecordType);


                        resultIntent.putExtra("EVENT", newEvent);
                    }*/
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

    private void fillData(Event event){

        final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
        final String NEW_FORMAT = "yyyy-MM-dd";
        String oldDateString = event.getEventDateTime();
        String newDateString;

        try {

            SimpleDateFormat formatter = new SimpleDateFormat(OLD_FORMAT);
            Date d = formatter.parse(oldDateString);
            ((SimpleDateFormat) formatter).applyPattern(NEW_FORMAT);
            newDateString = formatter.format(d);
        }catch (Exception e){
            newDateString =  event.getEventDateTime();
        }

        mEventType.setText(event.getEventType());
        mEventDateTime.setText(newDateString);
        mEventRemarks.setText(event.getEventRemarks());

        refreshButtons();
    }

    private void refreshButtons(){
        //if ("S".equals(eventRecordType)|| !editMode) {
        if ("V".equals(mode)) {
            mEventType.setEnabled(false);
            mEventDateTime.setEnabled(false);
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
            if ((selectedEvent.getEventDateTime() == null) && (mEventDateTime.getText().toString().trim() != null))
                return true;
            if ((selectedEvent.getEventDateTime() != null) &&
                    (!(selectedEvent.getEventDateTime().equals(mEventDateTime.getText().toString().trim()))))
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

    private String getText(RadioButton radioButton){
        if (radioButton == null) return null;
        if (!TextUtils.isEmpty(radioButton.getText().toString().trim()))
            return radioButton.getText().toString().trim();
        else return null;
    }

}
