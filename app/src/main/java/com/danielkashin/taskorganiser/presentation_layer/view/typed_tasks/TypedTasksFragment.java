package com.danielkashin.taskorganiser.presentation_layer.view.typed_tasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTypedTaskGroupUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.typed_tasks.ITypedTasksPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.ITaskViewOpener;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.ITaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.domain_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_group.ITaskGroupAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_group.TaskGroupAdapter;
import com.danielkashin.taskorganiser.presentation_layer.application.ITasksLocalServiceProvider;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.typed_tasks.TypedTasksPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.ITagViewOpener;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IToolbarContainer;


public class TypedTasksFragment extends PresenterFragment<TypedTasksPresenter, ITypedTasksView>
    implements ITypedTasksView, ITaskGroupAdapter.Callbacks {

  private RecyclerView mRecyclerView;
  private State mRestoredState;

  // --------------------------------------- getInstance ------------------------------------------

  public static TypedTasksFragment getInstance(State.Type type) {
    TypedTasksFragment fragment = new TypedTasksFragment();

    Bundle arguments = State.wrap(type);
    fragment.setArguments(arguments);

    return fragment;
  }

  // ----------------------------------------- lifecycle ------------------------------------------


  @Override
  public void onCreate(Bundle savedInstanceState) {
    mRestoredState = new State();
    mRestoredState.initializeWithBundle(savedInstanceState);
    if (!mRestoredState.isInitialized()) {
      mRestoredState.initializeWithBundle(getArguments());
    }
    ExceptionHelper.assertTrue("Fragment state must be initialized", mRestoredState.isInitialized());

    super.onCreate(savedInstanceState);
  }

  @Override
  public void onStart() {
    super.onStart();

    if (mRestoredState.getType() == State.Type.NoDate) {
      ((IToolbarContainer) getActivity()).setToolbar(getString(R.string.drawer_no_date), false, false, false, false);
    } else if (mRestoredState.getType() == State.Type.Important) {
      ((IToolbarContainer) getActivity()).setToolbar(getString(R.string.drawer_important), false, false, false, false);
    } else if (mRestoredState.getType() == State.Type.Done) {
      ((IToolbarContainer) getActivity()).setToolbar(getString(R.string.drawer_done), false, false, false, true);
    }

    ((TypedTasksPresenter)getPresenter()).onGetTaskGroupData();
  }

  @Override
  public void onStop() {
    if (mRecyclerView.getAdapter() != null) {
      ((ITaskGroupAdapter) mRecyclerView.getAdapter()).detachCallbacks();
    }

    super.onStop();
  }

  // --------------------------------- ITaskGroupAdapter.Callbacks --------------------------------

  @Override
  public void onCreateTask(String name, String UUID, ITaskGroup taskGroup) {
    Task task;
    if (mRestoredState.getType() == State.Type.NoDate) {
      task = new Task(name, UUID, Task.Type.NoDate, null);
    } else if (mRestoredState.getType() == State.Type.Important) {
      task = new Task(name, UUID, Task.Type.NoDate, null);
      task.setImportant(true);
    } else {
      throw new IllegalStateException("");
    }

    ((ITypedTasksPresenter) getPresenter()).onSaveTask(task);
  }

  @Override
  public void onTaskChanged(Task task) {
    ((ITypedTasksPresenter) getPresenter()).onSaveTask(task);
  }

  @Override
  public void onTagClicked(String tagName) {
    ((ITagViewOpener) getActivity()).onOpenTagView(tagName);
  }

  @Override
  public void onTaskClicked(Task task) {
    ((ITaskViewOpener) getActivity()).onOpenTaskView(task.getType(), task.getUUID());
  }

  // ------------------------------------- INoDateTaskView ----------------------------------------


  @Override
  public State.Type getType() {
    return mRestoredState.getType();
  }

  @Override
  public void addTaskToViewInterface(Task task) {
    if (mRecyclerView.getAdapter() != null) {
      ((ITaskGroupAdapter) mRecyclerView.getAdapter()).addTask(task);
    }
  }

  @Override
  public void initializeAdapter(ITaskGroup taskGroup) {
    if (mRecyclerView.getAdapter() == null) {
      mRecyclerView.setAdapter(new TaskGroupAdapter(taskGroup, mRestoredState.getType() != State.Type.Done));
    } else {
      ((ITaskGroupAdapter) mRecyclerView.getAdapter()).changeTaskGroup(taskGroup);
    }

    ((ITaskGroupAdapter) mRecyclerView.getAdapter()).attachCallbacks(this);
  }

  // ------------------------------------- PresenterFragment --------------------------------------

  @Override
  protected ITypedTasksView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<TypedTasksPresenter, ITypedTasksView> getPresenterFactory() {
    ITasksLocalService tasksLocalService = ((ITasksLocalServiceProvider) getActivity()
        .getApplication())
        .getTasksLocalService();

    ITasksRepository tasksRepository = TasksRepository.Factory.create(tasksLocalService);

    GetTypedTaskGroupUseCase getTaskGroupUseCase = new GetTypedTaskGroupUseCase(
        mRestoredState.getType(),
        tasksRepository,
        AsyncTask.THREAD_POOL_EXECUTOR);

    SaveTaskUseCase saveTaskUseCase = new SaveTaskUseCase(
        tasksRepository,
        AsyncTask.THREAD_POOL_EXECUTOR);

    return new TypedTasksPresenter.Factory(getTaskGroupUseCase, saveTaskUseCase);
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


  // ---------------------------------------- inner types -----------------------------------------

  public static class State {

    static final String KEY_FRAGMENT_TYPE = "KEY_FRAGMENT_TYPE";

    private Type type;

    State() {
    }

    void initializeWithBundle(Bundle bundle) {
      if (bundle != null && bundle.containsKey(KEY_FRAGMENT_TYPE)) {
        type = (Type) bundle.getSerializable(KEY_FRAGMENT_TYPE);
      } else {
        type = null;
      }
    }

    Type getType() {
      return type;
    }

    boolean isInitialized() {
      return type != null;
    }

    void saveToOutState(Bundle outState) {
      if (isInitialized()) {
        outState.putSerializable(KEY_FRAGMENT_TYPE, type);
      }
    }

    static Bundle wrap(Type type) {
      Bundle bundle = new Bundle();
      bundle.putSerializable(KEY_FRAGMENT_TYPE, type);
      return bundle;
    }

    public enum Type {
      NoDate,
      Important,
      Done
    }
  }
}