package com.raul.rsd.android.smanager.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public abstract class PreferencesHelper {

    // --------------------------- VALUES ----------------------------

    public abstract static class Keys{
        public static final String FIRST_TIME = "first_time";
        public static final String LOGGED_USER = "logged_user";
    }

    public abstract static class Defaults{
        public static final boolean FIRST_TIME = true;
        public static final int LOGGED_USER = -1;
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

    public static long getLong(Context context, String key, long defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, defaultValue);
    }
    public static void setLong(Context context, String key, long value) {
        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
        ed.putLong(key, value);
        ed.apply();                     // Beware: Background thread
    }
}