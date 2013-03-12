package com.gawdl3y.android.tasktimer.context;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.layout.GroupEditDialogFragment;
import com.gawdl3y.android.tasktimer.layout.GroupEditDialogFragment.GroupEditDialogListener;
import com.gawdl3y.android.tasktimer.layout.MainFragment;
import com.gawdl3y.android.tasktimer.layout.TaskEditDialogFragment;
import com.gawdl3y.android.tasktimer.layout.TaskEditDialogFragment.TaskEditDialogListener;
import com.gawdl3y.android.tasktimer.layout.TaskListFragment;
import com.gawdl3y.android.tasktimer.layout.TaskListItem;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.utilities.TaskTimerReceiver;

/**
 * The main activity of Task Timer
 * @author Schuyler Cebulskie
 */
public class MainActivity extends SherlockFragmentActivity implements GroupEditDialogListener, TaskEditDialogListener {
	private static final String TAG = "MainActivity";
	
	private ArrayList<Group> groups;
	private boolean fetchedData = false;
	
	private TaskTimerApplication app;
	private MainFragment mainFragment;
	
	private Messenger messenger = new Messenger(new IncomingHandler()), serviceMessenger;
	
	/**
	 * The service connection
	 */
	private ServiceConnection serviceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			if(app.debug) Log.v(TAG, "Service connected: " + name);
			
			// Set the service messenger
			MainActivity.this.serviceMessenger = new Messenger(service);
			
