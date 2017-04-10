package com.raul.rsd.android.smanager.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Skill extends RealmObject {

    // --------------------------- VALUES ----------------------------


    // ------------------------- ATTRIBUTES --------------------------

    @PrimaryKey
    private long id;
    private String name;

    // ------------------------- CONSTRUCTOR -------------------------


    public Skill() { }

    public Skill(long id, String name) {
        this.id = id;
        this.name = name;
    }

    // ---------------------- GETTERS & SETTERS ----------------------

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
