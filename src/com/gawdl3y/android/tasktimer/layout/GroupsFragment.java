package com.gawdl3y.android.tasktimer.layout;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.pojos.TaskTimerEvents;
import com.gawdl3y.android.tasktimer.util.Log;

import java.util.ArrayList;

/**
 * The list of groups; contains a list using GroupListAdapter
 */
public class GroupsFragment extends ListFragment implements TaskTimerEvents.GroupListener, TaskTimerEvents.TaskListener {
    private static final String TAG = "GroupsFragment";

    // Data
    private ArrayList<Group> groups = TaskTimerApplication.GROUPS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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

    public static GroupsFragment newInstance() {
        return new GroupsFragment();
    }

    @Override
    public void onGroupAdd(Group group) {

    }

    @Override
    public void onGroupRemove(Group group) {

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
}
