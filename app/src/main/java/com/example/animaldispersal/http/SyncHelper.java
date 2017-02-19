package com.example.animaldispersal.http;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.animaldispersal.AppController;
import com.example.animaldispersal.localdb.LocalDBHelper;
import com.example.davaodemo.R;

/**
 * Created by user on 21/6/2016.
 */
public class SyncHelper {

    private static final String TAG = SyncHelper.class.getName();

    ProgressDialog progressDialog;
    LocalDBHelper localDBHelper;
    String get_keys_url = "http://www.careagriculture.com/get_keys.php";
    String read_animal_url = "http://www.careagriculture.com/get_all_animals6.php";
    String insert_animals_url = "http://www.careagriculture.com/insert_animals28.php";

    Context context;

    public SyncHelper(Context c) {
        context = c;
        localDBHelper = LocalDBHelper.getInstance(c);
    }

    // JSON Node names
    public static final String ITEM_ID = "id";
    public static final String ITEM_NAME = "item";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ANIMAL = "animal";
    public static final String TAG_ANIMAL_ID = "animal_id";
    public static final String TAG_SUPERVISOR = "supervisor";
    public static final String TAG_CARETAKER_UID = "caretaker_uid";
    public static final String TAG_ANIMAL_TYPE = "animal_type";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_DATE_OF_BIRTH = "date_of_birth";
    public static final String TAG_COUNTRY = "country";
    public static final String TAG_DATE_PURCHASED = "date_purchased";
    public static final String TAG_PURCHASE_PRICE = "purchase_price";
    public static final String TAG_DATE_DISTRIBUTED = "date_distributed";
    public static final String TAG_DATE_SOLD = "date_sold";
    public static final String TAG_SALE_PRICE = "sale_price";
    public static final String TAG_CREATE_USER = "create_user";
    public static final String TAG_CREATE_TIMESTAMP = "create_timestamp";

    public static final String TAG_EVENT_ID = "event_id";
    public static final String TAG_EVENT_TYPE= "event_type";
    public static final String TAG_EVENT_DATE_TIME = "event_date_time";
    public static final String TAG_EVENT_REMARKS = "event_remarks";

    private ArrayList<String> message;


    public void DeviceDataUpload() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Data transfer in progress.....");
        progressDialog.show();
        message = new ArrayList<String> ();

