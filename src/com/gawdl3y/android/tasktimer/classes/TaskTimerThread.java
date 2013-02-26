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
	 */
	public TaskTimerThread(Task task, int group, TaskService service) {
		super(new TaskTimerRunnable(task, group, service));
		this.task = task;
	}
	
	/**
	 * The Runnable for the thread
	 * @author Schuyler Cebulskie
	 */
	public static class TaskTimerRunnable implements Runnable {
		private final Task task;
		private final int group;
		private final TaskService service;
		
		/**
		 * Fill constructor
		 * @param task The task that the timer is for
		 */
		public TaskTimerRunnable(Task task, int group, TaskService service) {
			super();
			this.task = task;
			this.group = group;
			this.service = service;
		}
		
		/* (non-Javadoc)
		 * The actual timer
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while(task.isRunning()) {
				task.getTime().increment();
				if(service.connected) service.sendObjectToActivity(TaskService.MSG_UPDATE_TASK, "task", task, group);
				System.out.println(task.getTime());
				
				try {
					Thread.sleep(1000);
				} catch(InterruptedException e) {
					
				}
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