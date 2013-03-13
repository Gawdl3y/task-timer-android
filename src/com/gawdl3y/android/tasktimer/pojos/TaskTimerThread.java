package com.gawdl3y.android.tasktimer.pojos;

import java.util.Comparator;


/**
 * Thread that increments the time of a task
 * <p>For use in the UI
 * @author Schuyler Cebulskie
 */
public class TaskTimerThread extends Thread {
	public final Task task;
	
	/**
	 * Fill constructor
	 * @param task The task that the timer is for
	 * @param delay The amount of time to delay (in seconds)
	 * @param tickListener The tick listener
	 */
	public TaskTimerThread(Task task, int delay, TickListener tickListener) {
		super(new TaskTimerRunnable(task, delay, tickListener));
		this.task = task;
	}
	
	/**
	 * The Runnable for the thread
	 * @author Schuyler Cebulskie
	 */
	public static class TaskTimerRunnable implements Runnable {
		private final Task task;
		private final int delay;
		private final TickListener tickListener;
		private boolean running = true;
		
		/**
		 * Fill constructor
		 * @param task The task that the timer is for
		 * @param delay The amount of time to delay (in seconds)
		 * @param tickListener The tick listener
		 */
		public TaskTimerRunnable(Task task, int delay, TickListener tickListener) {
			this.task = task;
			this.delay = delay;
			this.tickListener = tickListener;
		}
		
		/* (non-Javadoc)
		 * The actual timer
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while(task.isRunning() && running) {
				try {
					// Get the difference between the goal and time
					int difference = (int) Math.ceil(task.getGoal().toDouble() * 3600 - task.getTime().toDouble() * 3600);
					
					// Delay and increment time
					if(difference >= delay || !task.getBooleanSetting("stop")) {
						Thread.sleep(delay * 1000);
						task.incrementTime(delay);
					} else {
						Thread.sleep(difference * 1000);
						task.incrementTime(difference);
					}
					
					// Call the listener
					if(tickListener != null) tickListener.onTick();
				} catch(InterruptedException e) {
					running = false;
				}
			}
		}
	}
	
	
	/**
	 * The interface for listening to ticks of the timer
	 * @author Schuyler Cebulskie
	 */
	public static interface TickListener {
		/**
		 * Called when the timer ticks
		 */
		void onTick();
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