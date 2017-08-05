package com.example.animaldispersal.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.example.davaodemo.R;

/**
 * Created by user on 15/6/2016.
 */
public class AnimalAdapter extends SimpleCursorAdapter {
    public AnimalAdapter(Context context, int layout, Cursor c,
                         String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get reference to the row
        View view = super.getView(position, convertView, parent);
        //check for odd or even to set alternate colors to the row background
        if (position % 2 == 1) {
            view.setBackgroundColor(ResourcesCompat.getColor(parent.getResources(), R.color.altrow,null));
        }
        else{
            view.setBackgroundColor(ResourcesCompat.getColor(parent.getResources(), R.color.background_material_light ,null));
        }
        return view;
    }
}
