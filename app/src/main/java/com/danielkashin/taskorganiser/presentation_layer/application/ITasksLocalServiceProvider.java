package com.danielkashin.taskorganiser.presentation_layer.application;

import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;


public interface ITasksLocalServiceProvider {

  ITasksLocalService getTasksLocalService();

}
