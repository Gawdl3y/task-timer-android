package com.gawdl3y.android.tasktimer.layout;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.gawdl3y.android.actionablelistview.ActionItem;
import com.gawdl3y.android.actionablelistview.ActionableListFragment;
import com.gawdl3y.android.actionablelistview.ActionableListView;
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
public class GroupsFragment extends ActionableListFragment implements TaskTimerEvents.GroupListener, TaskTimerEvents.TaskListener {
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
        ActionableListView list = (ActionableListView) getListView();
        list.setActionModeCallback((MainActivity) getActivity());
        list.addAction(new ActionItem(0, 0, android.R.drawable.ic_menu_delete, R.string.group_action_delete));
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


    /**
     * Creates a new instance of GroupsFragment
     * @return A new instance of GroupsFragment
     */
    public static GroupsFragment newInstance() {
        return new GroupsFragment();
    }
}
