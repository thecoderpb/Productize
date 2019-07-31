package com.pratik.productize.asynchronous;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pratik.productize.ui.NotificationHandler;
import com.pratik.productize.utils.Constants;

public class FCMService extends FirebaseMessagingService {

    public FCMService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String msg = remoteMessage.getFrom();
        if(msg != null)
            Log.i(Constants.TAG,msg);

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        NotificationHandler notificationHandler = new NotificationHandler(this);
        notificationHandler.createNotificationChannel();
        notificationHandler.displayNotification(notification);



    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.i("Token: ",s);

    }
}
