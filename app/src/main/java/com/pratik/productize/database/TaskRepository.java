package com.pratik.productize.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.pratik.productize.Utils.Constants;
import com.pratik.productize.asynchronous.AppExecutor;

import java.util.List;

public class TaskRepository {

    private TaskDAO taskDAO;
    private LiveData<List<Tasks>> allTasks;
    private TasksDB db;

    public TaskRepository(Application application){

        db = TasksDB.getInstance(application);

        taskDAO = db.taskDAO();
        allTasks = taskDAO.getAllActiveTasks();


    }

    public LiveData<List<Tasks>> getAllTasks(){
        return allTasks;
    }

    public void delete(final Tasks tasks){

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                taskDAO.deleteTask(tasks);
            }
        });
    }

    public void insert(final Tasks task){

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                taskDAO.insertTask(task);
            }
        });

        Log.i(Constants.TAG,"task inserted");

    }


}
