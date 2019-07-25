package com.pratik.productize.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = Tasks.class,exportSchema = false,version = 1)
public abstract class TasksDB extends RoomDatabase {

    public static final String DB_NAME = "tasks_db";
    public static final Object LOCK = new Object();
    private static TasksDB instance;

    public static synchronized TasksDB getInstance(Context context){

        if(instance == null){
            synchronized (LOCK){
                instance = Room.databaseBuilder(context.getApplicationContext(),TasksDB.class,DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return instance;
    }

    public abstract TaskDAO taskDAO();

}
