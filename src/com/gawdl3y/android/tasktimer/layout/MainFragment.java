package com.gawdl3y.android.tasktimer.layout;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.adapters.TaskListAdapter;
import com.gawdl3y.android.tasktimer.adapters.TaskListFragmentAdapter;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.pojos.TaskTimerEvents;
import com.gawdl3y.android.tasktimer.util.Log;

import java.util.ArrayList;

/**
 * The main fragment for Task Timer; contains a TaskListFragmentAdapter
 * @author Schuyler Cebulskie
 */
public class MainFragment extends SherlockFragment implements TaskListItem.TaskButtonListener, TaskTimerEvents.GroupListener, TaskTimerEvents.TaskListener {
    private static final String TAG = "MainFragment";

    // Data
    private ArrayList<Group> groups = TaskTimerApplication.GROUPS;

    // Stuff
    private ViewPager pager;

    /**
     * The fragment is being created
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.v(TAG, "Created");
    }

    /**
     * The view for the fragment is being created
     * @param inflater           The LayoutInflater to use
     * @param container          The container
     * @param savedInstanceState The saved instance state
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create the view and pager
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        pager = (ViewPager) view.findViewById(R.id.pager);

        // Set the adapter of the pager
        pager.setAdapter(new TaskListFragmentAdapter(getFragmentManager(), groups));

        Log.v(TAG, "View created");
        return view;
    }

    /**
     * A task button is clicked
     * @param view The view of the button that was clicked
     */
    @Override
    public void onTaskButtonClick(View view) {
        TaskListItem item = (TaskListItem) view.getParent().getParent();
        Task task = groups.get((Integer) item.getTag(R.id.tag_group)).getTasks().get((Integer) item.getTag(R.id.tag_task));

        if(view.getId() == R.id.task_toggle) {
            // Toggle the task
            if(!task.isComplete() || task.getBooleanSetting(Task.Settings.OVERTIME)) {
                task.toggle();
                item.invalidate(task);
                item.buildTimer();

                // Update the running task count, create a system alarm, and update the ongoing notification
                if(task.isRunning()) TaskTimerApplication.RUNNING_TASKS++; else TaskTimerApplication.RUNNING_TASKS--;
                TaskTimerApplication.createTaskGoalReachedAlarm(getActivity(), task);
                TaskTimerApplication.showOngoingNotification(getActivity());
            }
        }
    }

    @Override
    public void onGroupAdd(Group group) {
        buildList();
    }

    @Override
    public void onGroupRemove(Group group) {
        buildList();
    }

    @Override
    public void onGroupUpdate(Group group, Group oldGroup) {
        buildList();
    }

    @Override
    public void onTaskAdd(Task task, Group group) {
        // Update the task list fragment adapter for the group
        TaskListFragment fragment = (TaskListFragment) getFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + group.getPosition());
        if(fragment != null) {
            fragment.group = group;
            ((TaskListAdapter) fragment.getListAdapter()).notifyDataSetChanged();
        }

        // Update the adapter's groups and scroll to the group that the task was added to
        getAdapter().setGroups(groups);
        pager.setCurrentItem(group.getPosition(), true);
    }

    @Override
    public void onTaskRemove(Task task, Group group) {
        buildList();
    }

    @Override
    public void onTaskUpdate(Task task, Task oldTask, Group group) {
        // Update the view for the task
        try {
            TaskListFragment fragment = (TaskListFragment) getFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + group.getPosition());
            TaskListItem item = (TaskListItem) fragment.getView().findViewWithTag(task.getPosition());
            item.invalidate(task);
            item.buildTimer();
        } catch(Exception e) {
            Log.v(TAG, "Couldn't update view of task " + task.getId());
        }
    }


    /**
     * Builds/rebuilds the list of groups and tasks
     */
    public void buildList() {
        if(groups == null) {
            pager.setVisibility(View.GONE);
        } else {
            getAdapter().setGroups(groups);
            getAdapter().notifyDataSetChanged();
            pager.setVisibility(View.VISIBLE);
            pager.invalidate();
        }
    }


    /**
     * Sets the Groups
     */
    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
        buildList();
    }

    /**
     * Gets the ViewPager
     * @return The ViewPager
     */
    public ViewPager getPager() {
        return pager;
    }

    /**
     * Gets the adapter of the ViewPager
     * @return The adapter of the ViewPager
     */
    public TaskListFragmentAdapter getAdapter() {
        return (TaskListFragmentAdapter) pager.getAdapter();
    }


    /**
     * Creates a new instance of MainFragment
     * @return A new instance of MainFragment
     */
    public static MainFragment newInstance() {
        return new MainFragment();
    }
}
