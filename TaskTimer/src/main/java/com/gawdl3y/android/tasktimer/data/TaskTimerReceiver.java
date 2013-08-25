package com.gawdl3y.android.tasktimer.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.activities.MainActivity;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.util.Log;
import com.gawdl3y.android.tasktimer.util.Utilities;

/**
 * The receiver for any Task Timer broadcast
 * @author Schuyler Cebulskie
 */
public class TaskTimerReceiver extends BroadcastReceiver {
    private static final String TAG = "Receiver";
    public static final String ACTION_START_APP = "start_app";
    public static final String ACTION_TASK_GOAL_REACHED = "task_goal_reached";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Received broadcast: " + intent);
        String action = intent.getAction();
        Bundle data = intent.getExtras();

        if(action.equals(ACTION_START_APP)) {
            // Start the app
            Intent startIntent = new Intent(context, MainActivity.class);
            startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(startIntent);
        } else if(action.equals(ACTION_TASK_GOAL_REACHED)) {
            // Find the task
            Task task = Utilities.getGroupedTaskByID(data.getInt("task"), TaskTimerApplication.GROUPS);

            // Make sure the task is still going
            if(task != null && task.isRunning() && !task.isIndefinite()) {
                // Finish the task
                task.setTime(task.getGoal());
                task.setComplete(true);

                // Stop the task if necessary
                if(task.getBooleanSetting(Task.Settings.STOP_AT_GOAL) || !task.getBooleanSetting(Task.Settings.OVERTIME)) {
                    task.setRunning(false);
                    task.setLastTick(-1);
                    TaskTimerApplication.RUNNING_TASK_COUNT--;
                }

                // Show notifications
                TaskTimerApplication.showTaskGoalReachedNotification(context, task);
                TaskTimerApplication.showOngoingNotification(context);
            }
        }
    }
}
