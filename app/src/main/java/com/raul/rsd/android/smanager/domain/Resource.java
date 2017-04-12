package com.raul.rsd.android.smanager.domain;

import io.realm.RealmObject;

import io.realm.annotations.PrimaryKey;

public class Resource extends RealmObject {

    // ------------------------- ATTRIBUTES --------------------------

    @PrimaryKey
    private long id;
    private String farmer_id;
    private String farm_name;
    private String phone1;
    private String zipcode;
    private Location location_1;

    // ------------------------- CONSTRUCTOR -------------------------

    public Resource() { }

    // ---------------------- GETTERS & SETTERS ----------------------


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public String getFarm_name() {
        return farm_name;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getZipcode() {
        return zipcode;
    }

    public Location getLocation() {
        return location_1;
    }
}
