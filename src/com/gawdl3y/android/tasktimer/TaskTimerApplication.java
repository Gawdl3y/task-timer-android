package com.gawdl3y.android.tasktimer;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.SparseArray;
import com.gawdl3y.android.tasktimer.context.MainActivity;
import com.gawdl3y.android.tasktimer.data.TaskTimerReceiver;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;
import com.gawdl3y.android.tasktimer.pojos.TimeAmount;
import com.gawdl3y.android.tasktimer.util.Log;
import com.gawdl3y.android.tasktimer.util.Utilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * The base application class for Task Timer
 * @author Schuyler Cebulskie
 */
public class TaskTimerApplication extends Application {
    private static final String TAG = "Application";
    private static final int ONGOING_NOTIFICATION_ID = Integer.MAX_VALUE;

    // Useful stuff
    public static final boolean DEBUG = true;
    public static String PACKAGE;
    public static Resources RESOURCES;
    public static SharedPreferences PREFERENCES;
    public static int THEME = R.style.Theme_Light_DarkActionBar;
    public static Gson GSON;

    // Data
    public static ArrayList<Group> GROUPS;
    //public static SparseArray<Group> GROUPS_MAP;
    public static int RUNNING_TASKS;

    /* (non-Javadoc)
     * The application is being created
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Set static properties
        PACKAGE = getPackageName();
        RESOURCES = getResources();

        // Set preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PREFERENCES = PreferenceManager.getDefaultSharedPreferences(this);

        // Set theme
        String themeStr = PREFERENCES.getString("pref_theme", "0");
        THEME = themeStr.equals("2") ? R.style.Theme_Light_DarkActionBar : (themeStr.equals("1") ? R.style.Theme_Light : R.style.Theme_Dark);

        // Create GSON object
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(TimeAmount.class, new TimeAmount.Serializer());
        gsonBuilder.registerTypeAdapter(TimeAmount.class, new TimeAmount.Deserializer());
        GSON = gsonBuilder.create();

        Log.v(TAG, "Created");
    }

    /**
     * Load Groups from a Cursor
     * @param cursor The Cursor
     */
    public static void loadGroups(Cursor cursor) {
        GROUPS = new ArrayList<Group>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            // Create the group object
            Group group = new Group(cursor.getInt(Group.Columns.ID_INDEX));
            group.setName(cursor.getString(Group.Columns.NAME_INDEX));
            group.setPosition(cursor.getInt(Group.Columns.POSITION_INDEX));

            // Add it
            GROUPS.add(group);
            //GROUPS_MAP.put(group.getId(), GROUPS.get(group.getPosition()));

            Log.d(TAG, "Group loaded: " + group.toString());
            cursor.moveToNext();
        }

