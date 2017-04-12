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
    private Realm realm;

    @Inject
    InitializeDatabaseTask(Realm realm) {
        this.realm = realm;
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
        realm.beginTransaction();
        final Task task1 = realm.copyToRealm(new Task(getNewId(), "Reponer pasillo 12", reponer, 30));
        final Task task2 = realm.copyToRealm(new Task(getNewId(), "Reponer pasillo 2", reponer, 12));
        final Task task3 = realm.copyToRealm(new Task(getNewId(), "Cobrar pedido 27", cobrar, 10));
        final Task task4 = realm.copyToRealm(new Task(getNewId(), "Empaquetar cestas navidad", envolver, 55));
        final Task task5 = realm.copyToRealm(new Task(getNewId(), "Reparar ordenadores", soporte, 120));
        realm.commitTransaction();

        RealmList<Skill> skills1 = new RealmList<>(reponer, cobrar);
        RealmList<Skill> skills2 = new RealmList<>(envolver, cobrar);
        RealmList<Skill> skills3 = new RealmList<>(soporte);

        RealmList<Task> tasks1 = new RealmList<>(task1, task2);
        RealmList<Task> tasks2 = new RealmList<>(task3, task4);
        RealmList<Task> tasks3 = new RealmList<>(task5);
        RealmList<Task> tasks4 = new RealmList<>(task1, task2, task3, task4, task5);

        // Save users
        realm.beginTransaction();
        realm.copyToRealm(new User(getNewId(), "tech1", "password1", skills1, tasks1, User.TECH));
        realm.copyToRealm(new User(getNewId(), "tech2", "password2", skills2, tasks2, User.TECH));
        realm.copyToRealm(new User(getNewId(), "tech3", "password3", skills3, tasks3, User.TECH));
        realm.copyToRealm(new User(getNewId(), "admin", "admin", null, tasks4, User.ADMIN));
        realm.commitTransaction();

        Log.i(TAG, "firstTimePopulation: Completed");

        //TODO quitar
        Log.e(TAG, "firstTimePopulation: " + realm.where(Task.class).findAll());
        Log.e(TAG, "firstTimePopulation: " + realm.where(Skill.class).findAll());
        Log.e(TAG, "firstTimePopulation: " + realm.where(User.class).findAll());

        return null;
    }

    private long getNewId(){
        return PrimaryKeyHelper.nextKey();
    }
}