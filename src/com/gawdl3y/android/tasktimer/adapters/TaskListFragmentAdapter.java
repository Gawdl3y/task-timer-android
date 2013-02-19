package com.gawdl3y.android.tasktimer.adapters;

import java.util.ArrayList;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.gawdl3y.android.tasktimer.classes.Group;
import com.gawdl3y.android.tasktimer.fragments.TaskListFragment;

public class TaskListFragmentAdapter extends FragmentPagerAdapter {
	private static final String TAG = "TaskListFragmentAdapter";
	
	public ArrayList<Group> groups;

	/**
	 * Fill constructor
	 * @param fm The FragmentManager to use
	 * @param groups The groups to display
	 */
	public TaskListFragmentAdapter(FragmentManager fm, ArrayList<Group> groups) {
		super(fm);
		this.groups = groups;
	}

	/* (non-Javadoc)
	 * Gets the fragment at position
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public TaskListFragment getItem(int position) {
		Log.v(TAG, "Getting item " + position);
		return TaskListFragment.newInstance(groups.get(position));
	}
	
	/* (non-Javadoc)
	 * Gets the position of a fragment
	 * @see android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
	 */
	@Override
	public int getItemPosition(Object o) {
		TaskListFragment item = (TaskListFragment) o;
		int position = groups.indexOf(item.group);
		
		if(position >= 0) {
			Log.v(TAG, "Item " + position + " found: " + item.group.toString());
			return position;
		} else {
			Log.v(TAG, "Item " + position + " not found");
			return POSITION_NONE;
		}
	}

	/* (non-Javadoc)
	 * Gets the count of fragments
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return groups.size();
	}

	/* (non-Javadoc)
	 * Gets the title of a fragment
	 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		return groups.get(position).getName();
	}
}
