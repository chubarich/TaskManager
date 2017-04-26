package com.danielkashin.taskorganiser.presentation_layer.view.main_drawer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.danielkashin.taskorganiser.R;

public class MainDrawerActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private Toolbar mToolbar;
  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mDrawerToggle;
  private NavigationView mNavigationView;


  // --------------------------------------- lifecycle --------------------------------------------

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_drawer);

    initializeView();
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mDrawerToggle.syncState();
  }

  @Override
  protected void onStart() {
    super.onStart();

    setListeners();
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
  public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.navigation_input) {

    } else if (id == R.id.navigation_day) {

    } else if (id == R.id.navigation_week) {

    } else if (id == R.id.navigation_month) {

    }

    mDrawerLayout.closeDrawer(GravityCompat.START);

    return true;
  }

  // --------------------------------------- private ----------------------------------------------

  private void initializeView() {
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
  }

  private void setListeners() {
    setSupportActionBar(mToolbar);
    mDrawerLayout.addDrawerListener(mDrawerToggle);
    mNavigationView.setNavigationItemSelectedListener(this);
  }

}