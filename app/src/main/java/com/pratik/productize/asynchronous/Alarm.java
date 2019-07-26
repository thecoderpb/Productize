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
import com.pratik.productize.ui.NotificationHandler;

import static com.pratik.productize.Utils.Constants.ALARM_INTENT_FLAG;
import static com.pratik.productize.Utils.Constants.ALARM_INTENT_RC;
import static com.pratik.productize.Utils.Constants.FLAG_ALARM1;
import static com.pratik.productize.Utils.Constants.FLAG_ALARM2;
import static com.pratik.productize.Utils.Constants.FLAG_ALARM3;
import static com.pratik.productize.Utils.Constants.REQUEST_CODE_ALARM1;
import static com.pratik.productize.Utils.Constants.REQUEST_CODE_ALARM2;
import static com.pratik.productize.Utils.Constants.REQUEST_CODE_ALARM3;
import static com.pratik.productize.Utils.Constants.TAG;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHandler notificationHandler = new NotificationHandler(context);
        notificationHandler.createNotification();
        cancelAlarm(context,intent.getIntExtra(ALARM_INTENT_RC,0),FLAG_ALARM1);

        if(intent.hasExtra(ALARM_INTENT_RC)){
            int reqCode = intent.getIntExtra(ALARM_INTENT_RC,-1);

            if(reqCode == REQUEST_CODE_ALARM1){
                notificationHandler.displayNotfication(FLAG_ALARM1);
                setAlarm(context,FLAG_ALARM2,REQUEST_CODE_ALARM2,System.currentTimeMillis() + 1000*60);
            }else if (reqCode == REQUEST_CODE_ALARM2){
                notificationHandler.displayNotfication(FLAG_ALARM2);
            }else if (reqCode == REQUEST_CODE_ALARM3){
                notificationHandler.displayNotfication(FLAG_ALARM3);
            }

        }else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Log.i(TAG,"boot set alarm");
        }



        Log.i(TAG,"alarm manager worked");
    }

    public void setAlarm(@NonNull Context context, int flag, int requestCode, long time){

        Intent intent = new Intent(context,Alarm.class);
        intent.setAction("com.pratik.productize.SET_ALARM");
        intent.putExtra(ALARM_INTENT_RC,requestCode);
        intent.putExtra(ALARM_INTENT_FLAG,flag);
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
        Log.i(TAG,"converted time " + unitConverters.time24HrToMillSec(time));
        return unitConverters.time24HrToMillSec(time);
    }


    public void cancelAlarm(Context context,int requestCode,int flags){

        Intent intent = new Intent(context,Alarm.class);
        intent.setAction("com.pratik.productize.SET_ALARM");
        PendingIntent pi = PendingIntent.getBroadcast(context,requestCode,intent,flags);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
        pi.cancel();

    }

    public void manageAlarm(Context context,int itemCount,String time) {

        if(itemCount != 0){
           // if(integer == 1){
                long convTime = convertedTime(time);
                setAlarm(context,FLAG_ALARM1,REQUEST_CODE_ALARM1,convTime);
           // }
        }else
            cancelAlarm(context,REQUEST_CODE_ALARM1,FLAG_ALARM1);
    }

}
