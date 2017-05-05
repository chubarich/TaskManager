package com.danielkashin.taskorganiser.presentation_layer.view.main_drawer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.domain_layer.helper.DatetimeHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.presentation_layer.view.task_groups.TaskGroupsFragment;

public class MainDrawerActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, IToolbarContainer {

  private static final String KEY_TOOLBAR_LABEL = "TOOLBAR_LABEL";

  private Toolbar mToolbar;
  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mDrawerToggle;
  private NavigationView mNavigationView;
  private FrameLayout mFragmentContainer;


  // --------------------------------------- lifecycle --------------------------------------------

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_drawer);

    initializeView(savedInstanceState);
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mDrawerToggle.syncState();
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
    if (getSupportActionBar() != null && getSupportActionBar().getTitle() != null) {
      outState.putString(KEY_TOOLBAR_LABEL, getSupportActionBar().getTitle().toString());
    }
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
  public boolean onOptionsItemSelected(MenuItem item) {
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onNavigationItemSelected(final MenuItem item) {
    mDrawerLayout.closeDrawer(GravityCompat.START);

    Runnable workRunnable = new Runnable() {
      @Override
      public void run() {
        int id = item.getItemId();
        if (id == R.id.navigation_input) {

        } else if (id == R.id.navigation_week) {
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

  // ---------------------------------- IToolbarContainer -----------------------------------------

  @Override
  public void setToolbarLabel(String text) {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(text);
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
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
    mFragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

    setSupportActionBar(mToolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      if (savedInstanceState != null && savedInstanceState.containsKey(KEY_TOOLBAR_LABEL)) {
        getSupportActionBar().setTitle(savedInstanceState.getString(KEY_TOOLBAR_LABEL));
      } else {
        getSupportActionBar().setTitle("");
      }
    }
  }

  private void setListeners() {
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
    mDrawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
  }

}