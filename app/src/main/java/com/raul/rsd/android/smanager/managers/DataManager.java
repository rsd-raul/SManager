package com.raul.rsd.android.smanager.managers;

import android.util.Log;

import com.raul.rsd.android.smanager.domain.Resource;
import com.raul.rsd.android.smanager.domain.Skill;
import com.raul.rsd.android.smanager.domain.Task;
import com.raul.rsd.android.smanager.domain.User;
import com.raul.rsd.android.smanager.repositories.ResourceRepository;
import com.raul.rsd.android.smanager.repositories.SkillRepository;
import com.raul.rsd.android.smanager.repositories.TaskRepository;
import com.raul.rsd.android.smanager.repositories.UserRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import io.realm.RealmResults;

@Singleton
public class DataManager {

    // --------------------------- Values ----------------------------

    private static final String TAG = "MainActivity";

    // -------------------------- Injected ---------------------------
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private SkillRepository skillRepository;
    private ResourceRepository resourceRepository;

    // ------------------------ Constructor --------------------------

    @Inject
    public DataManager(TaskRepository taskRepository, UserRepository userRepository,
                       SkillRepository skillRepository, ResourceRepository resourceRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.resourceRepository = resourceRepository;
    }

    // ---------------------------- Find -----------------------------

    // User

    public User findUserByName(String name){
        return userRepository.findByName(name);
    }

    public User findUserById(long id){
        return userRepository.findOne(id);
    }

    public RealmResults<Skill> findAllSkills(){
        return skillRepository.findAll();
    }

    public RealmResults<Resource> findAllResources(){
        return resourceRepository.findAll();
    }

//    public TaskList findTaskListById(long id){
//        return taskListRepository.findOne(id);
//    }
//
//    public TaskList findTaskListByPosition(int position){
//        return taskListRepository.findByPosition(position);
//    }
//
//    public int findTaskListPositionById(int id){
//        RealmResults<TaskList> taskLists = taskListRepository.findAll();
//
//        // Gives you the position of a TaskList based on its id
//        for (int position = 0; position < taskLists.size(); position++)
//            if(taskLists.get(position).getId() == id)
//                return position;
//
//        return -1;
//    }
//
//    // Task
//
//    public Task findTaskById(long id){
//        return taskRepository.findOne(id);
//    }
//
//    public RealmList<Task> findAllTasksByTaskListPosition(int position){
//        return taskRepository.findAllByTaskListPosition(position);
//    }
//
//    public RealmResults<Task> findAllCompletedTasks(){
//        return taskRepository.findAll().where().equalTo("completed", true).findAll();
//    }
//
//    public RealmResults<Task> findAllSnoozedTasks(){
//        return taskRepository.findAll().where().isNotNull("due").findAll();
//    }
//
//    // Label
//
//    public RealmResults<Label> findAllLabels(){
//        return labelRepository.findAll();
//    }
//
//    public Label findLabelById(long id){
//        return labelRepository.findOne(id);
//    }
//
//    // Location
//
//    public RealmResults<Location> findAllLocations(){
//        return locationRepository.findAll();
//    }
//
//    public Location findLocationById(long id){
//        return locationRepository.findOne(id);
//    }
//
    // ---------------------------- Save -----------------------------

    public Task saveTask(final Task task){
        final Task saved = taskRepository.save(task);

        int aux, min = Integer.MAX_VALUE;
        long id = -1;
        for (User user : userRepository.findUsersWithSkill(saved.getRequiredSkill())) {
            aux = user.getTasks().sum("duration").intValue();
            if(aux < min) {
                min = aux;
                id = user.getId();
            }
        }

        if(id != -1) {
            userRepository.addTask(id, task);
            userRepository.addTaskToAdmin(task);
        } else
            throw new IllegalStateException("No user can perform that task");

        return task;
    }

    public void saveResources(List<Resource> resources){
        resourceRepository.saveResources(resources);
    }

    // -------------------------- Use Cases --------------------------

}
