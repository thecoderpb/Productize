package com.pratik.productize.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static com.pratik.productize.utils.Constants.ACTIVE_TAG;
import static com.pratik.productize.utils.Constants.HOME_TIME;
import static com.pratik.productize.utils.Constants.IS_FIRST_TIME_LAUNCH;
import static com.pratik.productize.utils.Constants.IS_TASK_ACTIVE;
import static com.pratik.productize.utils.Constants.IS_TASK_ONGOING;
import static com.pratik.productize.utils.Constants.IS_TASK_SCHEDULED;
import static com.pratik.productize.utils.Constants.PREF_NAME;
import static com.pratik.productize.utils.Constants.TAG_HOME;
import static com.pratik.productize.utils.Constants.TASK_HOURS;
import static com.pratik.productize.utils.Constants.WORK_TIME;

public class PrefManager {


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setTaskActive(boolean isTaskActive){
        editor.putBoolean(IS_TASK_ACTIVE,isTaskActive);
        editor.commit();

    }

    public void setTaskScheduled(boolean taskScheduled){
        editor.putBoolean(IS_TASK_SCHEDULED,taskScheduled);
        editor.commit();
    }

    public void setActiveTag(int activeTag){
        editor.putInt(ACTIVE_TAG,activeTag);
        editor.commit();
    }

    public void setHomeTime(String time){
        editor.putString(HOME_TIME,time);
        editor.commit();
    }

    public void setWorkTime(String time){
        editor.putString(WORK_TIME,time);
        editor.commit();
    }

    public void setHours(long hours){
        editor.putLong(TASK_HOURS,hours);
        editor.commit();
    }

    public void setTaskOngoing(boolean ongoing){
        editor.putBoolean(IS_TASK_ONGOING,ongoing);
        editor.commit();
    }

    public boolean isTaskOngoing(){return pref.getBoolean(IS_TASK_ONGOING,false);}
    public boolean isTaskScheduled(){return pref.getBoolean(IS_TASK_SCHEDULED,false);}
    public boolean isTaskActive(){ return pref.getBoolean(IS_TASK_ACTIVE,false);}
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
    public int getActiveTag(){return  pref.getInt(ACTIVE_TAG,TAG_HOME);}
    public String getHomeTime(){return pref.getString(HOME_TIME,"");}
    public String getWorkTime(){return pref.getString(WORK_TIME,"");}
    public long getHours() { return pref.getLong(TASK_HOURS,0);} }
