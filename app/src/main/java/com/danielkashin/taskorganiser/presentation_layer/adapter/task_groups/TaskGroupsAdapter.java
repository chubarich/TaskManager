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
import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;
import com.danielkashin.taskorganiser.presentation_layer.adapter.tasks_group.ITaskGroupAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.tasks_group.TaskGroupAdapter;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;


public class TaskGroupsAdapter extends RecyclerView.Adapter<TaskGroupsAdapter.TaskGroupHolder>
    implements ITaskGroupsAdapter, ITaskGroupAdapter.Callbacks {

  private static final String KEY_TASK_GROUPS = "TASK_GROUPS";
  private static final String KEY_LABELS = "LABELS";
  private static final String KEY_HIGHLIGHT_INDEX = "HIGHLIGHT_INDEX";
  private static final String KEY_HIGHLIGHT_COLOR = "HIGHLIGHT_COLOR";
  private static final String KEY_COMMON_COLOR = "COMMON_COLOR";

  private ArrayList<TaskGroup> mTaskGroups;
  private ArrayList<String> mLabels;
  private int mHighlightIndex;
  private int mHighlightColor;
  private int mCommonColor;
  private ITaskGroupsAdapter.Callbacks mCallbacks;


  public TaskGroupsAdapter(ArrayList<TaskGroup> taskGroups,
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
  }

  public TaskGroupsAdapter(Bundle bundle) throws IllegalStateException {
    boolean illegalBundle = bundle == null || !bundle.containsKey(KEY_LABELS)
        || !bundle.containsKey(KEY_TASK_GROUPS) || !bundle.containsKey(KEY_HIGHLIGHT_INDEX)
        || !bundle.containsKey(KEY_HIGHLIGHT_COLOR) || !bundle.containsKey(KEY_COMMON_COLOR);
    ExceptionHelper.assertFalse("Bundle must contain all the needed fields", illegalBundle);

    mTaskGroups = restoreTaskGroups(bundle);
    mLabels = bundle.getStringArrayList(KEY_LABELS);
    mHighlightIndex = bundle.getInt(KEY_HIGHLIGHT_INDEX);
    mHighlightColor = bundle.getInt(KEY_HIGHLIGHT_COLOR);
    mCommonColor = bundle.getInt(KEY_COMMON_COLOR);
  }

  // -------------------------------------- ITaskGroupsAdapter ------------------------------------

  @Override
  public void saveToOutState(Bundle outState) {
    outState.putParcelableArrayList(KEY_TASK_GROUPS, mTaskGroups);
    outState.putStringArrayList(KEY_LABELS, mLabels);
    outState.putInt(KEY_HIGHLIGHT_INDEX, mHighlightIndex);
    outState.putInt(KEY_HIGHLIGHT_COLOR, mHighlightColor);
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

    if (position == mHighlightIndex) {
      holder.colorText(mHighlightColor);
    } else {
      holder.colorText(mCommonColor);
    }

    if (position == 0 && (mTaskGroups.get(1).getType() == Task.Type.Day
        || mTaskGroups.get(1).getType() == Task.Type.Week)) {
      holder.showViewHighlighter();
    } else {
      holder.hideViewHighlighter();
    }

    holder.setText(mLabels.get(position));
    holder.setTaskGroup(mTaskGroups.get(position));
    holder.setAdapterCallbacks(this);
  }

  @Override
  public int getItemCount() {
    return mTaskGroups.size();
  }

  // ----------------------------------------- private --------------------------------------------

  private ArrayList<TaskGroup> restoreTaskGroups(Bundle savedInstanceState) {
    ArrayList<Parcelable> parcelableArrayList = savedInstanceState
        .getParcelableArrayList(KEY_TASK_GROUPS);

    if (parcelableArrayList == null) {
      throw new IllegalStateException("Bundle must contain TaskGroups key");
    }

    ArrayList<TaskGroup> result = new ArrayList<>();
    for (Parcelable parcelable : parcelableArrayList) {
      result.add((TaskGroup) parcelable);
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

    private void setTaskGroup(TaskGroup taskGroup) {
      ((ITaskGroupAdapter) tasksRecyclerView.getAdapter()).setOrRefreshTaskGroup(taskGroup);
    }

    private void showViewHighlighter() {
      viewHighlighter.setVisibility(View.VISIBLE);
    }

    private void hideViewHighlighter() {
      viewHighlighter.setVisibility(View.GONE);
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
