package com.gawdl3y.android.tasktimer.pojos;

import java.util.Comparator;


/**
 * Thread that increments the time of a task; for use in the UI
 * @author Schuyler Cebulskie
 */
public class TaskTimerThread extends Thread {
    public final Task task;

    /**
     * Fill constructor
     * @param task         The task that the timer is for
     * @param delay        The amount of time to delay (in seconds)
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
         * @param task         The task that the timer is for
         * @param delay        The amount of time to delay (in seconds)
         * @param tickListener The tick listener
         */
        public TaskTimerRunnable(Task task, int delay, TickListener tickListener) {
            this.task = task;
            this.delay = delay;
            this.tickListener = tickListener;
        }

        @Override
        public void run() {
            while(task.isRunning() && running) {
                try {
                    // Get the difference between the goal and time
                    int difference = (int) Math.ceil(task.getGoal().toDouble() * 3600 - task.getTime().toDouble() * 3600);

                    // Delay and increment time
                    if(difference >= delay || difference < 1 || !task.getBooleanSetting(Task.Settings.STOP_AT_GOAL)) {
                        Thread.sleep(delay * 1000);
                        if(task.isRunning()) task.incrementTime(delay, false);
                    } else {
                        Thread.sleep(difference * 1000);
                        if(task.isRunning()) task.incrementTime(difference, false);
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
     * Comparator for comparing tasks
     */
    public static final Comparator<TaskTimerThread> TASK_COMPARATOR = new Comparator<TaskTimerThread>() {
        @Override
        public int compare(TaskTimerThread lhs, TaskTimerThread rhs) {
            return Task.ID_COMPARATOR.compare(lhs.task, rhs.task);
        }
    };

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
}