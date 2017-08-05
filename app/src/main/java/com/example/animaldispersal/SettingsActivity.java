package com.example.animaldispersal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.davaodemo.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    //VY
    private static final String TAG = SettingsActivity.class.getName();
    private Locale newLocale;
    private String[] languages;
    private RadioGroup languagesRadioGroup;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.d(TAG, "SettingsActivity onCreate" );

        String[] languages = getResources().getStringArray(R.array.languages);

        //VY 20170507 redesign language to radiogroup
        //Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        //spinner.setPrompt("Select Language");

        Locale currentLocale = AppController.getInstance().getLocale();
        int intLocale;
        switch (currentLocale.toString()){
            case "en":
                intLocale=0;
                break;
            case "bn":
                intLocale=1;
                break;
            case "ur":
                intLocale=2;
                break;
            default: intLocale=0;
        }
        Log.d(TAG, "intLocale"+intLocale);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_enabled}},
                new int[] {ContextCompat.getColor(this, R.color.colorPrimary)}
        );

        languagesRadioGroup = (RadioGroup) findViewById(R.id.language_radiogroup);
        for (int i = 0; i < languages.length; i++) {
            RadioButton languageRadioButton = new RadioButton(this);
            languageRadioButton.setId(View.generateViewId());
            languageRadioButton.setText(languages[i]);
            languagesRadioGroup.addView(languageRadioButton);
        }

        RadioButton radioButton = (RadioButton)languagesRadioGroup.getChildAt(intLocale);
        languagesRadioGroup.check(radioButton.getId());


        /* VY 20170507 redesign language as radio buttons
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                switch (arg2) {
                    case 0:
                        //config.locale = Locale.ENGLISH;
                        newLocale = Locale.ENGLISH;
                        break;
                    case 1:
                        //config.locale = new Locale("bn");
                        newLocale = new Locale("bn");
                        break;
                    case 2:
                        //config.locale = new Locale("ur");
                        newLocale = new Locale("ur");
                        break;
                    default:
                        //config.locale = Locale.ENGLISH;
                        newLocale = Locale.ENGLISH;
                        break;
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });*/
    }
    public void onClick(View v) {
        /*VY
        Locale.setDefault(newLocale);
        Configuration config = new Configuration();
        config.locale = newLocale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        */
        //Locale.setDefault(config.locale);

        int selectedLanguage = languagesRadioGroup.getCheckedRadioButtonId();
        RadioButton languageRadioButtonSelection = (RadioButton) findViewById(selectedLanguage);
        int animalTypeIndex = languagesRadioGroup.indexOfChild(languageRadioButtonSelection);
        switch (animalTypeIndex) {
            case 0:
                newLocale = Locale.ENGLISH;
                break;
            case 1:
                newLocale = new Locale("bn");
                break;
            case 2:
                newLocale = new Locale("ur");
                break;
            default:
                newLocale = Locale.ENGLISH;
                break;
        }
        Locale.setDefault(newLocale);
        //Configuration config = new Configuration();
        Configuration existingConfig = getBaseContext().getResources().getConfiguration();
        Configuration config = new Configuration(existingConfig);
        //existingConfig.locale = newLocale;
        config.setLocale(newLocale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        AppController.getInstance().setLocale(newLocale);

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();

    }

}
