package com.danielkashin.taskorganiser.presentation_layer.adapter.tasks_group;

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
import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;

import java.util.UUID;

public class TaskGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements ITaskGroupAdapter {

  private final static int VIEW_TYPE_TASK = 21245;
  private final static int VIEW_TYPE_EDIT_TEXT = 12245;

  private ITaskGroupAdapter.Callbacks mCallbacks;
  private TaskGroup mTaskGroup;


  public TaskGroupAdapter() {
  }

  public TaskGroupAdapter(TaskGroup taskGroup) {
    ExceptionHelper.checkAllObjectsNonNull("All adapter arguments must be non null", taskGroup);

    mTaskGroup = taskGroup;
  }

  // ----------------------------------- ITaskGroupAdapter  --------------------------------

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
    ExceptionHelper.assertFalse("TaskGroup is null", mTaskGroup == null);

    int insertedIndex = mTaskGroup.addTask(task);
    notifyItemInserted(insertedIndex);
  }

  @Override
  public void setOrRefreshTaskGroup(TaskGroup taskGroup) {
    if (mTaskGroup != null && mTaskGroup.getTaskSize() + 1 == taskGroup.getTaskSize()) {
      int differIndex = -1;
      for (int i = 0; i < mTaskGroup.getTaskSize(); ++i) {
        if (!taskGroup.getTask(i).equals(mTaskGroup.getTask(i))) {
          differIndex = i;
          break;
        }
      }

      if (differIndex == -1) {
        mTaskGroup = taskGroup;
        notifyDataSetChanged();
        return;
      }

      for (int i = differIndex; i < mTaskGroup.getTaskSize(); ++i) {
        if (!taskGroup.getTask(i).equals(mTaskGroup.getTask(i))) {
          mTaskGroup = taskGroup;
          notifyDataSetChanged();
          return;
        }
      }

      notifyItemInserted(mTaskGroup.addTask(taskGroup.getTask(differIndex)));
    } else if (mTaskGroup != null && mTaskGroup.getTaskSize() == taskGroup.getTaskSize()) {
      for (int i = 0; i < mTaskGroup.getTaskSize(); ++i) {
        if (!taskGroup.getTask(i).equals(mTaskGroup.getTask(i))) {
          mTaskGroup = taskGroup;
          notifyDataSetChanged();
          return;
        }
      }

      for (int i = 0; i < mTaskGroup.getTaskSize(); ++i) {
        if (!taskGroup.getTask(i).equalsWithAdditionalInformation(mTaskGroup.getTask(i))) {
          mTaskGroup.setTask(mTaskGroup.getTask(i), i);
          notifyItemChanged(i);
        }
      }
    } else {
      mTaskGroup = taskGroup;
      notifyDataSetChanged();
    }
  }

  // ------------------------------------- RecyclerView.Adapter -----------------------------------

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_TASK) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_task, parent, false);
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
    boolean holderIsTask = getItemViewType(holder.getAdapterPosition()) == VIEW_TYPE_TASK
        && holder instanceof TaskViewHolder;
    boolean holderIsEditText = getItemViewType(holder.getAdapterPosition()) == VIEW_TYPE_EDIT_TEXT
        && holder instanceof EditTextViewHolder;

    if (holderIsTask) {
      final Task task = mTaskGroup.getTask(holder.getAdapterPosition());
      TaskViewHolder taskViewHolder = (TaskViewHolder) holder;

      // set name
      taskViewHolder.setTextName(task.getName());

      // set time
      if (mTaskGroup.getType() == Task.Type.Day) {
        taskViewHolder.setTextTime(task.getTimeToString());
      }

      // set done toggle
      taskViewHolder.setToggleButtonChecked(task.getDone());
      taskViewHolder.setOnToggleClickedListener(new TaskViewHolder.OnToggleClickedListener() {
        @Override
        public void onToggleClicked(boolean isChecked) {
          if (mCallbacks != null) {
            task.setDone(isChecked);
            mCallbacks.onTaskRefreshed(task);
          }
        }
      });
    } else if (holderIsEditText) {
      ((EditTextViewHolder) holder).setOnTextChangedListener(new EditTextViewHolder.OnTextChangedListener() {
        @Override
        public void onTextChanged(String text) {
          if (mCallbacks != null) {
            Task task = new Task(text,
                UUID.randomUUID().toString(),
                mTaskGroup.getType(),
                mTaskGroup.getDate());

            mCallbacks.onTaskCreated(task);
          }
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    if (mTaskGroup == null) {
      return 0;
    } else {
      return mTaskGroup.getTaskSize() + 1;
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (position == mTaskGroup.getTaskSize()) {
      return VIEW_TYPE_EDIT_TEXT;
    } else {
      return VIEW_TYPE_TASK;
    }
  }

  // --------------------------------------- inner types ------------------------------------------

  private static class TaskViewHolder extends RecyclerView.ViewHolder {

    private TextView textName;
    private TextView textTime;
    private ToggleButton toggleButton;
    private RecyclerView recyclerView;
    private OnToggleClickedListener onToggleClickedListener;

    private TaskViewHolder(View view) {
      super(view);
      textName = (TextView) view.findViewById(R.id.text_name);
      textTime = (TextView) view.findViewById(R.id.text_time);
      toggleButton = (ToggleButton) view.findViewById(R.id.toggle_button);
      recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
      setOnToggleClickedListenerLocal();
    }

    private void setTextName(String name) {
      textName.setText(name);
    }

    private void setTextTime(String time) {
      textTime.setText(time);
    }

    private void setToggleButtonChecked(boolean checked) {
      toggleButton.setOnCheckedChangeListener(null);
      toggleButton.setChecked(checked);
      setOnToggleClickedListenerLocal();
    }

    private void setOnToggleClickedListener(OnToggleClickedListener onToggleButtonListener) {
      this.onToggleClickedListener = onToggleClickedListener;
    }

    // ----------------------------------------- local --------------------------------------------

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

    // --------------------------------------- inner types ----------------------------------------

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
        private final static int INPUT_DELAY_IN_MS = 1000;
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