			// Retrieve ALL THE THINGS
			Message msg = Message.obtain(null, TaskService.MSG_GET_ALL);
			sendMessageToService(msg);
		}

		public void onServiceDisconnected(ComponentName name) {
			if(app.debug) Log.v(TAG, "Service disconnected: " + name);
			
			// Reset the service messenger
			MainActivity.this.serviceMessenger = null;
			
			// Stop the activity
			finish();
		}
	};
	
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
		
		// Display
		try { requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); } catch(Exception e) {}
		setContentView(R.layout.activity_main);
		
		// Start the service
		Intent intent = new Intent(app, TaskService.class);
		app.startService(intent);
		
		Log.v(TAG, "Created");
	}
	
	/* (non-Javadoc)
	 * The activity is being started
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	protected void onStart() {
		super.onStart();
		
		// Restart if necessary
		if(getIntent().hasExtra("restart")) {
			AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(this, TaskTimerReceiver.class);
			intent.setAction(TaskTimerReceiver.ACTION_START_APP);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
			alarmMgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, pendingIntent);
			
			finish();
			return;
		}
		
		// Show the loading indicator if we don't have the groups or tasks yet
		if(!fetchedData) setSupportProgressBarIndeterminateVisibility(true);
		
		// Bind the service
		Intent intent = new Intent(app, TaskService.class);
		if(bindService(intent, serviceConnection, Context.BIND_ABOVE_CLIENT | Context.BIND_ADJUST_WITH_ACTIVITY)) {
			if(app.debug) Log.v(TAG, "Service bound");
		} else {
			Toast.makeText(this, app.resources.getString(R.string.error_serviceNotBound), Toast.LENGTH_SHORT).show();
			Log.w(TAG, "Service couldn't be bound");
			finish();
		}
		
		if(app.debug) Log.v(TAG, "Started");
	}
	
	/* (non-Javadoc)
	 * The activity is being stopped
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
	 */
	protected void onStop() {
		super.onStop();
		
		// Unbind service
		try { unbindService(serviceConnection); } catch(IllegalArgumentException e) {}
		
		if(app.debug) Log.v(TAG, "Stopped");
	}
	
	/* (non-Javadoc)
	 * The activity is being destroyed
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(app.debug) Log.v(TAG, "Destroyed");
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
				TaskEditDialogFragment taskEditDialog = TaskEditDialogFragment.newInstance(groups, mainFragment.pager.getCurrentItem(), null);
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
	        case R.id.menu_exit:
	        	Message msg = Message.obtain(null, TaskService.MSG_EXIT);
	        	sendMessageToService(msg);
	        	finish();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/* (non-Javadoc)
	 * The add group dialog is finished
	 * @see com.gawdl3y.android.tasktimer.fragments.GroupEditDialogFragment.GroupEditDialogListener#onFinishEditDialog(com.gawdl3y.android.tasktimer.utilities.Group, int)
	 */
	@Override
	public void onFinishEditDialog(Group group) {
		Message msg = Message.obtain(null, TaskService.MSG_ADD_GROUP);
		Bundle contents = new Bundle();
		contents.putParcelable("group", group);
		msg.setData(contents);
		msg.arg1 = group.getPosition();
		sendMessageToService(msg);
	}
	
	/* (non-Javadoc)
	 * The add task dialog is finished
	 * @see com.gawdl3y.android.tasktimer.fragments.TaskEditDialogFragment.TaskEditDialogListener#onFinishEditDialog(com.gawdl3y.android.tasktimer.utilities.Task)
	 */
	@Override
	public void onFinishEditDialog(Task task, int group) {
		Message msg = Message.obtain(null, TaskService.MSG_ADD_TASK);
		Bundle contents = new Bundle();
		contents.putParcelable("task", task);
		msg.setData(contents);
		msg.arg1 = group;
		sendMessageToService(msg);
	}
	
	/**
	 * A task button is clicked
	 * @param view The view of the button that was clicked
	 */
	public void onTaskButtonClick(View view) {
		TaskListItem item = (TaskListItem) view.getParent().getParent();
		Task task = groups.get((Integer) item.getTag(R.id.tag_group)).getTasks().get((Integer) item.getTag(R.id.tag_task));
		Message msg;
				
		if(view.getId() == R.id.task_toggle) {
			task.toggle();
			item.invalidate(task);
			item.buildTimer();
			
			msg = Message.obtain(null, TaskService.MSG_TOGGLE_TASK);
			msg.arg1 = (Integer) item.getTag(R.id.tag_group);
			msg.arg2 = (Integer) item.getTag(R.id.tag_task);
			sendMessageToService(msg);
		}
	}
	
	/**
	 * The handler for the activity to receive messages from the service
	 * @author Schuyler Cebulskie
	 */
	private final class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if(app.debug) Log.v(TAG, "Received message: " + msg);
			
			Bundle data = msg.getData();
			data.setClassLoader(getClassLoader());
			
			Task task;
			TaskListFragment fragment;
			
			switch(msg.what) {
			case TaskService.MSG_ADD_TASK:
				// Set the containing group
				task = (Task) data.getParcelable("task");
				Group group = groups.get(msg.arg1);
				group.getTasks().add(task.getPosition(), task);
				
				// Update the task list fragment adapter for the group
				fragment = (TaskListFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + msg.arg1);
				if(fragment != null) {
					fragment.group = group;
					fragment.adapter.notifyDataSetChanged();
				}
				
				// Update the main adapter's groups and scroll to the group that the task was added to
				mainFragment.adapter.groups = groups;
				mainFragment.pager.setCurrentItem(msg.arg1, true);
				break;
			case TaskService.MSG_UPDATE_TASK:
				// Set the task
				task = (Task) data.getParcelable("task");
				groups.get(msg.arg1).getTasks().set(task.getPosition(), task);
				
				// Update the view of the task
				try {
					fragment = (TaskListFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + msg.arg1);
					TaskListItem view = (TaskListItem) fragment.getView().findViewWithTag(Integer.valueOf(task.getPosition()));
					view.invalidate(task);
				} catch(NullPointerException e) { }
				break;
			
			case TaskService.MSG_GET_GROUPS:
				groups = data.getParcelableArrayList("groups");
				buildList();
				if(msg.arg1 != -1) mainFragment.pager.setCurrentItem(msg.arg1, true);
				break;
			
			case TaskService.MSG_GET_ALL:
				// Get the data
				groups = data.getParcelableArrayList("groups");
				
				// Add the main fragment to the activity
				mainFragment = MainFragment.newInstance(groups);
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.add(R.id.activity_main, mainFragment);
				transaction.commit();
				
				// Hide the loading indicator
				fetchedData = true;
				setSupportProgressBarIndeterminateVisibility(false);
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}
	
	/**
	 * Builds the list of groups and tasks
	 */
	private void buildList() {
		mainFragment.adapter.groups = groups;
		mainFragment.adapter.notifyDataSetChanged();
		mainFragment.pager.invalidate();
	}
	
	/**
	 * Sends a message to the service
	 * @param msg The message to send
	 */
	private void sendMessageToService(Message msg) {
		// Set who to reply to
		msg.replyTo = messenger;
		
		// Send the message
		try {
			serviceMessenger.send(msg);
			if(app.debug) Log.v(TAG, "Sent message: " + msg);
		} catch(android.os.RemoteException e) {
			if(app.debug) Log.w(TAG, "Failed to send message: " + msg + " (" + e.getLocalizedMessage() + " caused by " + e.getCause() + ")");
		}
		
		// Return the message to the global pool
		msg.recycle();
	}
}
