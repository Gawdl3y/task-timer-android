package com.gawdl3y.android.tasktimer.layout;

import android.content.Context;
import android.content.res.TypedArray;
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
 * The view for task list items
 * @author Schuyler Cebulskie
 */
public class TaskListItem extends LinearLayout implements Checkable, TaskTimerThread.TickListener {
    private static final String TAG = "TaskListItem";

    // Data
    private Task mTask;
    private TaskTimerThread mTimer;
    private boolean mChecked = false;

    // Views
    private TextView mNameView, mDescriptionView, mTimeView, mGoalView;
    private ProgressBar mProgressView;
    private ImageView mToggleView;

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
        this.mTask = task;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.task_list_item, this);
        if(mNameView == null) onFinishInflate(); // onFinishInflate isn't ever being called by the LayoutInflater for some reason
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mNameView = (TextView) findViewById(R.id.task_name);
        mDescriptionView = (TextView) findViewById(R.id.task_description);
        mTimeView = (TextView) findViewById(R.id.task_time);
        mGoalView = (TextView) findViewById(R.id.task_goal);
        mProgressView = (ProgressBar) findViewById(R.id.task_progress);
        mToggleView = (ImageView) findViewById(R.id.task_toggle);

        mNameView.setTypeface(TaskTimerApplication.Typefaces.ROBOTO_LIGHT);

        invalidate();
        buildTimer();
        Log.v(TAG, "Inflated");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mTimer != null) mTimer.interrupt();
        Log.v(TAG, "Detached");
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = (Bundle) super.onSaveInstanceState();
        bundle.putParcelable("task", mTask);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        mTask = ((Bundle) state).getParcelable("task");
        invalidate();
        buildTimer();
    }

    @Override
    public void invalidate() {
        super.invalidate();

        // Set the view values
        mNameView.setText(mTask.getName());
        mDescriptionView.setText(mTask.getDescription());
        mTimeView.setText(mTask.getTime().toString());
        mGoalView.setText(mTask.isIndefinite() ? TaskTimerApplication.RESOURCES.getString(R.string.task_indefinite) : mTask.getGoal().toString());
        mProgressView.setProgress(mTask.getProgress());
        mProgressView.setIndeterminate(mTask.isIndefinite() && mTask.isRunning());

        // Change the toggle button to the proper image
        TypedArray ta = getContext().obtainStyledAttributes(new int[]{mTask.isRunning() ? R.attr.ic_pause : R.attr.ic_start});
        mToggleView.setImageDrawable(ta.getDrawable(0));
        ta.recycle();
    }

    /**
     * Creates and starts the timer if the task is running
     * <p>Stops the timer if the task isn't running
     */
    public void buildTimer() {
        if(mTask.isRunning()) {
            // Update the time from when the timer was last running
            if(mTask.getLastTick() > 0) {
                mTask.incrementTime((int) ((System.currentTimeMillis() - mTask.getLastTick()) / 1000));
                mTask.setLastTick(-1);
                invalidate();
            }

            // Create and start the timer
            if(mTimer != null) mTimer.interrupt();
            mTimer = new TaskTimerThread(mTask, 1, this);
            mTimer.start();
            Log.v(TAG, "Started timer");
        } else {
            // Stop the timer and clear the last tick
            if(mTimer != null) mTimer.interrupt();
            mTask.setLastTick(-1);
            Log.v(TAG, "Stopped timer");
        }
    }

    @Override
    public void setChecked(boolean checked) {
        this.mChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
    }

    @Override
    public void onTick() {
        mTask.setLastTick(System.currentTimeMillis());
        postInvalidate();
    }


    /**
     * @return The task
     */
    public Task getTask() {
        return mTask;
    }

    /**
     * @param task The task
     */
    public void setTask(Task task) {
        this.mTask = task;
    }

    /**
     * @return The timer thread
     */
    public TaskTimerThread getTimer() {
        return mTimer;
    }

    /**
     * @return The name view
     */
    public TextView getNameView() {
        return mNameView;
    }

    /**
     * @return The description view
     */
    public TextView getDescriptionView() {
        return mDescriptionView;
    }

    /**
     * @return The time view
     */
    public TextView getTimeView() {
        return mTimeView;
    }

    /**
     * @return The goal view
     */
    public TextView getGoalView() {
        return mGoalView;
    }

    /**
     * @return The progress bar
     */
    public ProgressBar getProgressBar() {
        return mProgressView;
    }

    /**
     * @return The toggle button
     */
    public ImageView getToggleButton() {
        return mToggleView;
    }
}
