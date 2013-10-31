package com.gawdl3y.android.tasktimer.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import com.gawdl3y.android.tasktimer.layout.TaskListFragment;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.util.Log;

import java.util.ArrayList;

/**
 * The adapter to display a ViewPager of Groups
 * @author Schuyler Cebulskie
 */
public class TaskListFragmentAdapter extends NewFragmentStatePagerAdapter {
    private static final String TAG = "TaskListFragmentAdapter";

    private ArrayList<Group> mGroups;

    /**
     * Fill constructor
     * @param fm     The FragmentManager to use
     * @param groups The Groups to display
     */
    public TaskListFragmentAdapter(FragmentManager fm, ArrayList<Group> groups) {
        super(fm);
        mGroups = groups;
    }

    @Override
    public int getCount() {
        return mGroups != null ? mGroups.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mGroups.get(position).getName();
    }

    @Override
    public TaskListFragment getItem(int position) {
        Log.v(TAG, "Getting item #" + position);
        return TaskListFragment.newInstance(mGroups.get(position));
    }

    @Override
    public int getItemId(int position) {
        return mGroups.get(position).getId();
    }

    @Override
    public int getItemPosition(Object o) {
        TaskListFragment item = (TaskListFragment) o;
        int position = mGroups.indexOf(item.getGroup());

        if(position >= 0) {
            Log.v(TAG, "Item found at index " + position + ": " + item.getGroup().toString());
            return position;
        } else {
            Log.v(TAG, "Item not found");
            return POSITION_NONE;
        }
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("super", super.saveState());
        bundle.putParcelableArrayList("groups", mGroups);
        return bundle;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        Bundle bundle = (Bundle) state;
        bundle.setClassLoader(loader);
        super.restoreState(bundle.getParcelable("super"), loader);
        mGroups = bundle.getParcelableArrayList("groups");
    }

    /**
     * Gets the groups
     * @return The groups
     */
    public ArrayList<Group> getGroups() {
        return mGroups;
    }

    /**
     * Sets the groups
     * @param groups The groups
     */
    public void setGroups(ArrayList<Group> groups) {
        mGroups = groups;
    }
}