        StringRequest postRequest = new StringRequest(Method.POST, insert_animals_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, insert_animals_url + " response " + response);
                            if (!(response == null)) {
                                //progressDialog.dismiss();

                                JSONObject c = new JSONObject(response);

                                JSONObject animalResponse = c.getJSONObject("animal");
                                Iterator animalKeys = animalResponse.keys();
                                while (animalKeys.hasNext()) {
                                    String key = (String) animalKeys.next();
                                    if ("NA".equals(key)) break;

                                    String animalId = key;
                                    JSONObject animalIdResponse = animalResponse.getJSONObject(animalId);
                                    int success = animalIdResponse.getInt("success");
                                    if (success == 1) {
                                        localDBHelper.updateAnimalSyncSuccess(animalId);
                                        localDBHelper.deleteAnimalbyAnimalIDFromDevice(animalId);
                                    } else {
                                        String errorMessage = animalIdResponse.getString("message");
                                        message.add(errorMessage);
                                        localDBHelper.updateAnimalSyncFail(animalId, errorMessage);
                                    }
                                }

                                JSONObject eventResponse = c.getJSONObject("event");
                                Iterator eventKeys = eventResponse.keys();
                                while (eventKeys.hasNext()) {
                                    String eventKey = (String) eventKeys.next();
                                    if ("NA".equals(eventKey)) break;

                                    String eventId = eventKey;
                                    JSONObject eventIdResponse = eventResponse.getJSONObject(eventId);
                                    int success = eventIdResponse.getInt("success");
                                    String animal_id = eventIdResponse.getString("animal_id");
                                    if (success == 1) {
                                        localDBHelper.updateEventSyncSuccess(eventId, animal_id);
                                        localDBHelper.deleteEventbyEventIdFromDevice(eventId, animal_id);
                                    } else {
                                        String errorMessage = eventIdResponse.getString("message");
                                        message.add(errorMessage);
                                        localDBHelper.updateEventSyncFail(eventId, animal_id, errorMessage);
                                    }
                                }



                                JSONObject caretakerResponse = c.getJSONObject("caretaker");
                                Iterator caretakerKeys = caretakerResponse.keys();
                                while (caretakerKeys.hasNext()) {
                                    String caretakerKey = (String) caretakerKeys.next();
                                    if ("NA".equals(caretakerKey)) break;

                                    String caretakerUid = caretakerKey;
                                    JSONObject caretakerIDResponse = caretakerResponse.getJSONObject(caretakerUid);
                                    int success = caretakerIDResponse.getInt("success");
                                    String animal_id = caretakerIDResponse.getString("animal_id");
                                    if (success == 1) {
                                        localDBHelper.updateCaretakerSyncSuccess(caretakerUid, animal_id);
                                        localDBHelper.deleteCaretakerbyCaretakerUidFromDevice(caretakerUid, animal_id);

                                    } else {
                                        String errorMessage = caretakerIDResponse.getString("message");
                                        message.add(errorMessage);
                                        localDBHelper.updateCaretakerSyncFail(caretakerUid, animal_id, errorMessage);
                                    }
                                }

                            }
                            ServerDataDownload();
                        }catch (JSONException e) {
                            e.printStackTrace();
                            message.add("JSONException: "+e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                error.printStackTrace();
                message.add("onErrorResponse: "+error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = localDBHelper.getAllAnimalMap();
                Log.d(TAG, insert_animals_url+" "+params);
                return params;
            }

            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }

        };

        AppController.getInstance().addToReqQueue(postRequest);
        //progressDialog.dismiss();
    }


    public void ServerDataDownload(){

        StringRequest readAnimalReq = new StringRequest(Method.POST, read_animal_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    if (!(response == null)) {
                        //progressDialog.dismiss();

                        JSONObject aResponse = new JSONObject(response);
                        int animal_success = aResponse.getInt("animal_success");
                        if (animal_success == 1 && !aResponse.isNull("animal")) {
                            JSONArray ja = aResponse.getJSONArray("animal");
                            localDBHelper.deleteAllAnimalFromServer();

                            for (int i = 0; i < ja.length(); i++) {

                                JSONObject c = ja.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                map.put("ANIMAL_ID", c.getString(TAG_ANIMAL_ID));
                                map.put("SUPERVISOR", c.getString(TAG_SUPERVISOR));
                                map.put("CARETAKER_UID", c.getString(TAG_CARETAKER_UID));
                                map.put("ANIMAL_TYPE", c.getString(TAG_ANIMAL_TYPE));
                                map.put("RECORD_TYPE", "S");
                                if (!c.isNull(TAG_GENDER))
                                    map.put("GENDER", c.getString(TAG_GENDER));
                                if (!c.isNull(TAG_DATE_OF_BIRTH))
                                    map.put("DATE_OF_BIRTH", c.getString(TAG_DATE_OF_BIRTH));
                                if (!c.isNull(TAG_COUNTRY))
                                    map.put("COUNTRY", c.getString(TAG_COUNTRY));
                                if (!c.isNull(TAG_DATE_PURCHASED))
                                    map.put("DATE_PURCHASED", c.getString(TAG_DATE_PURCHASED));
                                if (!c.isNull(TAG_PURCHASE_PRICE))
                                    map.put("PURCHASE_PRICE", c.getString(TAG_PURCHASE_PRICE));
                                if (!c.isNull(TAG_DATE_DISTRIBUTED))
                                    map.put("DATE_DISTRIBUTED", c.getString(TAG_DATE_DISTRIBUTED));
                                if (!c.isNull(TAG_DATE_SOLD))
                                    map.put("DATE_SOLD", c.getString(TAG_DATE_SOLD));
                                if (!c.isNull(TAG_SALE_PRICE))
                                    map.put("SALE_PRICE", c.getString(TAG_SALE_PRICE));
                                if (!c.isNull(TAG_CREATE_USER))
                                    map.put("CREATE_USER", c.getString(TAG_CREATE_USER));
                                if (!c.isNull(TAG_CREATE_TIMESTAMP))
                                    map.put("CREATE_TIMESTAMP", c.getString(TAG_CREATE_TIMESTAMP));

                                localDBHelper.insertAnimalFromServer(map);

                            } // for loop ends
                        } else if (animal_success == 0) {
                            //if (TextUtils.isEmpty(message)) {
                                message.add(aResponse.getString("animal_message"));
                            //} else
                                //message = message.concat("\n" + aResponse.getString("animal_message"));
                        }

                        int event_success = aResponse.getInt("event_success");
                        if (event_success == 1 && !aResponse.isNull("event")) {
                            JSONArray eventArray = aResponse.getJSONArray("event");
                            localDBHelper.deleteAllEventFromServer();
                            for (int i = 0; i < eventArray.length(); i++) {

                                JSONObject c = eventArray.getJSONObject(i);
                                // creating new HashMap
                                HashMap<String, String> map = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                map.put("ANIMAL_ID", c.getString(TAG_ANIMAL_ID));
                                map.put("EVENT_ID", c.getString(TAG_EVENT_ID));
                                map.put("RECORD_TYPE", "S");
                                if (!c.isNull(TAG_EVENT_TYPE))
                                    map.put("EVENT_TYPE", c.getString(TAG_EVENT_TYPE));
                                if (!c.isNull(TAG_EVENT_DATE_TIME))
                                    map.put("EVENT_DATE_TIME", c.getString(TAG_EVENT_DATE_TIME));
                                if (!c.isNull(TAG_EVENT_REMARKS))
                                    map.put("EVENT_REMARKS", c.getString(TAG_EVENT_REMARKS));
                                if (!c.isNull(TAG_CREATE_USER))
                                    map.put("CREATE_USER", c.getString(TAG_CREATE_USER));
                                if (!c.isNull(TAG_CREATE_TIMESTAMP))
                                    map.put("CREATE_TIMESTAMP", c.getString(TAG_CREATE_TIMESTAMP));

                                localDBHelper.insertEventFromServer(map);

                            }

                        } else if (event_success == 0) {
                            message.add(aResponse.getString("event_message"));
                        }

                        int caretaker_success = aResponse.getInt("caretaker_success");
                        if (caretaker_success == 1 && !aResponse.isNull("caretaker")) {
                            JSONArray caretakerArray = aResponse.getJSONArray("caretaker");
                            localDBHelper.deleteAllCaretakerFromServer();
                            for (int i = 0; i < caretakerArray.length(); i++) {

                                JSONObject c = caretakerArray.getJSONObject(i);
                                // creating new HashMap
                                HashMap<String, String> map = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                map.put("ANIMAL_ID", c.getString("animal_id"));
                                map.put("CARETAKER_ID", c.getString("caretaker_id"));
                                map.put("CARETAKER_UID", c.getString("caretaker_uid"));
                                if (!c.isNull("caretaker_name"))
                                    map.put("CARETAKER_NAME", c.getString("caretaker_name"));
                                if (!c.isNull("caretaker_tel"))
                                    map.put("CARETAKER_TEL", c.getString("caretaker_tel"));
                                if (!c.isNull("caretaker_addr_1"))
                                    map.put("CARETAKER_ADDR_1", c.getString("caretaker_addr_1"));
                                if (!c.isNull("caretaker_addr_2"))
                                    map.put("CARETAKER_ADDR_2", c.getString("caretaker_addr_2"));
                                if (!c.isNull("caretaker_addr_3"))
                                    map.put("CARETAKER_ADDR_3", c.getString("caretaker_addr_3"));
                                if (!c.isNull(TAG_CREATE_USER))
                                    map.put("CREATE_USER", c.getString(TAG_CREATE_USER));
                                if (!c.isNull(TAG_CREATE_TIMESTAMP))
                                    map.put("CREATE_TIMESTAMP", c.getString(TAG_CREATE_TIMESTAMP));
                                map.put("RECORD_TYPE", "S");
                                localDBHelper.insertCaretakerFromServer(map);

                            }

                        } else if (caretaker_success == 0) {
                            message.add(aResponse.getString("caretaker_message"));
                        }

                        /*
                        if (message.size()>0){
                            toast (TextUtils.join("\n",message));
                            localDBHelper.insertSystemProperty("LAST_SYNC_MESSAGE",TextUtils.join("\n",message));
                        } else
                            toast("Sync Completed Successfully");
                        */
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.add("onErrorResponse: "+e.getMessage());
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                message.add("onErrorResponse: "+error.getMessage());
            }
        }){

            @Override
            protected void onFinish() {
                super.onFinish();

                if (message.size()>0){
                    message.add(0,message.size()+" error(s):");
                    toast (TextUtils.join("\n -",message));
                    localDBHelper.updateSystemSyncMessage(TextUtils.join("\n",message));
                } else
                    toast(context.getString(R.string.sync_success));

                localDBHelper.updateSystemSyncTimestamp(Calendar.getInstance());

                TextView countRecordsToSync = (TextView) ((Activity)context).findViewById(R.id.syncRecordCount);
                countRecordsToSync.setText(String.valueOf(localDBHelper.getCountUnsyncRecords())+" record(s) have not been synced");
            }

            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();

                String user = AppController.getInstance().getUser();
                String userRole = String.valueOf(AppController.getInstance().getUserRole());
                String userCountry = AppController.getInstance().getUserCountry();

                try {
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("user",user );
                jsonParam.put("role", userRole);
                jsonParam.put("country", userCountry);
                params.put("params", jsonParam.toString());
                } catch (Exception e) {
                    Log.d(TAG, "Error in "+read_animal_url+" getParams");
                    e.printStackTrace();
                }
                Log.d(TAG, read_animal_url+" "+params);

                return params;
            }

            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToReqQueue(readAnimalReq);

    }

    public void KeysDownload(){

        //progressDialog = new ProgressDialog(context);
        //progressDialog.setMessage("Data transfer from server to device.....");
        //progressDialog.show();

        JsonObjectRequest keysRequest = new JsonObjectRequest(Method.GET, get_keys_url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    int key_success = response.getInt("key_success");
                    if (key_success == 1 && !response.isNull("key")) {
                        JSONArray ja = response.getJSONArray("key");
                        localDBHelper.deleteKey("ROLE_COUNTRY_MANAGER");
                        localDBHelper.deleteKey("ROLE_INTERNATIONAL_MANAGER");

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject c = ja.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put("ROLE", c.getString("role"));
                            map.put("KEY", c.getString("key"));
                            if (!c.isNull(TAG_CREATE_USER))
                                map.put("CREATE_USER", c.getString(TAG_CREATE_USER));
                            if (!c.isNull(TAG_CREATE_TIMESTAMP))
                                map.put("CREATE_TIMESTAMP", c.getString(TAG_CREATE_TIMESTAMP));

                            Log.d(TAG, "insertSystemPropertyFromServer");
                            localDBHelper.insertSystemPropertyFromServer(map);

                        } // for loop ends
                    } else if (key_success == 0 ){
                        Toast.makeText(context, "Error downloading keys.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();

            }
        }){
            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToReqQueue(keysRequest);

    }

    public void toast(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
