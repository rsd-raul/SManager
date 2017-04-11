package com.raul.rsd.android.smanager.tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.raul.rsd.android.smanager.domain.Skill;
import com.raul.rsd.android.smanager.domain.Task;
import com.raul.rsd.android.smanager.domain.User;
import com.raul.rsd.android.smanager.helpers.PrimaryKeyHelper;
import com.raul.rsd.android.smanager.managers.DataManager;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;

public class InitializeDatabaseTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "MainActivity";
    private DataManager dataManager;

    @Inject
    InitializeDatabaseTask(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.i(TAG, "firstTimePopulation: Started");
        Realm realm = Realm.getDefaultInstance();

        // Save skills
        realm.beginTransaction();
        final Skill reponer = realm.copyToRealm(new Skill(getNewId(), "Reponer productos"));
        final Skill cobrar = realm.copyToRealm(new Skill(getNewId(), "Cobrar"));
        final Skill envolver = realm.copyToRealm(new Skill(getNewId(), "Envolver"));
        final Skill soporte = realm.copyToRealm(new Skill(getNewId(), "Soporte"));
        realm.commitTransaction();

        // Save tasks
        dataManager.saveTask(new Task("Reponer pasillo 12", reponer, 30));
        dataManager.saveTask(new Task("Reponer pasillo 2", reponer, 12));
        dataManager.saveTask(new Task("Cobrar pedido 27", cobrar, 10));
        dataManager.saveTask(new Task("Empaquetar cestas navidad", envolver, 55));
        dataManager.saveTask(new Task("Reparar ordenadores", soporte, 120));

        RealmList<Skill> skills1 = new RealmList<>(reponer, cobrar);
        RealmList<Skill> skills2 = new RealmList<>(envolver, cobrar);
        RealmList<Skill> skills3 = new RealmList<>(soporte);

        // Save users
        realm.beginTransaction();
        realm.copyToRealm(new User(getNewId(), "tech1", "password1", skills1, User.TECH));
        realm.copyToRealm(new User(getNewId(), "tech2", "password2", skills2, User.TECH));
        realm.copyToRealm(new User(getNewId(), "tech3", "password3", skills3, User.TECH));
        realm.copyToRealm(new User(getNewId(), "admin", "admin", null, User.ADMIN));
        realm.commitTransaction();

        Log.i(TAG, "firstTimePopulation: Completed");

        Log.e(TAG, "firstTimePopulation: " + realm.where(Task.class).findAll());
        Log.e(TAG, "firstTimePopulation: " + realm.where(Skill.class).findAll());
        Log.e(TAG, "firstTimePopulation: " + realm.where(User.class).findAll());

        return null;
    }

    private long getNewId(){
        return PrimaryKeyHelper.nextKey();
    }
}