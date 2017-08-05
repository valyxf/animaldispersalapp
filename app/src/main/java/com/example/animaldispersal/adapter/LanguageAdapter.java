package com.example.animaldispersal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import com.example.davaodemo.R;

import java.util.List;

/**
 * Created by user on 5/5/2017.
 */
public class LanguageAdapter extends ArrayAdapter<String> {
    private static final String TAG = LanguageAdapter.class.getName();

    public LanguageAdapter(Context context, List<String> languageList) {
        super(context, 0, languageList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        String language = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_language, parent, false);
        }

        //fill rows
        RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.language_radiobutton);
        radioButton.setText(language);

        return convertView;
    }

}