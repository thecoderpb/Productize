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

    @Query("SELECT * FROM TASKS WHERE isTaskExpired = 0 ")
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

    @Query("SELECT COUNT(*) FROM TASKS WHERE isTaskExpired = 0")
    int getAllActiveTaskCount();

    @Query("SELECT COUNT(*) FROM TASKS WHERE tags=:tag AND isTaskExpired = 0")
    int getTaskCountByTag(int tag);

    @Query("SELECT SUM(duration) FROM TASKS WHERE isTaskExpired = 0")
    int getAllTaskDuration();

    @Query("SELECT SUM(duration) FROM TASKS WHERE tags=:tag AND isTaskExpired = 0")
    int getTaskDurationByTag(int tag);


}
