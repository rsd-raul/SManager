package com.raul.rsd.android.smanager;

import android.accounts.NetworkErrorException;
import android.os.Bundle;
import android.util.Log;

import com.raul.rsd.android.smanager.domain.Resource;
import com.raul.rsd.android.smanager.helpers.PreferencesHelper;
import com.raul.rsd.android.smanager.helpers.PreferencesHelper.*;
import com.raul.rsd.android.smanager.managers.DataManager;
import com.raul.rsd.android.smanager.helpers.NetworkHelper;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        NetworkHelper.getRequestedResource(new Callback<List<Resource>>() {
            @Override
            public void onResponse(Call<List<Resource>> call, Response<List<Resource>> response) {
                if(response == null || response.body() == null) {
                    onFailure(call, new NetworkErrorException("Resource request failed"));
                    return;
                }

                for (Resource aux : response.body()){
                    Log.e(TAG, "onResponse: " + aux.getLocation().getLatitude());
                }
            }

            @Override
            public void onFailure(Call<List<Resource>> call, Throwable t) {
                Log.e(TAG, "onCreate: onFailure: ", t);
            }
        });
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
