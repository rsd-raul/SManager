package com.raul.rsd.android.smanager;


import android.app.Application;

import javax.inject.Singleton;
import dagger.Component;

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
    }

    public AppComponent getComponent() {
        return appComponent;
    }

}
