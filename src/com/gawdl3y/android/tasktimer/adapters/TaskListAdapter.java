package com.gawdl3y.android.tasktimer.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.layout.TaskListItem;
import com.gawdl3y.android.tasktimer.pojos.Task;

/**
 * The adapter to display a list of Tasks
 * @author Schuyler Cebulskie
 */
public class TaskListAdapter extends BaseAdapter {
	private static final String TAG = "TaskListAdapter";
	
	private Context context;
	private ArrayList<Task> tasks;
	private int group;
	
	/**
	 * Fill constructor
	 * @param context The context of the adapter
	 * @param tasks The tasks to be displayed
	 * @param group The position of the group that the list is for
	 */
	public TaskListAdapter(Context context, ArrayList<Task> tasks, int group) {
		this.context = context;
		this.tasks = tasks;
		this.group = group;
	}

	/* (non-Javadoc)
	 * Gets the number of items
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return tasks.size();
	}

	/* (non-Javadoc)
	 * Gets the item at the specified position
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		if(TaskTimerApplication.DEBUG) Log.v(TAG, "Getting item #" + position);
		return tasks.get(position);
	}

	/* (non-Javadoc)
	 * Gets the unique ID for the item at the specified position
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		if(position >= 0 && position < tasks.size()) return tasks.get(position).getId();
		return 0;
	}
	
	/* (non-Javadoc)
	 * Gets the view for the item at the specified position
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(TaskTimerApplication.DEBUG) Log.v(TAG, "Getting view");
		
		TaskListItem v = new TaskListItem(context, (Task) getItem(position));
		v.setTag(R.id.tag_task, position);
		v.setTag(R.id.tag_group, group);
		return v;
	}
}
