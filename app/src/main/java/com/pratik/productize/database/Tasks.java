package com.pratik.productize.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.pratik.productize.utils.Converters;

import java.sql.Date;

@Entity(tableName = "tasks")
public class Tasks {

    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo
    private String taskText;
    private int priority;
    private long duration;
    private int tags;
    private boolean isTaskComplete = false;
    private boolean isTaskExpired = false;
    @TypeConverters({Converters.class})
    private Date timeStamp;

    public Tasks(Date timeStamp,String taskText, int priority, long duration, int tags,boolean isTaskExpired, boolean isTaskComplete){

        this.timeStamp = timeStamp;
        this.taskText = taskText;
        this.priority = priority;
        this.duration = duration;
        this.tags = tags;
        this.isTaskComplete = isTaskComplete;
        this.isTaskExpired = isTaskExpired;

    }

    public Date date(){
        return getTimeStamp();
    }

    public long getId() {
        return id;
    }

    public String getTaskText() {
        return taskText;
    }

    public int getPriority() {
        return priority;
    }

    public long getDuration() {
        return duration;
    }

    public int getTags() {
        return tags;
    }

    public boolean isTaskComplete() {
        return isTaskComplete;
    }

    public boolean isTaskExpired() {
        return isTaskExpired;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
