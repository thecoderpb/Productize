package com.pratik.productize.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.pratik.productize.Utils.Constants;
import com.pratik.productize.asynchronous.AppExecutor;

import java.util.List;

import static com.pratik.productize.Utils.Constants.TAG_HOME;
import static com.pratik.productize.Utils.Constants.TAG_OTHER;
import static com.pratik.productize.Utils.Constants.TAG_WORK;

public class TaskRepository {

    private TaskDAO taskDAO;
    private LiveData<List<Tasks>> allTasks,homeTasks,workTasks,otherTasks;
    private LiveData<Integer> allTaskCount,homeTaskCount,workTaskCount,otherTaskCount;
    private LiveData<Long> allDurationCount,homeDurationCount,workDurationCount,otherDurationCount;

    public TaskRepository(Application application){

        TasksDB db = TasksDB.getInstance(application);

        taskDAO = db.taskDAO();
        allTasks = taskDAO.getAllActiveTasks();
        homeTasks = taskDAO.getSortedTaskByTag(TAG_HOME);
        workTasks = taskDAO.getSortedTaskByTag(TAG_WORK);
        otherTasks = taskDAO.getSortedTaskByTag(TAG_OTHER);

        allTaskCount = taskDAO.getAllActiveTaskCount();
        homeTaskCount = taskDAO.getTaskCountByTag(TAG_HOME);
        workTaskCount = taskDAO.getTaskCountByTag(TAG_WORK);
        otherTaskCount = taskDAO.getTaskCountByTag(TAG_OTHER);

        allDurationCount = taskDAO.getAllTaskDuration();
        homeDurationCount = taskDAO.getTaskDurationByTag(TAG_HOME);
        workDurationCount = taskDAO.getTaskDurationByTag(TAG_WORK);
        otherDurationCount = taskDAO.getTaskDurationByTag(TAG_OTHER);

    }

    public LiveData<List<Tasks>> getAllTasks(){
        return allTasks;
    }

    public LiveData<List<Tasks>> getHomeTasks() { return homeTasks; }

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
