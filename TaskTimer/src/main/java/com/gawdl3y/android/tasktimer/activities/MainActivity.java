package com.gawdl3y.android.tasktimer.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.data.TaskTimerReceiver;
import com.gawdl3y.android.tasktimer.layout.*;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.util.Log;

/**
 * The main activity of Task Timer
 * @author Schuyler Cebulskie
 */
public class MainActivity extends FragmentActivity implements TaskListItem.TaskButtonListener, LoaderManager.LoaderCallbacks<Cursor>, ActionMode.Callback {
    private static final String TAG = "MainActivity";
    private static final int GROUPS_LOADER_ID = 1;
    private static final int TASKS_LOADER_ID = 2;

    // Fragments
    private Fragment mCurrentFragment;
    private TasksFragment mTasksFragment;
    private GroupsFragment mGroupsFragment;

    // Drawer stuff
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mMainDrawer;
    private String[] mMainDrawerItems;
    private LinearLayout mTaskDrawer;

    // Other stuff
    private ActionMode mActionMode;

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

        // Do drawer stuff
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        mDrawerLayout.setDrawerShadow(TaskTimerApplication.THEME == R.style.Theme_Dark ? R.drawable.drawer_shadow_dark : R.drawable.drawer_shadow_light, Gravity.START);
        //mDrawerLayout.setDrawerShadow(TaskTimerApplication.THEME == R.style.Theme_Dark ? R.drawable.drawer_shadow_dark : R.drawable.drawer_shadow_light, Gravity.END);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
        mMainDrawer = (ListView) findViewById(R.id.activity_main_drawer_left);
        mMainDrawerItems = getResources().getStringArray(R.array.drawer_main_items);
        mMainDrawer.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mMainDrawerItems));
        mMainDrawer.setItemChecked(0, true);
        mMainDrawer.setOnItemClickListener(new DrawerItemClickListener());
        mTaskDrawer = (LinearLayout) findViewById(R.id.activity_main_drawer_right);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, TaskTimerApplication.THEME == R.style.Theme_Light ? R.drawable.ic_drawer_light : R.drawable.ic_drawer_dark, R.string.menu_drawer_open, R.string.menu_drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mMainDrawerItems[mMainDrawer.getCheckedItemPosition()]);
                setTitle(mMainDrawerItems[mMainDrawer.getCheckedItemPosition()]);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                TaskTimerApplication.PREFERENCES.edit().putBoolean("drawer_opened", true).commit();
                getActionBar().setTitle(getResources().getString(R.string.app_name));
                setTitle(getResources().getString(R.string.app_name));
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Do action bar stuff
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(mMainDrawerItems[mMainDrawer.getCheckedItemPosition()]);
        setTitle(mMainDrawerItems[mMainDrawer.getCheckedItemPosition()]);

        // Open the drawer if the user never has done so themselves
        if(!TaskTimerApplication.PREFERENCES.getBoolean("drawer_opened", false)) {
            mDrawerLayout.openDrawer(mMainDrawer);
            getActionBar().setTitle(getResources().getString(R.string.app_name));
            setTitle(getResources().getString(R.string.app_name));
        }

        // Display the main fragment
        mTasksFragment = TasksFragment.newInstance();
        mCurrentFragment = mTasksFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_content, mTasksFragment);
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
        if(TaskTimerApplication.GROUPS == null) setProgressBarIndeterminateVisibility(true);
        TaskTimerApplication.showOngoingNotification(this);

        Log.v(TAG, "Started");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(TaskTimerApplication.RUNNING_TASK_COUNT <= 0) TaskTimerApplication.cancelOngoingNotification(this);
        Log.v(TAG, "Stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(TaskTimerApplication.RUNNING_TASK_COUNT <= 0) TaskTimerApplication.cancelOngoingNotification(this);
        Log.v(TAG, "Destroyed");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        mActionMode = super.startActionMode(callback);
        return mActionMode;
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
            mTasksFragment.setGroups(TaskTimerApplication.GROUPS);
            setProgressBarIndeterminateVisibility(false);
        }

        Log.v(TAG, "Loader " + loader.getId() + " finished");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v(TAG, "Loader " + loader.getId() + " reset");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mMainDrawer) || mDrawerLayout.isDrawerOpen(mTaskDrawer);
        menu.findItem(R.id.menu_new_task).setVisible(!drawerOpen && mCurrentFragment instanceof TasksFragment);
        menu.findItem(R.id.menu_new_group).setVisible(!drawerOpen && (mCurrentFragment instanceof TasksFragment || mCurrentFragment instanceof GroupsFragment));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) return true;

        Intent intent;
        switch(item.getItemId()) {
            case R.id.menu_new_task:
                TaskEditDialogFragment taskEditDialog = TaskEditDialogFragment.newInstance(TaskTimerApplication.GROUPS, mTasksFragment.getPager().getCurrentItem(), null);
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
        if(mTasksFragment != null) mTasksFragment.onTaskButtonClick(view);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        if(mActionMode != null) mActionMode.finish();
        mActionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        if(mActionMode == mode) mActionMode = null;
    }

    /**
     * Gets the current ActionMode
     * @return The current ActionMode
     */
    public ActionMode getActionMode() {
        return mActionMode;
    }

    /**
     * Clears the current ActionMode (does not call {@code finish})
     */
    public void clearActionMode() {
        mActionMode = null;
    }

    /**
     * The list item click listener for the main drawer
     * @author Schuyler Cebulskie
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position) {
                case 0:
                    if(!(mCurrentFragment instanceof TasksFragment)) {
                        mGroupsFragment = null;
                        if(mActionMode != null) mActionMode.finish();
                        if(mTasksFragment == null) mTasksFragment = TasksFragment.newInstance();
                        mCurrentFragment = mTasksFragment;
                        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content, mTasksFragment).commit();
                    }
                    break;
                case 1:
                    if(!(mCurrentFragment instanceof GroupsFragment)) {
                        mTasksFragment = null;
                        if(mActionMode != null) mActionMode.finish();
                        if(mGroupsFragment == null) mGroupsFragment = GroupsFragment.newInstance();
                        mCurrentFragment = mGroupsFragment;
                        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content, mGroupsFragment).commit();
                    }
            }

            mDrawerLayout.closeDrawer(mMainDrawer);
        }
    }
}
