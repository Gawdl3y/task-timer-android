package com.gawdl3y.android.tasktimer.classes;

import java.util.Comparator;

import com.gawdl3y.android.tasktimer.TaskService;

/**
 * Thread that increments the time of a task
 * @author Schuyler Cebulskie
 */
public class TaskTimerThread extends Thread {
	public final Task task;
	
	/**
	 * Fill constructor
	 * @param task The task that the timer is for
	 * @param group The position of the group that the task is in
	 * @param delay The amount of time to delay (in seconds)
	 * @param service The service that is running the thread
	 */
	public TaskTimerThread(Task task, int group, int delay, TaskService service) {
		super(new TaskTimerRunnable(task, group, delay, service));
		this.task = task;
	}
	
	/**
	 * The Runnable for the thread
	 * @author Schuyler Cebulskie
	 */
	public static class TaskTimerRunnable implements Runnable {
		private final Task task;
		private final int group, delay;
		private final TaskService service;
		
		/**
		 * Fill constructor
		 * @param task The task that the timer is for
		 * @param group The position of the group that the task is in
		 * @param delay The amount of time to delay (in seconds)
		 * @param service The service that is running the thread
		 */
		public TaskTimerRunnable(Task task, int group, int delay, TaskService service) {
			this.task = task;
			this.group = group;
			this.delay = delay;
			this.service = service;
		}
		
		/* (non-Javadoc)
		 * The actual timer
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while(task.isRunning() && !Thread.interrupted()) {
				// Get the difference between the goal and time
				int difference = (int) Math.ceil(task.getGoal().toDouble() * 3600 - task.getTime().toDouble() * 3600);
				
				if(difference >= delay || !task.getStopAtGoal()) {
					Utilities.sleep(delay * 1000);
					task.incrementTime(delay);
				} else {
					Utilities.sleep(difference * 1000);
					task.incrementTime(difference);
				}
				
				// Send the updated task to the activity if necessary
				if(service.isConnected()) service.sendObjectToActivity(TaskService.MSG_UPDATE_TASK, "task", task, group);
			}
		}
	}
	
	
	/**
	 * Comparator for comparing tasks
	 */
	public static final Comparator<TaskTimerThread> TaskComparator = new Comparator<TaskTimerThread>() {
		@Override
		public int compare(TaskTimerThread lhs, TaskTimerThread rhs) {
			return Task.IDComparator.compare(lhs.task,  rhs.task);
		}
	};
}