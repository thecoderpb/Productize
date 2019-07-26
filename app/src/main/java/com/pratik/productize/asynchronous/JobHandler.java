package com.pratik.productize.asynchronous;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pratik.productize.Utils.Constants;
import com.pratik.productize.Utils.Converters;
import com.pratik.productize.Utils.PrefManager;
import com.pratik.productize.database.TasksDB;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.pratik.productize.Utils.Constants.FLAG_ALARM1;
import static com.pratik.productize.Utils.Constants.FLAG_ALARM3;
import static com.pratik.productize.Utils.Constants.JOB_FLEX_INTERVAL;
import static com.pratik.productize.Utils.Constants.JOB_ID_TASK;
import static com.pratik.productize.Utils.Constants.JOB_INTERVAL;
import static com.pratik.productize.Utils.Constants.REQUEST_CODE_ALARM1;
import static com.pratik.productize.Utils.Constants.REQUEST_CODE_ALARM3;
import static com.pratik.productize.Utils.Constants.TAG1;
import static com.pratik.productize.Utils.Constants.TAG_HOME;
import static com.pratik.productize.Utils.Constants.TAG_WORK;

public class JobHandler extends JobService {



    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(TAG,"job running");

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                PrefManager prefManager = new PrefManager(JobHandler.this);
                Alarm alarm = new Alarm();


                TasksDB tasksDB = TasksDB.getInstance(JobHandler.this);
                if(tasksDB.taskDAO().getActiveTaskCount() == 0 ){

                    alarm.cancelAlarm(JobHandler.this,REQUEST_CODE_ALARM1,FLAG_ALARM1);
                }

                long userSetHours = prefManager.getHours();
                long calculatedHours = tasksDB.taskDAO().getTaskDurationByTags(prefManager.getActiveTag());

                if(userSetHours < calculatedHours){
                    Converters converters = new Converters();
                    long time = converters.time24HrToMillSec(prefManager.getHomeTime());;
                    switch (prefManager.getActiveTag()){
                        case TAG_HOME :
                            time = converters.time24HrToMillSec(prefManager.getHomeTime());
                            break;
                        case TAG_WORK :
                            time = converters.time24HrToMillSec(prefManager.getWorkTime());
                    }

                    alarm.setAlarm(JobHandler.this,REQUEST_CODE_ALARM3,FLAG_ALARM3,time);
                }


                Log.i(TAG,"job finished running");
            }
        });

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        Log.i(TAG,"job stopped");
        return false;
    }

    public void scheduleJob(Context context,int JOB_ID) {

        ComponentName componentName = new ComponentName(context,JobHandler.class);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,componentName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setPersisted(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .setPeriodic(JOB_INTERVAL,JOB_FLEX_INTERVAL);
        }else {
            builder.setPersisted(true)
                    .setRequiresCharging(false)
                    .setRequiresDeviceIdle(false)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setMinimumLatency(JOB_FLEX_INTERVAL)
                    .setOverrideDeadline(JOB_INTERVAL);
        }

        jobScheduler.schedule(builder.build());

    }

    public void cancelJob(Context context,int JOB_ID){

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);

    }

    public void manageJob(Context context,Integer integer) {


        if(integer != 0){
            if(!isJobRunning(context,JOB_ID_TASK)){
                scheduleJob(context,JOB_ID_TASK);
                Log.i(Constants.TAG,"job started");
            }else
                Log.i(Constants.TAG,"job already running");
        }else {

            cancelJob(context,JOB_ID_TASK);
            Log.i(Constants.TAG,"job cancelled");
        }

    }

    private boolean isJobRunning(Context context,int JOB_ID) {

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        boolean hasBeenScheduled = false;

        if (jobScheduler != null) {
            for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()){
                if(jobInfo.getId() == JOB_ID)
                    hasBeenScheduled = true;
            }
        }

        return hasBeenScheduled;
    }
}
