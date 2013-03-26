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
import com.gawdl3y.android.tasktimer.layout.GroupEditDialogFragment.GroupEditDialogListener;
import com.gawdl3y.android.tasktimer.layout.MainFragment;
import com.gawdl3y.android.tasktimer.layout.TaskEditDialogFragment;
import com.gawdl3y.android.tasktimer.layout.TaskEditDialogFragment.TaskEditDialogListener;
import com.gawdl3y.android.tasktimer.layout.TaskListItem;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.util.Log;
import com.gawdl3y.android.tasktimer.util.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The main activity of Task Timer
 * @author Schuyler Cebulskie
 */
public class MainActivity extends SherlockFragmentActivity implements GroupEditDialogListener, TaskEditDialogListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MainActivity";
    private static final int GROUPS_LOADER_ID = 0x01;
    private static final int TASKS_LOADER_ID = 0x02;

    // Stuff
    private TaskTimerApplication app;
    private MainFragment mainFragment;

    // Data
    private ArrayList<Group> groups;

    /* (non-Javadoc)
     * The activity is being created
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set app and switch theme (we do this before calling the super method so that the theme properly applies)
        app = (TaskTimerApplication) getApplication();
        setTheme(app.theme);

        // Call the superclass' method
        super.onCreate(savedInstanceState);

        // Load data from saved instance state
        if(savedInstanceState != null && groups == null) groups = savedInstanceState.getParcelableArrayList("groups");

        // Display
        try { requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); } catch(Exception e) {}
        setContentView(R.layout.activity_main);

        Log.v(TAG, "Created");
    }

    /* (non-Javadoc)
     * The activity is being started
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
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

        // Fetch the data if we don't already have it
        if(groups == null) {
            setSupportProgressBarIndeterminateVisibility(true);
            getSupportLoaderManager().initLoader(GROUPS_LOADER_ID, null, this);
        }

        Log.v(TAG, "Started");
    }

    /* (non-Javadoc)
     * The activity is being stopped
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "Stopped");
    }

    /* (non-Javadoc)
     * The activity is being destroyed
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Destroyed");
    }

    /**
     * A Loader is being created
     * @param id   The ID for the Loader
     * @param args Arguments
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id) {
            case GROUPS_LOADER_ID:
                return new CursorLoader(this, Group.Columns.CONTENT_URI, null, null, null, Group.Columns.DEFAULT_SORT_ORDER);
            case TASKS_LOADER_ID:
                return new CursorLoader(this, Task.Columns.CONTENT_URI, null, null, null, Task.Columns.DEFAULT_SORT_ORDER);
            default:
                throw new IllegalArgumentException("Invalid loader ID");
        }
    }

    /**
     * A Loader has finished
     * @param loader The Loader
     * @param cursor The cursor for the data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(loader.getId() == GROUPS_LOADER_ID) {
            groups = new ArrayList<Group>();

            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                // Create the group object
                Group group = new Group(cursor.getInt(Group.Columns.ID_INDEX));
                group.setName(cursor.getString(Group.Columns.NAME_INDEX));
                group.setPosition(cursor.getInt(Group.Columns.POSITION_INDEX));

                // Add it
                groups.add(group);
            }

            // Re-position groups
            Utilities.reposition(groups);

            // Get tasks
            getSupportLoaderManager().initLoader(TASKS_LOADER_ID, null, this);
        } else if(loader.getId() == TASKS_LOADER_ID) {
            HashMap<Integer, Group> groupMap = new HashMap<Integer, Group>();

            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                // Create the task object
                Task task = new Task(cursor.getInt(Task.Columns.ID_INDEX));
                task.setName(cursor.getString(Task.Columns.NAME_INDEX));
                task.setDescription(cursor.getString(Task.Columns.NAME_INDEX));
                task.setIndefinite(cursor.getInt(Task.Columns.INDEFINITE_INDEX) == 1);
                task.setComplete(cursor.getInt(Task.Columns.COMPLETE_INDEX) == 1);
                task.setPosition(cursor.getInt(Task.Columns.POSITION_INDEX));
                task.setGroup(cursor.getInt(Task.Columns.GROUP_INDEX));

                // Add it to its group
                if(groupMap.get(task.getGroup()) == null) groupMap.put(task.getGroup(), Utilities.getGroupByID(task.getGroup(), groups));
                if(groupMap.get(task.getGroup()).getTasks() == null) groupMap.get(task.getGroup()).setTasks(new ArrayList<Task>());
                groupMap.get(task.getGroup()).getTasks().add(task);
            }

            // Re-position tasks
            for(Group g : groups) Utilities.reposition(g.getTasks());

            // We finally get to display!
            mainFragment = MainFragment.newInstance(groups);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_main, mainFragment);
            transaction.commit();
            setSupportProgressBarIndeterminateVisibility(false);
        }
    }

    /**
     * The Loader has been reset
     * @param loader The Loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Nothing to do here
    }

    /* (non-Javadoc)
     * The action bar was created
     * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    /* (non-Javadoc)
     * An action bar menu button was pressed
     * @see com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch(item.getItemId()) {
            case R.id.menu_new_task:
                TaskEditDialogFragment taskEditDialog = TaskEditDialogFragment.newInstance(groups, mainFragment.getPager().getCurrentItem(), null);
                taskEditDialog.show(getSupportFragmentManager(), "fragment_task_edit");
                return true;
            case R.id.menu_new_group:
                GroupEditDialogFragment groupEditDialog = GroupEditDialogFragment.newInstance(groups, 0, null);
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

    /* (non-Javadoc)
     * The add group dialog is finished
     * @see com.gawdl3y.android.tasktimer.layout.GroupEditDialogFragment.GroupEditDialogListener#onFinishEditDialog(com.gawdl3y.android.tasktimer.pojos.Group, int)
     */
    @Override
    public void onFinishEditDialog(Group group) {
        mainFragment.onFinishEditDialog(group);
    }

    /* (non-Javadoc)
     * The add task dialog is finished
     * @see com.gawdl3y.android.tasktimer.layout.TaskEditDialogFragment.TaskEditDialogListener#onFinishEditDialog(com.gawdl3y.android.tasktimer.pojos.Task)
     */
    @Override
    public void onFinishEditDialog(Task task, int groupIndex) {
        mainFragment.onFinishEditDialog(task, groupIndex);
    }

    /**
     * A task button is clicked
     * @param view The view of the button that was clicked
     */
    public void onTaskButtonClick(View view) {
        TaskListItem item = (TaskListItem) view.getParent().getParent();
        Task task = groups.get((Integer) item.getTag(R.id.tag_group)).getTasks().get((Integer) item.getTag(R.id.tag_task));

        if(view.getId() == R.id.task_toggle) {
            // Toggle the task
            if(!task.isComplete() || task.getBooleanSetting(Task.Settings.OVERTIME)) {
                task.toggle();
                item.invalidate(task);
                item.buildTimer();
            }
        }
    }
}
