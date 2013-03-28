package com.gawdl3y.android.tasktimer.layout;

import android.os.AsyncTask;
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
import com.gawdl3y.android.tasktimer.util.Log;
import com.gawdl3y.android.tasktimer.util.Utilities;

import java.util.ArrayList;

/**
 * The main fragment for Task Timer; contains a TaskListFragmentAdapter
 * @author Schuyler Cebulskie
 */
public class MainFragment extends SherlockFragment implements TaskListItem.TaskButtonListener, GroupEditDialogFragment.GroupEditDialogListener, TaskEditDialogFragment.TaskEditDialogListener {
    private static final String TAG = "MainFragment";

    // Data
    private ArrayList<Group> groups = TaskTimerApplication.GROUPS;

    // Stuff
    private ViewPager pager;

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
        new SetAdapterTask().execute();

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

    /**
     * The Group edit dialog has finished
     * @param group The resulting Group
     */
    @Override
    public void onFinishEditDialog(Group group) {
        groups.add(group.getPosition(), group);
        Utilities.reposition(groups);
        buildList();
    }

    /**
     * The Task edit dialog has finished
     * @param task       The resulting Task
     * @param groupIndex The index of the Group the Task is in
     */
    @Override
    public void onFinishEditDialog(Task task, int groupIndex) {
        ArrayList<Task> tasks = groups.get(groupIndex).getTasks();
        if(tasks.contains(task)) {
            updateTask(groupIndex, Utilities.getTaskIndexByID(task.getId(), tasks), task);
        } else {
            addTask(task, groupIndex);
        }
    }


    /**
     * Update a Group
     * @param groupIndex The index of the Group to update
     * @param group      The Group
     */
    public void updateGroup(int groupIndex, Group group) {
        groups.set(groupIndex, group);
        buildList();
    }

    /**
     * Add a Group
     * @param group The Group to add
     */
    public void addGroup(Group group) {
        groups.add(group.getPosition(), group);
        buildList();
    }

    /**
     * Remove a Group
     * @param groupIndex The index of the Group to remove
     */
    public void removeGroup(int groupIndex) {

    }

    /**
     * Update a Task
     * @param groupIndex The index of the Group the Task is in
     * @param taskIndex  The index of the Task in the Group
     * @param task       The Task
     */
    public void updateTask(int groupIndex, int taskIndex, Task task) {
        // Set the task
        groups.get(groupIndex).getTasks().set(taskIndex, task);

        // Update the view for the task
        try {
            TaskListFragment fragment = (TaskListFragment) getFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + groupIndex);
            TaskListItem item = (TaskListItem) fragment.getView().findViewWithTag(task.getPosition());
            item.invalidate(task);
            item.buildTimer();
        } catch(Exception e) {
            Log.v(TAG, "Couldn't update view of task #" + taskIndex + " in group #" + groupIndex);
        }
    }

    /**
     * Add a Task
     * @param task       The Task to add
     * @param groupIndex The index of the Group to add it to
     */
    public void addTask(Task task, int groupIndex) {
        Group group = groups.get(groupIndex);
        group.getTasks().add(task.getPosition(), task);

        // Update the task list fragment adapter for the group
        TaskListFragment fragment = (TaskListFragment) getFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + groupIndex);
        if(fragment != null) {
            fragment.group = group;
            ((TaskListAdapter) fragment.getListAdapter()).notifyDataSetChanged();
        }

        // Update the adapter's groups and scroll to the group that the task was added to
        getAdapter().setGroups(groups);
        pager.setCurrentItem(groupIndex, true);
    }

    /**
     * Remove a Task
     * @param groupIndex The index of the Group the Task is in
     * @param taskIndex  The index of the Task in the Group
     */
    public void removeTask(int groupIndex, int taskIndex) {

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
     * Sets the adapter of the ViewPager
     * @author Schuyler Cebulskie
     */
    private class SetAdapterTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pager.setAdapter(new TaskListFragmentAdapter(getFragmentManager(), groups));
            Log.v(TAG, "Set ViewPager adapter");
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
