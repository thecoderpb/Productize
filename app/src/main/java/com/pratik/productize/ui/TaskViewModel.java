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
    private LiveData<List<Tasks>> tasks,homeTasks,workTasks,otherTasks,completeTasks,incompleteTasks;
    private LiveData<Integer> allTaskCount,homeTaskCount,workTaskCount,otherTaskCount;
    private LiveData<Long> allDurationCount,homeDurationCount,workDurationCount,otherDurationCount;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        repository = new TaskRepository(application);

        tasks = repository.getAllTasks();
        homeTasks = repository.getHomeTasks();
        workTasks = repository.getWorkTasks();
        otherTasks = repository.getOtherTasks();

        allTaskCount = repository.getAllTaskCount();
        homeTaskCount = repository.getHomeTaskCount();
        workTaskCount = repository.getWorkTaskCount();
        otherTaskCount = repository.getOtherTaskCount();

        allDurationCount = repository.getAllDurationCount();
        homeDurationCount = repository.getHomeDurationCount();
        workDurationCount = repository.getWorkDurationCount();
        otherDurationCount = repository.getOtherDurationCount();

        completeTasks = repository.getCompleteTasks();
        incompleteTasks = repository.getIncompleteTasks();


    }

    public LiveData<List<Tasks>> getAllTasks(){return  tasks;}

    public LiveData<List<Tasks>> getHomeTasks(){ return homeTasks;}

    public LiveData<List<Tasks>> getWorkTasks() { return workTasks; }

    public LiveData<List<Tasks>> getOtherTasks() { return otherTasks; }



    public LiveData<Integer> getAllTaskCount() { return allTaskCount; }

    public LiveData<Integer> getHomeTaskCount() { return homeTaskCount; }

    public LiveData<Integer> getWorkTaskCount() { return workTaskCount; }

    public LiveData<Integer> getOtherTaskCount() { return otherTaskCount; }



    public LiveData<Long> getAllDurationCount() { return allDurationCount; }

    public LiveData<Long> getHomeDurationCount() { return homeDurationCount; }

    public LiveData<Long> getWorkDurationCount() { return workDurationCount; }

    public LiveData<Long> getOtherDurationCount() { return otherDurationCount; }


    public LiveData<List<Tasks>> getCompleteTasks() { return completeTasks; }

    public LiveData<List<Tasks>> getIncompleteTasks() { return incompleteTasks; }



    public void insert(Tasks task){ repository.insert(task);}

    public void update(Tasks task){ repository.update(task);}

    public void updateTaskPerformed(long id,boolean flag){ repository.updateTaskPerformed(id,flag);}

    public void delete(Tasks task){ repository.delete(task);}

    public LiveData<Tasks> getTask(long id){ return repository.getTaskById(id,getApplication());}

    public void updateTaskCompleted(long id, boolean flag) { repository.updateTaskCompleted(id,flag);}
}
