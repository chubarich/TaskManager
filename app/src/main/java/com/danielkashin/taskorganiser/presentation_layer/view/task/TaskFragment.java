package com.danielkashin.taskorganiser.presentation_layer.view.task;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.domain_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTaskWithAllTagsUseCase;
import com.danielkashin.taskorganiser.presentation_layer.adapter.tags_with_selection.ITagsWithSelectionAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.tags_with_selection.TagsWithSelectionAdapter;
import com.danielkashin.taskorganiser.presentation_layer.application.ITasksLocalServiceProvider;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.task.ITaskPresenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.task.TaskPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IToolbarContainer;
import com.danielkashin.taskorganiser.util.DatetimeHelper;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.ArrayList;


public class TaskFragment extends PresenterFragment<TaskPresenter, ITaskView>
    implements ITaskView {

  private TextView mTextDate;
  private EditText mEditName;
  private EditText mEditNote;
  private LinearLayout mLayoutDuration;
  private LinearLayout mLayoutStartAndEnd;
  private LinearLayout mLayoutNotification;
  private RecyclerView mRecyclerTags;

  private State mRestoredState;

  // -------------------------------------- getInstance -------------------------------------------

  public static TaskFragment getInstance(Task.Type type, String UUID) {
    TaskFragment fragment = new TaskFragment();

    Bundle arguments = State.wrap(type, UUID);
    fragment.setArguments(arguments);

    return fragment;
  }

  // --------------------------------------- lifecycle --------------------------------------------

  @Override
  public void onCreate(Bundle savedInstanceState) {
    mRestoredState = new State();
    mRestoredState.initializeWithBundle(savedInstanceState);
    if (!mRestoredState.isInitialized()) {
      mRestoredState.initializeWithBundle(getArguments());
    }
    ExceptionHelper.assertTrue("Fragment state must be initialized", mRestoredState.isInitialized());

    super.onCreate(savedInstanceState);
  }

  @Override
  public void onStart() {
    super.onStart();

    ((IToolbarContainer) getActivity()).setToolbar(getString(R.string.task_toolbar), false, false, true, true);

    if (!mRestoredState.isTaskInitialized()) {
      ((ITaskPresenter) getPresenter()).onGetTask(mRestoredState.getType(), mRestoredState.getUUID());
    } else {
      attachTaskWithTags(mRestoredState.getStateTask(), mRestoredState.getTags());
    }

    setListeners();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mRestoredState.saveToOutState(outState);
  }

  // --------------------------------------- ITaskView --------------------------------------------

  @Override
  public void attachTaskWithTags(Task task, ArrayList<String> tags) {
    mRestoredState.setTask(task);
    mRestoredState.setTags(tags);

    if (mRecyclerTags.getAdapter() != null) {
      ((ITagsWithSelectionAdapter) mRecyclerTags.getAdapter()).initialize(tags, task.getTags());
    }

    refreshView();
  }

  @Override
  public Task getCurrentTask() {
    Task task = mRestoredState.getStateTask();
    if (mRecyclerTags.getAdapter() != null) {
      task.setTags(((ITagsWithSelectionAdapter)mRecyclerTags.getAdapter()).getSelectedTags());
    }

    return task;
  }

  // ------------------------------------ PresenterFragment ---------------------------------------

  @Override
  protected ITaskView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<TaskPresenter, ITaskView> getPresenterFactory() {
    ITasksLocalService tasksLocalService = ((ITasksLocalServiceProvider) getActivity()
        .getApplication())
        .getTasksLocalService();

    ITasksRepository tasksRepository = TasksRepository.Factory.create(tasksLocalService);

    GetTaskWithAllTagsUseCase getTaskWithAllTagsUseCase = new GetTaskWithAllTagsUseCase(tasksRepository, AsyncTask.THREAD_POOL_EXECUTOR);

    return new TaskPresenter.Factory(getTaskWithAllTagsUseCase);
  }

  @Override
  protected int getFragmentId() {
    return this.getClass().getSimpleName().hashCode();
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_task;
  }

  @Override
  protected void initializeView(View view) {
    mTextDate = (TextView) view.findViewById(R.id.text_date);
    mEditName = (EditText) view.findViewById(R.id.edit_name);
    mEditNote = (EditText) view.findViewById(R.id.edit_note);
    mRecyclerTags = (RecyclerView) view.findViewById(R.id.recycler_tags);
    mRecyclerTags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    mRecyclerTags.setAdapter(new TagsWithSelectionAdapter());
  }

  // ------------------------------------------ private -------------------------------------------

  private void refreshView() {
    Task task = mRestoredState.getStateTask();

    mEditName.setText(task.getName());
    mEditNote.setText(task.getNote());

    String[] months = getResources().getStringArray(R.array.months);
    String[] days = getResources().getStringArray(R.array.days);

    String text = null;
    if (task.getType() == Task.Type.Day) {
      text = getString(R.string.task_for_day) + ": " + DatetimeHelper.getDayLabel(months, days, task.getDate());
    } else if (task.getType() == Task.Type.Week) {
      text = getString(R.string.task_for_week) + ": " + DatetimeHelper.getWeekLabel(months, task.getDate());
    } else if (task.getType()== Task.Type.Month) {
      text = getString(R.string.task_for_month) + ": " + DatetimeHelper.getMonthLabel(months, task.getDate());
    } else if (task.getType()== Task.Type.NoDate) {
      text = getString(R.string.task_no_date);
    }

    if (text == null) {
      throw new IllegalStateException("Unhandled task date");
    } else {
      mTextDate.setText(text);
    }
  }

  private boolean checkTask() {
    Task task = mRestoredState.getStateTask();
    return task != null && task.isValid();
  }

  private void setListeners() {
    mEditName.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.toString().length() >= Task.MAX_NAME_LENGTH) {
          Toast.makeText(getContext(), getString(R.string.task_name_too_long), Toast.LENGTH_SHORT).show();
          mEditName.setText(s.toString().substring(0, Task.MAX_NAME_LENGTH - 1));
        } else if (s.toString().trim().length() == 0) {
          Toast.makeText(getContext(), getString(R.string.task_name_too_short), Toast.LENGTH_SHORT).show();
        } else {
          mRestoredState.setTaskName(s.toString().trim());
        }
      }
    });

    mEditNote.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() >= Task.MAX_NOTE_LENGTH) {
          Toast.makeText(getContext(), getString(R.string.task_note_too_long), Toast.LENGTH_SHORT).show();
          mEditNote.setText(s.toString().substring(0, Task.MAX_NOTE_LENGTH - 1));
        } else {
          mRestoredState.setTaskNote(s.toString().trim());
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
  }

  // ---------------------------------------- inner types -----------------------------------------

  public static class State {

    private static final String KEY_TYPE = "KEY_TYPE";
    private static final String KEY_UUID = "KEY_UUID";
    private static final String KEY_TASK = "KEY_TASK";
    private static final String KEY_TAGS = "KEY_TAGS";

    private Task.Type type;
    private String UUID;
    private Task task;
    private ArrayList<String> tags;

    private State() {
    }

    private void initializeWithBundle(Bundle bundle) {
      if (bundle != null && bundle.containsKey(KEY_TYPE) && bundle.containsKey(KEY_UUID)) {
        UUID = bundle.getString(KEY_UUID);
        type = (Task.Type) bundle.getSerializable(KEY_TYPE);

        if (bundle.containsKey(KEY_TASK) && bundle.containsKey(KEY_TAGS)) {
          task = bundle.getParcelable(KEY_TASK);
          tags = bundle.getStringArrayList(KEY_TAGS);
        } else {
          task = null;
          tags = null;
        }
      } else {
        UUID = null;
        type = null;
        task = null;
      }
    }

    private void setTask(Task task) {
      this.task = task;
    }

    private void setTags(ArrayList<String> tags) {
      this.tags = tags;
    }

    private void setTaskName(String name) {
      if (task != null) {
        task.setName(name);
      }
    }

    private void setTaskNote(String note) {
      if (task != null) {
        task.setNote(note);
      }
    }

    private boolean isTaskInitialized() {
      return task != null && tags != null;
    }

    private Task getStateTask() {
      return task;
    }

    private ArrayList<String> getTags() {
      return tags;
    }

    private Task.Type getType() {
      return type;
    }

    private String getUUID() {
      return UUID;
    }

    private boolean isInitialized() {
      return type != null && UUID != null;
    }

    private void saveToOutState(Bundle outState) {
      if (isInitialized()) {
        outState.putSerializable(KEY_TYPE, type);
        outState.putString(KEY_UUID, UUID);

        if (isTaskInitialized()) {
          outState.putParcelable(KEY_TASK, task);
          outState.putStringArrayList(KEY_TAGS, tags);
        }
      }
    }

    private static Bundle wrap(Task.Type type, String UUID) {
      Bundle bundle = new Bundle();
      bundle.putSerializable(KEY_TYPE, type);
      bundle.putString(KEY_UUID, UUID);
      return bundle;
    }
  }
}
