package com.raul.rsd.android.smanager.managers;

import android.content.Context;
import android.util.Log;

import com.raul.rsd.android.smanager.domain.Skill;
import com.raul.rsd.android.smanager.domain.Task;
import com.raul.rsd.android.smanager.domain.User;
import com.raul.rsd.android.smanager.helpers.PrimaryKeyHelper;
import com.raul.rsd.android.smanager.repositories.TaskRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmList;

@Singleton
public class DataManager {

    private Context context;
    private TaskRepository taskRepository;

    @Inject
    public DataManager(Context context, TaskRepository taskRepository) {
        this.context = context;
        this.taskRepository = taskRepository;
    }

    // --------------------------- Values ----------------------------

    private static final String TAG = "LoginActivity";

    // -------------------------- Use Cases --------------------------

    public void firstTimePopulation(){
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
        final Task task1 = taskRepository.save(new Task("Reponer pasillo 12", reponer, 30));
        final Task task2 = taskRepository.save(new Task("Reponer pasillo 2", reponer, 12));
        final Task task3 = taskRepository.save(new Task("Cobrar pedido 27", cobrar, 10));
        final Task task4 = taskRepository.save(new Task("Empaquetar cestas navidad", envolver, 55));
        final Task task5 = taskRepository.save(new Task("Reparar ordenadores", soporte, 120));

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

        Log.e(TAG, "firstTimePopulation: " + realm.where(Task.class).findAll());
        Log.e(TAG, "firstTimePopulation: " + realm.where(Skill.class).findAll());
        Log.e(TAG, "firstTimePopulation: " + realm.where(User.class).findAll());
    }

    private long getNewId(){
        return PrimaryKeyHelper.nextKey();
    }
}
