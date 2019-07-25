package com.pratik.productize.asynchronous;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public void setAlarm(@NonNull Context context, int flag, int requestCode, String time){

        Intent intent = new Intent(context,Alarm.class);
        intent.setAction("com.pratik.productize.SET_ALARM");
        PendingIntent pi = PendingIntent.getBroadcast(context,requestCode,intent,flag);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            assert am != null;
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),pi);
        }else
            am.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),pi);

    }


    public void cancelAlarm(Context context,int requestCode,int flags){

        Intent intent = new Intent(context,Alarm.class);
        intent.setAction("com.pratik.productize.SET_ALARM");
        PendingIntent pi = PendingIntent.getBroadcast(context,requestCode,intent,flags);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
        pi.cancel();

    }

}
