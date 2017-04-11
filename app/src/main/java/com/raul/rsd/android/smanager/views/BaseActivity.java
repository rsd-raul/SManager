package com.raul.rsd.android.smanager.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.raul.rsd.android.smanager.App;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dependency injection - Dagger
        inject(((App) getApplication()).getComponent());
    }

    /**
     * Implement and call component.inject(this) for dependency
     * injection of the Activity.
     */
    protected abstract void inject(App.AppComponent component);
}
