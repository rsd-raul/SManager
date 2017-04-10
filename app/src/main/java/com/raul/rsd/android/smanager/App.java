package com.raul.rsd.android.smanager;


import android.app.Application;

import com.raul.rsd.android.smanager.helpers.PrimaryKeyHelper;

import javax.inject.Singleton;
import dagger.Component;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    // ------------------------- ATTRIBUTES --------------------------

    private AppComponent appComponent;

    // -------------------------- INTERFACE --------------------------

    @Singleton
    @Component(modules = AppModule.class)
    public interface AppComponent {
        void inject(App application);
        void inject(LoginActivity loginActivity);
//        void inject(DashboardActivity dashboardActivity);
    }

    // ------------------------- CONSTRUCTOR -------------------------

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerApp_AppComponent
                .builder()
                .appModule(
                        new AppModule(this))
                .build();
        appComponent.inject(this);

        // Initialize Realm and our PrimaryKeyHelper
        Realm.init(this);
        PrimaryKeyHelper.initialize(Realm.getDefaultInstance());
    }

    public AppComponent getComponent() {
        return appComponent;
    }

}
