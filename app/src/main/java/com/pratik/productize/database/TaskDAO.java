package com.pratik.productize.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.google.android.gms.tasks.Task;

import java.util.List;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM TASKS WHERE isTaskExpired = 0 ORDER BY priority DESC ")
    LiveData<List<Tasks>> getAllActiveTasks();

    @Insert
    void insertTask(Tasks task);
    @Update
    void updateTask(Tasks tasks);
    @Delete
    void deleteTask(Tasks task);

    @Query("SELECT * FROM TASKS WHERE id=:id AND isTaskExpired = 0 AND isTaskComplete = 0")
    LiveData<Tasks> getActiveTask(long id);

    @Query("SELECT * FROM TASKS WHERE isTaskExpired = 1 AND isTaskComplete = 0 ORDER BY timeStamp")
    LiveData<List<Tasks>> getIncompleteTasks();

    @Query("SELECT * FROM TASKS WHERE isTaskExpired = 1 AND isTaskComplete = 1 ORDER BY timeStamp")
    LiveData<List<Tasks>> getCompleteTasks();

    @Query("SELECT * FROM TASKS WHERE tags=:tag AND isTaskExpired = 0 AND isTaskComplete = 0  ORDER BY priority DESC, duration DESC ")
    LiveData<List<Tasks>> getSortedTaskByTag(int tag);

    @Query("SELECT COUNT(taskText) FROM TASKS WHERE isTaskExpired = 0")
    LiveData<Integer> getAllActiveTaskCount();

    @Query("SELECT COUNT(taskText) FROM TASKS WHERE isTaskExpired = 0")
    int getActiveTaskCount();

    @Query("SELECT COUNT(*) FROM TASKS WHERE tags=:tag AND isTaskExpired = 0")
    LiveData<Integer> getTaskCountByTag(int tag);

    @Query("SELECT SUM(duration) FROM TASKS WHERE isTaskExpired = 0")
    LiveData<Long> getAllTaskDuration();

    @Query("SELECT SUM(duration) FROM TASKS WHERE tags=:tag AND isTaskExpired = 0")
    LiveData<Long> getTaskDurationByTag(int tag);

    @Query("SELECT SUM(duration) FROM TASKS WHERE tags=:tag AND isTaskExpired = 0")
    long getTaskDurationByTags(int tag);

    @Query("DELETE FROM TASKS")
    void nukeTable();

    @Query("UPDATE TASKS SET isTaskExpired =:flag  WHERE id =:ids")
    void updateTaskExpiry(long ids, int flag);

    @Query("UPDATE TASKS SET isTaskComplete =:flag WHERE id =:ids")
    void updateTaskCompleted(long ids,int flag);

    @Query("UPDATE TASKS SET taskText =:taskText , priority =:priority , duration =:duration WHERE id =:id")
    void updateEditTask(String taskText,int priority,long duration,long id);




}
