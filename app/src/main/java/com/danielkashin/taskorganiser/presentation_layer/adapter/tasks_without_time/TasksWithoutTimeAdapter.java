package com.danielkashin.taskorganiser.presentation_layer.adapter.tasks_without_time;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;

import java.util.ArrayList;
import java.util.Random;

public class TasksWithoutTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements ITasksWithoutTimeAdapter {

  private final static int VIEW_TYPE_TASK = 21245;
  private final static int VIEW_TYPE_EDIT_TEXT = 12245;

  private ITasksWithoutTimeAdapter.Callbacks callbacks;
  private ArrayList<Task> tasks;
  private Task.Type tasksType;

  public TasksWithoutTimeAdapter(ArrayList<Task> tasks, Task.Type tasksType) {
    ExceptionHelper.checkAllObjectsNonNull("All adapter arguments must be non null", tasks);

    this.tasks = tasks;
    this.tasksType = tasksType;
  }

  public TasksWithoutTimeAdapter(Task.Type tasksType) {
    this.tasks = new ArrayList<>();
    this.tasksType = tasksType;
  }

  // ----------------------------------- ITasksWithoutTimeAdapter  --------------------------------

  @Override
  public void setCallbacks(Callbacks callbacks) {
    this.callbacks = callbacks;
  }

  // ------------------------------------- RecyclerView.Adapter -----------------------------------

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_TASK) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_task_without_time, parent, false);
      return new TaskViewHolder(view);
    } else if (viewType == VIEW_TYPE_EDIT_TEXT) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_edit_text, parent, false);
      return new EditTextViewHolder(view);
    } else {
      throw new IllegalStateException("Unknown adapter view type");
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof TaskViewHolder && holder.getAdapterPosition() != tasks.size()) {
      final Task task = tasks.get(holder.getAdapterPosition());
      TaskViewHolder taskViewHolder = (TaskViewHolder) holder;

      taskViewHolder.setToggleButtonChecked(task.getDone());
      taskViewHolder.setOnToggleClickedListener(new TaskViewHolder.OnToggleClickedListener() {
        @Override
        public void onToggleClicked(boolean isChecked) {
          if (callbacks != null) {
            callbacks.onTaskDoneChanged(task, isChecked);
          }
        }
      });
      taskViewHolder.setText(task.getName());
    } else if (holder instanceof EditTextViewHolder && holder.getAdapterPosition() == tasks.size()) {
      ((EditTextViewHolder) holder).setOnTextChangedListener(new EditTextViewHolder.OnTextChangedListener() {
        @Override
        public void onTextChanged(String text) {
          // TODO
          tasks.add(new Task(text, String.valueOf(new Random().nextInt(1111111111)), tasksType, ""));
          notifyItemInserted(tasks.size() - 1);

          /* TODO
          if (callbacks != null) {
            callbacks.onEditTextTextChanged(text);
          }
          */
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    return tasks.size() + 1;
  }

  @Override
  public int getItemViewType(int position) {
    if (position == tasks.size()) {
      return VIEW_TYPE_EDIT_TEXT;
    } else {
      return VIEW_TYPE_TASK;
    }
  }

  // --------------------------------------- inner types ------------------------------------------

  private static class TaskViewHolder extends RecyclerView.ViewHolder {

    private TextView textView;
    private ToggleButton toggleButton;
    private OnToggleClickedListener onToggleClickedListener;

    private TaskViewHolder(View view) {
      super(view);
      textView = (TextView) view.findViewById(R.id.text_view);
      toggleButton = (ToggleButton) view.findViewById(R.id.toggle_button);
      setOnToggleClickedListenerLocal();
    }

    private void setText(String text) {
      textView.setText(text);
    }

    private void setToggleButtonChecked(boolean checked) {
      toggleButton.setOnCheckedChangeListener(null);
      toggleButton.setChecked(checked);
      setOnToggleClickedListenerLocal();
    }

    private void setOnToggleClickedListener(OnToggleClickedListener onToggleButtonListener) {
      this.onToggleClickedListener = onToggleClickedListener;
    }

    private void setOnToggleClickedListenerLocal() {
      toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          if (onToggleClickedListener != null) {
            onToggleClickedListener.onToggleClicked(isChecked);
          }
        }
      });
    }

    private interface OnToggleClickedListener {

      void onToggleClicked(boolean isChecked);

    }
  }

  private static class EditTextViewHolder extends RecyclerView.ViewHolder {

    private EditText editText;
    private OnTextChangedListener onTextChangedListener;


    private EditTextViewHolder(View view) {
      super(view);
      editText = (EditText) view.findViewById(R.id.edit_text);
      setOnTextChangedListenerLocal();
    }

    private void setOnTextChangedListener(OnTextChangedListener listener) {
      this.onTextChangedListener = listener;
    }

    private void setOnTextChangedListenerLocal() {
      editText.addTextChangedListener(new TextWatcher() {
        // fields to perform delay
        private final static int INPUT_DELAY_IN_MS = 750;
        private Handler handler = new Handler(Looper.getMainLooper());
        Runnable workRunnable;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
          handler.removeCallbacks(workRunnable);
        }

        @Override
        public void afterTextChanged(final Editable editable) {
          if (!editable.toString().trim().isEmpty()) {
            workRunnable = new Runnable() {
              @Override
              public void run() {
                if (onTextChangedListener != null) {
                  String text = editable.toString().trim();
                  onTextChangedListener.onTextChanged(text);
                  editText.setText("");
                }
              }
            };
            handler.postDelayed(workRunnable, INPUT_DELAY_IN_MS);
          }
        }
      });
    }

    private interface OnTextChangedListener {

      void onTextChanged(String text);

    }
  }
}
