package com.gawdl3y.android.tasktimer.layout;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.adapters.GroupListAdapter;
import com.gawdl3y.android.tasktimer.activities.MainActivity;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.pojos.TaskTimerEvents;
import com.gawdl3y.android.tasktimer.util.Log;

import java.util.ArrayList;

/**
 * The list of groups; contains a list using GroupListAdapter
 * @author Schuyler Cebulskie
 */
public class GroupsFragment extends ListFragment implements TaskTimerEvents.GroupListener, TaskTimerEvents.TaskListener, ActionMode.Callback {
    private static final String TAG = "GroupsFragment";

    // Data
    private ArrayList<Group> groups = TaskTimerApplication.GROUPS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setListAdapter(new GroupListAdapter(getActivity(), groups));
        TaskTimerEvents.registerListener(this);
        Log.v(TAG, "Created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TaskTimerEvents.unregisterListener(this);
        Log.v(TAG, "Destroyed");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        Log.v(TAG, "View created");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemLongClick(getListView(), view, position, id);
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        // Toggle the item if we've already toggled others
        if(list.getChoiceMode() == AbsListView.CHOICE_MODE_MULTIPLE) {
            if(list.getCheckedItemCount() > 0) {
                GroupListItem item = (GroupListItem) view;
                item.toggle();
                ((GroupListAdapter) getListAdapter()).setItemChecked(position, item.isChecked());
                Toast.makeText(getActivity(), Integer.toString(list.getCheckedItemCount()), Toast.LENGTH_SHORT).show();
            } else {
                ((MainActivity) getActivity()).getActionMode().finish();
            }
        } else {
            Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * A list item was long-clicked
     * @param list     The ListView that the item is in
     * @param view     The view of the item
     * @param position The position of the item
     * @param id       The ID of the item
     */
    public void onListItemLongClick(ListView list, View view, int position, long id) {
        // Start the ActionMode
        if(list.getChoiceMode() != AbsListView.CHOICE_MODE_MULTIPLE && list.getCheckedItemCount() == 0) getActivity().startActionMode(this);

        // Toggle the item
        GroupListItem item = (GroupListItem) view;
        item.toggle();
        list.setItemChecked(position, item.isChecked());
        ((GroupListAdapter) getListAdapter()).setItemChecked(position, item.isChecked());

        // Get rid of the action mode if there aren't any more checked
        if(list.getCheckedItemCount() == 0) ((MainActivity) getActivity()).getActionMode().finish();
        Toast.makeText(getActivity(), Integer.toString(getListView().getCheckedItemCount()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGroupAdd(Group group) {
        ((GroupListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onGroupRemove(Group group) {
        ((GroupListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onGroupUpdate(Group group, Group oldGroup) {

    }

    @Override
    public void onTaskAdd(Task task, Group group) {

    }

    @Override
    public void onTaskRemove(Task task, Group group) {

    }

    @Override
    public void onTaskUpdate(Task task, Task oldTask, Group group) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.action_group, menu);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        for(int i = 0; i < groups.size(); i++) {
            GroupListItem item = (GroupListItem) getListView().findViewWithTag(i);
            if(item != null) item.setChecked(false);
            getListView().setItemChecked(i, false);
            ((GroupListAdapter) getListAdapter()).setItemChecked(i, false);
        }
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_NONE);
        ((MainActivity) getActivity()).clearActionMode();
    }


    /**
     * Creates a new instance of GroupsFragment
     * @return A new instance of GroupsFragment
     */
    public static GroupsFragment newInstance() {
        return new GroupsFragment();
    }
}
