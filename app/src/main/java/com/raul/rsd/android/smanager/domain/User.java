package com.raul.rsd.android.smanager.domain;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    // --------------------------- VALUES ----------------------------

    public static final int ADMIN = 0, TECH = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ADMIN, TECH})
    public @interface Type {}

    // ------------------------- ATTRIBUTES --------------------------

    @PrimaryKey
    private long id;
    private String name;
    private String password;
    private RealmList<Skill> skills;
    private RealmList<Task> tasks;
    @Type private int userType;

    // ------------------------- CONSTRUCTOR -------------------------


    public User() { }

    public User(long id, String name, String password, RealmList<Skill> skills, RealmList<Task> tasks, int userType) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.skills = skills;
        this.userType = userType;
        this.tasks = tasks;
    }

    public User(long id, String name, String password, RealmList<Skill> skills, int userType) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.skills = skills;
        this.userType = userType;

        tasks = new RealmList<>();
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public RealmList<Skill> getSkills() {
        return skills;
    }
    public void setSkills(RealmList<Skill> skills) {
        this.skills = skills;
    }

    public RealmList<Task> getTasks() {
        return tasks;
    }
    public void setTasks(RealmList<Task> tasks) {
        this.tasks = tasks;
    }

    @Type public int getUserType() {
        return userType;
    }
    public void setUserType(@Type int userType) {
        this.userType = userType;
    }
}
