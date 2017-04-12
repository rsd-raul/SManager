package com.raul.rsd.android.smanager.repositories;

import com.raul.rsd.android.smanager.domain.Resource;
import com.raul.rsd.android.smanager.helpers.PrimaryKeyHelper;
import java.util.List;
import javax.inject.Inject;
import io.realm.Realm;
import io.realm.RealmResults;

public class ResourceRepository implements BaseRepository<Resource> {

    // -------------------------- Injected ---------------------------

    private Realm realm;

    // ------------------------ Constructor --------------------------

    @Inject
    ResourceRepository(Realm realm) {
        this.realm = realm;
    }

    // ---------------------------- Find -----------------------------

    @Override
    public Resource findOne(long id) {
        return realm.where(Resource.class).equalTo("id", id).findFirst();
    }

    @Override
    public RealmResults<Resource> findAll() {
        return realm.where(Resource.class).findAll();
    }

    // ---------------------------- Save -----------------------------

    @Override
    public Resource save(final Resource resource) {
        Resource managedResource;

        realm.beginTransaction();
        if (resource.getId() == 0)
            resource.setId(PrimaryKeyHelper.nextKey());

        managedResource = realm.copyToRealmOrUpdate(resource);
        realm.commitTransaction();

        return managedResource;
    }

    public void saveResources(final List<Resource> resources) {
        realm.beginTransaction();

        realm.where(Resource.class).findAll().deleteAllFromRealm();

        for (Resource resource : resources) {
            resource.setId(PrimaryKeyHelper.nextKey());
            realm.copyToRealmOrUpdate(resource);
        }

        realm.commitTransaction();
    }
}