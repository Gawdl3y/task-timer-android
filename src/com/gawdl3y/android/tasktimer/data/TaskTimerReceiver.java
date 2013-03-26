package com.gawdl3y.android.tasktimer.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.context.MainActivity;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.util.Utilities;

/**
 * The receiver for any Task Timer broadcast
 * @author Schuyler Cebulskie
 */
public class TaskTimerReceiver extends BroadcastReceiver {
    public static final String ACTION_START_APP = "start_app";
    public static final String ACTION_TASK_GOAL_REACHED = "task_goal_reached";

    /* (non-Javadoc)
     * A broadcast has been received
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle data = intent.getExtras();

        if(action.equals(ACTION_START_APP)) {
            // Start the app
            Intent startIntent = new Intent(context, MainActivity.class);
            startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(startIntent);
        } else if(action.equals(ACTION_TASK_GOAL_REACHED)) {
            // Show the task goal reached notification if this is the right alarm
            Task task = Utilities.getGroupedTaskByID(data.getInt("task"), TaskTimerApplication.GROUPS);
            if(task != null && task.getAlert() == data.getInt("alert") && task.isRunning() && !task.isIndefinite())
                TaskTimerApplication.showTaskGoalReachedNotification(context, task);
        }
    }
}
