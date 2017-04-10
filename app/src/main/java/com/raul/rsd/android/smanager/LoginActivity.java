package com.raul.rsd.android.smanager;

import android.os.Bundle;
import android.util.Log;

import com.raul.rsd.android.smanager.helpers.PreferencesHelper;
import com.raul.rsd.android.smanager.helpers.PreferencesHelper.*;
import com.raul.rsd.android.smanager.managers.DataManager;

import javax.inject.Inject;

import io.realm.Realm;

public class LoginActivity extends BaseActivity {

    // --------------------------- Values ----------------------------

    private static final String TAG = "LoginActivity";

    // -------------------------- Injected ---------------------------

    @Inject DataManager dataManager;

    // ------------------------- Constructor -------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // If it's the first time, populate the DB
        if (PreferencesHelper.getBoolean(this, Keys.FIRST_TIME, Defaults.FIRST_TIME)) {
            PreferencesHelper.setBoolean(this, Keys.FIRST_TIME, false);

            Log.i(TAG, "First Time Population - Starting");

            dataManager.firstTimePopulation();

            Log.i(TAG, "First Time Population - Completed");
        }


    }

    @Override
    protected void inject(App.AppComponent component) {
        component.inject(this);
    }

    // -------------------------- Use Cases --------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the Realm instance.
        Realm.getDefaultInstance().close();
    }
}
