package com.gawdl3y.android.tasktimer;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
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

import com.gawdl3y.android.tasktimer.classes.Group;
import com.gawdl3y.android.tasktimer.classes.Task;
import com.gawdl3y.android.tasktimer.classes.TaskTimerThread;
import com.gawdl3y.android.tasktimer.classes.TimeAmount;
import com.gawdl3y.android.tasktimer.classes.Utilities;

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
	public static boolean connected = false;
	public static Messenger activityMessenger;
	public static Messenger messenger;
	
	private TaskTimerApplication app;
	private Notification notification;
	private ArrayList<TaskTimerThread> timers = new ArrayList<TaskTimerThread>();
	private int groupID, taskID;
	
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
		tasks1.add(new Task("This is a task", "", new TimeAmount(1, 2, 3), new TimeAmount(), true, false, false, false, 22, 1, 42));
		tasks1.add(new Task("Really cool task", "", new TimeAmount(1, 59, 42), new TimeAmount(2, 0, 0), false, false, false, false, 4, 5, 42));
		tasks2.add(new Task("It's a task!", "", new TimeAmount(), new TimeAmount(2.54321), false, false, false, true, 0, 1, 43));
		
		Collections.sort(tasks1, Task.PositionComparator);
		Collections.sort(tasks2, Task.PositionComparator);
		Collections.sort(tasks3, Task.PositionComparator);
		
		app.tasks.addAll(tasks1);
		app.tasks.addAll(tasks2);
		app.tasks.addAll(tasks3);
		
		app.groups.add(new Group("It's a group!", tasks1, 0, 42));
		app.groups.add(new Group("Also a group", tasks2, 2, 43));
		app.groups.add(new Group("no way another group", tasks3, 1, 2));
		
		Collections.sort(app.groups, Group.PositionComparator);
		
		groupID = 43;
		taskID = 22;
		
		if(app.debug) Log.v(TAG, "Started");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		if(app.debug) Log.v(TAG, "Bound to activity");
		connected = true;
		return messenger.getBinder();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		if(app.debug) Log.v(TAG, "Unbound from activity");
		connected = false;
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		if(app.debug) Log.v(TAG, "Destroyed");
		super.onDestroy();
	}

	public class ServiceBinder extends Binder {
		TaskService getService() {
			return TaskService.this;
		}
	}
	
	private final class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			activityMessenger = msg.replyTo;
			if(app.debug) Log.d(TAG, "Received message: " + msg);
			
			Message response;
			Bundle contents = new Bundle();
			Task task;
			Bundle data = msg.getData();
			data.setClassLoader(getClassLoader());
			
			switch(msg.what) {
			case MSG_GET_TASKS:
				// Send the response message
				response = Message.obtain(null, MSG_GET_TASKS);
				contents.putParcelableArrayList("tasks", app.tasks);
				response.setData(contents);
				sendMessageToActivity(response);
				break;
			case MSG_ADD_TASK:
				// Add the task TODO SQL
				taskID++;
				task = (Task) data.getParcelable("task");
				task.setId(taskID);
				task.setGroup(app.groups.get(msg.arg1).getId());
				app.tasks.add(task);
				app.groups.get(msg.arg1).getTasks().add(task.getPosition(), task);
				reorder(app.groups.get(msg.arg1).getTasks());
				
				// Send the task back to the activity
				response = Message.obtain(null, MSG_ADD_TASK);
				contents.putParcelable("task", task);
				response.setData(contents);
				response.arg1 = msg.arg1;
				sendMessageToActivity(response);
				break;
			case MSG_DELETE_TASK:
				// TODO SQL
				app.groups.get(msg.arg1).getTasks().remove(msg.arg2);
				break;
			case MSG_UPDATE_TASK:
				// TODO SQL
				app.groups.get(msg.arg1).getTasks().set(msg.arg2, (Task) data.getParcelable("task"));
				break;
			case MSG_TOGGLE_TASK:
				task = app.groups.get(msg.arg1).getTasks().get(msg.arg2);
				task.toggle();
				
				// Create or destroy the timer thread
				if(task.isRunning()) {
					TaskTimerThread timer = new TaskTimerThread(task, msg.arg1);
					timer.start();
					timers.add(timer);
				} else {
					int position = Collections.binarySearch(timers, new TaskTimerThread(task, -1), TaskTimerThread.TaskComparator);
					TaskTimerThread timer = timers.get(position);
					timer.interrupt();
					timers.remove(position);
				}
				
				break;
				
			case MSG_GET_GROUPS:
				// Send the response message
				response = Message.obtain(null, MSG_GET_GROUPS);
				contents.putParcelableArrayList("groups", app.groups);
				response.setData(contents);
				sendMessageToActivity(response);
				break;
			case MSG_ADD_GROUP:
				// Add the group TODO SQL
				groupID++;
				Group group = (Group) data.getParcelable("group");
				group.setId(groupID);
				app.groups.add(msg.arg1, group);
				reorder(app.groups);
				
				// Send the groups back to the activity
				response = Message.obtain(null, MSG_GET_GROUPS);
				contents.putParcelableArrayList("groups", app.groups);
				response.setData(contents);
				response.arg1 = group.getPosition();
				sendMessageToActivity(response);
				break;
			case MSG_DELETE_GROUP:
				// TODO SQL
				break;
			case MSG_UPDATE_GROUP:
				// TODO SQL
				break;
				
			case MSG_GET_ALL:
				// Send the response message
				response = Message.obtain(null, MSG_GET_ALL);
				contents.putParcelableArrayList("groups", app.groups);
				contents.putParcelableArrayList("tasks", app.tasks);
				response.setData(contents);
				sendMessageToActivity(response);
				break;
				
			case MSG_EXIT:
				TaskService.this.stopSelf();
				break;
				
			default:
				super.handleMessage(msg);
			}
		}
	}
	
	public static void sendMessageToActivity(Message msg) {
		// Set who to reply to
		msg.replyTo = messenger;
		
		// Send the message
		try {
			activityMessenger.send(msg);
			if(TaskTimerApplication.DEBUG) Log.d(TAG, "Sent message: " + msg);
		} catch(android.os.RemoteException e) {
			if(TaskTimerApplication.DEBUG) Log.d(TAG, "Failed to send message: " + msg + " (" + e.getLocalizedMessage() + " caused by " + e.getCause() + ")");
		}
		
		// Return the message to the global pool
		msg.recycle();
	}
	
	public static void sendObjectToActivity(int msgType, String key, Object o) {
		Message msg = Message.obtain(null, msgType);
		Bundle contents = new Bundle();
		contents.putParcelable(key, (Parcelable) o);
		msg.setData(contents);
		sendMessageToActivity(msg);
	}
	
	public static void sendObjectToActivity(int msgType, String key, Object o, int arg1) {
		Message msg = Message.obtain(null, msgType);
		Bundle contents = new Bundle();
		contents.putParcelable(key, (Parcelable) o);
		msg.setData(contents);
		msg.arg1 = arg1;
		sendMessageToActivity(msg);
	}
	
	public void reorder(ArrayList<?> arr) {
		for(int i = 0; i < arr.size(); i++) {
			Object thing = arr.get(i);
			
			if(thing instanceof Task) {
				((Task) thing).setPosition(i);
			} else if(thing instanceof Group) {
				((Group) thing).setPosition(i);
			}  else {
				return;
			}
		}
	}
}
