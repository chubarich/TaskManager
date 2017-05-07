package com.danielkashin.taskorganiser.presentation_layer.view.tag_tasks;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.tag_tasks.TagTasksPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;


public class TagTasksFragment extends PresenterFragment<TagTasksPresenter, ITagTasksView>
    implements ITagTasksView {

  private RecyclerView mRecyclerView;


  public TagTasksFragment getInstance(String tagName) {
    TagTasksFragment tagTasksFragment = new TagTasksFragment();

    tagTasksFragment.setArguments(State.wrap(tagName));

    return new TagTasksFragment();
  }

  // ----------------------------------- PresenterFragment ----------------------------------------

  @Override
  protected ITagTasksView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<TagTasksPresenter, ITagTasksView> getPresenterFactory() {
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

  private static class State {

    static final String KEY_TAG_NAME = "KEY_TAG_NAME";

    private String tagName;


    State() {
    }

    void initializeWithBundle(Bundle bundle) {
      if (bundle != null && bundle.containsKey(KEY_TAG_NAME)) {
        tagName = bundle.getString(KEY_TAG_NAME);
      } else {
        tagName = null;
      }
    }

    String getTagName() {
      return tagName;
    }

    boolean isInitialized() {
      return tagName != null;
    }

    void wrap(Bundle outState) {
      if (isInitialized()) {
        outState.putString(KEY_TAG_NAME, tagName);
      }
    }

    static Bundle wrap(String tagName) {
      Bundle bundle = new Bundle();
      bundle.putString(KEY_TAG_NAME, tagName);
      return bundle;
    }
  }
}
