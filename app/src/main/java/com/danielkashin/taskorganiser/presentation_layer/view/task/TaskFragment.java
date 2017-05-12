package com.danielkashin.taskorganiser.presentation_layer.view.task;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.domain_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.application.ITasksLocalServiceProvider;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.task.ITaskPresenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.task.TaskPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IToolbarContainer;
import com.danielkashin.taskorganiser.util.ExceptionHelper;


public class TaskFragment extends PresenterFragment<TaskPresenter, ITaskView>
    implements ITaskView {

  private EditText mNameEditText;
  private EditText mNoteEditText;

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
      ((ITaskPresenter)getPresenter()).onGetTask(mRestoredState.getType(), mRestoredState.getUUID());
    } else {
      attachTask(mRestoredState.getStateTask());
    }

    setListeners();
  }

  // --------------------------------------- ITaskView --------------------------------------------

  @Override
  public void attachTask(Task task) {
    mRestoredState.setTask(task);

    mNameEditText.setText(task.getName());
    mNoteEditText.setText(task.getNote());
  }

  @Override
  public Task getCurrentTask() {
    return mRestoredState.getStateTask();
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

    GetTaskUseCase getTaskUseCase = new GetTaskUseCase(tasksRepository, AsyncTask.THREAD_POOL_EXECUTOR);

    return new TaskPresenter.Factory(getTaskUseCase);
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
    mNameEditText = (EditText) view.findViewById(R.id.edit_name);
    mNoteEditText = (EditText) view.findViewById(R.id.edit_note);
  }

  // ------------------------------------------ private -------------------------------------------

  private boolean checkTask() {
    Task task = mRestoredState.getStateTask();

    return task != null && task.isValid();
  }

  private void setListeners() {
    mNameEditText.addTextChangedListener(new TextWatcher() {
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
          mNameEditText.setText(s.toString().substring(0, Task.MAX_NAME_LENGTH - 1));
        } else if (s.toString().trim().length() == 0) {
          Toast.makeText(getContext(), getString(R.string.task_name_too_short), Toast.LENGTH_SHORT).show();
        } else {
          mRestoredState.setTaskName(s.toString().trim());
        }
      }
    });

    mNoteEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() >= Task.MAX_NOTE_LENGTH) {
          Toast.makeText(getContext(), getString(R.string.task_note_too_long), Toast.LENGTH_SHORT).show();
          mNoteEditText.setText(s.toString().substring(0, Task.MAX_NOTE_LENGTH - 1));
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

    private Task.Type type;
    private String UUID;
    private Task task;

    private State() {
    }

    private void initializeWithBundle(Bundle bundle) {
      if (bundle != null && bundle.containsKey(KEY_TYPE) && bundle.containsKey(KEY_UUID)) {
        UUID = bundle.getString(KEY_UUID);
        type = (Task.Type) bundle.getSerializable(KEY_TYPE);

        if (bundle.containsKey(KEY_TASK)) {
         task = bundle.getParcelable(KEY_TASK);
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
      return task != null;
    }

    private Task getStateTask() {
      return task;
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
