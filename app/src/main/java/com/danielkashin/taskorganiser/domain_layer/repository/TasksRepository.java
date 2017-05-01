package com.danielkashin.taskorganiser.domain_layer.repository;


import com.danielkashin.taskorganiser.data_layer.services.translate.local.ITasksLocalService;

public class TasksRepository {

  private ITasksLocalService tasksLocalService;

  public TasksRepository(ITasksLocalService tasksLocalService) {
    if (tasksLocalService == null) {
      throw new IllegalStateException("All repository arguments must be non null");
    }

    this.tasksLocalService = tasksLocalService;
  }



}
