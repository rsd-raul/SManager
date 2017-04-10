package com.raul.rsd.android.smanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject(((App) getApplication()).getComponent());
    }

    /**
     * Implement and call component.inject(this) for dependency
     * injection of the Activity.
     */
    protected abstract void inject(App.AppComponent component);
}