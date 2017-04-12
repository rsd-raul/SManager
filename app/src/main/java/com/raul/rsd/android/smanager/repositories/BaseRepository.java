package com.raul.rsd.android.smanager.repositories;

import io.realm.RealmObject;
import io.realm.RealmResults;

interface BaseRepository<V extends RealmObject> {

    V findOne(long id);

    RealmResults<V> findAll();

    V save(V domain);
}
