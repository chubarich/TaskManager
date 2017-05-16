package com.danielkashin.taskorganiser.data_layer.managers;


public interface INotificationManager {

  void registerAlarm(String UUID, long timestamp, String taskName);

  void unregisterAlarm(String UUID, String taskName);

}
