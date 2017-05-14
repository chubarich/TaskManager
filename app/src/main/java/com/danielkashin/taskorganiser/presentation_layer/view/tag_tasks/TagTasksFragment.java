package com.danielkashin.taskorganiser.presentation_layer.view.tag_tasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.ITaskViewOpener;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.ITaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.TagTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.domain_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTagTaskGroupUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_group.ITaskGroupAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_group.TaskGroupAdapter;
import com.danielkashin.taskorganiser.presentation_layer.application.ITasksLocalServiceProvider;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.tag_tasks.ITagTasksPresenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.tag_tasks.TagTasksPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.ITagViewOpener;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IToolbarContainer;

import java.util.ArrayList;


public class TagTasksFragment extends PresenterFragment<TagTasksPresenter, ITagTasksView>
    implements ITagTasksView, ITaskGroupAdapter.Callbacks {

  private RecyclerView mRecyclerView;
  private State mRestoredState;


  public static TagTasksFragment getInstance(String tagName) {
    TagTasksFragment tagTasksFragment = new TagTasksFragment();

    tagTasksFragment.setArguments(State.wrap(tagName));

    return tagTasksFragment;
  }

  // ----------------------------------- PresenterFragment ----------------------------------------

  @Override
  protected ITagTasksView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<TagTasksPresenter, ITagTasksView> getPresenterFactory() {
    ITasksLocalService tasksLocalService = ((ITasksLocalServiceProvider) getActivity()
        .getApplication())
        .getTasksLocalService();

    ITasksRepository tasksRepository = TasksRepository.Factory.create(tasksLocalService);

    GetTagTaskGroupUseCase getTaskGroupUseCase = new GetTagTaskGroupUseCase(
        tasksRepository,
        AsyncTask.THREAD_POOL_EXECUTOR,
        mRestoredState.getTagName());

    SaveTaskUseCase saveTaskUseCase = new SaveTaskUseCase(
        tasksRepository,
        AsyncTask.THREAD_POOL_EXECUTOR);

    return new TagTasksPresenter.Factory(getTaskGroupUseCase, saveTaskUseCase);
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

    ((IToolbarContainer) getActivity()).setToolbar(mRestoredState.getTagName(), false, false, false, true);

    ((ITagTasksPresenter) getPresenter()).onGetTaskGroupData();
  }

  @Override
  public void onStop() {
    if (mRecyclerView.getAdapter() != null) {
      ((ITaskGroupAdapter) mRecyclerView.getAdapter()).detachCallbacks();
    }

    super.onStop();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mRestoredState.saveToOutState(outState);
  }

  // --------------------------------- ITaskGroupAdapter.Callbacks --------------------------------

  @Override
  public void onCreateTask(String name, String UUID, ITaskGroup taskGroup) {
    ExceptionHelper.assertTrue("", taskGroup instanceof TagTaskGroup);

    Task task = new Task(name, UUID, Task.Type.NoDate, null);

    ArrayList<String> tags = new ArrayList<>();
    tags.add(mRestoredState.getTagName());
    task.setTags(tags);

    ((ITagTasksPresenter) getPresenter()).onSaveTask(task);
  }

  @Override
  public void onTaskChanged(Task task) {
    ((ITagTasksPresenter) getPresenter()).onSaveTask(task);
  }

  @Override
  public void onTagClicked(String tagName) {
    ((ITagViewOpener) getActivity()).onOpenTagView(tagName);
  }

  @Override
  public void onTaskClicked(Task task) {
    ((ITaskViewOpener) getActivity()).onOpenTaskView(task.getType(), task.getUUID());
  }

  // -------------------------------------- ITagTasksView -----------------------------------------

  @Override
  public String getTagName() {
    return mRestoredState.getTagName();
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
      mRecyclerView.setAdapter(new TaskGroupAdapter(taskGroup, true,
          ContextCompat.getColor(getContext(), R.color.colorAccent),
          ContextCompat.getColor(getContext(), R.color.grey)));
    } else {
      ((ITaskGroupAdapter) mRecyclerView.getAdapter()).changeTaskGroup(taskGroup);
    }

    ((ITaskGroupAdapter) mRecyclerView.getAdapter()).attachCallbacks(this);
  }

  // ---------------------------------------- inner types -----------------------------------------

  private static class State {

    static final String KEY_TAG_NAME = "KEY_TAG_NAME";

    private String tagName;


    State() {
    }

    void initializeWithBundle(Bundle bundle) {
      if (bundle != null && bundle.containsKey(KEY_TAG_NAME)) {
        tagName = bundle.getString(KEY_TAG_NAME);
      } else {
        tagName = null;
      }
    }

    String getTagName() {
      return tagName;
    }

    boolean isInitialized() {
      return tagName != null;
    }

    void saveToOutState(Bundle outState) {
      if (isInitialized()) {
        outState.putString(KEY_TAG_NAME, tagName);
      }
    }

    static Bundle wrap(String tagName) {
      Bundle bundle = new Bundle();
      bundle.putString(KEY_TAG_NAME, tagName);
      return bundle;
    }
  }
}
