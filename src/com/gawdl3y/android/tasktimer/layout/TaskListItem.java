package com.gawdl3y.android.tasktimer.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.pojos.TaskTimerThread;
import com.gawdl3y.android.tasktimer.util.Log;

/**
 * The fragment for list items in a TaskListFragment
 * @author Schuyler Cebulskie
 */
public class TaskListItem extends LinearLayout implements Checkable, TaskTimerThread.TickListener {
    private static final String TAG = "TaskListItem";
    private static Typeface ROBOTO_LIGHT;

    // Data
    private Task task;
    private TaskTimerThread timer;
    private boolean checked = false;

    // Views
    private TextView name, description, time, goal;
    private ProgressBar progress;
    private ImageView toggle;

    /**
     * The interface for listening to task button interactions
     * @author Schuyler Cebulskie
     */
    public static interface TaskButtonListener {
        /**
         * A task button was clicked
         * @param view The view of the button
         */
        public void onTaskButtonClick(View view);
    }

    /**
     * Constructor
     * @param context The context that the view is in
     * @param attrs   The AttributeSet
     * @param task    The task to display
     */
    public TaskListItem(Context context, AttributeSet attrs, Task task) {
        super(context, attrs);

        // Define the typefaces
        if(ROBOTO_LIGHT == null) {
            ROBOTO_LIGHT = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        }

        this.task = task;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.task_list_item, this);
        if(name == null) onFinishInflate(); // onFinishInflate isn't ever being called by the LayoutInflater for some reason
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        name = (TextView) findViewById(R.id.task_name);
        description = (TextView) findViewById(R.id.task_description);
        time = (TextView) findViewById(R.id.task_time);
        goal = (TextView) findViewById(R.id.task_goal);
        progress = (ProgressBar) findViewById(R.id.task_progress);
        toggle = (ImageView) findViewById(R.id.task_toggle);

        name.setTypeface(ROBOTO_LIGHT);

        invalidate();
        buildTimer();
        Log.v(TAG, "Inflated");
    }

    @Override
    protected void onDetachedFromWindow() {
        if(timer != null) timer.interrupt();
        Log.v(TAG, "Detached");
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = (Bundle) super.onSaveInstanceState();
        bundle.putParcelable("task", task);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        task = ((Bundle) state).getParcelable("task");
        invalidate();
        buildTimer();
    }

    @Override
    public void invalidate() {
        super.invalidate();

        // Set the view values
        name.setText(task.getName());
        description.setText(task.getDescription());
        time.setText(task.getTime().toString());
        goal.setText(task.isIndefinite() ? TaskTimerApplication.RESOURCES.getString(R.string.task_indefinite) : task.getGoal().toString());
        progress.setProgress(task.getProgress());
        progress.setIndeterminate(task.isIndefinite() && task.isRunning());

        // Change the toggle button to the proper image
        TypedArray ta = getContext().obtainStyledAttributes(new int[]{task.isRunning() ? R.attr.ic_pause : R.attr.ic_start});
        toggle.setImageDrawable(ta.getDrawable(0));
        ta.recycle();
    }

    /**
     * Creates and starts the timer if the task is running
     * <p>Stops the timer if the task isn't running
     */
    public void buildTimer() {
        if(task.isRunning()) {
            // Update the time from when the timer was last running
            if(task.getLastTick() > 0) {
                task.incrementTime((int) ((System.currentTimeMillis() - task.getLastTick()) / 1000));
                task.setLastTick(-1);
                invalidate();
            }

            // Create and start the timer
            if(timer != null) timer.interrupt();
            timer = new TaskTimerThread(task, 1, this);
            timer.start();
            Log.v(TAG, "Started timer");
        } else {
            // Stop the timer and clear the last tick
            if(timer != null) timer.interrupt();
            task.setLastTick(-1);
            Log.v(TAG, "Stopped timer");
        }
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
    }

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
