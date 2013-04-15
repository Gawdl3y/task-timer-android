package com.gawdl3y.android.tasktimer.context;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.data.TaskTimerReceiver;
import com.gawdl3y.android.tasktimer.layout.GroupEditDialogFragment;
import com.gawdl3y.android.tasktimer.layout.MainFragment;
import com.gawdl3y.android.tasktimer.layout.TaskEditDialogFragment;
import com.gawdl3y.android.tasktimer.layout.TaskListItem;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.pojos.TaskTimerEvents;
import com.gawdl3y.android.tasktimer.util.Log;

/**
 * The main activity of Task Timer
 * @author Schuyler Cebulskie
 */
public class MainActivity extends SherlockFragmentActivity implements TaskListItem.TaskButtonListener, TaskTimerEvents.GroupListener, TaskTimerEvents.TaskListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MainActivity";
    private static final int GROUPS_LOADER_ID = 1;
    private static final int TASKS_LOADER_ID = 2;

    // Stuff
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Switch theme (we do this before calling the super method so that the theme properly applies)
        setTheme(TaskTimerApplication.THEME);
        super.onCreate(savedInstanceState);

        // Load data if we don't already have it
        if(TaskTimerApplication.GROUPS == null) getSupportLoaderManager().initLoader(GROUPS_LOADER_ID, null, this);

        // Initialize activity view
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        // Display the main fragment
        mainFragment = MainFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main, mainFragment);
        transaction.commit();

        Log.v(TAG, "Created");
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Restart if necessary
        if(getIntent().hasExtra("restart")) {
            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, TaskTimerReceiver.class);
            intent.setAction(TaskTimerReceiver.ACTION_START_APP);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmMgr.set(AlarmManager.RTC, System.currentTimeMillis() + 250, pendingIntent);

            Log.v(TAG, "Restarting");
            finish();
            return;
        }

        // Show the loading indicator if we don't have the data, register as the event listener, and show the ongoing notification
        if(TaskTimerApplication.GROUPS == null) setSupportProgressBarIndeterminateVisibility(true);
        TaskTimerEvents.setListener(this);
        TaskTimerApplication.showOngoingNotification(this);

        Log.v(TAG, "Started");
    }

    @Override
    protected void onStop() {
        super.onStop();
        TaskTimerEvents.setListener(null);
        if(TaskTimerApplication.RUNNING_TASKS <= 0) TaskTimerApplication.cancelOngoingNotification(this);
        Log.v(TAG, "Stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(TaskTimerApplication.RUNNING_TASKS <= 0) TaskTimerApplication.cancelOngoingNotification(this);
        Log.v(TAG, "Destroyed");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(TAG, "Loader created with ID " + id);

        switch(id) {
            case GROUPS_LOADER_ID:
                return new CursorLoader(this, Group.Columns.CONTENT_URI, null, null, null, Group.Columns.DEFAULT_SORT_ORDER);
            case TASKS_LOADER_ID:
                return new CursorLoader(this, Task.Columns.CONTENT_URI, null, null, null, Task.Columns.DEFAULT_SORT_ORDER);
            default:
                throw new IllegalArgumentException("Invalid loader ID");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(loader.getId() == GROUPS_LOADER_ID) {
            TaskTimerApplication.loadGroups(cursor);
            getSupportLoaderManager().initLoader(TASKS_LOADER_ID, null, this);
        } else if(loader.getId() == TASKS_LOADER_ID) {
            TaskTimerApplication.loadTasks(cursor);
            mainFragment.setGroups(TaskTimerApplication.GROUPS);
            setSupportProgressBarIndeterminateVisibility(false);
        }

        Log.v(TAG, "Loader " + loader.getId() + " finished");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v(TAG, "Loader " + loader.getId() + " reset");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch(item.getItemId()) {
            case R.id.menu_new_task:
                TaskEditDialogFragment taskEditDialog = TaskEditDialogFragment.newInstance(TaskTimerApplication.GROUPS, mainFragment.getPager().getCurrentItem(), null);
                taskEditDialog.show(getSupportFragmentManager(), "fragment_task_edit");
                return true;
            case R.id.menu_new_group:
                GroupEditDialogFragment groupEditDialog = GroupEditDialogFragment.newInstance(TaskTimerApplication.GROUPS, 0, null);
                groupEditDialog.show(getSupportFragmentManager(), "fragment_group_edit");
                return true;
            case R.id.menu_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTaskButtonClick(View view) {
        mainFragment.onTaskButtonClick(view);
    }

    @Override
    public void onGroupAdd(Group group) {
        mainFragment.onGroupAdd(group);
    }

    @Override
    public void onGroupRemove(Group group) {
        mainFragment.onGroupRemove(group);
    }

    @Override
    public void onGroupUpdate(Group group, Group oldGroup) {
        mainFragment.onGroupUpdate(group, oldGroup);
    }

    @Override
    public void onTaskAdd(Task task, Group group) {
        mainFragment.onTaskAdd(task, group);
    }

    @Override
    public void onTaskRemove(Task task, Group group) {
        mainFragment.onTaskRemove(task, group);
    }

    @Override
    public void onTaskUpdate(Task task, Task oldTask, Group group) {
        mainFragment.onTaskUpdate(task, oldTask, group);
    }
}
