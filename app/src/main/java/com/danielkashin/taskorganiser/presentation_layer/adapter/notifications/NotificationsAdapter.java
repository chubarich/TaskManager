package com.danielkashin.taskorganiser.presentation_layer.adapter.notifications;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.util.DatetimeHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>
    implements INotificationsAdapter {

  private ArrayList<Task> mTaskNotifications;
  private INotificationsAdapter.Callbacks mCallbacks;


  public NotificationsAdapter() {
    mTaskNotifications = new ArrayList<>();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_notification, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Task task = mTaskNotifications.get(position);

    holder.setTextName(task.getName());
    holder.setTextTime(task.getNotificationTimestampToString("Время", "не установлено"));
    holder.setOnImageDeleteClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mCallbacks != null) {
          mCallbacks.onDeleteNotificationButtonClicked(task);
        }
      }
    });
    holder.setOnViewClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mCallbacks != null) {
          mCallbacks.onNotificationClicked(task);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return mTaskNotifications == null ? 0 : mTaskNotifications.size();
  }

  // ------------------------------------ INotificationsAdapter -----------------------------------

  @Override
  public void setTaskNotifications(ArrayList<Task> taskNotifications) {
    mTaskNotifications.clear();

    for (Task task : taskNotifications) {
      if (task.getNotificationTimestamp() != null) {
        mTaskNotifications.add(task);
      }
    }

    Collections.sort(mTaskNotifications, new Comparator<Task>() {
      @Override
      public int compare(Task o1, Task o2) {
        if (o1.getNotificationTimestamp() != null && o2.getNotificationTimestamp() == null) {
          return 1;
        } else if (o2.getNotificationTimestamp() != null && o1.getNotificationTimestamp() == null) {
          return -1;
        } else if (o1.getNotificationTimestamp() == null && o1.getNotificationTimestamp() == null) {
          return 0;
        }

        if (o1.getNotificationTimestamp() > o2.getNotificationTimestamp()) {
          return 1;
        } else {
          return -1;
        }
      }
    });

    notifyDataSetChanged();
  }

  @Override
  public void attachCallbacks(Callbacks callbacks) {
    mCallbacks = callbacks;
  }

  @Override
  public void detachCallbacks() {
    mCallbacks = null;
  }

  // ----------------------------------------- inner types ----------------------------------------

  class ViewHolder extends RecyclerView.ViewHolder {

    private TextView textName;
    private TextView textTime;
    private ImageView imageDelete;
    private View view;

    private ViewHolder(View view) {
      super(view);
      this.view = view;
      this.textName = (TextView) view.findViewById(R.id.text_name);
      this.textTime = (TextView) view.findViewById(R.id.text_time);
      this.imageDelete = (ImageView) view.findViewById(R.id.image_delete);
    }

    private void setTextName(String name) {
      textName.setText(name);
    }

    private void setTextTime(String time) {
      textTime.setText(time);
    }

    private void setOnImageDeleteClickListener(View.OnClickListener listener) {
      imageDelete.setOnClickListener(listener);
    }

    private void setOnViewClickListener(View.OnClickListener listener) {
      view.setOnClickListener(listener);
    }
  }
}
