package com.gawdl3y.android.tasktimer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.layout.TaskListItem;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.util.Log;

import java.util.List;

/**
 * The adapter to display a list of Tasks
 * @author Schuyler Cebulskie
 */
public class TaskListAdapter extends CheckableAdapter {
    private static final String TAG = "TaskListAdapter";

    private Context mContext;
    private List<Task> mTasks;
    private int mGroupPosition;

    /**
     * Fill constructor
     * @param context       The context of the adapter
     * @param tasks         The tasks to be displayed
     * @param groupPosition The position of the group that the list is for
     */
    public TaskListAdapter(Context context, List<Task> tasks, int groupPosition) {
        mContext = context;
        mTasks = tasks;
        mGroupPosition = groupPosition;
    }

    @Override
    public int getCount() {
        return mTasks.size();
    }

    @Override
    public Object getItem(int position) {
        Log.v(TAG, "Getting item #" + position);
        return mTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(position >= 0 && position < mTasks.size()) return mTasks.get(position).getId();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskListItem v;
        if(convertView != null && convertView instanceof TaskListItem) {
            Log.v(TAG, "Converting old view");
            v = (TaskListItem) convertView;
            v.setTask((Task) getItem(position));
            v.setChecked(isItemChecked(position));
            v.invalidate();
            v.buildTimer();
        } else {
            Log.v(TAG, "Getting new view");
            v = new TaskListItem(mContext, null, (Task) getItem(position));
            v.setChecked(isItemChecked(position));
        }

        v.setTag(R.id.tag_task, position);
        v.setTag(R.id.tag_group, mGroupPosition);
        return v;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
