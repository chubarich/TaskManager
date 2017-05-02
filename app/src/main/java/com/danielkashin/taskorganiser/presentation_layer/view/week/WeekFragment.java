package com.danielkashin.taskorganiser.presentation_layer.view.week;


import android.view.View;

import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.week.WeekPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;

public class WeekFragment extends PresenterFragment<WeekPresenter, IWeekView> implements IWeekView {






  // ------------------------------------- PresenterFragment --------------------------------------

  @Override
  protected IWeekView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<WeekPresenter, IWeekView> getPresenterFactory() {
    return null;
  }

  @Override
  protected int getFragmentId() {
    return 0;
  }

  @Override
  protected int getLayoutRes() {
    return 0;
  }

  @Override
  protected void initializeView(View view) {

  }

  // ---------------------------------------- inner types -----------------------------------------
}
