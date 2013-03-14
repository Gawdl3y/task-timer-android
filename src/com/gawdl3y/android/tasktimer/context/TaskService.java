package com.gawdl3y.android.tasktimer.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.pojos.TimeAmount;
import com.gawdl3y.android.tasktimer.utilities.TaskTimerReceiver;
import com.gawdl3y.android.tasktimer.utilities.Utilities;

/**
 * The service that handles storing/retrieving data and the timers
 * @author Schuyler Cebulskie
 */
public class TaskService extends Service {
	public static final String TAG = "TaskService";
	
	// Messages
	public static final int MSG_GET_TASKS = 1;
	public static final int MSG_ADD_TASK = 2;
	public static final int MSG_DELETE_TASK = 3;
	public static final int MSG_UPDATE_TASK = 4;
	public static final int MSG_TOGGLE_TASK = 5;
	public static final int MSG_GET_GROUPS = 6;
	public static final int MSG_ADD_GROUP = 7;
	public static final int MSG_DELETE_GROUP = 8;
	public static final int MSG_UPDATE_GROUP = 9;
	public static final int MSG_GET_ALL = 10;
	public static final int MSG_EXIT = 11;
	
	// Messaging things
	private boolean connected = false;
	private Messenger activityMessenger;
	private Messenger messenger;
	
	private TaskTimerApplication app;
	private Notification notification;
	
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private ArrayList<Group> groups = new ArrayList<Group>();
	private int groupID, taskID;
	
	/* (non-Javadoc)
	 * The service is being created
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// Set the app
		app = (TaskTimerApplication) getApplication();
		
		// Create the messenger
		messenger = new Messenger(new IncomingHandler());
		
		// Create the intent to launch the app
		Intent intent = new Intent(this, MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class).addNextIntent(intent);
		
		// Create the notification
		notification = new NotificationCompat.Builder(this)
				.setContentTitle(getString(R.string.app_name))
				.setContentText("Hey! Listen!")
				//.setTicker("Hey! Listen!")
				.setSmallIcon(R.drawable.ic_launcher)
				.setLargeIcon(Utilities.drawableToBitmap(getResources().getDrawable(R.drawable.ic_launcher)))
				.setWhen(System.currentTimeMillis())
				.setContentIntent(stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT))
				.build();
		
		// Start the service in the foreground
		startForeground(1, notification);
		
		// Use these until database is implemented
		ArrayList<Task> tasks1 = new ArrayList<Task>(), tasks2 = new ArrayList<Task>(), tasks3 = new ArrayList<Task>();
		tasks1.add(new Task("This is a task", "", new TimeAmount(1, 2, 3), new TimeAmount(), true, false, false, 22, 5, 42, new HashMap<String, Object>(), -1, -1));
		tasks1.add(new Task("Really cool task", "", new TimeAmount(1, 59, 42), new TimeAmount(2, 0, 0), false, false, false, 4, 1, 42, new HashMap<String, Object>(), -1, -1));
		tasks2.add(new Task("It's a task!", "", new TimeAmount(), new TimeAmount(2.54321), false, false, false, 0, 1, 43, new HashMap<String, Object>(), -1, -1));
		
		Collections.sort(tasks1, Task.PositionComparator);
		Collections.sort(tasks2, Task.PositionComparator);
		Collections.sort(tasks3, Task.PositionComparator);
		
		Utilities.reposition(tasks1);
		Utilities.reposition(tasks2);
		Utilities.reposition(tasks3);
		
		tasks.addAll(tasks1);
		tasks.addAll(tasks2);
		tasks.addAll(tasks3);
		
		groups.add(new Group("It's a group!", 42, 0, tasks1));
		groups.add(new Group("Also a group", 43, 2, tasks2));
		groups.add(new Group("no way another group", 2, 1, tasks3));
		
		Collections.sort(groups, Group.PositionComparator);
		
		groupID = 43;
		taskID = 22;
		
		if(app.debug) Log.v(TAG, "Started");
	}
	
	/* (non-Javadoc)
	 * The service is receiving a command to start
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(app.debug) Log.v(TAG, "Received start command: " + intent.toString());
		
		if(intent != null && intent.getAction() != null) {
			if(intent.getAction().equals(TaskTimerReceiver.ACTION_TASK_GOAL_REACHED)) {
				// A task's goal has been reached
				Task task = Utilities.getGroupedTaskByID(intent.getExtras().getInt("task"), groups);
				Group group = Utilities.getGroupByID(task.getGroup(), groups);
				int alarmID = intent.getExtras().getInt("alarm");
				
				if(task.isRunning() && task.getAlert() == alarmID) {
					task.setTime(task.getGoal());
					task.setComplete(true);
					if(task.getBooleanSetting("stop")) task.setRunning(false);
					sendObjectToActivity(MSG_UPDATE_TASK, "task", task, group.getPosition());
					Toast.makeText(this, String.format(app.resources.getString(R.string.task_goal_reached), task.getName()), Toast.LENGTH_LONG).show();
					if(app.debug) Log.d(TAG, "Task #" + task.getPosition() + " of group #" + group.getPosition() + " has reached its goal");
				}
			}
		}
		
		return START_STICKY;
	}

	/* (non-Javadoc)
	 * The service is being bound to the activity
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		connected = true;
		if(app.debug) Log.v(TAG, "Bound");
		return messenger.getBinder();
	}
	
	/* (non-Javadoc)
	 * The service is being unbound from an activity
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		connected = false;
		if(app.debug) Log.v(TAG, "Unbound");
		return true;
	}
	
	@Override
	public void onRebind(Intent intent) {
		// Update the times of running tasks
		for(Group g : groups) {
			for(Task t : g.getTasks()) {
				if(t.getLastTick() > 0) {
					int time = (int) ((System.currentTimeMillis() - t.getLastTick()) / 1000);
					t.incrementTime(time);
					t.setLastTick(System.currentTimeMillis());
					if(app.debug) Log.d(TAG, "Updated task #" + t.getPosition() + " of group #" + g.getPosition() + " time by " + time + " seconds");
				}
			}
		}
		
		connected = true;
		if(app.debug) Log.v(TAG, "Rebound");
	}

	/* (non-Javadoc)
	 * The service is being destroyed
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(app.debug) Log.v(TAG, "Destroyed");
	}

	/**
	 * The service binder to use for binding the service to an activity
	 * @author Schuyler Cebulskie
	 */
	public class ServiceBinder extends Binder {
		TaskService getService() {
			return TaskService.this;
		}
	}
	
