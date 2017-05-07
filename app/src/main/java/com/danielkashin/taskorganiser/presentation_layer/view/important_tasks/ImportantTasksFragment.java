package com.danielkashin.taskorganiser.presentation_layer.view.important_tasks;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.important_tasks.ImportantTasksPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;


public class ImportantTasksFragment extends PresenterFragment<ImportantTasksPresenter, IImportantTasksView>
    implements IImportantTasksView {

  private RecyclerView mRecyclerView;


  // ------------------------------------- PresenterFragment --------------------------------------

  @Override
  protected IImportantTasksView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<ImportantTasksPresenter, IImportantTasksView> getPresenterFactory() {
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
}
