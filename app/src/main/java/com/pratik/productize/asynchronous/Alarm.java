package com.pratik.productize.asynchronous;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.pratik.productize.Utils.Converters;

import static com.pratik.productize.Utils.Constants.FLAG_ALARM1;
import static com.pratik.productize.Utils.Constants.REQUEST_CODE_ALARM1;
import static com.pratik.productize.Utils.Constants.TAG;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG,"alarm manager worked");
    }

    public void setAlarm(@NonNull Context context, int flag, int requestCode, long time){

        Intent intent = new Intent(context,Alarm.class);
        intent.setAction("com.pratik.productize.SET_ALARM");
        PendingIntent pi = PendingIntent.getBroadcast(context,requestCode,intent,flag);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            assert am != null;
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time,pi);
        }else
            am.setExact(AlarmManager.RTC_WAKEUP,time,pi);

        Log.i(TAG,"alarm set");

    }

    private long convertedTime(String time) {

        Log.i(TAG,"in converted time");
        Converters unitConverters = new Converters();
        Log.i(TAG,"sys current time " + System.currentTimeMillis());
        Log.i(TAG,"converted time " + unitConverters.time24HrToMillsec(time));
        return unitConverters.time24HrToMillsec(time);
    }


    public void cancelAlarm(Context context,int requestCode,int flags){

        Intent intent = new Intent(context,Alarm.class);
        intent.setAction("com.pratik.productize.SET_ALARM");
        PendingIntent pi = PendingIntent.getBroadcast(context,requestCode,intent,flags);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
        pi.cancel();

    }

    public void manageAlarm(Context context,Integer integer,String time) {

        if(integer != 0){
           // if(integer == 1){
                long convTime = convertedTime(time);
                setAlarm(context,FLAG_ALARM1,REQUEST_CODE_ALARM1,convTime);
           // }
        }else
            cancelAlarm(context,REQUEST_CODE_ALARM1,FLAG_ALARM1);
    }

}
