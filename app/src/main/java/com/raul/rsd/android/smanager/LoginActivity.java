package com.raul.rsd.android.smanager;

import android.os.Bundle;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
    }

    @Override
    protected void inject(App.AppComponent component) {
        component.inject(this);
    }
}
