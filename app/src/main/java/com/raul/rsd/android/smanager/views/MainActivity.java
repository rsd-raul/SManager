package com.raul.rsd.android.smanager.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.raul.rsd.android.smanager.App;
import com.raul.rsd.android.smanager.R;
import com.raul.rsd.android.smanager.helpers.PreferencesHelper;
import com.raul.rsd.android.smanager.helpers.PreferencesHelper.*;
import com.raul.rsd.android.smanager.managers.DataManager;
import com.raul.rsd.android.smanager.tasks.InitializeDatabaseTask;
import javax.inject.Inject;
import javax.inject.Provider;
import io.realm.Realm;

public class MainActivity extends BaseActivity {

    // --------------------------- Values ----------------------------

    private final int LOGIN = 0, DASHBOARD = 1;
    private static final String TAG = "MainActivity";

    // -------------------------- Injected ---------------------------

    @Inject DataManager dataManager;
    @Inject InitializeDatabaseTask initializeDatabaseTask;
    @Inject Provider<DashboardFragment> dashboardFragmentProvider;
    @Inject Provider<LoginFragment> loginFragmentProvider;

    // ------------------------- Constructor -------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If it's the first time, populate the DB
        if (PreferencesHelper.getBoolean(this, Keys.FIRST_TIME, Defaults.FIRST_TIME)) {
            PreferencesHelper.setBoolean(this, Keys.FIRST_TIME, false);

            Log.i(TAG, "First Time Population - Starting");
            initializeDatabaseTask.execute();
            Log.i(TAG, "First Time Population - Completed");
        }

        long userId = PreferencesHelper.getLong(this, Keys.LOGGED_USER, Defaults.LOGGED_USER);
        setFragment(userId);
    }

    @Override
    protected void inject(App.AppComponent component) {
        component.inject(this);
    }

    // -------------------------- Use Cases --------------------------

    public void setFragment(long userId) {
        Fragment newFragment = null;

        int keyConstant = userId == Defaults.LOGGED_USER ? LOGIN : DASHBOARD;
        switch (keyConstant) {
            case DASHBOARD:
                newFragment = dashboardFragmentProvider.get();
                break;
            case LOGIN:
                newFragment = loginFragmentProvider.get();
                break;
        }

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
