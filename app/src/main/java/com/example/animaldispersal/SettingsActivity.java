package com.example.animaldispersal;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.davaodemo.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Configuration config = new Configuration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
                        config.locale = Locale.ENGLISH;
                        break;
                    case 1:
                        config.locale = new Locale("bn");
                        break;
                    case 2:
                        config.locale = new Locale("ur");
                        break;
                    default:
                        config.locale = Locale.ENGLISH;
                        break;
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }
    public void onClick(View v) {

        Locale.setDefault(config.locale);
        getResources().updateConfiguration(config,  getResources().getDisplayMetrics());
        AppController.getInstance().setLocale(config.locale);
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }
    private String[] languages = { "English", "Bengali", "Urdu" };
}
