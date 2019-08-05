package com.pratik.productize.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.pratik.productize.utils.Constants;
import com.pratik.productize.asynchronous.AppExecutor;

import java.util.List;

import static com.pratik.productize.utils.Constants.TAG_HOME;
import static com.pratik.productize.utils.Constants.TAG_OTHER;
import static com.pratik.productize.utils.Constants.TAG_WORK;

public class TaskRepository {

    private TaskDAO taskDAO;
    private LiveData<List<Tasks>> allTasks,homeTasks,workTasks,otherTasks,completeTasks,incompleteTasks;
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

        completeTasks = taskDAO.getCompleteTasks();
        incompleteTasks = taskDAO.getIncompleteTasks();

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


    public LiveData<List<Tasks>> getCompleteTasks() {
        return completeTasks;
    }

    public LiveData<List<Tasks>> getIncompleteTasks() {
        return incompleteTasks;
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

    public void update(final Tasks task){

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                taskDAO.updateTask(task);

                Log.i(Constants.TAG,"task updated");
            }
        });
    }

    public void updateTask(final long id, final String text, final int priority, final long duration){

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                taskDAO.updateEditTask(text,priority,duration,id);
            }
        });
    }

    public LiveData<Tasks> getTaskById(final long id,Application application){
        TasksDB db = TasksDB.getInstance(application);

        TaskDAO taskDAO = db.taskDAO();
        return taskDAO.getActiveTask(id);


    }

    public void updateTaskPerformed(final long id , final boolean flag){

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(flag)
                    taskDAO.updateTaskExpiry(id,0);
                else
                    taskDAO.updateTaskExpiry(id,1);
            }
        });
    }


    public void updateTaskCompleted(final long id, final boolean flag) {

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(flag)
                    taskDAO.updateTaskCompleted(id,1);
                else
                    taskDAO.updateTaskCompleted(id,0);
            }
        });
    }
}
