package com.danielkashin.taskorganiser.data_layer.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.danielkashin.taskorganiser.data_layer.receivers.AlarmReceiver;


public class NotificationManager implements INotificationManager {

  public static final String KEY_NOTIFICATION_TEXT = "KEY_NOTIFICATION_TEXT";
  public static final String KEY_NOTIFICATION_UUID = "KEY_NOTIFICATION_UUID";
  public static final String KEY_NOTIFICATION_ACTION = "taskorganiser.intent.action.NORIFICATION";

  private Context context;
  private AlarmManager alarmManager;


  public NotificationManager(Context context) {
    this.context = context.getApplicationContext();
    this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
  }


  @Override
  public void registerAlarm(String UUID, long timestamp, String taskName) {
    PendingIntent pendingIntent = getPendingIntent(UUID, taskName);
    alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp * 1000, pendingIntent);
  }

  @Override
  public void unregisterAlarm(String UUID, String taskName) {
    PendingIntent pendingIntent = getPendingIntent(UUID, taskName);
    alarmManager.cancel(pendingIntent);
  }


  private PendingIntent getPendingIntent(String UUID, String taskName) {
    Bundle bundle = new Bundle();
    bundle.putString(KEY_NOTIFICATION_TEXT, taskName);
    bundle.putString(KEY_NOTIFICATION_UUID, UUID);

    Intent intent = new Intent(context, AlarmReceiver.class);
    intent.setAction(KEY_NOTIFICATION_ACTION);
    intent.putExtras(bundle);

    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, UUID.hashCode(), intent,
        PendingIntent.FLAG_ONE_SHOT);

    return pendingIntent;
  }
}
