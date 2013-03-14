package com.gawdl3y.android.tasktimer.utilities;

import java.util.ArrayList;
import java.util.Collections;

import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.pojos.Task;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * General utility methods for Task Timer
 * @author Schuyler Cebulskie
 */
public final class Utilities {
	/**
	 * Sets the positions of tasks or groups in an ArrayList to the position in the array
	 * @param arr The ArrayList to reorder
	 */
	public static final void reposition(ArrayList<?> arr) {
		for(int i = 0; i < arr.size(); i++) {
			Object thing = arr.get(i);
			
			if(thing instanceof Task) {
				((Task) thing).setPosition(i);
			} else if(thing instanceof Group) {
				((Group) thing).setPosition(i);
			}
		}
	}
	
	/**
	 * Finds and returns a task by an ID in an ArrayList of tasks
	 * @param id The ID to search for
	 * @param groups The tasks to search in
	 * @return The task with the specified ID
	 */
	public static final Task getTaskByID(int id, ArrayList<Task> tasks) {
		for(Task t : tasks) {
			if(t.getId() == id) return t;
		}
		
		return null;
	}
	
	/**
	 * Finds and returns a task by an ID in an ArrayList of groups
	 * @param id The ID to search for
	 * @param groups The groups to search in
	 * @return The task with the specified ID
	 */
	public static final Task getGroupedTaskByID(int id, ArrayList<Group> groups) {
		for(Group g : groups) {
			for(Task t : g.getTasks()) {
				if(t.getId() == id) return t;
			}
		}
		
		return null;
	}
	
	/**
	 * Finds and returns a group by an ID
	 * @param id The ID to search for
	 * @param groups The groups to search in
	 * @return The group with the specified ID
	 */
	public static final Group getGroupByID(int id, ArrayList<Group> groups) {
		for(Group g : groups) {
			if(g.getId() == id) return g;
		}
		
		return null;
	}
	
	/**
	 * Generates a Bitmap from a Drawable
	 * @param drawable The Drawable to convert to a Bitmap
	 * @return Bitmap
	 */
	public static final Bitmap drawableToBitmap(Drawable drawable) {
	    if(drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable) drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	
}
