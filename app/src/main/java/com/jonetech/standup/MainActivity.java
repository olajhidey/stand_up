package com.jonetech.standup;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private NotificationManager fNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String NOTIFICATION_CHANNEL = "primary_notification_channell";
    private AlarmManager fAlarmManager;
    private Button fButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        fButton = findViewById(R.id.button);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            fButton.setVisibility(View.VISIBLE);

        } else {
            fButton.setVisibility(View.GONE);
        }

        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);

        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);

        alarmToggle.setChecked(alarmUp);

        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        fAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String toastMessage;

                if (isChecked) {

                    final long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                    final long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

                    if (fAlarmManager != null) {
                        fAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, repeatInterval, notifyPendingIntent);
                    }


                    //set the toast message for the "oN" case.
                    toastMessage = getString(R.string.toast_message_on);

                } else {

                    fNotificationManager.cancelAll();

                    if (fAlarmManager != null) {
                        fAlarmManager.cancel(notifyPendingIntent);
                    }

                    //set the toast message for the "off" case.
                    toastMessage = getString(R.string.toast_message_off);
                }

                // Show a toast to say the alarm is turned on or off
                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                ;
            }
        });

        // Initialize Notification channel for android Oreo and the likes

        createNotification();

    }

    /**
     * CREATE A NOTIFICATION FOR ANDROID OREO AND HIGHER
     */

    public void createNotification() {

        // Create a notification manager object
        fNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in Oreos and Higher
        // So check for android build and os

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the Notification Channel with all the parameters

            NotificationChannel mNotificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL, "Stand up! Notification", NotificationManager.IMPORTANCE_HIGH);

            mNotificationChannel.enableLights(true);
            mNotificationChannel.setLightColor(Color.RED);
            mNotificationChannel.enableVibration(true);
            mNotificationChannel.setDescription("Notifies every 15 minutes to stand up and walk!");

            // Use the member variable of the NotificationManger to create the channel
            fNotificationManager.createNotificationChannel(mNotificationChannel);
        }

    }

    public void checkAlarmList(View view) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            String toastMessage = String.valueOf(fAlarmManager.getNextAlarmClock().getTriggerTime());

            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "This feature is not available for your OS", Toast.LENGTH_SHORT).show();
        }


    }
}
