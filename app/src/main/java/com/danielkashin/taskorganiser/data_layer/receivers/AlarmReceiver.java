package com.danielkashin.taskorganiser.data_layer.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.presentation_layer.presenter.main_drawer.MainDrawerPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.MainDrawerActivity;

import static com.danielkashin.taskorganiser.data_layer.managers.NotificationManager.KEY_NOTIFICATION_TEXT;
import static com.danielkashin.taskorganiser.data_layer.managers.NotificationManager.KEY_NOTIFICATION_UUID;


public class AlarmReceiver extends WakefulBroadcastReceiver {

  private static final int NOTIFICATION_SIGNAL_LENGTH = 100;


  @Override
  public void onReceive(Context context, Intent intent) {
    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    vibrator.vibrate(NOTIFICATION_SIGNAL_LENGTH);


    String contentText = (intent.getExtras().containsKey(KEY_NOTIFICATION_TEXT)
        && intent.getStringExtra(KEY_NOTIFICATION_TEXT) != null)
        ? intent.getStringExtra(KEY_NOTIFICATION_TEXT)
        : context.getString(R.string.notification_default);

    String contentUUID = (intent.getExtras().containsKey(KEY_NOTIFICATION_UUID)
        && intent.getStringExtra(KEY_NOTIFICATION_UUID) != null)
        ? intent.getStringExtra(KEY_NOTIFICATION_UUID)
        : contentText;

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
        .setSmallIcon(R.mipmap.ic_notification)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(contentText);

    Intent resultIntent = new Intent(context, MainDrawerActivity.class);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addParentStack(MainDrawerActivity.class);
    stackBuilder.addNextIntent(resultIntent);

    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
    builder.setContentIntent(resultPendingIntent);

    NotificationManager notificationManager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(contentUUID.hashCode(), builder.build());
  }
}
