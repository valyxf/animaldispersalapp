package com.example.animaldispersal.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.davaodemo.R;
import com.example.animaldispersal.dataobject.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 22/5/2016.
 */
public class EventAdapter extends ArrayAdapter<Event> {

    private static final String TAG = EventAdapter.class.getName();

    public EventAdapter(Context context, List<Event> eventArrayList) {
        super(context, 0, eventArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Event event = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }

        //View view = super.getView(position, convertView, parent);

        //set alternating color
        if (position % 2 == 1) {
            convertView.setBackgroundColor(ResourcesCompat.getColor(parent.getResources(),R.color.altrow,null));
        }
        else{
            convertView.setBackgroundColor(ResourcesCompat.getColor(parent.getResources(),R.color.background_material_light,null));
        }

        //fill rows
        TextView eventType = (TextView) convertView.findViewById(R.id.event_type);
        TextView eventDate = (TextView) convertView.findViewById(R.id.event_date);
        TextView eventTime = (TextView) convertView.findViewById(R.id.event_time);
        TextView eventRemarks = (TextView) convertView.findViewById(R.id.event_remarks);


        final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
        final String NEW_FORMAT = "yyyy-MM-dd";
        String oldDateString = event.getEventDate();
        String newDateString;

        try {

            SimpleDateFormat formatter = new SimpleDateFormat(OLD_FORMAT);
            Date d = formatter.parse(oldDateString);
            ((SimpleDateFormat) formatter).applyPattern(NEW_FORMAT);
            newDateString = formatter.format(d);
        }catch (Exception e){
            newDateString =  event.getEventDate();
        }

        eventType.setText(event.getEventType());
        eventDate.setText(newDateString);

        String newTimeString;
        try {

            SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
            Date d = timeFormat.parse(event.getEventTime());
            ((SimpleDateFormat) timeFormat).applyPattern("h:mm aa");
            newTimeString = timeFormat.format(d);
        }catch (Exception e){
            newTimeString =  event.getEventTime();
        }

        eventTime.setText(newTimeString);
        eventRemarks.setText(event.getEventRemarks());
        // Return the completed view to render on screen
        return convertView;
    }
}
