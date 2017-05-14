package com.danielkashin.taskorganiser.presentation_layer.view.task_groups;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;

import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.ITaskViewOpener;
import com.danielkashin.taskorganiser.util.DatetimeHelper;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.domain_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetDateTypeTaskGroupsUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups.ITaskGroupsAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups.TaskGroupsAdapter;
import com.danielkashin.taskorganiser.presentation_layer.application.ITasksLocalServiceProvider;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.task_groups.ITaskGroupsPresenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.task_groups.TaskGroupsPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.ICalendarWalker;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.ITagViewOpener;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IToolbarContainer;

import static com.danielkashin.taskorganiser.util.DatetimeHelper.getDayLabel;
import static com.danielkashin.taskorganiser.util.DatetimeHelper.getMonthLabel;
import static com.danielkashin.taskorganiser.util.DatetimeHelper.getWeekLabel;
import static com.danielkashin.taskorganiser.util.DatetimeHelper.getYearLabel;

import java.util.ArrayList;
import java.util.Arrays;


public class TaskGroupsFragment extends PresenterFragment<TaskGroupsPresenter, ITaskGroupsView>
    implements ITaskGroupsView, ITaskGroupsAdapter.Callbacks, IDateContainer {

  private RecyclerView mRecyclerView;
  private State mRestoredState;

  // --------------------------------------- getInstance ------------------------------------------

  public static TaskGroupsFragment getInstance(String date, Task.Type type) {
    TaskGroupsFragment fragment = new TaskGroupsFragment();

    fragment.setArguments(State.wrap(date, type));

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

    showActivityLabel();

    ((ITaskGroupsPresenter) getPresenter()).onGetTaskGroupsData();
  }

  @Override
  public void onStop() {
    if (mRecyclerView.getAdapter() != null) {
      ((ITaskGroupsAdapter) mRecyclerView.getAdapter()).detachCallbacks();
    }

    super.onStop();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mRestoredState.saveToOutState(outState);
  }

  // -------------------------------------- ITaskGroupsView ---------------------------------------

  @Override
  public void addTaskToViewInterface(Task task) {
    if (mRecyclerView.getAdapter() != null) {
      ((ITaskGroupsAdapter) mRecyclerView.getAdapter()).changeTask(task);
    }
  }

  @Override
  public void initializeAdapter(ArrayList<DateTypeTaskGroup> taskGroups) {
    Pair<ArrayList<String>, Integer> labelsAndHighlightIndex = getLabelsAndHighlightIndex(taskGroups);

    mRecyclerView.setAdapter(new TaskGroupsAdapter(taskGroups,
        labelsAndHighlightIndex.first,
        labelsAndHighlightIndex.second,
        ContextCompat.getColor(getContext(), R.color.colorAccent),
        ContextCompat.getColor(getContext(), R.color.main_text)));
    ((ITaskGroupsAdapter) mRecyclerView.getAdapter()).attachCallbacks(this);
  }

  // -------------------------------------- IDateContainer ----------------------------------------

  @Override
  public Pair<String, Task.Type> getParentDate() {
    if (mRestoredState.getType() == Task.Type.Week) {
      return new Pair<>(mRestoredState.getDate(), Task.Type.Month);
    } else if (mRestoredState.getType() == Task.Type.Day) {
      String firstDayOfMonth = DatetimeHelper.getMonthDate(mRestoredState.getDate());
      return new Pair<>(firstDayOfMonth, Task.Type.Week);
    } else {
      return null;
    }
  }

  @Override
  public Pair<String, Task.Type> getUpDate() {
    String upDate = null;
    String date = mRestoredState.getDate();
    if (mRestoredState.getType() == Task.Type.Day) {
      upDate = DatetimeHelper.getUpWeek(date);
    } else if (mRestoredState.getType() == Task.Type.Week) {
      upDate = DatetimeHelper.getUpMonth(date);
    } else {
      upDate = DatetimeHelper.getUpYear(date);
    }

    if (upDate != null) {
      return new Pair<>(upDate, mRestoredState.getType());
    } else {
      return null;
    }
  }

  @Override
  public Pair<String, Task.Type> getDownDate() {
    String downDate = null;
    String date = mRestoredState.getDate();
    if (mRestoredState.getType() == Task.Type.Day) {
      downDate = DatetimeHelper.getDownWeek(date);
    } else if (mRestoredState.getType() == Task.Type.Week) {
      downDate = DatetimeHelper.getDownMonth(date);
    } else {
      downDate = DatetimeHelper.getDownYear(date);
    }

    if (downDate != null) {
      return new Pair<>(downDate, mRestoredState.getType());
    } else {
      return null;
    }
  }

  // -------------------------------- ITaskGroupsAdapter.Callbacks --------------------------------

  @Override
  public void onTaskChanged(Task task) {
    ((ITaskGroupsPresenter)getPresenter()).onSaveTask(task);
  }

  @Override
  public void onTaskLabelClicked(String date, Task.Type type) {
    ((ICalendarWalker) getActivity()).onOpenChildDate(date, type);
  }

  @Override
  public void onTagClicked(String tagName) {
    ((ITagViewOpener)getActivity()).onOpenTagView(tagName);
  }

  @Override
  public void onTaskClicked(Task task) {
    ((ITaskViewOpener) getActivity()).onOpenTaskView(task.getType(), task.getUUID());
  }

  // ------------------------------------- PresenterFragment --------------------------------------

  @Override
  protected ITaskGroupsView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<TaskGroupsPresenter, ITaskGroupsView> getPresenterFactory() {
    ITasksLocalService tasksLocalService = ((ITasksLocalServiceProvider) getActivity()
        .getApplication())
        .getTasksLocalService();

    ITasksRepository tasksRepository = TasksRepository.Factory.create(tasksLocalService);

    GetDateTypeTaskGroupsUseCase getTaskGroupUseCase = new GetDateTypeTaskGroupsUseCase(
        tasksRepository,
        AsyncTask.THREAD_POOL_EXECUTOR,
        mRestoredState.getType(),
        mRestoredState.getDate());

    SaveTaskUseCase saveTaskUseCase = new SaveTaskUseCase(
        tasksRepository,
        AsyncTask.THREAD_POOL_EXECUTOR);

    return new TaskGroupsPresenter.Factory(getTaskGroupUseCase, saveTaskUseCase);
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
    mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerView.setNestedScrollingEnabled(false);
  }

  // ------------------------------------------ private -------------------------------------------

  private Pair<ArrayList<String>, Integer> getLabelsAndHighlightIndex(ArrayList<DateTypeTaskGroup> taskGroups) {
    String[] days = getResources().getStringArray(R.array.days);
    String[] months = getResources().getStringArray(R.array.months);
    String[] simpleMonths = getResources().getStringArray(R.array.months_simple);

    // output
    ArrayList<String> labels = new ArrayList<>();
    Integer highlightIndex = -1;

    // fill output data
    if (mRestoredState.getType() == Task.Type.Day) { // --------------- Task.Type.Day -------------
      labels.add(getString(R.string.tasks_for_week));
      String currentDay = DatetimeHelper.getCurrentDay();
      for (int i = 1; i < taskGroups.size(); ++i) {
        String dayDate = taskGroups.get(i).getDate();
        if (dayDate.equals(currentDay)) {
          highlightIndex = i;
        }
        String label = getDayLabel(months, days, dayDate);
        labels.add(label);
      }
    } else if (mRestoredState.getType() == Task.Type.Week) { // -------- Task.Type.Week -----------
      labels.add(getString(R.string.tasks_for_month));
      String currentWeek = DatetimeHelper.getCurrentWeek();
      for (int i = 1; i < taskGroups.size(); ++i) {
        String weekDate = taskGroups.get(i).getDate();
        if (weekDate.equals(currentWeek)) {
          highlightIndex = i;
        }
        String label = getWeekLabel(months, weekDate);
        labels.add(label);
      }
    } else if (mRestoredState.getType() == Task.Type.Month) { // -------- Task.Type.Month ---------
      labels.addAll(Arrays.asList(simpleMonths));
      String currentMonth = DatetimeHelper.getCurrentMonth();
      for (int i = 0; i < taskGroups.size(); ++i) {
        if (taskGroups.get(i).getDate().equals(currentMonth)) {
          highlightIndex = i;
        }
      }
    } else {
      throw new IllegalStateException("Unsupported task type");
    }

    return new Pair<>(labels, highlightIndex);
  }

  private void showActivityLabel() {
    String[] months = getResources().getStringArray(R.array.months);
    String[] simpleMonths = getResources().getStringArray(R.array.months_simple);
    if (getActivity() != null) {
      switch (mRestoredState.getType()) {
        case Day:
          ((IToolbarContainer) getActivity()).setToolbar(getWeekLabel(months, mRestoredState.getDate()), true, true, false, false);
          break;
        case Week:
          ((IToolbarContainer) getActivity()).setToolbar(getMonthLabel(simpleMonths, mRestoredState.getDate()), true, true, false, false);
          break;
        case Month:
          ((IToolbarContainer) getActivity()).setToolbar(getYearLabel(mRestoredState.getDate()), false, true, false, false);
          break;
      }
    }
  }

  // ---------------------------------------- inner types -----------------------------------------

  private static class State {

    static final String KEY_DATE = "KEY_DATE";
    static final String KEY_TYPE = "KEY_TYPE";

    // MUST BE: first day of month for Type.Week, first day of week for Type.Day,
    // any day of year for Type.Month
    private String date;
    private Task.Type type;

    State() {
    }

    void initializeWithBundle(Bundle bundle) {
      if (bundle != null && bundle.containsKey(KEY_DATE) && bundle.containsKey(KEY_TYPE)) {
        date = bundle.getString(KEY_DATE);
        type = (Task.Type) bundle.getSerializable(KEY_TYPE);
      } else {
        date = null;
        type = null;
      }
    }

    String getDate() {
      return date;
    }

    Task.Type getType() {
      return type;
    }

    boolean isInitialized() {
      return date != null && type != null;
    }

    void saveToOutState(Bundle outState) {
      if (isInitialized()) {
        outState.putString(KEY_DATE, date);
        outState.putSerializable(KEY_TYPE, type);
      }
    }

    static Bundle wrap(String date, Task.Type type) {
      Bundle bundle = new Bundle();
      bundle.putString(KEY_DATE, date);
      bundle.putSerializable(KEY_TYPE, type);
      return bundle;
    }
  }
}