        // Re-position groups
        Utilities.reposition(TaskTimerApplication.GROUPS);
    }

    /**
     * Load Tasks from a Cursor
     * @param cursor The Cursor
     */
    public static void loadTasks(Cursor cursor) {
        SparseArray<Group> groupMap = new SparseArray<Group>();

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            // Create the task object
            Task task = new Task(cursor.getInt(Task.Columns.ID_INDEX));
            task.setName(cursor.getString(Task.Columns.NAME_INDEX));
            task.setDescription(cursor.getString(Task.Columns.NAME_INDEX));
            task.setTime(GSON.fromJson(cursor.getString(Task.Columns.TIME_INDEX), TimeAmount.class));
            task.setGoal(GSON.fromJson(cursor.getString(Task.Columns.GOAL_INDEX), TimeAmount.class));
            task.setIndefinite(cursor.getInt(Task.Columns.INDEFINITE_INDEX) == 1);
            task.setComplete(cursor.getInt(Task.Columns.COMPLETE_INDEX) == 1);
            task.setPosition(cursor.getInt(Task.Columns.POSITION_INDEX));
            task.setGroup(cursor.getInt(Task.Columns.GROUP_INDEX));

            // Add it to its group
            if(groupMap.get(task.getGroup()) == null) groupMap.put(task.getGroup(), Utilities.getGroupByID(task.getGroup(), GROUPS));
            if(groupMap.get(task.getGroup()).getTasks() == null) groupMap.get(task.getGroup()).setTasks(new ArrayList<Task>());
            groupMap.get(task.getGroup()).getTasks().add(task);

            Log.d(TAG, "Task loaded: " + task.toString());
            cursor.moveToNext();
        }

        // Re-position tasks
        for(Group g : GROUPS) Utilities.reposition(g.getTasks());
    }

    /**
     * Creates a system alarm for a task reaching its goal
     * @param context The context we're coming from
     * @param task    The task to create the alarm for
     */
    public static void createTaskGoalReachedAlarm(Context context, Task task) {
        // Set a future alarm for the task reaching its goal
        if(task.isRunning() && !task.isComplete() && !task.isIndefinite()) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            long alarmTime = System.currentTimeMillis() + (long) ((task.getGoal().toDouble() - task.getTime().toDouble()) * 3600 * 1000);

            // Create the alarm
            Intent alarmIntent = new Intent(context, TaskTimerReceiver.class);
            alarmIntent.setAction(TaskTimerReceiver.ACTION_TASK_GOAL_REACHED);
            alarmIntent.putExtra("task", task.getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

            Log.v(TAG, "Set alarm for task " + task.getId() + " in " + (alarmTime - System.currentTimeMillis()) / 1000 + " seconds");
        }
    }

    /**
     * Shows the main ongoing notification
     * @param context The context we're coming from
     */
    public static void showOngoingNotification(Context context) {
        // Create the intent to launch the app
        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class).addNextIntent(intent);

        // Create the notification
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(RESOURCES.getString(R.string.app_name))
                .setContentText(String.format(RESOURCES.getQuantityString(R.plurals.plural_tasks_running, RUNNING_TASKS), RUNNING_TASKS))
                .setSmallIcon(R.drawable.ic_stat_icon)
                .setLargeIcon(Utilities.drawableToBitmap(RESOURCES.getDrawable(R.drawable.ic_launcher)))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(stackBuilder.getPendingIntent(Integer.MAX_VALUE, PendingIntent.FLAG_UPDATE_CURRENT))
                .setOngoing(true)
                .build();

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
    }

    /**
     * Hides the main ongoing notification
     * @param context The context we're coming from
     */
    public static void cancelOngoingNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ONGOING_NOTIFICATION_ID);
    }

    /**
     * Shows a task goal reached notification
     * @param context The context we're coming from
     * @param task    The task that has reached its goal
     */
    public static void showTaskGoalReachedNotification(Context context, Task task) {
        if(PREFERENCES.getBoolean("pref_goalNotifications", Boolean.parseBoolean(RESOURCES.getString(R.string.pref_goalNotification_default)))) {
            // Create the intent and back stack for the notification
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.putExtra("task", task.getId());
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class).addNextIntent(notificationIntent);

            // Create the notification
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(RESOURCES.getString(R.string.notif_task_completed))
                    .setContentText(String.format(RESOURCES.getString(R.string.notif_task_completed_long), task.getName()))
                    .setTicker(String.format(RESOURCES.getString(R.string.notif_task_completed_long), task.getName()))
                    .setSmallIcon(R.drawable.ic_stat_icon)
                    .setLargeIcon(Utilities.drawableToBitmap(RESOURCES.getDrawable(R.drawable.ic_launcher)))
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(stackBuilder.getPendingIntent(task.getId(), PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();

            // Show the notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(task.getId(), notification);

            // Play the notification tone
            Ringtone notificationSound = RingtoneManager.getRingtone(context, Uri.parse(PREFERENCES.getString("pref_notificationSound", "content://settings/system/notification_sound")));
            notificationSound.play();
        }
    }

    /**
     * Cancels a task goal reached notification
     * @param context The context we're coming from
     * @param task    The task of the notification to cancel
     */
    public static void cancelTaskGoalReachedNotification(Context context, Task task) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(task.getId());
    }
}
