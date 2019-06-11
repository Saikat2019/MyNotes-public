package com.kajal.mynotes.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kajal.mynotes.R;
import com.kajal.mynotes.ui.NotificationsFragment;

public class FcmNotificationService extends FirebaseMessagingService {

    public static int notification_id = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);

        generateFcmNotification(remoteMessage.getNotification().getBody(),
                remoteMessage.getNotification().getTitle());

    }

    private void generateFcmNotification(String body, String title) {

        Intent intent = new Intent(this, NotificationsFragment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,
                                                            intent,PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(notification_id > 1073741824){
            notification_id = 0;
        }

        notificationManager.notify(notification_id ++,notificationBuilder.build());

    }
}
