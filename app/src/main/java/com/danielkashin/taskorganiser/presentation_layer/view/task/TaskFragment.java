package com.danielkashin.taskorganiser.presentation_layer.view.task;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.data_layer.managers.INotificationManager;
import com.danielkashin.taskorganiser.data_layer.managers.NotificationManager;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.data_layer.repository.TasksRepository;
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

import java.security.Timestamp;
import java.util.ArrayList;


public class TaskFragment extends PresenterFragment<TaskPresenter, ITaskView>
    implements ITaskView {

  private FrameLayout mRootLayout;

  private TextView mTextDate;
  private EditText mEditName;
  private EditText mEditNote;
  private TextView mTextTime;
  private TextView mTextNotification;

  private TextView mTextToNoDate;
  private TextView mTextToThisDay;
  private TextView mTextToNextDay;
  private TextView mTextToThisWeek;
  private TextView mTextToNextWeek;
  private TextView mTextToThisMonth;
  private TextView mTextToNextMonth;
  private TextView mTextChooseDay;
  private TextView mTextChooseWeek;
  private TextView mTextChooseMonth;
  private TextView mTextChooseDuration;
  private TextView mTextChooseZeroDuration;
  private TextView mTextChooseStart;
  private TextView mTextChooseEnd;
  private TextView mTextChooseZeroStart;
  private TextView mTextChooseZeroEnd;
  private TextView mTextChooseNotification;
  private TextView mTextChooseZeroNotification;


  private HorizontalScrollView mLayoutDuration;
  private HorizontalScrollView mLayoutStartAndEnd;
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
    if (mRestoredState != null && mRestoredState.isTaskInitialized() && mRecyclerTags.getAdapter() != null) {
      ArrayList<String> tags = ((ITagsWithSelectionAdapter) mRecyclerTags.getAdapter()).getSelectedTags();
      mRestoredState.getStateTask().setTags(tags);
      mRestoredState.saveToOutState(outState);
    }
  }

  // --------------------------------------- ITaskView --------------------------------------------

  @Override
  public void attachTaskWithTags(Task task, ArrayList<String> tags) {
    mRestoredState.setTask(task);
    mRestoredState.setTags(tags);

    if (mRecyclerTags.getAdapter() != null && tags != null && task != null) {
      ((ITagsWithSelectionAdapter) mRecyclerTags.getAdapter()).initialize(tags, task.getTags());
    }

    refreshView();
  }

  @Override
  public Task getCurrentTask() {
    if (mRestoredState.isTaskInitialized() && mRecyclerTags.getAdapter() != null) {
      Task task = mRestoredState.getStateTask();
      task.setTags(((ITagsWithSelectionAdapter) mRecyclerTags.getAdapter()).getSelectedTags());
      return task;
    } else {
      return null;
    }
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
    INotificationManager notificationManager = new NotificationManager(getContext());

    ITasksRepository tasksRepository = TasksRepository.Factory.create(
        tasksLocalService,
        notificationManager);

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
    mRootLayout = (FrameLayout) view.findViewById(R.id.root_layout);

    mTextDate = (TextView) view.findViewById(R.id.text_date);
    mEditName = (EditText) view.findViewById(R.id.edit_name);
    mEditNote = (EditText) view.findViewById(R.id.edit_note);
    mTextTime = (TextView) view.findViewById(R.id.text_time);
    mTextNotification = (TextView) view.findViewById(R.id.text_notification);

    mRecyclerTags = (RecyclerView) view.findViewById(R.id.recycler_tags);
    mRecyclerTags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    mRecyclerTags.setAdapter(new TagsWithSelectionAdapter());

    mTextToNoDate = (TextView) view.findViewById(R.id.text_to_no_date);
    mTextToThisDay = (TextView) view.findViewById(R.id.text_to_this_day);
    mTextToNextDay = (TextView) view.findViewById(R.id.text_to_next_day);
    mTextToThisWeek = (TextView) view.findViewById(R.id.text_to_this_week);
    mTextToNextWeek = (TextView) view.findViewById(R.id.text_to_next_week);
    mTextToThisMonth = (TextView) view.findViewById(R.id.text_to_this_month);
    mTextToNextMonth = (TextView) view.findViewById(R.id.text_to_next_month);
    mTextChooseDay = (TextView) view.findViewById(R.id.text_choose_day);
    mTextChooseWeek = (TextView) view.findViewById(R.id.text_choose_week);
    mTextChooseMonth = (TextView) view.findViewById(R.id.text_choose_month);
    mTextChooseDuration = (TextView) view.findViewById(R.id.text_choose_duration);
    mTextChooseZeroDuration = (TextView) view.findViewById(R.id.text_choose_zero_duration);
    mTextChooseZeroStart = (TextView) view.findViewById(R.id.text_choose_zero_start);
    mTextChooseZeroEnd = (TextView) view.findViewById(R.id.text_choose_zero_end);
    mTextChooseStart = (TextView) view.findViewById(R.id.text_choose_start);
    mTextChooseEnd = (TextView) view.findViewById(R.id.text_choose_end);
    mTextChooseNotification = (TextView) view.findViewById(R.id.text_choose_notification);
    mTextChooseZeroNotification = (TextView) view.findViewById(R.id.text_choose_zero_notification);

    mLayoutDuration = (HorizontalScrollView) view.findViewById(R.id.layout_duration);
    mLayoutStartAndEnd = (HorizontalScrollView) view.findViewById(R.id.layout_start_and_end);
  }

  // ------------------------------------------ private -------------------------------------------

  private void setTaskNotification(Long timestamp) {
    if (mRestoredState.isTaskInitialized()) {
      Task task = mRestoredState.getStateTask();
      task.setNotificationTimestamp(timestamp);

      refreshView();
    }
  }

  private void setTaskStart(Long start) {
    if (mRestoredState.isTaskInitialized()) {
      Task task = mRestoredState.getStateTask();
      if (!task.setMinuteStart(start)) {
        Toast.makeText(getContext(), getString(R.string.wrong_start_and_end), Toast.LENGTH_SHORT).show();
      } else {
        refreshView();
      }
    }
  }

  private void setTaskEnd(Long end) {
    if (mRestoredState.isTaskInitialized()) {
      Task task = mRestoredState.getStateTask();
      if (!task.setMinuteEnd(end)) {
        Toast.makeText(getContext(), getString(R.string.wrong_start_and_end), Toast.LENGTH_SHORT).show();
      } else {
        refreshView();
      }
    }
  }

  private void setTaskTypeAndDate(Task.Type type, String date) {
    if (mRestoredState.isTaskInitialized()) {
      Task task = mRestoredState.getStateTask();
      task.setType(type);
      task.setDate(date);

      refreshView();
    }
  }

  private void setTaskDuration(Long duration) {
    if (mRestoredState.isTaskInitialized()) {
      Task task = mRestoredState.getStateTask();
      task.setDuration(duration);

      refreshView();
    }
  }

  private void refreshView() {
    Task task = mRestoredState.getStateTask();

    if (task != null) {
      mEditName.setText(task.getName());
      mEditNote.setText(task.getNote());

      // set duration
      boolean showDurationLayout = task.canSetDuration();
      mLayoutDuration.setVisibility(showDurationLayout ? View.VISIBLE : View.GONE);

      String time = task.getTimeToString(getString(R.string.from), getString(R.string.to),
          getString(R.string.duration), getString(R.string.duration_not_set), getString(R.string.time), false);
      mTextTime.setText(time);

      String notificationTime = task.getNotificationTimestampToString(getString(R.string.notification),
          getString(R.string.notification_not_set));
      mTextNotification.setText(notificationTime);

      mLayoutStartAndEnd.setVisibility(task.getType() == Task.Type.Day ? View.VISIBLE : View.GONE);

      // set date label
      String[] months = getResources().getStringArray(R.array.months);
      String[] simpleMonths = getResources().getStringArray(R.array.months_simple);
      String[] days = getResources().getStringArray(R.array.days);
      String text = null;
      if (task.getType() == Task.Type.Day) {
        text = getString(R.string.task_for_day) + ": " + DatetimeHelper.getDayLabel(months, days, task.getDate());
      } else if (task.getType() == Task.Type.Week) {
        text = getString(R.string.task_for_week) + ": " + DatetimeHelper.getWeekLabel(months, task.getDate());
      } else if (task.getType() == Task.Type.Month) {
        text = getString(R.string.task_for_month) + ": " + DatetimeHelper.getMonthLabel(simpleMonths, task.getDate());
      } else if (task.getType() == Task.Type.NoDate) {
        text = getString(R.string.task_no_date);
      }
      if (text == null) {
        throw new IllegalStateException("Unhandled task date");
      } else {
        mTextDate.setText(text);
      }
    }

    mRootLayout.setVisibility(View.VISIBLE);
  }

  private boolean checkTask() {
    Task task = mRestoredState.getStateTask();
    return task != null && task.isValid();
  }

  private void setListeners() {
    View.OnClickListener patternListener = getPatternListener();
    mTextToNoDate.setOnClickListener(patternListener);
    mTextToThisDay.setOnClickListener(patternListener);
    mTextToNextDay.setOnClickListener(patternListener);
    mTextToThisWeek.setOnClickListener(patternListener);
    mTextToNextWeek.setOnClickListener(patternListener);
    mTextToThisMonth.setOnClickListener(patternListener);
    mTextToNextMonth.setOnClickListener(patternListener);

    View.OnClickListener chooseDateListener = getChooseDateListener();
    mTextChooseDay.setOnClickListener(chooseDateListener);
    mTextChooseWeek.setOnClickListener(chooseDateListener);
    mTextChooseMonth.setOnClickListener(chooseDateListener);

    mTextChooseDuration.setOnClickListener(getChooseDurationListener());
    mTextChooseZeroDuration.setOnClickListener(getChooseDurationZeroListener());

    View.OnClickListener chooseStartAndEndZeroListener = getChooseStartAndEndZeroListener();
    mTextChooseZeroStart.setOnClickListener(chooseStartAndEndZeroListener);
    mTextChooseZeroEnd.setOnClickListener(chooseStartAndEndZeroListener);

    View.OnClickListener chooseStartAndEndListener = getChooseStartAndEndListener();
    mTextChooseStart.setOnClickListener(chooseStartAndEndListener);
    mTextChooseEnd.setOnClickListener(chooseStartAndEndListener);

    mTextChooseZeroNotification.setOnClickListener(getChooseZeroNotificationListener());
    mTextChooseNotification.setOnClickListener(getChooseNotificationListener());

    mEditName.addTextChangedListener(getNameWatcher());
    mEditNote.addTextChangedListener(getNoteWatcher());
  }

  private View.OnClickListener getChooseZeroNotificationListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ExceptionHelper.assertTrue("Button is illegal", v == mTextChooseZeroNotification);
        setTaskNotification(null);
      }
    };
  }

  private View.OnClickListener getChooseNotificationListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ExceptionHelper.assertTrue("Button is illegal", v == mTextChooseNotification);
        final View dialogView = View.inflate(getContext(), R.layout.datetime_picker_layout, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        Button doneButton = (Button) dialogView.findViewById(R.id.button_done);

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.please_choose_notification));
        alertDialog.setView(dialogView);

        doneButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            alertDialog.cancel();

            int hour = -1;
            int minute = -1;
            if (Build.VERSION.SDK_INT >= 23) {
              hour = timePicker.getHour();
              minute = timePicker.getMinute();
            } else {
              hour = timePicker.getCurrentHour();
              minute = timePicker.getCurrentMinute();
            }

            Long timestamp = DatetimeHelper.getTimestamp(datePicker.getYear(),
                datePicker.getMonth() + 1, datePicker.getDayOfMonth(), hour, minute);
            if (DatetimeHelper.isGreaterThanNow(timestamp, 0)) {
              setTaskNotification(timestamp);
            } else {
              Toast.makeText(getContext(), getString(R.string.notification_not_set_message),
                  Toast.LENGTH_SHORT).show();
            }


          }
        });

        alertDialog.show();
      }
    };
  }

  private View.OnClickListener getChooseStartAndEndZeroListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        ExceptionHelper.assertTrue("Button is illegal", v == mTextChooseZeroStart || v == mTextChooseZeroEnd);

        if (mRestoredState.isTaskInitialized()) {
          if (v == mTextChooseZeroStart) {
            setTaskStart(null);
          } else {
            setTaskEnd(null);
          }
        }
      }
    };
  }

  private View.OnClickListener getChooseStartAndEndListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ExceptionHelper.assertTrue("Button is illegal", v == mTextChooseStart || v == mTextChooseEnd);
        final boolean isChoosingStart = v == mTextChooseStart;

        if (mRestoredState.isTaskInitialized()) {
          final Task task = mRestoredState.getStateTask();
          Long duration = isChoosingStart ? task.getMinuteStart() : task.getMinuteEnd();

          int hour = 0;
          int minute = 0;
          if (duration != null) {
            hour = (int) (duration / 60);
            minute = (int) (duration % 60);
          }

          TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
              Long minutes = hourOfDay * 60L + minute;
              if (isChoosingStart) {
                setTaskStart(minutes);
              } else {
                setTaskEnd(minutes);
              }
            }
          };


          TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
              onTimeSetListener, hour, minute, true);
          timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
              dialog.cancel();
              dialog.dismiss();
            }
          });
          timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
              dialog.cancel();
              dialog.dismiss();
            }
          });
          timePickerDialog.setCanceledOnTouchOutside(true);
          timePickerDialog.setMessage(isChoosingStart ? getString(R.string.please_choose_start)
              : getString(R.string.please_choose_end));
          timePickerDialog.setTitle(null);
          timePickerDialog.show();
        }
      }
    };
  }

  private TextWatcher getNoteWatcher() {
    return new TextWatcher() {
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
    };
  }

  private View.OnClickListener getChooseDurationZeroListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setTaskDuration(null);
      }
    };
  }

  private View.OnClickListener getChooseDurationListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        ExceptionHelper.assertTrue("Button is illegal", v == mTextChooseDuration);

        if (mRestoredState.isTaskInitialized()) {
          int hour = 0;
          int minute = 0;
          if (mRestoredState.getStateTask().getDuration() != null) {
            hour = hour / 60;
            minute = hour % 60;
          }

          TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
              setTaskDuration(hourOfDay * 60L + minute);
            }
          };

          TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
              onTimeSetListener, hour, minute, true);
          timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
              dialog.cancel();
            }
          });
          timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
              dialog.cancel();
            }
          });
          timePickerDialog.setCanceledOnTouchOutside(true);
          timePickerDialog.setMessage(getString(R.string.please_choose_duration));
          timePickerDialog.setTitle(null);
          timePickerDialog.show();
        }
      }
    };
  }

  private View.OnClickListener getChooseDateListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        ExceptionHelper.assertTrue("Button is illegal", v == mTextChooseDay
            || v == mTextChooseMonth || v == mTextChooseWeek);

        if (mRestoredState.isTaskInitialized()) {
          final int year = DatetimeHelper.getCurrentYearNumber();
          final int month = DatetimeHelper.getCurrentMonthNumber();
          final int day = DatetimeHelper.getCurrentDayOfMonthNumber();

          final DatePicker datePicker = new DatePicker(getContext());
          datePicker.updateDate(year, month, day);

          new AlertDialog.Builder(getContext())
              .setTitle(getString(R.string.please_choose_date))
              .setPositiveButton(R.string.choose, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  Task.Type type = null;
                  String date = DatetimeHelper.getDate(datePicker.getYear(),
                      datePicker.getMonth() + 1, datePicker.getDayOfMonth());
                  if (v == mTextChooseDay) {
                    type = Task.Type.Day;
                    date = date;
                  } else if (v == mTextChooseWeek) {
                    type = Task.Type.Week;
                    date = DatetimeHelper.getWeekDate(date);
                  } else {
                    type = Task.Type.Month;
                    date = DatetimeHelper.getMonthDate(date);
                  }
                  ;

                  setTaskTypeAndDate(type, date);
                }
              })
              .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  // do nothing
                }
              })
              .setView(datePicker)
              .show();
        }
      }
    };
  }

  private View.OnClickListener getPatternListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Task.Type type = null;
        String date = null;
        if (v == mTextToNoDate) {
          type = Task.Type.NoDate;
          date = null;
        } else if (v == mTextToThisDay) {
          type = Task.Type.Day;
          date = DatetimeHelper.getCurrentDay();
        } else if (v == mTextToNextDay) {
          type = Task.Type.Day;
          date = DatetimeHelper.getUpDay(DatetimeHelper.getCurrentDay());
        } else if (v == mTextToThisWeek) {
          type = Task.Type.Week;
          date = DatetimeHelper.getCurrentWeek();
        } else if (v == mTextToNextWeek) {
          type = Task.Type.Week;
          date = DatetimeHelper.getUpWeek(DatetimeHelper.getCurrentWeek());
        } else if (v == mTextToThisMonth) {
          type = Task.Type.Month;
          date = DatetimeHelper.getCurrentMonth();
        } else if (v == mTextToNextMonth) {
          type = Task.Type.Month;
          date = DatetimeHelper.getUpMonth(DatetimeHelper.getCurrentMonth());
        }

        if (type != null) {
          setTaskTypeAndDate(type, date);
        } else {
          throw new IllegalStateException("Illegal pattern button");
        }
      }
    };
  }

  private TextWatcher getNameWatcher() {
    return new TextWatcher() {
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
    };
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
