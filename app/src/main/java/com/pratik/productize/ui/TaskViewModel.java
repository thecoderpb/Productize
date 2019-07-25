package com.pratik.productize.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pratik.productize.database.TaskRepository;
import com.pratik.productize.database.Tasks;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;
    private LiveData<List<Tasks>> tasks,homeTasks,workTasks,otherTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        repository = new TaskRepository(application);
        tasks = repository.getAllTasks();
        homeTasks = repository.getHomeTasks();
        workTasks = repository.getWorkTasks();
        otherTasks = repository.getOtherTasks();
    }

    public LiveData<List<Tasks>> getAllTasks(){return  tasks;}

    public LiveData<List<Tasks>> getWorkTasks() { return workTasks; }

    public LiveData<List<Tasks>> getHomeTasks(){ return homeTasks;}

    public LiveData<List<Tasks>> getOtherTasks() { return otherTasks; }

    public void insert(Tasks task){ repository.insert(task);}

    public void delete(Tasks task){ repository.delete(task);}
}