package com.raul.rsd.android.smanager.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public abstract class PreferencesHelper {

    // --------------------------- VALUES ----------------------------

    public abstract static class Keys{
        // App settings
        public static final String FIRST_TIME = "first_time";
    }

    public abstract static class Defaults{
        // App settings
        public static final boolean FIRST_TIME = true;
    }


    // --------------------------- GETTERS ---------------------------

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
        ed.putBoolean(key, value);
        ed.apply();                     // Beware: Background thread
    }
}