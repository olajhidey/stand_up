package com.jonetech.standup;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager fNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String NOTIFICATION_CHANNEL = "primary_notification_channell";

    @Override
    public void onReceive(Context context, Intent intent) {

        fNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        // Deliver the Notification
        deliverNotification(context);

    }

    private void deliverNotification(Context context) {
        // Notification content intent
        Intent mIntent = new Intent(context, MainActivity.class);


        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setBadgeIconType(R.drawable.ic_stand_up)
                .setContentTitle(context.getString(R.string.notification_content_title))
                .setContentText(context.getString(R.string.notification_content_text))
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        fNotificationManager.notify(NOTIFICATION_ID, builder.build());


    }
}
