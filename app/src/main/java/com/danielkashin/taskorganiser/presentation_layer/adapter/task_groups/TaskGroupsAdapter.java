package com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.presentation_layer.adapter.tasks_without_time.TasksWithoutTimeAdapter;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.view.Gravity.TOP;
import static com.beloo.widget.chipslayoutmanager.ChipsLayoutManager.HORIZONTAL;


public class TaskGroupsAdapter extends RecyclerView.Adapter<TaskGroupsAdapter.TaskGroupHolder>
    implements ITaskGroupsAdapter {

  private ArrayList<ArrayList<Task>> mTaskLists;
  private ArrayList<String> mLabels;
  private Task.Type mTaskGroupType;


  public TaskGroupsAdapter(ArrayList<ArrayList<Task>> taskLists, ArrayList<String> labels, Task.Type dateType) {
    ExceptionHelper.checkAllObjectsNonNull("All arguments must be non null", taskLists,
        dateType, labels);
    ExceptionHelper.assertFalse("TaskGroupsAdapter does not support Task.Type.Mini",
        dateType == Task.Type.Mini);
    ExceptionHelper.assertFalse("Task.Type.Day requires 7 days as input",
        dateType == Task.Type.Day && (taskLists.size() != 8 || labels.size() != 8));
    ExceptionHelper.assertFalse("Task.Type.Month requires 12 months as input",
        dateType == Task.Type.Month && (taskLists.size() != 12 || labels.size() != 12));

    mTaskLists = taskLists;
    mTaskGroupType = dateType;
    mLabels = labels;
  }

  // -------------------------------------- ITaskGroupsAdapter ------------------------------------



  // ------------------------------------- RecyclerView.Adapter -----------------------------------

  @Override
  public TaskGroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    TaskGroupHolder.ViewType holderViewType;
    if (viewType == TaskGroupHolder.ViewType.WITH_DATE_TASKS.getValue()) {
      holderViewType = TaskGroupHolder.ViewType.WITH_DATE_TASKS;
    } else if (viewType == TaskGroupHolder.ViewType.WITHOUT_DATE_TASKS.getValue()) {
      holderViewType = TaskGroupHolder.ViewType.WITHOUT_DATE_TASKS;
    } else {
      throw new IllegalStateException("Unknown adapter holder ViewType");
    }

    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_task_group, parent, false);

    ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(parent.getContext())
        .setScrollingEnabled(false)
        .setOrientation(HORIZONTAL)
        .build();

    return new TaskGroupHolder(view, holderViewType, mTaskGroupType, chipsLayoutManager);
  }

  @Override
  public void onBindViewHolder(TaskGroupHolder holder, int p) {
    int position = holder.getAdapterPosition();

    int viewType = getItemViewType(position);
    if (viewType == holder.getItemViewType()) {
      holder.setText(mLabels.get(position));
    }
  }

  @Override
  public int getItemCount() {
    return mTaskLists.size();
  }

  @Override
  public int getItemViewType(int position) {
    if (mTaskGroupType == Task.Type.Day && position != 0) {
      return TaskGroupHolder.ViewType.WITH_DATE_TASKS.getValue();
    } else {
      return TaskGroupHolder.ViewType.WITHOUT_DATE_TASKS.getValue();
    }
  }

  // --------------------------------------- inner types ------------------------------------------

  static class TaskGroupHolder extends RecyclerView.ViewHolder {

    private final ViewType viewType;
    private final Task.Type taskType;
    private final RecyclerView withoutDatesRecyclerView;
    private final RecyclerView withDatesRecyclerView;
    private final ExpandableLayout expandableLayout;
    private final ImageView imageExpand;
    private final TextView label;

    private TaskGroupHolder(View view, ViewType viewType, Task.Type taskType,
                            ChipsLayoutManager chipsLayoutManager) {
      super(view);

      ExceptionHelper.checkAllObjectsNonNull("All arguments must be non null",
          view, viewType, taskType, chipsLayoutManager);

      this.viewType = viewType;
      this.taskType = taskType;

      imageExpand = (ImageView) view.findViewById(R.id.image_expand);
      expandableLayout = (ExpandableLayout) view.findViewById(R.id.expandable_layout);
      withoutDatesRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_without_dates);
      label = (TextView) view.findViewById(R.id.label);
      withDatesRecyclerView = null;

      initializeView(view, chipsLayoutManager);
    }

    private ViewType getViewType() {
      return viewType;
    }

    private void setText(String text) {
      label.setText(text);
    }

    private void initializeView(View view, ChipsLayoutManager chipsLayoutManager) {
      imageExpand.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          expandableLayout.toggle(false);
          expandableLayout.requestLayout();
        }
      });

      withoutDatesRecyclerView.setAdapter(new TasksWithoutTimeAdapter(taskType));
      withoutDatesRecyclerView.addItemDecoration(new SpacingItemDecoration(6, 6));
      withoutDatesRecyclerView.setLayoutManager(chipsLayoutManager);
      withoutDatesRecyclerView.setHasFixedSize(true);
      withoutDatesRecyclerView.setNestedScrollingEnabled(false);

      // TODO
    }

    private enum ViewType {
      WITH_DATE_TASKS(234),
      WITHOUT_DATE_TASKS(591);

      int value;

      ViewType(int value) {
        this.value = value;
      }

      public int getValue() {
        return value;
      }
    }

  }

}
