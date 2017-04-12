package com.raul.rsd.android.smanager.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject {

    // --------------------------- VALUES ----------------------------

    // ------------------------- ATTRIBUTES --------------------------

    @PrimaryKey
    private long id;
    private boolean completed;
    private Skill requiredSkill;
    private int duration;
    private String description;

    // ------------------------- CONSTRUCTOR -------------------------

    public Task() { }

    public Task(long id, String description, Skill requiredSkill, int duration) {
        this.id = id;
        this.requiredSkill = requiredSkill;
        this.duration = duration;
        this.description = description;
        completed = false;
    }

    public Task(String description, Skill requiredSkill, int duration) {
        this.requiredSkill = requiredSkill;
        this.duration = duration;
        this.description = description;
        completed = false;
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

    public int getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
