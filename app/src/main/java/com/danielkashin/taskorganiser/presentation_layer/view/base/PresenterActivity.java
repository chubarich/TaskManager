package com.danielkashin.taskorganiser.presentation_layer.view.base;


import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.PresenterLoader;


public abstract class PresenterActivity<P extends Presenter<V>, V extends IView>
    extends AppCompatActivity implements IView, LoaderManager.LoaderCallbacks<P> {

  private P mPresenter;

  private final Presenter<V> getPresenter() {
    return mPresenter;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutRes());

    // loader is created -> get presenter
    Loader loader = getSupportLoaderManager().getLoader(getActivityId());
    if (loader != null) {
      mPresenter = ((PresenterLoader<P, V>) loader).getPresenter();
    }

    // init loader
    if (mPresenter == null) {
      getSupportLoaderManager().initLoader(getActivityId(), null, this);
    }

    initializeView();
  }

  @Override
  public void onStart() {
    super.onStart();
    mPresenter.attachView(getViewInterface());
  }

  @Override
  public void onStop() {
    mPresenter.detachView();
    super.onStop();
  }

  // ------------------------------ LoaderManager.LoaderCallbacks ---------------------------------

  @Override
  public Loader<P> onCreateLoader(int i, Bundle bundle) {
    return new PresenterLoader<>(this, getPresenterFactory());
  }

  @Override
  public void onLoadFinished(Loader<P> loader, P presenter) {
    this.mPresenter = presenter;
  }

  @Override
  public void onLoaderReset(Loader<P> loader) {
    this.mPresenter = null;
  }

  // --------------------------------------- abstract ---------------------------------------------

  protected abstract V getViewInterface();

  protected abstract IPresenterFactory<P, V> getPresenterFactory();

  protected abstract int getActivityId();

  protected abstract int getLayoutRes();

  protected abstract void initializeView();

}
