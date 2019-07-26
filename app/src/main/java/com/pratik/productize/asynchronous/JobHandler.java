package com.pratik.productize.asynchronous;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.pratik.productize.Utils.Constants;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.pratik.productize.Utils.Constants.JOB_FLEX_INTERVAL;
import static com.pratik.productize.Utils.Constants.JOB_ID_TASK;
import static com.pratik.productize.Utils.Constants.JOB_INTERVAL;

public class JobHandler extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(TAG,"job running");

        Log.i(TAG,"job finished running");
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
