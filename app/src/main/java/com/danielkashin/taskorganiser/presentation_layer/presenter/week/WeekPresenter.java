package com.danielkashin.taskorganiser.presentation_layer.presenter.week;

import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.week.IWeekView;


public class WeekPresenter extends Presenter<IWeekView> {



  public WeekPresenter( ) {

  }


  // -------------------------------------- lifecycle ---------------------------------------------

  @Override
  protected void onViewDetached() {

  }

  @Override
  protected void onViewAttached() {

  }

  @Override
  protected void onDestroyed() {

  }

  // --------------------------------------- inner types ------------------------------------------

  public static class Factory implements IPresenterFactory<WeekPresenter, IWeekView> {


    public Factory() {
    }

    @Override
    public WeekPresenter create() {
      return new WeekPresenter();
    }
  }
}
