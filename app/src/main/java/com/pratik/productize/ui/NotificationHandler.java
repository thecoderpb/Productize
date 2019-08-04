package com.pratik.productize.ui;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.pratik.productize.R;
import com.pratik.productize.utils.PrefManager;
import com.pratik.productize.activites.MainActivity;
import com.pratik.productize.activites.WelcomeActivity;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;
import static com.pratik.productize.utils.Constants.CHANNEL_ID;
import static com.pratik.productize.utils.Constants.FLAG_ALARM1;
import static com.pratik.productize.utils.Constants.FLAG_ALARM2;
import static com.pratik.productize.utils.Constants.FLAG_ALARM3;
import static com.pratik.productize.utils.Constants.NOTIFICATION_ID;
import static com.pratik.productize.utils.Constants.TAG;

public class NotificationHandler {

    private NotificationCompat.Builder builder,builder2,builder3;
    private Context context;
    private PrefManager prefManager;

    public NotificationHandler(Context context){
        this.context = context;
        prefManager = new PrefManager(context);
    }

    public void createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

    }

    public void createNotification(){


        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground,3
                )
                .setContentTitle("Test Notification")
                .setContentText("Your productize hours starts in 5 mins")
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context,9,new Intent(context, WelcomeActivity.class),0))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        builder2 = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Test Notification")
                .setContentText("Looks like you have more tasks and less time. View or make changes to your day plans")
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context,9,new Intent(context, MainActivity.class),0))
                .setPriority(PRIORITY_DEFAULT);

        builder3 = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Test Notification")
                .setContentText("Get ready to tackle your first task. Tap to get started")
                .setAutoCancel(true)
                .setPriority(PRIORITY_DEFAULT)
                .setContentIntent(PendingIntent.getActivity(context, 9, new Intent(context,WelcomeActivity.class), 0));


    }

    public void displayNotification(int flag){

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        switch (flag){

            case FLAG_ALARM1 :
                notificationManager.notify(NOTIFICATION_ID,builder.build());
                break;
            case FLAG_ALARM2 :
                notificationManager.notify(NOTIFICATION_ID,builder3.build());
                prefManager.setTaskActive(true);
                break;
            case FLAG_ALARM3 :
                notificationManager.notify(NOTIFICATION_ID,builder2.build());
                break;

            default:
                Log.i(TAG,"error showing notification");
                break;
        }


    }

    public void displayNotification(RemoteMessage.Notification notification){

        String msg = notification.getBody();
        String title = notification.getTitle();
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setPriority(PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(85,builder.build());


    }
}
