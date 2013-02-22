package com.gawdl3y.android.tasktimer.classes;

import java.util.Comparator;

public class TaskTimerThread extends Thread {
	public final Task task;
	
	public TaskTimerThread(Task task) {
		super(new TaskTimerRunnable(task));
		this.task = task;
	}
	
	public static class TaskTimerRunnable implements Runnable {
		private final Task task;
		
		public TaskTimerRunnable(Task task) {
			super();
			this.task = task;
		}
		
		@Override
		public void run() {
			while(task.isRunning()) {
				task.getTime().increment();
				System.out.println(task.getTime());
				
				try {
					Thread.sleep(1000);
				} catch(InterruptedException e) {
					task.setRunning(false);
				}
			}
		}
	}
	
	public static final Comparator<TaskTimerThread> TaskComparator = new Comparator<TaskTimerThread>() {
		@Override
		public int compare(TaskTimerThread lhs, TaskTimerThread rhs) {
			return Task.IDComparator.compare(lhs.task,  rhs.task);
		}
	};
}