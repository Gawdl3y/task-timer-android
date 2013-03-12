package com.gawdl3y.android.tasktimer.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.pojos.TaskTimerThread;

/**
 * The fragment for list items in a TaskListFragment
 * @author Schuyler Cebulskie
 */
public class TaskListItem extends LinearLayout implements TaskTimerThread.TickListener {
	private static final String TAG = "TaskListItem";
	
	// Data
	private Task task;
	private TaskTimerThread timer;
	
	// Views
	private TextView name, time, goal;
	private ProgressBar progress;
	private ImageView toggle;
	
	/**
	 * Constructor
	 * @param context The context for the list item
	 */
	public TaskListItem(Context context) {
		this(context, null, null);
	}
	
	/**
	 * Constructor
	 * @param context The context for the list item
	 * @param task The task to display
	 */
	public TaskListItem(Context context, Task task) {
		this(context, null, task);
	}
	
	/**
	 * Constructor
	 * @param context The context that the view is in
	 * @param attrs The AttributeSet
	 * @param task THe task to display
	 */
	public TaskListItem(Context context, AttributeSet attrs, Task task) {
		super(context, attrs);
		this.task = task;
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.task_list_item, this);
		if(name == null) onFinishInflate(); // onFinishInflate isn't being called by the LayoutInflater for some reason
	}
	
	/* (non-Javadoc)
	 * The view has finished inflating
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		// Set the views
		name = (TextView) findViewById(R.id.task_name);
		time = (TextView) findViewById(R.id.task_time);
		goal = (TextView) findViewById(R.id.task_goal);
		progress = (ProgressBar) findViewById(R.id.task_progress);
		toggle = (ImageView) findViewById(R.id.task_toggle);

		// Set tags so we can figure out what task is being acted upon later
		setTag(task.getPosition());
		toggle.setTag(task.getPosition());
		
		invalidate();
		buildTimer();
		Log.v(TAG, "Inflated");
	}
	
	/* (non-Javadoc)
	 * The view has been removed
	 * @see android.view.View#onDetachedFromWindow()
	 */
	@Override
	protected void onDetachedFromWindow() {
		if(timer != null) timer.interrupt();
		Log.v(TAG, "Detached");
	}
	
	/* (non-Javadoc)
	 * The instance is being saved
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = (Bundle) super.onSaveInstanceState();
		bundle.putParcelable("task", task);
		return bundle;
	}
	
	/* (non-Javadoc)
	 * The instance is being restored
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(state);
		task = ((Bundle) state).getParcelable("task");
		invalidate();
		buildTimer();
	}
	
	/* (non-Javadoc)
	 * Trigger a re-draw of the view
	 * @see android.view.View#invalidate()
	 */
	@Override
	public void invalidate() {
		super.invalidate();
		
		// Set the view values
		name.setText(task.getName());
		time.setText(task.getTime().toString());
		goal.setText(task.getGoal().toString());
		progress.setProgress(task.getProgress());
		progress.setIndeterminate(task.isIndefinite() && task.isRunning());
		
		// Change the toggle button to the proper image
		TypedArray ta = getContext().obtainStyledAttributes(new int[] { task.isRunning() ? R.attr.ic_pause : R.attr.ic_start });
		toggle.setImageDrawable(ta.getDrawable(0));
		ta.recycle();
	}
	
	/**
	 * Trigger a re-draw of the view
	 * @param task The task to use
	 */
	public void invalidate(Task task) {
		this.task = task;
		invalidate();
	}
	
	/**
	 * Creates and starts the timer if the task is running
	 * <p>Stops the timer if the task isn't running
	 */
	public void buildTimer() {
		if(task.isRunning()) {
			// Update the time from when the timer was last running
			if(task.getLastTick() != -1) {
				task.incrementTime((int) ((System.currentTimeMillis() - task.getLastTick()) / 1000));
				task.setLastTick(-1);
			}
			
			// Create and start the timer
			timer = new TaskTimerThread(task, 1, this);
			timer.start();
		} else {
			// Clear the last tick
			task.setLastTick(-1);
			
			// Stop the timer
			if(timer != null) timer.interrupt();
		}
	}
	
	/* (non-Javadoc)
	 * The timer has ticked
	 * @see com.gawdl3y.android.tasktimer.pojos.TaskTimerThread.TickListener#onTick()
	 */
	@Override
	public void onTick() {
		task.setLastTick(System.currentTimeMillis());
		postInvalidate();
	}
	
	/**
	 * Gets the task
	 * @return The task
	 */
	public Task getTask() {
		return task;
	}
	
	/**
	 * Sets the task
	 * @param task The task
	 */
	public void setTask(Task task) {
		this.task = task;
	}
	
	/**
	 * Gets the timer thread
	 * @return The timer thread
	 */
	public TaskTimerThread getTimer() {
		return timer;
	}
	
	/**
	 * Gets the name view
	 * @return The name view
	 */
	public TextView getNameView() {
		return name;
	}
	
	/**
	 * Gets the time view
	 * @return The time view
	 */
	public TextView getTimeView() {
		return time;
	}
	
	/**
	 * Gets the goal view
	 * @return The goal view
	 */
	public TextView getGoalView() {
		return goal;
	}
	
	/**
	 * Gets the progress bar
	 * @return The progress bar
	 */
	public ProgressBar getProgressBar() {
		return progress;
	}
	
	/**
	 * Gets the toggle button
	 * @return The toggle button
	 */
	public ImageView getToggleButton() {
		return toggle;
	}
}
