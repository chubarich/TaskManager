package com.danielkashin.taskorganiser.presentation_layer.view.week;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups.TaskGroupsAdapter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.week.WeekPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;

import java.util.ArrayList;

import static com.beloo.widget.chipslayoutmanager.ChipsLayoutManager.HORIZONTAL;

public class WeekFragment extends PresenterFragment<WeekPresenter, IWeekView> implements IWeekView {

  private RecyclerView mRecyclerView;


  public static WeekFragment getInstance() {
    return new WeekFragment();
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

    ArrayList<ArrayList<Task>> taskLists = new ArrayList<>();
    for (int i = 0; i < 8; ++i) {
      ArrayList<Task> taskList = new ArrayList<>();
      taskList.add(new Task("Какая-то задача", "sss", i == 0 ? Task.Type.Week : Task.Type.Day, "03-05-2017"));
      taskLists.add(taskList);
    }

    mRecyclerView.setAdapter(new TaskGroupsAdapter(taskLists, labels, Task.Type.Day));
    mRecyclerView.setNestedScrollingEnabled(false);
  }

  // ---------------------------------------- inner types -----------------------------------------



}
