package com.danielkashin.taskorganiser.presentation_layer.adapter.tags_with_selection;


import java.util.ArrayList;

public interface ITagsWithSelectionAdapter {

  void initialize(ArrayList<String> tags, ArrayList<String> selectedTags);

  ArrayList<String> getSelectedTags();

}
