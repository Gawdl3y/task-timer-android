package com.gawdl3y.android.tasktimer.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import com.gawdl3y.android.tasktimer.layout.TaskListFragment;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.utilities.Log;

import java.util.ArrayList;

/**
 * The adapter to display a ViewPager of Groups
 * @author Schuyler Cebulskie
 */
public class TaskListFragmentAdapter extends NewFragmentStatePagerAdapter {
	private static final String TAG = "TaskListFragmentAdapter";
	
	public ArrayList<Group> groups;

	/**
	 * Fill constructor
	 * @param fm The FragmentManager to use
	 * @param groups The Groups to display
	 */
	public TaskListFragmentAdapter(FragmentManager fm, ArrayList<Group> groups) {
		super(fm);
		this.groups = groups;
	}
	
	/* (non-Javadoc)
	 * Gets the count of fragments
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return groups != null ? groups.size() : 0;
	}
	
	/* (non-Javadoc)
	 * Gets the title of a fragment
	 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		return groups.get(position).getName();
	}

	/* (non-Javadoc)
	 * Gets the fragment at position
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public TaskListFragment getItem(int position) {
		Log.v(TAG, "Getting item #" + position);
		return TaskListFragment.newInstance(groups.get(position));
	}
	
	/* (non-Javadoc)
	 * Gets a unique ID for a fragment
	 * @see com.gawdl3y.android.tasktimer.adapters.NewFragmentStatePagerAdapter#getItemId(int)
	 */
	@Override
	public int getItemId(int position) {
		return groups.get(position).getId();
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
			Log.v(TAG, "Item found at index " + position + ": " + item.group.toString());
			return position;
		} else {
			Log.v(TAG, "Item not found");
			return POSITION_NONE;
		}
	}
	
	/* (non-Javadoc)
	 * Saves the state
	 * @see com.gawdl3y.android.tasktimer.adapters.NewFragmentStatePagerAdapter#saveState()
	 */
	@Override
	public Parcelable saveState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable("super", super.saveState());
		bundle.putParcelableArrayList("groups", groups);
		return bundle;
	}
	
	/* (non-Javadoc)
	 * Restores the state
	 * @see com.gawdl3y.android.tasktimer.adapters.NewFragmentStatePagerAdapter#restoreState(android.os.Parcelable, java.lang.ClassLoader)
	 */
	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		Bundle bundle = (Bundle) state;
		super.restoreState(bundle.getParcelable("super"), loader);
		groups = bundle.getParcelableArrayList("groups");
	}
}