	/**
	 * The Handler to receive messages from the activity
	 * @author Schuyler Cebulskie
	 */
	private final class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			activityMessenger = msg.replyTo;
			if(app.debug) Log.v(TAG, "Received message: " + msg);
			
			Message response;
			Bundle data = msg.getData(), contents = new Bundle();
			data.setClassLoader(getClassLoader());
			
			Task task;
			Group group;
			
			switch(msg.what) {
			case MSG_GET_GROUPS:
				// Send the groups to the activity
				sendListToActivity(MSG_GET_GROUPS, "groups", groups);
				break;
			case MSG_ADD_GROUP:
				// Add the group TODO SQL
				groupID++;
				group = (Group) data.getParcelable("group");
				group.setId(groupID);
				groups.add(msg.arg1, group);
				Utilities.reposition(groups);
				
				// Send the groups to the activity
				sendListToActivity(MSG_GET_GROUPS, "groups", groups, group.getPosition());
				break;
			case MSG_DELETE_GROUP:
				// Delete a Group TODO SQL
				groups.remove(msg.arg1);
				break;
			case MSG_UPDATE_GROUP:
				// Update a Group TODO SQL
				group = (Group) data.getParcelable("group");
				groups.set(msg.arg1, group);
				break;
			
			case MSG_GET_TASKS:
				// Send the tasks to the activity
				sendListToActivity(MSG_GET_TASKS, "tasks", tasks);
				break;
			case MSG_ADD_TASK:
				// Add a Task TODO SQL
				taskID++;
				task = (Task) data.getParcelable("task");
				task.setId(taskID);
				task.setGroup(groups.get(msg.arg1).getId());
				
				tasks.add(task);
				groups.get(msg.arg1).getTasks().add(task.getPosition(), task);
				Utilities.reposition(groups.get(msg.arg1).getTasks());
				
				// Send the task back to the activity
				sendObjectToActivity(MSG_ADD_TASK, "task", task, msg.arg1);
				break;
			case MSG_DELETE_TASK:
				// Delete a Task TODO SQL
				task = groups.get(msg.arg1).getTasks().get(msg.arg2);
				tasks.remove(msg.arg2);
				groups.get(msg.arg1).getTasks().remove(msg.arg2);
				break;
			case MSG_UPDATE_TASK:
				// Update a Task TODO SQL
				task = (Task) data.getParcelable("task");
				tasks.set(msg.arg2, task);
				groups.get(msg.arg1).getTasks().set(msg.arg2, task);
				
				// Send the task back to the activity
				sendObjectToActivity(MSG_UPDATE_TASK, "task", task, msg.arg1);
				break;
			case MSG_TOGGLE_TASK:
				// Toggle a Task
				task = groups.get(msg.arg1).getTasks().get(msg.arg2);
				task.toggle();
				
				// Update its time if it WAS running
				if(!task.isRunning() && task.getLastTick() > 0) {
					int time = (int) ((System.currentTimeMillis() - task.getLastTick()) / 1000);
					task.incrementTime(time);
					if(app.debug) Log.d(TAG, "Updated task #" + msg.arg2 + " of group #" + msg.arg1 + " time by " + time + " seconds");
				}
				
				// Count the toggle as a tick
				task.setLastTick(task.isRunning() ? System.currentTimeMillis() : -1);
				
				// Set a future alarm for the task reaching its goal
				if(task.isRunning()) {
					AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
					int alarmID = task.getAlert() + 1;
					long alarmTime = System.currentTimeMillis() + (long) ((task.getGoal().toDouble() - task.getTime().toDouble()) * 3600 * 1000);
					
					// Create the alarm
					Intent alarmIntent = new Intent(TaskService.this, TaskTimerReceiver.class);
					alarmIntent.setAction(TaskTimerReceiver.ACTION_TASK_GOAL_REACHED);
					alarmIntent.putExtra("task", task.getId());
					alarmIntent.putExtra("alarm", alarmID);
					PendingIntent pendingIntent = PendingIntent.getBroadcast(TaskService.this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
					alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
					
					task.setAlert(alarmID);
					if(app.debug) Log.v(TAG, "Set alarm for task #" + msg.arg2 + " of group #" + msg.arg1 + " in " + (alarmTime - System.currentTimeMillis()) / 1000 + " seconds with ID " + alarmID);
				}
				break;
				
			case MSG_GET_ALL:
				// Get the Groups and Tasks
				response = Message.obtain(null, MSG_GET_ALL);
				contents.putParcelableArrayList("groups", groups);
				contents.putParcelableArrayList("tasks", tasks);
				response.setData(contents);
				sendMessageToActivity(response);
				break;
				
			case MSG_EXIT:
				// Exit the application
				TaskService.this.stopSelf();
				break;
				
			default:
				super.handleMessage(msg);
			}
		}
	}
	
	/**
	 * Sends a message to the activity
	 * @param msg The message to send
	 */
	public void sendMessageToActivity(Message msg) {
		// Set who to reply to
		msg.replyTo = messenger;
		
		// Send the message
		try {
			activityMessenger.send(msg);
			if(app.debug) Log.v(TAG, "Sent message: " + msg);
		} catch(android.os.RemoteException e) {
			if(app.debug) Log.w(TAG, "Failed to send message: " + msg + " (" + e.getLocalizedMessage() + " caused by " + e.getCause() + ")");
		}
		
		// Return the message to the global pool
		msg.recycle();
	}
	
	/**
	 * Sends a Parcelable object to the activity
	 * @param msgType The message type
	 * @param key The key for the object
	 * @param o The object
	 */
	public void sendObjectToActivity(int msgType, String key, Object o) {
		sendObjectToActivity(msgType, key, o, -1, -1);
	}
	
	/**
	 * Sends a Parcelable object and argument to the activity
	 * @param msgType The message type
	 * @param key The key for the object
	 * @param o The object
	 * @param arg The argument
	 */
	public void sendObjectToActivity(int msgType, String key, Object o, int arg) {
		sendObjectToActivity(msgType, key, o, arg, -1);
	}
	
	/**
	 * Sends a Parcelable object and arguments to the activity
	 * @param msgType The message type
	 * @param key The key for the object
	 * @param o The object
	 * @param arg1 Argument 1
	 * @param arg2 Argument 2
	 */
	public void sendObjectToActivity(int msgType, String key, Object o, int arg1, int arg2) {
		Message msg = Message.obtain(null, msgType);
		Bundle contents = new Bundle();
		contents.putParcelable(key, (Parcelable) o);
		msg.setData(contents);
		
		if(arg1 != -1) msg.arg1 = arg1;
		if(arg2 != -1) msg.arg2 = arg2;
		
		sendMessageToActivity(msg);
	}
	
	/**
	 * Sends an ArrayList of Parcelable objects to the activity
	 * @param msgType The message type
	 * @param key The key for the list
	 * @param list The list
	 */
	public void sendListToActivity(int msgType, String key, ArrayList<? extends Parcelable> list) {
		sendListToActivity(msgType, key, list, -1, -1);
	}
	
	/**
	 * Sends an ArrayList of Parcelable objects and an argument to the activity
	 * @param msgType The message type
	 * @param key The key for the list
	 * @param list The list
	 * @param arg The argument
	 */
	public void sendListToActivity(int msgType, String key, ArrayList<? extends Parcelable> list, int arg) {
		sendListToActivity(msgType, key, list, arg, -1);
	}
	
	/**
	 * Sends an ArrayList of Parcelable objects and arguments to the activity
	 * @param msgType The message type
	 * @param key The key for the list
	 * @param list The list
	 * @param arg1 Argument 1
	 * @param arg2 Argument 2
	 */
	public void sendListToActivity(int msgType, String key, ArrayList<? extends Parcelable> list, int arg1, int arg2) {
		Message msg = Message.obtain(null, msgType);
		Bundle contents = new Bundle();
		contents.putParcelableArrayList(key, list);
		msg.setData(contents);
		
		if(arg1 != -1) msg.arg1 = arg1;
		if(arg2 != -1) msg.arg2 = arg2;
		
		sendMessageToActivity(msg);
	}
}
