package com.danielkashin.taskorganiser.domain_layer.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;

import java.util.ArrayList;

public class TasksWithoutTimeAdapter extends RecyclerView.Adapter<TasksWithoutTimeAdapter.TaskWithoutTimeHolder> {

  ArrayList<Task> tasks;

  public TasksWithoutTimeAdapter(ArrayList<Task> tasks) {
    ExceptionHelper.checkAllObjectsNonNull("All adapter arguments must be non null", tasks);

  }

  public TasksWithoutTimeAdapter() {

  }


  @Override
  public TaskWithoutTimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ViewType.TASK.getCode()) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(0, parent, false);
      return null;
    } else if (viewType == ViewType.EDIT_TEXT.getCode()) {
      return null;
    } else {
      throw new IllegalStateException("Unknown adapter view type");
    }
  }

  @Override
  public void onBindViewHolder(TaskWithoutTimeHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return tasks.size() + 1;
  }

  @Override
  public int getItemViewType(int position) {
    if (position == tasks.size()) {
      return ViewType.EDIT_TEXT.getCode();
    } else {
      return ViewType.TASK.getCode();
    }
  }

  class TaskWithoutTimeHolder extends RecyclerView.ViewHolder {

    private TaskWithoutTimeHolder(View view) {
      super(view);
    }

  }

  enum  ViewType{
    TASK(1243),
    EDIT_TEXT(2435);

    private int code;

    private ViewType(int code) {
      this.code = code;
    }

    public int getCode() {
      return code;
    }

  }
}
