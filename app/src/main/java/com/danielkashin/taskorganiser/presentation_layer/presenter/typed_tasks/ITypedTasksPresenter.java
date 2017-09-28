package com.danielkashin.taskorganiser.presentation_layer.presenter.typed_tasks;


import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.presentation_layer.view.typed_tasks.TypedTasksFragment;

public interface ITypedTasksPresenter {

  void onSaveTask(Task task);

  void onGetTaskGroupData(TypedTasksFragment.State.Type type);

}
