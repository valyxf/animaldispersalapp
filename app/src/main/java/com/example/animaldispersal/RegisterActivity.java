package com.example.animaldispersal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animaldispersal.dataobject.SystemProperty;
import com.example.animaldispersal.http.SyncHelper;
import com.example.animaldispersal.localdb.LocalDBHelper;
import com.example.davaodemo.R;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getName();

    private Button saveButton;
    private Spinner mCountry;
    private EditText mUsername;
    private Spinner mRole;
    private EditText mKey;
    private TextView mCountryTextView;
    private TextView mKeyTextView;

    private String country;
    private String username;
    private int role;

    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Context mContext = this.getApplicationContext();
        mPrefs = mContext.getSharedPreferences("animalDispersalPrefs", 0);

        mCountryTextView = (TextView) findViewById(R.id.countryTextView);
        mKeyTextView = (TextView) findViewById(R.id.keyTextView);
        mUsername = (EditText) findViewById(R.id.username);
        mKey = (EditText) findViewById(R.id.key);
        mCountry = (Spinner) findViewById(R.id.country1);
        mRole = (Spinner) findViewById(R.id.role);
        mRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2==0) {
                    mKey.setVisibility(View.INVISIBLE);
                    mKeyTextView.setVisibility(View.INVISIBLE);
                }
                else{
                    mKey.setVisibility(View.VISIBLE);
                    mKeyTextView.setVisibility(View.VISIBLE);
                }


                if (arg2==2) {
                    mCountry.setVisibility(View.INVISIBLE);
                    mCountryTextView.setVisibility(View.INVISIBLE);
                    mCountry.setSelection(0);
                }
                else{
                    mCountry.setVisibility(View.VISIBLE);
                    mCountryTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        saveButton = (Button) findViewById(R.id.savebutton2);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean firstRun;

                if (!isFieldsValid()){
                    return;
                }


                if (!(mRole.getSelectedItemPosition()==0)){
                    if (!checkKeySuccess()){
                        toast(getString(R.string.incorrect_key));
                        firstRun = true;
                        return;
                    }
                }

                savePreferences();
                //firstRun = false;
                AppController.getInstance().setRunned();
                //Intent resultIntent = new Intent();
                //resultIntent.putExtra("FIRST_RUN",firstRun);
                //setResult(Activity.RESULT_OK, resultIntent);
                //finish();
                notifyUserSaved();


            }
        });

        if (!initialise()) {
            return;
        }
    }

    private Boolean isOnline(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    private void savePreferences(){
        SharedPreferences.Editor edit = mPrefs.edit();

        username = mUsername.getText().toString().trim();
        role = mRole.getSelectedItemPosition();

        if (role==2)
            country = null;
        else country = String.valueOf(mCountry.getSelectedItemPosition());

        edit.putInt("role",role);
        edit.putString("country", country);
        edit.putString("username", username);

        edit.commit();
        //finish();
    }

    private void notifyNoInternet(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle(getString(R.string.heading_no_internet));
        builder.setMessage(getString(R.string.alert_internet_connection_for_registration))
                .setCancelable(false)
                .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean initialise(){

        if (!(isOnline())){
            notifyNoInternet();
            return false;
        } else {
            Log.d(TAG, "Download keys enter");
            downloadKeys();
            Log.d(TAG, "Download keys complete");
            return true;
        }
    }

    private void downloadKeys() {
        SyncHelper syncHelper = new SyncHelper(RegisterActivity.this);
        syncHelper.KeysDownload();
    }

    private String getKey(String role){

        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(RegisterActivity.this);
        ArrayList<SystemProperty> list =  localDBHelper.getSystemPropertyByKey(role);
        SystemProperty sysprop = list.get(0);
        return sysprop.getValue();
    }

    private boolean isFieldsValid(){
        if (mCountry.getSelectedItemId() == AdapterView.INVALID_ROW_ID ||
                (TextUtils.isEmpty(mUsername.getText().toString().trim())) ||
                mRole.getSelectedItemId()  == AdapterView.INVALID_ROW_ID ||
                (mRole.getSelectedItemId() != 2 && mCountry.getSelectedItemPosition() == 0 ))
        {
            toast(getString(R.string.fill_user_country_role));
            return false;
        }
        if (!(mRole.getSelectedItemId()==0) &&
                (TextUtils.isEmpty(mKey.getText().toString().trim()))){
            toast(getString(R.string.fill_key_for_manager));
            return false;
        }
        else return true;

    }

    private boolean checkKeySuccess() {

        String key =null;
        switch (mRole.getSelectedItemPosition()) {
            case 1:
                key = getKey("ROLE_COUNTRY_MANAGER");
                break;
            case 2:
                key = getKey("ROLE_INTERNATIONAL_MANAGER");
                break;
            default:
                break;
        }
            if (key.equals(mKey.getText().toString()))
                return true;
            else return false;
    }

    private void notifyUserSaved(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(getString(R.string.alert_user_registered))
                .setCancelable(false)
                .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

}
