package com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups;

import android.os.Bundle;
import android.os.Parcelable;
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
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_group.ITaskGroupAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_group.TaskGroupAdapter;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;


public class TaskGroupsAdapter extends RecyclerView.Adapter<TaskGroupsAdapter.TaskGroupHolder>
    implements ITaskGroupsAdapter, ITaskGroupAdapter.Callbacks {

  private static final String KEY_TASK_GROUPS = "TASK_GROUPS";
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
    for (int i = 0; i < mCheckedPositions.length; ++i) {
      mCheckedPositions[i] = false;
    }
  }

  public TaskGroupsAdapter(Bundle bundle) throws IllegalStateException {
    boolean illegalBundle = bundle == null || !bundle.containsKey(KEY_LABELS)
        || !bundle.containsKey(KEY_TASK_GROUPS) || !bundle.containsKey(KEY_HIGHLIGHT_INDEX)
        || !bundle.containsKey(KEY_HIGHLIGHT_COLOR) || !bundle.containsKey(KEY_COMMON_COLOR)
        || !bundle.containsKey(KEY_CHECKED_POSITIONS);
    ExceptionHelper.assertFalse("Bundle must contain all the needed fields", illegalBundle);

    mTaskGroups = restoreTaskGroups(bundle);
    mLabels = bundle.getStringArrayList(KEY_LABELS);
    mHighlightIndex = bundle.getInt(KEY_HIGHLIGHT_INDEX);
    mHighlightColor = bundle.getInt(KEY_HIGHLIGHT_COLOR);
    mCommonColor = bundle.getInt(KEY_COMMON_COLOR);
    mCheckedPositions = bundle.getBooleanArray(KEY_CHECKED_POSITIONS);
  }

  // -------------------------------------- ITaskGroupsAdapter ------------------------------------

  @Override
  public void saveToOutState(Bundle outState) {
    outState.putParcelableArrayList(KEY_TASK_GROUPS, mTaskGroups);
    outState.putStringArrayList(KEY_LABELS, mLabels);
    outState.putInt(KEY_HIGHLIGHT_INDEX, mHighlightIndex);
    outState.putInt(KEY_HIGHLIGHT_COLOR, mHighlightColor);
    outState.putBooleanArray(KEY_CHECKED_POSITIONS, mCheckedPositions);
  }

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
    for (int i = 0; i < mTaskGroups.size(); ++i) {
      DateTypeTaskGroup taskGroup = mTaskGroups.get(i);

      if (task.getType() == taskGroup.getType() && task.getDate().equals(taskGroup.getDate())) {
        ArrayList<Task> tasks = taskGroup.getTasks();
        for (int j = 0; j < tasks.size(); ++j) {
          if (tasks.get(j).equals(task)) {
            taskGroup.setTask(task, j);
            notifyItemChanged(i);
            return;
          }
        }

        taskGroup.addTask(task);
        notifyItemChanged(i);
        return;
      }
    }
  }

  // ---------------------------------- ITaskGroupAdapter.Callbacks -------------------------------

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
  public void onBindViewHolder(TaskGroupHolder holder, int p) {
    final int position = holder.getAdapterPosition();

    boolean isHeader = position == 0 && (mTaskGroups.get(1).getType() == Task.Type.Day
        || mTaskGroups.get(1).getType() == Task.Type.Week);
    boolean isHighlighted = position == mHighlightIndex;

    if (!mCheckedPositions[position] && (isHeader || isHighlighted)) {
      mCheckedPositions[position] = true;
      holder.showExpandable();
    } else if (!mCheckedPositions[position]) {
      mCheckedPositions[position] = true;
      holder.hideExpandable();
    }

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

    holder.setText(mLabels.get(position));
    holder.setTaskGroup(mTaskGroups.get(position));
    holder.setAdapterCallbacks(this);

    if (!isHeader) {
      holder.setOnLabelClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mCallbacks != null) {
            Task.Type childType = null;
            Task.Type currentType = mTaskGroups.get(position).getType();
            if (currentType == Task.Type.Month) {
              childType = Task.Type.Week;
            } else if (currentType == Task.Type.Week) {
              childType = Task.Type.Day;
            }

            if (childType != null) {
              mCallbacks.onTaskLabelClicked(mTaskGroups.get(position).getDate(), childType);
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

  // ----------------------------------------- private --------------------------------------------

  private ArrayList<DateTypeTaskGroup> restoreTaskGroups(Bundle savedInstanceState) {
    ArrayList<Parcelable> parcelableArrayList = savedInstanceState
        .getParcelableArrayList(KEY_TASK_GROUPS);

    if (parcelableArrayList == null) {
      throw new IllegalStateException("Bundle must contain TaskGroups key");
    }

    ArrayList<DateTypeTaskGroup> result = new ArrayList<>();
    for (Parcelable parcelable : parcelableArrayList) {
      result.add((DateTypeTaskGroup) parcelable);
    }
    return result;
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
      tasksRecyclerView.setHasFixedSize(true);
      tasksRecyclerView.setNestedScrollingEnabled(false);

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
      expandableLayout.expand(false);
    }

    private void hideExpandable() {
      expandableLayout.collapse(false);
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
