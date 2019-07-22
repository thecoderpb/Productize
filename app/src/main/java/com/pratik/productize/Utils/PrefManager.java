package com.pratik.productize.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.pratik.productize.Utils.Constants.IS_FIRST_TIME_LAUNCH;
import static com.pratik.productize.Utils.Constants.IS_TASK_ACTIVE;
import static com.pratik.productize.Utils.Constants.IS_TASK_SCHEDULED;
import static com.pratik.productize.Utils.Constants.PREF_NAME;

public class PrefManager {


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;


    // Shared preferences file name


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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

    public boolean isTaskScheduled(){return pref.getBoolean(IS_TASK_SCHEDULED,false);}
    public boolean isTaskActive(){ return pref.getBoolean(IS_TASK_ACTIVE,false);}
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
