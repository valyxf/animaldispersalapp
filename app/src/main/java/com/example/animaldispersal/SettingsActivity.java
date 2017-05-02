package com.example.animaldispersal;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.davaodemo.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    //VY
    private static final String TAG = SettingsActivity.class.getName();
    private Locale newLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.d(TAG, "SettingsActivity onCreate" );

        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setPrompt("Select Language");

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
        });
    }
    public void onClick(View v) {
        /*VY
        Locale.setDefault(newLocale);
        Configuration config = new Configuration();
        config.locale = newLocale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        */
        //Locale.setDefault(config.locale);
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
    private String[] languages = { "English", "Bengali", "Urdu" };
}
