package com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;
import com.danielkashin.taskorganiser.presentation_layer.adapter.tasks_group.ITaskGroupAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.tasks_group.TaskGroupAdapter;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;


public class TaskGroupsAdapter extends RecyclerView.Adapter<TaskGroupsAdapter.TaskGroupHolder>
    implements ITaskGroupsAdapter, ITaskGroupAdapter.Callbacks {

  private ArrayList<TaskGroup> mTaskGroups;
  private ArrayList<String> mLabels;
  private ITaskGroupsAdapter.Callbacks mCallbacks;

  public TaskGroupsAdapter(ArrayList<TaskGroup> taskGroups, ArrayList<String> labels) {
    ExceptionHelper.checkAllObjectsNonNull("All arguments must be non null", taskGroups, labels);
    ExceptionHelper.assertTrue("Labels and groups sizes must be equal", taskGroups.size() == labels.size());

    mTaskGroups = taskGroups;
    mLabels = labels;
  }

  // -------------------------------------- ITaskGroupsAdapter ------------------------------------

  @Override
  public void attachCallbacks(Callbacks callbacks) {
    mCallbacks = callbacks;
  }

  @Override
  public void detachCallbacks() {
    mCallbacks = null;
  }

  @Override
  public void addTask(Task task) {
    for (int i = 0; i < mTaskGroups.size(); ++i) {
      TaskGroup taskGroup = mTaskGroups.get(i);

      if (task.getType() == taskGroup.getType() && task.getDate().equals(taskGroup.getDate())) {
        taskGroup.addTask(task);
        notifyItemChanged(i);
      }
    }
  }

  @Override
  public void refreshTask(Task task) {
    for (int i = 0; i < mTaskGroups.size(); ++i) {
      TaskGroup taskGroup = mTaskGroups.get(i);

      if (task.getType() == taskGroup.getType() && task.getDate().equals(taskGroup.getDate())) {
        notifyItemChanged(i);
      }
    }
  }

  // ---------------------------------- ITaskGroupAdapter.Callbacks -------------------------------

  @Override
  public void onTaskCreated(Task task) {
    if (mCallbacks != null) {
      mCallbacks.onTaskCreated(task);
    }
  }

  @Override
  public void onTaskRefreshed(Task task) {
    if (mCallbacks != null) {
      mCallbacks.onTaskRefreshed(task);
    }
  }

  // ------------------------------------- RecyclerView.Adapter -----------------------------------

  @Override
  public TaskGroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_task_group, parent, false);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(parent.getContext());

    return new TaskGroupHolder(view, linearLayoutManager);
  }

  @Override
  public void onBindViewHolder(TaskGroupHolder holder, int p) {
    int position = holder.getAdapterPosition();

    holder.setText(mLabels.get(position));
    holder.setTaskGroup(mTaskGroups.get(position));
    holder.setAdapterCallbacks(this);
  }

  @Override
  public int getItemCount() {
    return mTaskGroups.size();
  }

  // --------------------------------------- inner types ------------------------------------------

  static class TaskGroupHolder extends RecyclerView.ViewHolder {

    private final RecyclerView tasksRecyclerView;
    private final TextView label;
    private final ExpandableLayout expandableLayout;
    private final ImageView imageExpand;


    private TaskGroupHolder(View view, RecyclerView.LayoutManager layoutManager) {
      super(view);
      ExceptionHelper.checkAllObjectsNonNull("All arguments must be non null", view, layoutManager);

      label = (TextView) view.findViewById(R.id.label);
      expandableLayout = (ExpandableLayout) view.findViewById(R.id.expandable_layout);
      imageExpand = (ImageView) view.findViewById(R.id.image_expand);

      tasksRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_tasks);
      tasksRecyclerView.setAdapter(new TaskGroupAdapter());
      tasksRecyclerView.addItemDecoration(new SpacingItemDecoration(0, 10));
      tasksRecyclerView.setLayoutManager(layoutManager);
      tasksRecyclerView.setHasFixedSize(true);
      tasksRecyclerView.setNestedScrollingEnabled(false);

      imageExpand.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          expandableLayout.toggle(false);
        }
      });
    }

    private void setTaskGroup(TaskGroup taskGroup) {
      ((ITaskGroupAdapter) tasksRecyclerView.getAdapter()).setOrRefreshTaskGroup(taskGroup);
    }

    private void setText(String text) {
      label.setText(text);
    }

    private void setAdapterCallbacks(ITaskGroupAdapter.Callbacks callbacks) {
      ((ITaskGroupAdapter) tasksRecyclerView.getAdapter()).attachCallbacks(callbacks);
    }
  }
}
