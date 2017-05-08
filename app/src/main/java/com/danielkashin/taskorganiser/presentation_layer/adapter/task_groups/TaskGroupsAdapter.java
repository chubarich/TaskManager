package com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.ITaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_group.ITaskGroupAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_group.TaskGroupAdapter;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;


public class TaskGroupsAdapter extends RecyclerView.Adapter<TaskGroupsAdapter.TaskGroupHolder>
    implements ITaskGroupsAdapter, ITaskGroupAdapter.Callbacks {

  private static final String KEY_LABELS = "LABELS";
  private static final String KEY_HIGHLIGHT_INDEX = "HIGHLIGHT_INDEX";
  private static final String KEY_HIGHLIGHT_COLOR = "HIGHLIGHT_COLOR";
  private static final String KEY_COMMON_COLOR = "COMMON_COLOR";
  private static final String KEY_CHECKED_POSITIONS = "CHECKED_POSITIONS";

  private ArrayList<DateTypeTaskGroup> mTaskGroups;
  private ArrayList<String> mLabels;
  private int mHighlightIndex;
  private int mHighlightColor;
  private int mCommonColor;
  private boolean[] mCheckedPositions;
  private boolean[] mShowExpandable;
  private ITaskGroupsAdapter.Callbacks mCallbacks;


  public TaskGroupsAdapter(ArrayList<DateTypeTaskGroup> taskGroups,
                           ArrayList<String> labels,
                           int highlightIndex,
                           int highlightColor,
                           int commonColor) {
    ExceptionHelper.checkAllObjectsNonNull("All arguments must be non null", taskGroups, labels);
    ExceptionHelper.assertTrue("Labels and groups sizes must be equal", taskGroups.size() == labels.size());

    mTaskGroups = taskGroups;
    mLabels = labels;
    mHighlightIndex = highlightIndex;
    mHighlightColor = highlightColor;
    mCommonColor = commonColor;

    mCheckedPositions = new boolean[mTaskGroups.size()];
    mShowExpandable = new boolean[mTaskGroups.size()];
    for (int i = 0; i < mCheckedPositions.length; ++i) {
      mCheckedPositions[i] = false;
      mShowExpandable[i] = false;
    }
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
  public void changeTask(Task task) {
    int iToSet = -1;

    for (int i = 0; i < mTaskGroups.size(); ++i) {
      if (mTaskGroups.get(i).canBelongTo(task)) {
        iToSet = i;
      }
    }

    if (iToSet != -1) {
      mTaskGroups.get(iToSet).addTask(task);
      mShowExpandable[iToSet] = true;
      notifyItemChanged(iToSet);
    }
  }

  // ---------------------------------- ITaskGroupAdapter.Callbacks -------------------------------

  @Override
  public void onCreateTask(String name, String UUID, ITaskGroup taskGroup) {
    if (mCallbacks != null) {
      ExceptionHelper.assertTrue("", taskGroup instanceof DateTypeTaskGroup);
      Task task = new Task(name, UUID, ((DateTypeTaskGroup) taskGroup).getType(),
          ((DateTypeTaskGroup) taskGroup).getDate());
      mCallbacks.onTaskChanged(task);
    }
  }

  @Override
  public void onTaskChanged(Task task) {
    if (mCallbacks != null) {
      mCallbacks.onTaskChanged(task);
    }
  }

  @Override
  public void onTagClicked(String tagName) {
    if (mCallbacks != null) {
      mCallbacks.onTagClicked(tagName);
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
  public void onBindViewHolder(final TaskGroupHolder holder, final int position) {
    final DateTypeTaskGroup newTaskGroup = new DateTypeTaskGroup(mTaskGroups.get(position));

    boolean isHeader = position == 0 && (mTaskGroups.get(1).getType() == Task.Type.Day
        || mTaskGroups.get(1).getType() == Task.Type.Week);
    boolean isHighlighted = position == mHighlightIndex;

    if (mShowExpandable[position]) {
      mShowExpandable[position] = false;
      mCheckedPositions[position] = true;

      holder.showExpandable();
    } else if (!mCheckedPositions[position]) {
      mCheckedPositions[position] = true;

      if (isHeader || isHighlighted) {
        holder.showExpandable();
      } else {
        holder.hideExpandable();
      }
    }


    holder.setTaskGroup(newTaskGroup);
    holder.setText(mLabels.get(position));
    holder.setAdapterCallbacks(this);


    if (isHeader) {
      holder.showViewHighlighter();
    } else {
      holder.hideViewHighlighter();
    }

    if (isHighlighted) {
      holder.colorText(mHighlightColor);
    } else {
      holder.colorText(mCommonColor);
    }

    if (!isHeader) {
      holder.setOnLabelClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mCallbacks != null) {
            Task.Type childType = null;
            Task.Type currentType = newTaskGroup.getType();
            if (currentType == Task.Type.Month) {
              childType = Task.Type.Week;
            } else if (currentType == Task.Type.Week) {
              childType = Task.Type.Day;
            }

            if (childType != null) {
              mCallbacks.onTaskLabelClicked(newTaskGroup.getDate(), childType);
            }
          }
        }
      });
    }
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
    private final View viewHighlighter;

    private TaskGroupHolder(View view, RecyclerView.LayoutManager layoutManager) {
      super(view);
      ExceptionHelper.checkAllObjectsNonNull("All arguments must be non null", view, layoutManager);

      label = (TextView) view.findViewById(R.id.label);
      expandableLayout = (ExpandableLayout) view.findViewById(R.id.expandable_layout);
      imageExpand = (ImageView) view.findViewById(R.id.image_expand);
      viewHighlighter = view.findViewById(R.id.view_highlighter);

      tasksRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_tasks);
      tasksRecyclerView.setAdapter(new TaskGroupAdapter());
      tasksRecyclerView.addItemDecoration(new SpacingItemDecoration(0, 10));
      tasksRecyclerView.setLayoutManager(layoutManager);
      //tasksRecyclerView.setNestedScrollingEnabled(false);

      imageExpand.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          expandableLayout.toggle(false);
        }
      });
    }

    private void setTaskGroup(DateTypeTaskGroup taskGroup) {
      ((ITaskGroupAdapter) tasksRecyclerView.getAdapter()).changeTaskGroup(taskGroup);
    }

    private void setOnLabelClickListener(View.OnClickListener listener) {
      label.setOnClickListener(listener);
    }

    private void showViewHighlighter() {
      viewHighlighter.setVisibility(View.VISIBLE);
    }

    private void hideViewHighlighter() {
      viewHighlighter.setVisibility(View.GONE);
    }

    private void showExpandable() {
      if (!expandableLayout.isExpanded()) {
        expandableLayout.expand(false);
      }
    }

    private void hideExpandable() {
      if (expandableLayout.isExpanded()) {
        expandableLayout.collapse(false);
      }
    }

    private void colorText(int color) {
      label.setTextColor(color);
    }

    private void setText(String text) {
      label.setText(text);
    }

    private void setAdapterCallbacks(ITaskGroupAdapter.Callbacks callbacks) {
      ((ITaskGroupAdapter) tasksRecyclerView.getAdapter()).attachCallbacks(callbacks);
    }
  }
}
