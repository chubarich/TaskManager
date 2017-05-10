package com.danielkashin.taskorganiser.presentation_layer.adapter.tags;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.util.ColorHelper;

import java.util.ArrayList;


public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagViewHolder>
    implements ITagsAdapter {

  private ArrayList<String> mTags;
  private Callbacks mCallbacks;


  public TagsAdapter() {
  }

  public TagsAdapter(ArrayList<String> tags) {
    mTags = tags;
  }

  @Override
  public void attachCallbacks(Callbacks callbacks) {
    mCallbacks = callbacks;
  }

  @Override
  public void setTags(ArrayList<String> tags) {
    mTags = tags;
    notifyDataSetChanged();
  }

  @Override
  public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new TagViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_tag, parent, false));
  }

  @Override
  public void onBindViewHolder(TagViewHolder holder, int position) {
    final String tag = mTags.get(holder.getAdapterPosition());

    holder.setName(tag);
    holder.setOnNameClickedListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mCallbacks != null) {
          mCallbacks.onTagClicked(tag);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return mTags == null ? 0 : mTags.size();
  }


  // --------------------------------------- inner types ------------------------------------------

  static class TagViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTextView;

    private TagViewHolder(View view) {
      super(view);
      nameTextView = (TextView) view.findViewById(R.id.text_name);
    }

    private void setName(String name) {
      nameTextView.setText(name);
      nameTextView.setTextColor(ColorHelper.getMaterialColor(name));
    }

    private void setOnNameClickedListener(View.OnClickListener listener) {
      nameTextView.setOnClickListener(listener);
    }

  }
}
