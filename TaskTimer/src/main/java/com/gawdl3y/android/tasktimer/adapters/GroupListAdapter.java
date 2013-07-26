package com.gawdl3y.android.tasktimer.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.gawdl3y.android.tasktimer.layout.GroupListItem;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * The adapter to display a list of Groups
 * @author Schuyler Cebulskie
 */
public class GroupListAdapter extends BaseAdapter {
    private static final String TAG = "GroupListAdapter";

    private Context context;
    private List<Group> groups;
    private SparseBooleanArray itemsChecked = new SparseBooleanArray();

    /**
     * Fill constructor
     * @param context The context of the adapter
     */
    public GroupListAdapter(Context context, List<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        Log.v(TAG, "Getting item #" + position);
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(position >= 0 && position < groups.size()) return groups.get(position).getId();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupListItem v;
        if(convertView != null && convertView instanceof GroupListItem) {
            Log.v(TAG, "Converting old view");
            v = (GroupListItem) convertView;
            v.setGroup((Group) getItem(position));
            v.setChecked(itemsChecked.get(position));
            v.invalidate();
        } else {
            Log.v(TAG, "Getting new view");
            v = new GroupListItem(context, null, (Group) getItem(position));
            v.setChecked(itemsChecked.get(position));
        }

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
    public ArrayList<Group> getCheckedItems() {
        ArrayList<Group> checkedItems = new ArrayList<Group>();
        for(int i = 0; i < groups.size(); i++) if(itemsChecked.get(i)) checkedItems.add(groups.get(i));
        return checkedItems;
    }

    /**
     * Gets all of the checked item positions
     * @return An array of all of the checked item positions
     */
    public Integer[] getCheckedItemPositions() {
        ArrayList<Integer> checkedItems = new ArrayList<Integer>();
        for(int i = 0; i < groups.size(); i++) if(itemsChecked.get(i)) checkedItems.add(i);
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
