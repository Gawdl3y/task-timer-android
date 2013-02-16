package com.gawdl3y.android.tasktimer.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.classes.Task;

/**
 * @author Schuyler Cebulskie
 * The adapter to display a list of tasks
 */
public class TaskAdapter extends ArrayAdapter<Task> {
	private static final String TAG = "TaskAdapter";
	
	public int group;
	
	public TaskAdapter(Context context, ArrayList<Task> tasks, int group) {
		super(context, R.layout.task_list_item, R.id.task_name, tasks);
		this.group = group;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = super.getView(position, convertView, parent);
		row.setTag(R.id.tag_task, position);
		row.setTag(R.id.tag_group, group);
		Task.updateView(getItem(position), row);
		
		Log.v(TAG, "Getting view");
		return row;
	}
	
}
