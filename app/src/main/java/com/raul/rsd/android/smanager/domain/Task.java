package com.raul.rsd.android.smanager.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject {

    // --------------------------- VALUES ----------------------------

    // ------------------------- ATTRIBUTES --------------------------

    @PrimaryKey
    private long id;
    private Skill requiredSkill;
    private int duration;
    private String description;

    // ------------------------- CONSTRUCTOR -------------------------


    public Task() { }

    public Task(String description, Skill requiredSkill, int duration) {
        this.requiredSkill = requiredSkill;
        this.duration = duration;
        this.description = description;
    }

    // ---------------------- GETTERS & SETTERS ----------------------

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Skill getRequiredSkill() {
        return requiredSkill;
    }
    public void setRequiredSkill(Skill requiredSkill) {
        this.requiredSkill = requiredSkill;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
