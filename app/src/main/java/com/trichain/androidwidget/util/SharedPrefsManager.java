package com.trichain.androidwidget.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {
    private static SharedPreferences sharedPreferences;
    private static SharedPrefsManager mInstance;
    private static final String SHARED_PREFS_NAME = "android_widget";

    public static final String KEY_FONT_SIZE = "key_font_size";
    public static final String KEY_FONT_COLOR = "key_font_color";
    public static final String KEY_DATE_COLOR = "key_date_color";
    public static final String KEY_TIME_COLOR = "key_time_color";
    public static final String KEY_APPOINTMENT_COLOR = "key_appointment_color";
    public static final String KEY_LOCATION_COLOR = "key_location_color";
    public static final String KEY_DATE_FORMAT = "key_date_format";
    public static final String KEY_TIME_FORMAT = "key_time_format";

    public SharedPrefsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefsManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefsManager(context);
        }

        return mInstance;
    }

    public void savePref(String prefs_key, int colorInt) {
        sharedPreferences.edit().putInt(prefs_key, colorInt).apply();
    }

    public void savePref(String prefs_key, String selectedDateFormat) {
        sharedPreferences.edit().putString(prefs_key, selectedDateFormat).apply();
    }

    public void savePref(String prefs_key, float fontSize) {
        sharedPreferences.edit().putFloat(prefs_key, fontSize).apply();
    }

    public int getIntPref(String PREFS_KEY) {
        return sharedPreferences.getInt(PREFS_KEY, -12411905);
    }

    public String getStringPref(String PREFS_KEY) {
        return sharedPreferences.getString(PREFS_KEY, "");
    }

    public float getFloatPref(String PREFS_KEY) {
        return sharedPreferences.getFloat(PREFS_KEY, 11.0f);
    }
}
