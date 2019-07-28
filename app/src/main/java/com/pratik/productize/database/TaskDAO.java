package com.pratik.productize.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
    LiveData<List<Tasks>> getActiveTask(int id);

    @Query("SELECT * FROM TASKS WHERE tags=:tag AND isTaskExpired = 0 AND isTaskComplete = 0 ORDER BY priority DESC")
    LiveData<List<Tasks>> getTaskByTag(int tag);

    @Query("SELECT * FROM TASKS WHERE tags=:tag AND isTaskExpired = 0 AND isTaskComplete = 0 ORDER BY priority DESC, duration DESC ")
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

    @Query("UPDATE TASKS SET isTaskExpired = 1 WHERE id =:ids")
    void updateTaskExpiryTrue(long ids);




}
