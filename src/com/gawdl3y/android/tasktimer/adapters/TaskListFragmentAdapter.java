package com.gawdl3y.android.tasktimer.adapters;

import java.util.ArrayList;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.gawdl3y.android.tasktimer.classes.Group;
import com.gawdl3y.android.tasktimer.fragments.TaskListFragment;

public class TaskListFragmentAdapter extends FragmentPagerAdapter {
	private static final String TAG = "TaskListFragmentAdapter";
	
	private ArrayList<Group> groups;

	public TaskListFragmentAdapter(FragmentManager fm, ArrayList<Group> groups) {
		super(fm);
		this.groups = groups;
	}

	@Override
	public TaskListFragment getItem(int position) {
		Log.v(TAG, "Getting item");
		return TaskListFragment.newInstance(groups.get(position));
	}

	@Override
	public int getCount() {
		return groups.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return groups.get(position).getName();
	}
}
