package com.gawdl3y.android.tasktimer.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.gawdl3y.android.tasktimer.TaskTimerApplication;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * General utility methods for Task Timer
 * @author Schuyler Cebulskie
 */
public final class Utilities {
    /**
     * Sets the positions of tasks or groups in a List to the position in the array
     * @param arr The List to reorder
     */
    public static final ArrayList<Object> reposition(List<?> arr) {
        ArrayList<Object> modifiedEntries = new ArrayList<Object>();

        for(int i = 0; i < arr.size(); i++) {
            Object thing = arr.get(i);

            if(thing instanceof Task) {
                Task task = (Task) thing;
                if(task.getPosition() != i) {
                    task.setPosition(i);
                    modifiedEntries.add(task);
                }
            } else if(thing instanceof Group) {
                Group group = (Group) thing;
                if(group.getPosition() != i) {
                    group.setPosition(i);
                    modifiedEntries.add(group);
                }
            }
        }

        return modifiedEntries;
    }

    /**
     * Finds and returns a task by an ID in a List of tasks
     * @param id    The ID to search for
     * @param tasks The tasks to search in
     * @return The task with the specified ID
     */
    public static final Task getTaskByID(int id, List<Task> tasks) {
        if(tasks != null) {
            for(Task t : tasks) {
                if(t.getId() == id) return t;
            }
        }

        return null;
    }

    /**
     * Finds and returns the index of a Task in a List by ID
     * @param id    The ID of the Task to search for
     * @param tasks The List of Tasks to search in
     * @return The index of the Task found, or -1 if no match
     */
    public static final int getTaskIndexByID(int id, List<Task> tasks) {
        if(tasks != null) {
            for(int t = 0; t < tasks.size(); t++) {
                if(tasks.get(t).getId() == id) return t;
            }
        }

        return -1;
    }

    /**
     * Finds and returns a group by an ID
     * @param id     The ID to search for
     * @param groups The groups to search in
     * @return The group with the specified ID
     */
    public static final Group getGroupByID(int id, List<Group> groups) {
        if(groups != null) {
            for(Group g : groups) {
                if(g.getId() == id) return g;
            }
        }

        return null;
    }

    /**
     * Finds and returns the index of a Group in a List by ID
     * @param id The ID of the Group to search for
     * @param groups The List of Groups to search in
     * @return The index of the Group found, or -1 if no match
     */
    public static final int getGroupIndexByID(int id, List<Group> groups) {
        if(groups != null) {
            for(int t = 0; t < groups.size(); t++) {
                if(groups.get(t).getId() == id) return t;
            }
        }

        return -1;
    }

    /**
     * Finds and returns a task by an ID in a List of groups
     * @param id     The ID to search for
     * @param groups The groups to search in
     * @return The task with the specified ID
     */
    public static final Task getGroupedTaskByID(int id, List<Group> groups) {
        if(groups != null) {
            for(Group g : groups) {
                if(g.getTasks() != null) {
                    for(Task t : g.getTasks()) {
                        if(t.getId() == id) return t;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Generates a Bitmap from a Drawable
     * @param drawable The Drawable to convert to a Bitmap
     * @return Bitmap
     */
    public static final Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable instanceof BitmapDrawable) return ((BitmapDrawable) drawable).getBitmap();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Opens the Play Store item for the app
     * @param context The context we're coming from
     */
    public static final void openPlayStore(Context context) {
        openPlayStore(context, TaskTimerApplication.PACKAGE);
    }

    /**
     * Opens the Play Store item for a specified app package
     * @param context    The context we're coming from
     * @param appPackage The package of the target app
     */
    public static final void openPlayStore(Context context, String appPackage) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackage)));
        } catch(android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackage)));
        }
    }
}
