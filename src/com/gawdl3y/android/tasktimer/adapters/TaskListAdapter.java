package com.gawdl3y.android.tasktimer.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.layout.TaskListItem;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.util.Log;

import java.util.ArrayList;

/**
 * The adapter to display a list of Tasks
 * @author Schuyler Cebulskie
 */
public class TaskListAdapter extends BaseAdapter {
    private static final String TAG = "TaskListAdapter";

    private Context context;

    private ArrayList<Task> tasks;
    private int group;
    private SparseBooleanArray itemsChecked = new SparseBooleanArray();

    /**
     * Fill constructor
     * @param context The context of the adapter
     * @param tasks   The tasks to be displayed
     * @param group   The position of the group that the list is for
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
        Log.v(TAG, "Getting item #" + position);
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
        Log.v(TAG, "Getting view");

        TaskListItem v = new TaskListItem(context, (Task) getItem(position));
        v.setTag(R.id.tag_task, position);
        v.setTag(R.id.tag_group, group);
        return v;
    }

    /**
     * Sets whether or not an item is checked
     * @param position The position of the item
     * @param checked Whether or not the item should be checked
     */
    public void setItemChecked(int position, boolean checked) {
        itemsChecked.put(position, checked);
    }

    /**
     * Gets whether or not an item is checked
     * @param position The position of the item
     * @return Whether or not the item is checked
     */
    public boolean isItemChecked(int position) {
        return itemsChecked.get(position);
    }

    /**
     * Toggles the checked status of an item
     * @param position The position of the item
     */
    public void toggleItem(int position) {
        itemsChecked.put(position, !itemsChecked.get(position));
    }

    /**
     * Gets all of the checked items
     * @return An ArrayList of all of the checked items
     */
    public ArrayList<Task> getCheckedItems() {
        ArrayList<Task> checkedItems = new ArrayList<Task>();
        for(int i = 0; i < tasks.size(); i++) if(itemsChecked.get(i)) checkedItems.add(tasks.get(i));
        return checkedItems;
    }

    /**
     * Gets all of the checked item positions
     * @return An array of all of the checked item positions
     */
    public Integer[] getCheckedItemPositions() {
        ArrayList<Integer> checkedItems = new ArrayList<Integer>();
        for(int i = 0; i < tasks.size(); i++) if(itemsChecked.get(i)) checkedItems.add(i);
        return checkedItems.toArray(new Integer[checkedItems.size()]);
    }

    /**
     * Gets the number of checked items
     * @return The number of checked items
     */
    public int getCheckedCount() {
        int count = 0;
        for(int i = 0; i < itemsChecked.size(); i++) if(itemsChecked.get(i)) count++;
        return count;
    }
}
