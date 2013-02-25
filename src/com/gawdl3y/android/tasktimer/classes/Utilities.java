package com.gawdl3y.android.tasktimer.classes;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * General utilities for Task Timer
 * @author Schuyler Cebulskie
 */
public final class Utilities {
	
	/**
	 * Generates a Bitmap from a Drawable
	 * @param drawable The Drawable to convert to a Bitmap
	 * @return Bitmap
	 */
	public static final Bitmap drawableToBitmap(Drawable drawable) {
	    if(drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	
}
