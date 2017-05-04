package com.danielkashin.taskorganiser.presentation_layer.view.week;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.domain_layer.helper.DatetimeHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups.ITaskGroupsAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups.TaskGroupsAdapter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.week.WeekPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;

import java.util.ArrayList;

import static com.beloo.widget.chipslayoutmanager.ChipsLayoutManager.HORIZONTAL;

public class WeekFragment extends PresenterFragment<WeekPresenter, IWeekView>
    implements IWeekView, ITaskGroupsAdapter.Callbacks {

  private RecyclerView mRecyclerView;
  private State state;

  public static WeekFragment getInstance() {
    return new WeekFragment();
  }

  // ----------------------------------------- lifecycle ------------------------------------------

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    
  }

  @Override
  public void onStart() {
    super.onStart();

    String currentFirstDayOfWeek = DatetimeHelper.getCurrentFirstDayOfWeek();
    ArrayList<String> allDaysOfWeek = DatetimeHelper.getAllDaysOfWeek("2017-03-27");

    ((ITaskGroupsAdapter)mRecyclerView.getAdapter()).attachCallbacks(this);
  }

  @Override
  public void onStop() {
    ((ITaskGroupsAdapter)mRecyclerView.getAdapter()).detachCallbacks();

    super.onStop();
  }

  // -------------------------------- ITaskGroupsAdapter.Callbacks --------------------------------

  @Override
  public void onTaskCreated(Task task) {
    ((ITaskGroupsAdapter)mRecyclerView.getAdapter()).addTask(task);
  }

  @Override
  public void onTaskRefreshed(Task task) {
    ((ITaskGroupsAdapter)mRecyclerView.getAdapter()).refreshTask(task);
  }

  // ------------------------------------- PresenterFragment --------------------------------------

  @Override
  protected IWeekView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<WeekPresenter, IWeekView> getPresenterFactory() {
    WeekPresenter.Factory factory = new WeekPresenter.Factory();
    return factory;
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

    // TODO
    ArrayList<String> labels = new ArrayList<>();
    labels.add(getString(R.string.tasks_for_week));
    String[] days = getResources().getStringArray(R.array.days);
    for (String day : days) {
      labels.add(day);
    }

    ArrayList<TaskGroup> taskGroups = new ArrayList<>();

    TaskGroup weekTasks = new TaskGroup("2017-05-01", Task.Type.Week);
    taskGroups.add(weekTasks);

    for (int i = 0; i < 7; ++i) {
      TaskGroup taskGroup = new TaskGroup("2017-05-0" + (i+1), Task.Type.Day);
      taskGroup.addTask(new Task("Какая-то задача", "sss", Task.Type.Day, "2017-05-0" + (i+1)));
      taskGroups.add(taskGroup);
    }

    mRecyclerView.setAdapter(new TaskGroupsAdapter(taskGroups, labels));
    mRecyclerView.setNestedScrollingEnabled(false);
    mRecyclerView.addItemDecoration(new SpacingItemDecoration(0, 15));
  }

  // ---------------------------------------- inner types -----------------------------------------

  private class State {

    private static final String KEY_DATE = "KEY_DATE";

    private String date;




  }

}
