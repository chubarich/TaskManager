package com.danielkashin.taskorganiser.di.module;

import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteDoneTasksUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteTagUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteTaskUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetDateTypeTaskGroupsUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetNotificationTasksUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTagTaskGroupUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTagsUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTaskWithAllTagsUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTypedTaskGroupUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTagUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;

import java.util.concurrent.Executor;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;


@Module
public class UseCaseModule {

  @Provides
  public DeleteDoneTasksUseCase provideDeleteDoneTasksUseCase(ITasksRepository tasksRepository, Executor executor) {
    return new DeleteDoneTasksUseCase(tasksRepository, executor);
  }

  @Provides
  public DeleteTagUseCase provideDeleteTagUseCase(ITasksRepository tasksRepository, Executor executor) {
    return new DeleteTagUseCase(tasksRepository, executor);
  }

  @Provides
  public DeleteTaskUseCase provideDeleteTaskUseCase(ITasksRepository tasksRepository, Executor executor) {
    return new DeleteTaskUseCase(tasksRepository, executor);
  }

  @Provides
  public GetDateTypeTaskGroupsUseCase provideGetDateTypeTaskGroupsUseCase(
      ITasksRepository tasksRepository, Executor executor) {
    return new GetDateTypeTaskGroupsUseCase(tasksRepository, executor);
  }

  @Provides
  public GetNotificationTasksUseCase provideGetNotificationTasksUseCase(
      ITasksRepository tasksRepository, Executor executor) {
    return new GetNotificationTasksUseCase(tasksRepository, executor);
  }

  @Provides
  public GetTagsUseCase provideGetTagsUseCase(ITasksRepository tasksRepository, Executor executor) {
    return new GetTagsUseCase(tasksRepository, executor);
  }

  @Provides
  public GetTagTaskGroupUseCase provideGetTagTaskGroupUseCase(ITasksRepository tasksRepository, Executor executor) {
    return new GetTagTaskGroupUseCase(tasksRepository, executor);
  }

  @Provides
  public GetTaskWithAllTagsUseCase provideGetTaskWithAllTagsUseCase(ITasksRepository tasksRepository, Executor executor) {
    return new GetTaskWithAllTagsUseCase(tasksRepository, executor);
  }

  @Provides
  public GetTypedTaskGroupUseCase provideGetTypedTaskGroupUseCase(ITasksRepository tasksRepository, Executor executor) {
    return new GetTypedTaskGroupUseCase(tasksRepository, executor);
  }

  @Provides
  public SaveTagUseCase provideSaveTagUseCase(ITasksRepository tasksRepository, Executor executor) {
    return new SaveTagUseCase(tasksRepository, executor);
  }

  @Provides
  public SaveTaskUseCase provideSaveTaskUseCase(ITasksRepository tasksRepository, Executor executor) {
    return new SaveTaskUseCase(tasksRepository, executor);
  }

}
