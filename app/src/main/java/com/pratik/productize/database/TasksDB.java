package com.pratik.productize.database;

import android.content.Context;


import androidx.annotation.NonNull;
import androidx.room.Database;


import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = Tasks.class,exportSchema = false,version = 1)
public abstract class TasksDB extends RoomDatabase {

    private static final String DB_NAME = "tasks_db";
    private static final Object LOCK = new Object();
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

    private static final Migration migration = new Migration(1,3){

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE TASKS ADD COLUMN isTaskPerformed INTEGER DEFAULT NULL");

        }
    };

    public abstract TaskDAO taskDAO();

}
