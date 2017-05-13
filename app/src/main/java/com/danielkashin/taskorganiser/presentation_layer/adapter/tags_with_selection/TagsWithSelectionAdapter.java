package com.danielkashin.taskorganiser.presentation_layer.adapter.tags_with_selection;


import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.util.ColorHelper;

import java.util.ArrayList;

public class TagsWithSelectionAdapter
    extends RecyclerView.Adapter<TagsWithSelectionAdapter.TagWithSelectionViewHolder>
    implements ITagsWithSelectionAdapter {

  ArrayList<Pair<String, Boolean>> mTags;

  public TagsWithSelectionAdapter() {

  }

  // ------------------------------ ITagsWithSelectionAdapter -------------------------------------

  @Override
  public void initialize(ArrayList<String> tags, ArrayList<String> selectedTags) {
    mTags = new ArrayList<>();

    for (String tag : tags) {
      boolean selected = false;
      for (String selectedTag : selectedTags) {
        if (selectedTag.equals(tag)) {
          selected = true;
          break;
        }
      }

      mTags.add(new Pair<>(tag, selected));
    }

    notifyDataSetChanged();
  }

  @Override
  public ArrayList<String> getSelectedTags() {
    ArrayList<String> selectedTags = new ArrayList<>();
    for (Pair<String, Boolean> tag : mTags) {
      if (tag.second) selectedTags.add(tag.first);
    }

    return selectedTags;
  }

  // --------------------------------- RecyclerView.Adapter ---------------------------------------

  @Override
  public TagWithSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new TagWithSelectionViewHolder(LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.item_tag_with_selection, parent, false));
  }

  @Override
  public void onBindViewHolder(TagWithSelectionViewHolder holder, final int position) {
    final Pair<String, Boolean> tag = mTags.get(position);

    holder.setName(tag.first);
    holder.setChecked(tag.second);

    holder.setOnClickedListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mTags.set(position, new Pair<>(tag.first, isChecked));
      }
    });
  }

  @Override
  public int getItemCount() {
    return mTags == null ? 0 : mTags.size();
  }

  // -------------------------------------- inner types -------------------------------------------

  static class TagWithSelectionViewHolder extends RecyclerView.ViewHolder {

    private TextView textName;
    private ToggleButton toggleButton;
    private CompoundButton.OnCheckedChangeListener listener;

    private TagWithSelectionViewHolder(View view) {
      super(view);
      toggleButton = (ToggleButton) view.findViewById(R.id.toggle_button);
      textName = (TextView) view.findViewById(R.id.text_name);
    }

    private void setName(String name) {
      textName.setTextColor(ColorHelper.getMaterialColor(name));
      textName.setText(name);
    }

    private void setChecked(boolean checked) {
      toggleButton.setOnCheckedChangeListener(null);
      toggleButton.setChecked(checked);
      setListenerLocal();
    }

    private void setOnClickedListener(CompoundButton.OnCheckedChangeListener listener) {
      this.listener = listener;
      setListenerLocal();
    }

    private void setListenerLocal() {
      toggleButton.setOnCheckedChangeListener(listener);
    }
  }
}
