package com.raul.rsd.android.smanager.repositories;

import com.raul.rsd.android.smanager.domain.Skill;
import com.raul.rsd.android.smanager.domain.User;
import com.raul.rsd.android.smanager.helpers.PrimaryKeyHelper;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class SkillRepository implements BaseRepository<Skill> {

    // -------------------------- Injected ---------------------------

    private Realm realm;

    // ------------------------ Constructor --------------------------

    @Inject
    SkillRepository(Realm realm) {
        this.realm = realm;
    }

    // ---------------------------- Find -----------------------------

    @Override
    public Skill findOne(long id) {
        return realm.where(Skill.class).equalTo("id", id).findFirst();
    }

    @Override
    public RealmResults<Skill> findAll() {
        return realm.where(Skill.class).findAll();
    }

    // ---------------------------- Save -----------------------------

    @Override
    public Skill save(Skill domain) {
        throw new UnsupportedOperationException("Not requested");
    }
}