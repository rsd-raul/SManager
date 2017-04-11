package com.raul.rsd.android.smanager.repositories;

import com.raul.rsd.android.smanager.domain.Skill;
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
    public UserRepository(Realm realm) {
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

    // ----------------------------- Add -----------------------------

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

    // ---------------------------- SAVE -----------------------------

//    public void UserModifier(final int uniqueParameterId, final User User, final Date date){
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                switch (uniqueParameterId){
//
//                    case Constants.DASH_DONE:
//                        User.setCompleted(!User.isCompleted());
//                        break;
//
//                    case Constants.DASH_FAVE:
//                        User.setStarred(!User.isStarred());
//                        break;
//
//                    case Constants.DASH_DATE:
//                        User.setDue(date);
//                        break;
//                }
//            }
//        });
//    }
//
//    // --------------------------- Delete ----------------------------
//
//    @Override
//    public void deleteById(final long id) {
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                User User = realm.where(User.class).equalTo("id", id).findFirst();
//                User.deleteFromRealm();
//            }
//        });
//    }
//
//    @Override
//    public void deleteByPosition(final int position) {
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                RealmResults<User> results = realm.where(User.class).findAll();
//                results.get(position).deleteFromRealm();
//            }
//        });
//    }
}