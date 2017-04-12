package com.raul.rsd.android.smanager.managers;

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

    public void changeTaskCompletion(long taskId){
        taskRepository.changeTaskCompletion(taskId);
    }

}
