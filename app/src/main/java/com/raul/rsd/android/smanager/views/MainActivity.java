package com.raul.rsd.android.smanager.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.raul.rsd.android.smanager.R;
import com.raul.rsd.android.smanager.helpers.PreferencesHelper;
import com.raul.rsd.android.smanager.helpers.PreferencesHelper.*;
import com.raul.rsd.android.smanager.tasks.InitializeDatabaseTask;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity{

    // --------------------------- Values ----------------------------

    private static final String TAG = "MainActivity";

    // -------------------------- Injected ---------------------------

    // ------------------------- Constructor -------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If it's the first time, populate the DB
        if (PreferencesHelper.getBoolean(this, Keys.FIRST_TIME, Defaults.FIRST_TIME)) {
            PreferencesHelper.setBoolean(this, Keys.FIRST_TIME, false);

            Log.i(TAG, "First Time Population - Starting");
            new InitializeDatabaseTask().execute();
            Log.i(TAG, "First Time Population - Completed");
        }

        long userId = PreferencesHelper.getLong(this, Keys.LOGGED_USER, Defaults.LOGGED_USER);
        setFragment(userId);
    }

    // -------------------------- Use Cases --------------------------

    public void setFragment(long userId) {
        Fragment newFragment;

        if(userId == Defaults.LOGGED_USER)
            newFragment = new LoginFragment();
        else
            newFragment = new DashboardFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment_container, newFragment).commit();
    }

    // --------------------------- STATES ----------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the Realm instance.
        Realm.getDefaultInstance().close();
    }
}
