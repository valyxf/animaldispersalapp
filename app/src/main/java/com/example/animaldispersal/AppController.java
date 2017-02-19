package com.example.animaldispersal;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bugfender.sdk.Bugfender;
import com.example.davaodemo.R;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.Locale;

/**
 * Created by user on 21/6/2016.
 */
public class AppController extends Application {

    private static final String TAG = AppController.class.getName();
    private RequestQueue mRequestQueue;
    private static AppController mInstance;
    private static Context mContext;
    private SharedPreferences mPrefs;
    private Locale locale = null;

    @Override
    public void onCreate() {
        super.onCreate();

        //TODO Stetho Debugging
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());

        mInstance = this;
        mPrefs = getSharedPreferences("animalDispersalPrefs", Context.MODE_PRIVATE);

        //LOCALE CONFIGURATION SETTING
        Log.d(TAG, "APPLICATION ONCREATE");
        Configuration config = getBaseContext().getResources().getConfiguration();
        String lang = mPrefs.getString("locale", "");
        Log.d(TAG, "locale: "+lang);
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang))
        {
            locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,  getResources().getDisplayMetrics());
        }
        else {

        }

        //TODO Bugfender- remove this at live
        Bugfender.init(this, "KZv9hHwV0weAvKcsn9qewAcow0TmaBLi", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "APPLICATION CONFIG CHANGE");

        Configuration modConfig = new Configuration(newConfig);;
        String lang = mPrefs.getString("locale", "");
        Log.d(TAG, "locale: "+lang);
        if (! "".equals(lang) && ! modConfig.locale.getLanguage().equals(lang))
        {
            locale = new Locale(lang);
            Locale.setDefault(locale);
            modConfig.locale = locale;
            getBaseContext().getResources().updateConfiguration(modConfig,  getResources().getDisplayMetrics());
        }
        /*
        if (locale != null)
        {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getResources().getDisplayMetrics());
        }
        */
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToReqQueue(Request<T> req, String tag) {

        getRequestQueue().add(req);
    }

    public <T> void addToReqQueue(Request<T> req) {

        getRequestQueue().add(req);
    }

    public void cancelPendingReq(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public boolean getFirstRun() {
        return mPrefs.getBoolean("firstRun", true);
    }

    public void setRunned() {
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putBoolean("firstRun", false);
        edit.commit();
    }

    public String getUser() {
        return mPrefs.getString("username", null);
    }

    public String getUserCountry() {
        return mPrefs.getString("country", "");
    }

    public int getUserRole() {
        return mPrefs.getInt("role",9);
    }

    public void setTagLock(boolean tagLock) {
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putBoolean("tagLock", tagLock);
        edit.commit();
    }

    public boolean isTagLock() {
        return mPrefs.getBoolean("tagLock", true);
    }

    public void setLocale(Locale locale) {
        SharedPreferences.Editor edit = mPrefs.edit();
        Log.d(TAG, "Setting Locale Here: "+locale.toString());
        edit.putString("locale", locale.toString());
        edit.commit();
    }

    public Locale getLocale() {
        String x = mPrefs.getString("locale", null);
        Locale locale;
        if (x==null)
            locale = Locale.ENGLISH;
        else
            locale = new Locale(x);
        return locale;
    }
}
