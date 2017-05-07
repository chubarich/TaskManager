package com.danielkashin.taskorganiser.presentation_layer.adapter.tags;

import com.danielkashin.taskorganiser.data_layer.entities.local.data.Tag;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups.ITaskGroupsAdapter;

import java.util.ArrayList;


public interface ITagsAdapter {

  void setTags(ArrayList<String> mTags);

  void attachCallbacks(Callbacks callbacks);


  interface Callbacks {

    void onTagClicked(String tagName);

  }
}
