package com.example.animaldispersal.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by user on 12/11/2016.
 */
public class ViewUtils{

    public ViewUtils(){}


    public String getText(Editable editable){
        if (editable == null) return null;
        if (!TextUtils.isEmpty(editable.toString().trim()))
            return editable.toString().trim();
        else return null;
    }

    public String getText(TextView textView){
        if (textView == null) return null;
        if (!TextUtils.isEmpty(textView.getText().toString().trim()))
            return textView.getText().toString().trim();
        else return null;
    }

    public String getText(RadioButton radioButton){
        if (radioButton == null) return null;
        if (!TextUtils.isEmpty(radioButton.getText().toString().trim()))
            return radioButton.getText().toString().trim();
        else return null;
    }


}
