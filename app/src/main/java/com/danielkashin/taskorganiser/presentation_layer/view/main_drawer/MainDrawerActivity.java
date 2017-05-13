package com.danielkashin.taskorganiser.presentation_layer.view.main_drawer;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteDoneTasksUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteTagUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteTaskUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.view.task.ITaskView;
import com.danielkashin.taskorganiser.presentation_layer.view.task.TaskFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.typed_tasks.ITypedTasksView;
import com.danielkashin.taskorganiser.util.ColorHelper;
import com.danielkashin.taskorganiser.util.DatetimeHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.domain_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTagsUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTagUseCase;
import com.danielkashin.taskorganiser.presentation_layer.application.ITasksLocalServiceProvider;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.main_drawer.IMainDrawerPresenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.main_drawer.MainDrawerPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterActivity;
import com.danielkashin.taskorganiser.presentation_layer.view.typed_tasks.TypedTasksFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.tag_tasks.ITagTasksView;
import com.danielkashin.taskorganiser.presentation_layer.view.tag_tasks.TagTasksFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.task_groups.IDateContainer;
import com.danielkashin.taskorganiser.presentation_layer.view.task_groups.TaskGroupsFragment;

import static com.danielkashin.taskorganiser.presentation_layer.view.typed_tasks.TypedTasksFragment.State;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class MainDrawerActivity extends PresenterActivity<MainDrawerPresenter, IMainDrawerView>
    implements IMainDrawerView, ICalendarWalker, IToolbarContainer, ITaskViewOpener,
    NavigationView.OnNavigationItemSelectedListener {

  private static final String KEY_TOOLBAR_LABEL = "TOOLBAR_LABEL";
  private static final int ID_SUB_MENU = 1243;

  private Toolbar mToolbar;
  private TextView mTextToolbar;
  private ImageView mImageToolbarParent;
  private ImageView mImageToolbarUp;
  private ImageView mImageToolbarDown;
  private ImageView mImageToolbarDone;
  private ImageView mImageToolbarDelete;

  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mDrawerToggle;
  private NavigationView mNavigationView;
  private FrameLayout mFragmentContainer;
  private boolean mToolbarNavigationListenerIsRegistered;


  // --------------------------------------- lifecycle --------------------------------------------

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeViewMore(savedInstanceState);
  }

  @Override
  protected void onStart() {
    super.onStart();

    ((IMainDrawerPresenter) getPresenter()).onGetTags();

    if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
      openDefaultDate();
    }

    setListeners();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(KEY_TOOLBAR_LABEL, mTextToolbar.getText().toString());
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public void onBackPressed() {
    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
      mDrawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onNavigationItemSelected(final MenuItem item) {
    mImageToolbarDown.setClickable(false);
    mImageToolbarUp.setClickable(false);
    mImageToolbarParent.setClickable(false);
    mImageToolbarDone.setClickable(false);
    mImageToolbarDelete.setClickable(false);

    mDrawerLayout.closeDrawer(GravityCompat.START);

    if (item.getTitle().toString().equals(getString(R.string.new_tag))) {
      showCreateTagDialog();
    } else {
      Runnable workRunnable = new Runnable() {
        @Override
        public void run() {
          int id = item.getItemId();
          if (id == R.id.navigation_no_date) {
            addFragment(TypedTasksFragment.getInstance(State.Type.NoDate), false);
          } else if (id == R.id.navigation_week) {
            addFragment(TaskGroupsFragment.getInstance(DatetimeHelper.getCurrentWeek(), Task.Type.Day), false);
          } else if (id == R.id.navigation_month) {
            addFragment(TaskGroupsFragment.getInstance(DatetimeHelper.getCurrentMonth(), Task.Type.Week), false);
          } else if (id == R.id.navigation_year) {
            addFragment(TaskGroupsFragment.getInstance(DatetimeHelper.getCurrentMonth(), Task.Type.Month), false);
          } else if (id == R.id.navigation_important) {
            addFragment(TypedTasksFragment.getInstance(State.Type.Important), false);
          } else if (id == R.id.navigation_done) {
            addFragment(TypedTasksFragment.getInstance(State.Type.Done), false);
          } else {
            addFragment(TagTasksFragment.getInstance(item.getTitle().toString()), false);
          }
        }
      };

      int DELAY = 350;
      Handler handler = new Handler(Looper.getMainLooper());
      handler.postDelayed(workRunnable, DELAY);
    }

    return true;
  }

  // ---------------------------------- IMainDrawerView -------------------------------------------

  @Override
  public void showSavedToast() {
    Toast.makeText(this, getString(R.string.task_saved), Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showNotSavedToast() {
    Toast.makeText(this, getString(R.string.task_not_saved), Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showTaskNotDeletedToast() {
    Toast.makeText(this, getString(R.string.task_not_deleted), Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showTaskDeletedToast() {
    Toast.makeText(this, getString(R.string.task_deleted), Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showTagAlreadyExists() {
    showAlert(getString(R.string.tag_already_exists), null);
  }

  @Override
  public void setTags(ArrayList<String> tags) {
    if (mNavigationView.getMenu().findItem(ID_SUB_MENU) != null) {
      mNavigationView.getMenu().removeItem(ID_SUB_MENU);
    }

    SubMenu subMenu = mNavigationView.getMenu().addSubMenu(0, ID_SUB_MENU, 0, getString(R.string.tags));
    for (int i = 0; i < tags.size(); ++i) {
      MenuItem menuItem = subMenu.add(tags.get(i));
      Drawable drawable = ContextCompat.getDrawable(this, R.drawable.circle);
      drawable.setColorFilter(ColorHelper.getMaterialColor(tags.get(i)), PorterDuff.Mode.SRC_ATOP);
      menuItem.setIcon(drawable);
    }

    subMenu.add(getString(R.string.new_tag));
  }

  // --------------------------------- PresenterActivity ------------------------------------------

  @Override
  protected IMainDrawerView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<MainDrawerPresenter, IMainDrawerView> getPresenterFactory() {
    ITasksLocalService tasksLocalService = ((ITasksLocalServiceProvider) getApplication())
        .getTasksLocalService();

    ITasksRepository tasksRepository = TasksRepository.Factory.create(tasksLocalService);

    GetTagsUseCase getTagsUseCase = new GetTagsUseCase(tasksRepository, AsyncTask.THREAD_POOL_EXECUTOR);
    SaveTagUseCase saveTagUseCase = new SaveTagUseCase(tasksRepository, AsyncTask.THREAD_POOL_EXECUTOR);
    DeleteTagUseCase deleteTagUseCase = new DeleteTagUseCase(tasksRepository, AsyncTask.THREAD_POOL_EXECUTOR);
    DeleteDoneTasksUseCase deleteDoneTasksUseCase = new DeleteDoneTasksUseCase(tasksRepository, AsyncTask.THREAD_POOL_EXECUTOR);
    SaveTaskUseCase saveTaskUseCase = new SaveTaskUseCase(tasksRepository, AsyncTask.THREAD_POOL_EXECUTOR);
    DeleteTaskUseCase deleteTaskUseCase = new DeleteTaskUseCase(tasksRepository, AsyncTask.THREAD_POOL_EXECUTOR);

    return new MainDrawerPresenter.Factory(getTagsUseCase, saveTagUseCase, deleteTagUseCase,
        deleteDoneTasksUseCase, saveTaskUseCase, deleteTaskUseCase);
  }

  @Override
  protected int getActivityId() {
    return this.getClass().getSimpleName().hashCode();
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.activity_main_drawer;
  }

  @Override
  protected void initializeView() {
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    mTextToolbar = (TextView) findViewById(R.id.text_toolbar);
    mImageToolbarParent = (ImageView) findViewById(R.id.image_calendar_parent);
    mImageToolbarDown = (ImageView) findViewById(R.id.image_calendar_down);
    mImageToolbarUp = (ImageView) findViewById(R.id.image_calendar_up);
    mImageToolbarDone = (ImageView) findViewById(R.id.image_done);
    mImageToolbarDelete = (ImageView) findViewById(R.id.image_delete);
  }

  // ----------------------------------- ITaskViewOpener ------------------------------------------

  @Override
  public void onOpenTaskView(Task.Type type, String UUID) {
    addFragment(TaskFragment.getInstance(type, UUID), true);
  }

  // ----------------------------------- ITagViewOpener -------------------------------------------

  @Override
  public void onOpenTagView(String tagName) {
    addFragment(TagTasksFragment.getInstance(tagName), true);
  }

  // ----------------------------------- ICalendarWalker ------------------------------------------

  @Override
  public void onOpenChildDate(String date, Task.Type type) {
    addFragment(TaskGroupsFragment.getInstance(date, type), true);
  }

  @Override
  public void openDefaultDate() {
    addFragment(TaskGroupsFragment.getInstance(DatetimeHelper.getCurrentWeek(), Task.Type.Day), false);
  }

  // ---------------------------------- IToolbarContainer -----------------------------------------

  @Override
  public void setToolbar(String text, boolean showCalendarParentIcon, boolean showCalendarNavigateIcons,
                         boolean showSaveIcon, boolean showDeleteIcon) {

    mTextToolbar.setText(text);

    if (showCalendarParentIcon) {
      mImageToolbarParent.setVisibility(View.VISIBLE);
      mImageToolbarParent.setClickable(true);
    } else {
      mImageToolbarParent.setVisibility(View.GONE);
    }

    if (showCalendarNavigateIcons) {
      mImageToolbarDown.setVisibility(View.VISIBLE);
      mImageToolbarUp.setVisibility(View.VISIBLE);
      mImageToolbarDown.setClickable(true);
      mImageToolbarUp.setClickable(true);
    } else {
      mImageToolbarDown.setVisibility(View.GONE);
      mImageToolbarUp.setVisibility(View.GONE);
    }

    if (showSaveIcon) {
      mImageToolbarDone.setVisibility(View.VISIBLE);
      mImageToolbarDone.setClickable(true);
    } else {
      mImageToolbarDone.setVisibility(View.GONE);
    }

    if (showDeleteIcon) {
      mImageToolbarDelete.setVisibility(View.VISIBLE);
      mImageToolbarDelete.setClickable(true);
    } else {
      mImageToolbarDelete.setVisibility(View.GONE);
    }
  }

  // ----------------------------------- PresenterActivity ----------------------------------------


  // --------------------------------------- private ----------------------------------------------

  private void showAlert(String message, String title) {
    new AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(true)
        .create()
        .show();
  }

  private void addFragment(Fragment fragment, boolean addToBackStack) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

    transaction.replace(R.id.fragment_container, fragment);

    if (addToBackStack) {
      transaction.addToBackStack(null);
    } else if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
      getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    transaction.commit();
  }

  private void showDeleteTagDialog(final String tagName) {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    alertDialog.setTitle(getString(R.string.tag_deletion));
    alertDialog.setMessage(getString(R.string.tag_deletion_message));

    alertDialog.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        ((IMainDrawerPresenter) getPresenter()).onDeleteTag(tagName);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
          getSupportFragmentManager().popBackStack();
        } else {
          addFragment(TaskGroupsFragment.getInstance(DatetimeHelper.getCurrentWeek(), Task.Type.Day), false);
        }
      }
    });

    alertDialog.setNegativeButton(getString(R.string.cancel),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });

    alertDialog.show();
  }


  private void showDeleteDoneTasksDialog() {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    alertDialog.setTitle(getString(R.string.done_deletion));
    alertDialog.setMessage(getString(R.string.done_deletion_message));

    alertDialog.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        ((IMainDrawerPresenter) getPresenter()).onDeleteDoneTasks();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
          getSupportFragmentManager().popBackStack();
        } else {
          addFragment(TaskGroupsFragment.getInstance(DatetimeHelper.getCurrentWeek(), Task.Type.Day), false);
        }
      }
    });

    alertDialog.setNegativeButton(getString(R.string.cancel),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });

    alertDialog.show();
  }

  private void showCreateTagDialog() {
    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setPadding(30, 0, 30, 0);

    final EditText tagEditText = new EditText(this);
    tagEditText.setHint(getString(R.string.tag));
    tagEditText.setHintTextColor(ContextCompat.getColor(this, R.color.grey));
    layout.addView(tagEditText);

    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    alertDialog.setTitle(getString(R.string.new_tag));
    alertDialog.setView(layout);

    DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        final String tag = tagEditText.getText().toString().trim();
        if (Task.tagIsValid(tag)) {
          ((IMainDrawerPresenter) getPresenter()).onSaveTag(tag);
        } else {
          showAlert(getString(R.string.tag_parse_error), null);
        }
      }
    };
    alertDialog.setPositiveButton(getString(R.string.save), positiveListener);

    alertDialog.setNegativeButton(getString(R.string.cancel),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });

    alertDialog.show();
  }

  private void initializeViewMore(Bundle savedInstanceState) {
    if (savedInstanceState != null && savedInstanceState.containsKey(KEY_TOOLBAR_LABEL)) {
      mTextToolbar.setText(savedInstanceState.getString(KEY_TOOLBAR_LABEL));
    } else {
      mTextToolbar.setText("");
    }
    setSupportActionBar(mToolbar);

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
    mNavigationView.setItemIconTintList(null);

    mFragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

    mDrawerToggle.syncState();
  }

  private void setListeners() {
    mImageToolbarDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null) {
          if (fragment instanceof ITagTasksView) {
            String tagName = ((ITagTasksView) fragment).getTagName();
            showDeleteTagDialog(tagName);
          } else if (fragment instanceof ITypedTasksView && ((ITypedTasksView) fragment).getType() == State.Type.Done) {
            showDeleteDoneTasksDialog();
          } else if (fragment instanceof ITaskView) {
            Task task = ((ITaskView) fragment).getCurrentTask();
            if (task != null) {
              ((IMainDrawerPresenter) getPresenter()).onDeleteTask(task.getType(), task.getUUID());
              if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
              } else {
                addFragment(TaskGroupsFragment.getInstance(DatetimeHelper.getCurrentWeek(), Task.Type.Day), false);
              }
            }
          } else {
            mImageToolbarDelete.setVisibility(View.GONE);
          }
        }
      }
    });

    mImageToolbarDone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null) {
          if (fragment instanceof ITaskView) {
            Task task = ((ITaskView) fragment).getCurrentTask();
            if (task != null && task.isValid()) {
              ((IMainDrawerPresenter) getPresenter()).onSaveTask(task);
            } else {
              Toast.makeText(MainDrawerActivity.this, getString(R.string.could_not_save_task),
                  Toast.LENGTH_SHORT).show();
            }
          } else {
            mImageToolbarDelete.setVisibility(View.GONE);
          }
        }
      }
    });


    mImageToolbarParent.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment topFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (topFragment != null && topFragment instanceof IDateContainer) {
          Pair<String, Task.Type> nextDate = ((IDateContainer) topFragment).getParentDate();
          if (nextDate != null) {
            addFragment(TaskGroupsFragment.getInstance(nextDate.first, nextDate.second), true);
          }
        }
      }
    });

    mImageToolbarUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment topFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (topFragment != null && topFragment instanceof IDateContainer) {
          Pair<String, Task.Type> nextDate = ((IDateContainer) topFragment).getUpDate();
          if (nextDate != null) {
            addFragment(TaskGroupsFragment.getInstance(nextDate.first, nextDate.second), false);
          }
        }
      }
    });

    mImageToolbarDown.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment topFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (topFragment != null && topFragment instanceof IDateContainer) {
          Pair<String, Task.Type> nextDate = ((IDateContainer) topFragment).getDownDate();
          if (nextDate != null) {
            addFragment(TaskGroupsFragment.getInstance(nextDate.first, nextDate.second), false);
          }
        }
      }
    });

    mDrawerLayout.addDrawerListener(mDrawerToggle);
    mNavigationView.setNavigationItemSelectedListener(this);

    getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
      @Override
      public void onBackStackChanged() {
        setCurrentNavIcon();
      }
    });
    setCurrentNavIcon();
  }

  private void setCurrentNavIcon() {
    int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
    if (backStackEntryCount == 0) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      mDrawerToggle.setDrawerIndicatorEnabled(true);

      mToolbarNavigationListenerIsRegistered = false;
    } else {
      mDrawerToggle.setDrawerIndicatorEnabled(false);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      if (!mToolbarNavigationListenerIsRegistered) {
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onBackPressed();
          }
        });

        mToolbarNavigationListenerIsRegistered = true;
      }
    }
  }

}