package com.raul.rsd.android.smanager.managers;

import com.raul.rsd.android.smanager.domain.Task;
import com.raul.rsd.android.smanager.domain.User;
import com.raul.rsd.android.smanager.repositories.TaskRepository;
import com.raul.rsd.android.smanager.repositories.UserRepository;
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

    // ------------------------ Constructor --------------------------

    @Inject
    public DataManager(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // ---------------------------- Find -----------------------------

    // User

    public User findUserByName(String name){
        return userRepository.findByName(name);
    }

    public User findUserById(long id){
        return userRepository.findOne(id);
    }

    // ---------------------------- Save -----------------------------

    public void saveTask(Task task){
        final Task saved = taskRepository.save(task);

        // TODO assign to user => ASYNC
        RealmResults<User> users = userRepository.findUsersWithSkill(saved.getRequiredSkill());

        int aux, min = 0;
        long id = -1;
        for (User user : users) {
            aux = user.getTasks().sum("duration").intValue();
            if(aux > min) {
                min = aux;
                id = user.getId();
            }
        }

        if(id != -1){
            User user = userRepository.findOne(id);
            user.getTasks().add(task);
        } else
            throw new IllegalStateException("No user can perform that task");

    }

    // -------------------------- Use Cases --------------------------

}
