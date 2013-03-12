package com.gawdl3y.android.tasktimer.utilities;

import java.util.ArrayList;

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
	public static void reposition(ArrayList<?> arr) {
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
	 * Sleep for a given amount of time
	 * @param time The amount of time to sleep (in milliseconds)
	 */
	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch(InterruptedException e) {
			
		}
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
