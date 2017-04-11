package com.raul.rsd.android.smanager;

import android.app.Application;
import android.content.Context;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.raul.rsd.android.smanager.adapters.CustomItem;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;


@Module
public class AppModule {

    private Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context contextProvider() {
        return app;
    }

    @Provides
    Realm realmProvider(){
        return Realm.getDefaultInstance();
    }

    @Provides
    FastItemAdapter<CustomItem> fastCustomItemAdapterProvider(){
        return new FastItemAdapter<>();
    }
}