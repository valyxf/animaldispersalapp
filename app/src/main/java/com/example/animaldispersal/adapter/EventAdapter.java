package com.example.animaldispersal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
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
        TextView eventDateTime = (TextView) convertView.findViewById(R.id.event_date_time);
        TextView eventRemarks = (TextView) convertView.findViewById(R.id.event_remarks);

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

        eventType.setText(event.getEventType());
        eventDateTime.setText(newDateString);
        eventRemarks.setText(event.getEventRemarks());
        // Return the completed view to render on screen
        return convertView;
    }
}
