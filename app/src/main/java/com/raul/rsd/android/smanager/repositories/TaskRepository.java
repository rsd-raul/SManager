package com.raul.rsd.android.smanager.repositories;

import com.raul.rsd.android.smanager.domain.Task;
import com.raul.rsd.android.smanager.domain.User;
import com.raul.rsd.android.smanager.helpers.PrimaryKeyHelper;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TaskRepository implements BaseRepository<Task> {

    // -------------------------- Injected ---------------------------

    private Realm realm;

    // ------------------------ Constructor --------------------------

    @Inject
    TaskRepository(Realm realm) {
        this.realm = realm;
    }

    // ---------------------------- Find -----------------------------

    @Override
    public Task findOne(long id) {
        return realm.where(Task.class).equalTo("id", id).findFirst();
    }

    @Override
    public RealmResults<Task> findAll() {
        return realm.where(Task.class).findAll();
    }

    public RealmList<Task> findAllByUserId(long userId){
        return realm.where(User.class).contains("id", String.valueOf(userId)).findAll().get(0).getTasks();
    }

    // ---------------------------- Save -----------------------------

    @Override
    public Task save(final Task task) {
        Task managedTask;

        realm.beginTransaction();
        if (task.getId() == 0)
            task.setId(PrimaryKeyHelper.nextKey());

        managedTask = realm.copyToRealmOrUpdate(task);
        realm.commitTransaction();

        return managedTask;
    }

    // ------------------------- Use Cases ---------------------------

    public void changeTaskCompletion(long taskId) {
        Task task = findOne(taskId);

        realm.beginTransaction();
        task.setCompleted(!task.isCompleted());
        realm.commitTransaction();
    }
}