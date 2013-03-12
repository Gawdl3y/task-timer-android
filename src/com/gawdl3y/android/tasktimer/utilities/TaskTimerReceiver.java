package com.gawdl3y.android.tasktimer.utilities;

import com.gawdl3y.android.tasktimer.context.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * The receiver for any Task Timer broadcast
 * @author Schuyler Cebulskie
 */
public class TaskTimerReceiver extends BroadcastReceiver {
	public static final String ACTION_START_APP = "start_app";

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
		}
	}
}
