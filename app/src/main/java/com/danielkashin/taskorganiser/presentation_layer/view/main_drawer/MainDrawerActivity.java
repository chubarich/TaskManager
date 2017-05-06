package com.danielkashin.taskorganiser.presentation_layer.view.main_drawer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.domain_layer.helper.DatetimeHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.presentation_layer.view.task_groups.IDateContainer;
import com.danielkashin.taskorganiser.presentation_layer.view.task_groups.TaskGroupsFragment;


public class MainDrawerActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, IToolbarContainer, ICalendarWalker {

  private static final String KEY_TOOLBAR_LABEL = "TOOLBAR_LABEL";

  private Toolbar mToolbar;
  private TextView mTextToolbar;
  private ImageView mImageToolbarParent;
  private ImageView mImageToolbarUp;
  private ImageView mImageToolbarDown;
  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mDrawerToggle;
  private NavigationView mNavigationView;
  private FrameLayout mFragmentContainer;
  private boolean mToolbarNavigationListenerIsRegistered;


  // --------------------------------------- lifecycle --------------------------------------------

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_drawer);

    initializeView(savedInstanceState);
  }

  @Override
  protected void onStart() {
    super.onStart();

    if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
      addFragment(TaskGroupsFragment.getInstance(DatetimeHelper.getCurrentWeek(), Task.Type.Day), false);
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
    mDrawerLayout.closeDrawer(GravityCompat.START);

    Runnable workRunnable = new Runnable() {
      @Override
      public void run() {
        int id = item.getItemId();
        if (id == R.id.navigation_week) {
          addFragment(TaskGroupsFragment.getInstance(DatetimeHelper.getCurrentWeek(), Task.Type.Day), false);
        } else if (id == R.id.navigation_month) {
          addFragment(TaskGroupsFragment.getInstance(DatetimeHelper.getCurrentWeek(), Task.Type.Week), false);
        } else if (id == R.id.navigation_year) {
          addFragment(TaskGroupsFragment.getInstance(DatetimeHelper.getCurrentWeek(), Task.Type.Month), false);
        }
      }
    };

    int DELAY = 350;
    Handler handler = new Handler(Looper.getMainLooper());
    handler.postDelayed(workRunnable, DELAY);

    return true;
  }

  // ----------------------------------- ICalendarWalker ------------------------------------------

  @Override
  public void onOpenChildDate(String date, Task.Type type) {
    addFragment(TaskGroupsFragment.getInstance(date, type), true);
  }

  // ---------------------------------- IToolbarContainer -----------------------------------------

  @Override
  public void setToolbar(String text, boolean showCalendarParentIcon, boolean showCalendarNavigateIcons) {
    mTextToolbar.setText(text);

    if (showCalendarParentIcon) {
      mImageToolbarParent.setVisibility(View.VISIBLE);
    } else {
      mImageToolbarParent.setVisibility(View.GONE);
    }

    if (showCalendarNavigateIcons) {
      mImageToolbarDown.setVisibility(View.VISIBLE);
      mImageToolbarUp.setVisibility(View.VISIBLE);
    } else {
      mImageToolbarDown.setVisibility(View.GONE);
      mImageToolbarUp.setVisibility(View.GONE);
    }

  }

  // --------------------------------------- private ----------------------------------------------

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

  private void initializeView(Bundle savedInstanceState) {
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    mTextToolbar = (TextView) findViewById(R.id.text_toolbar);
    mImageToolbarParent = (ImageView) findViewById(R.id.image_calendar_parent);
    mImageToolbarDown = (ImageView) findViewById(R.id.image_calendar_down);
    mImageToolbarUp = (ImageView) findViewById(R.id.image_calendar_up);
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
    mFragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

    mDrawerToggle.syncState();
  }

  private void setListeners() {
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