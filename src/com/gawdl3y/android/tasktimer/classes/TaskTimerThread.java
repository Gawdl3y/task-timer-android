package com.gawdl3y.android.tasktimer.classes;

import java.util.Comparator;

import android.os.Bundle;
import android.os.Message;

import com.gawdl3y.android.tasktimer.TaskService;

/**
 * @author Schuyler
 * Thread that increments the time of a task
 */
public class TaskTimerThread extends Thread {
	public final Task task;
	
	/**
	 * Fill constructor
	 * @param task The task that the timer is for
	 */
	public TaskTimerThread(Task task, int group) {
		super(new TaskTimerRunnable(task, group));
		this.task = task;
	}
	
	/**
	 * @author Schuyler
	 * The Runnable for the thread
	 */
	public static class TaskTimerRunnable implements Runnable {
		private final Task task;
		private final int group;
		
		/**
		 * Fill constructor
		 * @param task The task that the timer is for
		 */
		public TaskTimerRunnable(Task task, int group) {
			super();
			this.task = task;
			this.group = group;
		}
		
		/* (non-Javadoc)
		 * The actual timer
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while(task.isRunning()) {
				task.getTime().increment();
				if(TaskService.connected) TaskService.sendObjectToActivity(TaskService.MSG_UPDATE_TASK, "task", task, group);
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