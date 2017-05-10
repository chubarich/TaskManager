package com.danielkashin.taskorganiser.presentation_layer.view.important_tasks;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.ITaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.ImportantTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.domain_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetImportantTaskGroupUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_group.ITaskGroupAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_group.TaskGroupAdapter;
import com.danielkashin.taskorganiser.presentation_layer.application.ITasksLocalServiceProvider;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.important_tasks.IImportantTasksPresenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.important_tasks.ImportantTasksPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.ITagViewOpener;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IToolbarContainer;


public class ImportantTasksFragment extends PresenterFragment<ImportantTasksPresenter, IImportantTasksView>
    implements IImportantTasksView, ITaskGroupAdapter.Callbacks {

  private RecyclerView mRecyclerView;


  // ----------------------------------------- lifecycle ------------------------------------------

  @Override
  public void onStart() {
    super.onStart();

    ((IToolbarContainer) getActivity()).setToolbar(getString(R.string.drawer_important), false, false, false, false);

    ((IImportantTasksPresenter) getPresenter()).onGetTaskGroupData();
  }

  @Override
  public void onStop() {
    if (mRecyclerView.getAdapter() != null) {
      ((ITaskGroupAdapter) mRecyclerView.getAdapter()).detachCallbacks();
    }

    super.onStop();
  }

  // --------------------------------------- getInstance ------------------------------------------

  public static ImportantTasksFragment getInstance() {
    return new ImportantTasksFragment();
  }

  // --------------------------------- ITaskGroupAdapter.Callbacks --------------------------------

  @Override
  public void onCreateTask(String name, String UUID, ITaskGroup taskGroup) {
    ExceptionHelper.assertTrue("", taskGroup instanceof ImportantTaskGroup);

    Task task = new Task(name, UUID, Task.Type.NoDate, null);
    task.setImportant(true);

    ((IImportantTasksPresenter) getPresenter()).onSaveTask(task);
  }

  @Override
  public void onTaskChanged(Task task) {
    ((IImportantTasksPresenter) getPresenter()).onSaveTask(task);
  }

  @Override
  public void onTagClicked(String tagName) {
    ((ITagViewOpener) getActivity()).onTagClicked(tagName);
  }

  // ------------------------------------ IImportantTasksView -------------------------------------

  @Override
  public void addTaskToViewInterface(Task task) {
    if (mRecyclerView.getAdapter() != null) {
      ((ITaskGroupAdapter) mRecyclerView.getAdapter()).addTask(task);
    }
  }

  @Override
  public void initializeAdapter(ImportantTaskGroup taskGroup) {
    if (mRecyclerView.getAdapter() == null) {
      mRecyclerView.setAdapter(new TaskGroupAdapter(taskGroup));
    } else {
      ((ITaskGroupAdapter) mRecyclerView.getAdapter()).changeTaskGroup(taskGroup);
    }

    ((ITaskGroupAdapter) mRecyclerView.getAdapter()).attachCallbacks(this);
  }

  // ------------------------------------- PresenterFragment --------------------------------------

  @Override
  protected IImportantTasksView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<ImportantTasksPresenter, IImportantTasksView> getPresenterFactory() {
    ITasksLocalService tasksLocalService = ((ITasksLocalServiceProvider) getActivity()
        .getApplication())
        .getTasksLocalService();

    ITasksRepository tasksRepository = TasksRepository.Factory.create(tasksLocalService);

    GetImportantTaskGroupUseCase getTaskGroupUseCase = new GetImportantTaskGroupUseCase(
        tasksRepository,
        AsyncTask.THREAD_POOL_EXECUTOR);

    SaveTaskUseCase saveTaskUseCase = new SaveTaskUseCase(
        tasksRepository,
        AsyncTask.THREAD_POOL_EXECUTOR);

    return new ImportantTasksPresenter.Factory(getTaskGroupUseCase, saveTaskUseCase);
  }

  @Override
  protected int getFragmentId() {
    return this.getClass().getSimpleName().hashCode();
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_task_container;
  }

  @Override
  protected void initializeView(View view) {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setReverseLayout(true);
    layoutManager.setStackFromEnd(true);

    mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.addItemDecoration(new SpacingItemDecoration(0, 10));
    mRecyclerView.setNestedScrollingEnabled(false);
  }
}
