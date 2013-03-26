package com.gawdl3y.android.tasktimer.data;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.context.MainActivity;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.util.Utilities;

import java.util.ArrayList;

/**
 * The receiver for any Task Timer broadcast
 * @author Schuyler Cebulskie
 */
public class TaskTimerReceiver extends BroadcastReceiver {
    public static final String ACTION_START_APP = "start_app";
    public static final String ACTION_TASK_GOAL_REACHED = "task_goal_reached";

    private static final int ONGOING_NOTIFICATION_ID = Integer.MAX_VALUE;

    private ArrayList<Group> groups;

    /* (non-Javadoc)
     * A broadcast has been received
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if(groups == null) groups = new ArrayList<Group>();

        String action = intent.getAction();
        if(action.equals(ACTION_START_APP)) {
            // Start the app
            Intent startIntent = new Intent(context, MainActivity.class);
            startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(startIntent);
        } else if(action.equals(ACTION_TASK_GOAL_REACHED)) {
            // TODO do stuff
        }
    }

    /**
     * Shows the main ongoing notification for the service
     * @param context The context we're coming from
     */
    public static void showOngoingNotification(Context context) {
        // Create the intent to launch the app
        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class).addNextIntent(intent);

        // Create the notification
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(String.format(context.getResources().getQuantityString(R.plurals.plural_tasks_running, 0), 0))
                .setSmallIcon(R.drawable.ic_stat_icon)
                .setLargeIcon(Utilities.drawableToBitmap(context.getResources().getDrawable(R.drawable.ic_launcher)))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(stackBuilder.getPendingIntent(Integer.MAX_VALUE, PendingIntent.FLAG_UPDATE_CURRENT))
                .build();

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
    }

    /**
     * Shows a task goal reached notification
     * @param context The context we're coming from
     * @param task    The task that has reached its goal
     */
    public static void showTaskGoalReachedNotification(Context context, Task task) {
        // Create the intent and back stack for the notification
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("task", task.getId());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class).addNextIntent(notificationIntent);

        // Create the notification
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.notif_task_completed))
                .setContentText(String.format(context.getString(R.string.notif_task_completed_long), task.getName()))
                .setTicker(String.format(context.getString(R.string.notif_task_completed_long), task.getName()))
                .setSmallIcon(R.drawable.ic_stat_icon)
                .setLargeIcon(Utilities.drawableToBitmap(context.getResources().getDrawable(R.drawable.ic_launcher)))
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(stackBuilder.getPendingIntent(task.getId(), PendingIntent.FLAG_UPDATE_CURRENT))
                .build();

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(task.getId(), notification);
    }
}
