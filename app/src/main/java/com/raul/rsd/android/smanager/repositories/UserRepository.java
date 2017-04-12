package com.raul.rsd.android.smanager.repositories;

import com.raul.rsd.android.smanager.domain.Skill;
import com.raul.rsd.android.smanager.domain.Task;
import com.raul.rsd.android.smanager.domain.User;
import com.raul.rsd.android.smanager.domain.User;
import com.raul.rsd.android.smanager.helpers.PrimaryKeyHelper;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class UserRepository implements BaseRepository<User> {

    // -------------------------- Injected ---------------------------

    private Realm realm;

    // ------------------------ Constructor --------------------------

    @Inject
    UserRepository(Realm realm) {
        this.realm = realm;
    }

    // ---------------------------- Find -----------------------------

    @Override
    public User findOne(long id) {
        return realm.where(User.class).equalTo("id", id).findFirst();
    }

    public User findByName(String name) {
        return realm.where(User.class).equalTo("name", name).findFirst();
    }

    @Override
    public RealmResults<User> findAll() {
        return realm.where(User.class).findAll();
    }

    public RealmResults<User> findUsersWithSkill(Skill skill){
        return realm.where(User.class).equalTo("skills.id", skill.getId()).findAll();
    }

    // ---------------------------- Save -----------------------------

    @Override
    public User save(final User User) {
        User managedUser;

        realm.beginTransaction();
        if (User.getId() == 0)
            User.setId(PrimaryKeyHelper.nextKey());

        managedUser = realm.copyToRealmOrUpdate(User);
        realm.commitTransaction();

        return managedUser;
    }

    // ----------------------------- Add -----------------------------

    public void addTask(long id, Task task) {
        realm.beginTransaction();

        User user = realm.where(User.class).equalTo("id", id).findFirst();
        user.getTasks().add(task);

        realm.commitTransaction();
    }

    public void addTaskToAdmin(Task task) {
        realm.beginTransaction();

        User user = realm.where(User.class).equalTo("userType", User.ADMIN).findFirst();
        user.getTasks().add(task);

        realm.commitTransaction();
    }
}