package com.danielkashin.taskorganiser.presentation_layer.view.task_groups;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;

import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.domain_layer.helper.DatetimeHelper;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;
import com.danielkashin.taskorganiser.domain_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTaskGroupUseCase;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups.ITaskGroupsAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups.TaskGroupsAdapter;
import com.danielkashin.taskorganiser.presentation_layer.application.ITasksLocalServiceProvider;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.task_groups.ITaskGroupsPresenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.task_groups.TaskGroupsPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IToolbarContainer;

import static com.danielkashin.taskorganiser.domain_layer.helper.DatetimeHelper.Day;
import static com.danielkashin.taskorganiser.domain_layer.helper.DatetimeHelper.Month;

import java.util.ArrayList;
import java.util.Arrays;


public class TaskGroupsFragment extends PresenterFragment<TaskGroupsPresenter, ITaskGroupsView>
    implements ITaskGroupsView, ITaskGroupsAdapter.Callbacks {

  private RecyclerView mRecyclerView;
  private State mRestoredState;

  // --------------------------------------- getInstance ------------------------------------------

  public static TaskGroupsFragment getInstance(String date, Task.Type type) {
    ExceptionHelper.assertFalse("Fragment does not support Task.Type.Mini", type == Task.Type.Mini);

    Bundle bundle = State.saveToBundle(date, type);

    TaskGroupsFragment fragment = new TaskGroupsFragment();
    fragment.setArguments(bundle);

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

    String[] months = getResources().getStringArray(R.array.months);
    String[] simpleMonths = getResources().getStringArray(R.array.months_simple);
    if (getActivity() != null) {
      switch (mRestoredState.getType()) {
        case Day:
          ((IToolbarContainer) getActivity()).setToolbarLabel(getWeekLabel(months, mRestoredState.getDate()));
          break;
        case Week:
          ((IToolbarContainer) getActivity()).setToolbarLabel(getMonthLabel(simpleMonths, mRestoredState.getDate()));
          break;
        case Month:
          ((IToolbarContainer) getActivity()).setToolbarLabel(getYearLabel(mRestoredState.getDate()));
          break;
      }
    }

    super.onCreate(savedInstanceState);
  }

  @Override
  public void onStart() {
    super.onStart();

    if (!mRestoredState.isAdapterInitialized()) {
      ((ITaskGroupsPresenter) getPresenter()).onGetTaskGroupsData();
    } else {
      ((ITaskGroupsAdapter) mRecyclerView.getAdapter()).attachCallbacks(this);
    }
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
    mRestoredState.setAdapter((ITaskGroupsAdapter) mRecyclerView.getAdapter());
    mRestoredState.saveToBundle(outState);
  }

  // -------------------------------------- ITaskGroupsView ---------------------------------------

  @Override
  public void initializeAdapter(ArrayList<TaskGroup> taskGroups) {
    Pair<ArrayList<String>, Integer> labelsAndHighlightIndex = getLabelsAndHighlightIndex(taskGroups);

    mRecyclerView.setAdapter(new TaskGroupsAdapter(taskGroups,
        labelsAndHighlightIndex.first,
        labelsAndHighlightIndex.second,
        ContextCompat.getColor(getContext(), R.color.colorAccent),
        ContextCompat.getColor(getContext(), R.color.main_text)));
    ((ITaskGroupsAdapter) mRecyclerView.getAdapter()).attachCallbacks(this);
  }

  // -------------------------------- ITaskGroupsAdapter.Callbacks --------------------------------

  @Override
  public void onTaskCreated(Task task) {
    ((ITaskGroupsAdapter) mRecyclerView.getAdapter()).addTask(task);
  }

  @Override
  public void onTaskRefreshed(Task task) {
    ((ITaskGroupsAdapter) mRecyclerView.getAdapter()).refreshTask(task);
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

    TasksRepository tasksRepository = new TasksRepository(tasksLocalService);

    GetTaskGroupUseCase getTaskGroupUseCase = new GetTaskGroupUseCase(
        tasksRepository,
        AsyncTask.THREAD_POOL_EXECUTOR,
        mRestoredState.getType(),
        mRestoredState.getDate());

    return new TaskGroupsPresenter.Factory(getTaskGroupUseCase);
  }

  @Override
  protected int getFragmentId() {
    return this.getClass().getSimpleName().hashCode();
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_tasks_container;
  }

  @Override
  protected void initializeView(View view) {
    mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerView.setNestedScrollingEnabled(false);

    if (mRestoredState.isAdapterInitialized()) {
      mRecyclerView.setAdapter((RecyclerView.Adapter) mRestoredState.getAdapter());
    }
  }

  // ------------------------------------------ private -------------------------------------------

  private Pair<ArrayList<String>, Integer> getLabelsAndHighlightIndex(ArrayList<TaskGroup> taskGroups) {
    String[] days = getResources().getStringArray(R.array.days);
    String[] months = getResources().getStringArray(R.array.months);
    String[] simpleMonths = getResources().getStringArray(R.array.months_simple);

    // output
    ArrayList<String> labels = new ArrayList<>();
    Integer highlightIndex = null;

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

  private String getDayLabel(String[] months, String[] days, String dayDate) {
    Day day = DatetimeHelper.getDay(dayDate);
    String label = days[day.getDayOfWeek() - 1] + ", " + day.getDayOfMonth() + " "
        + months[day.getMonth() - 1];

    return label;
  }

  private String getWeekLabel(String[] months, String weekDate) {
    Pair<Day, Day> firstAndLastDays = DatetimeHelper.getFirstAndLastDaysOfWeek(weekDate);
    String firstDayMonth = months[firstAndLastDays.first.getMonth() - 1];
    String lastDayMonth = months[firstAndLastDays.second.getMonth() - 1];

    String label = firstAndLastDays.first.getDayOfMonth()
        + (firstDayMonth.equals(lastDayMonth) ? "" : " " + firstDayMonth) + " - "
        + firstAndLastDays.second.getDayOfMonth() + " "
        + lastDayMonth;

    return label;
  }

  private String getMonthLabel(String[] months, String monthDate) {
    Month month = DatetimeHelper.getMonth(monthDate);
    String label = months[month.getMonth() - 1] + ", " + month.getYear();

    return label;
  }

  private String getYearLabel(String yearDate) {
    int year = DatetimeHelper.getYear(yearDate);
    return "" + year;
  }

  // ---------------------------------------- inner types -----------------------------------------

  private static class State {

    static final String KEY_DATE = "KEY_DATE";
    static final String KEY_TYPE = "KEY_TYPE";

    private String date;
    private Task.Type type;
    private ITaskGroupsAdapter adapter;

    State() {
    }

    void initializeWithBundle(Bundle bundle) {
      if (bundle != null && bundle.containsKey(KEY_DATE) && bundle.containsKey(KEY_TYPE)) {
        date = bundle.getString(KEY_DATE);
        type = (Task.Type) bundle.getSerializable(KEY_TYPE);

        try {
          adapter = new TaskGroupsAdapter(bundle);
        } catch (IllegalStateException e) {
          adapter = null;
        }
      } else {
        date = null;
        type = null;
        adapter = null;
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

    boolean isAdapterInitialized() {
      return adapter != null;
    }

    void setAdapter(ITaskGroupsAdapter adapter) {
      this.adapter = adapter;
    }

    ITaskGroupsAdapter getAdapter() {
      return adapter;
    }

    void saveToBundle(Bundle outState) {
      if (isInitialized()) {
        outState.putString(KEY_DATE, date);
        outState.putSerializable(KEY_TYPE, type);

        if (isAdapterInitialized()) {
          adapter.saveToOutState(outState);
        }
      }
    }

    static Bundle saveToBundle(String date, Task.Type type) {
      Bundle bundle = new Bundle();
      bundle.putString(KEY_DATE, date);
      bundle.putSerializable(KEY_TYPE, type);
      return bundle;
    }
  }
}
