package com.gawdl3y.android.tasktimer.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.gawdl3y.android.tasktimer.context.MainActivity;
import com.gawdl3y.android.tasktimer.context.TaskService;

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

        if(action.equals(ACTION_START_APP)) {
            // Start the app
            Intent startIntent = new Intent(context, MainActivity.class);
            startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(startIntent);
        } else if(action.equals(ACTION_TASK_GOAL_REACHED)) {
            // Tell the service
            Intent serviceIntent = new Intent(context, TaskService.class);
            serviceIntent.setAction(ACTION_TASK_GOAL_REACHED);
            serviceIntent.putExtras(intent.getExtras());
            context.startService(serviceIntent);
        }
    }
}
